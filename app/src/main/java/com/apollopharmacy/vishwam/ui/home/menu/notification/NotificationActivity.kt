package com.apollopharmacy.vishwam.ui.home.menu.notification

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityNotificationBinding
import com.apollopharmacy.vishwam.databinding.ViewNotificationItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.menu.notificationwebview.NotificationWebViewActivity
import com.apollopharmacy.vishwam.util.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout

class NotificationActivity : AppCompatActivity(), NotificationClickListener {
    lateinit var viewModel: NotificationViewModel
    lateinit var notificationBinding: ActivityNotificationBinding
    val TAG = "NotificationActivity"
    var notificationArrList = arrayListOf<NotificationItem>()

    lateinit var adapter: NotificationRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        loadNotifications()

        notificationBinding.recyclerViewApproved.visibility = View.VISIBLE
        adapter = NotificationRecyclerView(notificationArrList, this)
        notificationBinding.recyclerViewApproved.adapter = adapter
        adapter.notifyAdapter(notificationArrList)

        notificationBinding.notificationBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadNotifications() {
        var item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "09-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "10-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "11-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "12-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "13-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "14-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "15-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "16-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "17-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "18-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "19-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "20-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "21-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "22-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "23-10-2021 13:15:10"
        item.webUrl = "https://www.apollopharmacy.in/"
        notificationArrList.add(item)

        item = NotificationItem()
        item.header = "Offer"
        item.body = "October month Offers are with 25%"
        item.time = "24-10-2021 18:30:10"
        item.webUrl = ""
        notificationArrList.add(item)
    }


    class NotificationRecyclerView(
        var notificationData: ArrayList<NotificationItem>,
        var notificationClickListener: NotificationClickListener,
    ) :
        SimpleRecyclerView<ViewNotificationItemBinding, NotificationItem>(
            notificationData,
            R.layout.view_notification_item
        ) {

        override fun bindItems(
            binding: ViewNotificationItemBinding,
            items: NotificationItem,
            position: Int,
        ) {
            binding.notiHeaderLabel.setText(items.header)
            binding.notiDescLabel.setText(items.body)
            binding.notiDate.setText(Utils.getNotificationDate(items.time))
            binding.notiTime.setText(Utils.getNotificationTime(items.time))
            binding.parentLayout.setOnClickListener {
                notificationClickListener.onItemClick(position, items.webUrl)
            }
        }

        fun notifyAdapter(notifList: ArrayList<NotificationItem>) {
            this.notificationData = notifList
            notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    override fun onItemClick(position: Int, webUrl: String) {
        if (webUrl.isNotEmpty()) {
            val homeIntent = Intent(this, NotificationWebViewActivity::class.java)
            homeIntent.putExtra("WebURL", webUrl)
            startActivity(homeIntent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        } else {
            val snackbar =
                Snackbar.make(notificationBinding.parentLayout, "", Snackbar.LENGTH_SHORT)
            val customSnackView: View = layoutInflater.inflate(R.layout.custom_snackbar, null)
            snackbar.view.setBackgroundColor(Color.TRANSPARENT)
            val snackBarText: TextView = customSnackView.findViewById(R.id.snackBarText)
            snackBarText.text = "Unable to Load Website"
            val snackbarLayout = snackbar.view as SnackbarLayout
            snackbarLayout.setPadding(0, 0, 0, 0)
            snackbarLayout.addView(customSnackView, 0)
            snackbar.show()
        }
    }
}

interface NotificationClickListener {
    fun onItemClick(position: Int, webUrl: String)
}