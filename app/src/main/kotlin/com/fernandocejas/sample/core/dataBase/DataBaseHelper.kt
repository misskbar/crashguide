package com.fernandocejas.sample.core.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import com.fernandocejas.sample.features.contacs.Contacts
import com.fernandocejas.sample.features.signup.Usuario
import com.fernandocejas.sample.features.signup.VehiculoUsuario

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseHelper.DB_NAME, null, DataBaseHelper.DB_VERSION) {

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "DBCrashGuide"


        private val TABLE_NAME_USUARIO = "Usuario"
        private val IDUSUARIO = "U_Id"
        private val NOMBRES = "Nombres"
        private val APELLIDOS = "Apellidos"
        private val RUT = "RUT"
        private val TELEFONO = "Telefono"
        private val CORREO = "Correo"
        private val SEGURO = "Seguro"
        private val FOTOCARNET = "FotoCarnet"
        private val FOTOLICENCIA = "FotoLicencia"
        private val FOTOQR = "FotoQR"
        private val FOTOVEHICULO1 = "FotoVehiculo1"
        private val FOTOVEHICULO2 = "FotoVehiculo2"
        private val FOTOVEHICULO3 = "FotoVehiculo3"
        private val FOTOVEHICULO4 = "FotoVehiculo4"
        private val FOTOVEHICULO5 = "FotoVehiculo5"
        private val FOTOVEHICULO6 = "FotoVehiculo6"
        private val FOTOVEHICULO7 = "FotoVehiculo7"
        private val FOTOVEHICULO8 = "FotoVehiculo8"
        private val FOTOVEHICULO9 = "FotoVehiculo9"


        private val TABLE_NAME_VEHICULO = "Vehiculo"
        private val IDVEHICULO = "V_Id"
        private val MARCA = "Marca"
        private val MODELO = "Modelo"
        private val PATENTE = "Patente"
        private val ANO = "Ano"
        private val COLOR = "Color"
        private val FOREINGUSUARIO = "Usuario_id"

        private val TABLE_NAME_CONTACTO = "Contacto"
        private val IDCONTACTO = "C_Id"
        private val NOMBRECONTACTO = "Nombre"
        private val NUMEROCONTACTO = "Numero"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_USUARIO = "CREATE TABLE $TABLE_NAME_USUARIO (" +
                IDUSUARIO + " INTEGER PRIMARY KEY," +
                NOMBRES + " TEXT," +
                APELLIDOS + " TEXT," +
                RUT + " TEXT," +
                TELEFONO + " INTEGER," +
                CORREO + " TEXT," +
                SEGURO + " TEXT," +
                FOTOCARNET + " TEXT," +
                FOTOLICENCIA + " TEXT,  " +
                FOTOQR + " TEXT," +
                FOTOVEHICULO1 + " TEXT," +
                FOTOVEHICULO2 + " TEXT," +
                FOTOVEHICULO3 + " TEXT," +
                FOTOVEHICULO4 + " TEXT," +
                FOTOVEHICULO5 + " TEXT," +
                FOTOVEHICULO6 + " TEXT," +
                FOTOVEHICULO7 + " TEXT," +
                FOTOVEHICULO8 + " TEXT," +
                FOTOVEHICULO9 + " TEXT" +
                ");"
        db.execSQL(CREATE_TABLE_USUARIO)

        val CREATE_TABLE_VEHICULO = "CREATE TABLE $TABLE_NAME_VEHICULO (" +
                IDVEHICULO + " INTEGER PRIMARY KEY," +
                MARCA + " TEXT," +
                MODELO + " TEXT," +
                PATENTE + " TEXT," +
                ANO + " INTEGER," +
                COLOR + " TEXT," +
                FOREINGUSUARIO + " INTEGER," +
                "FOREIGN KEY($FOREINGUSUARIO) REFERENCES $TABLE_NAME_USUARIO($IDUSUARIO));"
        db.execSQL(CREATE_TABLE_VEHICULO)

        val CREATE_TABLE_CONTACTO = "CREATE TABLE $TABLE_NAME_CONTACTO (" +
                IDCONTACTO + " INTEGER PRIMARY KEY," +
                NOMBRECONTACTO + " TEXT," +
                NUMEROCONTACTO + " TEXT);"
        db.execSQL(CREATE_TABLE_CONTACTO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_VEHICULO = "DROP TABLE IF EXISTS $TABLE_NAME_VEHICULO"
        db.execSQL(DROP_TABLE_VEHICULO)

        val DROP_TABLE_USUARIO = "DROP TABLE IF EXISTS $TABLE_NAME_USUARIO"
        db.execSQL(DROP_TABLE_USUARIO)

        val DROP_TABLE_CONTACTO = "DROP TABLE IF EXISTS $TABLE_NAME_CONTACTO"
        db.execSQL(DROP_TABLE_CONTACTO)
        onCreate(db)
    }


    fun addVehiculo(vehiculo: VehiculoUsuario, idUsuario : Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(MARCA, vehiculo.marca)
        values.put(MODELO, vehiculo.modelo)
        values.put(PATENTE, vehiculo.patente)
        values.put(ANO, vehiculo.ano)
        values.put(COLOR, vehiculo.color)
        values.put(FOREINGUSUARIO, idUsuario)
        val _success = db.insert(TABLE_NAME_VEHICULO, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun addContacto(contacts: Contacts): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRECONTACTO, contacts.nombres)
        values.put(NUMEROCONTACTO, contacts.telefono)
        val _success = db.insert(TABLE_NAME_CONTACTO, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun addUsuario(usuario: Usuario): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRES, usuario.nombres)
        values.put(APELLIDOS, usuario.apellidos)
        values.put(RUT, usuario.rut)
        values.put(TELEFONO, usuario.telefono)
        values.put(CORREO, usuario.correo)
        values.put(SEGURO, usuario.seguro)
        values.put(FOTOCARNET, usuario.fotoCarnet)
        values.put(FOTOLICENCIA, usuario.fotoLicencia)
        values.put(FOTOQR, usuario.fotoQR)
        values.put(FOTOVEHICULO1, usuario.fotoVechiculo1)
        values.put(FOTOVEHICULO2, usuario.fotoVechiculo2)
        values.put(FOTOVEHICULO3, usuario.fotoVechiculo3)
        values.put(FOTOVEHICULO4, usuario.fotoVechiculo4)
        values.put(FOTOVEHICULO5, usuario.fotoVechiculo5)
        values.put(FOTOVEHICULO6, usuario.fotoVechiculo6)
        values.put(FOTOVEHICULO7, usuario.fotoVechiculo7)
        values.put(FOTOVEHICULO8, usuario.fotoVechiculo8)
        values.put(FOTOVEHICULO9, usuario.fotoVechiculo9)
        val _success = db.insert(TABLE_NAME_USUARIO, null, values)
        db.close()
        return _success
    }

    fun updateVehiculo(vehiculo: VehiculoUsuario): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(MARCA, vehiculo.marca)
        values.put(MODELO, vehiculo.modelo)
        values.put(PATENTE, vehiculo.patente)
        values.put(ANO, vehiculo.ano)
        values.put(COLOR, vehiculo.color)
        val _success = db.update(TABLE_NAME_VEHICULO, values, IDVEHICULO + "=?", arrayOf(vehiculo.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun updateUsuario(usuario: Usuario): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRES, usuario.nombres)
        values.put(APELLIDOS, usuario.apellidos)
        values.put(RUT, usuario.rut)
        values.put(TELEFONO, usuario.telefono)
        values.put(CORREO, usuario.correo)
        values.put(SEGURO, usuario.seguro)
        values.put(FOTOCARNET, usuario.fotoCarnet)
        values.put(FOTOLICENCIA, usuario.fotoLicencia)
        values.put(FOTOQR, usuario.fotoQR)
        values.put(FOTOVEHICULO1, usuario.fotoVechiculo1)
        values.put(FOTOVEHICULO2, usuario.fotoVechiculo2)
        values.put(FOTOVEHICULO3, usuario.fotoVechiculo3)
        values.put(FOTOVEHICULO4, usuario.fotoVechiculo4)
        values.put(FOTOVEHICULO5, usuario.fotoVechiculo5)
        values.put(FOTOVEHICULO6, usuario.fotoVechiculo6)
        values.put(FOTOVEHICULO7, usuario.fotoVechiculo7)
        values.put(FOTOVEHICULO8, usuario.fotoVechiculo8)
        values.put(FOTOVEHICULO9, usuario.fotoVechiculo9)
        val _success = db.update(TABLE_NAME_USUARIO, values, IDUSUARIO + "=?", arrayOf(usuario.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteVehiculo(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME_VEHICULO, FOREINGUSUARIO + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }


    fun deleteAllVehiculo(): Boolean {
        val db = this.writableDatabase
//        val _success = db.delete(TABLE_NAME_VEHICULO, FOREINGUSUARIO + "=?", arrayOf(_id.toString())).toLong()
        val _success = db.delete(TABLE_NAME_VEHICULO, "", null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteUsuario(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME_USUARIO, IDUSUARIO + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllUsuario(): Boolean {
        val db = this.writableDatabase
//        val _success = db.delete(TABLE_NAME_USUARIO, IDUSUARIO + "=?", arrayOf(_id.toString())).toLong()
        val _success = db.delete(TABLE_NAME_USUARIO, "", null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteContacto(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME_CONTACTO, IDCONTACTO + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun getUsuario(): Usuario {
        val usuario = Usuario()
        val vehiculoUsuario = VehiculoUsuario()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_USUARIO, $TABLE_NAME_VEHICULO "
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()

            while (cursor.isAfterLast == false) {
                //Datos de vehiculo
                vehiculoUsuario.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(IDVEHICULO)))
                vehiculoUsuario.marca = cursor.getString(cursor.getColumnIndex(MARCA))

                vehiculoUsuario.modelo = cursor.getString(cursor.getColumnIndex(MODELO))
                vehiculoUsuario.patente = cursor.getString(cursor.getColumnIndex(PATENTE))
                vehiculoUsuario.ano = cursor.getInt(cursor.getColumnIndex(ANO))
                vehiculoUsuario.color = cursor.getString(cursor.getColumnIndex(COLOR))
                //Datos de usuario
                usuario.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(IDUSUARIO)))
                usuario.nombres = cursor.getString(cursor.getColumnIndex(NOMBRES))
                usuario.apellidos = cursor.getString(cursor.getColumnIndex(APELLIDOS))
                usuario.rut = cursor.getString(cursor.getColumnIndex(RUT))
                usuario.telefono = cursor.getInt(cursor.getColumnIndex(TELEFONO))
                usuario.correo = cursor.getString(cursor.getColumnIndex(CORREO))
                usuario.seguro = cursor.getString(cursor.getColumnIndex(SEGURO))
                usuario.fotoCarnet = cursor.getString(cursor.getColumnIndex(FOTOCARNET))
                usuario.fotoLicencia = cursor.getString(cursor.getColumnIndex(FOTOLICENCIA))
                usuario.fotoQR = cursor.getString(cursor.getColumnIndex(FOTOQR))
                usuario.fotoVechiculo1 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO1))
                usuario.fotoVechiculo2 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO2))
                usuario.fotoVechiculo3 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO3))
                usuario.fotoVechiculo4 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO4))
                usuario.fotoVechiculo5 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO5))
                usuario.fotoVechiculo6 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO6))
                usuario.fotoVechiculo7 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO7))
                usuario.fotoVechiculo8 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO8))
                usuario.fotoVechiculo9 = cursor.getString(cursor.getColumnIndex(FOTOVEHICULO9))
                usuario.vehiculo = vehiculoUsuario
                cursor.moveToNext()
                break
            }
        }
        cursor.close()
        return usuario
    }

    fun getContactos(): ArrayList<Contacts> {
        var lista = ArrayList<Contacts>()

        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_CONTACTO "
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                var contacts = Contacts()
                contacts.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(IDCONTACTO)))
                contacts.nombres = cursor.getString(cursor.getColumnIndex(NOMBRECONTACTO))
                contacts.telefono = cursor.getString(cursor.getColumnIndex(NUMEROCONTACTO))
                println("contacto ${contacts.nombres}")
                lista.add(contacts)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return lista
    }

    fun existsUsuario(): Boolean {
        var existe = false
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_USUARIO"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.isAfterLast == false) {
                existe = true
                cursor.moveToNext()
                break
            }
        }
        cursor.close()
        return existe
    }

    fun existsContacts(): Boolean {
        var existe = false
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_CONTACTO"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.isAfterLast == false) {
                existe = true
                cursor.moveToNext()
                break
            }
        }
        cursor.close()
        return existe
    }
}