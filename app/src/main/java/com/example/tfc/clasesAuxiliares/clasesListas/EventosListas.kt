package com.example.tfc.clasesAuxiliares.clasesListas

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

//Clase para gestionar eventos en las distintas listas
class EventosListas (
    private val adapter:AdapterRVEjercicios
) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val posicionInicial= viewHolder.bindingAdapterPosition
        val posicionFinal =target.bindingAdapterPosition
        adapter.mover(posicionInicial,posicionFinal)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = (viewHolder.bindingAdapterPosition)
       adapter.eliminar(position)
    }


}