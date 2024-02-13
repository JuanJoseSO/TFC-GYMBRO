package com.example.tfc.Entrenamiento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient

import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tfc.R
import com.example.tfc.SQLite.DatabaseHelper
import com.example.tfc.clasesAuxiliares.Ejercicio
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityInfoEjercicios : AppCompatActivity() {

        private lateinit var tvEjercicio: TextView
        private lateinit var tvRepeticiones: TextView
        private lateinit var botonSumarRepeticiones: FloatingActionButton
        private lateinit var tvNumRepeticiones: TextView
        private lateinit var botonRestarRepeticiones: FloatingActionButton

        private lateinit var tvSeries: TextView
        private lateinit var botonSumarSeries: FloatingActionButton
        private lateinit var tvNumSeries: TextView
        private lateinit var botonRestarSeries: FloatingActionButton

        private lateinit var tvPeso: TextView
        private lateinit var botonSumarPeso: FloatingActionButton
        private lateinit var tvNumPeso: TextView
        private lateinit var botonRestarPeso: FloatingActionButton

        private lateinit var vvReproductor: WebView
        private lateinit var btnAnadir: Button

        private var repeticiones:Int= 4
        private var series:Int= 4
        private var pesoInicial:Double= 20.0

        private val db = DatabaseHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_ejercicios)


        iniciaComponentes()
        iniciaListeners()
        infoEjercicio()
    }

        private fun iniciaComponentes() {
            tvEjercicio =findViewById(R.id.tvEjercicio)
            tvRepeticiones = findViewById(R.id.tvRepeticiones)
            botonSumarRepeticiones =findViewById(R.id.botonSumarRepeticiones)
            tvNumRepeticiones = findViewById(R.id.tvNumRepeticiones)
            botonRestarRepeticiones = findViewById(R.id.botonRestarRepeticiones)
            botonSumarSeries = findViewById(R.id.botonSumarSeries)
            tvSeries = findViewById(R.id.tvSeries)
            botonRestarSeries = findViewById(R.id.botonRestarSeries)
            tvPeso = findViewById(R.id.tvPeso)
            tvNumSeries = findViewById(R.id.tvNumSeries)
            botonSumarPeso = findViewById(R.id.botonSumarPeso)
            botonRestarPeso = findViewById(R.id.botonRestarPeso)
            tvNumPeso = findViewById(R.id.tvNumPeso)
            vvReproductor = findViewById(R.id.vvReproductor)
            btnAnadir = findViewById(R.id.btnAnadir)
        }

        private fun iniciaListeners() {
            botonSumarRepeticiones.setOnClickListener{
                repeticiones++
                setRepeticiones()

            }
            botonRestarRepeticiones.setOnClickListener{
                repeticiones--
                setRepeticiones()

            }


            botonSumarSeries.setOnClickListener{
                series++
                setSeries()

            }
            botonRestarSeries.setOnClickListener{
                series--
                setSeries()

            }

            botonSumarPeso.setOnClickListener{
                pesoInicial+=2.5
                setPeso()

            }
            botonRestarPeso.setOnClickListener{
                pesoInicial-=2.5
                setPeso()
            }
        }

        private fun setPeso(){
            if(pesoInicial>=0) {
                tvNumPeso.text=pesoInicial.toString()
            }else{
                Toast.makeText(this, "No puedes levantar menos de 0 kilos", Toast.LENGTH_SHORT).show()
            }
        }
        private fun setRepeticiones(){
            if(repeticiones>=1) {
                tvNumRepeticiones.text=repeticiones.toString()
            }else{
                Toast.makeText(this, "No puedes hacer menos de una repetición", Toast.LENGTH_SHORT).show()
            }
        }
    private fun setSeries(){
        if(series>=1) {
            tvNumSeries.text=series.toString()
        }else{
            Toast.makeText(this, "No puedes hacer menos de una Serie", Toast.LENGTH_SHORT).show()
        }
    }

    private fun infoEjercicio(){
        val ejercicio=db.getEjercicio(intent.getIntExtra("ejercicio",-1))
        tvEjercicio.text = ejercicio?.nombre
        añadirVideo(ejercicio?.video)
    }

    private fun añadirVideo(ruta:String?){
        val video : String="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+ruta+"\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        vvReproductor.loadData(video,"text/html","utf-8")
        vvReproductor.settings.javaScriptEnabled=true
        vvReproductor.webChromeClient= WebChromeClient()
    }
}


