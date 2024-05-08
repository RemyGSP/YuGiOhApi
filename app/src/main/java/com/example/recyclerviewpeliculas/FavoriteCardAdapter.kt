import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewpeliculas.Card
import com.example.recyclerviewpeliculas.CardDataSource
import com.example.recyclerviewpeliculas.DetallesCarta
import com.example.recyclerviewpeliculas.R
import com.squareup.picasso.Picasso
import java.io.Serializable

class FavoriteCardAdapter(private val favoriteCards: List<Card>) :
    RecyclerView.Adapter<FavoriteCardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.cardImage2)
        var name: TextView = itemView.findViewById(R.id.cardName2)
        val removeButton: Button = itemView.findViewById(R.id.remove)
        init {
                removeButton.setOnClickListener {
                    var cardDataSource: CardDataSource
                    cardDataSource = CardDataSource(itemView.context)
                    cardDataSource.open()
                    val position = adapterPosition
                    cardDataSource.removeFavoriteCard(position)
                    }


            itemView.setOnClickListener {
                // Handle item click here
                val position = adapterPosition
                val clickedMovie = favoriteCards[position]

                val newScreen = Intent(itemView.context, DetallesCarta::class.java)
                newScreen.putExtra("clickedMovie", clickedMovie as Serializable)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = favoriteCards[position]
        holder.name.text = card.name
        Picasso.get().load(card.cardImages[0].imageUrl)
            .placeholder(R.drawable.film_placeholder)
            .into(holder.image)

        // Bind other views if needed
    }

    override fun getItemCount(): Int {
        return favoriteCards.size
    }
}