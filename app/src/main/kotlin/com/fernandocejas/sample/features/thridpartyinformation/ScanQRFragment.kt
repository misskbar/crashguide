package com.fernandocejas.sample.features.thridpartyinformation

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


class ScanQRFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    override fun layoutId()= R.layout.fragment_scan_qr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Creates a vertical Layout Manager
        // creo el detector qr
        val barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()

        // creo la camara fuente
        val cameraSource = CameraSource.Builder(context!!, barcodeDetector)
                .setRequestedPreviewSize(640, 640)
                .build()


        camera_view.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {


                // verifico si el usuario dio los permisos para la camara
                if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(camera_view.getHolder())
                    } catch (ie: IOException) {
//                            Log.e("CAMERA SOURCE", ie.getMessage())
                    }

                } else {
                    Toast.makeText(context, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object :  Detector.Processor<Barcode> {
            override fun release() {
            }


            override fun receiveDetections( detections : Detector.Detections<Barcode> ) {
                val  barcodes : SparseArray<Barcode> = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    var token = barcodes.valueAt(0).displayValue.toString();
                    Toast.makeText(context, "El texto es: $token",Toast.LENGTH_LONG).show()
                }

                barcodeDetector.release()
            }
        })


    }
}
