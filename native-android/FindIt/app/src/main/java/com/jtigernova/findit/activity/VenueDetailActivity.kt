package com.jtigernova.findit.activity

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jtigernova.findit.R
import com.jtigernova.findit.data.Venue
import kotlinx.android.synthetic.main.activity_venue_detail.*

class VenueDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mLocationPermissionGranted: Boolean = false

    private lateinit var name: TextView
    private lateinit var category: TextView
    private lateinit var address: TextView

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private var mCurrentLocation: Location? = null
    private var mLastKnownLocation: Location? = null
    private var mCameraPosition: CameraPosition? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val venue = intent.getParcelableExtra<Venue>(Venue::class.java.name)

        initDetail(venue)
        initMap()

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initDetail(venue: Venue?) {
        name = findViewById(R.id.name)
        category = findViewById(R.id.category)
        address = findViewById(R.id.address)

        name.text = venue?.name
        category.text = venue?.mainCategoryName ?: "Unknown"

        val sb = StringBuilder()

        for (line in venue?.location?.formattedAddress!!) {
            sb.appendln(line)
        }

        address.text = sb
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //add marker for Seattle

        mMap.addMarker(MarkerOptions().position(GPS_SEATTLE).title("Center of Seattle"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GPS_SEATTLE))

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
        outState.putParcelable(KEY_LOCATION, mCurrentLocation)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getLocationPermission() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    private fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: ${e.message}")
        }

    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    private fun getDeviceLocation() {

        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        mCurrentLocation = it.result
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(mCurrentLocation!!.latitude,
                                        mCurrentLocation!!.longitude), DEFAULT_ZOOM))
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: ${it.exception}")

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GPS_SEATTLE, DEFAULT_ZOOM))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: ${e.message}")
        }

    }

    companion object {
        private val TAG = javaClass.simpleName
        private const val KEY_LOCATION = "location"
        private const val KEY_CAMERA_POSITION = "camera.position"
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 990
        private const val DEFAULT_ZOOM = 12.0f

        private val GPS_SEATTLE = LatLng(47.6062, 122.3321)
    }
}
