package com.example.tfc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.tfc.clasesAuxiliares.clasesBase.Usuario
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.UserDb
import com.example.tfc.sqlite.sqliteMetodos.UsuarioRutinaDb
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.RangeSlider
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class ActivityLogin : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        iniciaComponentes()
        iniciaListeners()
        initUI()
    }


    @SuppressLint("SetTextI18n")
    private fun iniciaListeners() {
        //Listener para seleccionar u aumentar/reducir según el componenete y guardar el usuario en la base de datos
        cvMasculino.setOnClickListener {
            //Selecciona masculino y cambia el color (selección por defecto)
            btnGeneroM = true
            btnGeneroF = false
            setColorGenero()
        }
        cvMujer.setOnClickListener {
            //Selecciona femenino y cambia el color
            btnGeneroM = false
            btnGeneroF = true
            setColorGenero()
        }

        rsAltura.addOnChangeListener { _, value, _ ->
            //Actualiza el campo de texto de altura con el valor seleccionado al deslizar la barra
            val df = DecimalFormat("#.##")
            df.format(value).toInt().also { this.alturaActual = it }
            tvAltura.text = "$alturaActual cm"
        }

        btnSumarPeso.setOnClickListener {
            pesoActual++
            setPeso()

        }
        btnRestarPeso.setOnClickListener {
            pesoActual--
            setPeso()
        }
        btnSumarEdad.setOnClickListener {
            edadActual++
            setEdad()
        }
        btnRestarEdad.setOnClickListener {
            edadActual--
            setEdad()
        }

        btnCrearUser.setOnClickListener {
            //Creamos el usuario y lo guardamos en la base de datos al pulsar el botón
            if (nombre.text.toString().isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = Usuario(
                    nombre.text.toString(),
                    edadActual,
                    pesoActual,
                    alturaActual,
                    calcularIMC(),
                    btnGeneroM
                )
                try {
                    val idUser = userDb.addUsuario(usuario)
                    setRutinasPredefinidas(idUser)
                    navegarActivityPrincipal()
                } catch (e: Exception) {
                    Log.e("Error", "Error al crear el usuario")
                }
            }
        }
    }

    private fun setRutinasPredefinidas(id: Int) {
        usuarioRutinaDb.addRutinaAUsuario(1, id)
        usuarioRutinaDb.addRutinaAUsuario(2, id)
        usuarioRutinaDb.addRutinaAUsuario(3, id)
    }

    private fun navegarActivityPrincipal() {
        val intent = Intent(this, ActivityPrincipal::class.java)
        startActivity(intent)
    }

    //Funciones SETTER/GETTER con seguridad de parametros
    private fun calcularIMC(): Double {
        //Hay que realizar esto por que rompia la aplicación,asi que nos aseguramos de darle un formato y lo cambiamos luego

        val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
        val imc = pesoActual / (alturaActual.toDouble() / 100 * alturaActual.toDouble() / 100)
        val imcFormateado = df.format(imc).replace(",", ".")

        return imcFormateado.toDouble()
    }

    private fun setEdad() {
        if (edadActual > 0) {
            tvEdad.text = edadActual.toString()
        } else {
            Toast.makeText(this, "La edad no puede ser menor que 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPeso() {
        if (pesoActual > 0) {
            tvPeso.text = pesoActual.toString()
        } else {
            Toast.makeText(this, "La edad no puede ser menor que 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setColorGenero() {
        cvMasculino.setCardBackgroundColor(getColorFondo(btnGeneroM)) // Configura el color de fondo del componente masculino
        cvMujer.setCardBackgroundColor(getColorFondo(btnGeneroF)) // Configura el color de fondo del componente femenino
    }

    private fun getColorFondo(componenteSeleccionado: Boolean): Int {
        return if (componenteSeleccionado) {
            ContextCompat.getColor(
                this, R.color.background_component_selected
            ) // Obtiene el color de fondo seleccionado
        } else {
            ContextCompat.getColor(
                this, R.color.background_component
            ) // Obtiene el color de fondo no seleccionado
        }
    }

    private fun initUI() {
        setColorGenero()
        setEdad()
        setPeso()
    }

    override fun onDestroy() {
        DatabaseHelper(this).close()
        super.onDestroy()
    }


    private fun iniciaComponentes() {
        cvMasculino = findViewById(R.id.viewHombre)
        cvMujer = findViewById(R.id.viewMujer)
        tvAltura = findViewById(R.id.tvAltura)
        rsAltura = findViewById(R.id.rsAltura)
        btnSumarEdad = findViewById(R.id.btnSumarEdad)
        btnRestarEdad = findViewById(R.id.btnRestarEdad)
        btnRestarPeso = findViewById(R.id.btnRestarPeso)
        btnSumarPeso = findViewById(R.id.btnSumarPeso)
        tvPeso = findViewById(R.id.tvPeso)
        tvEdad = findViewById(R.id.tvEdad)
        btnCrearUser = findViewById(R.id.btnCrearUser)
        nombre = findViewById(R.id.etNombreUsuario)
        db = DatabaseHelper(this)
        userDb = UserDb(db)
        usuarioRutinaDb = UsuarioRutinaDb(db)
    }

    private lateinit var cvMasculino: CardView
    private lateinit var cvMujer: CardView
    private var pesoActual: Int = 70//Weight peso
    private var edadActual: Int = 20
    private var alturaActual = 120
    private var btnGeneroM: Boolean = true //El género masculino está seleccionado
    private var btnGeneroF: Boolean = false //El género femenino no está seleccionado
    private lateinit var tvAltura: TextView
    private lateinit var rsAltura: RangeSlider
    private lateinit var btnSumarEdad: FloatingActionButton
    private lateinit var btnRestarEdad: FloatingActionButton
    private lateinit var btnRestarPeso: FloatingActionButton
    private lateinit var btnSumarPeso: FloatingActionButton
    private lateinit var tvPeso: TextView
    private lateinit var tvEdad: TextView
    private lateinit var btnCrearUser: AppCompatButton
    private lateinit var nombre: EditText
    private lateinit var db: DatabaseHelper
    private lateinit var usuarioRutinaDb: UsuarioRutinaDb
    private lateinit var userDb: UserDb
}