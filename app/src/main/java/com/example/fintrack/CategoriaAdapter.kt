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

    private var onLongClick: (CategoriaUi) -> Unit = {
        throw IllegalArgumentException("onLongClick not initialized")
    }

    private var selectedPosition = RecyclerView.NO_POSITION

    fun setOnItemClickListener(onClick: (CategoriaUi) -> Unit) {
        this.onClick = onClick
    }

    fun setOnLongClickListener(onLongClick: (CategoriaUi) -> Unit) { // Correção aqui
        this.onLongClick = onLongClick
    }

    fun setSelectedCategory(category: CategoriaUi?) {
        selectedPosition = if (category == null) RecyclerView.NO_POSITION else currentList.indexOf(category)
        notifyDataSetChanged()
    }

    // Cria um view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    // Atrela o dado com a UI views
    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick, position, onLongClick)
    }

    // View que segura os dados
    inner class CategoriaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val btnCategoria = view.findViewById<ImageView>(R.id.icon_categoria)

        fun bind(
            categoria: CategoriaUi,
            onClick: (CategoriaUi) -> Unit,
            position: Int,
            onLongClickListener: (CategoriaUi) -> Unit
        ) {
            btnCategoria.setImageResource(categoria.iconeCategoria)
            btnCategoria.isSelected = position == selectedPosition

            // Adiciona um listener ao botão para atualizar a seleção imediatamente
            btnCategoria.setOnClickListener {
                // Verifica se a mesma categoria foi clicada novamente
                val isSameCategoryClicked = position == selectedPosition

                // Atualiza a seleção apenas se for uma categoria diferente
                if (!isSameCategoryClicked) {
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }

                // Invoca o listener onClick passando o item clicado
                onClick.invoke(categoria)
            }

            view.setOnLongClickListener {
                onLongClickListener.invoke(categoria)
                true
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
