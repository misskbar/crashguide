package com.fernandocejas.sample.features.pointinmap

import android.content.Context
import android.content.Intent
import com.fernandocejas.sample.core.platform.BaseActivity


class MapActivity : BaseActivity() {
    companion object {
        fun callingIntent(context: Context) = Intent(context, MapActivity::class.java)
    }

    override fun fragment() = MapFragment()
}