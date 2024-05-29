package com.example.fintrack

// Data class que representa uma Despesa na interface do usuário (UI)
data class DespesaUi(
    val id: Long,          // ID único da despesa
    val nome: String,      // Nome da despesa
    val valor: String,     // Valor da despesa, armazenado como String
    var categoria: Int,    // Categoria da despesa, representada por um inteiro (pode ser um ID ou um recurso)
    var cor: Int           // Cor associada à despesa, representada por um inteiro (pode ser um recurso de cor)
)
