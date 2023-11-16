package com.apollopharmacy.vishwam.ui.home.notification

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityNotificationsBinding
import com.apollopharmacy.vishwam.databinding.NotificationsNewAppUpdateDialogBinding
import com.apollopharmacy.vishwam.ui.home.notification.adapter.NotificationsAdapter

class NotificationsActivity : AppCompatActivity(), NotificationsActivityCallback {
    private lateinit var activityNotificationsBinding: ActivityNotificationsBinding
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var notificationsAdapter: NotificationsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNotificationsBinding = DataBindingUtil.setContentView(this@NotificationsActivity,
            R.layout.activity_notifications)
        notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        activityNotificationsBinding.callback = this
        setup()
    }

    private fun setup() {
        notificationsAdapter = NotificationsAdapter(this@NotificationsActivity)
        activityNotificationsBinding.notificationsRcv.layoutManager =
            LinearLayoutManager(this@NotificationsActivity)
        activityNotificationsBinding.notificationsRcv.adapter = notificationsAdapter
    }

    @SuppressLint("ResourceAsColor")
    override fun onclickHelpIcon() {
        val dialog = Dialog(this)
        val notificationsNewAppUpdateDialogBinding: NotificationsNewAppUpdateDialogBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.notifications_new_app_update_dialog,
                null,
                false
            )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(notificationsNewAppUpdateDialogBinding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
        notificationsNewAppUpdateDialogBinding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onclickBackArrow() {
        super.onBackPressed()
    }
}