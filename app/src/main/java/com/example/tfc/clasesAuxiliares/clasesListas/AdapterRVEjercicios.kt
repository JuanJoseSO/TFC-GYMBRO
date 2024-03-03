package com.example.tfc.clasesAuxiliares.clasesListas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.RutinaEjercicioDb
import java.util.Collections


class AdapterRVEjercicios(
    private val context: Context,
    private val listaEjercicios: MutableList<Ejercicio>,
    private val listaInfoEjercicios: List<ArrayList<Double>>
) : RecyclerView.Adapter<EjerciciosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosViewHolder {
        return EjerciciosViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_ejercicio, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EjerciciosViewHolder, position: Int) {
        val ejercicio = listaEjercicios[position]
        val infoEjercicio = listaInfoEjercicios[position]

        holder.tvNombre.text = ejercicio.nombre
        //Simplemente asignamos texto y msotramos el contenedor
        holder.clInfoEjercicio.visibility = View.VISIBLE
        holder.tvRecSeries.text = infoEjercicio[0].toInt().toString()
        holder.tvRecRepeticiones.text = infoEjercicio[1].toInt().toString()

    }

    override fun getItemCount(): Int = listaEjercicios.size
    fun moverEjercicio(posicionInicial: Int, posicionFinal: Int) {
        //Si la posicion inicial es menor que la final
        if (posicionInicial < posicionFinal) {
            for (i in posicionInicial until posicionFinal) {
                //Cambiamos todos los elementos hasta el final con una posicion mas
                Collections.swap(listaEjercicios, i, i + 1)
            }
        } else {
            //Si es menor,al reves
            for (i in posicionInicial downTo posicionFinal + 1) {
                Collections.swap(listaEjercicios, i, i - 1)
            }
        }
        //notifica el cambio en la posicion del item
        notifyItemMoved(posicionInicial, posicionFinal)
        val rutinaEjercicioDb = RutinaEjercicioDb(DatabaseHelper(context))
        //Actualizamos en la base de datos
        rutinaEjercicioDb.updateOrden(listaEjercicios)
    }

    fun eliminarEjercicio(position: Int) {
        //Elimina el item de la posicion que le pasemos
        val idEjercicio = listaEjercicios[position].id
        listaEjercicios.removeAt(position)
        //Notifica el cambio
        notifyItemRemoved(position)
        //Necesario para que no haya una excepcion de fuera de rango al eliminar el ultimo item
        notifyItemRangeChanged(position, listaEjercicios.size - position)

        //Lo guardamos en la base de datos
        val rutinaEjercicioDb = RutinaEjercicioDb(DatabaseHelper(context))
        rutinaEjercicioDb.eliminarEjercicio(idEjercicio)
    }
}