package com.example.tfc.entrenamiento

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.tfc.ActivityPrincipal
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.RutinaDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb
import com.example.tfc.sqlite.sqliteMetodos.UsuarioRutinaDb
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityCrearRutina : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        initComponentes()
        initListeners()
    }

    private fun initListeners() {
        //Listeners para sumar o restar según el parámetro (por parejas)
        btnSumarTiempo.setOnClickListener {
            tiempoInicial++
            setTiempo()
        }
        btnRestarTiempo.setOnClickListener {
            tiempoInicial--
            setTiempo()
        }

        btnSumarIntensidad.setOnClickListener {
            if (nivel < niveles.size - 1) {
                nivel++
                setIntensidad()
            }
        }

        btnRestarIntensidad.setOnClickListener {
            if (nivel > 0) {
                nivel--
                setIntensidad()
            }
        }

        btnSumarDia.setOnClickListener {
            if (dia < dias.size - 1) {
                dia++
                setDia()
            }
        }

        btnRestarDia.setOnClickListener {
            if (dia > 0) {
                dia--
                setDia()
            }
        }
        btnSumarDescanso.setOnClickListener {
            descansoInicial++
            setDescanso()
        }
        btnRestarDescanso.setOnClickListener {
            if (descansoInicial > 0) {
                descansoInicial--
                setDescanso()
            }
        }


        btnCrearRutina.setOnClickListener {
            //Si el nombre está vacio
            if (etNombreRutina.text.toString().isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese un nombre válido", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val rutina = crearRutina()

                try {
                    //Como se explica en rutinaDB añadir rutina devuelve un Long con el id,para añadirlo a la tabla usuario_rutina
                    usuarioRutinaDb.addRutinaAUsuario(
                        rutinaDb.addRutina(rutina), userDb.getUsuarioSeleccionado()?.id!!
                    )
                    Toast.makeText(this, "Rutina creada correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityPrincipal::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al crear la rutina", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun crearRutina(): Rutina {
        val intensidad = when (tvResultadoIntensidad.text.toString()) {
            "Baja" -> 0
            "Media" -> 1
            "Alta" -> 2
            else -> 3
        }
        //Si no creamos el objeto rutina y lo añadimos a la base de datos
        val rutina = Rutina(
            etNombreRutina.text.toString(),
            tiempoInicial,
            intensidad,
            descansoInicial,
            tvResultadoDia.text.toString()
        )
        return rutina
    }

    //Funciones para simplificar los listeners anteriores
    private fun setDia() {
        tvResultadoDia.text = dias[dia]
    }

    private fun setIntensidad() {
        tvResultadoIntensidad.text = niveles[nivel]
    }

    private fun setTiempo() {
        tvResultadoTiempo.text = tiempoInicial.toString()
    }

    private fun setDescanso() {
        tvResultadoDescanso.text = descansoInicial.toString()
    }

    private fun initComponentes() {
        etNombreRutina = findViewById(R.id.etNombreRutina)
        btnSumarTiempo = findViewById(R.id.btnSumarTiempo)
        tvResultadoTiempo = findViewById(R.id.tvResultadoTiempo)
        btnRestarTiempo = findViewById(R.id.btnRestarTiempo)
        btnSumarIntensidad = findViewById(R.id.btnSumarIntensidad)
        tvResultadoIntensidad = findViewById(R.id.tvResultadoIntensidad)
        btnRestarIntensidad = findViewById(R.id.btnRestarIntensidad)
        btnSumarDia = findViewById(R.id.btnSumarDia)
        tvResultadoDia = findViewById(R.id.tvResultadoDia)
        btnRestarDia = findViewById(R.id.btnRestarDia)
        btnCrearRutina = findViewById(R.id.btnCrearRutina)
        tvResultadoDescanso = findViewById(R.id.tvResultadoDescanso)
        btnSumarDescanso = findViewById(R.id.btnSumarDescanso)
        btnRestarDescanso = findViewById(R.id.btnRestarDescanso)
        db = DatabaseHelper(this)
        rutinaDb = RutinaDb(db)
        usuarioRutinaDb = UsuarioRutinaDb(db)
        userDb = UserDb(db)
        tvResultadoTiempo.text = tiempoInicial.toString()
        tvResultadoIntensidad.text = niveles[nivel]
        tvResultadoDescanso.text = descansoInicial.toString()
        tvResultadoDia.text = dias[dia]
    }

    private lateinit var etNombreRutina: EditText
    private lateinit var btnSumarTiempo: FloatingActionButton
    private lateinit var tvResultadoTiempo: TextView
    private lateinit var btnRestarTiempo: FloatingActionButton
    private lateinit var btnSumarIntensidad: FloatingActionButton
    private lateinit var tvResultadoIntensidad: TextView
    private lateinit var btnRestarIntensidad: FloatingActionButton
    private lateinit var btnSumarDia: FloatingActionButton
    private lateinit var tvResultadoDia: TextView
    private lateinit var btnRestarDia: FloatingActionButton
    private lateinit var tvResultadoDescanso: TextView
    private lateinit var btnRestarDescanso: FloatingActionButton
    private lateinit var btnSumarDescanso: FloatingActionButton
    private lateinit var btnCrearRutina: AppCompatButton
    private lateinit var rutinaDb: RutinaDb
    private lateinit var usuarioRutinaDb: UsuarioRutinaDb
    private lateinit var db: DatabaseHelper
    private lateinit var userDb: UserDb
    private var descansoInicial = 30
    private var tiempoInicial = 60
    private val niveles: Array<String> = arrayOf("Baja", "Media", "Alta")
    private var nivel: Int = 1
    private val dias: Array<String> =
        arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private var dia: Int = 0
}