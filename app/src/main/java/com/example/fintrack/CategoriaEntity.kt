package com.example.fintrack

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index(value = ["key"], unique = true)])
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("key")
    val iconeCategoria: Int,
    @ColumnInfo("is_selected")
    val isSelected: Boolean,
    val cor: Int
)
