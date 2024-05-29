package com.example.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

// Adapter para exibir itens de categoria em um RecyclerView
class CategoriaAdapter :
    ListAdapter<CategoriaUi, CategoriaAdapter.CategoriaViewHolder>(CategoriaDiffCallback()) {

    // Inicializa os listeners de clique e clique longo
    private var onClick: (CategoriaUi) -> Unit = {
        throw IllegalArgumentException("onClick not initialized")
    }

    private var onLongClick: (CategoriaUi) -> Unit = {
        throw IllegalArgumentException("onLongClick not initialized")
    }

    // Define o listener de clique
    fun setOnItemClickListener(onClick: (CategoriaUi) -> Unit) {
        this.onClick = onClick
    }

    // Define o listener de clique longo
    fun setOnLongClickListener(onLongClick: (CategoriaUi) -> Unit) {
        this.onLongClick = onLongClick
    }

    // Cria um ViewHolder para exibir itens de categoria
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    // Vincula os dados de categoria ao ViewHolder
    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick, onLongClick)
    }

    // ViewHolder que mantém a exibição de cada item de categoria
    inner class CategoriaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val btnCategoria = view.findViewById<ImageView>(R.id.icon_categoria)

        // Vincula os dados de categoria ao ViewHolder
        fun bind(
            categoria: CategoriaUi,
            onClick: (CategoriaUi) -> Unit,
            onLongClickListener: (CategoriaUi) -> Unit
        ) {
            // Define o ícone da categoria
            btnCategoria.setImageResource(categoria.iconeCategoria)
            // Define se a categoria está selecionada ou não
            btnCategoria.isSelected = categoria.isSelected

            // Atualiza a aparência baseada na seleção
            if (categoria.isSelected) {
                btnCategoria.setBackgroundResource(R.drawable.selected_button_background)
            } else {
                btnCategoria.setBackgroundResource(R.drawable.filter_chips_background)
            }

            // Adiciona um listener de clique ao botão para atualizar a seleção imediatamente
            btnCategoria.setOnClickListener {
                onClick.invoke(categoria)
            }

            // Adiciona um listener de clique longo ao item para executar a ação correspondente
            view.setOnLongClickListener {
                onLongClickListener.invoke(categoria)
                true
            }
        }
    }

    // Classe interna para comparar a diferença entre os itens de categoria
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
