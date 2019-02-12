package com.fernandocejas.sample.features.thridpartyinformation

import android.content.Context
import android.content.Intent
import com.fernandocejas.sample.core.platform.BaseActivity

class ThirdPartyListActivity : BaseActivity() {
    companion object {
        fun callingIntent(context: Context) = Intent(context, ThirdPartyListActivity::class.java)
    }

    override fun fragment() = ThirdPartyListFragment()
}
