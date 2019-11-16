package com.jtigernova.findit.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.text.trimmedLength
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.findit.Constants.CITY_CENTER_GPS
import com.jtigernova.findit.Nav
import com.jtigernova.findit.R
import com.jtigernova.findit.model.Venue
import com.jtigernova.findit.persistence.Prefs
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

    private var currentItemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.app_name_city)
        setSupportActionBar(toolbar)

        fab.hide()

        initSearch()
        initViewModels()
        initResults()
    }

    override fun onStop() {
        super.onStop()

        Prefs.saveFavoriteVenues(this, favViewModel.favoriteVenues.value!!.toList())
    }

    private fun initSearch() {
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {
                cancelRequests()
                error.visibility = View.INVISIBLE

                if (text.toString().trimmedLength() > 2) {
                    loading.visibility = View.VISIBLE

                    mFourSq.getPlaces(text.toString()) { success: Boolean, venues: Array<Venue> ->
                        if (success && venues.any()) {
                            recyclerView.adapter = getAdapter(venues = venues)
                        } else {
                            error.visibility = View.VISIBLE
                        }

                        loading.visibility = View.INVISIBLE
                    }
                } else {
                    recyclerView.adapter = null
                    loading.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun initViewModels() {
        favViewModel = AppState.favoriteViewModel
//        favViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)

        favViewModel.favoriteVenues.value = Prefs.getFavoriteVenues(this).toMutableSet()

        favViewModel.favoriteVenues.observe(this, Observer<MutableSet<String>> {
            if (currentItemPosition < 0 ||
                    (currentItemPosition >= viewAdapter.itemCount)) return@Observer

            //use post() to avoid state issues with recyclerView
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

            adapter = null
        }
    }

    private fun venueClick(): (venue: Venue, viewPosition: Int) -> Unit {
        return { venue: Venue, viewPosition: Int ->
            currentItemPosition = viewPosition

            Nav.venueDetails(context = this, venue = venue)
        }
    }

    private fun getAdapter(venues: Array<Venue>): RecyclerView.Adapter<*> {
        viewAdapter = VenueItemAdapter(context = this@MainActivity, venues = venues,
                favoriteViewModel = favViewModel,
                venueClick = venueClick())

        for (venue in venues) {
            venue.location?.calculateDistanceFromCityCenter(CITY_CENTER_GPS)
        }

        if (venues.any()) {
            fab.setOnClickListener {
                Nav.venuesMap(context = this@MainActivity, venues =
                venues.toCollection(ArrayList()))
            }

            fab.show()
        } else {
            fab.hide()
            fab.setOnClickListener(null)
        }

        return viewAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
