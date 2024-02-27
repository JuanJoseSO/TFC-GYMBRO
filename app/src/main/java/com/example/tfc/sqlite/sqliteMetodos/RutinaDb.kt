package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.sqlite.DatabaseHelper

class RutinaDb(private val dbHelper: DatabaseHelper) {

    //**************** MÉTODOS RUTINA ********************
    fun addRutina(rutina: Rutina) {
        val db = dbHelper.writableDatabase

        val insert = ContentValues().apply {
            put(DatabaseHelper.NOMBRE_RUTINA, rutina.nombre)
            put(DatabaseHelper.TIEMPO_OBJETIVO, rutina.tiempoObjetivo)
            put(DatabaseHelper.INTENSIDAD, rutina.intensidad)
            put(DatabaseHelper.DESCANSO, rutina.descanso)
            put(DatabaseHelper.DIA_PREFERENTE, rutina.diaPreferente)
        }
        try {
            db.insert(DatabaseHelper.TABLA_RUTINAS, null, insert)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir ejercicios", e)
        }
    }

    @SuppressLint("Range")
    fun getRutinas(): List<Rutina> {
        val listaRutina = ArrayList<Rutina>()
        val select = "SELECT * FROM ${DatabaseHelper.TABLA_RUTINAS}"

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(select, null)

        if (cursor.moveToFirst()) {
            do {
                val rutina = Rutina(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_RUTINA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_RUTINA)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIEMPO_OBJETIVO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.INTENSIDAD)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DESCANSO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIA_PREFERENTE))
                )
                listaRutina.add(rutina)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaRutina
    }

    @SuppressLint("Range")
    fun getRutina(id:Int):Rutina? {
        val db=dbHelper.readableDatabase
        var rutina:Rutina?=null

        try{
            //Obtenemos una rutina por su id
            val cursor=db.query(
                DatabaseHelper.TABLA_RUTINAS,
                null,
                "${DatabaseHelper.ID_RUTINA} = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
            if(cursor.moveToFirst()){
                rutina = Rutina(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_RUTINA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_RUTINA)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIEMPO_OBJETIVO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.INTENSIDAD)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DESCANSO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIA_PREFERENTE))
                )
            }
            cursor.close()
        }catch (e: SQLiteException) {
            Log.e("SQLite", "Error al obtener la rutina", e)
        }
        return rutina


    }
}