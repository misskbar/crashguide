package com.fernandocejas.sample.features.signup

import com.fernandocejas.sample.features.thridpartyinformation.Vehiculo

class Usuario (var id : Int, var nombres: String, var apellidos: String, var rut: String,
               var telefono: Int, var correo: String, var seguro: String, var fotoCarnet: String,
               var fotoLicencia: String, var fotoQR: String, var fotoVechiculo1: String
               , var fotoVechiculo2: String, var fotoVechiculo3: String, var fotoVechiculo4: String
               , var fotoVechiculo5: String, var fotoVechiculo6: String, var fotoVechiculo7: String
               , var fotoVechiculo8: String, var fotoVechiculo9: String, var vehiculo: VehiculoUsuario?){


    constructor() : this(0,"","","",0,"","",
            "","","","","","",
            "","","","","",
            "",null)
}