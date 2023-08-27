package com.example.retrofitapicalling

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    // Third Step - 3
    // using GET from retrofit get the response of the API url after the base url
    // Then create a fun for calling the model class data which we have to create using Json to Kotlin Converter

    @GET("gimme")
    fun getData(): Call<responseDataFromAPI>
}