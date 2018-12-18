package com.fernandocejas.sample.features.pointinmap

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.fernandocejas.sample.features.pointinmap.PlaceArrayAdapter.PlaceAutocomplete
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import java.util.concurrent.TimeUnit


class PlaceArrayAdapter(context: Context?, resource: Int) : ArrayAdapter<PlaceAutocomplete>(context, resource), Filterable {

    private val TAG = "PlaceArrayAdapter"
    private var mGoogleApiClient: GoogleApiClient? = null
    private val mPlaceFilter: AutocompleteFilter? = null
    private val mBounds: LatLngBounds? = null
    private var mResultList: ArrayList<PlaceAutocomplete> = ArrayList<PlaceAutocomplete>()

    inner class PlaceAutocomplete(var placeId: CharSequence?, var description: CharSequence?) {
        override fun toString(): String {
            return description.toString()
        }
    }

    fun setGoogleApiClient(googleApiClient: GoogleApiClient?) {
        if (googleApiClient == null || !googleApiClient.isConnected) {
            mGoogleApiClient = null
        } else {
            mGoogleApiClient = googleApiClient
        }
    }

    override fun getCount(): Int {

            return mResultList!!.size

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {

                val filterResults = FilterResults()
                synchronized(filterResults) {
                    if (constraint != null) {
                        // Clear and Retrieve the autocomplete results.
                        val resultList = getPredictions(constraint)

                        filterResults.values = resultList
                        filterResults.count = resultList!!.size

                    }
                    return filterResults
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results!!.count > 0) {
                    mResultList!!.clear()
                    mResultList = results!!.values as ArrayList<PlaceAutocomplete>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }


    private fun getPredictions(constraint: CharSequence?): ArrayList<PlaceAutocomplete>? {

        if (mGoogleApiClient != null && constraint!=null) {

            val results = Places.GeoDataApi
                    .getAutocompletePredictions(mGoogleApiClient!!, constraint.toString(),
                            mBounds, mPlaceFilter)

            val autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS)

            val status = autocompletePredictions.status
            if (!status.isSuccess) {
                autocompletePredictions.release()
                return null
            }

            val iterator = autocompletePredictions.iterator()

            val resultList = ArrayList<PlaceAutocomplete>(autocompletePredictions.count)

            while (iterator.hasNext()) {
                val prediction = iterator.next()
                resultList.add(PlaceAutocomplete(prediction.placeId as CharSequence,
                        prediction.getFullText(null)))
            }

            autocompletePredictions.release()
            return resultList
        }

        return null
    }

    override fun getItem(position: Int): PlaceAutocomplete? {
        return mResultList!!.get(position)
    }
}