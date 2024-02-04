package com.example.recyclerviewpeliculas

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.io.Serializable

class PeliculasAdapter(private val FilmList : List<Card>) : RecyclerView.Adapter<PeliculasAdapter.ViewHolder>() {

    var allCards: MutableList<Card> = FilmList.toMutableList()

    fun addData(newCards: List<Card>) {
        val startPosition = allCards.size
        allCards.addAll(newCards)
        notifyItemRangeInserted(startPosition, allCards.size)
        notifyDataSetChanged()

    }



    inner class ViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imagenPelicula)
        init {
            itemView.setOnClickListener {
                // Handle item click here
                val position = adapterPosition
                    val clickedMovie = allCards[position]

                    val newScreen = Intent(itemView.context, DetallesCarta::class.java)
                    newScreen.putExtra("clickedMovie", clickedMovie as Serializable)


                itemView.context.startActivity(newScreen)

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
        return allCards!!.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card: Card = allCards!!.get(position)
        Log.d("ImagenURL", card.cardImages[0].imageUrl);
        Picasso.get().load(card.cardImages[0].imageUrl)
            .placeholder(R.drawable.film_placeholder)
            .into(holder.image)

        Log.d("Position", position.toString());
    }


}