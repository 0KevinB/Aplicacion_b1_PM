package com.example.aplicacion

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val txtEmail = findViewById<EditText>(R.id.email)
        val txtPassword = findViewById<EditText>(R.id.password)
        val btnLogin = findViewById<Button>(R.id.login_button)

        btnLogin.setOnClickListener { view: View? ->
            val correo = txtEmail.text.toString()
            val clave = txtPassword.text.toString()

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(
                    this,
                    "Email o contraseña no pueden estar vacíos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            auth!!.signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(
                    this
                ) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Login exitoso
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        Toast.makeText(
                            this,
                            "Inicio de sesión exitoso",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        val intent = Intent(
                            this,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        // Fallo en el login
                        Log.w(
                            ContentValues.TAG,
                            "signInWithEmail:failure",
                            task.exception
                        )
                        Toast.makeText(
                            this,
                            "Error de autenticación: " + task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}