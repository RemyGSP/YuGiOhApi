package com.example.recyclerviewpeliculas

import FavoriteCardAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewpeliculas.CardDataSource
import com.example.recyclerviewpeliculas.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteCardAdapter
    private lateinit var cardDataSource: CardDataSource
    val movieApi = CardAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cardDataSource = CardDataSource(this)
        cardDataSource.open()

        lifecycleScope.launch {
            // Make API call to get all cards
            val cards = movieApi.makeApiCall(0)
            cards?.let { apiCards ->
                // Fetch the list of favorite cards from the database
                val favoriteCards = cardDataSource.getAllFavoriteCards()
                val favoriteCardList = favoriteCards.mapNotNull { apiCards.getOrNull(it) }
                // Initialize the adapter with favorite cards
                adapter = FavoriteCardAdapter(favoriteCardList)
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroy() {
        cardDataSource.close()
        super.onDestroy()
    }
}