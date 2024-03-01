package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Ejercicio
import com.example.tfc.sqlite.DatabaseHelper

class EjerciciosDb(private val dbHelper: DatabaseHelper) {

    //*****MÉTODOS EJERCICIOS
    fun addEjercicio(ejercicio: Ejercicio) {
        val db = dbHelper.writableDatabase
        try {
            val insert = ContentValues().apply {
                put(DatabaseHelper.CATEGORIA_EJERCICIOS, ejercicio.categoria)
                put(DatabaseHelper.NOMBRE_EJERCICIOS, ejercicio.nombre)
                put(DatabaseHelper.YT_VIDEO, ejercicio.video)
            }

            db.insertOrThrow(DatabaseHelper.TABLA_EJERCICIOS, null, insert)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir ejercicios", e)
        }

    }

    @SuppressLint("Range")
    fun getEjercicio(id: Int): Ejercicio? {
        val db = dbHelper.readableDatabase
        var ejercicio: Ejercicio? = null
        try {
            //Obtenemos todas las columnas de la tabla
            val cursor = db.query(
                DatabaseHelper.TABLA_EJERCICIOS,
                null,
                "${DatabaseHelper.ID_EJERCICIO} = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )

            //Movemos el cursor al inicio y creamos un objeto usuario con todos sus datos
            if (cursor.moveToFirst()) {
                ejercicio = Ejercicio(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_EJERCICIO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORIA_EJERCICIOS)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_EJERCICIOS)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.YT_VIDEO))
                )
            }
            cursor.close()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al obtener usuario", e)
        }
        return ejercicio
    }

    fun obtenerCategorias(): List<String> {
        val lista = mutableListOf<String>()
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery(
                "SELECT DISTINCT ${DatabaseHelper.CATEGORIA_EJERCICIOS} FROM ${DatabaseHelper.TABLA_EJERCICIOS}",
                null
            )
            //Necesitamos el indice para asegurarnos de que no es -1,lo cual seria una excepcion que rompería la aplicación
            val indiceCategoria = cursor.getColumnIndex("categoria")
            if (indiceCategoria != -1 && cursor.moveToFirst()) {
                do {
                    val categoria = cursor.getString(indiceCategoria)
                    lista.add(categoria)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al obtenter las categorias", e)
        }
        return lista
    }

    //Obtenemos una lista de OBJETOS ejercicio recogiendo los distintos campos de la tabla
    fun getEjerciciosPorCategoria(categoria: String): List<Ejercicio> {
        val lista = mutableListOf<Ejercicio>()
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery(
                "SELECT * FROM ${DatabaseHelper.TABLA_EJERCICIOS} WHERE ${DatabaseHelper.CATEGORIA_EJERCICIOS} = ?",
                arrayOf(categoria)
            )
            //Necesitamos el indice para asegurarnos de que no es -1,lo cual seria una excepcion que rompería la aplicación
            val indiceId =
                cursor.getColumnIndex(DatabaseHelper.ID_EJERCICIO) // Asume que tu columna se llama "id"
            val indiceNombre = cursor.getColumnIndex(DatabaseHelper.NOMBRE_EJERCICIOS)
            val indiceVideo = cursor.getColumnIndex(DatabaseHelper.YT_VIDEO)
            if (indiceNombre != -1 && cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(indiceId)
                    val nombre = cursor.getString(indiceNombre)
                    val video = cursor.getString(indiceVideo)
                    lista.add(Ejercicio(id, categoria, nombre, video))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al obtenter las categorias", e)
        }
        return lista
    }
}