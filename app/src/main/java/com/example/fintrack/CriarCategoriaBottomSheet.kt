package com.example.fintrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class CriarCategoriaBottomSheet : BottomSheetDialogFragment() {

    private var listener: OnCategoriaCreatedListener? = null
    private var selectedCategoria: CategoriaUi? = null

    fun setOnCategoriaCreatedListener(listener: OnCategoriaCreatedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_category_bottom_sheet, container, false)
        val btnCriar = view.findViewById<Button>(R.id.btn_criar_categoria_sheet)
        val recyclerViewCategoria = view.findViewById<RecyclerView>(R.id.rv_criar_categoria)
        recyclerViewCategoria.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        val listaCategoria = mutableListOf<CategoriaUi>()
        val categoriaAdapter = CategoriaAdapter(requireContext(), listaCategoria) { categoria ->
            selectedCategoria = categoria
        }
        recyclerViewCategoria.setHasFixedSize(true)
        recyclerViewCategoria.adapter = categoriaAdapter
        getCategoria(listaCategoria)

        btnCriar.setOnClickListener {
            selectedCategoria?.let { categoria ->
                listener?.onCategoriaCreated(categoria)
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

    private fun getCategoria(listaCategoria: MutableList<CategoriaUi>) {
        listaCategoria.apply {
            add(CategoriaUi(R.drawable.ic_home,false))
            add(CategoriaUi(R.drawable.ic_key, false))
            add(CategoriaUi(R.drawable.ic_wifi, false))
            add(CategoriaUi(R.drawable.ic_clothes, false))
            add(CategoriaUi(R.drawable.ic_electricity, false))
            add(CategoriaUi(R.drawable.ic_car, false))
            add(CategoriaUi(R.drawable.ic_credit_card,  false))
            add(CategoriaUi(R.drawable.ic_shopping_cart, false))
            add(CategoriaUi(R.drawable.ic_water_drop, false))
            add(CategoriaUi(R.drawable.ic_gas_station, false))
            add(CategoriaUi(R.drawable.ic_game_control, false))
        }
    }

    interface OnCategoriaCreatedListener {
        fun onCategoriaCreated(categoria: CategoriaUi)
    }
}