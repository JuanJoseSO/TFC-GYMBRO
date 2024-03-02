package com.example.tfc.clasesAuxiliares.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R

class EjercicioViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val tvNombre: TextView = view.findViewById(R.id.tvNombre)
}