package com.apollopharmacy.vishwam.ui.sampleui.uploadnowbutton

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityUploadNowButtonBinding
import com.apollopharmacy.vishwam.dialog.ImageDeleteSwachDialog
import com.apollopharmacy.vishwam.dialog.SignOutDialog

class UploadNowButtonActivity : AppCompatActivity() {
    lateinit var activityUploadNowButtonBinding: ActivityUploadNowButtonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadNowButtonBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_upload_now_button
        )

        activityUploadNowButtonBinding.cameraDelete.setOnClickListener {
            dialogExit()

        }
    }

    private fun dialogExit() {
        Handler().postDelayed({ ImageDeleteSwachDialog().show(supportFragmentManager, "") }, 200)
    }
}