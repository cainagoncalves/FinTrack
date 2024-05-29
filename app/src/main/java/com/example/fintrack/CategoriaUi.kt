package com.example.fintrack

// Classe de modelo para representar uma categoria na UI
data class CategoriaUi(
    val id: Long,               // Identificador único da categoria
    val iconeCategoria: Int,    // ID do recurso do ícone associado à categoria
    var isSelected: Boolean,    // Indica se a categoria está selecionada
    val cor: Int = R.color.white // Cor padrão da categoria (branco, se não especificado)
)
