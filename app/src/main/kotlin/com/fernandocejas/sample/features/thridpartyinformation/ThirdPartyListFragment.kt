package com.fernandocejas.sample.features.thridpartyinformation

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_third_party_list.*
import javax.inject.Inject
import android.transition.Explode


class ThirdPartyListFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    var isFABOpen = false

    override fun layoutId() = R.layout.fragment_third_party_list




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Creates a vertical Layout Manager
        listaTerceros.layoutManager = LinearLayoutManager(activity!!)


        var explode = Explode()

        // Access the RecyclerView Adapter and load the data into it
        listaTerceros.adapter = ThirdPartyAdapter(AndroidApplication.globalListTerceros, activity!!)
//        fabQR.setOnClickListener( View.OnClickListener { navigator.showThirdPartyInformation(activity!!) })
        fabWrite.setOnClickListener( View.OnClickListener { navigator.showThirdPartyInformation(activity!!) })
    }





}
