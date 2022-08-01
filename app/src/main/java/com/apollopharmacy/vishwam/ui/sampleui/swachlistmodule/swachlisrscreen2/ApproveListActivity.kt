package com.apollopharmacy.vishwam.ui.sampleui.swachlistmodule.swachlisrscreen2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityApproveListBinding
import com.apollopharmacy.vishwam.databinding.ActivityUploadNowButtonBinding
import com.apollopharmacy.vishwam.ui.sampleui.swachlistmodule.swachlistscreen3.PreviewLastImageActivity
import com.apollopharmacy.vishwam.ui.sampleui.uploadnowbutton.UploadNowButtonActivity

class ApproveListActivity : AppCompatActivity() {
    lateinit var activityApproveListBinding: ActivityApproveListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApproveListBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_approve_list
        )

        activityApproveListBinding.updateButtonApproveList.setOnClickListener {
            val intent = Intent(ViswamApp.context, PreviewLastImageActivity::class.java)
            startActivity(intent)
        }

    }
}