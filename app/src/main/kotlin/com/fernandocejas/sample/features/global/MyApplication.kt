package com.fernandocejas.sample.features.global

import android.app.Application
import com.fernandocejas.sample.features.thridpartyinformation.Terceros

class MyApplication : Application() {
    var globalListTerceros: ArrayList<Terceros> = ArrayList<Terceros>()
}