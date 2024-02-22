package com.example.tfc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.clasesAuxiliares.Usuario
import com.example.tfc.sqlite.UserDb
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
        viewHombre.setOnClickListener {
            //Selecciona masculino y cambia el color (selección por defecto)
            isMaleSelected = true
            isFemaleSelected = false
            setColorGenero()
        }
        viewMujer.setOnClickListener {
            //Selecciona femenino y cambia el color
            isMaleSelected = false
            isFemaleSelected = true
            setColorGenero()
        }

        rsAltura.addOnChangeListener { _, value, _ ->
            //Actualiza el campo de texto de altura con el valor seleccionado al deslizar la barra
            val df = DecimalFormat("#.##")
            df.format(value).toInt().also { this.alturaActual = it }
            tvAltura.text = "$alturaActual cm"
        }

        botonSumarPeso.setOnClickListener{
            pesoActual++
            setPeso()

        }
        botonRestarPeso.setOnClickListener{
            pesoActual--
            setPeso()
        }
        botonSumarEdad.setOnClickListener{
            edadActual++
            setEdad()
        }
        botonRestarEdad.setOnClickListener{
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
                    isMaleSelected
                )
                try{
                    usersDb.addUsuario(usuario)
                    Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                    navegarActivityPrincipal()
                }catch (e: Exception) {
                    Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navegarActivityPrincipal(){
        val intent= Intent(this,ActivityPrincipal::class.java)
        startActivity(intent)
    }

    //Funciones SETTER/GETTER con seguridad de parametros
    private fun calcularIMC():Double{
        //Hay que realizar esto por que rompia la aplicación,asi que nos aseguramos de darle un formato y lo cambiamos luego

        val df= DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
        val imc=pesoActual/(alturaActual.toDouble()/100*alturaActual.toDouble()/100)
        val imcFormateado = df.format(imc).replace(",",".")

        return imcFormateado.toDouble()
    }

    private fun setEdad(){
        if(edadActual>0) {
            tvEdad.text = edadActual.toString()
        }else{
            Toast.makeText(this, "La edad no puede ser menor que 0", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setPeso(){
        if(pesoActual>0) {
            tvPeso.text=pesoActual.toString()
        }else{
            Toast.makeText(this, "La edad no puede ser menor que 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setColorGenero() {
        viewHombre.setCardBackgroundColor(getColorFondo(isMaleSelected)) // Configura el color de fondo del componente masculino
        viewMujer.setCardBackgroundColor(getColorFondo(isFemaleSelected)) // Configura el color de fondo del componente femenino
    }

    private fun getColorFondo(componenteSeleccionado: Boolean): Int {
        return if (componenteSeleccionado) {
            ContextCompat.getColor(this, R.color.background_component_selected) // Obtiene el color de fondo seleccionado
        } else {
            ContextCompat.getColor(this, R.color.background_component) // Obtiene el color de fondo no seleccionado
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
        viewHombre = findViewById(R.id.viewHombre) // Recupera la vista masculina por su ID
        viewMujer = findViewById(R.id.viewMujer) // Recupera la vista femenina por su ID
        tvAltura = findViewById(R.id.tvAltura) // Recupera el campo de texto de altura por su ID
        rsAltura = findViewById(R.id.rsAltura) // Recupera el control deslizante de altura por su ID
        botonSumarEdad=findViewById(R.id.botonSumarEdad)
        botonRestarEdad=findViewById(R.id.botonRestarEdad)
        botonRestarPeso=findViewById(R.id.btnRestarPeso)
        botonSumarPeso=findViewById(R.id.btnSumarPeso)
        tvPeso=findViewById(R.id.tvPeso)
        tvEdad=findViewById(R.id.tvEdad)
        btnCrearUser=findViewById(R.id.btnCrearUser)
        nombre=findViewById(R.id.etNombreUsuario)
    }

    private lateinit var viewHombre: CardView
    private lateinit var viewMujer: CardView
    private var pesoActual:Int=70//Weight peso
    private var edadActual:Int=20
    private var alturaActual=120
    private var isMaleSelected: Boolean = true //El género masculino está seleccionado
    private var isFemaleSelected: Boolean = false //El género femenino no está seleccionado
    private lateinit var tvAltura: TextView
    private lateinit var rsAltura: RangeSlider
    private lateinit var botonSumarEdad: FloatingActionButton
    private lateinit var botonRestarEdad: FloatingActionButton
    private lateinit var botonRestarPeso: FloatingActionButton
    private lateinit var botonSumarPeso: FloatingActionButton
    private lateinit var tvPeso: TextView
    private lateinit var tvEdad: TextView
    private lateinit var btnCrearUser: AppCompatButton
    private lateinit var nombre: EditText
    private val usersDb = UserDb(DatabaseHelper(this))

}
