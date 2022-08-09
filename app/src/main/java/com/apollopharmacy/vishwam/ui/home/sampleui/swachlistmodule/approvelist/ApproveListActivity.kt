package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityApproveListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.adapter.ApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage.PreviewLastImageActivity
import com.apollopharmacy.vishwam.util.Utlis

class ApproveListActivity : AppCompatActivity(), ApproveListcallback {
    private var pendingAndApproved: PendingAndApproved? = null
    private lateinit var activityApproveListBinding: ActivityApproveListBinding
    private lateinit var approveListViewModel: ApproveListViewModel
    private var approveListAdapter: ApproveListAdapter? = null
    private var getImageUrlsResponses = GetImageUrlsResponse()
    private var imageUrlsList = ArrayList<GetImageUrlsResponse.ImageUrl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApproveListBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_approve_list
        )

        approveListViewModel = ViewModelProvider(this)[ApproveListViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        pendingAndApproved =
            intent.getSerializableExtra("PENDING_AND_APPROVED") as PendingAndApproved
        when (pendingAndApproved != null) {
            true -> {
                activityApproveListBinding.model = pendingAndApproved
                approveListViewModel.getImageUrlsApiCall(pendingAndApproved!!)

            }
        }
        getImageUrlsApiResponse()
    }

    private fun getImageUrlsApiResponse() {
        approveListViewModel.getImageUrlsResponse.observeForever { getImageUrlsResponse ->
            Utlis.hideLoading()
            getImageUrlsResponses = getImageUrlsResponse
            when (getImageUrlsResponse != null && getImageUrlsResponse.categoryList != null && getImageUrlsResponse.categoryList!!.size > 0) {
                true -> {
                    approveListAdapter =
                        ApproveListAdapter(this, getImageUrlsResponses.categoryList!!, this)
                    activityApproveListBinding.categoryListRecycler.layoutManager =
                        LinearLayoutManager(
                            this
                        )
                    activityApproveListBinding.categoryListRecycler.adapter =
                        approveListAdapter
                    report()
                }
            }
        }
    }

    private fun report() {
        var pendingCount = 0
        var acceptedCount = 0
        var reShootCount = 0

        var isAccepted = true
        var isReShoot = true
        var isPending = true

        for (i in getImageUrlsResponses.categoryList!!) {
            for (j in i.imageUrls!!) {
                if (j.status.equals("0")) {
                    pendingCount++
                    isAccepted = false
                    isReShoot = false
                } else if (j.status.equals("1")) {
                    acceptedCount++
                    isReShoot = false
                    isPending = false
                } else if (j.status.equals("2")) {
                    reShootCount++
                    isAccepted = false
                    isPending = false
                }
            }
        }
        activityApproveListBinding.pendingCount = "" + pendingCount
        activityApproveListBinding.acceptedCount = "" + acceptedCount
        activityApproveListBinding.reShootCount = "" + reShootCount

        if (isPending) {
            activityApproveListBinding.status = "0"
        } else if (isAccepted) {
            activityApproveListBinding.status = "1"
        } else if (isReShoot) {
            activityApproveListBinding.status = "2"
        } else {
            activityApproveListBinding.status = "3"
        }
    }

    override fun onClickImage() {
        val intent = Intent(this, PreviewLastImageActivity::class.java)
        intent.putExtra("GET_IMAGE_URLS_RESPONSE", getImageUrlsResponses)
        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
        startActivityForResult(intent, PreviewLastImageActivity().PREVIEW_LAST_IMAGE_ACTIVITY)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSubmit() {
        val saveAcceptAndReshootRequest = SaveAcceptAndReshootRequest()
        saveAcceptAndReshootRequest.swachhid = pendingAndApproved!!.swachhid
        saveAcceptAndReshootRequest.storeid = Preferences.getSiteId()
        saveAcceptAndReshootRequest.statusid = "1"
        saveAcceptAndReshootRequest.reamrks = ""
        saveAcceptAndReshootRequest.rating = ""
        saveAcceptAndReshootRequest.userid = "APL49396"

        for (i in getImageUrlsResponses.categoryList!!) {
            for (j in i.imageUrls!!) {
                val imageUrl = SaveAcceptAndReshootRequest.Imageurl()
                imageUrl.imageid = j.imageid
                imageUrl.statusid = j.status
                imageUrl.remarks = ""
                saveAcceptAndReshootRequest.imageurls!!.add(imageUrl)
            }
        }

        approveListViewModel.saveAccepetAndReshoot(saveAcceptAndReshootRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PreviewLastImageActivity().PREVIEW_LAST_IMAGE_ACTIVITY) {
                imageUrlsList =
                    data?.getSerializableExtra("IMAGE_URLS_OBJECT") as ArrayList<GetImageUrlsResponse.ImageUrl>

                val categoryList = ArrayList<GetImageUrlsResponse.Category>()
                for (i in imageUrlsList) {
                    var isAlreadyExist = -1
                    if (categoryList.size > 0) {
                        for (j in categoryList) {
                            if (j.categoryid!!.equals(i.mainCategoryId)) {
                                isAlreadyExist = categoryList.indexOf(j)
                                break
                            }
                        }
                    }
                    if (isAlreadyExist != -1) {
                        categoryList.get(isAlreadyExist).imageUrls!!.add(i)
                    } else {
                        val category = GetImageUrlsResponse.Category()
                        category.categoryid = i.mainCategoryId
                        category.categoryname = i.categoryname
                        val imageUrlListss = ArrayList<GetImageUrlsResponse.ImageUrl>()
                        imageUrlListss.add(i)
                        category.imageUrls = imageUrlListss
                        categoryList.add(category)
                    }
                }
                getImageUrlsResponses.categoryList = categoryList

                approveListAdapter =
                    ApproveListAdapter(this, getImageUrlsResponses.categoryList!!, this)
                activityApproveListBinding.categoryListRecycler.layoutManager =
                    LinearLayoutManager(
                        this
                    )
                activityApproveListBinding.categoryListRecycler.adapter =
                    approveListAdapter
                report()
            }
        }
    }
}