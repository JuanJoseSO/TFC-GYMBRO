package com.example.tfc.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.Rutina

class RutinaDb (private val dbHelper:DatabaseHelper){

    //**************** MÉTODOS RUTINA ********************
    fun addRutina(rutina : Rutina){
        val db = dbHelper.writableDatabase
        try {
            val values = ContentValues().apply {
                put(DatabaseHelper.NOMBRE_RUTINA, rutina.nombre)
                put(DatabaseHelper.TIEMPO_OBJETIVO, rutina.tiempoObjetivo)
                put(DatabaseHelper.INTENSIDAD, rutina.intensidad)
                put(DatabaseHelper.DIA_PREFERENTE,rutina.diaPreferente)
            }

            db.insert(DatabaseHelper.TABLA_RUTINAS, null, values)
        }catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir ejercicios", e)
        }
    }
    @SuppressLint("Range")
    fun getRutinas():List<Rutina>{
        val listaRutina = ArrayList<Rutina>()
        val selectQuery = "SELECT * FROM ${DatabaseHelper.TABLA_RUTINAS}"

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val rutina = Rutina(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_RUTINA)), // ID del usuario
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_RUTINA)), // Nombre de Usuario
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIEMPO_OBJETIVO)), // Edad
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.INTENSIDAD)), // Peso
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIA_PREFERENTE)) // Altura

                )
                listaRutina.add(rutina)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaRutina
    }


}