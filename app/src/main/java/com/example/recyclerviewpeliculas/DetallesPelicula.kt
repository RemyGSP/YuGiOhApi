package com.example.recyclerviewpeliculas

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.recyclerviewpeliculas.MovieAPI
import java.io.Serializable
import com.example.recyclerviewpeliculas.Card

@Suppress("DEPRECATION")
class DetallesPelicula : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalles_pelicula)
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            // Finish the current activity to go back to the previous one
            finish()
        }

    }

    override fun onStart() {
        super.onStart()

        val clickedMovie: Card? = intent?.extras?.getSerializable("clickedMovie") as? Card

        val titleTextView = findViewById<TextView>(R.id.tituloIntentCompleto)
        val imageView = findViewById<ImageView>(R.id.imagenPeliculaCompleta)
        val ratingTextView = findViewById<TextView>(R.id.rating)
        val overviewTextView = findViewById<TextView>(R.id.OverviewPelicula)

        clickedMovie?.let {


            imageView.load(it.imageUrl) {
                placeholder(R.drawable.film_placeholder)
            }
        }
    }
}