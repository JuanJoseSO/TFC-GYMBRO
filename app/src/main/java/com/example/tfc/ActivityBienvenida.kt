package com.example.tfc

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.tfc.SQLite.DatabaseHelper

import com.example.tfc.clasesAuxiliares.Ejercicio


class ActivityBienvenida : AppCompatActivity() {
    private val db = DatabaseHelper(this)

    private lateinit var btnAcceso: Button

    //Iniciamos componenestes
    private fun iniciaComponentes() {
        btnAcceso = findViewById(R.id.btnAcceso)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        iniciaComponentes()

        cargarCategoriasEjercicios(db, resources, categoriasEjercicios)

        btnAcceso.setText(R.string.btnAcceso)

        //Con sharedPreferences podemos comprobar sí es la primera ves que se inicia la aplicación
        //Si fuera así cargamos los ejercicios, con esto eviatamos que se carguen cada vez que de abre la app
        val prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("firstTime", true)) {
            //Es la primera vez que se ejecuta la app

            prefs.edit().putBoolean("firstTime", false).apply()
        }
        try {
            btnAcceso.setOnClickListener {
                Toast.makeText(this, "Botón presionado", Toast.LENGTH_SHORT).show()
            }

            //Realizamos una consulta para saber si existe el usuario
            //Si existe
            if (db.getUsuario() != null) {
                //Redirigimos a la actividad de principal
                btnAcceso.setOnClickListener {
                    val intent = Intent(this, ActivityPrincipal::class.java)
                    startActivity(intent)
                }

            } else {
                btnAcceso.setOnClickListener {
                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)
                }

            }
        } catch (e: SQLiteException) {
            Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para cargar los ejercicios de una categoría
    fun cargarEjerciciosCategoria(
        categoria: String,
        ejercicios: Array<String>,
        videos: Array<String>,
        db: DatabaseHelper
    ) {
        ejercicios.forEachIndexed { indice, ejercicio ->
            val videoId =
                videos.getOrNull(indice) ?: "" // Obtener el ID del video correspondiente si existe
            db.addEjercicio(Ejercicio(categoria = categoria, nombre = ejercicio, video = videoId))
        }
    }

    // Función para cargar las categorías de ejercicios
    fun cargarCategoriasEjercicios(
        db: DatabaseHelper,
        resources: Resources,
        categoriasEjercicios: Map<Int, Pair<Int, Int>>
    ) {
        categoriasEjercicios.forEach { (categoriaResId, recursos) ->
            val categoria = resources.getString(categoriaResId)
            val ejerciciosResId = recursos.first
            val videosResId = recursos.second

            val ejercicios = resources.getStringArray(ejerciciosResId)
            val videos = resources.getStringArray(videosResId)

            cargarEjerciciosCategoria(categoria, ejercicios, videos, db)
        }
    }


    // Uso de las funciones anteriores
    val categoriasEjercicios = mapOf(
        R.string.pecho to Pair(R.array.ejercicios_pecho, R.array.yt_pecho),
        R.string.espalda to Pair(R.array.ejercicios_espalda, R.array.yt_espalda),
        R.string.biceps to Pair(R.array.ejercicios_biceps, R.array.yt_biceps),
        R.string.triceps to Pair(R.array.ejercicios_triceps, R.array.yt_triceps),
        R.string.abs to Pair(R.array.ejercicios_abs, R.array.yt_abs),
        R.string.hombros to Pair(R.array.ejercicios_hombro, R.array.yt_hombros),
        R.string.gluteo to Pair(R.array.ejercicios_gluteo, R.array.yt_gluteo),
        R.string.cuadriceps to Pair(R.array.ejercicios_cuadriceps, R.array.yt_cuadriceps),
        R.string.gemelo to Pair(R.array.ejercicios_gemelo, R.array.yt_gemelo)
    )

    override fun onDestroy() {
        // Cierra la base de datos cuando la actividad es destruida
        db.close()
        super.onDestroy()
    }
}


