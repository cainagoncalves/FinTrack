package com.example.fintrack

import androidx.room.Database
import androidx.room.RoomDatabase


@Database([CategoriaEntity::class, DespesaEntity::class], version = 3)
abstract class FinTrackDataBase : RoomDatabase() {

    abstract fun getCategoriaDao(): CategoriaDao

    abstract fun getDespesaDao(): DespesaDao
}