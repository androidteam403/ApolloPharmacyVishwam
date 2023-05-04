package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPostRectroReviewScreenBinding
import com.apollopharmacy.vishwam.databinding.ActivityPreRectroReviewBinding
import com.apollopharmacy.vishwam.databinding.ActivityReviewPreretroBinding
import com.bumptech.glide.Glide
import org.apache.commons.lang3.text.WordUtils

class PreRectroReviewActivity : AppCompatActivity() {
    var stage: String = ""
    private var retroId: String = ""
    private var store: String = ""
    var categoryName: String = ""
    var categorypos: Int = 0
    private var status: String = ""
    var url: String = ""

    var pos: Int = 0
    lateinit var activityPreRectroReviewScreenBinding: ActivityPreRectroReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPreRectroReviewScreenBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_pre_rectro_review
        )
        setUp()

    }

    private fun setUp() {


        if (intent != null) {
            retroId = intent.getStringExtra("retroId")!!
            status = intent.getStringExtra("status")!!
            stage = intent.getStringExtra("stage")!!
            url = intent.getStringExtra("url")!!

            pos = intent.getIntExtra("position", 0)!!

            categoryName = intent.getStringExtra("categoryName")!!
            categorypos = intent.getIntExtra("categoryPos", 0)!!
            store = intent.getStringExtra("store")!!
        }

        activityPreRectroReviewScreenBinding.categoryNumber.setText((categorypos+1).toString())
        activityPreRectroReviewScreenBinding.categoryName.setText(categoryName)
        activityPreRectroReviewScreenBinding.status.setText(status)
        if (status.toLowerCase().contains("pen")){
            activityPreRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        }
        else  if (status.toLowerCase().contains("app")){
            activityPreRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#39B54A"))
        }
        else  if (status.toLowerCase().contains("res")){
            activityPreRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        }
        activityPreRectroReviewScreenBinding.stage.setText(WordUtils.capitalizeFully(stage.replace(
            "-",
            " ")) + " Review")

        activityPreRectroReviewScreenBinding.retroId.setText(retroId)
        activityPreRectroReviewScreenBinding.store.setText(store)

        Glide.with(this).load(url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPreRectroReviewScreenBinding.image)


    }
}