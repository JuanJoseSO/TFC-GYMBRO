package com.example.tfc.clasesAuxiliares

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.min

class CirculosAnimados(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var sweepAngle = 0f

    //Funcion para el circulo animado de FragmentHome
    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 40f //Grosor
        paint.color = Color.RED //Color
        paint.strokeCap = Paint.Cap.ROUND //Redondea la linea
    }

    fun rellenarCirculo() {
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = 60000 //Duración de la animación en ms. ******CAMBIARLO A ENTRENAMIENTO DIARO
        animator.interpolator = LinearInterpolator() //Progresión uniforme

        animator.addUpdateListener { animation ->
            sweepAngle = animation.animatedValue as Float
            invalidate() //Redibuja la vista con un nuevo valor de sweepAngle
        }

        animator.start() //Inicia la animación
    }
    //Función que dibuja la linea con sus atributos
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val diameter = min(width, height) - paint.strokeWidth
        val radius = diameter / 2
        val cx = width / 2f
        val cy = height / 2f
        val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)

        //Color del circulo vacio
        paint.color = Color.LTGRAY
        canvas.drawCircle(cx, cy, radius, paint)

        //Cambia el color a rojo al dibujar el circulo
        paint.color = Color.RED
        canvas.drawArc(rect, -90f, sweepAngle, false, paint) //Dibuja el circulo
    }
}
