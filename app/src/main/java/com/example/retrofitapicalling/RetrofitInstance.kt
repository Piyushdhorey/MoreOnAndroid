package com.example.retrofitapicalling

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // First Step - 1
// Creating the retrofit builder by lazy and including the base url of API response,
    // also converting it to Gson for easy access in android
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://meme-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // Second step - 2
    // creating variable for getting the data via interface of API created earlier

    val apiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}