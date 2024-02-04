package com.example.recyclerviewpeliculas

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.awaitResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class CardAPI {


    suspend fun makeApiCall(page: Int): List<Card>? = withContext(Dispatchers.IO) {
        try {
            val call: Call<CardList> = RetrofitInstance.cardApiService.getCardDetails()
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                val cardList: CardList? = response.body()
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
    @SerializedName("data")
    val cards: List<Card>,
)
data class Card(
    @SerializedName("name")
    val name: String,
    @SerializedName("desc")
    val description: String,
    @SerializedName("card_images")
    val cardImages: List<CardImage>,
) :Serializable


data class CardImage(
    @SerializedName("image_url")
    val imageUrl: String,
) :Serializable



object RetrofitInstance {
    private const val APIURL = "https://db.ygoprodeck.com/api/v7/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(APIURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cardApiService: ApiService = retrofit.create(ApiService::class.java)
}