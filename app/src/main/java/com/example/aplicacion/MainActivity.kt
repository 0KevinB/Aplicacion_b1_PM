package com.example.aplicacion

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Aplicacion)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        navegar()
    }

    private fun navegar() {
        val buttonFormulario = findViewById<Button>(R.id.button_formulario)
        buttonFormulario.setOnClickListener() {
            val cambiar_ventana = Intent(this, formulario::class.java)
            startActivity(cambiar_ventana)
        }

        val buttonNavegar = findViewById<Button>(R.id.button_imagen)
        buttonNavegar.setOnClickListener() {
            val cambiar_ventana = Intent(this, imagen::class.java)
            startActivity(cambiar_ventana)
        }

        val buttonToRows = findViewById<Button>(R.id.button_rows)
        buttonToRows.setOnClickListener() {
            val cambiar_ventana = Intent(this, rows::class.java)
            startActivity(cambiar_ventana)
        }

        val buttonToColumns = findViewById<Button>(R.id.button_columns)
        buttonToColumns.setOnClickListener() {
            val cambiar_ventana = Intent(this, columns::class.java)
            startActivity(cambiar_ventana)
        }

        val buttonToRowsColumns = findViewById<Button>(R.id.button_rowcolumns)
        buttonToRowsColumns.setOnClickListener() {
            val cambiar_ventana = Intent(this, rowcolumns::class.java)
            startActivity(cambiar_ventana)
        }

        val buttonCamara = findViewById<Button>(R.id.button_camara)
        buttonCamara.setOnClickListener() {
            val cambiar_ventana = Intent(this, camara::class.java)
            startActivity(cambiar_ventana)
        }

        val buttonLocal = findViewById<Button>(R.id.button_localizacion)
        buttonLocal.setOnClickListener() {
            val cambiar_ventana = Intent(this, localizar::class.java)
            startActivity(cambiar_ventana)
        }

        val buttonPodometro = findViewById<Button>(R.id.button_podometro)
        buttonPodometro.setOnClickListener() {
            val cambiar_ventana = Intent(this, podometro::class.java)
            startActivity(cambiar_ventana)
        }

    }

    }

