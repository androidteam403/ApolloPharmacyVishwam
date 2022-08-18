package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityApproveListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.adapter.ApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage.PreviewLastImageActivity
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.apollopharmacy.vishwam.util.Utlis

class ApproveListActivity : AppCompatActivity(), ApproveListcallback {
    private var pendingAndApproved: PendingAndApproved? = null
    private lateinit var activityApproveListBinding: ActivityApproveListBinding
    private lateinit var approveListViewModel: ApproveListViewModel
    private var approveListAdapter: ApproveListAdapter? = null
    private var getImageUrlsResponses = GetImageUrlsResponse()
    private var imageUrlsList = ArrayList<GetImageUrlsResponse.ImageUrl>()
    private var overallStatus: String? = null
    var view: View? = null
    val APPROVE_LIST_ACTIVITY = 101
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
        activityApproveListBinding.callback = this
        pendingAndApproved =
            intent.getSerializableExtra("PENDING_AND_APPROVED") as PendingAndApproved
        when (pendingAndApproved != null) {
            true -> {
                activityApproveListBinding.model = pendingAndApproved
                Utlis.showLoading(this)
                approveListViewModel.getImageUrlsApiCall(pendingAndApproved!!)

            }
        }
        errorMessage()
        getImageUrlsApiResponse()
        saveAcceptandReshoot()
    }

    private fun getImageUrlsApiResponse() {
        approveListViewModel.getImageUrlsResponse.observeForever { getImageUrlsResponse ->
            Utlis.hideLoading()
            getImageUrlsResponses = getImageUrlsResponse
            when (getImageUrlsResponse != null && getImageUrlsResponse.categoryList != null && getImageUrlsResponse.categoryList!!.size > 0) {
                true -> {
//                   getImageUrlsResponses.categoryList?.get(0)?.imageUrls?.get(0)?.status="0"
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

    private fun saveAcceptandReshoot() {
        approveListViewModel.saveAcceptAndReshootResponse.observeForever { saveAcceptAndReshootResponse ->
            Utlis.hideLoading()
            when (saveAcceptAndReshootResponse != null) {
                true -> {
                    when (saveAcceptAndReshootResponse.status == true) {
                        true -> {
                            Toast.makeText(this, "Status has been updated", Toast.LENGTH_SHORT)
                                .show()
                            onBackPressed()
                        }
                    }
                }
            }
        }
    }

    private fun errorMessage() {
        approveListViewModel.errorMessage.observeForever { errorMessage ->
            Utlis.hideLoading()
            if (errorMessage != null)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()

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
            overallStatus = "0"
//            activityApproveListBinding.reshootButton.visibility = View.GONE
//            activityApproveListBinding.buttonForPending.visibility = View.VISIBLE

        } else if (isAccepted) {
            activityApproveListBinding.status = "1"
            overallStatus = "1"
        } else if (isReShoot) {
            activityApproveListBinding.status = "2"
            overallStatus = "2"
        } else {
            activityApproveListBinding.status = "3"
            overallStatus = "3"

        }
    }

    override fun onClickImage(position: Int, imagePath: String, viewClick: View) {


        PhotoPopupWindow(
            ViswamApp.context, R.layout.layout_image_fullview, viewClick,
            imagePath, null
        )

//        val intent = Intent(this, PreviewLastImageActivity::class.java)
//        intent.putExtra("GET_IMAGE_URLS_RESPONSE", getImageUrlsResponses)
//        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
//        startActivityForResult(intent, PreviewLastImageActivity().PREVIEW_LAST_IMAGE_ACTIVITY)
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//
    }

    override fun onClickSubmit() {
        val saveAcceptAndReshootRequest = SaveAcceptAndReshootRequest()
        saveAcceptAndReshootRequest.swachhid = pendingAndApproved!!.swachhid
        saveAcceptAndReshootRequest.storeid = "16001"// Preferences.getSiteId()
        saveAcceptAndReshootRequest.statusid = overallStatus
        saveAcceptAndReshootRequest.reamrks = activityApproveListBinding.comment.text.toString()
        saveAcceptAndReshootRequest.rating = ""
        saveAcceptAndReshootRequest.userid = "APL49396"
        val imageUrlsList = ArrayList<SaveAcceptAndReshootRequest.Imageurl>()
        for (i in getImageUrlsResponses.categoryList!!) {
            for (j in i.imageUrls!!) {
                val imageUrl = SaveAcceptAndReshootRequest.Imageurl()
                imageUrl.imageid = j.imageid
                imageUrl.statusid = j.status
                imageUrl.remarks = ""
                imageUrlsList.add(imageUrl)
            }
        }

        saveAcceptAndReshootRequest.imageurls = imageUrlsList
        Utlis.showLoading(this)
        approveListViewModel.saveAccepetAndReshoot(saveAcceptAndReshootRequest)
    }

    override fun onClickBack() {
        onBackPressed()
    }

    override fun onClickStartReiew() {
        val intent = Intent(this, PreviewLastImageActivity::class.java)
        intent.putExtra("GET_IMAGE_URLS_RESPONSE", getImageUrlsResponses)
        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
        startActivityForResult(intent, PreviewLastImageActivity().PREVIEW_LAST_IMAGE_ACTIVITY)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onePendingStatus() {
        activityApproveListBinding.reshootButton.visibility = View.GONE
        activityApproveListBinding.startReviewButton.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
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
                activityApproveListBinding.reshootButton.visibility=View.VISIBLE
                activityApproveListBinding.startReviewButton.visibility=View.GONE
            }
        }
    }
}