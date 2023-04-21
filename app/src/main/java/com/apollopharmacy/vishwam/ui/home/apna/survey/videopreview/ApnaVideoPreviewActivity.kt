package com.apollopharmacy.vishwam.ui.home.apna.survey.videopreview

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityVideopreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter
import kotlinx.android.synthetic.main.activity_videopreview.*
import kotlinx.android.synthetic.main.adapter_get_sub_category_details.view.*
import java.io.File
import java.util.jar.Manifest

class ApnaVideoPreviewActivity : AppCompatActivity() {
    lateinit var activityVideopreviewBinding: ActivityVideopreviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVideopreviewBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_videopreview)
        setUp()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setUp() {
        if (intent!=null) {
            val mediaController=MediaController(this)

            val videoUrl =intent.getStringExtra("activity")
            val uri = Uri.parse(videoUrl)
            activityVideopreviewBinding.videoView.setVideoURI(uri)
            mediaController.setAnchorView(activityVideopreviewBinding.videoView)

            activityVideopreviewBinding.videoView.setMediaController(mediaController)
            activityVideopreviewBinding.videoView.requestFocus()
            activityVideopreviewBinding.videoView.start()
        }

//        val videoUrl =
//          "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1"

//        activityVideopreviewBinding.videoView.setVideoURI(uri)    online
//        if (checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,1001)){
//            var videoPath: String =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
//            var file: File = File(videoPath, "jurassicpark" + ".mp4")
//            Log.e("directory",file.absolutePath)
//
//            activityVideopreviewBinding.videoView.setVideoPath(file.absolutePath)
//            mediaController.setAnchorView(activityVideopreviewBinding.videoView)
//
//            activityVideopreviewBinding.videoView.setMediaController(mediaController)
//            activityVideopreviewBinding.videoView.requestFocus()
//            activityVideopreviewBinding.videoView.start()
//
//        }else{
//
//        }
//
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }


    private  fun checkPermission(permission: String,requestCode:Int):Boolean{
        if (ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
            return false
        }
        return true
    }

}
