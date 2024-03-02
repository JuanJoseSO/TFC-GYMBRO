package com.example.tfc

import android.content.Intent
import android.content.res.Resources
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tfc.clasesAuxiliares.clasesBase.Dieta
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.DietaDb
import com.example.tfc.sqlite.sqliteMetodos.EjerciciosDb
import com.example.tfc.sqlite.sqliteMetodos.RutinaDb
import com.example.tfc.sqlite.sqliteMetodos.RutinaEjercicioDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb


class ActivityBienvenida : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        iniciaComponentes()

        try {
            //Si no existe el ejercicio,cargamos todos los ejercicios,asi evitamos cargarlos siempre al abrir la app
            if (ejerciciosDb.getEjercicio(1) == null) cargarTablaEjercicios(
                resources, categoriasEjercicios
            )
            //Lo mismo con las dietas
            if (dietaDb.getDieta(1) == null) cargarDietas()
            if (rutinaDb.getRutina(1) == null) cargarRutinas()

            //Realizamos una consulta para saber si existe el usuario
            //Si existe
            if (usersDb.contarUsuarios() != 0) {
                //Redirigimos a la actividad de principal
                btnAcceso.setOnClickListener {
                    val intent = Intent(this, ActivityPrincipal::class.java)
                    startActivity(intent)
                }

            } else {
                //Si no vamos a la activity para crearlo
                btnAcceso.setOnClickListener {
                    navegarActivityPrincipal()
                }
            }
        } catch (e: SQLiteException) {
            Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navegarActivityPrincipal() {
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish() //Cierra la activity para evitar volver a ella
    }

    private fun cargarRutinas() {
        rutinaDb.addRutina(Rutina("Pecho", 90, 1, 40, "Lunes"))
        rutinaDb.addRutina(Rutina("Espalda", 90, 1, 40, "Miércoles"))
        rutinaDb.addRutina(Rutina("Pierna", 90, 1, 40, "Viernes"))

        rutinaEjercicioDb.addEjercicioARutina(1, 10, 12, 4, 75.0)
        rutinaEjercicioDb.addEjercicioARutina(1, 14, 12, 4, 22.5)
        rutinaEjercicioDb.addEjercicioARutina(1, 2, 12, 4, 12.5)
        rutinaEjercicioDb.addEjercicioARutina(1, 1, 12, 4, 20.0)
        rutinaEjercicioDb.addEjercicioARutina(1, 47, 12, 4, 7.5)
        rutinaEjercicioDb.addEjercicioARutina(1, 52, 12, 4, 0.0)
        rutinaEjercicioDb.addEjercicioARutina(1, 48, 12, 4, 20.0)

        rutinaEjercicioDb.addEjercicioARutina(2, 20, 12, 4, 0.0)
        rutinaEjercicioDb.addEjercicioARutina(2, 22, 12, 4, 80.0)
        rutinaEjercicioDb.addEjercicioARutina(2, 19, 12, 4, 60.0)
        rutinaEjercicioDb.addEjercicioARutina(2, 28, 12, 4, 50.0)
        rutinaEjercicioDb.addEjercicioARutina(2, 43, 12, 4, 15.0)
        rutinaEjercicioDb.addEjercicioARutina(2, 39, 12, 4, 15.0)
        rutinaEjercicioDb.addEjercicioARutina(2, 38, 12, 4, 25.0)


        rutinaEjercicioDb.addEjercicioARutina(3, 116, 12, 4, 80.0)
        rutinaEjercicioDb.addEjercicioARutina(3, 114, 12, 4, 40.0)
        rutinaEjercicioDb.addEjercicioARutina(3, 120, 30, 4, 20.0)
        rutinaEjercicioDb.addEjercicioARutina(3, 123, 12, 4, 180.0)
        rutinaEjercicioDb.addEjercicioARutina(3, 125, 12, 4, 40.0)
        rutinaEjercicioDb.addEjercicioARutina(3, 122, 12, 4, 90.0)


    }

    //Funcíon para insertar las tres dietas ejemplo
    private fun cargarDietas() {
        val dieta1 = Dieta(getString(R.string.dieta_d_ficit), 0, "dieta_deficit")
        val dieta2 = Dieta(getString(R.string.dieta_mantenimiento), 1, "dieta_mantenimiento")
        val dieta3 = Dieta(getString(R.string.dieta_volum_n), 2, "dieta_volumen")
        dietaDb.addDieta(dieta1)
        dietaDb.addDieta(dieta2)
        dietaDb.addDieta(dieta3)
    }

    //Función para cargar los ejercicios de una categoría
    private fun crearObjetosEjercico(
        categoria: String, ejercicios: Array<String>, videos: Array<String>
    ) {
        //Por cada ejercicio recoge el video por su mismo indice y lo añade a la base de datos
        ejercicios.forEachIndexed { indice, ejercicio ->
            val videoId = videos.getOrNull(indice) ?: ""
            ejerciciosDb.addEjercicio(
                Ejercicio(
                    categoria = categoria, nombre = ejercicio, video = videoId
                )
            )
        }
    }

    private fun cargarTablaEjercicios(
        resources: Resources, categoriasEjercicios: Map<Int, Pair<Int, Int>>
    ) {/*Crea la tabla a través de las posiciones de los ejercicios en el mapeo,es decir,recoge mismo indice para el nombre del
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
        usersDb = UserDb(db)
        ejerciciosDb = EjerciciosDb(db)
        rutinaDb = RutinaDb(db)
        dietaDb = DietaDb(db)
        rutinaEjercicioDb = RutinaEjercicioDb(db)
    }

    private lateinit var rutinaDb: RutinaDb
    private lateinit var btnAcceso: Button
    private lateinit var db: DatabaseHelper
    private lateinit var rutinaEjercicioDb: RutinaEjercicioDb
    private lateinit var usersDb: UserDb
    private lateinit var ejerciciosDb: EjerciciosDb
    private lateinit var dietaDb: DietaDb
}