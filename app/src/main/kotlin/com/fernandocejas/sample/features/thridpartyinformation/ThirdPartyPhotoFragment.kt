package com.fernandocejas.sample.features.thridpartyinformation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.BuildConfig
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.dataBase.DataBaseHelper
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import com.fernandocejas.sample.features.signup.Usuario
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.fragment_third_party_photo.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject


class ThirdPartyPhotoFragment : BaseFragment(), View.OnClickListener {

    private lateinit var button: View
    private val CAMERA_REQUEST_CODE = 102
    private var mCurrentPhotoPath: String = ""
    private var capture1Path: String = ""
    private var capture2Path: String = ""
    private var capture3Path: String = ""
    private var capture4Path: String = ""
    private var capture5Path: String = ""
    private var capture6Path: String = ""
    private var capture7Path: String = ""
    private var capture8Path: String = ""
    private var capture9Path: String = ""

    val catFont = Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD);
    val redFont = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL, BaseColor.RED);
    val subFont = Font(Font.FontFamily.TIMES_ROMAN, 16f, Font.BOLD);
    val smallBold = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD);


    override fun onClick(v: View?) {

        if (v!!.id == Capture1.id || v!!.id == Capture2.id || v!!.id == Capture3.id ||
                v!!.id == Capture4.id || v!!.id == Capture5.id || v!!.id == Capture6.id ||
                v!!.id == Capture7.id || v!!.id == Capture8.id || v!!.id == Capture9.id ) {

            button = v

            if (permission == PackageManager.PERMISSION_GRANTED) {

                launchCamera()
            } else {

                requestPermission()

            }

        }
        else if (v!!.id == continueButton.id) {
            if(validatePaths()){
                // actualiza
                if(positionArray == -1 ){
                    updateUsuario()
                    navigator.showThirdPartyPhoto(activity!!, 0)
                }else{

                    updateThird()//actualiza las fotos del tercero
                    //verifica si existen mas terceros si existen mas va a la ventana de las fotos
                    //sino genera el pdf
                    if(positionArray < AndroidApplication.globalListTerceros.size-1){
                        navigator.showThirdPartyPhoto(activity!!, positionArray+1)
                    }else{
                        //genera el pdf
                        val document = Document()
                        PdfWriter.getInstance(document,  FileOutputStream(Environment.getExternalStorageDirectory().toString()+File.separator+"firstPdf.pdf"))
                        document.open()
                        addMetaData(document)
                        addTitlePage(document)
                        addContent(document)
                        document.close()

                        sendEmail()
                        Toast.makeText(activity!!, "GeneraPDF", Toast.LENGTH_LONG).show()
                    }

                }

            }else{
                Toast.makeText(activity!!, "NO CONTINUA", Toast.LENGTH_LONG).show()
            }

        }
    }

    @Inject
    lateinit var navigator: Navigator
    private var permission: Int = 0
    var dbHandler: DataBaseHelper? = null
    var positionArray = 0;

    override fun layoutId() = R.layout.fragment_third_party_photo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        permission = ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.CAMERA)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        positionArray = (activity as ThirdPartyPhotoActivity).positionArray
        dbHandler = DataBaseHelper(context!!)

        Capture1.setOnClickListener(this)
        Capture2.setOnClickListener(this)
        Capture3.setOnClickListener(this)
        Capture4.setOnClickListener(this)
        Capture5.setOnClickListener(this)
        Capture6.setOnClickListener(this)
        Capture7.setOnClickListener(this)
        Capture8.setOnClickListener(this)
        Capture9.setOnClickListener(this)
        if(positionArray >= 0 ){
            var third : Terceros = AndroidApplication.globalListTerceros.get(positionArray)
            titulo.setText(  "${titulo.text} ${third.nombres} ${third.apellidos}"  )
        }else{
            titulo.setText(  "Ahora agregue las fotos de su vehiculo:"  )
        }
        continueButton.setOnClickListener(this)
    }
    private fun updateUsuario(){
        var usuario = dbHandler!!.getUsuario()
        usuario.fotoVechiculo1 = capture1Path
        usuario.fotoVechiculo2 = capture2Path
        usuario.fotoVechiculo3 = capture3Path
        usuario.fotoVechiculo4 = capture4Path
        usuario.fotoVechiculo5 = capture5Path
        usuario.fotoVechiculo6 = capture6Path
        usuario.fotoVechiculo7 = capture8Path
        usuario.fotoVechiculo8 = capture8Path
        usuario.fotoVechiculo9 = capture9Path
        dbHandler!!.updateUsuario(usuario)
    }
    private fun updateThird(){
        var third : Terceros = AndroidApplication.globalListTerceros.get(positionArray)
        third.fotoVechiculo1 = capture1Path
        third.fotoVechiculo2 = capture2Path
        third.fotoVechiculo3 = capture3Path
        third.fotoVechiculo4 = capture4Path
        third.fotoVechiculo5 = capture5Path
        third.fotoVechiculo6 = capture6Path
        third.fotoVechiculo7 = capture8Path
        third.fotoVechiculo8 = capture8Path
        third.fotoVechiculo9 = capture9Path
    }

    private fun setAll (photoPath:String){
//        if (view.id == Capture1.id) {
            capture1Path = photoPath
            textCapture1.visibility = View.GONE
//        }else if (view.id == Capture2.id) {
            capture2Path = photoPath
            textCapture2.visibility = View.GONE
//        }else if (view.id == Capture3.id) {
            capture3Path = photoPath
            textCapture3.visibility = View.GONE
//        }else if (view.id == Capture4.id) {
            capture4Path = photoPath
            textCapture4.visibility = View.GONE
//        }else if (view.id == Capture5.id) {
            capture5Path = photoPath
            textCapture5.visibility = View.GONE
//        }else if (view.id == Capture6.id) {
            capture6Path = photoPath
            textCapture6.visibility = View.GONE
//        }else if (view.id == Capture7.id) {
            capture7Path = photoPath
            textCapture7.visibility = View.GONE
//        }else if (view.id == Capture8.id) {
            capture8Path = photoPath
            textCapture8.visibility = View.GONE
//        }else if (view.id == Capture9.id) {
            capture9Path = photoPath
            textCapture9.visibility = View.GONE
//        }
    }
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = activity!!.contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    private fun requestPermission() {

        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    processCapturedPhoto(button as SimpleDraweeView)
                } else {
                    launchCamera()
                }
            }
        }
    }

    private fun processCapturedPhoto(view: SimpleDraweeView) {
        val cursor = activity!!.contentResolver.query(Uri.parse(mCurrentPhotoPath),
                Array(1) { android.provider.MediaStore.Images.ImageColumns.DATA },
                null, null, null)
        cursor.moveToFirst()
        val photoPath = cursor.getString(0)
        cursor.close()


//        encodeImage(photoPath, view.id)

        val file = File(photoPath)
        val uri = Uri.fromFile(file)

        setPath(view,photoPath)
//        setAll(photoPath)
        val height = resources.getDimensionPixelSize(R.dimen.photo_height)
        val width = resources.getDimensionPixelSize(R.dimen.photo_width)

        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(width, height))
                .build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(view.controller)
                .setImageRequest(request)
                .build()
        view.controller = controller
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            processCapturedPhoto(button as SimpleDraweeView)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setPath(view: SimpleDraweeView, photoPath : String){
        if (view.id == Capture1.id) {
            capture1Path = photoPath
            textCapture1.visibility = View.GONE
        }else if (view.id == Capture2.id) {
            capture2Path = photoPath
            textCapture2.visibility = View.GONE
        }else if (view.id == Capture3.id) {
            capture3Path = photoPath
            textCapture3.visibility = View.GONE
        }else if (view.id == Capture4.id) {
            capture4Path = photoPath
            textCapture4.visibility = View.GONE
        }else if (view.id == Capture5.id) {
            capture5Path = photoPath
            textCapture5.visibility = View.GONE
        }else if (view.id == Capture6.id) {
            capture6Path = photoPath
            textCapture6.visibility = View.GONE
        }else if (view.id == Capture7.id) {
            capture7Path = photoPath
            textCapture7.visibility = View.GONE
        }else if (view.id == Capture8.id) {
            capture8Path = photoPath
            textCapture8.visibility = View.GONE
        }else if (view.id == Capture9.id) {
            capture9Path = photoPath
            textCapture9.visibility = View.GONE
        }
    }

    private fun validatePaths() : Boolean{
        var bien = true
        if(capture1Path.isEmpty()){
            textCapture1.setTextColor(Color.RED)
        }
        if(capture2Path.isEmpty()){
            textCapture2.setTextColor(Color.RED)
        }
        if(capture3Path.isEmpty()){
            textCapture3.setTextColor(Color.RED)
        }
        if(capture4Path.isEmpty()){
            textCapture4.setTextColor(Color.RED)
        }
        if(capture5Path.isEmpty()){
            textCapture5.setTextColor(Color.RED)
        }
        if(capture6Path.isEmpty()){
            textCapture6.setTextColor(Color.RED)
        }
        if(capture7Path.isEmpty()){
            textCapture7.setTextColor(Color.RED)
        }
        if(capture8Path.isEmpty()){
            textCapture8.setTextColor(Color.RED)
        }
        if(capture9Path.isEmpty()){
            textCapture9.setTextColor(Color.RED)
        }
        if(capture1Path.isEmpty() ||capture2Path.isEmpty() ||capture3Path.isEmpty() ||
                capture4Path.isEmpty() ||capture5Path.isEmpty() ||capture6Path.isEmpty() ||
                capture7Path.isEmpty() ||capture8Path.isEmpty() ||capture9Path.isEmpty()){
            bien = false
        }
        return bien
    }

    private fun validateEmptyPath(path : String,textView: TextView): Boolean{
        if(path.isEmpty()){
            textView.setTextColor(Color.RED)
            return false
        }
        return true
    }

    private fun sendEmail() {
        var emailIntent = Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
        var root = Environment.getExternalStorageDirectory();
        var pathToMyAttachedFile = "temp/attachement.xml";
        var file = File(Environment.getExternalStorageDirectory().toString() + File.separator + "firstPdf.pdf")
        if (!file.exists() || !file.canRead()) {
            return;
        }
//        var uri = Uri.fromFile(file);
        var uri = FileProvider.getUriForFile(activity!!,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }


    private fun addMetaData(document: Document) {
        document.addTitle("CrashGuide")
        document.addAuthor("CrashGuide")
        document.addCreator("CrashGuide")
    }

    @Throws(DocumentException::class)
    private fun addTitlePage(document: Document) {
        val preface = Paragraph()
        // We add one empty line
        addEmptyLine(preface, 1)
        // Lets write a big header
        preface.add(Paragraph("Title of the document", catFont))

        addEmptyLine(preface, 1)
        // Will create: Report generated by: _name, _date
        preface.add(Paragraph(
                "Fecha: " + Date(), smallBold))
        addEmptyLine(preface, 3)

        preface.add(Paragraph(
                "Ubicación del accidente: " + AndroidApplication.ubicacionAccidente, smallBold))
        addEmptyLine(preface, 3)


//        preface.add(Paragraph(
//                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.de ;-).",
//                redFont))

        document.add(preface)
        // Start a new page
        document.newPage()
    }

    @Throws(DocumentException::class)
    private fun addContent(document: Document) {
        var anchor = Anchor("CrashGuide", catFont)
        anchor.name = "CrashGuide"


        // Second parameter is the number of the chapter
        var catPart = Chapter(Paragraph(anchor), 1)
        createTableUsuario(catPart)
//        val usuario = dbHandler!!.getUsuario()
//        addUserInformation(usuario, catPart)

        // Add a list
//        createList(subCatPart)
        val paragraph = Paragraph()
        addEmptyLine(paragraph, 5)

        // Now add all this to the document
        document.add(catPart)

        // Next section
        anchor = Anchor("Datos de los involucrados", catFont)
        anchor.name = "Datos de los involucrados"

        // Second parameter is the number of the chapter
        catPart = Chapter(Paragraph(anchor), 2)

        createTableTercero(catPart)
//        addThirdPartyInformation(catPart)

        // Now add all this to the document
        document.add(catPart)

        anchor = Anchor("Fotos de los vehiculos", catFont)
        anchor.name = "Fotos de los vehiculos"

        // Second parameter is the number of the chapter
        catPart = Chapter(Paragraph(anchor), 3)

        addUserVehiclePhoto(catPart)
        addThirdVehiclePhoto(catPart)
        // Now add all this to the document
        document.add(catPart)
    }

    @Throws(BadElementException::class)
    private fun createTableUsuario(subCatPart: Section) {
        val usuario = dbHandler!!.getUsuario()

        var paragraph = Paragraph()
        addEmptyLine(paragraph, 2)
        subCatPart.add(paragraph)

        var table = PdfPTable(12)
        table.totalWidth = Utilities.millimetersToPoints(120f)
        table.isLockedWidth = true
        table.defaultCell.border = PdfPCell.NO_BORDER
        table.addCell(getCell(12,"Datos del conductor:"))
        table.addCell(getCell(6, "Nombres:\n ${usuario.nombres}"))
        table.addCell(getCell(6, "Apellidos:\n ${usuario.apellidos}"))
        table.addCell(getCell(4, "RUT:\n ${usuario.rut}"))
        table.addCell(getCell(4, "Telefono:\n ${usuario.telefono}"))
        table.addCell(getCell(4, "Correo:\n ${usuario.correo}"))
        table.completeRow()


        table.addCell(getCell(12,"Datos del vehiculo:"))
        table.addCell(getCell(6, "Marca: \n ${usuario.vehiculo!!.marca}"))
        table.addCell(getCell(6, "Modelo: \n ${usuario.vehiculo!!.modelo}"))
        table.addCell(getCell(4, "Patente: \n ${usuario.vehiculo!!.patente}"))
        table.addCell(getCell(4, "Año: \n ${usuario.vehiculo!!.ano}"))
        table.addCell(getCell(4, "Color: \n ${usuario.vehiculo!!.color}"))
        table.completeRow()
        subCatPart.add(table)

        paragraph = Paragraph()
        addEmptyLine(paragraph, 2)
        subCatPart.add(paragraph)

        subCatPart.add(Paragraph("Foto de Carnet:"))
        var image = Image.getInstance (usuario.fotoCarnet)
        image.setAlignment(Image.MIDDLE )
        image.scaleToFit(300f, 600f)
        subCatPart.add(image)

        subCatPart.add(Paragraph("Foto licencia de conducir:"))
        image = Image.getInstance (usuario.fotoLicencia)
        image.setAlignment(Image.MIDDLE )
        image.scaleToFit(300f, 600f)
        subCatPart.add(image)

    }

    @Throws(BadElementException::class)
    private fun createTableTercero(subCatPart: Section) {

        var contador = 1
        for(tercero in AndroidApplication.globalListTerceros){

            var paragraph = Paragraph()
            addEmptyLine(paragraph, 2)
            subCatPart.add(paragraph)

            var table = PdfPTable(12)
            table.totalWidth = Utilities.millimetersToPoints(120f)
            table.isLockedWidth = true
            table.defaultCell.border = PdfPCell.NO_BORDER
            table.addCell(getCell(12,"Datos del involucrado $contador:"))
            table.addCell(getCell(6, "Nombres:\n ${tercero.nombres}"))
            table.addCell(getCell(6, "Apellidos:\n ${tercero.apellidos}"))
            table.addCell(getCell(4, "RUT:\n ${tercero.rut}"))
            table.addCell(getCell(4, "Telefono:\n ${tercero.telefono}"))
            table.addCell(getCell(4, "Correo:\n ${tercero.correo}"))
            table.completeRow()


            table.addCell(getCell(12,"Datos del vehiculo:"))
            table.addCell(getCell(6, "Marca: \n ${tercero.vehiculo!!.marca}"))
            table.addCell(getCell(6, "Modelo: \n ${tercero.vehiculo!!.modelo}"))
            table.addCell(getCell(4, "Patente: \n ${tercero.vehiculo!!.patente}"))
            table.addCell(getCell(4, "Año: \n ${tercero.vehiculo!!.ano}"))
            table.addCell(getCell(4, "Color: \n ${tercero.vehiculo!!.color}"))
            table.completeRow()
            subCatPart.add(table)

            paragraph = Paragraph()
            addEmptyLine(paragraph, 2)
            subCatPart.add(paragraph)

            subCatPart.add(Paragraph("Foto de Carnet:"))
            var image = Image.getInstance (tercero.fotoCarnet)
            image.setAlignment(Image.MIDDLE )
            image.scaleToFit(300f, 600f)
            subCatPart.add(image)

            subCatPart.add(Paragraph("Foto licencia de conducir:"))
            image = Image.getInstance (tercero.fotoLicencia)
            image.setAlignment(Image.MIDDLE )
            image.scaleToFit(300f, 600f)
            subCatPart.add(image)

            subCatPart.newPage()
            contador++
        }




    }

    private fun getCell(cm: Int, text: String): PdfPCell {
        val cell = PdfPCell()
        cell.colspan = cm
        cell.isUseAscender = true
        cell.isUseDescender = true
        val p = Paragraph(
                String.format("%s", text),
                Font(Font.FontFamily.HELVETICA, 8f))
        p.alignment = Element.ALIGN_LEFT
        cell.addElement(p)
        return cell
    }


    private fun addUserInformation(usuario: Usuario, catPart: Chapter){

        var subPara = Paragraph("Datos del conductor", subFont)
        var subCatPart = catPart.addSection(subPara)
        subCatPart.add(Paragraph("Nombres: ${usuario.nombres}"))
        subCatPart.add(Paragraph("Apellidos: ${usuario.apellidos}"))
        subCatPart.add(Paragraph("RUT: ${usuario.rut}"))
        subCatPart.add(Paragraph("Telefono: ${usuario.telefono}"))
        subCatPart.add(Paragraph("Correo electronico: ${usuario.correo}"))
        subCatPart.add(Paragraph("Foto de Carnet:"))
        var image = Image.getInstance (usuario.fotoCarnet)
        image.setAlignment(Image.LEFT )
        image.scaleToFit(300f, 600f)
        subCatPart.add(image)

        subCatPart.add(Paragraph("Foto licencia de conducir:"))
        image = Image.getInstance (usuario.fotoLicencia)
        image.setAlignment(Image.MIDDLE )
        image.scaleToFit(300f, 600f)
        subCatPart.add(image)

        subPara = Paragraph("Datos de vehiculo", subFont)

        subCatPart = catPart.addSection(subPara)
        subCatPart.add(Paragraph("Marca: ${usuario.vehiculo!!.marca}"))
        subCatPart.add(Paragraph("Modelo: ${usuario.vehiculo!!.modelo}"))
        subCatPart.add(Paragraph("Patente ${usuario.vehiculo!!.patente}"))
        subCatPart.add(Paragraph("Año: ${usuario.vehiculo!!.ano}"))
        subCatPart.add(Paragraph("Color: ${usuario.vehiculo!!.color}"))
    }

    private fun addUserVehiclePhoto(catPart: Chapter){
        var subPara = Paragraph("Fotos del vehiculo del usuario", subFont)
        var subCatPart = catPart.addSection(subPara)

        var usuario = dbHandler!!.getUsuario()
        insertImage("Foto frontal",usuario.fotoVechiculo1,subCatPart)
        insertImage("Foto trasera",usuario.fotoVechiculo2,subCatPart)
        insertImage("Foto lado derecho",usuario.fotoVechiculo3,subCatPart)
        subCatPart.newPage()
        insertImage("Foto Lado izquierdo",usuario.fotoVechiculo4,subCatPart)
        insertImage("Foto frontal derecho",usuario.fotoVechiculo5,subCatPart)
        insertImage("Foto frontal izquierdo",usuario.fotoVechiculo6,subCatPart)
        subCatPart.newPage()
        insertImage("Foto trasero izquierdo",usuario.fotoVechiculo7,subCatPart)
        insertImage("Foto trasero derecho",usuario.fotoVechiculo8,subCatPart)
        insertImage("Foto patente frontal",usuario.fotoVechiculo9,subCatPart)
        subCatPart.newPage()

    }

    private fun addThirdVehiclePhoto(catPart: Chapter){
        val contador = 1
        for(tercero in AndroidApplication.globalListTerceros) {
            var subPara = Paragraph("Fotos del vehiculo del involucrado $contador", subFont)
            var subCatPart = catPart.addSection(subPara)

            insertImage("Foto frontal",tercero.fotoVechiculo1,subCatPart)
            insertImage("Foto trasera",tercero.fotoVechiculo2,subCatPart)
            insertImage("Foto lado derecho",tercero.fotoVechiculo3,subCatPart)
            subCatPart.newPage()
            insertImage("Foto Lado izquierdo",tercero.fotoVechiculo4,subCatPart)
            insertImage("Foto frontal derecho",tercero.fotoVechiculo5,subCatPart)
            insertImage("Foto frontal izquierdo",tercero.fotoVechiculo6,subCatPart)
            subCatPart.newPage()
            insertImage("Foto trasero izquierdo",tercero.fotoVechiculo7,subCatPart)
            insertImage("Foto trasero derecho",tercero.fotoVechiculo8,subCatPart)
            insertImage("Foto patente frontal",tercero.fotoVechiculo9,subCatPart)
            subCatPart.newPage()

        }
    }

    private fun addThirdPartyInformation(catPart: Chapter){
        var contador = 1
        for(tercero in AndroidApplication.globalListTerceros){
            var subPara = Paragraph("Subcategory 1", subFont)
            var subCatPart = catPart.addSection(subPara)
            subCatPart.add(Paragraph("Nombres: ${tercero.nombres}"))
            subCatPart.add(Paragraph("Apellidos: ${tercero.apellidos}"))
            subCatPart.add(Paragraph("RUT: ${tercero.rut}"))
            subCatPart.add(Paragraph("Telefono: ${tercero.telefono}"))
            subCatPart.add(Paragraph("Correo electronico: ${tercero.correo}"))

            subCatPart.add(Paragraph("Foto de Carnet:"))
            var image = Image.getInstance (tercero.fotoCarnet)
            image.setAlignment(Image.MIDDLE )
            image.scaleToFit(300f, 600f)
            subCatPart.add(image)

            subCatPart.add(Paragraph("Foto licencia de conducir:"))
            image = Image.getInstance (tercero.fotoLicencia)
            image.setAlignment(Image.MIDDLE )
            image.scaleToFit(300f, 600f)
            subCatPart.add(image)

            subPara = Paragraph("Subcategory 2", subFont)

            subCatPart = catPart.addSection(subPara)
            subCatPart.add(Paragraph("Marca: ${tercero.vehiculo!!.marca}"))
            subCatPart.add(Paragraph("Modelo: ${tercero.vehiculo!!.modelo}"))
            subCatPart.add(Paragraph("Patente ${tercero.vehiculo!!.patente}"))
            subCatPart.add(Paragraph("Año: ${tercero.vehiculo!!.ano}"))
            subCatPart.add(Paragraph("Color: ${tercero.vehiculo!!.color}"))
        }
    }


    private fun addEmptyLine(paragraph: Paragraph, number: Int) {
        for (i in 0 until number) {
            paragraph.add(Paragraph(" "))
        }
    }

    private fun insertImage(titulo: String, imagePath: String, subCatPart: Section){
        subCatPart.add(Paragraph(titulo))
        var image = Image.getInstance (imagePath)
        image.setAlignment(Image.MIDDLE )
        image.scaleToFit(300f, 600f)
        subCatPart.add(image)
    }

}
