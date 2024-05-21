package com.example.fintrack

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.databinding.ItemCategoriaBinding

class CategoriaAdapter(
    private val context: Context,
    private val listaCategoria: MutableList<CategoriaUi>,
    private val onCategoriaSelected: (CategoriaUi) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val binding = ItemCategoriaBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoriaViewHolder(binding)
    }

    override fun getItemCount() = listaCategoria.size

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = listaCategoria[position]
        holder.bind(categoria)
        holder.itemView.isSelected = selectedPosition == position

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onCategoriaSelected(categoria)
        }
    }

    inner class CategoriaViewHolder(private val binding: ItemCategoriaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoria: CategoriaUi) {
            binding.categoria = categoria
            binding.executePendingBindings()
        }
    }
}
