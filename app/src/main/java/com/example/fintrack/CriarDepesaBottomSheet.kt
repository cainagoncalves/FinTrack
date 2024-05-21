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
import com.google.android.material.textfield.TextInputEditText


class CriarDespesaBottomSheet(
    private val categoriaList: List<CategoriaUi>,
    private val onCreateClicked: (DespesaUi) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_despesa_bottom_sheet, container, false)

        val btnAdicionarDespesa = view.findViewById<Button>(R.id.btn_adicionar_despesa_sheet)
        val tieNomeDespesa = view.findViewById<TextInputEditText>(R.id.tie_nome_despesa)
        val tieValorDespesa = view.findViewById<TextInputEditText>(R.id.tie_valor_despesa)
        val rvCategoriaDespesa = view.findViewById<RecyclerView>(R.id.rv_vincular_categoria)

        var categoriaSelecionada: CategoriaUi? = null

        rvCategoriaDespesa.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCategoriaDespesa.adapter = CategoriaAdapter(requireContext(), categoriaList.toMutableList()) { categoria ->
            categoriaSelecionada = categoria
        }

        btnAdicionarDespesa.setOnClickListener {
            val nome = tieNomeDespesa.text.toString()
            val valor = tieValorDespesa.text.toString()

            if (categoriaSelecionada == null || nome.isEmpty() || valor.isEmpty()) {
                Snackbar.make(
                    btnAdicionarDespesa,
                    "Por favor, preencha todos os campos e selecione uma categoria.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                onCreateClicked.invoke(
                    DespesaUi(
                        nome = nome,
                        valor = valor,
                        categoria = requireNotNull(categoriaSelecionada).iconeCategoria
                    )
                )
                dismiss()
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

    interface OnDespesaCreatedListener {
        fun onDespesaCreated(despesa: DespesaUi)
    }
}
