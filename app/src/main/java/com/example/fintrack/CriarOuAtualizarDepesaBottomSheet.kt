package com.example.fintrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

// BottomSheetDialogFragment para criar ou atualizar uma despesa
class CriarOuAtualizarDespesaBottomSheet(
    private val categoriaList: List<CategoriaEntity>,
    private val despesa: DespesaUi? = null,
    private val onCreateClicked: (DespesaUi) -> Unit,
    private val onUpdateClicked: (DespesaUi) -> Unit,
    private val onDeleteClicked: (DespesaUi) -> Unit

) : BottomSheetDialogFragment() {

    // Ícones a serem ocultados
    private val iconsToHide = listOf(R.drawable.ic_add, R.drawable.ic_add_all)

    @SuppressLint("MissingInflatedId", "LongLogTag", "ResourceAsColor", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Filtra a lista de categorias para ocultar os ícones especificados
        val filteredCategoriaList = categoriaList.filter { it.iconeCategoria !in iconsToHide }

        // Infla o layout do bottom sheet
        val view =
            inflater.inflate(R.layout.create_or_update_despesa_bottom_sheet, container, false)

        // Inicializa os componentes da UI
        val btnAdicionarOuAtualizarDespesa =
            view.findViewById<Button>(R.id.btn_adicionar_ou_atualizar_despesa_sheet)
        val btnDeletarDespesa = view.findViewById<Button>(R.id.btn_deletar_despesa_sheet)
        val tieNomeDespesa = view.findViewById<TextInputEditText>(R.id.tie_nome_despesa)
        val tieValorDespesa = view.findViewById<TextInputEditText>(R.id.tie_valor_despesa)
        val rvCategoriaDespesa = view.findViewById<RecyclerView>(R.id.rv_vincular_categoria)
        val tvAdicionarDespesa = view.findViewById<TextView>(R.id.Adicionar_despesa)

        var categoriaSelecionada: CategoriaUi? = null

        // Configura o RecyclerView para mostrar as categorias
        rvCategoriaDespesa.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val categoriaAdapter = CategoriaAdapter()
        rvCategoriaDespesa.adapter = categoriaAdapter
        val categoriaUiList = convertToCategoriaUiList(filteredCategoriaList)
        categoriaAdapter.submitList(categoriaUiList)

        // Configura o clique nos itens do RecyclerView
        categoriaAdapter.setOnItemClickListener { categoria ->
            categoriaSelecionada = categoria
            categoriaUiList.forEach { it.isSelected = false }
            categoria.isSelected = true
            categoriaAdapter.notifyDataSetChanged()
        }

        // Configura os títulos e botões dependendo se é para criar ou atualizar uma despesa
        if (despesa == null) {
            btnDeletarDespesa.isVisible = false
            tvAdicionarDespesa.setText("Adicionar despesa")
            btnAdicionarOuAtualizarDespesa.setText("Adicionar")
        } else {
            tvAdicionarDespesa.setText("Atualizar despesa")
            btnAdicionarOuAtualizarDespesa.setText("Atualizar")
            tieNomeDespesa.setText(despesa.nome)
            tieValorDespesa.setText(despesa.valor)
            btnDeletarDespesa.isVisible = true

            // Seleciona a categoria vinculada à despesa e mostra no bottom sheet
            val categoria = categoriaList.find { it.iconeCategoria == despesa.categoria }
            categoriaSelecionada = categoria?.let {
                val categoriaUi = CategoriaUi(it.id, it.iconeCategoria, true, it.cor)
                val index = categoriaUiList.indexOfFirst { ui -> ui.id == categoriaUi.id }
                if (index != -1) {
                    categoriaUiList[index].isSelected = true
                    categoriaAdapter.notifyItemChanged(index)
                }
                categoriaUi
            }
        }

        // Configura o clique no botão de deletar despesa
        btnDeletarDespesa.setOnClickListener {
            if (despesa != null) {
                onDeleteClicked.invoke(despesa)
                dismiss()
            } else {
                Log.d("CriarOuAtualizarDespesaBottomSheet", "Despesa não encontrada")
            }
        }

        // Configura o clique no botão de adicionar ou atualizar despesa
        btnAdicionarOuAtualizarDespesa.setOnClickListener {
            val nome = tieNomeDespesa.text.toString().trim()
            val valor = tieValorDespesa.text.toString().trim()
            if (categoriaSelecionada != null && nome.isNotEmpty() && valor.isNotEmpty()) {
                if (despesa == null) {
                    // Cria uma nova despesa
                    onCreateClicked.invoke(
                        DespesaUi(
                            id = 0,
                            nome = nome,
                            valor = valor,
                            categoria = requireNotNull(categoriaSelecionada).iconeCategoria,
                            cor = requireNotNull(categoriaSelecionada).cor
                        )
                    )
                } else {
                    // Atualiza a despesa existente
                    onUpdateClicked.invoke(
                        DespesaUi(
                            id = despesa.id,
                            nome = nome,
                            valor = valor,
                            categoria = requireNotNull(categoriaSelecionada).iconeCategoria,
                            cor = requireNotNull(categoriaSelecionada).cor
                        )
                    )
                }
                dismiss()
            } else {
                // Mostra uma mensagem de erro se os campos não estiverem preenchidos
                Snackbar.make(
                    btnAdicionarOuAtualizarDespesa,
                    "Por favor, preencha todos os campos e selecione uma categoria",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        // Configura o BottomSheet para ocupar a tela inteira
        dialog?.setOnShowListener { dialog ->
            val bottomSheet = dialog as BottomSheetDialog
            val bottomSheetView =
                bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val layoutParams = bottomSheetView?.layoutParams
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheetView?.layoutParams = layoutParams
        }

        return view
    }

    // Converte uma lista de CategoriaEntity para uma lista de CategoriaUi
    private fun convertToCategoriaUiList(categoriasFromDb: List<CategoriaEntity>): List<CategoriaUi> {
        return categoriasFromDb.map {
            CategoriaUi(
                id = it.id,
                iconeCategoria = it.iconeCategoria,
                isSelected = it.isSelected,
                cor = it.cor
            )
        }
    }
}

