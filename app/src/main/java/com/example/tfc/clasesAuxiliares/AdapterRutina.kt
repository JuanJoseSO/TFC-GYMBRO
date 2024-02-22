package com.example.tfc.clasesAuxiliares

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tfc.R

class AdapterRutina (private val context : Context, private val listaRutina:List<Rutina>)
    : ArrayAdapter<Rutina>(context,0,listaRutina){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        //Voy a usar el mismo layout que para diertas pero cambiando nivel por intensidad
        val view =convertView?: LayoutInflater.from(context).inflate(R.layout.layout_rutinas, parent, false)
        //Optenemos el ejercicio por su posicion
        val rutina = listaRutina[position]
        //Relacionamos con el layout
        val tvNombreRutina = view.findViewById<TextView>(R.id.tvNombreRutina)
        tvNombreRutina.text = rutina.nombre
        val tvDiaRutina= view.findViewById<TextView>(R.id.tvDiaRutina)
        tvDiaRutina.text=rutina.diaPreferente

        /*Esta parte es complicada por que tenemos que darle forma a un drawable que define el estilo que le estamos dando a lis items
        de la lista en layout_dietas.xml,basicamente lo que hacemos es recoger el color dependiendo del nivel asignado a cada dieta,
        obtenemos el drawable que queremos y le damos forma con el método setStrole()*/
        val celdasDieta= ContextCompat.getDrawable(context, R.drawable.celdas_dieta)?.mutate() as? GradientDrawable
        celdasDieta.let {
            val colorBackground =when(rutina.intensidad){
                "Baja"-> ContextCompat.getColor(context, R.color.green)
                "Media"-> ContextCompat.getColor(context, R.color.orange)
                "Alta"-> ContextCompat.getColor(context, R.color.red)
                else-> ContextCompat.getColor(context, R.color.red)
            }
            it?.setStroke(15,colorBackground)
            view.background = celdasDieta
        }

        return view
    }
}