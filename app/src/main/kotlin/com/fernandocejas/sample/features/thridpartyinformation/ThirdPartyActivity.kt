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
package com.fernandocejas.sample.features.thridpartyinformation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fernandocejas.sample.core.platform.BaseActivity

class ThirdPartyActivity : BaseActivity() {

    var idThird: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra(THIRD_DATA) != null)
            idThird = intent!!.getStringExtra(THIRD_DATA)
    }

    public fun getThirdID() = idThird

    companion object {

        private val THIRD_DATA = "user_id"


        fun callingIntent(context: Context) = Intent(context, ThirdPartyActivity::class.java)


        fun callingIntent(context: Context, extra: String): Intent{
            val intent = Intent(context, ThirdPartyActivity::class.java)
            intent.putExtra(THIRD_DATA, extra)
            return intent
        }

    }

    override fun fragment() = ThirdPartyFragment()
}
