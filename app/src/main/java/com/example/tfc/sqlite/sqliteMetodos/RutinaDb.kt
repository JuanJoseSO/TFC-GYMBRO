package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.sqlite.DatabaseHelper

class RutinaDb(private val dbHelper: DatabaseHelper) {

    //Crear rutina devuelve un long con su id para añadirlo a la tabla usuarios_rutina
    fun addRutina(rutina: Rutina): Int {
        val db = dbHelper.writableDatabase

        val insert = ContentValues().apply {
            put(DatabaseHelper.NOMBRE_RUTINA, rutina.nombre)
            put(DatabaseHelper.TIEMPO_OBJETIVO, rutina.tiempoObjetivo)
            put(DatabaseHelper.INTENSIDAD, rutina.intensidad)
            put(DatabaseHelper.DESCANSO, rutina.descanso)
            put(DatabaseHelper.DIA_PREFERENTE, rutina.diaPreferente)
        }
        try {
            return db.insert(DatabaseHelper.TABLA_RUTINAS, null, insert).toInt()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir ejercicios", e)
        }
        return -1
    }

    @SuppressLint("Range")
    fun getRutina(id: Int): Rutina? {
        val db = dbHelper.readableDatabase
        var rutina: Rutina? = null
        try {
            //Obtenemos una rutina por su id
            val cursor = db.query(
                DatabaseHelper.TABLA_RUTINAS,
                null,
                "${DatabaseHelper.ID_RUTINA} = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
            if (cursor.moveToFirst()) {
                rutina = Rutina(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_RUTINA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_RUTINA)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIEMPO_OBJETIVO)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.INTENSIDAD)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DESCANSO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIA_PREFERENTE))
                )
            }
            cursor.close()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al obtener la rutina", e)
        }
        return rutina
    }

    fun eliminarRutina(idRutina: Int) {
        val db = dbHelper.writableDatabase
        try {
            db.delete(
                DatabaseHelper.TABLA_RUTINAS,
                "${DatabaseHelper.ID_RUTINA}=?",
                arrayOf(idRutina.toString())
            )
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al cambiar la posicion del ejercicio", e)
        } finally {
            db.close()
        }
    }
}