package com.example.tfc.clasesAuxiliares.clasesListas

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Dieta


class AdapterDieta(private val context: Context, private val listaDieta: List<Dieta>) :
    ArrayAdapter<Dieta>(context, 0, listaDieta) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.layout_dietas, parent, false)
        //Optenemos el ejercicio por su posicion
        val dieta = listaDieta[position]
        val borde = view.findViewById<LinearLayout>(R.id.celda_dieta)
        //Relacionamos con el layout
        val tvNombreDieta = view.findViewById<TextView>(R.id.tvNivelDieta)
        tvNombreDieta.text = dieta.nombre

        /*Esta parte es complicada por que tenemos que darle forma a un drawable que define el estilo que le estamos dando a lis items
        de la lista en layout_dietas.xml,basicamente lo que hacemos es recoger el color dependiendo del nivel asignado a cada dieta,
        obtenemos el drawable que queremos y le damos forma con el método setStrole()*/
        val celdasDieta = ContextCompat.getDrawable(context, R.drawable.layout_celdas)
            ?.mutate() as? GradientDrawable
        celdasDieta.let {
            val colorBackground = when (dieta.nivel) {
                0 -> ContextCompat.getColor(context, R.color.green)
                1 -> ContextCompat.getColor(context, R.color.orange)
                2 -> ContextCompat.getColor(context, R.color.red)
                else -> ContextCompat.getColor(context, R.color.red)
            }
            it?.setStroke(15, colorBackground)
            borde.background = celdasDieta
        }
        return view
    }
}