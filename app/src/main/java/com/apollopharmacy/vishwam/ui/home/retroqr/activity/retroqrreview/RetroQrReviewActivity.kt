package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrReviewBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.adapter.ImagesAdapterRetroReviewQr

class RetroQrReviewActivity : AppCompatActivity(), RetroQrReviewCallback {
    private lateinit var activityRetroQrReviewBinding: ActivityRetroQrReviewBinding
    private lateinit var viewModel: RetroQrReviewViewModel
    private lateinit var imagesAdapterRetroReviewQr: ImagesAdapterRetroReviewQr
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrReviewBinding = DataBindingUtil.setContentView(this@RetroQrReviewActivity,
            R.layout.activity_retro_qr_review)
        viewModel = ViewModelProvider(this)[RetroQrReviewViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        imagesAdapterRetroReviewQr =
            ImagesAdapterRetroReviewQr(this@RetroQrReviewActivity, this@RetroQrReviewActivity)
        activityRetroQrReviewBinding.rackRecyclerView.adapter = imagesAdapterRetroReviewQr
        activityRetroQrReviewBinding.rackRecyclerView.layoutManager =
            LinearLayoutManager(this@RetroQrReviewActivity, LinearLayoutManager.VERTICAL, false)
    }
}