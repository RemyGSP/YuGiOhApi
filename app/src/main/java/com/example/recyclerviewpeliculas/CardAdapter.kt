package com.example.recyclerviewpeliculas

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.io.Serializable

class CardAdapter(private val FilmList : List<Card>,private val cardDataSource: CardDataSource) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    var allCards: MutableList<Card> = FilmList.toMutableList()
    var filteredCards: MutableList<Card> = allCards;
    fun addData(newCards: List<Card>) {
        val startPosition = allCards.size
        allCards.addAll(newCards)
        notifyItemRangeInserted(startPosition, allCards.size)
        notifyDataSetChanged()

    }

    fun filterByQuery(query: String?) {
        if (query.isNullOrEmpty()) {
            filteredCards = allCards.toMutableList() // Reset to full list if no query
        } else {
            filteredCards = allCards.toMutableList().filter { card ->
                card.name.contains(query, ignoreCase = true) // Case-insensitive search
            }.toMutableList()
        }

        notifyDataSetChanged() // Update the RecyclerView with filtered data
    }


    inner class ViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.cardImage)
        var name: TextView = itemView.findViewById(R.id.cardName)
        var favoriteButton: Button = itemView.findViewById(R.id.favourites)
        init {
            itemView.setOnClickListener {
                // Handle item click here
                val position = adapterPosition
                val clickedMovie = filteredCards[position]

                val newScreen = Intent(itemView.context, DetallesCarta::class.java)
                newScreen.putExtra("clickedMovie", clickedMovie as Serializable)


            }
            favoriteButton.setOnClickListener {
                val position = adapterPosition
                val clickedCard = filteredCards[position]
                val cardIndex = allCards.indexOf(clickedCard)
                // Store the index of the clicked card in the database
                cardDataSource.addFavoriteCard(cardIndex)
                // Optionally, you can provide feedback to the user that the card was added to favorites
                Toast.makeText(itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show()


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context);
        val filmView = inflater.inflate(R.layout.layoutrecyclerview,parent,false);
        return ViewHolder(filmView)
    }

    override fun getItemCount(): Int {
        return filteredCards!!.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card: Card = filteredCards!!.get(position)
        holder.name.text = card.name;
        Picasso.get().load(card.cardImages[0].imageUrl)
            .placeholder(R.drawable.film_placeholder)
            .into(holder.image)

    }


}