package com.example.tfc.Entrenamiento

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.tfc.R
import com.example.tfc.SQLite.DatabaseHelper
import com.example.tfc.clasesAuxiliares.AdapterEjercicios

class FragmentEjercicios : Fragment() {

    private lateinit var db: DatabaseHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ejercicios, container, false)
    }

    override fun onViewCreated(vista: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vista, savedInstanceState)

        db = DatabaseHelper(requireContext())
        initUI(vista)
    }

    private fun initUI(vista : View) {
        val lista = vista.findViewById<ListView>(R.id.listaEjercicios)

        /*Creamos una variable ejercicio de tipo lista(SmartCast) y hacemos la consulta con el string de intent,ahorrando
          la creacion de dos variables*/
        val ejercicios = db.getEjerciciosPorCategoria(requireArguments().getString("categoria") ?: "")

        //Recuperamos los nombres de los ejercicios
        val nombresEjercicios = ejercicios.map{it.nombre}

        val adapter = AdapterEjercicios(requireContext(), nombresEjercicios)
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
        db.close() // Asumiendo que `db` es accesible a nivel de clase y su ciclo de vida está bien gestionado
    }

}
