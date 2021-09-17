package com.example.android.ktukilite.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ktukilite.R
import com.example.android.ktukilite.data.DataRetriever
import com.example.android.ktukilite.model.*
import com.example.android.ktukilite.ui.adapters.CategoryGridAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable


class VideoCategoryActivity : AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categorySets: MutableMap<String, MutableSet<VideoDetailItem>>
    private lateinit var categoryList: CategoryResult

    private val dataRetriever = DataRetriever()

    private val categoryCallback = object : Callback<CategoryServerResponse> {
        override fun onFailure(call: Call<CategoryServerResponse>?, t: Throwable?) {
            Log.e("VideoCategoryActivity", "Problem fetching categories {${t?.message}}")
        }

        override fun onResponse(
            call: Call<CategoryServerResponse>?,
            response: Response<CategoryServerResponse>?
        ) {
            response?.isSuccessful.let {
                dataRetriever.retrieveVideoDetails(videoDetailCallback)
                categoryList = CategoryResult(
                    (response?.body()?.response?.videoCategory?.values?.toList()
                        ?: emptyList())
                )
                val adapter = CategoryGridAdapter(categoryList)
                categoryRecyclerView.adapter = adapter
                adapter.setOnItemClickListener(object : CategoryGridAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        //Toast.makeText(this@VideoCategoryActivity,
                        //  "clicked onitem no. ${categorySets["Category${position+1}"].toString()}", Toast.LENGTH_SHORT ).show()
                        val intent =
                            Intent(this@VideoCategoryActivity, VideoDisplayActivity::class.java)
                        val list = categorySets["Category${position + 1}"]?.toList()
                        intent.putExtra("data", list as Serializable)
                        startActivity(intent)
                    }

                })
            }
        }
    }

    private val videoDetailCallback = object : Callback<VideoDetailServerResponse> {
        override fun onFailure(call: Call<VideoDetailServerResponse>?, t: Throwable?) {
            Log.e("VideoCategoryActivity", "Problem fetching videos {${t?.message}}")
        }

        override fun onResponse(
            call: Call<VideoDetailServerResponse>?,
            response: Response<VideoDetailServerResponse>?
        ) {
            response?.isSuccessful.let {
                val videoDetailList = VideoDetailResult(
                    (response?.body()?.response?.videoDetailList?.values?.toList()
                        ?: emptyList())
                )
                processVideoDetailList(videoDetailList)
            }
        }
    }

    private fun processVideoDetailList(videoDetailList: VideoDetailResult) {
        categorySets = mutableMapOf()
        for (item in categoryList.items)
            categorySets[item.name!!] = mutableSetOf()
        for (item in videoDetailList.items) {
            var list: List<String> = item.categories?.split(",")?.toList() ?: emptyList()
            for (category in list)
                categorySets[category]?.add(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_category)
        categoryRecyclerView = findViewById<RecyclerView>(R.id.category_recycler_view)

        categoryRecyclerView.layoutManager =
            GridLayoutManager(applicationContext, 2, LinearLayoutManager.HORIZONTAL, false)


        if (isNetworkConnected()) {
            dataRetriever.retrieveCategories(categoryCallback)

        } else {
            AlertDialog.Builder(this).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again!")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }


    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}