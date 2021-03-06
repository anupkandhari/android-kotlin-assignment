package com.example.android.ktukilite.view.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ktukilite.R
import com.example.android.ktukilite.model.schemas.VideoDetailItem
import com.example.android.ktukilite.view.adapters.VideoListAdapter
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class VideoDisplayActivity : AppCompatActivity(), VideoListAdapter.OnItemClickListener,
    Player.Listener {

    private lateinit var videoListRecyclerView: RecyclerView
    private lateinit var videoListAdapter: VideoListAdapter
    private lateinit var videoView: PlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var backButton: Button
    private lateinit var fullScreenButton: ImageView
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0
    private lateinit var videoItemList: List<VideoDetailItem>
    private var selectedVideoPos: Int = 0
    private var fullscreen = false

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_display)
        videoView = findViewById(R.id.ep_video_view)
        progressBar = findViewById(R.id.progress_bar)
        fullScreenButton = videoView.findViewById(R.id.exo_fullscreen_icon)
        setUpFullScreenButton()
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener { finish() }


        videoItemList =
            intent.getSerializableExtra(VideoCategoryActivity.VIDEO_DETAIL_DATA) as List<VideoDetailItem>
        videoListRecyclerView = findViewById(R.id.thumbnail_recycler_view)

        videoListRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        videoListAdapter = VideoListAdapter(videoItemList)
        videoListRecyclerView.adapter = videoListAdapter
        videoListAdapter.setOnItemClickListener(this)


    }

    private fun setUpFullScreenButton() {
        fullScreenButton.setOnClickListener {
            if (fullscreen) {

                fullScreenButton.setBackgroundResource(R.drawable.ic_baseline_fullscreen_open)

                val params = videoView.layoutParams as RelativeLayout.LayoutParams

                params.width =
                    applicationContext.resources.getDimension(R.dimen.video_player_width).toInt()
                params.height =
                    applicationContext.resources.getDimension(R.dimen.video_player_height).toInt()
                params.topMargin =
                    applicationContext.resources.getDimension(R.dimen.dimen_16).toInt()
                videoView.layoutParams = params
                fullscreen = false
                videoListRecyclerView.visibility = View.VISIBLE
                backButton.visibility = View.VISIBLE

            } else {
                fullScreenButton.setBackgroundResource(R.drawable.ic_baseline_fullscreen_exit)

                val params = videoView.layoutParams as RelativeLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                params.topMargin = 0
                videoView.layoutParams = params
                fullscreen = true
                videoListRecyclerView.visibility = View.GONE
                backButton.visibility = View.GONE


            }
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

    override fun onStart() {
        super.onStart()
        initializePlayer()
        preparePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }


    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        videoView.player = simpleExoplayer
        simpleExoplayer.addListener(this)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer() {
        val videoUrl = videoItemList[selectedVideoPos].videoURL
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri)
        simpleExoplayer.prepare(mediaSource)
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
    }

    private fun releasePlayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }

    override fun onPlayerError(error: PlaybackException) {
        Toast.makeText(this, R.string.audio_playback_error, Toast.LENGTH_SHORT).show()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            progressBar.visibility = View.INVISIBLE
    }


    override fun onItemClick(position: Int) {
        if (position == selectedVideoPos)
            return
        videoListAdapter.setSelected(position)
        videoListAdapter.notifyItemChanged(selectedVideoPos)
        selectedVideoPos = position
        videoListAdapter.notifyItemChanged(selectedVideoPos)
        preparePlayer()

    }
}