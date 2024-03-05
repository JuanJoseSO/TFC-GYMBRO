package com.example.tfc.entrenamiento

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesListas.AdapterRVEjercicios
import com.example.tfc.clasesAuxiliares.clasesListas.AdapterRVRutina
import com.example.tfc.clasesAuxiliares.clasesListas.EventosListas
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.EntrenamientoDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb


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
        mostrartListaRutina()
    }

    //Recogemos la lista de rutinas de la base de datos y la mostramos
    private fun mostrartListaRutina() {
        rvEjercicios.visibility = View.GONE
        rvRutina.visibility = View.VISIBLE
        val listaRutina = entrenamientoDb.getRutinaPorUsuario(userDb.getUsuarioSeleccionado()?.id!!)

        val adapterRutina = AdapterRVRutina(requireContext(), listaRutina).also {
            it.onItemClick = { rutina -> mostrarEjerciciosPorRutina(rutina.id) }
        }
        rvRutina.adapter = adapterRutina

        rvRutina.layoutManager = LinearLayoutManager(requireContext())
        //Usanmos los envento de EventosListas
        evento = EventosListas(null, adapterRutina)
        //Lo asignamos al recycler view
        val itemTouchHelper = ItemTouchHelper(evento)
        itemTouchHelper.attachToRecyclerView(rvRutina)
    }

    //Mostramos los ejercicios que PERTENECEN a una rutina con la tabla rutina_ejercicios
    private fun mostrarEjerciciosPorRutina(id: Int) {
        val listaEjerciciosPorRutina = entrenamientoDb.getEjerciciosPorRutina(userDb.getUsuarioSeleccionado()!!.id,id)
        val listaInfoEjercicios = listaEjerciciosPorRutina.map { ejercicio ->
            entrenamientoDb.getInfoRutina(userDb.getUsuarioSeleccionado()!!.id,id, ejercicio.id)
        }/*Usamos el adapter del recycler view,ocultamos el listview para evitar tener conflictor entre ellos y mostramos
          el recicler view*/
        rvEjercicios.visibility = View.VISIBLE
        rvRutina.visibility = View.GONE
        val adapterEjercicios =
            AdapterRVEjercicios(requireContext(), listaEjerciciosPorRutina, listaInfoEjercicios,userDb.getUsuarioSeleccionado()!!.id,id)
        rvEjercicios.adapter = adapterEjercicios
        rvEjercicios.layoutManager = LinearLayoutManager(requireContext())
        //Usanmos los envento de EventosListas
        evento = EventosListas(adapterEjercicios, null)
        //Lo asignamos al recycler view
        val itemTouchHelper = ItemTouchHelper(evento)
        itemTouchHelper.attachToRecyclerView(rvEjercicios)

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
        rvRutina = requireView().findViewById(R.id.rvListas2)
        rvEjercicios = requireView().findViewById(R.id.rvListas1)
        contenedor = requireView().findViewById(R.id.contenedor_rutina)

        db = DatabaseHelper(requireContext())
        entrenamientoDb = EntrenamientoDb(db)
        userDb = UserDb(db)

    }

    private lateinit var rvRutina: RecyclerView
    private lateinit var rvEjercicios: RecyclerView
    private lateinit var contenedor: FrameLayout
    private lateinit var btnCrear: AppCompatButton
    private lateinit var db: DatabaseHelper
    private lateinit var userDb: UserDb

    private lateinit var entrenamientoDb: EntrenamientoDb
    private lateinit var evento: EventosListas
}