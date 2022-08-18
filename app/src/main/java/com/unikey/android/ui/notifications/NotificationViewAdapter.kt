package com.unikey.android.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.NOT_VIEWED
import com.unikey.android.databinding.NotificationItemViewBinding
import com.unikey.android.objects.NotificationObj
import com.unikey.android.setHostSharedAxisAnim

class NotificationViewAdapter(private val fragment: NotificationsFragment)
    : ListAdapter<NotificationObj, NotificationViewAdapter.NotifyViewHolder>(DiffCallBack){

    inner class NotifyViewHolder(private val item: NotificationItemViewBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: NotificationObj) {
            item.apply {
                title.text = data.title
                description.text = data.description

                root.setOnClickListener {
                    fragment.setHostSharedAxisAnim(MaterialSharedAxis.Z)
//                    TODO: Make current notification viewed = true
                    fragment.findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToNotificationContentFragment2(data))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        val viewHolder = NotificationItemViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifyViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallBack : DiffUtil.ItemCallback<NotificationObj> =
            object : DiffUtil.ItemCallback<NotificationObj>(){
                override fun areItemsTheSame(
                    oldItem: NotificationObj,
                    newItem: NotificationObj
                ): Boolean {
                    return oldItem.time == newItem.time
                }

                override fun areContentsTheSame(
                    oldItem: NotificationObj,
                    newItem: NotificationObj
                ): Boolean {
                    return oldItem.description == newItem.description
                }

            }
    }

}