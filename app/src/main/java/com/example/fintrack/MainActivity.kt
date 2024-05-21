package com.example.fintrack

import DespesaAdapter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), CriarCategoriaBottomSheet.OnCategoriaCreatedListener, CriarDespesaBottomSheet.OnDespesaCreatedListener {

    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var despesaAdapter: DespesaAdapter
    private val listaCategoria = mutableListOf<CategoriaUi>()
    private val listaDespesa = mutableListOf<DespesaUi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView de categorias
        val rvCategoria = findViewById<RecyclerView>(R.id.rv_categoria)
        categoriaAdapter = CategoriaAdapter(this, listaCategoria) { /* Empty */ }
        rvCategoria.adapter = categoriaAdapter
        rvCategoria.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Configurar RecyclerView de despesas
        val rvDespesa = findViewById<RecyclerView>(R.id.rv_despesa)
        despesaAdapter = DespesaAdapter(this, listaDespesa, listaCategoria) { despesa, categoria ->
            despesa.categoria = categoria.iconeCategoria
        }
        rvDespesa.adapter = despesaAdapter
        rvDespesa.layoutManager = LinearLayoutManager(this)

        // Configurar botão para abrir CriarCategoriaBottomSheet
        val btnCriarCategoria = findViewById<Button>(R.id.btn_category_create)
        btnCriarCategoria.setOnClickListener {
            val createCategoryBottomSheet = CriarCategoriaBottomSheet()
            createCategoryBottomSheet.setOnCategoriaCreatedListener(this)
            createCategoryBottomSheet.show(supportFragmentManager, "createCategoryBottomSheet")
        }

        // Configurar botão para abrir CriarDespesaBottomSheet
        val btnAdicionarDespesa = findViewById<Button>(R.id.button)
        btnAdicionarDespesa.setOnClickListener {
            val createDespesaBottomSheet = CriarDespesaBottomSheet(listaCategoria) { despesa ->
                onDespesaCreated(despesa)
            }
            createDespesaBottomSheet.show(supportFragmentManager, "createDespesaBottomSheet")
        }
    }

    override fun onCategoriaCreated(categoria: CategoriaUi) {
        listaCategoria.add(categoria)
        categoriaAdapter.notifyItemInserted(listaCategoria.size - 1)
    }

    override fun onDespesaCreated(despesa: DespesaUi) {
        listaDespesa.add(despesa)
        despesaAdapter.notifyItemInserted(listaDespesa.size - 1)
    }
}
