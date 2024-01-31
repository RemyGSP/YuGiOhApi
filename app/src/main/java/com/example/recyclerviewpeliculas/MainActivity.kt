package com.example.recyclerviewpeliculas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var peliculasAdapter: PeliculasAdapter
    val movieApi = MovieAPI()
    var counter = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPeliculas)
        peliculasAdapter = PeliculasAdapter(emptyList())
        recyclerView.adapter = peliculasAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

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
                Log.d("ImageURL", movies.toString());
                // Check for null before updating the adapter
                if (movies != null) {
                    // Update the existing adapter with new data
                    peliculasAdapter.addData(movies)
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


    public fun loadApiOnRecyclerView(page : Int) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPeliculas)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Clear the previous adapter and data
        recyclerView.adapter = null
        // Call the Movie API and update the adapter when the data is received

        // Use the lifecycleScope of the activity to launch the coroutine
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val movies: List<Card>? = movieApi.makeApiCall(counter);
                // Check for null before creating the adapter
                if (movies != null) {
                    // Ensure that the context is not null
                    val context: Context = this@MainActivity
                    peliculasAdapter = PeliculasAdapter(movies)
                    Log.d("NotAmogus", movies.toString())
                    peliculasAdapter.addData(movies)

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
}

