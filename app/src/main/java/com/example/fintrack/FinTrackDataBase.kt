package com.example.fintrack

import androidx.room.Database
import androidx.room.RoomDatabase

// Define a classe de banco de dados do Room, especificando as entidades e a versão do banco de dados
@Database(entities = [CategoriaEntity::class, DespesaEntity::class], version = 3)
abstract class FinTrackDataBase : RoomDatabase() {

    // Função abstrata para obter o DAO de Categoria
    abstract fun getCategoriaDao(): CategoriaDao

    // Função abstrata para obter o DAO de Despesa
    abstract fun getDespesaDao(): DespesaDao
}
