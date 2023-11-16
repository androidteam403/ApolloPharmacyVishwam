package com.apollopharmacy.vishwam.ui.home.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.NotificationLayoutBinding

class NotificationsAdapter(val mContext: Context) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val notificationLayoutBinding: NotificationLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.notification_layout,
            parent,
            false)
        return ViewHolder(notificationLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.notificationLayoutBinding.parentLayout.setBackgroundResource(R.drawable.gray_border_white_bg)
        } else {
            holder.notificationLayoutBinding.parentLayout.setBackgroundResource(R.drawable.notifications_light_gray_bg)
        }
    }

    override fun getItemCount(): Int {
        return 3;
    }

    class ViewHolder(val notificationLayoutBinding: NotificationLayoutBinding) :
        RecyclerView.ViewHolder(notificationLayoutBinding.root)
}