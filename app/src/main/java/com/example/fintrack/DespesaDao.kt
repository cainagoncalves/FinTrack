package com.example.fintrack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface DespesaDao {

    @Query("Select * From despesaentity")
    fun getAll(): List<DespesaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(despesaEntity: List<DespesaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(despesaEntity: DespesaEntity)

    @Update
    fun update(despesaEntity: DespesaEntity)

    @Delete
    fun delete (despesaEntity: DespesaEntity)

    @Query("Select * From despesaentity where categoria is :categoriaIcon")
    fun getAllByCategoriaIcon(categoriaIcon: Int): List<DespesaEntity>

    @Delete
    fun deleteAll (despesaEntity: List<DespesaEntity>)
}