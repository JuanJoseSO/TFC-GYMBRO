package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import com.example.tfc.clasesAuxiliares.clasesBase.HistorialPeso
import com.example.tfc.sqlite.DatabaseHelper

@SuppressLint("Range")
class HistorialPesoDb(private val dbHelper: DatabaseHelper) {
    fun getHistorialPesoPorUsuario(idUser: Int): MutableList<HistorialPeso> {
        val lista = mutableListOf<HistorialPeso>()

        val db = dbHelper.readableDatabase

        val select = """
    SELECT ${DatabaseHelper.TABLA_EJERCICIOS}.${DatabaseHelper.NOMBRE_EJERCICIOS},
           ${DatabaseHelper.TABLA_HISTORIAL_PESO}.${DatabaseHelper.PESO_ANTERIOR},
           ${DatabaseHelper.TABLA_HISTORIAL_PESO}.${DatabaseHelper.PESO_ACTUAL},
           ${DatabaseHelper.TABLA_HISTORIAL_PESO}.${DatabaseHelper.FECHA}
    FROM ${DatabaseHelper.TABLA_HISTORIAL_PESO}
    JOIN ${DatabaseHelper.TABLA_USUARIO_RUTINA_EJERCICIO} ON ${DatabaseHelper.TABLA_HISTORIAL_PESO}.${DatabaseHelper.ID_ENTRENAMIENTO} = ${DatabaseHelper.TABLA_USUARIO_RUTINA_EJERCICIO}.${DatabaseHelper.ID_ENTRENAMIENTO}
    JOIN ${DatabaseHelper.TABLA_EJERCICIOS} ON ${DatabaseHelper.TABLA_USUARIO_RUTINA_EJERCICIO}.${DatabaseHelper.ID_EJERCICIO_FK} = ${DatabaseHelper.TABLA_EJERCICIOS}.${DatabaseHelper.ID_EJERCICIO}
    WHERE ${DatabaseHelper.TABLA_USUARIO_RUTINA_EJERCICIO}.${DatabaseHelper.ID_USUARIO_FK}=?
""".trimIndent()

        val cursor = db.rawQuery(select, arrayOf(idUser.toString()))

        if (cursor.moveToFirst()) {
            do {
                val historialPeso = HistorialPeso(
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_EJERCICIOS)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PESO_ANTERIOR)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PESO_ACTUAL)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.FECHA))
                )
                lista.add(historialPeso)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}