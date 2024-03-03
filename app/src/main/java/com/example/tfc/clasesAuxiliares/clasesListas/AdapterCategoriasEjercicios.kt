package com.example.tfc.clasesAuxiliares.clasesListas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tfc.R

class AdapterCategoriasEjercicios(
    private val context: Context, private val listaCategorias: List<Pair<Int, String>>
) : ArrayAdapter<Pair<Int, String>>(context, 0, listaCategorias) {

    /*Con esta función optimizamos el codigo evitando crear una clase contenedor individual para las categorias lo que nos va a
    permitir trabajar con una consulta a la base de datos y darle una imagen para inflar la ListView                         */
    override fun getView(posicion: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.layout_cat_ejercicios, parent, false)
        icono = view.findViewById(R.id.icono)
        nombre = view.findViewById(R.id.nombre)

        //Recogemos la categoria por su posicion
        val categoria = listaCategorias[posicion]
        //Le damos el icono y el nombre
        icono.setImageResource(categoria.first) //La imagen
        nombre.text = categoria.second //El nombre de la categoría

        return view
    }

    private lateinit var icono: ImageView
    private lateinit var nombre: TextView
}