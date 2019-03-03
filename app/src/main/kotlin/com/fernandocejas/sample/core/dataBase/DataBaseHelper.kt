package com.fernandocejas.sample.core.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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


        private val TABLE_NAME_VEHICULO = "Vehiculo"
        private val IDVEHICULO = "V_Id"
        private val MARCA = "Marca"
        private val MODELO = "Modelo"
        private val PATENTE = "Patente"
        private val ANO = "Ano"
        private val COLOR = "Color"
        private val FOREINGUSUARIO = "Usuario_id"


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
                FOTOQR + " TEXT);"
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_VEHICULO = "DROP TABLE IF EXISTS $TABLE_NAME_VEHICULO"
        db.execSQL(DROP_TABLE_VEHICULO)

        val DROP_TABLE_USUARIO = "DROP TABLE IF EXISTS $TABLE_NAME_USUARIO"
        db.execSQL(DROP_TABLE_USUARIO)
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
                println("____________________________datos usuario___________________________________")
                println("el $IDUSUARIO  es: |${cursor.getString(cursor.getColumnIndex(IDUSUARIO))}|")
                println("el $NOMBRES  es: |${cursor.getString(cursor.getColumnIndex(NOMBRES))}|")
                println("el $APELLIDOS  es: |${cursor.getString(cursor.getColumnIndex(APELLIDOS))}|")
                println("el $RUT  es: |${cursor.getString(cursor.getColumnIndex(RUT))}|")
                println("el $TELEFONO  es: |${cursor.getString(cursor.getColumnIndex(TELEFONO))}|")
                println("el $CORREO  es: |${cursor.getString(cursor.getColumnIndex(CORREO))}|")
                println("el $SEGURO  es: |${cursor.getString(cursor.getColumnIndex(SEGURO))}|")
                println("_____________________________datos vehiculo________________________________")
                println("La $IDVEHICULO  es: |${cursor.getString(cursor.getColumnIndex(IDVEHICULO))}|")
                println("La $MARCA  es: |${cursor.getString(cursor.getColumnIndex(MARCA))}|")
                println("La $PATENTE  es: |${cursor.getString(cursor.getColumnIndex(PATENTE))}|")
                println("La $MARCA  es: |${cursor.getString(cursor.getColumnIndex(MARCA))}|")
                println("La $COLOR  es: |${cursor.getString(cursor.getColumnIndex(COLOR))}|")
                println("La $FOREINGUSUARIO  es: |${cursor.getString(cursor.getColumnIndex(FOREINGUSUARIO))}|")
                println("___________________________________________________________________________")

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
                usuario.vehiculo = vehiculoUsuario
                cursor.moveToNext()
                break
            }
        }
        cursor.close()
        return usuario
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
}