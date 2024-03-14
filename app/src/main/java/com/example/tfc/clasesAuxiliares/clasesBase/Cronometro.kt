package com.example.tfc.clasesAuxiliares.clasesBase

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Locale

class Cronometro {
    //Variables cronometro
    private var tiempoInicial: Long = 0
    private var tiempoAcumulado: Long = 0
    private var tiempoTotal: Long = 0

    //Bolean que nos ayudará a gestionar la logica de los relojes
    private var iniciado: Boolean = false
    private var pausado: Boolean = false

    //permite ejecutar codigo en el hilo principal
    private var handler = Handler(Looper.getMainLooper())

    //Variable funcional para actualizar el cronometro
    private var onUpdate: ((String) -> Unit)? = null

    //Funcion que se llama al actualizar el cronometro
    fun setOnUpdateCronometro(onUpdate: ((String) -> Unit)) {
        this.onUpdate = onUpdate
    }

    fun iniciar() {
        if (!iniciado) {
            //Asignamos tiempo inicial para recuperarlo en tiempo de entrenamiento más tarde
            tiempoInicial = System.currentTimeMillis()
        }
        //Cambiamos el bolleando a true
        iniciado = true
        pausado = false
        this.cicloCronometro()
    }

    fun pausar() {
        if (iniciado && !pausado) {
            //Dejamos de sumar el tiempo durante la pausa
            tiempoAcumulado += System.currentTimeMillis() - tiempoInicial
            iniciado = false
            pausado = true
        }
    }

    fun reanudar() {
        if (!iniciado && pausado) {
            //Reanudamos el tiempo
            tiempoAcumulado = System.currentTimeMillis()
            iniciado = true
            pausado = false
            this.cicloCronometro()
        }
    }

    private fun cicloCronometro() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!pausado) {
                    //Calculamos el tiempo total para asignarlo a nuestro cronometro
                    tiempoTotal = System.currentTimeMillis() + tiempoAcumulado - tiempoInicial

                    // Calculamos el tiempo en minutos, segundos y milisegundos
                    val milisegundos = (tiempoTotal % 1000) / 10
                    val segundos = (tiempoTotal / 1000) % 60
                    val minutos = (tiempoTotal / (1000 * 60)) % 60
                    //Mandamos el timepo ya formateado al hilo principal
                    onUpdate?.invoke(
                        String.format(
                            Locale.getDefault(), "%02d:%02d:%02d", minutos, segundos, milisegundos
                        )
                    )

                    // Reprogramar la ejecución de este bloque para que se ejecute nuevamente después de 10 milisegundos
                    handler.postDelayed(this, 10)
                }
            }
        }, 0)
    }

    fun estaPausado(): Boolean {
        return pausado
    }

    fun estaIniciado(): Boolean {
        return iniciado
    }

    //VAriables cuenta atras
    private var onUpdateCuentaAtras: ((String) -> Unit)? = null
    private var cuentaAtrasFinal: Long = 0
    private var cuentaAtrasRestante: Long = 0
    private var onFinish: (() -> Unit)? = null

    //permite ejecutar codigo en el hilo principal
    fun setOnUpdateCuentaAtras(onUpdateCuentaAtras: ((String) -> Unit)) {
        this.onUpdateCuentaAtras = onUpdateCuentaAtras
    }

    //Métodos para iniciar la cuenta atras
    //Le pasamos el textview para gestionar aqui su visibilidad
    fun initCuentaAtras(cuentaInicial: Int, clDesncaso: ConstraintLayout) {
        //Multiplicamos por mil y pasamos a long para obtener los milisegundos seleccionados de descanso
        this.cuentaAtrasRestante = (cuentaInicial * 1000).toLong()
        cuentaAtrasFinal = System.currentTimeMillis() + cuentaAtrasRestante
        cicloCuentaAtras(clDesncaso)
    }

    // Ciclo del contador regresivo,similar al anterior,pero restando tiempo y cambiando la visibilidad de la cuenta atras
    private fun cicloCuentaAtras(clDesncaso: ConstraintLayout) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val tiempoActual = System.currentTimeMillis()
                cuentaAtrasRestante = cuentaAtrasFinal - tiempoActual

                if (cuentaAtrasRestante > 0) {
                    val segundos = (cuentaAtrasRestante / 1000) % 60
                    val milisegundos = (cuentaAtrasRestante % 1000) / 10
                    onUpdateCuentaAtras?.invoke(
                        String.format(
                            Locale.getDefault(), "%02d:%02d", segundos, milisegundos
                        )
                    )
                    handler.postDelayed(this, 10)
                } else {
                    onUpdateCuentaAtras?.invoke("0")
                    clDesncaso.visibility = View.GONE
                    comprobarCuentaAtras()
                }
            }
        }, 10)
    }

    fun comprobarCuentaAtras() {
        if (cuentaAtrasRestante <= 0) {
            onFinish?.invoke()
        }
    }

    fun terminarCuentaAtras(onFinish: () -> Unit) {
        this.onFinish = onFinish
    }

}