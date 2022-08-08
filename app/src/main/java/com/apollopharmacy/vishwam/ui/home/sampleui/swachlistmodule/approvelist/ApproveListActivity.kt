package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityApproveListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.swachlistscreen3.PreviewLastImageActivity

class ApproveListActivity : AppCompatActivity(), ApproveListcallback {
    var pendingAndApproved: PendingAndApproved? = null
    lateinit var activityApproveListBinding: ActivityApproveListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApproveListBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_approve_list
        )
        setUp()
    }

    fun setUp() {
        when (pendingAndApproved != null) {
            true -> {
                pendingAndApproved =
                    intent.getSerializableExtra("PENDING_AND_APPROVED") as PendingAndApproved
            }
        }
        activityApproveListBinding.updateButtonApproveList.setOnClickListener {
            val intent = Intent(ViswamApp.context, PreviewLastImageActivity::class.java)
            startActivity(intent)
        }
    }
}