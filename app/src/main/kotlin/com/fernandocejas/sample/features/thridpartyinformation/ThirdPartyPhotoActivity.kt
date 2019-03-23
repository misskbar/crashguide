package com.fernandocejas.sample.features.thridpartyinformation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.fernandocejas.sample.core.platform.BaseActivity


class ThirdPartyPhotoActivity : BaseActivity() {

    var positionArray = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
        positionArray = intent!!.getIntExtra(ThirdPartyPhotoActivity.POSITIION_ARRAY,-1)
    }

    companion object {

        private val POSITIION_ARRAY = "position_array"

        fun callingIntent(context: Context) = Intent(context, ThirdPartyPhotoActivity::class.java)

        fun callingIntent(context: Context, nextActivity: Int): Intent{
            val intent = Intent(context, ThirdPartyPhotoActivity::class.java)
            intent.putExtra(POSITIION_ARRAY, nextActivity)
            return intent
        }
    }

    override fun fragment() = ThirdPartyPhotoFragment()

}
