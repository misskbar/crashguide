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
        fabMyQR.setOnClickListener( View.OnClickListener { navigator.generateQR(activity!!,Navigator.activityThirdPartyList) })
        fabQR.setOnClickListener( View.OnClickListener { navigator.showScanQR(activity!!) })
        fabWrite.setOnClickListener( View.OnClickListener { navigator.showThirdPartyInformation(activity!!) })
        createPDF.setOnClickListener( View.OnClickListener {

            navigator.showThirdPartyPhoto(activity!!,-1)
        })

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


}
