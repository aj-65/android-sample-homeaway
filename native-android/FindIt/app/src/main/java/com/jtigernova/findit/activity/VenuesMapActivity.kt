package com.jtigernova.findit.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.jtigernova.findit.Constants.CITY_CENTER_GPS
import com.jtigernova.findit.Constants.DEFAULT_ZOOM
import com.jtigernova.findit.Nav
import com.jtigernova.findit.R
import com.jtigernova.findit.ext.dpToPixels
import com.jtigernova.findit.model.Venue

const val PARAM_VENUES = "venues"

class VenuesMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var venues: ArrayList<Venue>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        venues = intent.getParcelableArrayListExtra(PARAM_VENUES)!!
    }

    override fun onStart() {
        super.onStart()

        Toast.makeText(this@VenuesMapActivity, getString(R.string.click_marker),
                Toast.LENGTH_SHORT).show()
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
        mMap.uiSettings.isZoomControlsEnabled = true

        val padding = dpToPixels(R.dimen.padding_map_content)

        val bounds = LatLngBounds.Builder()

        for (venue in venues) {
            if (venue.location == null) continue

            val venueLatLng = LatLng(venue.location.lat, venue.location.lng)

            mMap.addMarker(MarkerOptions().position(venueLatLng)
                    .title(venue.name).snippet(getString(R.string.click_learn))).tag = venue

            bounds.include(venueLatLng)
        }

        mMap.setOnInfoWindowClickListener {
            val venue = it.tag as Venue

            Nav.venueDetails(this@VenuesMapActivity, venue)
        }

        //as we wait for the map to fully load, let's focus on the city center
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_CENTER_GPS, DEFAULT_ZOOM))

        //make sure that map layout is loaded before setting bounds
        mMap.setOnMapLoadedCallback {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), padding))
        }
    }
}
