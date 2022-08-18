package com.unikey.android.ui.gallery

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unikey.android.R
import com.unikey.android.databinding.GalleryItemBinding
import com.unikey.android.objects.GalleryImage
import java.net.URL

class GalleryImgViewHolder(private val binding: GalleryItemBinding, private val context: Fragment) : RecyclerView.ViewHolder(binding.root) {
    fun bind(imgData: GalleryImage){
        binding.dateItem.text = imgData.date

        Glide.with(context)
            .load(imgData.imgURL)
            .centerCrop()
            .placeholder(R.drawable.ic_image_24)
            .error(R.drawable.ic_broken_image_24)
            .into(binding.galleryImg)
    }
}