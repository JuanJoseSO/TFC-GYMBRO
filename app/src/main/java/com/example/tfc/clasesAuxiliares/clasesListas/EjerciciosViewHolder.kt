package com.example.tfc.clasesAuxiliares.clasesListas

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfc.R

class EjerciciosViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvNombre: TextView = view.findViewById(R.id.tvNombre)
    val tvRecSeries: TextView = view.findViewById(R.id.tvRecSeries)
    val tvRecRepeticiones: TextView = view.findViewById(R.id.tvRecRepeticiones)
    val tv1: TextView = view.findViewById(R.id.textView2)
    val tv2: TextView = view.findViewById(R.id.textView3)
    val v1:View= view.findViewById(R.id.view)

}