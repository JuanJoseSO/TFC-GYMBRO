package com.example.tfc.entrenamiento

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.tfc.ActivityPrincipal
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.AdapterRutina
import com.example.tfc.clasesAuxiliares.Ejercicio
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.EjerciciosDb
import com.example.tfc.sqlite.RutinaDb
import com.example.tfc.sqlite.RutinaEjercicioDb
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

            btnAnadir.setOnClickListener{
                /*Lo que hacemos en este Listener es abrir un AlertDialog que estamos inflando con el mismo adapter que la lista de
                  rutinas lo que hace que en lugar de ser una lista gris sin estilo sea una lista con un fondo y un formato individual
                  para cada ejercicio,quedando mucho mas atractiva visualmente*/
                if (rutinaDb.getRutinas().isEmpty()) {
                    //Si no hay rutinas no nos permitirá crearlo
                    Toast.makeText(this, "No hay rutinas creadas", Toast.LENGTH_LONG).show()
                } else {
                    //Si hay rutinas
                    val listaRutinas = rutinaDb.getRutinas() //Obtienemos la lista de rutinas
                    val adapter= AdapterRutina(this, listaRutinas) //Creamos el adapter
                    val layoutRutina= LayoutInflater.from(this).inflate(R.layout.fragment_listas,null)//inflamos la rutina
                    layoutRutina.background= ContextCompat.getDrawable(this,R.drawable.background)//Le cambiamos el fondo
                    val lvRutina = layoutRutina.findViewById<ListView>(R.id.listas)//Rlacionamos con nuestro layout
                    lvRutina.adapter=adapter //Relacionamos con el adapter
                    lvRutina.setOnItemClickListener { _, _, position, _ ->
                        //Añadimos a la base de datos el item seleccionado
                        rutinaEjercicioDb.addEjercicioARutina(
                            listaRutinas[position].id,
                            ejercicio.id,
                            repeticiones,
                            series,
                            pesoInicial,
                        )
                        Toast.makeText(this, "Ejercicio añadido", Toast.LENGTH_SHORT).show()
                        //Volvemos a la activityPrincipal
                        val intent = Intent(this, ActivityPrincipal::class.java)
                        startActivity(intent)
                    }
                    //Creamos el Alerdialog y le damos el estilo y un boton atrás
                    val builder = AlertDialog.Builder(this)
                    builder.setView(layoutRutina)
                    builder.setNegativeButton("Atrás"){ dialog, _->dialog.dismiss()}
                    val dialog=builder.create()
                    dialog.show()
                }
            }
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
        ejercicio= ejerciciosDb.getEjercicio(this.intent.getIntExtra("ejercicio",-1))!!
        tvEjercicio.text = ejercicio.nombre
        setVideo(ejercicio.video)
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

    override fun onDestroy(){
        db.close()
        super.onDestroy()
    }

    private fun initComponentes() {
        tvEjercicio =findViewById(R.id.tvEjercicio)
        botonSumarRepeticiones =findViewById(R.id.btnSumarRepeticiones)
        tvNumRepeticiones = findViewById(R.id.tvNumRepeticiones)
        botonRestarRepeticiones = findViewById(R.id.botonRestarRepeticiones)
        botonSumarSeries = findViewById(R.id.botonSumarSeries)
        botonRestarSeries = findViewById(R.id.botonRestarSeries)
        tvNumSeries = findViewById(R.id.tvNumSeries)
        botonSumarPeso = findViewById(R.id.botonSumarPeso)
        botonRestarPeso = findViewById(R.id.botonRestarPeso)
        tvNumPeso = findViewById(R.id.tvNumPeso)
        vvReproductor = findViewById(R.id.vvReproductor)
        btnAnadir = findViewById(R.id.btnAnadirRutina)
        db= DatabaseHelper(this)
        ejerciciosDb = EjerciciosDb(db)
        rutinaDb = RutinaDb(db)
        rutinaEjercicioDb = RutinaEjercicioDb(db)
        tvNumPeso.text=pesoInicial.toString()
        tvNumRepeticiones.text=repeticiones.toString()
        tvNumPeso.text=pesoInicial.toString()
        tvNumSeries.text=series.toString()
        infoEjercicio()
    }

    private lateinit var tvEjercicio: TextView
    private lateinit var botonSumarRepeticiones: FloatingActionButton
    private lateinit var tvNumRepeticiones: TextView
    private lateinit var botonRestarRepeticiones: FloatingActionButton
    private lateinit var botonSumarSeries: FloatingActionButton
    private lateinit var tvNumSeries: TextView
    private lateinit var botonRestarSeries: FloatingActionButton
    private lateinit var botonSumarPeso: FloatingActionButton
    private lateinit var tvNumPeso: TextView
    private lateinit var botonRestarPeso: FloatingActionButton
    private lateinit var vvReproductor: WebView
    private lateinit var btnAnadir: Button
    private lateinit var ejercicio : Ejercicio
    private var repeticiones= 4
    private var series= 4
    private var pesoInicial= 20.0
    private lateinit var db : DatabaseHelper
    private lateinit var ejerciciosDb : EjerciciosDb
    private lateinit var rutinaDb : RutinaDb
    private lateinit var rutinaEjercicioDb : RutinaEjercicioDb
}



