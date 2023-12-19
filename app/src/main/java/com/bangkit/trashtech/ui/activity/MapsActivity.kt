package com.bangkit.trashtech.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.trashtech.R
import com.bangkit.trashtech.data.api.ApiConfig.Companion.getMapsApiService
import com.bangkit.trashtech.data.api.ApiService
import com.bangkit.trashtech.data.response.MapsResponse
import com.bangkit.trashtech.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var mMarker: Marker? = null
    private lateinit var apiService: ApiService

    companion object {
        private const val MY_PERMISSION_CODE: Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Bank Sampah Terdekat"
            setHomeAsUpIndicator(R.drawable.ic_backspace)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MapsActivity, R.color.primary)))
        }

        apiService = getMapsApiService()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        buildLocationRequest()
        buildLocationCallback()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    private fun nearbyPlace(typePlace: String) {
        mMap.clear()
        val url = getUrl(latitude, longitude, typePlace)
        apiService.getNearbyPlaces(url)
            .enqueue(object : Callback<MapsResponse> {
                override fun onResponse(
                    call: Call<MapsResponse>,
                    response: Response<MapsResponse>,
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            for (i in 0 until it.results!!.size) {
                                val markerOptions = MarkerOptions()
                                val googlePlace = it.results[i]
                                val lat = googlePlace?.geometry?.location?.lat
                                val lng = googlePlace?.geometry?.location?.lng
                                val placeName = googlePlace?.name
                                val latLng = LatLng(lat as Double, lng as Double)

                                markerOptions.position(latLng)
                                markerOptions.title(placeName)
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

                                mMap.addMarker(markerOptions)
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MapsResponse>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlaceUrl.append("location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=10000")
        googlePlaceUrl.append("&type=point_of_interest&keyword=$typePlace")
        googlePlaceUrl.append("&key=${getString(R.string.maps_api_key)}")
        return googlePlaceUrl.toString()
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                handleLocationResult(p0)
            }
        }
    }

    private fun handleLocationResult(locationResult: LocationResult) {
        val lastLocation = locationResult.lastLocation
        mMarker?.remove()
        lastLocation?.let {
            latitude = it.latitude
            longitude = it.longitude
            val latLng = LatLng(latitude, longitude)
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Posisi Anda")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            mMarker = mMap.addMarker(markerOptions)

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

            // Request nearby places with the updated location
            nearbyPlace("banksampah")
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 3000
            smallestDisplacement = 10f
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_CODE
            )
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true
            }
        }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                    nearbyPlace("banksampah")

                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_CODE
            )
        }

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
