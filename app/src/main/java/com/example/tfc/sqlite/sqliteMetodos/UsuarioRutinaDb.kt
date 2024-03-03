package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Rutina
import com.example.tfc.sqlite.DatabaseHelper

class UsuarioRutinaDb(private val dbHelper: DatabaseHelper) {

    fun addRutinaAUsuario(idRutina: Int, idUsuario: Int) {
        val db = dbHelper.writableDatabase
        val insert = ContentValues().apply {
            put(DatabaseHelper.ID_RUTINA_FK, idRutina)
            put(DatabaseHelper.ID_USUARIO_FK, idUsuario)
        }
        try {
            db.insert(DatabaseHelper.TABLA_USUARIOS_RUTINAS, null, insert)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir  la rutina al usuario", e)
        }
    }

    @SuppressLint("Range")
    fun getRutinaPorUsuario(idUsuario: Int): MutableList<Rutina> {
        val listaRutina = mutableListOf<Rutina>()
        val db = dbHelper.readableDatabase
        val select =
            "SELECT * FROM ${DatabaseHelper.TABLA_RUTINAS} " + "JOIN ${DatabaseHelper.TABLA_USUARIOS_RUTINAS} ON " + "${DatabaseHelper.TABLA_RUTINAS}.${DatabaseHelper.ID_RUTINA}=" + "${DatabaseHelper.TABLA_USUARIOS_RUTINAS}.${DatabaseHelper.ID_RUTINA_FK} WHERE ${DatabaseHelper.ID_USUARIO_FK}= ? " + "ORDER BY ${DatabaseHelper.INTENSIDAD}"
        val cursor = db.rawQuery(select, arrayOf(idUsuario.toString()))

        if (cursor.moveToFirst()) {
            do {
                val rutina = Rutina(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_RUTINA)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_RUTINA)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIEMPO_OBJETIVO)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.INTENSIDAD)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DESCANSO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIA_PREFERENTE))
                )
                listaRutina.add(rutina)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaRutina
    }
}