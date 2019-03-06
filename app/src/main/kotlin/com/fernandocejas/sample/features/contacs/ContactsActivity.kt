package com.fernandocejas.sample.features.contacs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fernandocejas.sample.core.platform.BaseActivity

class ContactsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, ContactsActivity::class.java)
    }

    override fun fragment() = ContacsFragment()
}
