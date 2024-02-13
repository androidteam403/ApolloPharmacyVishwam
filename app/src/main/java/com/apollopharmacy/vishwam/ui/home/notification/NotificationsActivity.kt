package com.apollopharmacy.vishwam.ui.home.notification

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityNotificationsBinding
import com.apollopharmacy.vishwam.databinding.NotificationsNewAppUpdateDialogBinding
import com.apollopharmacy.vishwam.ui.home.notification.adapter.NotificationsAdapter
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.apollopharmacy.vishwam.ui.home.notification.webview.WebViewwActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import java.util.*


class NotificationsActivity : AppCompatActivity(), NotificationsActivityCallback {
    private lateinit var activityNotificationsBinding: ActivityNotificationsBinding
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var response: NotificationModelResponse? = null
    private var isFirstTime: Boolean = true
    private var pageNo = 1
    private var rowSize = 10
    private var isLoading = false
    private var isLastPage = false
    var surveyResponseList = ArrayList<NotificationModelResponse.Data.ListData.Row>()
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNotificationsBinding = DataBindingUtil.setContentView(
            this@NotificationsActivity,
            R.layout.activity_notifications
        )
        notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        activityNotificationsBinding.callback = this
        setup()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setup() {
        if (intent.getSerializableExtra("notificationResponse") != null)
            response =
                intent.getSerializableExtra("notificationResponse") as NotificationModelResponse
        callAPI(pageNo, rowSize)
        activityNotificationsBinding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClick()
        })
