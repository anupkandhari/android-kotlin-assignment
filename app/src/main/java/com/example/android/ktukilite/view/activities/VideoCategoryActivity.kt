package com.example.android.ktukilite.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ktukilite.R
import com.example.android.ktukilite.model.*
import com.example.android.ktukilite.model.schemas.VideoDetailItem
import com.example.android.ktukilite.view.adapters.CategoryGridAdapter
import com.example.android.ktukilite.viewmodel.CustomViewModel
import java.util.*
import java.io.Serializable


class VideoCategoryActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_GRID_SPAN = 2
        const val VIDEO_DETAIL_DATA = "videos data"
    }
    private val categoriesAdapter = CategoryGridAdapter(arrayListOf())

    private lateinit var viewModel: CustomViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var loadingProgress: ProgressBar
        private var categorySets: Map<String, MutableSet<VideoDetailItem>> = mutableMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_category)

        viewModel = ViewModelProviders.of(this).get(CustomViewModel::class.java)



        categoryRecyclerView = findViewById(R.id.category_recycler_view)
        errorTextView = findViewById(R.id.error_textview)
        loadingProgress = findViewById(R.id.progress_bar)

        categoryRecyclerView.layoutManager =
            GridLayoutManager(
                this,
                CATEGORY_GRID_SPAN, GridLayoutManager.HORIZONTAL, false
            )
        categoryRecyclerView.adapter = categoriesAdapter
        categoriesAdapter.setOnItemClickListener(object : CategoryGridAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent =
                    Intent(this@VideoCategoryActivity, VideoDisplayActivity::class.java)
                val list = categorySets["Category${position + 1}"]?.toList()
                intent.putExtra(VIDEO_DETAIL_DATA, list as Serializable)
                startActivity(intent)
            }

        })

        viewModel.fetchData()
        observeViewModel()

    }

    private fun observeViewModel() {

        viewModel.dataLoadError.observe(this, {
            errorTextView.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.categories.observe(this, {
            categoriesAdapter.updateCategories(it)
            categoryRecyclerView.visibility = View.VISIBLE
        })

        viewModel.loading.observe(this, {
            loadingProgress.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                errorTextView.visibility = View.GONE
                categoryRecyclerView.visibility = View.GONE
            }
        })

        viewModel.videoList.observe(this, {
            categorySets = viewModel.getVideoListByCategory()
        })
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


}