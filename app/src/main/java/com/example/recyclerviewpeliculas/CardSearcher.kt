package com.example.recyclerviewpeliculas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.size.ViewSizeResolver
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class CardSearcher: AppCompatActivity() {
    private lateinit var cardDataSource: CardDataSource

    private lateinit var cardAdapter: CardAdapter
    val movieApi = CardAPI()
    var counter = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView = findViewById<ImageView>(R.id.spinningImage)
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.spin_card)
        imageView.startAnimation(rotateAnimation)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navBar)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {

                R.id.homeButton -> {
                    val newScreen = Intent(this, MainActivity::class.java)
                    this.startActivity(newScreen)
                    true
                }
                else -> false
            }
        }
        cardDataSource = CardDataSource(this)
        cardDataSource.open()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPeliculas)
        cardAdapter = CardAdapter(emptyList(),cardDataSource)
        if (cardAdapter.allCards.count() > 0 ){
            imageView.visibility = View.GONE
        }
        recyclerView.adapter = cardAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        val searchView= findViewById<SearchView>(R.id.searchBar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search submission (optional)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cardAdapter.filterByQuery(newText)
                return true
            }
        })



        // Load the initial data
        addDataToAdapter(counter)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Load the next page when reaching the end of the list
                    addDataToAdapter(counter)
                }
            }
        })

    }

    private fun addDataToAdapter(page: Int) {

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val movies: List<Card>? = movieApi.makeApiCall(counter);
                val image = findViewById<ImageView>(R.id.spinningImage)
                image.imageAlpha = 0
                if (movies != null) {
                    // Update the existing adapter with new data
                    cardAdapter.addData(movies)
                } else {
                    // Handle the case where movies is null (e.g., API call failed)
                    Log.e("NotAmogus", "Failed to fetch movies")
                }
            } catch (e: Exception) {
                // Handle exceptions here
                Log.e("NotAmogus", "Error: ${e.message}")
            }
            counter++;
        }
    }

    private fun addDataToAdapter(page: Int, image :ImageView) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val movies: List<Card>? = movieApi.makeApiCall(counter);

                // Check for null before updating the adapter
                if (movies != null) {
                    // Update the existing adapter with new data
                    cardAdapter.addData(movies)
                } else {
                    // Handle the case where movies is null (e.g., API call failed)
                    Log.e("NotAmogus", "Failed to fetch movies")
                }
            } catch (e: Exception) {
                // Handle exceptions here
                Log.e("NotAmogus", "Error: ${e.message}")
            }
            counter++;
        }
    }


    suspend fun ChangeVisibility(image: ImageView)
    {
        delay(3000)
        Log.d("NotAmogus", "Setting image visibility to GONE")
        image.visibility = View.GONE
        Log.d("NotAmogus", "Image visibility set to: ${image.visibility}")
    }


    public fun loadApiOnRecyclerView(page : Int) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPeliculas)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        recyclerView.adapter = null

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val movies: List<Card>? = movieApi.makeApiCall(counter);
                // Check for null before creating the adapter
                if (movies != null) {
                    // Ensure that the context is not null
                    val context: Context = this@CardSearcher
                    cardAdapter = CardAdapter(movies,cardDataSource)
                    Log.d("NotAmogus", movies.toString())
                    cardAdapter.addData(movies)

                } else {
                    // Handle the case where movies is null (e.g., API call failed)
                    Log.e("NotAmogus", "Failed to fetch movies")
                }
            } catch (e: Exception) {
                // Handle exceptions here
                Log.e("NotAmogus", "Error: ${e.message}")
            }
            counter++;

        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


}