//        Toast.makeText(applicationContext, notificationModelResponse.data!!.listData!!.rows!!.size.toString(), Toast.LENGTH_SHORT).show()
//        if(response!=null && response!!.data!=null && response!!.data!!.listData!=null
//            && response!!.data!!.listData!!.rows!=null && response!!.data!!.listData!!.rows!!.size>0){
//            activityNotificationsBinding.emptyList.visibility=View.GONE
//            activityNotificationsBinding.notificationsRcv.visibility=View.VISIBLE
//            notificationsAdapter = NotificationsAdapter(this@NotificationsActivity, response!!.data!!.listData!!.rows!!.sortedByDescending { it.createdTime },this )
//            activityNotificationsBinding.notificationsRcv.layoutManager =
//                LinearLayoutManager(this@NotificationsActivity)
//            activityNotificationsBinding.notificationsRcv.adapter = notificationsAdapter
//        }else{
//            activityNotificationsBinding.emptyList.visibility=View.VISIBLE
//            activityNotificationsBinding.notificationsRcv.visibility=View.GONE
//        }
        val currentTime = Date()
        com.apollopharmacy.vishwam.data.Preferences.setNotificationTime(currentTime)
    }

    private fun submitClick() {
        if (!activityNotificationsBinding.pullToRefresh.isRefreshing) Utlis.showLoading(this)
        pageNo = 1
        callAPI(pageNo, rowSize)
    }

    @SuppressLint("ResourceAsColor")
    override fun onclickHelpIcon() {

    }


    private fun loadMore() {
        if (!isLoading) {
            isLoading = true
//            var res = NotificationModelResponse()
//            var data = res.Data()
//            var listData = data.ListData()
//            var rowssList = ArrayList<NotificationModelResponse.Data.ListData.Row>()
//            var rows = listData.Row()
//            rows.createdTime="2023-11-20 17:16:20"
//            rows.description="Lorem Ipsum is simply"
//            rows.title="SS"
//            rows.link="https://chat.openai.com/c/0fdc32c6-982f-4df1-975c-ffec8596377a"
//            rowssList.add(rows)
//            listData.rows= rowssList
//            data.listData=listData

            val newdata = NotificationModelResponse()
            var data = newdata.Data()
            var listData = data.ListData()
            var rows = listData.Row()
            rows.isLoading = "YES"
            surveyResponseList.add(rows)
//            adapter!!.getData().add(newdata)
            notificationsAdapter!!.notifyItemInserted(surveyResponseList.size - 1)
            callAPI(pageNo, rowSize)
        }
        /*handler.post(Runnable {
            if (!isLoading) {
                isLoading = true
                val newdata = SurveyListResponse.Row()
                newdata.isLoading = "YES"
                surveyResponseList.add(newdata)
//            adapter!!.getData().add(newdata)
                adapter!!.notifyItemInserted(surveyResponseList.size - 1)
                callAPI(pageNo, rowSize, false)
            }
        })*/

    }

    override fun onBackPressed() {
        var intent = Intent()
        setResult(RESULT_OK, intent);
        finish()
    }

    override fun onclickBackArrow() {
        super.onBackPressed()
    }

    fun callAPI(page: Int, rows: Int) {
        if (NetworkUtil.isNetworkConnected(this)) {
            isFirstTime = false
            if (!isLoading) showLoading(this)
            notificationsViewModel.getNotificationDetailsApi(this, page, rows, applicationContext)


        } else {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSuccessNotificationDetails(response: NotificationModelResponse?) {
        Utlis.hideLoading()
//        if(response!=null && response.data!=null && response.data!!.listData!=null
//            && response.data!!.listData!!.rows!=null && response.data!!.listData!!.rows!!.size>0){
        if (activityNotificationsBinding.pullToRefresh.isRefreshing) {
            activityNotificationsBinding.pullToRefresh.isRefreshing = false
            pageNo = 1
            isLastPage = false
        }

        if (response != null && response.data != null && response.data!!.listData != null && response.data!!.listData!!.rows != null && response.data!!.listData!!.rows!!.size > 0) {
            var getNotificationList = response!!.data!!.listData!!.rows
            activityNotificationsBinding.pullToRefresh.visibility = View.VISIBLE
            activityNotificationsBinding.notificationsRcv.visibility = View.VISIBLE
            activityNotificationsBinding.emptyList.visibility = View.GONE
            if (pageNo == 1) {
                isLoading = false
                isLastPage = false
                surveyResponseList.clear()
                surveyResponseList.addAll(getNotificationList!!)
                layoutManager = LinearLayoutManager(applicationContext)
                activityNotificationsBinding.notificationsRcv!!.removeAllViews()
                activityNotificationsBinding.notificationsRcv.layoutManager = layoutManager
                initAdapter()
                pageNo++
                addScrollerListener()
            } else {
                if (isLoading) {
                    isLastPage = false
                    isLoading = false
                    val pos = surveyResponseList.size - 1
                    surveyResponseList.removeAt(surveyResponseList.size - 1)
                    surveyResponseList.addAll(getNotificationList!!)
                    pageNo++
//                    adapter!!.notifyDataSetChanged()
//                    viewBinding.recyclerViewApproved.smoothScrollToPosition(pos)
                    initAdapter()
                    activityNotificationsBinding.notificationsRcv.smoothScrollToPosition(pos)
//                    addScrollerListener()
                }
            }


        } else {
            isLastPage = true
            if (isLoading) {
                isLoading = false
            }
            if (pageNo == 1) {
                activityNotificationsBinding.pullToRefresh.visibility = View.GONE
                activityNotificationsBinding.notificationsRcv.visibility = View.GONE
                activityNotificationsBinding.emptyList.visibility = View.VISIBLE
            } else {
                val pos = surveyResponseList.size - 1
                surveyResponseList.removeAt(surveyResponseList.size - 1)
                initAdapter()
                activityNotificationsBinding.notificationsRcv.smoothScrollToPosition(pos)

            }

        }
//            activityNotificationsBinding.emptyList.visibility=View.GONE
//            activityNotificationsBinding.notificationsRcv.visibility=View.VISIBLE
//            notificationsAdapter = NotificationsAdapter(this@NotificationsActivity, response.data!!.listData!!.rows!!.sortedByDescending { it.createdTime },this )
//            activityNotificationsBinding.notificationsRcv.layoutManager =
//                LinearLayoutManager(this@NotificationsActivity)
//            activityNotificationsBinding.notificationsRcv.adapter = notificationsAdapter
//        }
//        else{
//            activityNotificationsBinding.emptyList.visibility=View.VISIBLE
//            activityNotificationsBinding.notificationsRcv.visibility=View.GONE
//        }

    }

    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        activityNotificationsBinding.notificationsRcv.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isFirstTime && !isLastPage) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.

                    if (layoutManager.findLastCompletelyVisibleItemPosition() == surveyResponseList.size!! - 1) {//adapter!!.getData()?
                        if (surveyResponseList.size >= rowSize)
                            loadMore()
                    }

                }
            }
        })
    }

    private fun initAdapter() {
        notificationsAdapter = NotificationsAdapter(
            applicationContext,
            surveyResponseList,
            this,
        )
        activityNotificationsBinding.notificationsRcv.adapter = notificationsAdapter
//        notificationsAdapter = NotificationsAdapter(this@NotificationsActivity, surveyResponseList,this )
//            activityNotificationsBinding.notificationsRcv.layoutManager =
//                LinearLayoutManager(this@NotificationsActivity)
//            activityNotificationsBinding.notificationsRcv.adapter = notificationsAdapter
    }

    override fun onFailureNotificationDetails(saveUpdateRequestJsonResponse: NotificationModelResponse?) {

    }

    @SuppressLint("ResourceAsColor")
    override fun onClickParentLayout(
        response: NotificationModelResponse.Data.ListData.Row,
        date: String,
    ) {
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
        notificationsNewAppUpdateDialogBinding.title.text = response.title
        notificationsNewAppUpdateDialogBinding.dateAndTimeN.text = date
        notificationsNewAppUpdateDialogBinding.description.text = response.description
//        notificationsNewAppUpdateDialogBinding.linkN.setText("More details:"+ response.link);
        if (!response.link.isNullOrEmpty()) {
            notificationsNewAppUpdateDialogBinding.linkN.visibility = View.VISIBLE
        } else {
            notificationsNewAppUpdateDialogBinding.linkN.visibility = View.GONE
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

    override fun onFailureUat() {
        hideLoading()
    }
}