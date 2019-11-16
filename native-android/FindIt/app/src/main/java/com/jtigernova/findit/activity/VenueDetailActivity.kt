package com.jtigernova.findit.activity

import android.os.Bundle
import android.util.Log
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
import com.jtigernova.findit.Constants.MAP_DEFAULT_ZOOM
import com.jtigernova.findit.R
import com.jtigernova.findit.ext.dpToPixels
import com.jtigernova.findit.ext.setupFavorite
import com.jtigernova.findit.model.Venue
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //require a venue
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
        category.text = venue?.getMainCategoryName() ?: getString(R.string.category_unknown)
        venue_distance_from_city_center.text = getString(R.string.distance_from_city_center,
                venue?.location?.getDistanceFromCityCenterInMilesDisplay())

        //check from app state if this venue is a favorite
        fav.isChecked = favViewModel.favoriteVenues.value!!.contains(venue!!.id)

        fav.setupFavorite(context = context, favViewModel = favViewModel, venue = venue, onCheckedChange = {
            AppState.persist(this)
        })

        //build address
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
        //add zoom controls
        mMap.uiSettings.isZoomControlsEnabled = true

        //add marker for city
        mMap.addMarker(MarkerOptions().position(CITY_CENTER_GPS).title("Center of $CITY_NAME"))
                .showInfoWindow()

        val venueLoc = venue.location

        //TODO every venue probably has a location from FourSquare, so this can be updated
        if (venueLoc != null) {
            val venueLocLatLng = LatLng(venueLoc.lat, venueLoc.lng)
            val locOpts = MarkerOptions().position(venueLocLatLng).apply {
                title(venue.name)
                snippet(venue.getMainCategoryName() ?: getString(R.string.category_unknown))
            }

            mMap.addMarker(locOpts).showInfoWindow()

            val padding = dpToPixels(R.dimen.padding_map_content)

            val bounds = LatLngBounds.Builder().include(CITY_CENTER_GPS).include(venueLocLatLng).build()

            //as we wait for the map to fully load, let's focus on the city center
            //Note: CameraUpdateFactory.newLatLngBounds can not be used until the map is fully loaded
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_CENTER_GPS, MAP_DEFAULT_ZOOM))

            //make sure that map layout is loaded before setting bounds
            mMap.setOnMapLoadedCallback {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            }
        } else {
            //fallback to the city marker
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_CENTER_GPS, MAP_DEFAULT_ZOOM))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private val TAG = VenueDetailActivity::class.java.simpleName
        private const val KEY_CAMERA_POSITION = "camera.position"
    }
}
