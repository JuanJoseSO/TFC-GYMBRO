package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Dieta
import com.example.tfc.sqlite.DatabaseHelper

class DietaDb(private val dbHelper: DatabaseHelper){

    fun addDieta(dieta: Dieta) {
        val db= dbHelper.writableDatabase
        try{
            val insert= ContentValues().apply {
                put(DatabaseHelper.NOMBRE_DIETA,dieta.nombre)
                put(DatabaseHelper.NIVEL_DIETA,dieta.nivel)
                put(DatabaseHelper.IMAGEN_DIETA,dieta.imagen)
            }
            db.insert(DatabaseHelper.TABLA_DIETAS, null, insert)
        }catch (e: SQLiteException) {
            Log.e("SQLite","Error al añadir la dieta")
        }

    }

    @SuppressLint("Range")
    fun getDieta(id: Int): Dieta? {
        val db=dbHelper.readableDatabase
        var dieta: Dieta? = null

        try {
            //Obtenemos todas las columnas de la tabla
            val cursor = db.query(
                DatabaseHelper.TABLA_DIETAS,
                null,
                "${DatabaseHelper.ID_DIETA} = ?",
                arrayOf(id.toString()),//Esto incluye el id en la consulta
                null,
                null,
                null
            )

            //Movemos el cursor al inicio y creamos un objeto usuario con todos sus datos
            if (cursor.moveToFirst()) {
                dieta = Dieta(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_DIETA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_DIETA)),// ID del usuario
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NIVEL_DIETA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGEN_DIETA))
                )
            }
            cursor.close()
        }catch (e: SQLiteException) {
            Log.e("SQLite", "Error al obtener usuario", e)
        }
        return dieta
    }

    @SuppressLint("Range")
    fun getDietas () : List<Dieta> {
        val listaDietas = ArrayList<Dieta>()
        val select = "SELECT * FROM ${DatabaseHelper.TABLA_DIETAS}"

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(select, null)

        if (cursor.moveToFirst()) {
            do {
                val dieta = Dieta(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_DIETA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_DIETA)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NIVEL_DIETA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGEN_DIETA))

                )
                listaDietas.add(dieta)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaDietas
    }
}