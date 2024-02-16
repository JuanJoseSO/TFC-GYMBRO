package com.example.tfc.sqlite

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
        private const val SELECCION = "isSelected"

        //Tabla ejercicios
        private const val TABLA_EJERCICIOS = "ejercicios"
        private const val ID_EJERCICIO = "id_ejercicio"
        private const val CATEGORIA_EJERCICIOS = "categoria"
        private const val NOMBRE_EJERCICIOS = "nombre_ejercicio"
        private const val YT_VIDEO = "yt_video"
    }

    //Creamos las tablas
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val createUserTable = """
                    CREATE TABLE $TABLA_USERS (
                        $ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT,
                        $NOMBRE_USUARIO TEXT,
                        $EDAD INTEGER,
                        $PESO INTEGER,
                        $ALTURA INTEGER,
                        $IMC REAL,
                        $GENERO INTEGER,
                        $SELECCION INTEGER DEFAULT 0
                    )
             """.trimIndent()
            db?.execSQL(createUserTable)

            val createEjerciciosTable = """
                CREATE TABLE $TABLA_EJERCICIOS (
                    $ID_EJERCICIO INTEGER PRIMARY KEY AUTOINCREMENT,
                    $CATEGORIA_EJERCICIOS TEXT,
                    $NOMBRE_EJERCICIOS TEXT,
                    $YT_VIDEO TEXT
                )
            """.trimIndent()
            db?.execSQL(createEjerciciosTable)

            //Dos trigger que mantendra SOLO un usuario seleccionado a la vez ya que SQLite no permite INSERT OR UPDATE
            val createTriggerInsertUser="""
                CREATE TRIGGER insert_usuario_seleccionado
                AFTER INSERT ON $TABLA_USERS
                FOR EACH ROW
                WHEN NEW.$SELECCION=1
                BEGIN
                    UPDATE $TABLA_USERS
                    SET $SELECCION=0
                    WHERE $ID_USUARIO != NEW.$ID_USUARIO AND $SELECCION=1;
                END;                
            """.trimIndent()
            db?.execSQL(createTriggerInsertUser)

            val createTriggerUpdateUser="""
                CREATE TRIGGER update_usuario_seleccionado
                AFTER UPDATE ON $TABLA_USERS
                FOR EACH ROW
                WHEN NEW.$SELECCION=1
                BEGIN
                    UPDATE $TABLA_USERS
                    SET $SELECCION=0
                    WHERE $ID_USUARIO != NEW.$ID_USUARIO AND $SELECCION=1;
                END;                
            """.trimIndent()
            db?.execSQL(createTriggerUpdateUser)

        } catch (e: SQLiteException) {
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
            values.put(SELECCION, 1) //Siempre selecciona el último usuario creado

            db.insert(TABLA_USERS, null, values)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir usuario", e)
        }
    }

    //Contamos el número de usuarios y lo aplicaremos a la lógica de la aplicacion
    fun contarUsuarios(): Int {
        val db = this.readableDatabase
        var suma = 0
        try {
            val cursor = db.rawQuery("SELECT * FROM $TABLA_USERS", null)
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
        val selectQuery = "SELECT * FROM $TABLA_USERS"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(
                    cursor.getInt(cursor.getColumnIndex(ID_USUARIO)), // ID del usuario
                    cursor.getString(cursor.getColumnIndex(NOMBRE_USUARIO)), // Nombre de Usuario
                    cursor.getInt(cursor.getColumnIndex(EDAD)), // Edad
                    cursor.getInt(cursor.getColumnIndex(PESO)), // Peso
                    cursor.getInt(cursor.getColumnIndex(ALTURA)), // Altura
                    cursor.getDouble(cursor.getColumnIndex(IMC)), // IMC
                    cursor.getInt(cursor.getColumnIndex(GENERO)) != 0 // Género
                )
                listaUsuarios.add(usuario)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaUsuarios
    }

    fun seleccionUsuario(usuarioId: Int): Boolean {
        val db = this.writableDatabase
        var exito = false
        try {
            db.beginTransaction()
            //Selecciona al usuario
            val seleccionar = ContentValues().apply { put(SELECCION, 1) }
            val seleccionarUsuario = db.update(TABLA_USERS, seleccionar, "$ID_USUARIO = ?", arrayOf(usuarioId.toString()))

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
        val db = this.readableDatabase
        //Cursor para recoger solo los usuarios seleccionados
        val cursor = db.query(TABLA_USERS, null, "$SELECCION=1", null, null, null, null)

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {
            usuario = Usuario(
                cursor.getInt(cursor.getColumnIndex(ID_USUARIO)),
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


    //*****MÉTODOS EJERCICIOS
    fun addEjercicio(ejercicio: Ejercicio) {
        val db = this.writableDatabase
        try {
        val values = ContentValues().apply {
            put(CATEGORIA_EJERCICIOS, ejercicio.categoria)
            put(NOMBRE_EJERCICIOS, ejercicio.nombre)
            put(YT_VIDEO, ejercicio.video)
        }

        db.insertOrThrow(TABLA_EJERCICIOS, null, values)
    }catch (e:SQLiteException) {
            Log.e("SQLite", "Error al añadir ejercicios", e)
        }

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
            val cursor = db.rawQuery("SELECT DISTINCT $CATEGORIA_EJERCICIOS FROM $TABLA_EJERCICIOS", null)
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









