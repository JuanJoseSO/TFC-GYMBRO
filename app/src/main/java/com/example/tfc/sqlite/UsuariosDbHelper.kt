package com.example.tfc.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.tfc.clasesAuxiliares.Usuario

private const val TABLA_USERS = "usuarios"
private const val ID_USUARIO = "id_usuario"
private const val NOMBRE_USUARIO = "nombre_usuario"
private const val EDAD = "edad"
private const val PESO = "peso"
private const val ALTURA = "altura"
private const val IMC = "IMC_usuario"
private const val GENERO = "genero"
private const val SELECCION = "isSelected"

class UsuariosDbHelper (private val dbHelper:DatabaseHelper){
    //Añadimos usuarios
    fun addUsuario(usuario: Usuario) {
        var db = dbHelper.writableDatabase
        try {
            //Guardamos el objeto usuario repartiendo la información en las distinta columnas
            val values = ContentValues()
            values.put(DatabaseHelper.NOMBRE_USUARIO, usuario.nombreUsuario)
            values.put(DatabaseHelper.EDAD, usuario.edad)
            values.put(DatabaseHelper.PESO, usuario.peso)
            values.put(DatabaseHelper.ALTURA, usuario.altura)
            values.put(DatabaseHelper.IMC, usuario.imc)
            values.put(
                DatabaseHelper.GENERO,
                if (usuario.genero) 1 else 0
            ) //El genero será un 1 o un 0 dependiendo de la opcion elegida
            values.put(DatabaseHelper.SELECCION, 1) //Siempre selecciona el último usuario creado

            db.insert(DatabaseHelper.TABLA_USERS, null, values)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir usuario", e)
        }
    }

    //Contamos el número de usuarios y lo aplicaremos a la lógica de la aplicacion
    fun contarUsuarios(): Int {
        var suma = 0
        try {
            val cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM ${DatabaseHelper.TABLA_USERS}", null)
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
        val selectQuery = "SELECT * FROM ${DatabaseHelper.TABLA_USERS}"

        val cursor = dbHelper.readableDatabase.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_USUARIO)), // ID del usuario
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_USUARIO)), // Nombre de Usuario
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EDAD)), // Edad
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PESO)), // Peso
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ALTURA)), // Altura
                    cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.IMC)), // IMC
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GENERO)) != 0 // Género
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
            val seleccionarUsuario = db.update(DatabaseHelper.TABLA_USERS, seleccionar, "${DatabaseHelper.ID_USUARIO} = ?", arrayOf(usuarioId.toString()))

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
        val cursor = db.query(DatabaseHelper.TABLA_USERS, null, "${DatabaseHelper.SELECCION}=1", null, null, null, null)

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {
            usuario = Usuario(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_USUARIO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_USUARIO)), // Obtener el nombre de usuario
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EDAD)), // Obtener la edad
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PESO)), // Obtener el peso
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ALTURA)), // Obtener la altura
                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.IMC)), // Obtener el IMC
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GENERO)) != 0 // Obtener el género (convertido de entero a booleano)
            )
        }
        cursor.close()
        return usuario
    }
}