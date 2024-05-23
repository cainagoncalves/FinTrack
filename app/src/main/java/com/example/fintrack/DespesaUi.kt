package com.example.fintrack

data class DespesaUi(
    val id: Long,
    val nome: String,
    val valor: String,
    var categoria: Int,
    var cor: Int
)