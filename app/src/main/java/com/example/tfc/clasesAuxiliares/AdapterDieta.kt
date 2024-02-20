package com.example.tfc.clasesAuxiliares

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tfc.R


class AdapterDieta(private val context: Context, listaDieta: List<String>)
    : ArrayAdapter<String>(context,0, listaDieta) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        val view =convertView?: LayoutInflater.from(context).inflate(R.layout.layout_dietas, parent, false)
        //Optenemos el ejercicio por su posicion
        val nombreDieta = getItem(position)
        //Relacionamos con el layout
        val textView = view.findViewById<TextView>(R.id.tvNivelDieta)
        textView.text = nombreDieta

        return view
    }
}