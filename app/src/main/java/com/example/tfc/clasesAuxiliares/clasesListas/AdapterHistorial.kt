package com.example.tfc.clasesAuxiliares.clasesListas

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.clasesAuxiliares.clasesBase.Sesion
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.RutinaDb

class AdapterHistorial(private val context: Context, private val listaHistorial: List<Sesion>) :
    ArrayAdapter<Sesion>(context, 0, listaHistorial) {

    private val rutinasDb = RutinaDb(DatabaseHelper(context))

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //ConvertView nos permite reciclar las vistas(aumenta el rendimiento)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.layout_rutinas, parent, false)
        //Optenemos el ejercicio por su posicion
        val sesiones = listaHistorial[position]
        val borde = view.findViewById<ConstraintLayout>(R.id.celda_rutina)
        //Relacionamos con el layout
        val tvNombreRutina = view.findViewById<TextView>(R.id.tvNombreRutina)
        val rutina: Rutina? = rutinasDb.getRutina(sesiones.idRutina)
        if (rutina != null) {
            tvNombreRutina.text = rutina.nombre
        }
        val tvDiaRutina = view.findViewById<TextView>(R.id.tvDiaRutina)
        tvDiaRutina.text = sesiones.dia

        borde.background = rutina?.intensidad?.let { setColorCeldas(it) }

        return view
    }

    private fun setColorCeldas(intensidad: Int): Drawable? {/*Esta parte es complicada por que tenemos que darle forma a un drawable que define el estilo que le estamos dando a lis items
               de la lista en layout_rutina.xml,basicamente lo que hacemos es recoger el color dependiendo de la intensidad asignada a cada rutina,
               obtenemos el drawable que queremos y le damos forma con el método setStrole()*/
        val bordeCeldas = ContextCompat.getDrawable(context, R.drawable.layout_celdas)
            ?.mutate() as? GradientDrawable
        bordeCeldas.let {
            val colorBackground = when (intensidad) {
                0 -> ContextCompat.getColor(context, R.color.green)
                1 -> ContextCompat.getColor(context, R.color.orange)
                2 -> ContextCompat.getColor(context, R.color.red)
                else -> ContextCompat.getColor(context, R.color.red)
            }
            it?.setStroke(15, colorBackground)
        }
        return bordeCeldas
    }
}