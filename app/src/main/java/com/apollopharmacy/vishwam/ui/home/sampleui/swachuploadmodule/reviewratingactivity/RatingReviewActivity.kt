package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityRatingReviewBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity.adapter.RatingReviewAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity.adapter.RatingReviewViewModel
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelRequest

class RatingReviewActivity : AppCompatActivity() {
    private lateinit var ratingReviewAdapter: RatingReviewAdapter
    lateinit var ratingReviewBinding: ActivityRatingReviewBinding
    lateinit var viewModel: RatingReviewViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_review)

        ratingReviewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_rating_review

        )
        viewModel = ViewModelProvider(this)[RatingReviewViewModel::class.java]
        val swachId = intent.getStringExtra("swachhid")
        val storeId = intent.getStringExtra("storeId")
        val userDesignation = intent.getStringExtra("userDesignation")
        ratingReviewBinding.swachId.text= swachId


        var submit = GetImageUrlModelRequest()
        submit.storeid =  storeId
        submit.swachhId = swachId
        showLoadingTemp(this)

        viewModel.getImageUrl(submit)

        viewModel.getImageUrlsList.observeForever {
            if (it != null) {

            if(it.remarks!=null && it.remarks!!.size>0){
                ratingReviewBinding.noOrdersFound.visibility = View.GONE
                ratingReviewAdapter =
                    RatingReviewAdapter(this, it.remarks,userDesignation)
                val layoutManager = LinearLayoutManager(ViswamApp.context)
                ratingReviewBinding.ratingReviewRecyclerView.layoutManager = layoutManager
                ratingReviewBinding.ratingReviewRecyclerView.itemAnimator =
                    DefaultItemAnimator()
                ratingReviewBinding.ratingReviewRecyclerView.adapter = ratingReviewAdapter
                hideLoadingTemp()
            }else{
                ratingReviewBinding.noOrdersFound.visibility = View.VISIBLE
                hideLoadingTemp()
            }


            } else{
                ratingReviewBinding.noOrdersFound.visibility = View.VISIBLE
                hideLoadingTemp()
            }
        }

        ratingReviewBinding.backButton.setOnClickListener {
            super.onBackPressed()
        }

    }

    var mProgressDialogTemp: ProgressDialog? = null
    fun showLoadingTemp(context: Context) {
        hideLoadingTemp()
        mProgressDialogTemp = showLoadingDialogTemp(context)
    }

    fun hideLoadingTemp() {
        if (mProgressDialogTemp != null && mProgressDialogTemp!!.isShowing()) {
            mProgressDialogTemp!!.dismiss()
        }
    }
    fun showLoadingDialogTemp(context: Context?): ProgressDialog? {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }
}