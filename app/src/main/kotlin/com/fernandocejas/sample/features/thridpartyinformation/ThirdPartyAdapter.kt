package com.fernandocejas.sample.features.thridpartyinformation

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fernandocejas.sample.R
import kotlinx.android.synthetic.main.third_party_item.view.*

class ThirdPartyAdapter (val items : ArrayList<Terceros>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.third_party_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.thirdPartyName?.text = items.get(position).nombres
        holder.thirdPartySurname?.text = items.get(position).apellidos
        holder.thirdPartyBrand?.text = items.get(position).vehiculo.marca
        holder.thirdPartyModel?.text = items.get(position).vehiculo.modelo
        holder.thirdPartyPatent?.text = items.get(position).vehiculo.patente

        holder.editThird.setOnClickListener( View.OnClickListener {
            println("probando el editar en la posicion $position")
        })

        holder.deleteThird.setOnClickListener( View.OnClickListener {
            items.remove(items.get(position))
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        })

    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val thirdPartyName = view.thirdPartyName
    val thirdPartySurname = view.thirdPartySurname
    val thirdPartyBrand = view.thirdPartyBrand
    val thirdPartyModel = view.thirdPartyModel
    val thirdPartyPatent = view.thirdPartyPatent

    val editThird = view.editThird
    val deleteThird = view.deleteThird
}