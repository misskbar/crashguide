package com.fernandocejas.sample.features.pointinmap


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.dataBase.DataBaseHelper
import com.fernandocejas.sample.core.navigation.Navigator
import com.fernandocejas.sample.core.platform.BaseFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.fragment_maps.*
import java.util.*
import javax.inject.Inject


class MapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, AdapterView.OnItemClickListener {

    private lateinit var mGoogleApiClient: GoogleApiClient

    private val GOOGLE_API_CLIENT_ID = 0

    private lateinit var mPlaceArrayAdapter: PlaceArrayAdapter

    private var geoDataClient: GeoDataClient? = null

    private var mUpdatePlaceDetailsCallback: ResultCallback<PlaceBuffer> = ResultCallback<PlaceBuffer>() {

        placeBuffer ->
        run {
            if (!placeBuffer.getStatus().isSuccess()) {
                Log.e("PlaceAutocompleteFragme", "Place query did not complete. Error: " +
                        placeBuffer.getStatus().toString());

            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val item = mPlaceArrayAdapter.getItem(position);

        val placeId = item?.placeId

        var placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId.toString())

        placeResult.setResultCallback(mUpdatePlaceDetailsCallback)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    override fun onConnectionSuspended(p0: Int) {
        mPlaceArrayAdapter.setGoogleApiClient(null); }

    override fun onCameraIdle() {

        val center = mMap.cameraPosition.target
        val geocoder = Geocoder(context, Locale.getDefault())
        try{
            if (geocoder.getFromLocation(center.latitude, center.longitude, 1).size > 0){
                address.setText(geocoder.getFromLocation(center.latitude, center.longitude, 1)[0].getAddressLine(0).toString().split(",")[0])
//                AndroidApplication.ubicacionAccidente = geocoder.getFromLocation(center.latitude, center.longitude, 1)[0].getAddressLine(0).toString().split(",")[0]
                address.setSelection(address.getText().length)
                address.dismissDropDown()
            }
        }catch(e: Exception){

        }


        AndroidApplication.ubicacionAccidente = address.getText().toString()

    }

    @Inject
    lateinit var navigator: Navigator

    private lateinit var mMap: GoogleMap

    private val LOCATION_REQUEST_CODE = 101

    override fun layoutId() = R.layout.fragment_maps

    private var isFirstTime = true

    lateinit var currentLocation: LatLng

    var dbHandler: DataBaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        geoDataClient = Places.getGeoDataClient(context!!)

        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(activity!!, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        address.setThreshold(3)

        (address as AutoCompleteTextView).setOnItemClickListener(this)

        mPlaceArrayAdapter = PlaceArrayAdapter(context, android.R.layout.simple_list_item_1)
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient)

        address.setAdapter(mPlaceArrayAdapter)

        dbHandler = DataBaseHelper(context!!)
        nextButton.setOnClickListener(View.OnClickListener {
            if(dbHandler!!.existsUsuario()){
                println("el usuario existe")
                navigator.showThirdPartyInformation(activity!!)
            }else{
                println("el usuario NO existe")
                navigator.showSignUp(activity!!,Navigator.activityMap)
            }

        })

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-33.447487, -70.673676), 10.0f));
        mMap.setOnCameraIdleListener(this)


            val permission = ContextCompat.checkSelfPermission(activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
                getLocation()

            } else {
                requestPermission()

            }

    }

    private fun requestPermission() {

        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                } else {

                    val mapFragment = childFragmentManager
                            .findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)

                    getLocation()

                    mMap.isMyLocationEnabled = true
                }
            }
        }
    }


    fun getLocation() {

        var locationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location?) {
                var latitude = location!!.latitude
                var longitude = location!!.longitude

                Log.i("test", "Latitute: $latitude ; Longitute: $longitude")

                currentLocation = LatLng(latitude, longitude)

                if (isFirstTime) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
                    isFirstTime = false

                    val geocoder = Geocoder(context, Locale.getDefault())
                    println("latitude: $latitude longitude: $longitude");
                    try {
                        if (geocoder.getFromLocation(latitude, longitude, 1).size > 0) {
                            address.setText(geocoder.getFromLocation(latitude, longitude, 1)[0].getAddressLine(0).toString().split(",")[0])
//                            AndroidApplication.ubicacionAccidente = geocoder.getFromLocation(latitude, longitude, 1)[0].getAddressLine(0).toString().split(",")[0]
                            AndroidApplication.ubicacionAccidente = address.getText().toString()
                            address.setSelection(address.getText().length)
                            address.dismissDropDown()
                        }
                    }
                    catch (e: Exception) {
                        // handler
                    }

                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }
        }

        try {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE)
            return
        }

        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)


    }

    override fun onResume() {
        super.onResume()
        isFirstTime = true
    }

    fun toBounds(center: LatLng, radiusInMeters: Double): LatLngBounds {
        var distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        var southwestCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        var northeastCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return LatLngBounds(southwestCorner, northeastCorner);
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.stopAutoManage(activity!!);
        mGoogleApiClient.disconnect();
    }

}