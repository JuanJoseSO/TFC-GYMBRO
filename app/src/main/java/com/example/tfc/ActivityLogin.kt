package com.example.tfc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.tfc.SQLite.DatabaseHelper
import com.example.tfc.clasesAuxiliares.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.RangeSlider
import java.text.DecimalFormat

class ActivityLogin : AppCompatActivity() {

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
    private lateinit var btnCrearUser: Button
    private lateinit var nombre: EditText

    private val db = DatabaseHelper(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        iniciaComponentes()
        iniciaListeners()
        initUI()

    }

    private fun iniciaComponentes() {
        viewHombre = findViewById(R.id.viewHombre) // Recupera la vista masculina por su ID
        viewMujer = findViewById(R.id.viewMujer) // Recupera la vista femenina por su ID
        tvAltura = findViewById(R.id.tvAltura) // Recupera el campo de texto de altura por su ID
        rsAltura = findViewById(R.id.rsAltura) // Recupera el control deslizante de altura por su ID
        botonSumarEdad=findViewById(R.id.botonSumarEdad)
        botonRestarEdad=findViewById(R.id.botonRestarEdad)
        botonRestarPeso=findViewById(R.id.botonRestarPeso)
        botonSumarPeso=findViewById(R.id.botonSumarPeso)
        tvPeso=findViewById(R.id.tvPeso)
        tvEdad=findViewById(R.id.tvEdad)
        btnCrearUser=findViewById(R.id.btnCrearUser)
        nombre=findViewById(R.id.etNombreUsuario)
    }

    private fun iniciaListeners() {
        viewHombre.setOnClickListener {
            isMaleSelected = true
            isFemaleSelected = false//Actualiza la selección de género al hacer clic en el componente masculino
            setColorGenero() //Actualiza el color de fondo del componente
        }
        viewMujer.setOnClickListener {
            isMaleSelected = false
            isFemaleSelected = true //Actualiza la selección de género al hacer clic en el componente femenino
            setColorGenero() //Actualiza el color de fondo del componente
        }

        rsAltura.addOnChangeListener { slider, value, fromUser ->
            val df = DecimalFormat("#.##")
            alturaActual = df.format(value).toInt()
            tvAltura.text = "$alturaActual cm" //Actualiza el campo de texto de altura con el valor seleccionado
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
            val nombreUsuario=nombre.text.toString()

            if (nombreUsuario.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = Usuario(
                    nombreUsuario,
                    edadActual,
                    pesoActual,
                    alturaActual,
                    calcularIMC(),
                    isMaleSelected
                )

              try{
                   val db=DatabaseHelper(this)
                   db.addUsuario(usuario)
                   Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                   irVentanaPrincipal()
              }catch (e: Exception) {
                  Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
              }
            }
        }

    }

    private fun irVentanaPrincipal(){
        val intent= Intent(this,ActivityPrincipal::class.java)
        startActivity(intent)
    }

    private fun calcularIMC():Double{
        val df= DecimalFormat("#.##")
        val imc=pesoActual/(alturaActual.toDouble()/100*alturaActual.toDouble()/100)
        return df.format(imc).toDouble()
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
        super.onDestroy()
        db.close() // Asumiendo que `db` es accesible a nivel de clase y su ciclo de vida está bien gestionado
    }
}
