package com.example.fintrack

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Entidade para representar uma categoria no banco de dados
@Entity(indices = [Index(value = ["key"], unique = true)])
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                 // Identificador único da categoria (gerado automaticamente)
    @ColumnInfo("key")
    val iconeCategoria: Int,          // ID do recurso do ícone associado à categoria
    @ColumnInfo("is_selected")
    val isSelected: Boolean,          // Indica se a categoria está selecionada
    val cor: Int                      // Cor associada à categoria
)
