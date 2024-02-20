package com.example.tfc.entrenamiento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.example.tfc.R
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.clasesAuxiliares.AdapterCategoriasEjercicios


class FragmentCategorias : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categorias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
    }

    private fun initUI(view:View){
        //Relacionamos con la ListView en el layout del fragment
        val lista = view.findViewById<ListView>(R.id.listaCategorias)
        //Obtenemos las categorias de la base de datos
        db= DatabaseHelper(requireContext())
        val listaSinImagenes = db.obtenerCategorias()

        //Relacionamos cada categoria con un icono con la función iconoPorCategoria
        val listaConImagenes = listaSinImagenes.map { categoria->
            Pair(iconoPorCategoria(categoria),categoria)
        }

        //Crea el adaptador
        val adapter = AdapterCategoriasEjercicios(requireContext(), listaConImagenes) //Con requireContext() obtenemos el contexto del fragment
        //Une el adaptador a la lista
        lista.adapter=adapter

        initListeners(lista)
    }

    private fun initListeners(lista:ListView){
        //Listener para saber que categoria seleccionamos
        lista.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val categoria = lista.adapter.getItem(position) as Pair<*, *>
            val nombreCategoria = categoria.second

            //Lanzamos el nuevo fragment
            mostrarEjerciciosCategoria(nombreCategoria)
        }
    }

    private fun mostrarEjerciciosCategoria(categoria: Any?) {
        //Enviamos la categoria seleccionada al fragmet siguiente
        val fragmentEjerciciosCategoria = FragmentEjercicios().apply {
            arguments = Bundle().apply {
                putString("categoria", categoria.toString())
            }
        }

        //Cambiamos el fragment,pasamos de la lista de categorias a la lista de ejercicios
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragmentContenedorEntrenamiento, fragmentEjerciciosCategoria)
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
            getString(R.string.hombro)-> R.drawable.ic_hombros
            getString(R.string.gluteo)-> R.drawable.ic_gluteo
            getString(R.string.cuadriceps)-> R.drawable.ic_cuadriceps
            getString(R.string.gemelo)-> R.drawable.ic_gemelo

            //La sentencia return when siempre requiere un else
            else -> R.drawable.ic_gluteo
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        db.close() // Asumiendo que `db` es accesible a nivel de clase y su ciclo de vida está bien gestionado
    }

    private lateinit var db : DatabaseHelper
}

