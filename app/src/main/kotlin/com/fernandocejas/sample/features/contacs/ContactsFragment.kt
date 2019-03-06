package com.fernandocejas.sample.features.contacs

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.dataBase.DataBaseHelper

import kotlinx.android.synthetic.main.fragment_contacts.*
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import kotlinx.android.synthetic.main.contact_item.*
import javax.inject.Inject

class ContacsFragment : BaseFragment() {


    override fun layoutId() = R.layout.fragment_contacts

    @Inject
    lateinit var navigator: Navigator
    var dbHandler: DataBaseHelper? = null
    val PICK_CONTACT = 1

    var lista : ArrayList<Contacts> = ArrayList<Contacts>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Inflate the layout for this fragment


        listaContactos.layoutManager = LinearLayoutManager(activity!!)


        lista.add(Contacts(0,"oswaldo","4491561"))
        lista.add(Contacts(0,"aquiles","4324256"))
        dbHandler = DataBaseHelper(context!!)

        // Access the RecyclerView Adapter and load the data into it
        listaContactos.adapter = ThirdPartyAdapter(dbHandler!!.getContactos() , activity!!)

        fabWrite.setOnClickListener( View.OnClickListener {
            val i = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult(i, PICK_CONTACT)
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            var number = ""
            var name = ""
            val contactUri = data!!.data
            val cursor = activity!!.getContentResolver().query(contactUri, null, null, null, null)
            cursor.moveToFirst()
            var column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            number = cursor.getString(column)
            column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            name = cursor.getString(column)
            var contacto = Contacts(0,name,number)
            dbHandler!!.addContacto(contacto)
            listaContactos.adapter = ThirdPartyAdapter(dbHandler!!.getContactos() , activity!!)

        }
    }


}
