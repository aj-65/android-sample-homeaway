package com.jtigernova.findit.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.jtigernova.findit.Constants.CITY_CENTER_GPS
import com.jtigernova.findit.Constants.CITY_NAME
import com.jtigernova.findit.Constants.DEFAULT_ZOOM
import com.jtigernova.findit.R
import com.jtigernova.findit.ext.dpToPixels
import com.jtigernova.findit.ext.setupFavorite
import com.jtigernova.findit.model.Venue
import com.jtigernova.findit.persistence.Prefs
import com.jtigernova.findit.repository.AppState
import com.jtigernova.findit.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.content_venue_detail.*

const val PARAM_VENUE = "venue"

class VenueDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var venue: Venue
    private lateinit var mMap: GoogleMap

    private lateinit var favViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        venue = intent.getParcelableExtra(PARAM_VENUE)!!

        initViewModels()
        initDetail(venue)
        initMap()
    }

    private fun initViewModels() {
        favViewModel = AppState.favoriteViewModel
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initDetail(venue: Venue?) {
        val context = this@VenueDetailActivity
        name.text = venue?.name
        category.text = venue?.mainCategoryName ?: "Unknown"
        venue_distance_from_city_center.text = getString(R.string.distance_from_city_center,
                venue?.location?.getDistanceFromCityCenterInMilesDisplay())

        fav.isChecked = Prefs.isFavoriteVenue(context, venue?.id!!)

        fav.setupFavorite(context = context, favViewModel = favViewModel, venue = venue, onCheckedChange = {

        })

        val sb = StringBuilder()

        for (line in venue.location?.formattedAddress!!) {
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
        Log.d(TAG, "map is ready")

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        //add marker for city

        mMap.addMarker(MarkerOptions().position(CITY_CENTER_GPS).title("Center of $CITY_NAME"))
                .showInfoWindow()

        val venueLoc = venue.location

        if (venueLoc != null) {
            val venueLocLatLng = LatLng(venueLoc.lat, venueLoc.lng)
            val locOpts = MarkerOptions().position(venueLocLatLng).apply {
                title(venue.name)
                snippet(venue.mainCategoryName)
            }

            mMap.addMarker(locOpts).showInfoWindow()

            val padding = dpToPixels(R.dimen.padding_map_content)

            val bounds = LatLngBounds.Builder().include(CITY_CENTER_GPS).include(venueLocLatLng).build()

            //as we wait for the map to fully load, let's focus on the city center
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_CENTER_GPS, DEFAULT_ZOOM))

            //make sure that map layout is loaded before setting bounds
            mMap.setOnMapLoadedCallback {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            }
        } else {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_CENTER_GPS, DEFAULT_ZOOM))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = VenueDetailActivity::class.java.simpleName
        private const val KEY_CAMERA_POSITION = "camera.position"
    }
}
