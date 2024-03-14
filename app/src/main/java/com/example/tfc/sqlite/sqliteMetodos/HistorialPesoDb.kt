package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteException
import android.util.Log
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
        ${DatabaseHelper.TABLA_HISTORIAL_PESO}.${DatabaseHelper.FECHA} FROM ${DatabaseHelper.TABLA_HISTORIAL_PESO}
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

    /* Esta función me ha costado hacerla de forma correcta por la forma en la que esta estructurada la base de datos,
    pero basicamente elimina el historial de ejercicios haciendo una especie de innerjoin con la tabla entrenamiento
    mediante una subconsulta, ya que sqlite al parecer no perminte usar innerjoin y delete en la misma clausula
     */
    fun borrarHistorialPeso(idUser: Int) {
        val db = dbHelper.writableDatabase
        try {
            // Construye la subconsulta para seleccionar los ID_ENTRENAMIENTO basados en el ID_USUARIO_FK
            val subQuery =
                "SELECT ${DatabaseHelper.ID_ENTRENAMIENTO} FROM ${DatabaseHelper.TABLA_USUARIO_RUTINA_EJERCICIO} WHERE ${DatabaseHelper.ID_USUARIO_FK} = ?"

            // Ejecuta la operación de DELETE usando la subconsulta en la cláusula WHERE
            db.delete(
                DatabaseHelper.TABLA_HISTORIAL_PESO,
                "${DatabaseHelper.ID_ENTRENAMIENTO} IN ($subQuery)",
                arrayOf(idUser.toString())
            )
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al borrar el historial", e)
        }
    }
}