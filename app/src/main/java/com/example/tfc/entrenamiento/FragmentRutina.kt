package com.example.tfc.entrenamiento

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.adapters.AdapterEjercicios
import com.example.tfc.clasesAuxiliares.adapters.AdapterRutina
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.RutinaDb
import com.example.tfc.sqlite.sqliteMetodos.RutinaEjercicioDb


class FragmentRutina : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rutina, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes()
        initListeners()
        mostrartLista()

    }

    //Recogemos la lista de rutinas de la base de datos y la mostramos
    private fun mostrartLista() {
        val listaRutina = rutinaDb.getRutinas()
        lvRutina.adapter = AdapterRutina(requireContext(), listaRutina)

        //Al seleccionar una rutina nos muestra sus ejercicios
        lvRutina.setOnItemClickListener { _, _, position, _ ->
            mostrarEjerciciosPorRutina(listaRutina[position].id)
        }

    }

    //Mostramos los ejercicios que PERTENECEN a una rutina con la tabla rutina_ejercicios
    private fun mostrarEjerciciosPorRutina(id: Int) {
        val listaEjerciciosPorRutina = rutinaEjercicioDb.getEjerciciosPorRutina(id)
        //Aprovechamos el adapter y el mismo contenedor para mostrar la lista e ejercicios
        lvRutina.adapter = AdapterEjercicios(requireContext(), listaEjerciciosPorRutina)

    }


    override fun onDestroy() {
        DatabaseHelper(requireContext()).close()
        super.onDestroy()
    }

    private fun initListeners() {

        btnCrear.setOnClickListener {
            val intent = Intent(requireContext(), ActivityCrearRutina()::class.java)
            startActivity(intent)
        }
    }

    private fun initComponentes() {
        btnCrear = requireView().findViewById(R.id.btnCrearRutina)
        lvRutina= requireView().findViewById(R.id.listas)
        btnModificar = requireView().findViewById(R.id.btnModificarRutina)
        btnEliminar = requireView().findViewById(R.id.btnEliminarRutina)
        contenedor = requireView().findViewById(R.id.contenedor_rutina)
        db = DatabaseHelper(requireContext())
        rutinaDb = RutinaDb(db)
        rutinaEjercicioDb = RutinaEjercicioDb(db)
    }

    private lateinit var lvRutina : ListView
    private lateinit var contenedor: FrameLayout
    private lateinit var btnCrear: AppCompatButton
    private lateinit var btnModificar: AppCompatButton
    private lateinit var btnEliminar: AppCompatButton
    private lateinit var db: DatabaseHelper
    private lateinit var rutinaDb: RutinaDb
    private lateinit var rutinaEjercicioDb: RutinaEjercicioDb

}
