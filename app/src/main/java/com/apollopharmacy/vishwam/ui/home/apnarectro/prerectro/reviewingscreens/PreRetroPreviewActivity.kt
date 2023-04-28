package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPreviewPreretroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.RetroPreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen.PostRectroReviewScreen
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreRectroReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.adapter.RectroCategoryListAdapter

class PreRetroPreviewActivity : AppCompatActivity(), PreRetroReviewingCallback {
    lateinit var activityPreviewBinding: ActivityPreviewPreretroBinding
    private var stage:String=""
    private var fragmentName: String=""
    var adapter: RectroCategoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview_preretro)
        setUp()
    }

    private fun setUp() {
        activityPreviewBinding.callback=this
        stage= intent.getStringExtra("stage")!!
        fragmentName=intent.getStringExtra("fragmentName")!!
//        if(fragmentName.equals("nonApprovalFragment")){
//            activityPreviewBinding.review.visibility=View.GONE
//        }else{
//            activityPreviewBinding.review.visibility=View.VISIBLE
//        }
        var approvelist= java.util.ArrayList<String>()
        approvelist!!.add("Service Desk Covering System")
        approvelist!!.add("Signage")
        approvelist!!.add("Pharmas Rack Left and Right")
        adapter= RectroCategoryListAdapter(this, approvelist,this)
        activityPreviewBinding.recyclerViewcategories.adapter=adapter




    }

    override fun onClickItemView(position: Int, status: String) {
        if(stage.equals("isPreRetroStage")){
            val intent=Intent(this, PreRectroReviewActivity::class.java)
            intent.putExtra("stage", stage)
            startActivity(intent)
        }else{
            val intent=Intent(this, PostRectroReviewScreen::class.java)
            intent.putExtra("stage", stage)
            startActivity(intent)
        }
        }

    override fun onClickReview() {
        val intent=Intent(this, RetroPreviewImageActivity::class.java)
        intent.putExtra("stage", stage)
        startActivity(intent)
    }


}
