package com.unikey.android.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unikey.android.databinding.GalleryItemBinding
import com.unikey.android.objects.GalleryImage

class GalleryAdapter(val context: Fragment) : ListAdapter<GalleryImage, GalleryImgViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImgViewHolder {
        val item = GalleryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryImgViewHolder(item, context = context)
    }

    override fun onBindViewHolder(holder: GalleryImgViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val DiffCallBack = object : DiffUtil.ItemCallback<GalleryImage>(){
            override fun areItemsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
                return oldItem == newItem
            }
        }
    }
}