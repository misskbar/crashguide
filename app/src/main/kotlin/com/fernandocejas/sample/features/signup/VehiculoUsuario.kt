package com.fernandocejas.sample.features.signup

class VehiculoUsuario (var id: Int, var marca: String, var modelo: String, var patente: String,
                var ano: Int, var color: String, var foraneaUsuario: Int){

    constructor() : this(0,"","","",0,"",0)

}