package com.example.tfc.entrenamiento

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import com.example.tfc.R


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

        initComponentes(view)
        initListeners()

    }

    private fun initListeners() {
        btnCrear.setOnClickListener {
            val intent = Intent(requireContext(),ActivityCrearRutina()::class.java)
            startActivity(intent)
        }
    }

    private fun initComponentes(view: View){
        btnCrear=view.findViewById(R.id.btnCrearRutina)
        btnModificar=view.findViewById(R.id.btnModificarRutina)
        btnEliminar=view.findViewById(R.id.btnEliminarRutina)
        listaRutinas=view.findViewById(R.id.listaRutinas)

    }

    private lateinit var listaRutinas : ListView
    private lateinit var btnCrear : AppCompatButton
    private lateinit var btnModificar : AppCompatButton
    private lateinit var btnEliminar : AppCompatButton
}
