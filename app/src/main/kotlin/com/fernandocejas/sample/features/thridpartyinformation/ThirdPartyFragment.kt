/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.sample.features.thridpartyinformation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_third_party_information.*
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

class ThirdPartyFragment : BaseFragment(), View.OnClickListener {


    private var idPath: String = ""
    private var driverLicencePath: String = ""

    override fun onClick(v: View?) {

        if (v!!.id == driverLicenceCapture.id) {

            button = v

            if (permission == PackageManager.PERMISSION_GRANTED) {

                launchCamera()
            } else {

                requestPermission()

            }

        } else if (v.id == idCapture.id) {

            button = v

            if (permission == PackageManager.PERMISSION_GRANTED) {


                launchCamera()

            } else {

                requestPermission()

            }
        }else if (v.id == nextButton.id) {
            Toast.makeText(activity!!,"probando boron",Toast.LENGTH_LONG)
             saveThirdPartyInformation()
            println("el tamano es ${AndroidApplication.globalListTerceros.size}")
             navigator.showThirdPartyList(activity!!)
        }
    }


    private val CAMERA_REQUEST_CODE = 102
    private var mCurrentPhotoPath: String = ""
    private lateinit var button: View

    @Inject
    lateinit var navigator: Navigator

    override fun layoutId() = R.layout.fragment_third_party_information


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        permission = ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.CAMERA)
    }

    private var permission: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val res: Resources = resources
        val hasProtection = res.getStringArray(R.array.has_protection)
        spinner.adapter = ArrayAdapter(activity, R.layout.spinner_item, hasProtection)
        getActivity()!!.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        driverLicenceCapture.setOnClickListener(this)
        idCapture.setOnClickListener(this)
        nextButton.setOnClickListener(this)
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

    private fun saveThirdPartyInformation(){
        var vehiculo = Vehiculo(brand.text.toString(),model.text.toString(),
                registrationNumber.text.toString(),year.text.toString().toInt(),
                color.text.toString())

        var terceros = Terceros(firstName.text.toString(),surname.text.toString(),
                rut.text.toString(),telefono.text.toString().toInt(),email.text.toString(),
                spinner.selectedItem.toString(),idPath,driverLicencePath,vehiculo)

        AndroidApplication.globalListTerceros.add(terceros)

    }

    private fun encodeImage( photoPath: String, id: Int) {
        var  bm: Bitmap = BitmapFactory.decodeFile(photoPath.toString())
        val height = 200
        val width = 300
        bm = Bitmap.createScaledBitmap(bm, width, height, true)
        var baos: ByteArrayOutputStream = ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        var b = baos.toByteArray()
        val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
        if (id == idCapture.id) {
            idPath = encodedImage
        }else {
            driverLicencePath = encodedImage
        }

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

    private fun processCapturedPhoto(view: SimpleDraweeView) {
        val cursor = activity!!.contentResolver.query(Uri.parse(mCurrentPhotoPath),
                Array(1) { android.provider.MediaStore.Images.ImageColumns.DATA },
                null, null, null)
        cursor.moveToFirst()
        val photoPath = cursor.getString(0)
        cursor.close()


        encodeImage(photoPath, view.id)


        val file = File(photoPath)
        val uri = Uri.fromFile(file)

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
}
