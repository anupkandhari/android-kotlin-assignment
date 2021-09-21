package com.example.android.ktukilite.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ktukilite.R
import com.example.android.ktukilite.model.schemas.CategoryItem
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class CategoryGridAdapter(private val categoryItems: ArrayList<CategoryItem>) :
    RecyclerView.Adapter<CategoryGridAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateCategories(categories: List<CategoryItem>) {
        categoryItems.clear()
        categoryItems.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_row_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(categoryItems[position].image).resize(96, 96)
            .transform(CropCircleTransformation()).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return categoryItems.size
    }

    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView

        init {
            imageView = view.findViewById(R.id.category)
            imageView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }


}
