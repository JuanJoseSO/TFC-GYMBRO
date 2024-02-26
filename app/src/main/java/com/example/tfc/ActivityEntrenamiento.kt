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

import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityEntrenamiento : AppCompatActivity() { override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento)

    initComponentes()
    initCronometro()
    initListeners()

    }
    private fun initListeners() {
        btnOcultar.setOnClickListener {
            if(wvReproductor.visibility== View.VISIBLE)
                wvReproductor.visibility= View.GONE
            else
                wvReproductor.visibility= View.VISIBLE
        }
        btnSiguiente.setOnClickListener {
            tvCuentaAtras.visibility=View.VISIBLE
            cuentaAtras()
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



    }
    private fun initComponentes() {
        tvCronometro = findViewById(R.id.tvCronometro)
        tvNombreEjercicio = findViewById(R.id.tvNombreEjercicio)
        btnOcultar = findViewById(R.id.btnOcultar)
        wvReproductor = findViewById(R.id.wvReproductor)
        tvEjercicio = findViewById(R.id.tvEjercicio)
        tvRepeticiones = findViewById(R.id.tvRepeticiones)
        btnRestarPeso = findViewById(R.id.btnRestarPeso)
        tvPeso = findViewById(R.id.tvPeso)
        btnSumarPeso = findViewById(R.id.btnSumarPeso)
        btnStart = findViewById(R.id.btnStart)
        btnSiguiente = findViewById(R.id.btnSiguiente)
        tvCuentaAtras = findViewById(R.id.tvCuentaAtras)


    }
    private fun initCronometro(){
        cronometro= Cronometro()
        cronometro.setOnUpdateCronometro { tiempo->
            runOnUiThread{
                tvCronometro.text=tiempo
            }
        }
        if(!cronometro.estaIniciado()){
            cronometro.iniciar()
        }
    }
    private fun cuentaAtras(){
        cuentaAtras= Cronometro()
        cuentaAtras.setOnUpdateCuentaAtras { tiempo->
            runOnUiThread{
                tvCuentaAtras.text=tiempo
            }
        }
        cuentaAtras.initCuentaAtras(4000,tvCuentaAtras)
    }


    private lateinit var tvCronometro: TextView
    private lateinit var tvNombreEjercicio: TextView
    private lateinit var btnOcultar: ImageButton
    private lateinit var wvReproductor: WebView
    private lateinit var tvEjercicio: TextView
    private lateinit var tvRepeticiones: TextView
    private lateinit var tvPeso: TextView
    private lateinit var btnRestarPeso: FloatingActionButton
    private lateinit var btnSumarPeso: FloatingActionButton
    private lateinit var btnStart: FloatingActionButton
    private lateinit var btnSiguiente:AppCompatButton
    private lateinit var tvCuentaAtras: TextView
    private lateinit var cronometro: Cronometro
    private lateinit var cuentaAtras: Cronometro


}

