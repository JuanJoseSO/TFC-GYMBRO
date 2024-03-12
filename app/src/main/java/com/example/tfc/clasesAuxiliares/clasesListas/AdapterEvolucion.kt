package com.example.tfc.clasesAuxiliares.clasesListas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.HistorialPeso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterEvolucion(
    private val context: Context,
    private val listaHistorialpeso: MutableList<HistorialPeso>
) :
    ArrayAdapter<HistorialPeso>(context, 0, listaHistorialpeso) {

    var listaCompleta: MutableList<HistorialPeso> = ArrayList(listaHistorialpeso)
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
        val unixTime = historialPeso.fecha.toLong() // Convierte la cadena a Long
        val date = Date(unixTime * 1000) // Convierte a milisegundos y crea un objeto Date

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = formatter.format(date)

        tvFecha.text = fechaFormateada

        val viewInfo = view.findViewById<View>(R.id.viewEvoInfo)
        if (historialPeso.pesoAnterior > historialPeso.pesoActual)
            viewInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        else
            viewInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.green))

        return view
    }

    //Función para filtar la lista por los nombres de los ejercicios,fuente Stackoverflow
    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val listaFiltrada = mutableListOf<HistorialPeso>()
                //Si la lista esta vacia o es nula,mostramos la lista completa
                if (constraint.isNullOrEmpty()) {
                    listaFiltrada.addAll(listaCompleta)
                } else {
                    //Si no,creamos un filtro y lo pasamos for todos los elementos de la lista
                    val filtro = constraint.toString().lowercase().trim()
                    for (i in listaCompleta) {
                        if (i.nombreEjercicio.lowercase().contains(filtro))
                            listaFiltrada.add(i)
                    }
                }
                //Retornamos la lista filtrada
                return FilterResults().apply { values = listaFiltrada }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                if (results?.values != null) {
                    addAll(results.values as List<HistorialPeso>)
                }
                notifyDataSetChanged()
            }
        }
    }
}