package com.example.fintrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DespesaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val valor: String,
    var categoria: Int,
    var cor: Int
)
