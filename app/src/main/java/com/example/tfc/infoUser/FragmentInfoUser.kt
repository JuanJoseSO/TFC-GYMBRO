package com.example.tfc.infoUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tfc.R
import com.example.tfc.sqlite.DatabaseHelper

class FragmentInfoUser : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_user, container, false)

        iniciaComponentes(view)
        datosUsuario()

        return view
    }

    private fun datosUsuario() {
        val usuario = db.getUsuarioSeleccionado()
        if (usuario != null) {
            tvNombre.text = usuario.nombreUsuario
            tvAltura.text = usuario.altura.toString()
            tvPeso.text = usuario.peso.toString()
            tvIMC.text = String.format("%.2f", usuario.imc)
            tvEdad.text = usuario.edad.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    private fun iniciaComponentes(view:View) {
        db = DatabaseHelper(requireContext())
        tvNombre=view.findViewById(R.id.tvNombre)
        tvAltura=view.findViewById(R.id.tvAltura)
        tvPeso=view.findViewById(R.id.tvPeso)
        tvIMC=view.findViewById(R.id.tvIMC)
        tvEdad=view.findViewById(R.id.tvEdad)
    }

    private lateinit var tvNombre: TextView
    private lateinit var tvAltura: TextView
    private lateinit var tvPeso: TextView
    private lateinit var tvIMC: TextView
    private lateinit var tvEdad: TextView
    private lateinit var db: DatabaseHelper
}
