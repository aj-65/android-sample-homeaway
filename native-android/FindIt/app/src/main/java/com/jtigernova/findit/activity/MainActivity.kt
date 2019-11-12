package com.jtigernova.findit.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jtigernova.findit.R
import com.jtigernova.findit.data.Venue
import com.jtigernova.findit.data.VenueCategory
import com.jtigernova.findit.data.VenueCategoryIcon
import com.jtigernova.findit.data.VenueLocation
import com.jtigernova.findit.ui.VenueItemAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        initResults()
    }

    private fun initResults() {

        val mockIcon = VenueCategoryIcon("https://www.google.com/images/branding/" +
                "googlelogo/2x/googlelogo_color_272x92dp", ".png")

        val mock = arrayOf(
                Venue(id = "0", name = "Bob's", url = "https://google.com",
                        categories = listOf(
                                VenueCategory(id = "0", name = "Test", shortName = "Testtest",
                                        pluralName = "Tests", primary = true, icon = mockIcon)),
                        location = VenueLocation(address = "123 lane", lat = 21.20202,
                                lng = 30.23222, postalCode = "23231", cc = "", city = "Austin",
                                state = "Texas", country = "USA",
                                formattedAddress = listOf("123 land"))),
                Venue(id = "1", name = "Tim Burger Shop", url = "https://google.com",
                        categories = listOf(
                                VenueCategory(id = "1", name = "Test", shortName = "Testtest",
                                        pluralName = "Tests", primary = true, icon = mockIcon)),
                        location = VenueLocation(address = "123 lane", lat = 21.20202,
                                lng = 30.23222, postalCode = "23231", cc = "", city = "Austin",
                                state = "Texas", country = "USA",
                                formattedAddress = listOf("1001 1st Ave (at Madison St)",
                                        "Seattle, WA 98104",
                                        "United States"))))

        viewManager = LinearLayoutManager(this@MainActivity)
        viewAdapter = VenueItemAdapter(context = this@MainActivity, data = mock)

        recyclerView = findViewById<RecyclerView>(R.id.results).apply {
            //content changes do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }
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
