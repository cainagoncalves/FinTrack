package com.example.fintrack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// Define a interface DAO (Data Access Object) para operações no banco de dados Room relacionadas a DespesaEntity
@Dao
interface DespesaDao {

    // Consulta para obter todas as despesas
    @Query("Select * From despesaentity")
    fun getAll(): List<DespesaEntity>

    // Inserção de uma lista de despesas, substituindo em caso de conflito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(despesaEntity: DespesaEntity)

    // Inserção de uma única despesa, substituindo em caso de conflito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(despesaEntity: DespesaEntity)

    // Atualização de uma despesa existente
    @Update
    fun update(despesaEntity: DespesaEntity)

    // Exclusão de uma despesa
    @Delete
    fun delete(despesaEntity: DespesaEntity)

    // Consulta para obter todas as despesas por ícone de categoria
    @Query("Select * From despesaentity where categoria is :categoriaIcon")
    fun getAllByCategoriaIcon(categoriaIcon: Int): List<DespesaEntity>

    // Exclusão de uma lista de despesas
    @Delete
    fun deleteAll(despesaEntity: List<DespesaEntity>)
}
