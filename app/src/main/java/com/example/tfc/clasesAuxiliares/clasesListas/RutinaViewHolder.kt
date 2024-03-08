package com.example.tfc.clasesAuxiliares.clasesListas

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R

class RutinaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvNombreRutina: TextView = view.findViewById(R.id.tvNombreRutina)
    val tvDiaRutina: TextView = view.findViewById(R.id.tvDiaRutina)
    val borde: ConstraintLayout = view.findViewById(R.id.celda_rutina)
}