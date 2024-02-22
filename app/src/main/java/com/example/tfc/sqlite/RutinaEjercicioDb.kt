package com.example.tfc.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log

class RutinaEjercicioDb (private val dbHelper:DatabaseHelper) {

    fun addEjercicioARutina(idRutina:Int,idEjercicio:Int,repeticiones:Int,series:Int,peso:Double) {
        val db = dbHelper.writableDatabase
        //try {
            val values = ContentValues().apply {
                put(DatabaseHelper.ID_RUTINA, idRutina)
                put(DatabaseHelper.ID_EJERCICIO, idEjercicio)
                put(DatabaseHelper.REPETICIONES, repeticiones)
                put(DatabaseHelper.SERIES, series)
                put(DatabaseHelper.PESO, peso)
            }
            db.insert(DatabaseHelper.TABLA_RUTINA_EJERCICIOS, null, values)
       /* } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir  el ejercicio", e)
        }*/
    }
}