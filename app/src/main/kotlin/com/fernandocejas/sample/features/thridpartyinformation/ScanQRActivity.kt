package com.fernandocejas.sample.features.thridpartyinformation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fernandocejas.sample.core.platform.BaseActivity

class ScanQRActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, ScanQRActivity::class.java)
    }

    override fun fragment() = ScanQRFragment()
}
