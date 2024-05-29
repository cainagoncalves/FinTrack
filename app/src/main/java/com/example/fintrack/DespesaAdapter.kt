package com.example.fintrack

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

// Adapter para exibir uma lista de despesas em um RecyclerView
class DespesaAdapter :
    ListAdapter<DespesaUi, DespesaAdapter.DespesaViewHolder>(DespesaAdapter) {

    // Callback para tratar cliques em itens da lista
    private lateinit var callback: (DespesaUi) -> Unit

    // Define o listener de clique
    fun setOnClickListener(onClick: (DespesaUi) -> Unit) {
        callback = onClick
    }

    // Cria o ViewHolder para a visualização do item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_despesa, parent, false)
        return DespesaViewHolder(view)
    }

    // Associa os dados do item ao ViewHolder
    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        val categoria = getItem(position)
        holder.bind(categoria, callback)
    }

    // ViewHolder para a visualização do item
    class DespesaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val iconCategoria = view.findViewById<ImageView>(R.id.ic_categoria)
        private val tvDespesa = view.findViewById<TextView>(R.id.tv_despesa)
        private val tvValorDespesa = view.findViewById<TextView>(R.id.tv_valor)
        private val setColor = view.findViewById<View>(R.id.set_color_despesa)

        // Associa os dados do objeto DespesaUi aos componentes da view
        @SuppressLint("SetTextI18n")
        fun bind(despesa: DespesaUi, callback: (DespesaUi) -> Unit) {
            iconCategoria.setImageResource(despesa.categoria)
            tvDespesa.text = despesa.nome
            tvValorDespesa.text = "- R$ ${despesa.valor}"

            val background = setColor.background as GradientDrawable
            background.setColor(despesa.cor)

            // Configura o clique na view para chamar o callback
            view.setOnClickListener {
                callback.invoke(despesa)
            }
        }
    }

    // Define como o Adapter identifica e compara itens
    companion object : DiffUtil.ItemCallback<DespesaUi>() {
        // Verifica se dois itens são os mesmos
        override fun areItemsTheSame(oldItem: DespesaUi, newItem: DespesaUi): Boolean {
            return oldItem == newItem
        }

        // Verifica se o conteúdo de dois itens é o mesmo
        override fun areContentsTheSame(oldItem: DespesaUi, newItem: DespesaUi): Boolean {
            return oldItem.nome == newItem.nome
        }
    }
}
