package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityRatingReviewBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity.adapter.RatingReviewAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity.adapter.RatingReviewViewModel
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.ReShootActivityViewModel
import com.apollopharmacy.vishwam.util.Utlis

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
        ratingReviewBinding.swachId.text= swachId


        var submit = GetImageUrlModelRequest()
        submit.storeid =  Preferences.getSiteId()
        submit.swachhId = swachId
        Utlis.showLoading(this)

        viewModel.getImageUrl(submit)

        viewModel.getImageUrlsList.observeForever {
            if (it != null && it.categoryList != null) {

            if(it.remarks!=null && it.remarks!!.size>0){
                ratingReviewBinding.noOrdersFound.visibility = View.GONE
                ratingReviewAdapter =
                    RatingReviewAdapter(this, it.remarks)
                val layoutManager = LinearLayoutManager(ViswamApp.context)
                ratingReviewBinding.ratingReviewRecyclerView.layoutManager = layoutManager
                ratingReviewBinding.ratingReviewRecyclerView.itemAnimator =
                    DefaultItemAnimator()
                ratingReviewBinding.ratingReviewRecyclerView.adapter = ratingReviewAdapter
                Utlis.hideLoading()
            }else{
                ratingReviewBinding.noOrdersFound.visibility = View.VISIBLE
                Utlis.hideLoading()
            }


            }
        }

        ratingReviewBinding.backButton.setOnClickListener {
            super.onBackPressed()
        }

    }
}