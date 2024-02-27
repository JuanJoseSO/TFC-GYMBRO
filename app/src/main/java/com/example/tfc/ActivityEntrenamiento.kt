package com.example.tfc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.tfc.clasesAuxiliares.Cronometro
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.RutinaDb
import com.example.tfc.sqlite.sqliteMetodos.RutinaEjercicioDb

import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityEntrenamiento : AppCompatActivity() { override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento)

    initComponentes()
    initCronometro()
    initListeners()

    }
    /*Aqui empieza lo más complicado de la aplicacion donde manejamos toda la logica de como realizamos nuestra rutina de entrenamiento
      con varias consultas a las distintas tablas,guardando datos en tambien de historial y permitiendo al usuario cambiar entre una lista
      de ejercicios con sus pesos,repeticiones y series,con un cronometro y un contador de descanso*/
    private fun infoEjercicios(){
        val listaEjercicio= rutina?.id?.let { rutinaEjericicoDb.getEjerciciosPorRutina(it) }
        if (listaEjercicio != null) {
            for(i in listaEjercicio.indices){
                tvNombreEjercicio.text=listaEjercicio[i].nombre
                val infoEjercicio=rutina?.let { rutinaEjericicoDb.getInfoRutina(it.id,listaEjercicio[i].id)}
                tvSeries.text= infoEjercicio?.get(0)?.toInt().toString()
                tvRepeticiones.text= infoEjercicio?.get(1)?.toInt().toString()
                peso= infoEjercicio?.get(2)!!
                setPeso()

            }
        }
    }

    //Obtenemos la rutina de la lista anteriror seleccionada
    private fun initCronometro(){
        cronometro= Cronometro()
        //Usamos los metodos de cronometro para inicarlo,la logica de start,play y pause la manejamos en la funcion initListeners
        cronometro.setOnUpdateCronometro { tiempo->
            runOnUiThread{
                tvCronometro.text=tiempo
            }
        }
        if(!cronometro.estaIniciado()){
            cronometro.iniciar()
        }
    }
    private fun initCuentaAtras(){
        cuentaAtras= Cronometro()
        //Similar a inicar cronometro pero usando los metodos de la cuentaAtras
        cuentaAtras.setOnUpdateCuentaAtras { tiempo->
            runOnUiThread{
                tvCuentaAtras.text=tiempo
            }
        }
        //Asignamos el descanso que hemos configurado al crear la rutina a la cuenta atras
        rutina?.descanso?.let { cuentaAtras.initCuentaAtras(it,tvCuentaAtras) }
    }


    //Solo hago un Setter para el peso por que el resto de parametros se presuponen dinámicos
    private fun setPeso(){
        tvPeso.text= peso.toString()
    }

    private fun initListeners() {
        //Boton para ocultar/mostrar el video y dejar la interfaz mas limpia
        btnOcultar.setOnClickListener {
            if(wvReproductor.visibility== View.VISIBLE)
                wvReproductor.visibility= View.GONE
            else
                wvReproductor.visibility= View.VISIBLE
        }

        //Boton que gestiona la lista de ejercicios mostrando el tiempo de descanso entre ellos y pasando a la siguiente repeticion/ejercicio
        btnSiguiente.setOnClickListener {
            tvCuentaAtras.visibility=View.VISIBLE
            initCuentaAtras()
        }

        btnStart.setOnClickListener {
            /*setImageResource no funcionaba como debia por algún motivo,aqui simplemente llamamos a los métodos del cronometro y cambiamos la imagen pause/play
            segun nos convenga*/
            if(!cronometro.estaIniciado()){
                cronometro.iniciar()
                btnStart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
            }else if (cronometro.estaPausado()) {
                cronometro.reanudar()
                btnStart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
            }else{
                cronometro.pausar()
                btnStart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play))
            }
        }

        btnSumarPeso.setOnClickListener{
            peso+=2.5
            setPeso()
        }
        btnRestarPeso.setOnClickListener {
            peso-=2.5
            setPeso()
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
        db= DatabaseHelper(this)
        rutinaEjericicoDb= RutinaEjercicioDb(db)
        rutinaDb= RutinaDb(db)
        rutina= rutinaDb.getRutina(1 )
        infoEjercicios()
    }

    private lateinit var tvCronometro: TextView
    private lateinit var tvNombreEjercicio: TextView
    private lateinit var btnOcultar: ImageButton
    private lateinit var wvReproductor: WebView
    private lateinit var tvSeries: TextView
    private lateinit var tvRepeticiones: TextView
    private lateinit var tvPeso: TextView
    private lateinit var btnRestarPeso: FloatingActionButton
    private lateinit var btnSumarPeso: FloatingActionButton
    private lateinit var btnStart: FloatingActionButton
    private lateinit var btnSiguiente:AppCompatButton
    private lateinit var tvCuentaAtras: TextView
    private lateinit var cronometro: Cronometro
    private lateinit var cuentaAtras: Cronometro
    private lateinit var db: DatabaseHelper
    private lateinit var rutinaEjericicoDb : RutinaEjercicioDb
    private lateinit var rutinaDb:RutinaDb
    private var rutina:Rutina?=null
    private var peso= 0.0
    private var serieNum=0
}

