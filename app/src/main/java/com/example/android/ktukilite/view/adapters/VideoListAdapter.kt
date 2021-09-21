package com.example.android.ktukilite.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ktukilite.R
import com.example.android.ktukilite.model.schemas.VideoDetailItem
import com.squareup.picasso.Picasso

class VideoListAdapter(
    private val videoListItems: List<VideoDetailItem>
    ) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener
    private var selectedPos = 0

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setSelected(position: Int) {
        selectedPos = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.video_list_row_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.isSelected = selectedPos == position
        Picasso.get().load(videoListItems[position].thumbnailURL).resize(128, 96).centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return videoListItems.size
    }

    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.thumbnail)

        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }


}
