package com.example.tfc.Entrenamiento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.example.tfc.R
import com.example.tfc.SQLite.DatabaseHelper
import com.example.tfc.clasesAuxiliares.AdapterCategoriasEjercicios


class FragmentCategorias : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categorias, container, false)
    }

    override fun onViewCreated(vista: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vista, savedInstanceState)
        initUI(vista)
    }

    //Función con la lógica de la activity
    private fun initUI(vista:View){
        //Relacionamos con la ListView en el layout del fragment
        val lista = vista.findViewById<ListView>(R.id.listaCategorias)
        //Obtenemos las categorias de la base de datos
        val db= DatabaseHelper(requireContext())
        val listaSinImagenes = db.obtenerCategorias()

        //Relacionamos cada categoria con un icono con la función iconoPorCategoria
        val listaConImagenes = listaSinImagenes.map { categoria->
            Pair(iconoPorCategoria(categoria),categoria)
        }

        //Crea el adaptador
        val adapter = AdapterCategoriasEjercicios(requireContext(), listaConImagenes) //Con requireContext() obtenemos el contexto del fragment
        //Une el adaptador a la lista
        lista.adapter=adapter

        navegacionLista(lista)
    }

    private fun navegacionLista(lista:ListView){
        //Listener para saber que categoria seleccionamos
        lista.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val categoria = lista.adapter.getItem(position) as Pair<Int, String>
            val nombreCategoria = categoria.second

            //Lanzamos el nuevo fragment
            mostrarEjerciciosCategoria(nombreCategoria)
        }

    }

    private fun mostrarEjerciciosCategoria(categoria: String) {
        //Enviamos la categoria seleccionada al fragmet siguiente
        val fragmentEjerciciosCategoria = FragmentEjercicios().apply {
            arguments = Bundle().apply {
                putString("categoria", categoria)
            }
        }

        //Cambiamos el fragment,pasamos de la lista de categorias a la lista de ejercicios
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragment_contenedor, fragmentEjerciciosCategoria)
            addToBackStack(null) //El boton atrás vuelve al fragment anterior
            commit()
        }
    }

    //Función para relacionar cada categoría con un icono
    private fun iconoPorCategoria(categoria: String): Int {
        return when(categoria){
            getString(R.string.pecho)-> R.drawable.ic_pecho
            getString(R.string.espalda)-> R.drawable.ic_espalda
            getString(R.string.biceps)-> R.drawable.ic_biceps
            getString(R.string.triceps)-> R.drawable.ic_triceps
            getString(R.string.abs)-> R.drawable.ic_abs
            getString(R.string.hombros)-> R.drawable.ic_hombros
            getString(R.string.gluteo)-> R.drawable.ic_gluteo
            getString(R.string.cuadriceps)-> R.drawable.ic_cuadriceps
            getString(R.string.gemelo)-> R.drawable.ic_gemelo

            //La sentencia return when siempre requiere un else
            else -> R.drawable.ic_gluteo
        }

    }
}

