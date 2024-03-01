package com.example.tfc.clasesAuxiliares.clasesBase

//Dataclass con un constructor sin id,lo autoasigna la base de datos
data class Rutina(
    val id: Int = 0,
    val nombre: String,
    val tiempoObjetivo: Int,
    val intensidad: String,
    val descanso: Int,
    val diaPreferente: String
) {
    constructor(
        nombre: String,
        tiempoObjetivo: Int,
        intensidad: String,
        descanso: Int,
        diaPreferente: String
    ) : this(0, nombre, tiempoObjetivo, intensidad, descanso, diaPreferente)
}