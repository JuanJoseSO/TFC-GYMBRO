package com.example.tfc.entrenamiento

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tfc.R
import com.example.tfc.sqlite.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityInfoEjercicios : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_ejercicios)

        initComponentes()
        initListeners()
    }


        private fun initListeners() {
            //Listeners para sumar o restar según el parámetro (por parejas)
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
            tvNumPeso.text=pesoInicial.toString()
        }

    //Funciones para aumentar o reducir los campos seleccionados
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
    //Función que recoge el ejerccio seleccionado de FragmentEjercicios y muestra el nombre y el video relacionado
    private fun infoEjercicio(){
        val ejercicio=db.getEjercicio(intent.getIntExtra("ejercicio",-1))
        tvEjercicio.text = ejercicio?.nombre
        setVideo(ejercicio?.video)
    }

    //Función que recoge la ruta del video del ejercicio seleccionado y le da formato
    @SuppressLint("SetJavaScriptEnabled")
    private fun setVideo(ruta:String?){
        //String que consigura el video con su enlace
        val video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$ruta\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        vvReproductor.loadData(video,"text/html","utf-8")
        vvReproductor.settings.javaScriptEnabled=true //Habilita javaScript para reproducir el video
        vvReproductor.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW//Mejora el rendimietno del reproductor
        //Fortalecemos la seguridad de la aplicación capando el acceso a ella desde internet
        vvReproductor.settings.domStorageEnabled = false //Evita el DOM y cookies de las paginas cargadas en el reproductor
        vvReproductor.settings.databaseEnabled = false //Evita el acceso a la base de datos de las paginas cargadas en el reproductor
        vvReproductor.settings.allowFileAccess = false //Evita el acceso a los archivos del dispositivo de las paginas cargadas en el reproductor
        vvReproductor.webChromeClient= WebChromeClient()
    }

    private fun initComponentes() {
        tvEjercicio =findViewById(R.id.tvEjercicio)
        tvRepeticiones = findViewById(R.id.tvRepeticiones)
        botonSumarRepeticiones =findViewById(R.id.btnSumarTiempo)
        tvNumRepeticiones = findViewById(R.id.tvResultadoTiempo)
        botonRestarRepeticiones = findViewById(R.id.btnRestarRepIntensidad)
        botonSumarSeries = findViewById(R.id.btnSumarSeries)
        tvSeries = findViewById(R.id.tvSeries)
        botonRestarSeries = findViewById(R.id.btnRestarSeries)
        tvPeso = findViewById(R.id.tvPeso)
        tvNumSeries = findViewById(R.id.tvNumSeries)
        botonSumarPeso = findViewById(R.id.btnSumarPeso)
        botonRestarPeso = findViewById(R.id.btnRestarPeso)
        tvNumPeso = findViewById(R.id.tvNumPeso)
        vvReproductor = findViewById(R.id.vvReproductor)
        btnAnadir = findViewById(R.id.btnAnadirRutina)
        tvNumPeso.text=pesoInicial.toString()
        tvNumRepeticiones.text=repeticiones.toString()
        tvNumPeso.text=pesoInicial.toString()
        tvNumSeries.text=series.toString()
        infoEjercicio()
    }

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
    private var repeticiones= 4
    private var series= 4
    private var pesoInicial= 20.0
    private val db = DatabaseHelper(this)
}


