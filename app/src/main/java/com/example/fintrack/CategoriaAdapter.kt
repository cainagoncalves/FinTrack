package com.example.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CategoriaAdapter :
    ListAdapter<CategoriaUi, CategoriaAdapter.CategoriaViewHolder>(CategoriaDiffCallback()) {

    private var onClick: (CategoriaUi) -> Unit = {
        throw IllegalArgumentException("onClick not initialized")
    }

    private var onLongClick: (CategoriaUi) -> Unit = {
        throw IllegalArgumentException("onLongClick not initialized")
    }

    fun setOnItemClickListener(onClick: (CategoriaUi) -> Unit) {
        this.onClick = onClick
    }

    fun setOnLongClickListener(onLongClick: (CategoriaUi) -> Unit) {
        this.onLongClick = onLongClick
    }

    // Cria um view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    // Atrela o dado com a UI views
    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick, onLongClick)
    }

    // View que segura os dados
    inner class CategoriaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val btnCategoria = view.findViewById<ImageView>(R.id.icon_categoria)

        fun bind(
            categoria: CategoriaUi,
            onClick: (CategoriaUi) -> Unit,
            onLongClickListener: (CategoriaUi) -> Unit
        ) {
            btnCategoria.setImageResource(categoria.iconeCategoria)
            btnCategoria.isSelected = categoria.isSelected

            // Atualiza a aparência baseada na seleção
            if (categoria.isSelected) {
                btnCategoria.setBackgroundResource(R.drawable.selected_button_background)
            } else {
                btnCategoria.setBackgroundResource(R.drawable.filter_chips_background)
            }

            // Adiciona um listener ao botão para atualizar a seleção imediatamente
            btnCategoria.setOnClickListener {
                onClick.invoke(categoria)
            }

            view.setOnLongClickListener {
                onLongClickListener.invoke(categoria)
                true
            }
        }
    }

    // Compara a diferença quando a lista é atualizada
    companion object {
        class CategoriaDiffCallback : DiffUtil.ItemCallback<CategoriaUi>() {
            override fun areItemsTheSame(oldItem: CategoriaUi, newItem: CategoriaUi): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CategoriaUi, newItem: CategoriaUi): Boolean {
                return oldItem.iconeCategoria == newItem.iconeCategoria
            }
        }
    }
}
