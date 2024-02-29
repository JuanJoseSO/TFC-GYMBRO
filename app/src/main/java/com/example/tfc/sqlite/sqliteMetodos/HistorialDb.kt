package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Sesion
import com.example.tfc.sqlite.DatabaseHelper


class HistorialDb(private val dbHelper: DatabaseHelper)  {
    //Metodos historial
    fun addSesion(sesion: Sesion) {
        val db = dbHelper.writableDatabase

        val insert = ContentValues().apply {
            put(DatabaseHelper.ID_RUTINA_FK, sesion.idRutina)
            put(DatabaseHelper.ID_USUARIO_FK, sesion.idUsuario)
            put(DatabaseHelper.DIA_ENTRENAMIENTO, sesion.dia)
            put(DatabaseHelper.HORA_INICIO, sesion.horaInicio)
            put(DatabaseHelper.TIEMPO_TOTAL, sesion.tiempoTotal)
            put(DatabaseHelper.CALORIAS_QUEMADAS, sesion.caloriasQuemadas)
        }
        try {
            db.insert(DatabaseHelper.TABLA_HISTORIAL, null, insert)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir ejercicios", e)
        }
    }
    //Solo recojo los datos que quiero para crear la lista

    @SuppressLint("Range")
    fun getSesiones(idUsuario:Int):List<Sesion>{
        val lista = mutableListOf<Sesion>()
        val db = dbHelper.readableDatabase
        val select= "SELECT ${DatabaseHelper.ID_HISTORIAL}, ${DatabaseHelper.ID_RUTINA_FK}, ${DatabaseHelper.DIA_ENTRENAMIENTO} FROM  ${DatabaseHelper.TABLA_HISTORIAL} WHERE ${DatabaseHelper.ID_USUARIO_FK}=?"
        val cursor = db.rawQuery(select, arrayOf(idUsuario.toString()))

        if (cursor.moveToFirst()) {
            do {
                val sesion = Sesion(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_HISTORIAL)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_RUTINA_FK)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIA_ENTRENAMIENTO))
                )
                lista.add(sesion)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    @SuppressLint("Range")
    fun getSesion(id:Int): Sesion? {
        var sesion: Sesion? = null
        val db = dbHelper.readableDatabase
        val select= "SELECT * FROM  ${DatabaseHelper.TABLA_HISTORIAL} WHERE ${DatabaseHelper.ID_HISTORIAL}=?"
        val cursor = db.rawQuery(select,arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            do {
                sesion = Sesion(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_HISTORIAL)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_RUTINA_FK)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_USUARIO_FK)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIA_ENTRENAMIENTO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.HORA_INICIO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIEMPO_TOTAL)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CALORIAS_QUEMADAS))
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return sesion
    }
}
