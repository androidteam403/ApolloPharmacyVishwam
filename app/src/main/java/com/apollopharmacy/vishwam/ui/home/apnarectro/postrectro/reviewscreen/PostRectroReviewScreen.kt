package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.method.Touch
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPostRectroReviewScreenBinding
import com.apollopharmacy.vishwam.util.zoomage.ZoomMultiImageView
import com.apollopharmacy.vishwam.util.zoomage.ZoomageView


class PostRectroReviewScreen : AppCompatActivity() {
    var stage: String = ""
    lateinit var activityPostRectroReviewScreenBinding: ActivityPostRectroReviewScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPostRectroReviewScreenBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_post_rectro_review_screen
        )
        setUp()
    }
var handler = Handler()
        var runnable = Runnable {

        activityPostRectroReviewScreenBinding.imageOne.dispatchTouchEvent(
            MotionEvent.obtain(
                0,
                0,
                MotionEvent.ACTION_DOWN,
                100F,
                100F,
                0.5f,
                5F,
                0,
                1F,
                1F,
                0,
                0
            )
        )
        activityPostRectroReviewScreenBinding.imageTwo.dispatchTouchEvent(
            MotionEvent.obtain(
                0,
                0,
                MotionEvent.ACTION_DOWN,
                100F,
                100F,
                0.5f,
                5F,
                0,
                1F,
                1F,
                0,
                0
            )
        )
        }
    private fun setUp() {
        stage = intent.getStringExtra("stage")!!
        if(stage.equals("isPostRetroStage")){
            activityPostRectroReviewScreenBinding.reviewName.setText("Post Retro Review")
        }else{
            activityPostRectroReviewScreenBinding.reviewName.setText("After Completion Review")
        }
//        val zoomageView1: ZoomMultiImageView = findViewById<ZoomMultiImageView>(R.id.image_one)
//        val zoomageView2 : ZoomMultiImageView= findViewById<ZoomMultiImageView>(R.id.image_two)
        activityPostRectroReviewScreenBinding.imageOne.setImageView2(
            activityPostRectroReviewScreenBinding.imageTwo
        )
        activityPostRectroReviewScreenBinding.imageTwo.setImageView1(
            activityPostRectroReviewScreenBinding.imageOne
        )
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)
        activityPostRectroReviewScreenBinding.preRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.blue))

            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))

            }
        }
        activityPostRectroReviewScreenBinding.postRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(resources.getColor(R.color.blue))

            } else {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))

            }
        }
        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(resources.getColor(R.color.blue))

            } else {
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(resources.getColor(R.color.grey))

            }
        }
//        activityPostRectroReviewScreenBinding.imageOne.dispatchTouchEvent(
//            MotionEvent.obtain(
//                0,
//                0,
//                MotionEvent.ACTION_DOWN,
//                100F,
//                100F,
//                0.5f,
//                5F,
//                0,
//                1F,
//                1F,
//                0,
//                0
//            )
//        )
//        activityPostRectroReviewScreenBinding.imageTwo.dispatchTouchEvent(
//            MotionEvent.obtain(
//                0,
//                0,
//                MotionEvent.ACTION_DOWN,
//                100F,
//                100F,
//                0.5f,
//                5F,
//                0,
//                1F,
//                1F,
//                0,
//                0
//            )
//        )
        if (stage.equals("isPostRetroStage")) {
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                View.GONE
            activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
            activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.blue))

        } else {
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                View.VISIBLE
            activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=false
            activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
        }
    }

    override fun onResume() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)
        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }
}
