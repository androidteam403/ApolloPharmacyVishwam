package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApproveListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.adapter.ApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.util.Utlis

class ApproveListActivity : AppCompatActivity(), ApproveListcallback {
    var pendingAndApproved: PendingAndApproved? = null
    lateinit var activityApproveListBinding: ActivityApproveListBinding
    lateinit var approveListViewModel: ApproveListViewModel
    var approveListAdapter: ApproveListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApproveListBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_approve_list
        )

        approveListViewModel = ViewModelProvider(this)[ApproveListViewModel::class.java]
        setUp()
    }

    fun setUp() {

        pendingAndApproved =
            intent.getSerializableExtra("PENDING_AND_APPROVED") as PendingAndApproved
        when (pendingAndApproved != null) {
            true -> {
                activityApproveListBinding.model = pendingAndApproved
                approveListViewModel.getImageUrlsApiCall(pendingAndApproved!!)

            }
        }
        getImageUrlsApiResponse()
//        activityApproveListBinding.updateButtonApproveList.setOnClickListener {
//            val intent = Intent(ViswamApp.context, PreviewLastImageActivity::class.java)
//            startActivity(intent)
//        }
    }

    fun getImageUrlsApiResponse() {
        approveListViewModel.getImageUrlsResponse.observeForever { getImageUrlsResponse ->
            Utlis.hideLoading()
            when (getImageUrlsResponse != null && getImageUrlsResponse.categoryList != null && getImageUrlsResponse.categoryList.size > 0) {
                true -> {
                    approveListAdapter =
                        ApproveListAdapter(this, getImageUrlsResponse.categoryList, this)
                    activityApproveListBinding.categoryListRecycler.layoutManager =
                        LinearLayoutManager(
                            this
                        )
                    activityApproveListBinding.categoryListRecycler.adapter =
                        approveListAdapter


                }
            }
        }
    }
}