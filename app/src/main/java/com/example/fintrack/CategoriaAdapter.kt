package com.example.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

// Faz a adaptação entre o data class e o item_list layout
class CategoriaAdapter :
    ListAdapter<CategoriaUi, CategoriaAdapter.CategoriaViewHolder>(CategoriaAdapter) {

    private var onClick: (CategoriaUi) -> Unit = {
        throw IllegalArgumentException("onClick not initialized")
    }

    private var selectedPosition = RecyclerView.NO_POSITION

    fun setOnItemClickListener(onClick: (CategoriaUi) -> Unit) {
        this.onClick = onClick
    }

    // Cria um view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    // Atrela o dado com a UI views
    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick, position)
    }

    // View que segura os dados
    inner class CategoriaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val btnCategoria = view.findViewById<ImageView>(R.id.icon_categoria)

        fun bind(categoria: CategoriaUi, onClick: (CategoriaUi) -> Unit, position: Int) {
            btnCategoria.setImageResource(categoria.iconeCategoria)
            btnCategoria.isSelected = categoria.isSelected

            // Change appearance if the item is selected
            view.isSelected = (position == selectedPosition)

            view.setOnClickListener {
                // Update selected position and notify changes
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onClick.invoke(categoria)
            }
        }
    }

    // Compara a diferença quando a lista é atualizada
    companion object : DiffUtil.ItemCallback<CategoriaUi>() {
        override fun areItemsTheSame(oldItem: CategoriaUi, newItem: CategoriaUi): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoriaUi, newItem: CategoriaUi): Boolean {
            return oldItem.iconeCategoria == newItem.iconeCategoria
        }
    }

}