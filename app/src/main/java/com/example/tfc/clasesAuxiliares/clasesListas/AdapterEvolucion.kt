package com.example.tfc.clasesAuxiliares.clasesListas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.HistorialPeso

class AdapterEvolucion(private val context: Context, private val listaHistorialpeso: MutableList<HistorialPeso>) :
    ArrayAdapter<HistorialPeso>(context, 0, listaHistorialpeso) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.layout_evolucion, parent, false)
        //Optenemos la lista de evolucion y asignamos al layout creado para ella
        val historialPeso = listaHistorialpeso[position]

        val tvNombreEjercicio = view.findViewById<TextView>(R.id.tvEvoEjercicio)
        tvNombreEjercicio.text = historialPeso.nombreEjercicio

        val tvPesoInicial = view.findViewById<TextView>(R.id.tvEvoPesoInicial)
        tvPesoInicial.text = historialPeso.pesoAnterior.toString()

        val tvPesoFinal = view.findViewById<TextView>(R.id.tvPesoFinal)
        tvPesoFinal.text = historialPeso.pesoActual.toString()

        val tvFecha = view.findViewById<TextView>(R.id.tvEvoFecha)
        tvFecha.text = historialPeso.fecha

        val viewInfo = view.findViewById<View>(R.id.viewEvoInfo)
        if (historialPeso.pesoAnterior > historialPeso.pesoActual)
            viewInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        else
            viewInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.green))

        return view
    }
}