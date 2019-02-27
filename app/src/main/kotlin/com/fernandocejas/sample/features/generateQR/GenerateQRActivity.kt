/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.sample.features.generateQR

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.fernandocejas.sample.core.platform.BaseActivity

class GenerateQRActivity : BaseActivity() {

    var nextActivity = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
        nextActivity = intent!!.getIntExtra(NEXT_ACTIVITY,0)
    }


    companion object {

        private val NEXT_ACTIVITY = "next_activity"

        fun callingIntent(context: Context) = Intent(context, GenerateQRActivity::class.java)

        fun callingIntent(context: Context, nextActivity: Int): Intent{
            val intent = Intent(context, GenerateQRActivity::class.java)
            intent.putExtra(NEXT_ACTIVITY, nextActivity)
            return intent
        }
    }

    override fun fragment() = GenerateQRFragment()
}
