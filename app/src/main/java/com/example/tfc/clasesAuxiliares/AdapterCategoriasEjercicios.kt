package com.example.tfc.clasesAuxiliares

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tfc.R

class AdapterCategoriasEjercicios(private val context: Context, private val listaCategorias:List<Pair<Int,String>>)
    :ArrayAdapter<Pair<Int,String>>(context,0,listaCategorias) {

    /*Con esta función optimizamos el codigo evitando crear una clase contenedor individual para las categorias lo que nos va a
    permitir trabajar con una consulta a la base de datos y darle una imagen para inflar la ListView                         */

    override fun getView(posicion: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        val vista =convertView?:LayoutInflater.from(context).inflate(R.layout.layout_cat_ejercicios, parent, false)

        initComponentes(vista)
        initUI(posicion)

        return vista
    }

    private fun initUI(posicion :Int){
        //Recogemos la categoria por su posicion
        val categoria = listaCategorias[posicion]
        //Le damos el icono y el nombre
        icono.setImageResource(categoria.first) //La imagen
        nombre.text = categoria.second //El nombre de la categoría
    }

    private fun initComponentes(vista:View){
        icono=vista.findViewById(R.id.icono)
        nombre=vista.findViewById(R.id.nombre)
    }
    private lateinit var icono: ImageView
    private lateinit var nombre:TextView

}
