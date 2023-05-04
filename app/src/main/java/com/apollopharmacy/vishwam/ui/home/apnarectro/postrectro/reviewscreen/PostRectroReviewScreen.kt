package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen

import android.graphics.Color
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
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.util.zoomage.ZoomMultiImageView
import com.apollopharmacy.vishwam.util.zoomage.ZoomageView
import com.bumptech.glide.Glide
import org.apache.commons.lang3.text.WordUtils


class PostRectroReviewScreen : AppCompatActivity() {
    var stage: String = ""
    private var retroId: String = ""
    private var store: String = ""
    var categoryName: String = ""
    var categorypos: Int = 0
    private var status: String = ""
    private var url1: String = ""
    private var url2: String = ""
    private var url3: String = ""

    public var imageUrlList = ArrayList<List<GetImageUrlResponse.ImageUrl>>()
    var pos: Int = 0

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


        if (intent != null) {
            retroId = intent.getStringExtra("retroId")!!
            status = intent.getStringExtra("status")!!

            pos = intent.getIntExtra("position", 0)!!

            categoryName = intent.getStringExtra("categoryName")!!
            categorypos = intent.getIntExtra("categoryPos", 0)!!
            store = intent.getStringExtra("store")!!
            imageUrlList =
                intent.getSerializableExtra("imageList") as ArrayList<List<GetImageUrlResponse.ImageUrl>>


        }



        activityPostRectroReviewScreenBinding.categoryNumber.setText((categorypos + 1).toString())
        activityPostRectroReviewScreenBinding.categoryName.setText(categoryName)
        activityPostRectroReviewScreenBinding.status.setText(status)
        if (status.toLowerCase().contains("pen")) {
            activityPostRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        } else if (status.toLowerCase().contains("app")) {
            activityPostRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#39B54A"))
        } else if (status.toLowerCase().contains("res")) {
            activityPostRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        }
        activityPostRectroReviewScreenBinding.reviewName.setText(WordUtils.capitalizeFully(stage.replace(
            "-",
            " ")) + " Review")

        activityPostRectroReviewScreenBinding.retroId.setText(retroId)
        activityPostRectroReviewScreenBinding.storeId.setText(store)

url1= imageUrlList.get(pos).get(0).url.toString()
  url2= imageUrlList.get(pos).get(1).url.toString()

        Glide.with(this).load(imageUrlList.get(pos).get(0).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.imageOne)


        Glide.with(this).load(imageUrlList.get(pos).get(1).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.imageTwo)


        Glide.with(this).load(imageUrlList.get(pos).get(0).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.preRectroCbLayoutImage)

        Glide.with(this).load(imageUrlList.get(pos).get(1).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.postRectroCbLayoutImage)



        if (stage.toLowerCase().contains("pos")) {
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE

        } else if (stage.toLowerCase().contains("aft")) {
            Glide.with(this).load(imageUrlList.get(pos).get(2).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.afterCompletionCbLayoutImage)
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.VISIBLE


        }

        if (imageUrlList.get(pos).get(0).stage.equals("1")) {
            activityPostRectroReviewScreenBinding.imageOneStage.setText("Pre Retro Image")
        }
        if (imageUrlList.get(pos).get(1).stage.equals("2")) {
            activityPostRectroReviewScreenBinding.imageTwoStage.setText("Post Retro Image")
        }



        activityPostRectroReviewScreenBinding.imageOne.setImageView2(
            activityPostRectroReviewScreenBinding.imageTwo
        )
        activityPostRectroReviewScreenBinding.imageTwo.setImageView1(
            activityPostRectroReviewScreenBinding.imageOne
        )






        activityPostRectroReviewScreenBinding.preRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (stage.toLowerCase().contains("aft")) {
                if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {

                    if (isChecked) {

                        Glide.with(this).load(imageUrlList.get(pos).get(0).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                        Glide.with(this).load(imageUrlList.get(pos).get(2).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)


                        activityPostRectroReviewScreenBinding.imageOneStage.setText("Pre Retro Image")
                        activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                        activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                            resources.getColor(
                                R.color.blue))
                        activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                        activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                            resources.getColor(R.color.grey))
                    }
                } else {
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                }


            }

        }
        activityPostRectroReviewScreenBinding.postRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (stage.toLowerCase().contains("aft")) {
                if (activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {

                    if (isChecked) {
                        Glide.with(this).load(imageUrlList.get(pos).get(0).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                        Glide.with(this).load(imageUrlList.get(pos).get(1).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)

                        activityPostRectroReviewScreenBinding.imageTwoStage.setText("Post Retro Image")

                        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                            false
                        activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                        activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                            resources.getColor(R.color.blue))
                        activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                            resources.getColor(R.color.grey))
                    }
                } else {

                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                }
            }

        }
        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (stage.toLowerCase().contains("aft")) {
                Glide.with(this).load(imageUrlList.get(pos).get(1).url)
                    .placeholder(R.drawable.thumbnail_image)
                    .into(activityPostRectroReviewScreenBinding.imageOne)

                Glide.with(this).load(imageUrlList.get(pos).get(2).url)
                    .placeholder(R.drawable.thumbnail_image)
                    .into(activityPostRectroReviewScreenBinding.imageTwo)

                if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {

                    if (isChecked) {


                        activityPostRectroReviewScreenBinding.imageOne.setImageView2(activityPostRectroReviewScreenBinding.imageTwo)
                        activityPostRectroReviewScreenBinding.imageTwo.setImageView1(activityPostRectroReviewScreenBinding.imageOne)


                        activityPostRectroReviewScreenBinding.imageTwoStage.setText("After-Completion  Image")
                        activityPostRectroReviewScreenBinding.imageOneStage.setText("Post Retro Image")
                        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                            true
                        activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                            resources.getColor(R.color.blue))

                        activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                        activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                            resources.getColor(
                                R.color.grey))

                    }
                } else {
                    if (isChecked == false)
                        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                            true
                }

            }
        }


        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)

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
//        if (stage.equals("isPostRetroStage")) {
//            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
//                View.GONE
//            activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
//            activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(
//                R.color.blue))
//
//        } else {
//            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
//                View.VISIBLE
//            activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
//            activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(
//                R.color.grey))
//        }


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
