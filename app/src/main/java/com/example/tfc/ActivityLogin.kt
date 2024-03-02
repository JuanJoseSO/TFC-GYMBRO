package com.example.tfc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
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
        modificarUsuario(intent.getBooleanExtra("Modificar",false))
        iniciaListeners()
        initUI()
    }
    private fun modificarUsuario(modificar: Boolean){
        if(modificar){
            val usuario= userDb.getUsuarioSeleccionado()!!
            alturaActual= usuario.altura
            setAltura()
            pesoActual= usuario.peso
            setPeso()
            objetivo= usuario.objetivoDiario
            setObjetivo()
            val genero=usuario.genero
            if(genero){
                generoM = true
                generoF = false
                setColorGenero()
            }else{
                generoM = false
                generoF = true
                setColorGenero()
            }

            nombre.text= Editable.Factory.getInstance().newEditable(usuario.nombreUsuario)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun iniciaListeners() {
        //Listener para seleccionar u aumentar/reducir según el componenete y guardar el usuario en la base de datos
        cvMasculino.setOnClickListener {
            generoM = true
            generoF = false//Actualiza la selección de género al hacer clic en el componente masculino
            setColorGenero() //Actualiza el color de fondo del componente
        }
        cvFemenino.setOnClickListener {
            generoM = false
            generoF = true //Actualiza la selección de género al hacer clic en el componente femenino
            setColorGenero() //Actualiza el color de fondo del componente
        }

        rsAltura.addOnChangeListener { _, value, _ ->
            //Actualiza el campo de texto de altura con el valor seleccionado al deslizar la barra
            val df = DecimalFormat("#.##")
            df.format(value).toInt().also { this.alturaActual = it }
            setAltura()
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
            objetivo++
            setObjetivo()
        }
        btnRestarEdad.setOnClickListener {
            objetivo--
            setObjetivo()
        }

        btnCrearUser.setOnClickListener {
            //Creamos el usuario y lo guardamos en la base de datos al pulsar el botón
            if (nombre.text.toString().isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show()
            }
            val usuario = Usuario(
                nombre.text.toString(),
                objetivo,
                pesoActual,
                alturaActual,
                calcularIMC(),
                generoM
            )

             try {
                 if(intent.getBooleanExtra("Modificar",false)) {
                     userDb.getUsuarioSeleccionado()
                         ?.let { it1 -> userDb.updateUsuario(it1.id,usuario) }
                 }else {
                     val idUser = userDb.addUsuario(usuario)
                     setRutinasPredefinidas(idUser)
                 }
                 navegarActivityPrincipal()
             } catch (e: Exception) {
                 Log.e("Error", "Error al crear el usuario")
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
        finish() //Cierra la activity para evitar volver a ella
    }

    //Funciones SETTER/GETTER con seguridad de parametros
    private fun calcularIMC(): Double {
        //Hay que realizar esto por que rompia la aplicación,asi que nos aseguramos de darle un formato y lo cambiamos luego

        val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
        val imc = pesoActual / (alturaActual.toDouble() / 100 * alturaActual.toDouble() / 100)
        val imcFormateado = df.format(imc).replace(",", ".")

        return imcFormateado.toDouble()
    }

    private fun setObjetivo() {
        if (objetivo > 0)
            tvObjetivoDiaro.text = objetivo.toString()
    }
    private fun setAltura() {
        tvAltura.text = "$alturaActual cm"
    }

    private fun setPeso() {
        if (pesoActual > 0)
            tvPeso.text = pesoActual.toString()
    }

    private fun setColorGenero() {
        cvMasculino.setCardBackgroundColor(getColorFondo(generoM)) // Configura el color de fondo del componente masculino
        cvFemenino.setCardBackgroundColor(getColorFondo(generoF)) // Configura el color de fondo del componente femenino
    }

    private fun getColorFondo(seleccionado: Boolean): Int {
        return if (seleccionado) {
            ContextCompat.getColor(
                this, R.color.background_component_selected
            )
        } else {
            ContextCompat.getColor(
                this, R.color.background_component
            )
        }
    }

    private fun initUI() {
        setColorGenero()
        setObjetivo()
        setPeso()
    }

    override fun onDestroy() {
        DatabaseHelper(this).close()
        super.onDestroy()
    }


    private fun iniciaComponentes() {
        cvMasculino = findViewById(R.id.viewHombre)
        cvFemenino = findViewById(R.id.viewMujer)
        tvAltura = findViewById(R.id.tvAltura)
        rsAltura = findViewById(R.id.rsAltura)
        btnSumarEdad = findViewById(R.id.btnSumarEdad)
        btnRestarEdad = findViewById(R.id.btnRestarEdad)
        btnRestarPeso = findViewById(R.id.btnRestarPeso)
        btnSumarPeso = findViewById(R.id.btnSumarPeso)
        tvPeso = findViewById(R.id.tvPeso)
        tvObjetivoDiaro = findViewById(R.id.tvObjetivoDiario)
        btnCrearUser = findViewById(R.id.btnCrearUser)
        nombre = findViewById(R.id.etNombreUsuario)
        db = DatabaseHelper(this)
        userDb = UserDb(db)
        usuarioRutinaDb = UsuarioRutinaDb(db)
    }

    private lateinit var cvMasculino: CardView
    private lateinit var cvFemenino: CardView
    private var pesoActual: Int = 70//Weight peso
    private var objetivo: Int = 20
    private var alturaActual = 120
    private var generoM: Boolean = true //El género masculino está seleccionado
    private var generoF: Boolean = false //El género femenino no está seleccionado
    private lateinit var tvAltura: TextView
    private lateinit var rsAltura: RangeSlider
    private lateinit var btnSumarEdad: FloatingActionButton
    private lateinit var btnRestarEdad: FloatingActionButton
    private lateinit var btnRestarPeso: FloatingActionButton
    private lateinit var btnSumarPeso: FloatingActionButton
    private lateinit var tvPeso: TextView
    private lateinit var tvObjetivoDiaro: TextView
    private lateinit var btnCrearUser: AppCompatButton
    private lateinit var nombre: EditText
    private lateinit var db: DatabaseHelper
    private lateinit var usuarioRutinaDb: UsuarioRutinaDb
    private lateinit var userDb: UserDb
}