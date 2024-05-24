package com.example.fintrack

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["key"],
            childColumns = ["categoria"]
        )
    ]
)
data class DespesaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val valor: String,
    var categoria: Int,
    var cor: Int
)
