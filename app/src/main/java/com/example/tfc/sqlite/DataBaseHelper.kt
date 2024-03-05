package com.example.tfc.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//No hay mucho que comentar,gestión de la base de datos,creación de tablas
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "GYMBROUsers"

        //Tabla Usuarios
        const val TABLA_USERS = "usuarios"
        const val ID_USUARIO = "id_usuario"
        const val NOMBRE_USUARIO = "nombre_usuario"
        const val OBJETIVO_DIARIO = "objetivoDiario"
        const val PESO = "peso"
        const val ALTURA = "altura"
        const val IMC = "IMC_usuario"
        const val GENERO = "genero"
        const val SELECCION = "isSelected"

        //Tabla ejercicios
        const val TABLA_EJERCICIOS = "ejercicios"
        const val ID_EJERCICIO = "id_ejercicio"
        const val CATEGORIA_EJERCICIOS = "categoria"
        const val NOMBRE_EJERCICIOS = "nombre_ejercicio"
        const val YT_VIDEO = "yt_video"

        //Tabla rutina
        const val TABLA_RUTINAS = "rutinas"
        const val ID_RUTINA = "id_rutina"
        const val NOMBRE_RUTINA = "nombre_rutina"
        const val TIEMPO_OBJETIVO = "tiempo_objetivo"
        const val INTENSIDAD = "intensidad"
        const val DESCANSO = "descanso"
        const val DIA_PREFERENTE = "dia_preferente"

        //Tabla rutina_ejercicios,resultado de la relación N:M de esta
        const val TABLA_ENTRENAMIENTO = "entrenamiento"
        const val ID_ENTRENAMIENTO = "id_entrenamiento"
        const val ID_USUARIO_FK = "id_usuario"
        const val ID_RUTINA_FK = "id_rutina"
        const val ID_EJERCICIO_FK = "id_ejercicio"
        const val SERIES = "series"
        const val REPETICIONES = "repeticiones"
        const val PESO_SERIE = "peso"
        const val ORDEN = "orden"

        //Tabla dietas
        const val TABLA_DIETAS = "dietas"
        const val ID_DIETA = "id_dietas"
        const val NOMBRE_DIETA = "nombre_dieta"
        const val NIVEL_DIETA = "nivel_dieta"
        const val IMAGEN_DIETA = "imagen_dieta"

        //Tabla historial
        const val TABLA_HISTORIAL = "historial"
        const val ID_HISTORIAL = "id_historial"

        //id_usuario_fk
        //id_rutina_fk
        const val DIA_ENTRENAMIENTO = "dia_entrenamiento"
        const val HORA_INICIO = "hora_inicio"
        const val TIEMPO_TOTAL = "tiempo_de_entrenamiento"
        const val CALORIAS_QUEMADAS = "calorias_quemadas"

        //Tabla evolucion
        const val TABLA_EVOLUCION = "evolucion"
        const val ID_EVOLUCION = "id_evolucion"
        //usuario_fk
        //ejercicio_fk
        const val PESO_ANTERIOR = "peso_anterior"
        const val PESO_ACTUAL = "peso_actual"
        const val FECHA = "fecha"
    }

    //Creamos las tablas
    override fun onCreate(db: SQLiteDatabase?) {
        try {

            val createUserTable = """
                    CREATE TABLE $TABLA_USERS (
                        $ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT,
                        $NOMBRE_USUARIO TEXT,
                        $OBJETIVO_DIARIO INTEGER,
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
                    $INTENSIDAD INTEGER,
                    $DESCANSO INTEGER,
                    $DIA_PREFERENTE TEXT
                )                
            """.trimIndent()
            db?.execSQL(createRutinaTable)

            val createRutinaEjercicioTable = """
                   CREATE TABLE $TABLA_ENTRENAMIENTO( 
                   $ID_ENTRENAMIENTO INTEGER PRIMARY KEY AUTOINCREMENT,
                   $ID_RUTINA_FK INTEGER,
                   $ID_EJERCICIO_FK INTEGER,
                   $ID_USUARIO_FK INTEGER,
                   $SERIES INTEGER,
                   $REPETICIONES INTEGER,
                   $PESO_SERIE REAL,
                   $ORDEN INTEGER,
                   FOREIGN KEY ($ID_USUARIO_FK) REFERENCES $TABLA_USERS($ID_USUARIO),
                   FOREIGN KEY ($ID_RUTINA_FK) REFERENCES $TABLA_RUTINAS($ID_RUTINA),
                   FOREIGN KEY ($ID_EJERCICIO_FK) REFERENCES $TABLA_EJERCICIOS($ID_EJERCICIO)
                )
                """.trimIndent()
            db?.execSQL(createRutinaEjercicioTable)

            //Jugaremos con 0,1 y 2 en nivel_dieta para asisnar 3 niveles distintos
            val createDietaTable = """
                CREATE TABLE $TABLA_DIETAS (
                $ID_DIETA INTEGER PRIMARY KEY AUTOINCREMENT,
                $NOMBRE_DIETA TEXT,
                $NIVEL_DIETA INTEGER, 
                $IMAGEN_DIETA TEXT
                )
            """.trimIndent()
            db?.execSQL(createDietaTable)

            val createTableHistorial = """
                 CREATE TABLE $TABLA_HISTORIAL(
                   $ID_HISTORIAL INTEGER PRIMARY KEY AUTOINCREMENT,
                   $ID_USUARIO_FK INTEGER,
                   $ID_RUTINA_FK INTEGER,
                   $DIA_ENTRENAMIENTO TEXT,
                   $HORA_INICIO TEXT,
                   $TIEMPO_TOTAL TEXT,
                   $CALORIAS_QUEMADAS INTEGER,
                   FOREIGN KEY ($ID_USUARIO_FK) REFERENCES $TABLA_USERS($ID_USUARIO),
                   FOREIGN KEY ($ID_RUTINA_FK) REFERENCES $TABLA_RUTINAS($ID_RUTINA)
                )                
            """.trimIndent()
            db?.execSQL(createTableHistorial)

            val createTableEvolucion = """
                 CREATE TABLE $TABLA_EVOLUCION(
                   $ID_EVOLUCION INTEGER PRIMARY KEY AUTOINCREMENT,
                   $ID_RUTINA_FK INTEGER,
                   $ID_EJERCICIO_FK INTEGER,
                   $PESO_ANTERIOR INTEGER,
                   $PESO_ACTUAL  INTEGER,
                   $FECHA INTEGER,
                   FOREIGN KEY ($ID_RUTINA_FK) REFERENCES $TABLA_RUTINAS($ID_RUTINA_FK),
                   FOREIGN KEY ($ID_EJERCICIO_FK) REFERENCES $TABLA_EJERCICIOS($ID_EJERCICIO)
                )                
            """.trimIndent()
            db?.execSQL(createTableEvolucion)
            //Un trigger que rellenará de fomra automática la tabla evolución
            val createTriggerUpdateEvolucion = """
                CREATE TRIGGER update_peso
                AFTER UPDATE ON $TABLA_ENTRENAMIENTO
                FOR EACH ROW
                WHEN OLD.$PESO <> NEW.$PESO
                BEGIN
                    INSERT INTO $TABLA_EVOLUCION($ID_RUTINA_FK,$ID_EJERCICIO_FK,$PESO_ANTERIOR,$PESO_ACTUAL,$FECHA)
                    VALUES (NEW.$ID_RUTINA_FK,NEW.$ID_EJERCICIO_FK,OLD.$PESO,NEW.$PESO,strftime('%s','now'));
                END;
            """.trimIndent()
            db?.execSQL(createTriggerUpdateEvolucion)
            //Dos trigger que mantendra SOLO un usuario seleccionado a la vez ya que SQLite no permite INSERT OR UPDATE
            val createTriggerInsertUser = """
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

            val createTriggerUpdateUser = """
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
    }
}