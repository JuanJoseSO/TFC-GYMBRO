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
import com.example.tfc.sqlite.sqliteMetodos.RutinaEjercicioDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb
import com.example.tfc.sqlite.sqliteMetodos.UsuarioRutinaDb


class FragmentRutina : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        val listaRutina = usuarioRutinaDb.getRutinaPorUsuario(userDb.getUsuarioSeleccionado()?.id!!)
        lvRutina.adapter = AdapterRutina(requireContext(), listaRutina)

        //Al seleccionar una rutina nos muestra sus ejercicios
        lvRutina.setOnItemClickListener { _, _, position, _ ->
            mostrarEjerciciosPorRutina(listaRutina[position].id)
        }
    }

    //Mostramos los ejercicios que PERTENECEN a una rutina con la tabla rutina_ejercicios
    private fun mostrarEjerciciosPorRutina(id: Int) {
        val listaEjerciciosPorRutina = rutinaEjercicioDb.getEjerciciosPorRutina(id)
        val lista = requireView().findViewById<ListView>(R.id.lvListas)
        lista.visibility = View.VISIBLE
       /*Creamos una variable ejercicio de tipo lista(SmartCast) y hacemos la consulta con el string de intent,ahorrando
      la creacion de dos variables*/
        lista.adapter = AdapterEjercicios(requireContext(), listaEjerciciosPorRutina)
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
        lvRutina = requireView().findViewById(R.id.lvListas)
        contenedor = requireView().findViewById(R.id.contenedor_rutina)
        db = DatabaseHelper(requireContext())
        usuarioRutinaDb = UsuarioRutinaDb(db)
        rutinaEjercicioDb = RutinaEjercicioDb(db)
        userDb = UserDb(db)
    }

    private lateinit var lvRutina: ListView
    private lateinit var contenedor: FrameLayout
    private lateinit var btnCrear: AppCompatButton
    private lateinit var db: DatabaseHelper
    private lateinit var userDb: UserDb
    private lateinit var usuarioRutinaDb: UsuarioRutinaDb
    private lateinit var rutinaEjercicioDb: RutinaEjercicioDb
}