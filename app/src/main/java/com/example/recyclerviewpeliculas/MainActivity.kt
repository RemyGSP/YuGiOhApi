package com.example.recyclerviewpeliculas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var cardDataSource: CardDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navBar)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {

                R.id.searchButton -> {
                    val newScreen = Intent(this, CardSearcher::class.java)
                    this.startActivity(newScreen)
                    Log.d("BottomMenu","True");
                    true
                }
                R.id.favorites -> {
                    val newScreen = Intent(this, FavoritesActivity::class.java)
                    this.startActivity(newScreen)
                    Log.d("BottomMenu","True");
                    true
                }
                else -> false
            }
        }    }
}
