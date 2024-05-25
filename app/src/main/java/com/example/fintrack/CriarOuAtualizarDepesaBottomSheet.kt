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

class CriarOuAtualizarDespesaBottomSheet(
    private val categoriaList: List<CategoriaUi>,
    private val despesa: DespesaUi? = null,
    private val onCreateClicked: (DespesaUi) -> Unit,
    private val onUpdateClicked: (DespesaUi) -> Unit,
    private val onDeleteClicked: (DespesaUi) -> Unit

) : BottomSheetDialogFragment() {

    private val iconsToHide = listOf(R.drawable.ic_add, R.drawable.ic_add_all)

    @SuppressLint("MissingInflatedId", "LongLogTag", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val filteredCategoriaList = categoriaList.filter { it.iconeCategoria !in iconsToHide }

        val view =
            inflater.inflate(R.layout.create_or_update_despesa_bottom_sheet, container, false)

        val btnAdicionarOuAtualizarDespesa =
            view.findViewById<Button>(R.id.btn_adicionar_ou_atualizar_despesa_sheet)
        val btnDeletarDespesa = view.findViewById<Button>(R.id.btn_deletar_despesa_sheet)
        val tieNomeDespesa = view.findViewById<TextInputEditText>(R.id.tie_nome_despesa)
        val tieValorDespesa = view.findViewById<TextInputEditText>(R.id.tie_valor_despesa)
        val rvCategoriaDespesa = view.findViewById<RecyclerView>(R.id.rv_vincular_categoria)
        val tvAdicionarDespesa = view.findViewById<TextView>(R.id.Adicionar_despesa)

        var categoriaSelecionada: CategoriaUi? = null

        rvCategoriaDespesa.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val categoriaAdapter = CategoriaAdapter()
        rvCategoriaDespesa.adapter = categoriaAdapter
        categoriaAdapter.submitList(filteredCategoriaList.toMutableList())

        categoriaAdapter.setOnItemClickListener { categoria ->
            categoriaSelecionada = categoria
        }

        // faz a alteração dos títulos e botões entre criar/atualizar despesa
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

            // Seleciona a categoria vinculada à despesa
            val categoria = categoriaList.find { it.iconeCategoria == despesa.categoria }
            categoriaSelecionada = categoria
            categoriaAdapter.setSelectedCategory(categoria)
        }

        btnDeletarDespesa.setOnClickListener {
            if (despesa != null) {
                onDeleteClicked.invoke(despesa)
                dismiss()
            } else {
                Log.d("CriarOuAtualizarDespesaBottomSheet", "Task not found")
            }
        }

        btnAdicionarOuAtualizarDespesa.setOnClickListener {
            val nome = tieNomeDespesa.text.toString().trim()
            val valor = tieValorDespesa.text.toString().trim()
            if (categoriaSelecionada != null && nome.isNotEmpty() && valor.isNotEmpty()) {

                if (despesa == null) {
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
                Snackbar.make(
                    btnAdicionarOuAtualizarDespesa,
                    "Por favor, preencha todos os campos e selecione uma categoria.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

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


    interface OnDespesaCreatedListener {
        fun onDespesaCreated(despesa: DespesaUi)
    }
}