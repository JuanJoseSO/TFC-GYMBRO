@file:Suppress("DEPRECATION")

package com.example.tfc.entrenamiento

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.tfc.ActivityPrincipal
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Cronometro
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.clasesAuxiliares.clasesBase.Sesion
import com.example.tfc.clasesAuxiliares.clasesBase.Usuario
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.HistorialDb
import com.example.tfc.sqlite.sqliteMetodos.RutinaDb
import com.example.tfc.sqlite.sqliteMetodos.EntrenamientoDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class ActivityEntrenamiento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento)
        initComponentes()
        initCronometro()
        initListeners()
    }

    /*Aqui empieza lo más complicado de la aplicacion donde manejamos toda la logica de como realizamos nuestra rutina de entrenamiento
      con varias consultas a las distintas tablas,guardando datos en tambien de historial y permitiendo al usuario cambiar entre una lista
      de ejercicios con sus pesos,repeticiones y series,con un cronometro y un contador de descanso*/
    private fun initEjercicios() {
        //Iniciamos la lista ejercicios y variables necesarias
        posicionEjercicioActual = 0
        serieActual = 0
        listaEjercicio = rutina.id.let { rutinaEjericicoDb.getEjerciciosPorRutina(userDb.getUsuarioSeleccionado()!!.id,it) }
        if (listaEjercicio.isNotEmpty()) {
            cargarEjercicio(posicionEjercicioActual)
        }
    }

    private fun cargarEjercicio(idEjercicio: Int) {
        ejercicio = listaEjercicio[idEjercicio]
        //Asignamos el nombre y el video
        ejercicio.nombre.also { tvNombreEjercicio.text = it }
        setVideo(ejercicio.video)/*Expresion lambda bastate compleja,estamos recuperando la informacion de cada ejercicio en una variable pasandole el id de la rutina y el id del ejercicio
          al método getInfoRutina que se escuentra en la clase RutinaEjericicoDb,aunque compleja,la formatea el propio IDE a partir de una expresión más larga */
        val infoEjercicio =
            rutina.let { ejercicio.let { it1 -> rutinaEjericicoDb.getInfoRutina(userDb.getUsuarioSeleccionado()!!.id,it.id, it1.id) } }
        tvRepeticiones.text = infoEjercicio[1].toInt().toString()
        peso = (infoEjercicio[2])
        setPeso()
        val totalSeries = recogerSeriesTotales()
        // Restablece la serie actual al cargar un nuevo ejercicio
        serieActual = 1
        tvSeries.text = getString(R.string.serie_info, serieActual, totalSeries)
    }

    private fun secuenciaUI() {
        //Cargamos tanto la cuenta de descanso por ejercicio
        clDescanso.visibility = View.VISIBLE
        initCuentaAtras()
        //De nuevo una expresion lambda similar a la anterior,que formatea el propio IDE
        val totalSeries = recogerSeriesTotales()

        //Si serie actual es la ultima,mostramos una View con un mensaje que dura dos segundos en pantalla
        if (serieActual == totalSeries - 1) {
            lyUltimo.visibility = View.VISIBLE
            //Creamos de nuevo un handler para que el mensaje solo se muestre x segundos,tres en este caso
            Handler(Looper.getMainLooper()).postDelayed({
                lyUltimo.visibility = View.GONE
            }, 2000)
        }

        //Si la serie actual es menor que la serie total,aumentamos la serie
        if (serieActual < totalSeries) {
            serieActual++
            tvSeries.text = getString(R.string.serie_info, serieActual, totalSeries)
            rutinaEjericicoDb.updatePeso(userDb.getUsuarioSeleccionado()!!.id,rutina.id, ejercicio.id, peso)

        }
        //Si no,es decir,llegamos al limite,pasamos al siguiente ejercicio
        else if (posicionEjercicioActual < listaEjercicio.size - 1) {
            posicionEjercicioActual++
            btnSiguiente.text = resources.getText(R.string.siguiente_ejercicio)
            cargarEjercicio(posicionEjercicioActual)
        }
        //Si no,vamos al resumen de la rutina
        else {
            val idSession = guardarHistorial()
            val intent = Intent(this, ActivityPrincipal::class.java)
            intent.putExtra("cargarHistorial", true)
            intent.putExtra("idHistorial", idSession)
            startActivity(intent)
            finish() //Cierra la activity para evitar volver a ella
        }
    }

    //Función que recoge la ruta del video del ejercicio seleccionado y le da formato
    @SuppressLint("SetJavaScriptEnabled")
    private fun setVideo(ruta: String?) {
        //String que consigura el video con su enlace
        val video =
            "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$ruta\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        wvReproductor.loadData(video, "text/html", "utf-8")
        wvReproductor.settings.javaScriptEnabled =
            true //Habilita javaScript para reproducir el video
        wvReproductor.settings.mixedContentMode =
            WebSettings.MIXED_CONTENT_ALWAYS_ALLOW//Mejora el rendimietno del reproductor
        //Fortalecemos la seguridad de la aplicación capando el acceso a ella desde internet
        wvReproductor.settings.domStorageEnabled =
            false //Evita el DOM y cookies de las paginas cargadas en el reproductor
        wvReproductor.settings.databaseEnabled =
            false //Evita el acceso a la base de datos de las paginas cargadas en el reproductor
        wvReproductor.settings.allowFileAccess =
            false //Evita el acceso a los archivos del dispositivo de las paginas cargadas en el reproductor
        wvReproductor.webChromeClient = WebChromeClient()
    }


    //Obtenemos la rutina de la lista anteriror seleccionada
    private fun initCronometro() {
        cronometro = Cronometro()
        //Usamos los metodos de cronometro para inicarlo,la logica de start,play y pause la manejamos en la funcion initListeners
        cronometro.setOnUpdateCronometro { tiempo ->
            runOnUiThread {
                tvCronometro.text = tiempo
            }
        }
        if (!cronometro.estaIniciado()) {
            cronometro.iniciar()
        }
    }

    private fun initCuentaAtras() {
        //Si no hay contador,es null,permite iniciar la cuenta atras,para que no se reinicie cada vez que cambiamos de ejercicio
        if (cuentaAtras == null) {
            cuentaAtras = Cronometro().apply {
                setOnUpdateCuentaAtras { tiempo ->
                    runOnUiThread {
                        tvCuentaAtras.text = tiempo
                    }
                }
                terminarCuentaAtras {
                    cuentaAtras=null
                }
                // Iniciar el cronómetro con el valor de descanso configurado
                initCuentaAtras(rutina.descanso, clDescanso)
            }
        }
    }

    //Solo hago un Setter para el peso por que el resto de parametros se presuponen dinámicos
    private fun setPeso() {
        tvPeso.text = peso.toString()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //Creamos un dialog y evitamos que se cierre hasta que el usuario tome una opción
        val builder=AlertDialog.Builder(this)
        val dialog=builder.create()
        dialog.setCancelable(false)
        //Nuevo alerdialog para evitar que el usuario salga sin querrer
        builder.setTitle("¿Estas seguro?")
            .setMessage("Si no terminas la rutina no se guardará el progreso.")
            .setPositiveButton("Sí") { _, _ ->
                super.onBackPressed()
            }.setNegativeButton("No", null).show()
    }


    private fun initListeners() {
        //Boton para ocultar/mostrar el video y dejar la interfaz mas limpia
        btnOcultar.setOnClickListener {
            if (wvReproductor.visibility == View.VISIBLE) wvReproductor.visibility = View.GONE
            else wvReproductor.visibility = View.VISIBLE
        }

        //Boton que gestiona la lista de ejercicios mostrando el tiempo de descanso entre ellos y pasando a la siguiente repeticion/ejercicio
        btnSiguiente.setOnClickListener {
            secuenciaUI()
        }

        btnStart.setOnClickListener {
           logicaCronometro()
        }

        btnSumarPeso.setOnClickListener {
            peso += 2.5
            setPeso()
        }
        btnRestarPeso.setOnClickListener {
            if (peso > 0) peso -= 2.5
            setPeso()
        }
    }
    private fun logicaCronometro(){
        /*setImageResource no funcionaba como debia por algún motivo,aqui simplemente llamamos a los métodos del cronometro y cambiamos la imagen pause/play
           segun nos convenga*/
        if (!cronometro.estaIniciado()) {
            cronometro.iniciar()
            btnStart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
        } else if (cronometro.estaPausado()) {
            cronometro.reanudar()
            btnStart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
        } else {
            cronometro.pausar()
            btnStart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play))
        }
    }

    private fun guardarHistorial(): Int {
        val fechaActual = LocalDate.now().toString()
        val tiempoEntrenamiento = tvCronometro.text.toString()
        val calorias = (tiempoEntrenamiento.split(":")[0].toInt() * 7.4).toInt()

        return historialDb.addSesion(
            Sesion(
                rutina.id, user.id, fechaActual, horaInicio, tiempoEntrenamiento, calorias
            )
        )
    }
    private fun recogerSeriesTotales() : Int{
        return listaEjercicio[posicionEjercicioActual].let {
            rutina.id.let { it1 ->
                it.let { it2 ->
                    rutinaEjericicoDb.getInfoRutina(userDb.getUsuarioSeleccionado()!!.id,it1, it2.id)[0].toInt()
                }
            }
        }
    }

    private fun initComponentes() {
        tvCronometro = findViewById(R.id.tvCronometro)
        tvNombreEjercicio = findViewById(R.id.tvNombreEjercicio)
        btnOcultar = findViewById(R.id.btnOcultar)
        wvReproductor = findViewById(R.id.wvReproductor)
        tvSeries = findViewById(R.id.tvSerieIniciada)
        tvRepeticiones = findViewById(R.id.tvRepeticiones)
        btnRestarPeso = findViewById(R.id.btnRestarPeso)
        tvPeso = findViewById(R.id.tvPeso)
        btnSumarPeso = findViewById(R.id.btnSumarPeso)
        btnStart = findViewById(R.id.btnStart)
        btnSiguiente = findViewById(R.id.btnSiguiente)
        tvCuentaAtras = findViewById(R.id.tvCuentaAtras)
        lyUltimo = findViewById(R.id.lyUltimo)
        clDescanso = findViewById(R.id.clDescanso)
        //Inicio aquí la hora para tenerla desde el principio de la aplicación
        horaInicio = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        db = DatabaseHelper(this)
        rutinaEjericicoDb = EntrenamientoDb(db)
        rutinaDb = RutinaDb(db)
        userDb = UserDb(db)
        historialDb = HistorialDb(db)
        user = userDb.getUsuarioSeleccionado()!!
        rutina = rutinaDb.getRutina(intent.getIntExtra("idRutina", -1))!!
        //De nuevo una expresion lambda similar a la anterior,que formatea el propio IDE
        initEjercicios()
    }

    private lateinit var clDescanso: ConstraintLayout
    private lateinit var tvCronometro: TextView
    private lateinit var tvNombreEjercicio: TextView
    private lateinit var btnOcultar: ImageButton
    private lateinit var wvReproductor: WebView
    private lateinit var tvSeries: TextView
    private lateinit var tvRepeticiones: TextView
    private lateinit var tvPeso: TextView
    private lateinit var btnRestarPeso: FloatingActionButton
    private lateinit var btnSumarPeso: FloatingActionButton
    private lateinit var btnStart: AppCompatImageButton
    private lateinit var btnSiguiente: AppCompatButton
    private lateinit var tvCuentaAtras: TextView
    private lateinit var cronometro: Cronometro
    private lateinit var lyUltimo: LinearLayout
    private lateinit var horaInicio: String
    private lateinit var db: DatabaseHelper
    private lateinit var userDb: UserDb
    private lateinit var historialDb: HistorialDb
    private lateinit var rutinaEjericicoDb: EntrenamientoDb
    private lateinit var rutinaDb: RutinaDb
    private lateinit var listaEjercicio: List<Ejercicio>
    private lateinit var rutina: Rutina
    private lateinit var ejercicio: Ejercicio
    private lateinit var user: Usuario
    private var cuentaAtras: Cronometro?=null
    private var serieActual = 0
    private var posicionEjercicioActual = 0
    private var peso = 0.0
}