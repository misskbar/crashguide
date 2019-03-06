package com.fernandocejas.sample.features.contacs

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.dataBase.DataBaseHelper
import kotlinx.android.synthetic.main.contact_item.view.*

class ThirdPartyAdapter (val items : ArrayList<Contacts>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    var dbHandler: DataBaseHelper? = DataBaseHelper(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contactID?.text = items.get(position).id.toString()
        holder.contactName?.text = items.get(position).nombres
        holder.contactNumber?.text = items.get(position).telefono

        holder.deleteContact.setOnClickListener( View.OnClickListener {
            dbHandler!!.deleteContacto(items.get(position).id)
            items.remove(items.get(position))
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        })

    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val contactID = view.contactID
    val contactName = view.contactName
    val contactNumber = view.contactNumber

    val deleteContact = view.deleteContact
}