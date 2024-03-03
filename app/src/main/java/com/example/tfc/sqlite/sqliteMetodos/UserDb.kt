package com.example.tfc.sqlite.sqliteMetodos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.clasesBase.Usuario
import com.example.tfc.sqlite.DatabaseHelper

class UserDb(private val dbHelper: DatabaseHelper) {

    //******MÉTODOS TABLA USUARIO
    //Añadimos usuarios,devolvemos el id para autoasignar las tres rutinas base cada vez que se crea un usuario
    fun addUsuario(usuario: Usuario): Int {
        val db = dbHelper.writableDatabase
        var userId = 0
        try {
            //Guardamos el objeto usuario repartiendo la información en las distinta columnas
            val values = ContentValues()
            values.put(DatabaseHelper.NOMBRE_USUARIO, usuario.nombreUsuario)
            values.put(DatabaseHelper.OBJETIVO_DIARIO, usuario.objetivoDiario)
            values.put(DatabaseHelper.PESO, usuario.peso)
            values.put(DatabaseHelper.ALTURA, usuario.altura)
            values.put(DatabaseHelper.IMC, usuario.imc)
            values.put(
                DatabaseHelper.GENERO, if (usuario.genero) 1 else 0
            ) //El genero será un 1 o un 0 dependiendo de la opcion elegida
            values.put(DatabaseHelper.SELECCION, 1) //Siempre selecciona el último usuario creado

            userId = db.insert(DatabaseHelper.TABLA_USERS, null, values).toInt()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir usuario", e)
        }
        return userId
    }

    //Contamos el número de usuarios y lo aplicaremos a la lógica de la aplicacion
    fun contarUsuarios(): Int {
        val db = dbHelper.readableDatabase
        var suma = 0
        try {
            val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLA_USERS}", null)
            suma = cursor.count
            cursor.close()
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al contar usuarios", e)
        }
        return suma
    }

    // Función para obtener todos los usuarios
    @SuppressLint("Range")
    fun getUsuarios(): List<Usuario> {
        val listaUsuarios = ArrayList<Usuario>()
        val select = "SELECT * FROM ${DatabaseHelper.TABLA_USERS}"

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(select, null)

        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_USUARIO)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_USUARIO)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.OBJETIVO_DIARIO)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PESO)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ALTURA)),
                    cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.IMC)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GENERO)) != 0
                )
                listaUsuarios.add(usuario)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaUsuarios
    }

    fun seleccionUsuario(usuarioId: Int): Boolean {
        val db = dbHelper.writableDatabase
        var exito = false
        try {
            db.beginTransaction()
            //Selecciona al usuario
            val seleccionar = ContentValues().apply { put(DatabaseHelper.SELECCION, 1) }
            val seleccionarUsuario = db.update(
                DatabaseHelper.TABLA_USERS,
                seleccionar,
                "${DatabaseHelper.ID_USUARIO} = ?",
                arrayOf(usuarioId.toString())
            )
            if (seleccionarUsuario > 0) exito = true

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBError", "Error al seleccionar usuario", e)
        } finally {
            db.endTransaction()
        }
        return exito
    }

    @SuppressLint("Range")
    fun getUsuarioSeleccionado(): Usuario? {
        val db = dbHelper.readableDatabase
        //Cursor para recoger solo los usuarios seleccionados
        val cursor = db.query(
            DatabaseHelper.TABLA_USERS,
            null,
            "${DatabaseHelper.SELECCION}=1",
            null,
            null,
            null,
            null
        )

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {
            usuario = Usuario(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_USUARIO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_USUARIO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.OBJETIVO_DIARIO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PESO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ALTURA)),
                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.IMC)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GENERO)) != 0
            )
        }
        cursor.close()
        return usuario
    }

    fun eliminarUsuario() {
        val db = dbHelper.writableDatabase
        //Eliminamos el usuario seleccionado
        db.delete(
            DatabaseHelper.TABLA_USERS, "${DatabaseHelper.SELECCION}=?", arrayOf("1")
        )
    }

    fun updateUsuario(idUsuario: Int, usuario: Usuario) {
        val db = dbHelper.writableDatabase
        try {
            //Guardamos el objeto usuario repartiendo la información en las distinta columnas
            val values = ContentValues()
            values.put(DatabaseHelper.NOMBRE_USUARIO, usuario.nombreUsuario)
            values.put(DatabaseHelper.OBJETIVO_DIARIO, usuario.objetivoDiario)
            values.put(DatabaseHelper.PESO, usuario.peso)
            values.put(DatabaseHelper.ALTURA, usuario.altura)
            values.put(DatabaseHelper.IMC, usuario.imc)
            values.put(
                DatabaseHelper.GENERO, if (usuario.genero) 1 else 0
            ) //El genero será un 1 o un 0 dependiendo de la opcion elegida
            values.put(DatabaseHelper.SELECCION, 1) //Siempre selecciona el último usuario creado

            db.update(
                DatabaseHelper.TABLA_USERS,
                values,
                "${DatabaseHelper.ID_USUARIO} = ?",
                arrayOf(idUsuario.toString())
            )
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir usuario", e)
        }
    }
}