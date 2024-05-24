package com.example.fintrack

data class CategoriaUi(
    val id: Long,
    val iconeCategoria: Int,
    var isSelected: Boolean,
    val cor: Int = R.color.white
)
