package com.example.tfc.clasesAuxiliares.clasesBase

//Dataclass con un constructor sin id,lo autoasigna la base de datos
data class Dieta (
    val id:Int=0,
    val nombre:String,
    val nivel:Int,
    val imagen:String
){
    constructor(
        nombre:String,
        nivel:Int,
        imagen:String
    ) : this(0,nombre,nivel,imagen)
}
