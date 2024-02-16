package com.example.tfc

import android.content.Intent
import android.content.res.Resources
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.tfc.sqlite.DatabaseHelper

import com.example.tfc.clasesAuxiliares.Ejercicio


class ActivityBienvenida : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        iniciaComponentes()

        try {
            //Si no existe el ejercicio,cargamos todos los ejercicios,asi evitamos cargarlos siempre al abrir la app
            if(db.getEjercicio(1)==null)
                cargarTablaEjercicios(resources, categoriasEjercicios)

            //Realizamos una consulta para saber si existe el usuario
            //Si existe
            if (db.contarUsuarios() != 0) {
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
    private fun crearObjetosEjercico(
        categoria: String,
        ejercicios: Array<String>,
        videos: Array<String>
    ) {
        //Por cada ejercicio recoge el video por su mismo indice y lo añade a la base de datos
        ejercicios.forEachIndexed { indice, ejercicio ->
            val videoId = videos.getOrNull(indice) ?: ""
            db.addEjercicio(Ejercicio(categoria = categoria, nombre = ejercicio, video = videoId))
        }
    }

    private fun cargarTablaEjercicios(
        resources: Resources,
        categoriasEjercicios: Map<Int, Pair<Int, Int>>
    ) {
        /*Crea la tabla a través de las posiciones de los ejercicios en el mapeo,es decir,recoge mismo indice para el nombre del
          ejercicio y del video para poder crear los objetos Ejercicio*/
        categoriasEjercicios.forEach { (categoria, recursos) ->
            val categoriaPosition = resources.getString(categoria)
            val ejerciciosPosition = recursos.first
            val videosIdPosition = recursos.second

            val ejercicios = resources.getStringArray(ejerciciosPosition)
            val videos = resources.getStringArray(videosIdPosition)

            crearObjetosEjercico(categoriaPosition, ejercicios, videos)
        }
    }

    //Mapeo de ejercicios y videos por categorias
    private val categoriasEjercicios = mapOf(
        R.string.pecho to Pair(R.array.ejercicios_pecho, R.array.yt_pecho),
        R.string.espalda to Pair(R.array.ejercicios_espalda, R.array.yt_espalda),
        R.string.biceps to Pair(R.array.ejercicios_biceps, R.array.yt_biceps),
        R.string.triceps to Pair(R.array.ejercicios_triceps, R.array.yt_triceps),
        R.string.abs to Pair(R.array.ejercicios_abs, R.array.yt_abs),
        R.string.hombro to Pair(R.array.ejercicios_hombro, R.array.yt_hombro),
        R.string.gluteo to Pair(R.array.ejercicios_gluteo, R.array.yt_gluteo),
        R.string.cuadriceps to Pair(R.array.ejercicios_cuadriceps, R.array.yt_cuadriceps),
        R.string.gemelo to Pair(R.array.ejercicios_gemelo, R.array.yt_gemelo)
    )

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    private fun iniciaComponentes() {
        btnAcceso = findViewById(R.id.btnAcceso)
        db = DatabaseHelper(this)

    }
    private lateinit var btnAcceso: Button
    private lateinit var db : DatabaseHelper
}


