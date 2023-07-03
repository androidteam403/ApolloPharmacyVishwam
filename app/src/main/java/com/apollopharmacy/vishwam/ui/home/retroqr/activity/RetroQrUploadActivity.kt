package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrUploadBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter.UploadRackAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison.ImageComparisonActivity

class RetroQrUploadActivity : AppCompatActivity(), RetroQrUploadCallback {
    private lateinit var activityRetroQrUploadBinding: ActivityRetroQrUploadBinding
    private lateinit var viewModel: RetroQrUploadViewModel
    private lateinit var uploadRackAdapter: UploadRackAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrUploadBinding = DataBindingUtil.setContentView(this@RetroQrUploadActivity,
            R.layout.activity_retro_qr_upload)
        viewModel = ViewModelProvider(this)[RetroQrUploadViewModel::class.java]
        activityRetroQrUploadBinding.callback = this@RetroQrUploadActivity
        setUp()
    }

    private fun setUp() {
        uploadRackAdapter =
            UploadRackAdapter(this@RetroQrUploadActivity, this@RetroQrUploadActivity)
        activityRetroQrUploadBinding.uploadRackRcv.adapter = uploadRackAdapter
        activityRetroQrUploadBinding.uploadRackRcv.layoutManager =
            LinearLayoutManager(this@RetroQrUploadActivity)

        // For demo
        activityRetroQrUploadBinding.lastUpdateLayout.setOnClickListener {
            val intent = Intent(this@RetroQrUploadActivity, ImageComparisonActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClickBackArrow() {
        finish()
    }

    override fun onClickSubmit() {
        // For demo
        val intent = Intent(this@RetroQrUploadActivity, RetroImagePreviewActivity::class.java)
        startActivity(intent)
    }
}