package com.example.tfc.infoUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.tfc.R
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.HistorialDb
import com.example.tfc.sqlite.sqliteMetodos.RutinaDb

class FragmentHistorial : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponentes()
        cargarInfoSesion()

    }

    private fun cargarInfoSesion() {
        val sesion= historialDb.getSesion(arguments?.getInt("id",-1)?:-1)

        if(sesion!=null){
            val rutina= sesion.let { rutinaDb.getRutina(it.idRutina) }
            if (rutina != null) tvNomRutina.text= rutina.nombre
            tvDiaEntrenado.text= sesion.dia
            tvHoraInicio.text= sesion.horaInicio
            tvTiempoTotal.text= sesion.tiempoTotal
            tvCalorias.text= sesion.caloriasQuemadas.toString()
        }
    }

    private fun initComponentes() {
        tvNomRutina=requireView().findViewById(R.id.tvNomRutina)
        tvDiaEntrenado=requireView().findViewById(R.id.tvDiaEntrenado)
        tvHoraInicio=requireView().findViewById(R.id.tvHoraInicio)
        tvTiempoTotal=requireView().findViewById(R.id.tvTiempoTotal)
        tvCalorias=requireView().findViewById(R.id.tvCalorias)
        btnCrearRutina=requireView().findViewById(R.id.btnCrearRutina)
        db= DatabaseHelper(requireContext())
        historialDb=HistorialDb(db)
        rutinaDb=RutinaDb(db)
    }

    private lateinit var tvNomRutina: TextView
    private lateinit var tvDiaEntrenado: TextView
    private lateinit var tvHoraInicio: TextView
    private lateinit var tvTiempoTotal: TextView
    private lateinit var tvCalorias: TextView
    private lateinit var btnCrearRutina: AppCompatButton
    private lateinit var db: DatabaseHelper
    private lateinit var historialDb: HistorialDb
    private lateinit var rutinaDb: RutinaDb

}