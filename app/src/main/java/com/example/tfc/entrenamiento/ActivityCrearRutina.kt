package com.example.tfc.entrenamiento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.tfc.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityCrearRutina : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        initComponentes()
        initListeners()

    }






    private fun initListeners(){
        //Listeners para sumar o restar según el parámetro (por parejas)
        btnSumarTiempo.setOnClickListener{
            tiempoInicial++
            setTiempo()
        }
        btnRestarTiempo.setOnClickListener{
            tiempoInicial--
            setTiempo()
        }

        btnSumarIntensidad.setOnClickListener{
            if(nivel<niveles.size -1){
                nivel++
                setIntensidad()
            }
        }

        btnRestarIntensidad.setOnClickListener{
            if(nivel>0){
                nivel--
                setIntensidad()
            }
        }


    }

    private fun setIntensidad() {
        tvResultadoIntensidad.text=niveles[nivel]
    }

    private fun setTiempo() {
        tvResultadoTiempo.text=tiempoInicial.toString()
    }

    private fun initComponentes(){
        etNombreRutina=findViewById(R.id.etNombreRutina)
        btnSumarTiempo=findViewById(R.id.btnSumarTiempo)
        tvResultadoTiempo=findViewById(R.id.tvResultadoTiempo)
        btnRestarTiempo=findViewById(R.id.btnRestarTiempo)
        btnSumarIntensidad=findViewById(R.id.btnSumarIntensidad)
        tvResultadoIntensidad=findViewById(R.id.tvResultadoIntensidad)
        btnRestarIntensidad=findViewById(R.id.btnRestarIntensidad)
        btnAnadirRutina=findViewById(R.id.btnAnadirRutina)
        tvResultadoTiempo.text=tiempoInicial.toString()
        tvResultadoIntensidad.text=niveles[nivel]
    }
    private lateinit var etNombreRutina : EditText
    private lateinit var btnSumarTiempo : FloatingActionButton
    private lateinit var tvResultadoTiempo : TextView
    private lateinit var btnRestarTiempo : FloatingActionButton
    private lateinit var btnSumarIntensidad : FloatingActionButton
    private lateinit var tvResultadoIntensidad : TextView
    private lateinit var btnRestarIntensidad : FloatingActionButton
    private lateinit var btnAnadirRutina : AppCompatButton
    private var tiempoInicial=60
    private val niveles : Array<String> = arrayOf("Baja","Media","Alta")
    private var nivel : Int= 1


}