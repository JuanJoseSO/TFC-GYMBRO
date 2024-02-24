package com.example.tfc.clasesAuxiliares.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio

class AdapterEjercicios(private val context: Context, listaEjercicios: List<Ejercicio>)
    : ArrayAdapter<Ejercicio>(context,0, listaEjercicios){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        val view =convertView?:LayoutInflater.from(context).inflate(R.layout.layout_ejercicio, parent, false)
        //Optenemos el ejercicio por su posicion
        val ejercicio = getItem(position)
        //Relacionamos con el layout
        val textView = view.findViewById<TextView>(R.id.tvNombre)
        textView.text = ejercicio?.nombre

        return view
    }
}