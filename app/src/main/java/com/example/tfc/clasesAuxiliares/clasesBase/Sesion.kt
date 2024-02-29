package com.example.tfc.clasesAuxiliares.clasesBase

data class Sesion(
    val idHistorial:Int,
    val idRutina: Int,
    val idUsuario: Int?,
    val dia: String,
    val horaInicio: String?,
    val tiempoTotal: String?,
    val caloriasQuemadas: Int?
)
{
    constructor(
        idRutina: Int,
        idUsuario: Int,
        dia: String,
        horaInicio: String,
        tiempoTotal: String,
        caloriasQuemadas: Int
    ) : this(0, idRutina, idUsuario, dia, horaInicio, tiempoTotal,caloriasQuemadas)

    constructor(
        idHistorial: Int,
        idRutina: Int,
        dia: String
    ) : this(idHistorial,idRutina,null,dia,null,null,null)
}