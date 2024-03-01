package com.example.tfc.entrenamiento

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.adapters.AdapterCategoriasEjercicios
import com.example.tfc.clasesAuxiliares.adapters.AdapterEjercicios
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.EjerciciosDb


class FragmentCategorias : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ejerciciosDb = EjerciciosDb(DatabaseHelper(requireContext()))
        initUICategorias()
    }

    private fun initUICategorias() {
        //Relacionamos con la ListView en el layout del fragment
        val lista = requireView().findViewById<ListView>(R.id.listas)

        //Obtenemos las categorias de la base de datos
        val listaSinImagenes = ejerciciosDb.obtenerCategorias()

        //Relacionamos cada categoria con un icono con la función iconoPorCategoria
        val listaConImagenes = listaSinImagenes.map { categoria ->
            Pair(iconoPorCategoria(categoria), categoria)
        }

        //Crea el adaptador
        val adapter = AdapterCategoriasEjercicios(
            requireContext(), listaConImagenes
        ) //Con requireContext() obtenemos el contexto del fragment
        //Une el adaptador a la lista
        lista.adapter = adapter
        initListeners(lista)
    }

    private fun initListeners(lista: ListView) {
        //Listener para saber que categoria seleccionamos
        lista.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val categoria = lista.adapter.getItem(position) as Pair<*, *>
            val nombreCategoria = categoria.second

            //Lanzamos el nuevo fragment
            initUIEjercicios(nombreCategoria)
        }
    }
    //Función para relacionar cada categoría con un icono
    private fun iconoPorCategoria(categoria: String): Int {
        return when (categoria) {
            getString(R.string.pecho) -> R.drawable.ic_pecho
            getString(R.string.espalda) -> R.drawable.ic_espalda
            getString(R.string.biceps) -> R.drawable.ic_biceps
            getString(R.string.triceps) -> R.drawable.ic_triceps
            getString(R.string.abs) -> R.drawable.ic_abs
            getString(R.string.hombro) -> R.drawable.ic_hombros
            getString(R.string.gluteo) -> R.drawable.ic_gluteo
            getString(R.string.cuadriceps) -> R.drawable.ic_cuadriceps
            getString(R.string.gemelo) -> R.drawable.ic_gemelo

            //La sentencia return when siempre requiere un else
            else -> R.drawable.ic_gluteo
        }
    }

    override fun onDestroy() {
        DatabaseHelper(requireContext()).close()
        super.onDestroy()
    }

    private fun initUIEjercicios(categoria: Any?) {
        val lista = requireView().findViewById<ListView>(R.id.listas)

        /*Creamos una variable ejercicio de tipo lista(SmartCast) y hacemos la consulta con el string de intent,ahorrando
          la creacion de dos variables*/
        val ejercicios = ejerciciosDb.getEjerciciosPorCategoria(categoria.toString())

        val adapter = AdapterEjercicios(requireContext(), ejercicios)
        lista.adapter = adapter

        //Listener para en el ejercicio que seleccionemos desde la lista
        lista.setOnItemClickListener { _, _, position, _ ->
            val ejercicioSeleccionado =
                ejercicios[position] //Guardamos el ejercicio seleccionado en una variable
            val intent = Intent(requireContext(), ActivityInfoEjercicios::class.java)
            intent.putExtra(
                "ejercicio", ejercicioSeleccionado.id
            ) //Enviamos SOLO el ID ejericio seleccionado
            startActivity(intent)
        }
    }

    private lateinit var ejerciciosDb: EjerciciosDb
}