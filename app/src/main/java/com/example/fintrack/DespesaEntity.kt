package com.example.fintrack

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Define a entidade Despesa no banco de dados Room, com chave estrangeira para CategoriaEntity
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,       // Entidade pai: CategoriaEntity
            parentColumns = ["key"],               // Coluna na entidade pai: "key"
            childColumns = ["categoria"]           // Coluna na entidade filha: "categoria"
        )
    ]
)
data class DespesaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,      // ID único da despesa, gerado automaticamente
    val nome: String,      // Nome da despesa
    val valor: String,     // Valor da despesa, armazenado como String
    var categoria: Int,    // Categoria da despesa, representada por um inteiro (chave estrangeira)
    var cor: Int           // Cor associada à despesa, representada por um inteiro (pode ser um recurso de cor)
)
