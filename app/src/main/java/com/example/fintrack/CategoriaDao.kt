package com.example.fintrack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Data Access Object (DAO) para operações relacionadas à entidade CategoriaEntity no banco de dados
@Dao
interface CategoriaDao {

    // Retorna todas as categorias no banco de dados
    @Query("SELECT * FROM categoriaentity")
    fun getAll(): List<CategoriaEntity>

    // Insere uma lista de categorias no banco de dados. Se houver conflito, a categoria existente será substituída.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categoriaEntity: CategoriaEntity)

    // Insere uma categoria no banco de dados. Se houver conflito, a categoria existente será substituída.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoriaEntity: CategoriaEntity)

    // Exclui uma categoria do banco de dados
    @Delete
    fun delete(categoriaEntity: CategoriaEntity)
}
