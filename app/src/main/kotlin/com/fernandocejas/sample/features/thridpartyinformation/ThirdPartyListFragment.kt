package com.fernandocejas.sample.features.thridpartyinformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_third_party_list.*
import javax.inject.Inject
import android.transition.Explode
import com.fernandocejas.sample.BuildConfig
import com.fernandocejas.sample.core.dataBase.DataBaseHelper
import com.fernandocejas.sample.features.signup.Usuario
import com.fernandocejas.sample.features.signup.VehiculoUsuario
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Chunk
import com.itextpdf.text.BaseColor
import com.itextpdf.text.pdf.draw.LineSeparator
import java.io.File
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.BadElementException
import com.itextpdf.text.Chapter
import com.itextpdf.text.Anchor
import com.itextpdf.text.DocumentException
import com.itextpdf.text.xml.xmp.DublinCoreProperties.addAuthor
import com.itextpdf.text.xml.xmp.DublinCoreProperties.addSubject
import com.itextpdf.text.xml.xmp.DublinCoreProperties.addTitle
import java.util.*


class ThirdPartyListFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    val catFont = Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD);
    val redFont = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL, BaseColor.RED);
    val subFont = Font(Font.FontFamily.TIMES_ROMAN, 16f, Font.BOLD);
    val smallBold = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD);

    var dbHandler: DataBaseHelper? = null

    override fun layoutId() = R.layout.fragment_third_party_list




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Creates a vertical Layout Manager
        listaTerceros.layoutManager = LinearLayoutManager(activity!!)

        dbHandler = DataBaseHelper(context!!)

        // Access the RecyclerView Adapter and load the data into it
        listaTerceros.adapter = ThirdPartyAdapter(AndroidApplication.globalListTerceros, activity!!)
        fabQR.setOnClickListener( View.OnClickListener { navigator.showScanQR(activity!!) })
        fabWrite.setOnClickListener( View.OnClickListener { navigator.showThirdPartyInformation(activity!!) })
        createPDF.setOnClickListener( View.OnClickListener {
//            val document = Document()
//            PdfWriter.getInstance(document,  FileOutputStream(Environment.getExternalStorageDirectory().toString()+File.separator+"firstPdf.pdf"))
//            document.open()
//            addMetaData(document)
//            addTitlePage(document)
//            addContent(document)
//            document.close()
//
//            sendEmail()

            navigator.showThirdPartyPhoto(activity!!,-1)
        })

    }

    private fun sendEmail(){
        var emailIntent =  Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
        var root = Environment.getExternalStorageDirectory();
        var pathToMyAttachedFile = "temp/attachement.xml";
        var file = File(Environment.getExternalStorageDirectory().toString()+File.separator+"firstPdf.pdf")
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
                "Fecha: "  + Date(), smallBold))
        addEmptyLine(preface, 3)

        preface.add(Paragraph(
                "Ubicación del accidente: "  + AndroidApplication.ubicacionAccidente, smallBold))
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

        val usuario = dbHandler!!.getUsuario()
        addUserInformation(usuario, catPart)

        // Add a list
//        createList(subCatPart)
        val paragraph = Paragraph()
        addEmptyLine(paragraph, 5)

        // Now add all this to the document
        document.add(catPart)

        // Next section
        anchor = Anchor("Second Chapter", catFont)
        anchor.name = "Second Chapter"

        // Second parameter is the number of the chapter
        catPart = Chapter(Paragraph(anchor), 1)

        addThirdPartyInformation(catPart)

        // Now add all this to the document
        document.add(catPart)

    }

    @Throws(BadElementException::class)
    private fun createTable(subCatPart: Section) {
        val table = PdfPTable(3)

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        var c1 = PdfPCell(Phrase("Job Name:"))
        c1.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(c1)

        c1 = PdfPCell(Phrase("Test 001"))
        c1.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(c1)

        c1 = PdfPCell(Phrase(""))
        c1.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(c1)
        table.headerRows = 1

        table.addCell("Date:")
        table.addCell("1.1")
        table.addCell("")
        table.addCell("Labor Rate:")
        table.addCell("2.2")
        table.addCell("")
        table.addCell("Labor Cost:")
        table.addCell("3.2")
        table.addCell("3.3")

        subCatPart.add(table)

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





}
