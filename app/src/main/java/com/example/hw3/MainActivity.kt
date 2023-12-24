package com.example.hw3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    private val apiKey = "pub_35353d9de0ae0710dee4a7a49d56775b6d62c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = NewsAdapter()
        recyclerView = findViewById(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton = findViewById(R.id.searchButton)
        searchEditText = findViewById(R.id.searchEditText)

        searchButton.setOnClickListener {
            val searchQuery = searchEditText.text.toString()
            if (searchQuery.isNotEmpty()) {
                fetchNews(searchQuery)
            }
        }
    }

    private fun fetchNews(searchQuery: String) {
        val apiUrl = "https://newsdata.io/api/1/news?apikey=$apiKey&q=$searchQuery&language=en"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            apiUrl,
            { response -> parseNews(response) },
            { error -> Log.d("MyLog", "Error: $error") }
        )
        requestQueue.add(stringRequest)
    }

    private fun parseNews(response: String) {
        val newsList = ArrayList<News>()
        val jsonResponse = JSONObject(response)
        val resultsArray = jsonResponse.getJSONArray("results")

        for (i in 0 until resultsArray.length()) {
            val newsObject = resultsArray[i] as JSONObject
            val newsItem = News(
                newsObject.getString("title"),
                newsObject.getString("link")
            )
            newsList.add(newsItem)
        }

        adapter.setAllItems(newsList)
    }
}