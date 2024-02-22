package com.example.tfc.entrenamiento

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.tfc.R
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.clasesAuxiliares.AdapterEjercicios
import com.example.tfc.sqlite.EjerciciosDb

class FragmentEjercicios : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ejercicios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ejerciciosDb = EjerciciosDb(DatabaseHelper(requireContext()))
        initUI(view)
    }

    private fun initUI(view : View) {
        val lista = view.findViewById<ListView>(R.id.listaEjercicios)

        /*Creamos una variable ejercicio de tipo lista(SmartCast) y hacemos la consulta con el string de intent,ahorrando
          la creacion de dos variables*/
        val ejercicios = ejerciciosDb.getEjerciciosPorCategoria(requireArguments().getString("categoria") ?: "")

        val adapter = AdapterEjercicios(requireContext(), ejercicios)
        lista.adapter = adapter

        //Listener para en el ejercicio que seleccionemos desde la lista
        lista.setOnItemClickListener { _, _, position, _->
            val ejercicioSeleccionado = ejercicios[position] //Guardamos el ejercicio seleccionado en una variable
            val intent = Intent(requireContext(), ActivityInfoEjercicios::class.java)
            intent.putExtra("ejercicio", ejercicioSeleccionado.id) //Enviamos SOLO el ID ejericio seleccionado
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseHelper(requireContext()).close() // Asumiendo que `db` es accesible a nivel de clase y su ciclo de vida está bien gestionado
    }

    private lateinit var ejerciciosDb: EjerciciosDb
}
