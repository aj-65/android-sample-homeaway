package com.jtigernova.findit.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.text.trimmedLength
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.findit.Constants.CITY_CENTER_GPS
import com.jtigernova.findit.Constants.CITY_NAME
import com.jtigernova.findit.Nav
import com.jtigernova.findit.R
import com.jtigernova.findit.model.Venue
import com.jtigernova.findit.repository.AppState
import com.jtigernova.findit.view.VenueItemAdapter
import com.jtigernova.findit.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: VenueItemAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var favViewModel: FavoriteViewModel

    //used to know which item UI to update after data changes
    private var currentItemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.app_name_city, CITY_NAME)
        setSupportActionBar(toolbar)

        fab.hide()

        initSearch()
        initViewModels()
        initResults()
    }

    override fun onStop() {
        super.onStop()

        AppState.persist(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        AppState.persist(this)
    }

    private fun initSearch() {
        //do searches after the search text changes
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {
                //cancel pending requests
                cancelRequests()

                error.visibility = View.INVISIBLE

                //require at least 3 chars
                if (text.toString().trimmedLength() > 2) {
                    loading.visibility = View.VISIBLE

                    //call four FourSquare to get places
                    mFourSq.getPlaces(text.toString()) { success: Boolean, venues: Array<Venue> ->
                        if (success && venues.any()) {
                            //update results
                            recyclerView.adapter = getAdapter(venues = venues)
                        } else {
                            recyclerView.adapter = null
                            error.visibility = View.VISIBLE
                        }

                        loading.visibility = View.INVISIBLE
                    }
                } else {
                    //clear results
                    recyclerView.adapter = null
                    loading.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun initViewModels() {
        favViewModel = AppState.favoriteViewModel
        //load app state from disk
        AppState.load(this)

        //watch for changes to favorites
        favViewModel.favoriteVenues.observe(this, Observer<MutableSet<String>> {
            //current position my not be set yet
            if (currentItemPosition < 0 ||
                    (currentItemPosition >= viewAdapter.itemCount)) return@Observer

            //use post() to avoid state issues with recyclerView (UI thread)
            //for example, we can't notify while the view is scrolling
            recyclerView.post {
                viewAdapter.notifyItemChanged(currentItemPosition)
            }
        })
    }

    private fun initResults() {
        viewManager = LinearLayoutManager(this@MainActivity)

        recyclerView = findViewById<RecyclerView>(R.id.results).apply {
            //content changes do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            layoutManager = viewManager

            //nothing in list by default
            adapter = null
        }
    }

    private fun venueClick(): (venue: Venue, viewPosition: Int) -> Unit {
        return { venue: Venue, viewPosition: Int ->
            //update position
            currentItemPosition = viewPosition

            //send to details screen
            Nav.venueDetails(context = this, venue = venue)
        }
    }

    private fun getAdapter(venues: Array<Venue>): RecyclerView.Adapter<*> {
        //create a new adapter for list
        viewAdapter = VenueItemAdapter(context = this@MainActivity, venues = venues,
                favoriteViewModel = favViewModel,
                venueClick = venueClick())

        for (venue in venues) {
            //we need to calculate the distance for each venue
            venue.location?.calculateDistanceFromCityCenter(CITY_CENTER_GPS)
        }

        if (venues.any()) {
            //allow the user to display a map of all venues
            fab.setOnClickListener {
                Nav.venuesMap(context = this@MainActivity,
                        venues = venues.toCollection(ArrayList()))
            }

            fab.show()
        } else {
            //don't allow map if there are no venues
            fab.hide()
            fab.setOnClickListener(null)
        }

        return viewAdapter
    }
}
