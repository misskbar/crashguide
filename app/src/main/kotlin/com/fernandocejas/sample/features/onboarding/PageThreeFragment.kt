package com.fernandocejas.sample.features.onboarding

import android.os.Bundle
import android.view.View
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_page_three.*
import javax.inject.Inject


class PageThreeFragment : BaseFragment() {
    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)


    }


    override fun layoutId() = R.layout.fragment_page_three


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startButton.setOnClickListener(View.OnClickListener { navigator.showIsAnyInjured(activity!!)})

    }
}
