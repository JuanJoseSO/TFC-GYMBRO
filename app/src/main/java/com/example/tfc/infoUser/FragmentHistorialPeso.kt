package com.example.tfc.infoUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesListas.AdapterEvolucion
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.HistorialPesoDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb

class FragmentHistorialPeso : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historial_peso, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes()
        cargarListaHistorialPeso()
    }

    private fun cargarListaHistorialPeso() {
        val listaHistorialPeso = historialPesoDb.getHistorialPesoPorUsuario(userDb.getUsuarioSeleccionado()!!.id)
        lvHistorial.adapter = AdapterEvolucion(requireContext(), listaHistorialPeso)
    }

    private fun initListeners() {

    }

    private fun initComponentes() {
        lvHistorial = requireView().findViewById(R.id.lvHistorial)
        btnEliminarHistorial = requireView().findViewById(R.id.btnEliminarHistorial)
        db = DatabaseHelper(requireContext())
        historialPesoDb = HistorialPesoDb(db)
        userDb = UserDb(db)


    }

    private lateinit var lvHistorial: ListView
    private lateinit var btnEliminarHistorial: AppCompatButton
    private lateinit var db: DatabaseHelper
    private lateinit var historialPesoDb: HistorialPesoDb
    private lateinit var userDb: UserDb
}