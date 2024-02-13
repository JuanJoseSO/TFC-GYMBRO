package com.example.tfc.clasesAuxiliares

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tfc.R

class AdapterEjercicios(private val contexto: Context, private val listaEjercicios: List<String>)
    : ArrayAdapter<String>(contexto,0, listaEjercicios){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas (aumenta el rendimiento)
        val view =convertView?:LayoutInflater.from(contexto).inflate(R.layout.layout_ejercicio, parent, false)
        val nombreEjercicio = getItem(position)
        //Relacionamos con el layout
        val textView = view.findViewById<TextView>(R.id.tvNombre)
        textView.text = nombreEjercicio

        return view
    }
}