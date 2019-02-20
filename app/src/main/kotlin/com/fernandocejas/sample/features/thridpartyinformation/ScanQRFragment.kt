package com.fernandocejas.sample.features.thridpartyinformation

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.transition.Explode
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_scan_qr.*
import javax.inject.Inject
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import java.io.IOException
import android.Manifest.permission
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import android.os.Build.VERSION.SDK_INT
import android.support.v4.app.ActivityCompat
import android.util.Log
import java.lang.Thread.sleep


class ScanQRFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    private var token = ""
    private var tokenanterior = ""

    private val MY_PERMISSIONS_REQUEST_CAMERA = 1
    override fun layoutId()= R.layout.fragment_scan_qr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // creo el detector qr
        val barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()
        // creo la camara fuente
        val cameraSource = CameraSource.Builder(context!!, barcodeDetector)
                .setRequestedPreviewSize(1024 , 1024)
                .build()


        camera_view.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(holder: SurfaceHolder) {


                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de Android que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                        Manifest.permission.CAMERA))
                        ;
                        requestPermissions(arrayOf(Manifest.permission.CAMERA),
                                MY_PERMISSIONS_REQUEST_CAMERA)
                    }
                    return
                } else {
                    try {
                        cameraSource.start(camera_view.getHolder())
                    } catch (ie: IOException) {
                        Log.e("CAMERA SOURCE", ie.message)
                    }

                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object :  Detector.Processor<Barcode> {
            override fun release() {
            }


            override fun receiveDetections( detections : Detector.Detections<Barcode> ) {
                val  barcodes = detections.detectedItems;
//
                if (barcodes.size() != 0) {
                    token = barcodes.valueAt(0).displayValue.toString();
                    if (token != tokenanterior) {
                        tokenanterior = token
                        println("El texto es: ${token}")
                        if(token.split(";").size == 14){
                            navigator.showThirdPartyInformation(context!!,token)
                        }
                    }

                }
//
                Thread(Runnable {
                    try {
                        synchronized(this) {
                            sleep(5000L)
                            tokenanterior = ""
                        }
                    } catch (e: InterruptedException) {
                        // TODO Auto-generated catch block
                        Log.e("Error", "Waiting didnt work!!")
                        e.printStackTrace()
                    }
                }).start()
            }
        })


    }
}
