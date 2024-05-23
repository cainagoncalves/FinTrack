package com.example.fintrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CategoriaDao {

    @Query("Select * From categoriaentity")
    fun getAll(): List<CategoriaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categoriaEntity: List<CategoriaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoriaEntity: CategoriaEntity)
}