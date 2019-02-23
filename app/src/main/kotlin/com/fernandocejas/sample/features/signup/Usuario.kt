package com.fernandocejas.sample.features.signup

import com.fernandocejas.sample.features.thridpartyinformation.Vehiculo

class Usuario (var id : Int, var nombres: String, var apellidos: String, var rut: String,
               var telefono: Int, var correo: String, var seguro: String, var fotoCarnet: String,
               var fotoLicencia: String, var vehiculo: VehiculoUsuario?){


    constructor() : this(0,"","","",0,"","","","",null)
}