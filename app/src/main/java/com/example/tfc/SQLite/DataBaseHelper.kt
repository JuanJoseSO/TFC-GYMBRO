package com.example.tfc.SQLite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.tfc.clasesAuxiliares.Usuario
import com.example.tfc.clasesAuxiliares.Ejercicio

//No hay mucho que comentar,gestión de la base de datos,creación y métodos de consulta o persistencia
class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "GYMBROUsers"

        //Tabla Usuarios
        private const val TABLA_USERS = "usuarios"
        private const val ID_USUARIO = "id_usuario"
        private const val NOMBRE_USUARIO = "nombre_usuario"
        private const val EDAD = "edad"
        private const val PESO = "peso"
        private const val ALTURA = "altura"
        private const val IMC = "IMC_usuario"
        private const val GENERO = "genero"

        //Tabla ejercicios
        private const val TABLA_EJERCICIOS = "ejercicios"
        private const val ID_EJERCICIO = "id_ejercicio"
        private const val CATEGORIA_EJERCICIOS = "categoria"
        private const val NOMBRE_EJERCICIOS = "nombre_ejercicio"
        private const val YT_VIDEO="yt_video"
    }

    //Creamos las tablas
    override fun onCreate(db: SQLiteDatabase?) {
         try {
             val CREATE_USERS_TABLE = """
                    CREATE TABLE $TABLA_USERS (
                        $ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT,
                        $NOMBRE_USUARIO TEXT,
                        $EDAD INTEGER,
                        $PESO INTEGER,
                        $ALTURA INTEGER,
                        $IMC REAL,
                        $GENERO INTEGER,
                        isSelected INTEGER DEFAULT 0
                    )
             """.trimIndent()
             //INTEGER para representar booleano
             db?.execSQL(CREATE_USERS_TABLE)

             val CREATE_EJERCICIOS_TABLE = """
                    CREATE TABLE $TABLA_EJERCICIOS (
                        $ID_EJERCICIO INTEGER PRIMARY KEY AUTOINCREMENT,
                        $CATEGORIA_EJERCICIOS TEXT,
                        $NOMBRE_EJERCICIOS TEXT,
                        $YT_VIDEO TEXT
                    )
             """.trimIndent()

             db?.execSQL(CREATE_EJERCICIOS_TABLE)
         }catch (e: SQLiteException) {
             Log.e("SQLite", "Error al crear las tablas", e)
         }
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLA_USERS")
        onCreate(db)
    }

    //******MÉTODOS TABLA USUARIO
    //Añadimos usuarios
    fun addUsuario(usuario: Usuario) {
        val db = this.writableDatabase
        try {
            //Guardamos el objeto usuario repartiendo la información en las distinta columnas
            val values = ContentValues()
            values.put(NOMBRE_USUARIO, usuario.nombreUsuario)
            values.put(EDAD, usuario.edad)
            values.put(PESO, usuario.peso)
            values.put(ALTURA, usuario.altura)
            values.put(IMC, usuario.imc)
            values.put(
                GENERO,
                if (usuario.genero) 1 else 0
            ) //El genero será un 1 o un 0 dependiendo de la opcion elegida
            values.put(
                "isSelected",
                if (contarUsuarios() == 0) 1 else 0
            ) //Selecciona el ultimo usuario creado por defecto

            db.insert(TABLA_USERS, null, values)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir usuario", e)

        }
    }

    //Recogemos la informacion del usuario
    @SuppressLint("Range")
    fun getUsuario(): Usuario? {
        val db = this.readableDatabase
        var usuario: Usuario?= null

        try {
        //Obtenemos todas las columnas de la tabla
        val cursor = db.query(TABLA_USERS, null, "isSelected=1", null, null, null, null)
            //Movemos el cursor al inicio y creamos un objeto usuario con todos sus datos
            if (cursor.moveToFirst()) {
                usuario = Usuario(
                    cursor.getString(cursor.getColumnIndex(NOMBRE_USUARIO)),
                    cursor.getInt(cursor.getColumnIndex(EDAD)),
                    cursor.getInt(cursor.getColumnIndex(PESO)),
                    cursor.getInt(cursor.getColumnIndex(ALTURA)),
                    cursor.getDouble(cursor.getColumnIndex(IMC)),
                    cursor.getInt(cursor.getColumnIndex(GENERO)) != 0
                )
            }
            cursor.close()
        }catch (e:SQLiteException){
            Log.e("SQLite", "Error al obtener usuario", e)
        }
        return usuario
    }

    //Contamos el número de usuarios y lo aplicaremos a la lógica de la aplicacion
    fun contarUsuarios(): Int {
        val db = this.readableDatabase
        var suma=0
        try {
            val cursor = db.rawQuery("SELECT * FROM $TABLA_USERS", null)
            suma = cursor.count
            cursor.close()
        }catch (e:SQLiteException) {
            Log.e("SQLite", "Error al contar usuarios", e)
        }
        return suma
    }


    //*****MÉTODOS EJERCICIOS
    fun addEjercicio(ejercicio: Ejercicio) {
        val db = this.writableDatabase
       // try {
            val values = ContentValues().apply {
                put(CATEGORIA_EJERCICIOS, ejercicio.categoria)
                put(NOMBRE_EJERCICIOS, ejercicio.nombre)
                put(YT_VIDEO,ejercicio.video)
            }

            db.insertOrThrow(TABLA_EJERCICIOS, null, values)
      //  }catch (e:SQLiteException) {
        //    Log.e("SQLite", "Error al añadir ejercicios", e)
        //}
    }


    @SuppressLint("Range")
    fun getEjercicio(id:Int): Ejercicio? {
        val db = this.readableDatabase
        var ejercicio: Ejercicio? = null

        try {
            //Obtenemos todas las columnas de la tabla
            val cursor = db.query(TABLA_EJERCICIOS,
                null,
                "$ID_EJERCICIO = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )

            //Movemos el cursor al inicio y creamos un objeto usuario con todos sus datos
            if (cursor.moveToFirst()) {
                ejercicio = Ejercicio(
                    cursor.getInt(cursor.getColumnIndex(ID_EJERCICIO)),
                    cursor.getString(cursor.getColumnIndex(CATEGORIA_EJERCICIOS)),
                    cursor.getString(cursor.getColumnIndex(NOMBRE_EJERCICIOS)),
                    cursor.getString(cursor.getColumnIndex(YT_VIDEO))
                )
            }
            cursor.close()
        }catch (e:SQLiteException) {
            Log.e("SQLite", "Error al obtener usuario", e)
        }
        return ejercicio
    }


    fun obtenerCategorias(): List<String> {
        val lista = mutableListOf<String>()
        val db = this.readableDatabase
        try {
            val cursor = db.rawQuery("SELECT DISTINCT "+CATEGORIA_EJERCICIOS+" FROM "+ TABLA_EJERCICIOS, null)
            //Necesitamos el indice para asegurarnos de que no es -1,lo cual seria una excepcion que rompería la aplicación
            val indiceCategoria = cursor.getColumnIndex("categoria")
            if (indiceCategoria != -1 && cursor.moveToFirst()) {
                do {
                    val categoria = cursor.getString(indiceCategoria)
                    lista.add(categoria)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }catch (e:SQLiteException) {
            Log.e("SQLite", "Error al obtenter las categorias", e)
        }
        return lista
    }

    //Obtenemos una lista de OBJETOS ejercicio recogiendo los distintos campos de la tabla
    fun getEjerciciosPorCategoria(categoria: String): List<Ejercicio> {
        val lista = mutableListOf<Ejercicio>()
        val db = this.readableDatabase
        try {
            val cursor = db.rawQuery(
                "SELECT * FROM $TABLA_EJERCICIOS WHERE $CATEGORIA_EJERCICIOS = ?",
                arrayOf(categoria)
            )
            //Necesitamos el indice para asegurarnos de que no es -1,lo cual seria una excepcion que rompería la aplicación
            val indiceId = cursor.getColumnIndex(ID_EJERCICIO) // Asume que tu columna se llama "id"
            val indiceNombre = cursor.getColumnIndex(NOMBRE_EJERCICIOS)
            val indiceVideo = cursor.getColumnIndex(YT_VIDEO)
            if (indiceNombre != -1 && cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(indiceId)
                    val nombre = cursor.getString(indiceNombre)
                    val video =cursor.getString(indiceVideo)
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







    //*************ESTO SERIA SI QUISIERAMOS MÁS DE UN USUARIO POR DISPOSITIVO
    /*
    //Regemos la informacion de un usuario
    fun getUsuario(id: Int): Usuario {
        val db = this.readableDatabase

        val cursor = db.query(TABLA_USERS, arrayOf(ID_USUARIO, NOMBRE_USUARIO, EDAD, PESO, ALTURA, IMC, GENERO), "$ID_USUARIO=?",
            arrayOf(id.toString()), null, null, null, null)

        cursor?.moveToFirst()

        val user = Usuario(
            cursor.getString(1), // Nombre Usuario
            cursor.getInt(2), // Edad
            cursor.getInt(3), // Peso
            cursor.getInt(4), // Altura
            cursor.getDouble(5), // IMC
            cursor.getInt(6) != 0 // esHombre, convierte 1 o 0 a Boolean
        )
        cursor.close()
        return user
    }



    // Función para obtener todos los usuarios
    fun getUsuarios(): List<Usuario> {
        val listaUsuarios = ArrayList<Usuario>()
        val selectQuery = "SELECT * FROM $TABLA_USERS"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(
                    cursor.getString(1), // Nombre de Usuario
                    cursor.getInt(2), // Edad
                    cursor.getInt(3), // Peso
                    cursor.getInt(4), // Altura
                    cursor.getDouble(5), // IMC
                    cursor.getInt(6) != 0 // Genero
                )
                listaUsuarios.add(usuario)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaUsuarios
    }

    fun seleccionUsuario(usuarioId:Int){
        val db=this.writableDatabase

        //Primero deselecciono todos los usuarios
        val deseleccion=ContentValues().apply {
            put("isSelected",0)
        }
        //Actualizo la seleccion a 0 en toda la BBDD
        db.update(TABLA_USERS,deseleccion,null,null)

        //Seleccionamos a un usuario
        val seleccion=ContentValues().apply {
            put("isSelected",1)
        }
        db.update(TABLA_USERS,seleccion,"$ID_USUARIO=?", arrayOf(usuarioId.toString()))

        db.close()


    }

    @SuppressLint("Range")
    fun getUsuarioSeleccionado():Usuario?{
        val db=this.readableDatabase
        //Cursor para recoger solo los usuarios seleccionados
        val cursor=db.query(TABLA_USERS,null,"isSelected=1",null,null,null,null)

        var usuario:Usuario?=null

        if(cursor.moveToFirst()){
            usuario= Usuario(
                cursor.getString(cursor.getColumnIndex(NOMBRE_USUARIO)), // Obtener el nombre de usuario
                cursor.getInt(cursor.getColumnIndex(EDAD)), // Obtener la edad
                cursor.getInt(cursor.getColumnIndex(PESO)), // Obtener el peso
                cursor.getInt(cursor.getColumnIndex(ALTURA)), // Obtener la altura
                cursor.getDouble(cursor.getColumnIndex(IMC)), // Obtener el IMC
                cursor.getInt(cursor.getColumnIndex(GENERO)) != 0 // Obtener el género (convertido de entero a booleano)
            )
        }
        cursor.close()
        return usuario
    }
    */
