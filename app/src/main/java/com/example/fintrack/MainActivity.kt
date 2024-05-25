package com.example.fintrack

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

        //Configurando adapter para atualizar despesas
        despesaAdapter.setOnClickListener { despesa ->
            showCriarAtualizarDespesaBottomSheet(despesa)
        }

        //Configurando longoClick para deletar categorias
        categoriaAdapter.setOnLongClickListener { categoriaAserDeletada ->
            // Configurando a caixa de informação ao deletar categoria
            if (categoriaAserDeletada.iconeCategoria != R.drawable.ic_add && categoriaAserDeletada.iconeCategoria != R.drawable.ic_add_all) {
                val title: String = this.getString(R.string.deletar_categoria_title)
                val descricao: String = this.getString(R.string.deletar_categoria_descricao)
                val btnText: String = this.getString(R.string.delete)

                showInfoDialog(
                    title,
                    descricao,
                    btnText
                ) {
                    val categoriaEntityAserDeletada = CategoriaEntity(
                        categoriaAserDeletada.id,
                        categoriaAserDeletada.iconeCategoria,
                        categoriaAserDeletada.isSelected,
                        categoriaAserDeletada.cor
                    )
                    deleteCategoria(categoriaEntityAserDeletada)
                }
            }
        }

        // Configurar botão para abrir CriarCategoriaBottomSheet
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
                criarCategoriaBottomSheet.show(
                    supportFragmentManager,
                    "criarCategoriaBottomSheet"
                )
            } else {
                val categoriaTemp = listaCategoria.map { item ->
                    when {
                        item.iconeCategoria == selected.iconeCategoria && item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.iconeCategoria == selected.iconeCategoria && !item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.iconeCategoria != selected.iconeCategoria && item.isSelected -> item.copy(
                            isSelected = false
                        )

                        else -> item
                    }
                }
                if (selected.iconeCategoria != R.drawable.ic_add_all) {
                    filterDespesaPelaCategoriaIcon(selected.iconeCategoria)
                    // Calcular o total das despesas para a categoria selecionada
                    val totalDespesasCategoria = calcularDespesasTotaisPorCategoria(listaDespesa, selected.iconeCategoria)
                    // Atualizar o TextView com o valor total das despesas da categoria
                    GlobalScope.launch(Dispatchers.Main) {
                        atualizarDespesasTotais(totalDespesasCategoria)
                    }
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        getDespesasFromDataBase()
                    }
                }
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

    //Função para exibir infoBottomSheet
    private fun showInfoDialog(
        title: String,
        description: String,
        btnText: String,
        onClick: () -> Unit
    ) {
        val infoBottomSheet = InfoBottomSheet(
            title = title,
            description = description,
            btnText = btnText,
            onClick
        )
        infoBottomSheet.show(
            supportFragmentManager,
            "InfoBottomSheet"
        )
    }

    private fun getCategoriasFromDataBase() {
        val categoriasFromDb: List<CategoriaEntity> = categoriaDao.getAll()
        val categoriasUiData = categoriasFromDb.map {
            CategoriaUi(
                id = it.id,
                iconeCategoria = it.iconeCategoria,
                isSelected = it.isSelected,
                cor = it.cor
            )
        }.toMutableList()

        // Add fake + category
        categoriasUiData.add(
            CategoriaUi(
                id = 0,
                iconeCategoria = R.drawable.ic_add,
                isSelected = false,
                cor = R.color.white
            )
        )

        val categoriaListTemp = mutableListOf(
            CategoriaUi(
                id = 0,
                iconeCategoria = R.drawable.ic_add_all,
                isSelected = true,
                cor = R.color.white
            )
        )

        categoriaListTemp.addAll(categoriasUiData)
        // Ajuda a não dar erro, pois para não dar erro precisa ir para a Main thread
        GlobalScope.launch(Dispatchers.Main) {
            listaCategoria = categoriaListTemp
            categoriaAdapter.submitList(listaCategoria)
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
            // Atualiza o total de despesas gerais
            val totalDespesas = calcularDespesasTotais(despesasUi)
            atualizarDespesasTotais(totalDespesas)
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

    private fun deleteCategoria(categoriaEntity: CategoriaEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            val despesasASeremDeletadas =
                despesaDao.getAllByCategoriaIcon(categoriaEntity.iconeCategoria)
            despesaDao.deleteAll(despesasASeremDeletadas)
            categoriaDao.delete(categoriaEntity)
            getCategoriasFromDataBase()  // Atualiza a lista de categoria na UI
            getDespesasFromDataBase()
        }
    }

    private fun filterDespesaPelaCategoriaIcon(categoria: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val despesasFromDb: List<DespesaEntity> = despesaDao.getAllByCategoriaIcon(categoria)
            val despesasUi: List<DespesaUi> = despesasFromDb.map {
                DespesaUi(
                    id = it.id,
                    nome = it.nome,
                    valor = it.valor,
                    categoria = it.categoria,
                    cor = it.cor
                )
            }

            //Ajuda a não dar erro, pois para não dar erro precisar ir para a Main thread
            GlobalScope.launch(Dispatchers.Main) {
                despesaAdapter.submitList(despesasUi)
            }
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
            onDeleteClicked = { despesaAserDeletada ->
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

    private fun calcularDespesasTotais(despesas: List<DespesaUi>): Double {
        return despesas.sumByDouble { it.valor.replace(",", ".").toDouble() }
    }

    private fun calcularDespesasTotaisPorCategoria(despesas: List<DespesaUi>, categoria: Int): Double {
        return despesas.filter { it.categoria == categoria }.sumByDouble { it.valor.replace(",", ".").toDouble() }
    }

    private fun atualizarDespesasTotais(total: Double) {
        val valorSaldoTextView = findViewById<TextView>(R.id.valor_saldo)
        valorSaldoTextView.text = String.format("R$ %.2f", total)
    }
}
