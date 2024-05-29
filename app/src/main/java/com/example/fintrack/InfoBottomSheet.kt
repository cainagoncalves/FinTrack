package com.example.fintrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// Classe que representa o BottomSheetDialogFragment para exibir informações
class InfoBottomSheet(
    private val title: String,           // Título do bottom sheet
    private val description: String,      // Descrição do bottom sheet
    private val btnText: String,          // Texto do botão de ação
    private val onClicked: () -> Unit     // Ação a ser executada ao clicar no botão
) : BottomSheetDialogFragment() {

    // Função que cria e infla a view do bottom sheet
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do bottom sheet
        val view = inflater.inflate(R.layout.info_bottom_sheet, container, false)

        // Inicializa os componentes da view
        val tvTitle = view.findViewById<TextView>(R.id.tv_info_title)         // TextView para o título
        val tvDesc = view.findViewById<TextView>(R.id.tv_info_descricao)      // TextView para a descrição
        val btnAction = view.findViewById<Button>(R.id.btn_info)              // Botão de ação

        // Define os textos dos componentes com os valores recebidos
        tvTitle.text = title
        tvDesc.text = description
        btnAction.text = btnText

        // Define o comportamento do botão de ação
        btnAction.setOnClickListener {
            onClicked.invoke()   // Executa a ação passada como parâmetro
            dismiss()            // Fecha o bottom sheet
        }

        return view  // Retorna a view criada
    }
}
