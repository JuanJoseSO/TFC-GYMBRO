package com.example.tfc.clasesAuxiliares.clasesListas

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R

class RutinaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvNombreRutina = view.findViewById<TextView>(R.id.tvNombreRutina)
    val tvDiaRutina = view.findViewById<TextView>(R.id.tvDiaRutina)
    val borde = view.findViewById<ConstraintLayout>(R.id.celda_rutina)
}