package com.example.tfc.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.tfc.clasesAuxiliares.Dieta
import com.example.tfc.clasesAuxiliares.Usuario
import com.example.tfc.clasesAuxiliares.Ejercicio
import com.example.tfc.clasesAuxiliares.Rutina

//No hay mucho que comentar,gestión de la base de datos,creación y métodos de consulta o persistencia
class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "GYMBROUsers"

        //Tabla Usuarios
        const val TABLA_USERS = "usuarios"
        const val ID_USUARIO = "id_usuario"
        const val NOMBRE_USUARIO = "nombre_usuario"
        const val EDAD = "edad"
        const val PESO = "peso"
        const val ALTURA = "altura"
        const val IMC = "IMC_usuario"
        const val GENERO = "genero"
        const val SELECCION = "isSelected"

        //Tabla ejercicios
        private const val TABLA_EJERCICIOS = "ejercicios"
        private const val ID_EJERCICIO = "id_ejercicio"
        private const val CATEGORIA_EJERCICIOS = "categoria"
        private const val NOMBRE_EJERCICIOS = "nombre_ejercicio"
        private const val YT_VIDEO = "yt_video"

        //Tabla rutina
        private const val TABLA_RUTINAS= "rutinas"
        private const val ID_RUTINA= "id_rutina"
        private const val NOMBRE_RUTINA= "nombre_rutina"
        private const val TIEMPO_OBJETIVO= "tiempo_objetivo"
        private const val INTENSIDAD= "intensidad"
        private const val DIA_PREFERENTE= "dia_preferente"

        //Tabla rutina_ejercicios,resultado de la relación N:M de estas
        private const val TABLA_RUTINA_EJERCICIOS= "rutina_ejercicios"
        private const val ID_RUTINA_FK= "id_rutina"
        private const val ID_EJERCICIO_FK= "id_ejercicio"
        private const val HORA_INICIO= "hora_inicio"
        private const val TIEMPO_ENTRENAMIENTO= "tiempo_entrenamiento"
        private const val CALORIAS_QUEMADAS= "calorias_quemadas"

        //Tabla usuario-rutina
        //"id_rutina_fk"
        private const val TABLA_USUARIOS_RUTINAS= "usuarios_rutinas"
        private const val ID_USUARIO_FK= "id_ejercicio"

        //Tabla dietas
        private const val TABLA_DIETAS="dietas"
        private const val ID_DIETA="id_dietas"
        private const val NOMBRE_DIETA="nombre_dieta"
        private const val NIVEL_DIETA="nivel_dieta"
        private const val IMAGEN_DIETA="imagen_dieta"
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

            val createRutinaTable = """
                CREATE TABLE $TABLA_RUTINAS (
                    $ID_RUTINA INTEGER PRIMARY KEY AUTOINCREMENT,
                    $NOMBRE_RUTINA TEXT,
                    $TIEMPO_OBJETIVO INTEGER,
                    $INTENSIDAD TEXT,
                    $DIA_PREFERENTE TEXT
                )                
            """.trimIndent()
            db?.execSQL(createRutinaTable)

            val createRutinaEjercicioTable = """
                   CREATE TABLE $TABLA_RUTINA_EJERCICIOS( 
                   $ID_RUTINA_FK INTEGER,
                   $ID_EJERCICIO_FK INTEGER,                                                             
                   PRIMARY KEY ($ID_RUTINA_FK,$ID_EJERCICIO_FK),
                   FOREIGN KEY ($ID_RUTINA_FK) REFERENCES $TABLA_RUTINAS($ID_RUTINA),
                   FOREIGN KEY ($ID_EJERCICIO_FK) REFERENCES $TABLA_EJERCICIOS($ID_EJERCICIO)
                )
                """.trimIndent()
            db?.execSQL(createRutinaEjercicioTable)

            val createUsuariosRutinasTable = """
                   CREATE TABLE $TABLA_USUARIOS_RUTINAS(      
                   $ID_USUARIO_FK INTEGER,                                 
                   $ID_RUTINA_FK INTEGER,       
                   $HORA_INICIO TIME,
                   $TIEMPO_ENTRENAMIENTO INTEGER,
                   $CALORIAS_QUEMADAS INTEGER,  
                   PRIMARY KEY ($ID_USUARIO_FK,$ID_RUTINA_FK),               
                   FOREIGN KEY ($ID_USUARIO_FK) REFERENCES $TABLA_USERS($ID_USUARIO),
                   FOREIGN KEY ($ID_RUTINA_FK) REFERENCES $TABLA_RUTINAS($ID_RUTINA)
                )
                """.trimIndent()
            db?.execSQL(createUsuariosRutinasTable)

            //Jugaremos con 0,1 y 2 en nivel_dieta para asisnar 3 niveles distintos
            val createDietaTable="""
                CREATE TABLE $TABLA_DIETAS (
                $ID_DIETA INTEGER PRIMARY KEY AUTOINCREMENT,
                $NOMBRE_DIETA TEXT,
                $NIVEL_DIETA INTEGER, 
                $IMAGEN_DIETA TEXT
                )
            """.trimIndent()
            db?.execSQL(createDietaTable)

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


    //**************** MÉTODOS RUTINA ********************
    fun addRutina(rutina : Rutina){
        val db = this.writableDatabase
        try {
            val values = ContentValues().apply {
                put(NOMBRE_RUTINA, rutina.nombre)
                put(TIEMPO_OBJETIVO, rutina.tiempoObjetivo)
                put(INTENSIDAD, rutina.intensidad)
                put(DIA_PREFERENTE,rutina.diaPreferente)
            }

            db.insert(TABLA_RUTINAS, null, values)
        }catch (e:SQLiteException) {
            Log.e("SQLite", "Error al añadir ejercicios", e)
        }
    }
    @SuppressLint("Range")
    fun getRutinas():List<Rutina>{
            val listaRutina = ArrayList<Rutina>()
            val selectQuery = "SELECT * FROM $TABLA_RUTINAS"

            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val rutina = Rutina(
                        cursor.getInt(cursor.getColumnIndex(ID_RUTINA)), // ID del usuario
                        cursor.getString(cursor.getColumnIndex(NOMBRE_RUTINA)), // Nombre de Usuario
                        cursor.getInt(cursor.getColumnIndex(TIEMPO_OBJETIVO)), // Edad
                        cursor.getString(cursor.getColumnIndex(INTENSIDAD)), // Peso
                        cursor.getString(cursor.getColumnIndex(DIA_PREFERENTE)) // Altura

                    )
                    listaRutina.add(rutina)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return listaRutina
        }

    //**************MÉTODOS DIETA*********************
    fun addDieta(dieta: Dieta) {
        val db= this.writableDatabase
        try{
            val values=ContentValues().apply {
                put(NOMBRE_DIETA,dieta.nombre)
                put(NIVEL_DIETA,dieta.nivel)
                put(IMAGEN_DIETA,dieta.imagen)
            }
            db.insert(TABLA_DIETAS, null, values)
        }catch (e:SQLiteException) {
            Log.e("SQLite","Error al añadir la dieta")
        }

    }

    @SuppressLint("Range")
    fun getDieta(id: Int): Dieta? {
        val db=this.readableDatabase
        var dieta: Dieta? = null

        try {
            //Obtenemos todas las columnas de la tabla
            val cursor = db.query(
                TABLA_DIETAS,
                null,
                "$ID_DIETA = ?",
                arrayOf(id.toString()),//Esto incluye el id en la consulta
                null,
                null,
                null
            )

            //Movemos el cursor al inicio y creamos un objeto usuario con todos sus datos
            if (cursor.moveToFirst()) {
                dieta = Dieta(
                    cursor.getInt(cursor.getColumnIndex(ID_DIETA)),
                    cursor.getString(cursor.getColumnIndex(NOMBRE_DIETA)),// ID del usuario
                    cursor.getInt(cursor.getColumnIndex(NIVEL_DIETA)),
                    cursor.getString(cursor.getColumnIndex(IMAGEN_DIETA))
                )
            }
            cursor.close()
        }catch (e:SQLiteException) {
            Log.e("SQLite", "Error al obtener usuario", e)
        }
        return dieta
    }

    @SuppressLint("Range")
    fun getDietas () : List<Dieta> {
            val listaDietas = ArrayList<Dieta>()
            val selectQuery = "SELECT * FROM $TABLA_DIETAS"

            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val dieta = Dieta(
                        cursor.getInt(cursor.getColumnIndex(ID_DIETA)),
                        cursor.getString(cursor.getColumnIndex(NOMBRE_DIETA)),
                        cursor.getInt(cursor.getColumnIndex(NIVEL_DIETA)),
                        cursor.getString(cursor.getColumnIndex(IMAGEN_DIETA))

                    )
                    listaDietas.add(dieta)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return listaDietas
    }
}










