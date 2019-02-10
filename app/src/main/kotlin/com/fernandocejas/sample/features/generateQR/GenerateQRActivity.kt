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

    var datasingUpData: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        datasingUpData = intent.getStringExtra(SING_UP_DATA)
                ?: throw IllegalStateException("field $SING_UP_DATA missing in Intent")
    }

    public fun getSingUpData() = datasingUpData

    companion object {

        private val SING_UP_DATA = "user_id"

        fun callingIntent(context: Context) = Intent(context, GenerateQRActivity::class.java)

        fun callingIntent(context: Context, extra: String): Intent{
            println("GenerateQRActivity inicia el metodo callingIntent")
            val intent = Intent(context, GenerateQRActivity::class.java)
            println("GenerateQRActivity creo el inten")
            intent.putExtra(SING_UP_DATA, extra)
            println("GenerateQRActivity coloca el extra")
            return intent
        }
    }

    override fun fragment() = GenerateQRFragment()
}
