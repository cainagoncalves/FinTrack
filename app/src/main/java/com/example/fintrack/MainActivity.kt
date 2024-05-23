package com.example.fintrack

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var listaCategoria = listOf<CategoriaUi>()
    private var listaDespesa = listOf<DespesaUi>()

    private lateinit var categoriaAdapter: CategoriaAdapter
    private val despesaAdapter by lazy {
        DespesaAdapter()
    }

    // Faz com que a base de dados seja utilizada apenas se necessário, p/ não gastar memória
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinTrackDataBase::class.java, "database-fintrack"
        ).build()
    }

    private val categoriaDao by lazy {
        db.getCategoriaDao()
    }

    private val despesaDao by lazy {
        db.getDespesaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView de categorias
        val rvCategoria = findViewById<RecyclerView>(R.id.rv_categoria)
        categoriaAdapter = CategoriaAdapter()
        rvCategoria.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Configurar RecyclerView de despesas
        val rvDespesa = findViewById<RecyclerView>(R.id.rv_despesa)
        rvDespesa.layoutManager = LinearLayoutManager(this)

        // Chamar botão para criar despesas
        val criarDespesa = findViewById<Button>(R.id.btn_adicionar_despesa)

        criarDespesa.setOnClickListener {
            showCriarAtualizarDespesaBottomSheet()
        }

        despesaAdapter.setOnClickListener { despesa ->
            showCriarAtualizarDespesaBottomSheet(despesa)
        }

        // Configurar botão para abrir CriarCategoriaBottomSheet
//        val btnCriarCategoria = findViewById<Button>(R.id.btn_category_create)
        categoriaAdapter.setOnItemClickListener { selected ->
            if (selected.iconeCategoria == R.drawable.ic_add) {
                val criarCategoriaBottomSheet =
                    CriarCategoriaBottomSheet { idCategoria, iconeCategoria, corCategoria ->
                        val categoriaEntity = CategoriaEntity(
                            id = idCategoria,
                            iconeCategoria = iconeCategoria,
                            isSelected = false,
                            cor = corCategoria
                        )
                        insertCategorias(categoriaEntity)
                    }
                criarCategoriaBottomSheet.show(supportFragmentManager, "criarCategoriaBottomSheet")
            } else {
                val categoriaTemp = listaCategoria.map { item ->
                    when {
                        item.iconeCategoria == selected.iconeCategoria && !item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.iconeCategoria == selected.iconeCategoria && item.isSelected -> item.copy(
                            isSelected = false
                        )

                        else -> item
                    }
                }
                val despesaTemp =
                    if (selected.iconeCategoria != R.drawable.ic_add) {
                        listaDespesa.filter { it.categoria == selected.iconeCategoria }
                    } else {
                        listaDespesa
                    }
                despesaAdapter.submitList(despesaTemp)
                categoriaAdapter.submitList(categoriaTemp)
            }
        }

        // Exibe as categorias selecionadas na view
        rvCategoria.adapter = categoriaAdapter
        GlobalScope.launch(Dispatchers.IO) {
            getCategoriasFromDataBase()
        }

        // Exibe as despesas selecionadas na view
        rvDespesa.adapter = despesaAdapter
        GlobalScope.launch(Dispatchers.IO) {
            getDespesasFromDataBase()
        }
    }

    private fun getCategoriasFromDataBase() {
        val categoriasFromDb: List<CategoriaEntity> = categoriaDao.getAll()
        val categoriasUiData = categoriasFromDb.map {
            CategoriaUi(
                iconeCategoria = it.iconeCategoria,
                isSelected = it.isSelected,
                cor = it.cor
            )
        }.toMutableList()

        // Add fake + category
        categoriasUiData.add(
            CategoriaUi(
                iconeCategoria = R.drawable.ic_add,
                isSelected = false,
                cor = R.color.white
            )
        )
        // Ajuda a não dar erro, pois para não dar erro precisa ir para a Main thread
        GlobalScope.launch(Dispatchers.Main) {
            listaCategoria = categoriasUiData
            categoriaAdapter.submitList(categoriasUiData)
        }
    }

    private fun getDespesasFromDataBase() {
        val despesasFromDb: List<DespesaEntity> = despesaDao.getAll()
        val despesasUi: List<DespesaUi> = despesasFromDb.map {
            DespesaUi(
                id = it.id,
                nome = it.nome,
                valor = it.valor,
                categoria = it.categoria,
                cor = it.cor
            )
        }
        // Ajuda a não dar erro, pois para não dar erro precisa ir para a Main thread
        GlobalScope.launch(Dispatchers.Main) {
            listaDespesa = despesasUi
            despesaAdapter.submitList(despesasUi)
        }
    }

    private fun insertCategorias(categoriaEntity: CategoriaEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            categoriaDao.insert(categoriaEntity)
            getCategoriasFromDataBase()
        }
    }

    private fun insertDespesas(despesaEntity: DespesaEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            despesaDao.insert(despesaEntity)
            getDespesasFromDataBase()  // Atualiza a lista de despesas na UI
        }
    }

    private fun updateDespesas(despesaEntity: DespesaEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            despesaDao.update(despesaEntity)
            getDespesasFromDataBase()  // Atualiza a lista de despesas na UI
        }
    }

    private fun deleteDespesas(despesaEntity: DespesaEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            despesaDao.delete(despesaEntity)
            getDespesasFromDataBase()  // Atualiza a lista de despesas na UI
        }
    }

    private fun showCriarAtualizarDespesaBottomSheet(despesaUi: DespesaUi? = null) {
        val createDespesaBottomSheet = CriarOuAtualizarDespesaBottomSheet(
            despesa = despesaUi,
            categoriaList = listaCategoria,
            onCreateClicked = { despesaAserCriada ->
                val despesaEntityToBeInsert = DespesaEntity(
                    id = despesaAserCriada.id,
                    nome = despesaAserCriada.nome,
                    valor = despesaAserCriada.valor,
                    categoria = despesaAserCriada.categoria,
                    cor = despesaAserCriada.cor
                )
                insertDespesas(despesaEntityToBeInsert)
            },
            onUpdateClicked = { despesaAserAtualizada ->
                val despesaEntityToBeUpdate = DespesaEntity(
                    id = despesaAserAtualizada.id,
                    nome = despesaAserAtualizada.nome,
                    valor = despesaAserAtualizada.valor,
                    categoria = despesaAserAtualizada.categoria,
                    cor = despesaAserAtualizada.cor
                )
                updateDespesas(despesaEntityToBeUpdate)
            },
            onDeleteClicked = {despesaAserDeletada ->
                val despesaEntityToBeDeleted = DespesaEntity(
                    id = despesaAserDeletada.id,
                    nome = despesaAserDeletada.nome,
                    valor = despesaAserDeletada.valor,
                    categoria = despesaAserDeletada.categoria,
                    cor = despesaAserDeletada.cor
                )
                deleteDespesas(despesaEntityToBeDeleted)
            }
        )

        createDespesaBottomSheet.show(supportFragmentManager, "createDespesaBottomSheet")
    }
}
