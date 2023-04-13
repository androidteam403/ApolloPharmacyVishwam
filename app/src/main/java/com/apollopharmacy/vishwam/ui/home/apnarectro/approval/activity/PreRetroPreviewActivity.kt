package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPreviewPreretroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.PreviewLastImageCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.RetroPreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter.RectroCategoryListAdapter

class PreRetroPreviewActivity : AppCompatActivity(), PreviewLastImageCallback {
    lateinit var activityPreviewBinding: ActivityPreviewPreretroBinding

    var adapter: RectroCategoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview_preretro)
        setUp()
    }

    private fun setUp() {
        var approvelist= java.util.ArrayList<String>()
        approvelist!!.add("Service Desk Covering System")
        approvelist!!.add("Signage")
        approvelist!!.add("Pharmas Rack Left and Right")
        adapter= RectroCategoryListAdapter(this, approvelist,this)
        activityPreviewBinding.recyclerViewcategories.adapter=adapter




    }

    override fun onClick(position: Int, status: String) {
        val intent=Intent(this,PreRetroReviewActivity::class.java)
        startActivity(intent)    }


}
