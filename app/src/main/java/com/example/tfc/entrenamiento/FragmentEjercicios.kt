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
        return inflater.inflate(R.layout.fragment_listas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ejerciciosDb = EjerciciosDb(DatabaseHelper(requireContext()))
        initUI(view)
    }

    private fun initUI(view : View) {
        val lista = view.findViewById<ListView>(R.id.listas)

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
        DatabaseHelper(requireContext()).close()
        super.onDestroy()
    }

    private lateinit var ejerciciosDb: EjerciciosDb
}
