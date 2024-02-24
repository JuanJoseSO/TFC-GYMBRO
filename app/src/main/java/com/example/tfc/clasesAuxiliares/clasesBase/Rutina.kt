package com.example.tfc.clasesAuxiliares.clasesBase

//Dataclass con un constructor sin id,lo autoasigna la base de datos
data class Rutina(
    val id: Int =0,
    val nombre: String,
    val tiempoObjetivo:Int,
    val intensidad:String,
    val diaPreferente:String
) {
    constructor(
        nombre: String,
        tiempoObjetivo: Int,
        intensidad: String,
        diaPreferente:String
    ) : this(0, nombre, tiempoObjetivo, intensidad,diaPreferente)
}





