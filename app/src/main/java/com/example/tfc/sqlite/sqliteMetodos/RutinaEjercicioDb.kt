package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio
import com.example.tfc.sqlite.DatabaseHelper


class RutinaEjercicioDb(private val dbHelper: DatabaseHelper) {

    fun addEjercicioARutina(
        idRutina: Int, idEjercicio: Int, repeticiones: Int, series: Int, peso: Double
    ) {
        val db = dbHelper.writableDatabase
        val insert = ContentValues().apply {
            put(DatabaseHelper.ID_RUTINA_FK, idRutina)
            put(DatabaseHelper.ID_EJERCICIO_FK, idEjercicio)
            put(DatabaseHelper.REPETICIONES, repeticiones)
            put(DatabaseHelper.SERIES, series)
            put(DatabaseHelper.PESO, peso)
        }
        try {
            db.insert(DatabaseHelper.TABLA_RUTINA_EJERCICIOS, null, insert)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir  el ejercicio", e)
        }
    }

    @SuppressLint("Range")
    fun getInfoRutina(idRutina: Int, idEjercicio: Int): ArrayList<Double> {
        val lista = ArrayList<Double>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLA_RUTINA_EJERCICIOS} WHERE ${DatabaseHelper.ID_RUTINA_FK}=? AND ${DatabaseHelper.ID_EJERCICIO_FK}=?",
            arrayOf(idRutina.toString(), idEjercicio.toString())
        )
        try {
            if (cursor.moveToFirst()) {
                do {
                    val series = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SERIES))
                    val repeticiones =
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.REPETICIONES))
                    val pesoSerie =
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.PESO_SERIE))
                    lista.add(series.toDouble())
                    lista.add(repeticiones.toDouble())
                    lista.add(pesoSerie)
                } while (cursor.moveToNext())
                cursor.close()
            }
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir  el ejercicio", e)
        }
        return lista
    }

    @SuppressLint("Range")
    fun getEjerciciosPorRutina(idRutina: Int): MutableList<Ejercicio> {
        val lista = mutableListOf<Ejercicio>()
        val db = dbHelper.readableDatabase
        val select =
            "SELECT * FROM ${DatabaseHelper.TABLA_EJERCICIOS} JOIN ${DatabaseHelper.TABLA_RUTINA_EJERCICIOS} ON " + "${DatabaseHelper.TABLA_EJERCICIOS}.${DatabaseHelper.ID_EJERCICIO}=" + "${DatabaseHelper.TABLA_RUTINA_EJERCICIOS}.${DatabaseHelper.ID_EJERCICIO_FK} " + "WHERE ${DatabaseHelper.ID_RUTINA_FK}= ? ORDER BY ${DatabaseHelper.ORDEN} ASC"
        val cursor = db.rawQuery(select, arrayOf(idRutina.toString()))
        if (cursor.moveToFirst()) {
            try {
                do {
                    val idEjercicio =
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_EJERCICIO))
                    val categoria =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORIA_EJERCICIOS))
                    val nombre =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_EJERCICIOS))
                    val video = cursor.getString(cursor.getColumnIndex(DatabaseHelper.YT_VIDEO))

                    lista.add(Ejercicio(idEjercicio, categoria, nombre, video))
                } while (cursor.moveToNext())
                cursor.close()
            } catch (e: SQLiteException) {
                Log.e("SQLite", "Error al añadir  el ejercicio", e)
            }
        }
        return lista
    }

    //Metodo para actualizar unicamente el peso de un ejercicio
    fun updatePeso(idRutina: Int, idEjercicio: Int, nuevoPeso: Double) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.PESO_SERIE, nuevoPeso)
        }
        try {
            db.update(
                DatabaseHelper.TABLA_RUTINA_EJERCICIOS,
                values,
                "${DatabaseHelper.ID_RUTINA_FK}=? AND ${DatabaseHelper.ID_EJERCICIO_FK}=?",
                arrayOf(
                    idRutina.toString(), idEjercicio.toString()
                )
            )
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir  el ejercicio", e)
        }
    }

    fun updateOrden(listaEjercicios: List<Ejercicio>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            listaEjercicios.forEachIndexed { nuevoOrden, ejercicio ->
                val values = ContentValues().apply {
                    put(DatabaseHelper.ORDEN, nuevoOrden)
                }
                db.update(
                    DatabaseHelper.TABLA_RUTINA_EJERCICIOS,
                    values,
                    "${DatabaseHelper.ID_EJERCICIO_FK}=?",
                    arrayOf(ejercicio.id.toString())
                )
            }
            db.setTransactionSuccessful()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al cambiar la posicion del ejercicio", e)
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun eliminarEjercicio(idEjercicio: Int) {
        val db = dbHelper.writableDatabase
        try {
            // Ejecuta la operación DELETE en la base de datos
            db.delete(
                DatabaseHelper.TABLA_RUTINA_EJERCICIOS,
                "${DatabaseHelper.ID_EJERCICIO_FK}=?",
                arrayOf(idEjercicio.toString())
            )
        }catch (e: SQLiteException) {
            Log.e("SQLite", "Error al cambiar la posicion del ejercicio", e)
        } finally {
            db.close()
        }
    }
}