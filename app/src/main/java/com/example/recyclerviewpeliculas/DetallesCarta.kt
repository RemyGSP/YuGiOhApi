package com.example.recyclerviewpeliculas

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class DetallesCarta : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_details)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navBar)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {

                R.id.goBack -> {

                    finish()
                    true
                }
                R.id.homeButton -> {
                    val newScreen = Intent(this, MainActivity::class.java)
                    this.startActivity(newScreen)
                    true
                }

                R.id.searchButton -> {
                    val newScreen = Intent(this, CardSearcher::class.java)
                    this.startActivity(newScreen)
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val clickedCard: Card? = intent?.extras?.getSerializable("clickedMovie") as? Card

        val titleTextView = findViewById<TextView>(R.id.tituloIntentCompleto)
        val imageView = findViewById<ImageView>(R.id.imagenPeliculaCompleta)
        val overviewTextView = findViewById<TextView>(R.id.OverviewPelicula)

        clickedCard?.let {
            imageView.load(it.cardImages[0].imageUrl) {
                placeholder(R.drawable.film_placeholder)
            }
            titleTextView.text = clickedCard.name
            overviewTextView.text = clickedCard.description
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }
}