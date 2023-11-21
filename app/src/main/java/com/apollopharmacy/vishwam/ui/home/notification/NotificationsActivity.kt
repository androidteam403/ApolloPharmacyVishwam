package com.apollopharmacy.vishwam.ui.home.notification

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityNotificationsBinding
import com.apollopharmacy.vishwam.databinding.NotificationsNewAppUpdateDialogBinding
import com.apollopharmacy.vishwam.ui.home.notification.adapter.NotificationsAdapter
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.apollopharmacy.vishwam.ui.home.notification.webview.WebViewwActivity
import java.util.*


class NotificationsActivity : AppCompatActivity(), NotificationsActivityCallback {
    private lateinit var activityNotificationsBinding: ActivityNotificationsBinding
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var response: NotificationModelResponse?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNotificationsBinding = DataBindingUtil.setContentView(this@NotificationsActivity,
            R.layout.activity_notifications)
        notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        activityNotificationsBinding.callback = this
        setup()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setup() {
        if(intent.getSerializableExtra("notificationResponse")!=null)
        response = intent.getSerializableExtra("notificationResponse") as NotificationModelResponse
//        Toast.makeText(applicationContext, notificationModelResponse.data!!.listData!!.rows!!.size.toString(), Toast.LENGTH_SHORT).show()
        if(response!=null && response!!.data!=null && response!!.data!!.listData!=null
            && response!!.data!!.listData!!.rows!=null && response!!.data!!.listData!!.rows!!.size>0){
            activityNotificationsBinding.emptyList.visibility=View.GONE
            activityNotificationsBinding.notificationsRcv.visibility=View.VISIBLE
            notificationsAdapter = NotificationsAdapter(this@NotificationsActivity, response!!.data!!.listData!!.rows!!.sortedByDescending { it.createdTime },this )
            activityNotificationsBinding.notificationsRcv.layoutManager =
                LinearLayoutManager(this@NotificationsActivity)
            activityNotificationsBinding.notificationsRcv.adapter = notificationsAdapter
        }else{
            activityNotificationsBinding.emptyList.visibility=View.VISIBLE
            activityNotificationsBinding.notificationsRcv.visibility=View.GONE
        }
        val currentTime = Date()
        com.apollopharmacy.vishwam.data.Preferences.setNotificationTime(currentTime)
    }

    @SuppressLint("ResourceAsColor")
    override fun onclickHelpIcon() {

    }

    override fun onBackPressed() {
       var intent =  Intent()
        setResult(RESULT_OK, intent);
        finish()
    }

    override fun onclickBackArrow() {
        super.onBackPressed()
    }

    override fun onSuccessNotificationDetails(response: NotificationModelResponse?) {
        if(response!=null && response.data!=null && response.data!!.listData!=null
            && response.data!!.listData!!.rows!=null && response.data!!.listData!!.rows!!.size>0){
            activityNotificationsBinding.emptyList.visibility=View.GONE
            activityNotificationsBinding.notificationsRcv.visibility=View.VISIBLE
            notificationsAdapter = NotificationsAdapter(this@NotificationsActivity, response.data!!.listData!!.rows!!.sortedByDescending { it.createdTime },this )
            activityNotificationsBinding.notificationsRcv.layoutManager =
                LinearLayoutManager(this@NotificationsActivity)
            activityNotificationsBinding.notificationsRcv.adapter = notificationsAdapter
        }else{
            activityNotificationsBinding.emptyList.visibility=View.VISIBLE
            activityNotificationsBinding.notificationsRcv.visibility=View.GONE
        }

    }

    override fun onFailureNotificationDetails(saveUpdateRequestJsonResponse: NotificationModelResponse?) {

    }

    @SuppressLint("ResourceAsColor")
    override fun onClickParentLayout(response: NotificationModelResponse.Data.ListData.Row) {
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
        notificationsNewAppUpdateDialogBinding.title.text=response.title
        notificationsNewAppUpdateDialogBinding.description.text =  response.description
//        notificationsNewAppUpdateDialogBinding.linkN.setText("More details:"+ response.link);
        if(!response.link.isNullOrEmpty()){
            notificationsNewAppUpdateDialogBinding.linkN.visibility= View.VISIBLE
        }else{
            notificationsNewAppUpdateDialogBinding.linkN.visibility= View.GONE
        }
        notificationsNewAppUpdateDialogBinding.linkN.setOnClickListener {
            val url = response.link
            val intent = Intent(this, WebViewwActivity::class.java)
            intent.putExtra("response", response)
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        notificationsNewAppUpdateDialogBinding.linkN.setMovementMethod(LinkMovementMethod.getInstance());
        notificationsNewAppUpdateDialogBinding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }
}