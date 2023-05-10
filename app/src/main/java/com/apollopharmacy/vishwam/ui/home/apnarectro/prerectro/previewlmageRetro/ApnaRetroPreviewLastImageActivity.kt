package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityPreviewLastImageBinding
import com.apollopharmacy.vishwam.databinding.ActivityPreviewRetroLastImageBinding
import com.apollopharmacy.vishwam.databinding.DialogLastimagePreviewAlertBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter.RetroPreviewImage
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.previewlastimage.adapter.PreviewImageViewPager
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.util.Utlis

class ApnaRetroPreviewLastImageActivity : AppCompatActivity(), PreviewLastImageCallback,
    ViewPager.OnPageChangeListener {
    lateinit var activityPreviewImageBinding: ActivityPreviewRetroLastImageBinding
    private var getImageUrlsResponse: GetImageUrlResponse? = null
    private var previewImageAdapter: RetroPreviewImage? = null
    private lateinit var previewLastImageViewModel: ApnaPreviewLastImageViewModel
    private var isApiHit: Boolean = false
    private var currentPosition: Int = 0
    var ratingbar: RatingBar? = null
    var ratingforsubmit: String? = null
    private lateinit var dialog: Dialog
    private var overallStatus: String? = null
    var isAllapproved:Boolean?=false
    var list = ArrayList<String>()
    public var store: String = ""
  public  var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()
    public var imageUrlList = java.util.ArrayList<GetImageUrlResponse.Category>()

    val PREVIEW_LAST_IMAGE_ACTIVITY: Int = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewImageBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview_retro_last_image
        )

        if (intent != null) {

            getImageUrlsResponse =
                intent.getSerializableExtra("GET_IMAGE_URLS_RESPONSE") as GetImageUrlResponse?
            list =
                getIntent().getExtras()?.getStringArrayList("imageList")!!
            store = intent.getStringExtra("store")!!

            imageUrlList =
                intent.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>
        }
        setUp()
    }

    private fun setUp() {
//        list.add("https://askapollopr.blob.core.windows.net/sampleimage/ReturnImage_3857527_20230403_154049.jpg")


//        activityPreviewImageBinding.imageUrlModel = categorysList.get(0)
        previewImageAdapter = RetroPreviewImage(this, this, imageUrlsList)

        activityPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityPreviewImageBinding.previewImageViewpager.adapter = previewImageAdapter
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {


    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onClick(position: Int, status: String) {

    }

    override fun onClickReShoot() {
    }

    override fun onClickAccept() {
    }

    override fun onClickCompleted() {
    }

    override fun onClickBack() {
    }

    override fun onSuccessSaveAcceptReshoot(value: SaveAcceptResponse) {
        TODO("Not yet implemented")
    }

    override fun onFailureSaveAcceptReshoot(value: SaveAcceptResponse) {
        TODO("Not yet implemented")
    }


}
