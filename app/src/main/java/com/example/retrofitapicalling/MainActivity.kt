package com.example.retrofitapicalling

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.retrofitapicalling.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

//    https://meme-api.com/gimme

    // Fourth step - 4

    // Create binding for the getting all the id's from xml files
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fifth step - 5

        getData() // Call the getData function for getting response as data from API

        binding.btnNewMeme.setOnClickListener {
            getData()
        }
    }

    private fun getData() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait while data is being fetched")
        progressDialog.show()

        // Sixth step - 6

        // Using RetrofitInstance get all the data using enqueue using Callback from model class
        // Implement member functions (directly gets created)
        // That's how you use Retrofit for getting API data

        RetrofitInstance.apiInterface.getData().enqueue(object : Callback<responseDataFromAPI?> {
            override fun onResponse(
                call: Call<responseDataFromAPI?>,
                response: Response<responseDataFromAPI?>
            ) {

                binding.memeTitle.text = response.body()?.title
                binding.memeAuthor.text = response.body()?.author
                Glide.with(this@MainActivity).load(response.body()?.url).into(binding.memeImage)
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<responseDataFromAPI?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }

        })
    }
}