package com.example.fintrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// BottomSheetDialogFragment para criar uma nova categoria
class CriarCategoriaBottomSheet(
    private val onCreateClicked: (idCategoria: Long, iconeCategoria: Int, corCategoria: Int) -> Unit,
    private val categoriaDao: CategoriaDao
) : BottomSheetDialogFragment() {

    private var selectedCategoria: CategoriaUi? = null
    private var selectedColor: Int = R.color.white // Cor padrão
    private var selectedButton: Button? = null
    private var categoriaEntity = listOf<CategoriaEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_category_bottom_sheet, container, false)

        // Inicialização dos componentes da UI
        val btnCreate = view.findViewById<Button>(R.id.btn_criar_categoria_sheet)
        val rvCriarCategoria = view.findViewById<RecyclerView>(R.id.rv_criar_categoria)
        val colorButtons = listOf(
            view.findViewById<Button>(R.id.set_color_categoria_blue),
            view.findViewById(R.id.set_color_categoria_red),
            view.findViewById(R.id.set_color_categoria_green),
            view.findViewById(R.id.set_color_categoria_yellow),
            view.findViewById(R.id.set_color_categoria_orange),
            view.findViewById(R.id.set_color_categoria_black)
        )
        rvCriarCategoria.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        val listaCategoria = mutableListOf<CategoriaUi>()
        // Método para adicionar as categorias predefinidas
        getCategoria(listaCategoria)

        // Crie e configure o adapter
        val categoriaAdapter = CategoriaAdapter()
        categoriaAdapter.setOnItemClickListener { categoria ->
            listaCategoria.forEach { it.isSelected = false }
            categoria.isSelected = true
            selectedCategoria = categoria
            categoriaAdapter.notifyDataSetChanged()
        }
        rvCriarCategoria.adapter = categoriaAdapter
        categoriaAdapter.submitList(listaCategoria)

        // Configurando os botões de cor
        colorButtons.forEach { button ->
            button.setOnClickListener {
                selectedColor = button.backgroundTintList?.defaultColor ?: R.color.white
                selectedButton?.setBackgroundResource(R.drawable.filter_chips_background)
                selectedButton = button
                selectedButton?.setBackgroundResource(R.drawable.selected_button_background_colors)
            }
        }

        // Carregar categorias existentes do banco de dados
        GlobalScope.launch(Dispatchers.Main) {
            categoriaEntity = withContext(Dispatchers.IO) {
                categoriaDao.getAll()
            }
        }

        btnCreate.setOnClickListener {
            if (selectedCategoria == null || selectedButton == null) {
                Snackbar.make(btnCreate, "Por favor, selecione um ícone e uma cor para a categoria", Snackbar.LENGTH_LONG).show()
            } else {
                // Verifique se o ícone já está em uso
                val iconeSelecionado = selectedCategoria!!.iconeCategoria
                val iconeExistente = categoriaEntity.any { it.iconeCategoria == iconeSelecionado }
                if (iconeExistente) {
                    Snackbar.make(btnCreate, "O ícone selecionado já está em uso, por favor, escolha outro", Snackbar.LENGTH_LONG).show()
                } else {
                    selectedCategoria?.let { categoria ->
                        val idCategoria = System.currentTimeMillis() // Exemplo para gerar um ID único
                        onCreateClicked(idCategoria, categoria.iconeCategoria, selectedColor)
                        dismiss()
                    }
                }
            }
        }

        dialog?.setOnShowListener { dialog ->
            val bottomSheet = dialog as BottomSheetDialog
            val bottomSheetView = bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val layoutParams = bottomSheetView?.layoutParams
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheetView?.layoutParams = layoutParams
        }

        return view
    }

    // Método para adicionar categorias predefinidas
    private fun getCategoria(listaCategoria: MutableList<CategoriaUi>) {
        listaCategoria.apply {
            add(CategoriaUi(0, R.drawable.ic_home, false))
            add(CategoriaUi(0, R.drawable.ic_key, false))
            add(CategoriaUi(0, R.drawable.ic_wifi, false))
            add(CategoriaUi(0, R.drawable.ic_clothes, false))
            add(CategoriaUi(0, R.drawable.ic_electricity, false))
            add(CategoriaUi(0, R.drawable.ic_car, false))
            add(CategoriaUi(0, R.drawable.ic_credit_card, false))
            add(CategoriaUi(0, R.drawable.ic_shopping_cart, false))
            add(CategoriaUi(0, R.drawable.ic_water_drop, false))
            add(CategoriaUi(0, R.drawable.ic_gas_station, false))
            add(CategoriaUi(0, R.drawable.ic_game_control, false))
        }
    }
}
