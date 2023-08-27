package com.example.retrofitapicalling

data class responseDataFromAPI(

    // This is created using Json to Kotlin Data Class plugin
    // Directly paste the response from API link into the Json to Kotlin Class converter and get model Class

    val author: String,
    val nsfw: Boolean,
    val postLink: String,
    val preview: List<String>,
    val spoiler: Boolean,
    val subreddit: String,
    val title: String,
    val ups: Int,
    val url: String
)