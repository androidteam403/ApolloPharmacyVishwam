package com.apollopharmacy.vishwam.ui.home.notification.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.NotificationLayoutBinding
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.notification.NotificationsActivityCallback
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(
    val mContext: Context,
    val rows: List<NotificationModelResponse.Data.ListData.Row>?,
    val notificationsActivityCallback: NotificationsActivityCallback,
) :
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
        var rowsList = rows!!.get(position)
        holder.notificationLayoutBinding.descriptionN.text=rowsList.description
        holder.notificationLayoutBinding.titleN.text=rowsList.title
//        if (position == 0) {
//            holder.notificationLayoutBinding.parentLayout.setBackgroundResource(R.drawable.gray_border_white_bg)
//        } else {
//            holder.notificationLayoutBinding.parentLayout.setBackgroundResource(R.drawable.notifications_light_gray_bg)
//        }

        holder.notificationLayoutBinding.parentLayout.setOnClickListener {
            notificationsActivityCallback.onClickParentLayout(rows!!.get(position))
        }

        val currentTime = Date()

        // Example: Get the previous time (replace this with your own logic)

        // Example: Get the previous time (replace this with your own logic)
        val previousTimeString = rowsList.createdTime
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val previousTime: Date
        try {
            previousTime = sdf.parse(previousTimeString)

            // Calculate the time difference in minutes
            var different = currentTime.getTime() - previousTime.getTime()
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different = different % daysInMilli
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli
            System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
            );
            if (elapsedDays > 0) {
                holder.notificationLayoutBinding.timeN.text =
                    "$elapsedDays days ago"
            } else if (elapsedHours > 0) {
                holder.notificationLayoutBinding.timeN.text = "$elapsedHours hours, $elapsedMinutes minutes ago"

            } else if (elapsedMinutes > 0) {
                holder.notificationLayoutBinding.timeN.text = "$elapsedMinutes minutes ago"
            } else {
                holder.notificationLayoutBinding.timeN.text = "$elapsedSeconds seconds ago"
            }

//            val timeDifferenceMinutes = timeDifferenceMillis / (60 * 1000)

            // Display the time difference to the user
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return rows!!.size;
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class ViewHolder(val notificationLayoutBinding: NotificationLayoutBinding) :
        RecyclerView.ViewHolder(notificationLayoutBinding.root)
}