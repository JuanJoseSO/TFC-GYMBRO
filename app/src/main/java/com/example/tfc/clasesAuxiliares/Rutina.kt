package com.example.tfc.clasesAuxiliares

//Dataclass con un constructor sin id,lo autoasigna la base de datos
data class Rutina(
    val id:Int=0,
    val nombre: String,
    val tiempoObjetivo:Int,
    val intensidad:String
)





