package com.example.fintrack

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DespesaAdapter :
    ListAdapter<DespesaUi, DespesaAdapter.DespesaViewHolder>(DespesaAdapter) {

    private lateinit var callback: (DespesaUi) -> Unit

    fun setOnClickListener(onClick: (DespesaUi) -> Unit) {
        callback = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_despesa, parent, false)
        return DespesaViewHolder(view)
    }

    //override fun getItemCount() = listaDespesa.size

    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        val categoria = getItem(position)
        holder.bind(categoria, callback)
    }

    class DespesaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val iconCategoria = view.findViewById<ImageView>(R.id.ic_categoria)
        private val tvDespesa = view.findViewById<TextView>(R.id.tv_despesa)
        private val tvValorDespesa = view.findViewById<TextView>(R.id.tv_valor)
        private val setColor = view.findViewById<View>(R.id.set_color_despesa)

        // Dita o comportamento da view
        @SuppressLint("SetTextI18n")
        fun bind(despesa: DespesaUi, callback: (DespesaUi) -> Unit) {
            iconCategoria.setImageResource(despesa.categoria)
//            iconCategoria.setColorFilter(despesa.cor)
            tvDespesa.text = despesa.nome
            tvValorDespesa.text = despesa.valor
            tvValorDespesa.text = "- R$ ${despesa.valor}"

            val background = setColor.background as GradientDrawable
            background.setColor(despesa.cor)


            view.setOnClickListener {
                callback.invoke(despesa)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<DespesaUi>() {
        override fun areItemsTheSame(oldItem: DespesaUi, newItem: DespesaUi): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DespesaUi, newItem: DespesaUi): Boolean {
            return oldItem.nome == newItem.nome
        }

    }
}