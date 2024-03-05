package com.example.tfc.clasesAuxiliares.clasesListas

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.EntrenamientoDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb

class AdapterRVRutina(
    private val context: Context, private val listaRutina: MutableList<Rutina>
) : RecyclerView.Adapter<RutinaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        return RutinaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_rutinas, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        val rutina = listaRutina[position]
        holder.tvNombreRutina.text = rutina.nombre
        holder.tvDiaRutina.text = rutina.diaPreferente
        holder.borde.background = setColorCeldas(rutina.intensidad)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(rutina)
        }
    }

    override fun getItemCount(): Int = listaRutina.size

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

    fun eliminarRutina(position: Int) {
        //Elimina el item de la posicion que le pasemos
        val idRutina = listaRutina[position].id
        listaRutina.removeAt(position)
        //Notifica el cambio
        notifyItemRemoved(position)
        //Necesario para que no haya una excepcion de fuera de rango al eliminar el ultimo item
        notifyItemRangeChanged(position, listaRutina.size - position)

        //Lo guardamos en la base de datos
        val entrenamientoDb = EntrenamientoDb(DatabaseHelper(context))
        val userDb = UserDb(DatabaseHelper(context))
        entrenamientoDb.eliminarRutina(userDb.getUsuarioSeleccionado()!!.id, idRutina)
    }

    //Variable para eventos de clic,es decir,gestionarlo desde codigo
    var onItemClick: ((Rutina) -> Unit)? = null
}