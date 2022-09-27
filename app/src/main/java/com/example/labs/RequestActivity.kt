package com.example.labs

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class RequestActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
        val textView = findViewById<TextView>(R.id.tv_animeQuote)
        val button = findViewById<TextView>(R.id.btn_load)

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://animechan.vercel.app/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val requestApi = retrofit.create(RequestApi::class.java)

        button.setOnClickListener {
            lifecycleScope.launch {
                while (isActive) {
                    try {
                        val quote = requestApi.randomQuote()
                        textView.text =
                            "Anime: " + quote.anime + "\n" + "Charter: " + quote.character + "\n" + "Quote: " + " ${quote.quote}"
                    } catch (e: Throwable) {
                        Toast.makeText(
                            this@RequestActivity,
                            e.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    delay(5000L)

                }
            }
        }
    }

    interface RequestApi {

        @GET("random")
        suspend fun randomQuote(): RandomQuote
    }

    data class RandomQuote(
        @SerializedName("anime")
        val anime: String,
        @SerializedName("character")
        val character: String,
        @SerializedName("quote")
        val quote: String
    )
}
