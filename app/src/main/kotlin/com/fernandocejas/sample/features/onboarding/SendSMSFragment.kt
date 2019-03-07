package com.fernandocejas.sample.features.onboarding

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_send_sms.*
import javax.inject.Inject
import android.widget.Toast
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.SEND_SMS
import android.content.res.Resources
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.fernandocejas.sample.core.dataBase.DataBaseHelper


class SendSMSFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    override fun layoutId() = R.layout.fragment_send_sms

    var dbHandler: DataBaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHandler = DataBaseHelper(context!!)

        possitiveButton.setOnClickListener(View.OnClickListener {

            requestSmsPermission()
            navigator.showMap(activity!!)
 })

        negativeButton.setOnClickListener(View.OnClickListener { v -> navigator.showMap(activity!!) })
    }


    private val PERMISSION_SEND_SMS = 123

    private fun requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.SEND_SMS),
                    PERMISSION_SEND_SMS)
        } else {
            // permission already granted run sms send
            sendSms()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_SEND_SMS -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    sendSms()
                } else {
                    // permission denied
                }
                return
            }
        }
    }

    private fun sendSms( ) {
        val message = getString(R.string.sms_text)
        try {
            val smsManager = SmsManager.getDefault()
            val listaTelefonos = dbHandler!!.getContactos()
            for(contact in listaTelefonos){
                smsManager.sendTextMessage(contact.telefono, null, message, null, null)
            }

            Toast.makeText(activity!!, "Mensaje enviado",
                    Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            Toast.makeText(activity!!, ex.message.toString(),
                    Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }
    }

}
