package com.apollopharmacy.vishwam.ui.sampleui.reshootactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityReShootBinding
import com.apollopharmacy.vishwam.databinding.ActivityUploadNowButtonBinding
import com.apollopharmacy.vishwam.ui.sampleui.uploadnowbutton.UploadNowButtonActivity

class ReShootActivity : AppCompatActivity() {
    lateinit var activityreShootBinding: ActivityReShootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityreShootBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_re_shoot

        )

        activityreShootBinding.reshootButton.setOnClickListener {
            val intent = Intent(context, UploadNowButtonActivity::class.java)
            startActivity(intent)
        }
    }
}