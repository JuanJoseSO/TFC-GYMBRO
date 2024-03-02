package com.example.tfc.clasesAuxiliares.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio


class AdapterRVEjercicios(private val context: Context, private val listaEjercicios: List<Ejercicio>,private val listaInfoEjercicios: List<ArrayList<Double>>) :
    RecyclerView.Adapter<EjerciciosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosViewHolder {
        return EjerciciosViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_ejercicio, parent,false))
    }

    override fun onBindViewHolder(holder: EjerciciosViewHolder, position: Int) {
        val ejercicio=listaEjercicios[position]
        val infoEjercicio = listaInfoEjercicios[position]

        holder.tvNombre.text = ejercicio.nombre
        //Simplemente asignamos texto y lo mostramos por cada ejercicio
        holder.tvRecSeries.text = infoEjercicio[0].toInt().toString()
        holder.tvRecSeries.visibility=View.VISIBLE
        holder.tvRecRepeticiones.text =infoEjercicio[1].toInt().toString()
        holder.tvRecRepeticiones.visibility=View.VISIBLE
        holder.tv1.visibility=View.VISIBLE
        holder.tv2.visibility=View.VISIBLE
        holder.v1.visibility=View.VISIBLE
    }

    override fun getItemCount(): Int=listaEjercicios.size
}