package com.example.tfc.clasesAuxiliares.clasesListas

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

//Clase para gestionar eventos en las distintas listas
class EventosListas(
    private val adapterEjercicios: AdapterRVEjercicios? = null,
    private val adapterRutina: AdapterRVRutina? = null,
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    //Al mover la lista en vertical
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val posicionInicial = viewHolder.bindingAdapterPosition
        val posicionFinal = target.bindingAdapterPosition
        adapterEjercicios?.moverEjercicio(posicionInicial, posicionFinal)

        return true
    }

    //Al deplazarla en horizontal
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = (viewHolder.bindingAdapterPosition)
        adapterEjercicios?.eliminarEjercicio(position)
        adapterRutina?.eliminarRutina(position)
    }
}