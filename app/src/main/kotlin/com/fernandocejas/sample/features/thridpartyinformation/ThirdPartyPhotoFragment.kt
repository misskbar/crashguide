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
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.dataBase.DataBaseHelper
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_third_party_photo.*
import java.io.File
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
                    if(positionArray < AndroidApplication.globalListTerceros.size){
                        navigator.showThirdPartyPhoto(activity!!, positionArray+1)
                    }else{
                        //genera el pdf
                    }

                }
                Toast.makeText(activity!!, "CONTINUA", Toast.LENGTH_LONG).show()
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

//        setPath(view,photoPath)
        setAll(photoPath)
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

}
