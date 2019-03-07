package com.fernandocejas.sample.features.onboarding

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fernandocejas.sample.core.platform.BaseActivity

class SendSMSActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, SendSMSActivity::class.java)
    }

    override fun fragment() = SendSMSFragment()
}
