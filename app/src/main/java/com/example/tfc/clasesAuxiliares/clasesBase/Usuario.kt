package com.example.tfc.clasesAuxiliares.clasesBase

//Dataclass con un constructor sin id,lo autoasigna la base de datos
class Usuario(
    val id: Int,
    var nombreUsuario: String,
    var objetivoDiario: Int,
    var peso: Int,
    var altura: Int,
    var imc: Double,
    var genero: Boolean
) {
    constructor(
        nombreUsuario: String, edad: Int, peso: Int, altura: Int, imc: Double, genero: Boolean
    ) : this(0, nombreUsuario, edad, peso, altura, imc, genero)
}