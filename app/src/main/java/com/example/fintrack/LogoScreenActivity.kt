package com.example.fintrack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

// Classe que exibe uma tela de logotipo temporária antes de abrir a MainActivity
class LogoScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout da tela de logotipo
        setContentView(R.layout.logo_screen)

        // Após um tempo de espera, redireciona para a MainActivity
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a atividade atual para evitar que o usuário volte para a tela de logotipo
        }, 3000) // Define o tempo de espera do logotipo para 3 segundos
    }
}
