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


class InfoBottomSheet(
    private val title: String,
    private val description: String,
    private val btnText: String,
    private val onClicked: () -> Unit
    ) : BottomSheetDialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.info_bottom_sheet, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.tv_info_title)
        val tvDesc = view.findViewById<TextView>(R.id.tv_info_descricao)
        val btnAction = view.findViewById<Button>(R.id.btn_info)

        tvTitle.text = title
        tvDesc.text = description
        btnAction.text = btnText

        btnAction.setOnClickListener {
            onClicked.invoke()
            dismiss()
        }

        return view
    }

}
