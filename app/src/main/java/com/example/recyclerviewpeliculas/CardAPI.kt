package com.example.recyclerviewpeliculas

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.awaitResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class MovieAPI {


    suspend fun makeApiCall(page: Int): List<Card>? = withContext(Dispatchers.IO) {
        try {
            // Update the URL to include the page parameter
            val call: Call<CardList> = RetrofitInstance.cardApiService.getCardDetails()
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                val cardList: CardList? = response.body()
                Log.d("ImamgeURL", cardList.toString());
                return@withContext cardList?.cards;
            } else {
                // Handle unsuccessful response
                Log.e("Hola", "Unsuccessful response: ${response.code()}")
            }
        } catch (e: Exception) {
            // Handle exception
            Log.e("Hola", "Error during API call: ${e.message}", e)
        }
        return@withContext null
    }
}

interface ApiService {
    @GET("cardinfo.php")
    fun getCardDetails(): Call<CardList>
}

data class CardList(
    @SerializedName("results")
    val cards: List<Card>,
)
data class Card(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("desc")
    val description: String,
    @SerializedName("race")
    val race: String,
    @SerializedName("archetype")
    val archetype: String,
    @SerializedName("card_images")
    val imageUrl: String,

) : Serializable


object RetrofitInstance {
    private const val APIURL = "https://db.ygoprodeck.com/api/v7/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(APIURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cardApiService: ApiService = retrofit.create(ApiService::class.java)
}