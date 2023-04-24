package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerectropending

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityReviewPreretroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerectropending.adapter.RectroReviewCategoryListAdapter

class PreRetroPendingReviewActivity : AppCompatActivity(), PreRetroReviewCallback {
    lateinit var activityReviewBinding: ActivityReviewPreretroBinding

    var adapter: RectroReviewCategoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_review_preretro)
        setUp()
    }

    private fun setUp() {
        var approvelist= java.util.ArrayList<String>()
        approvelist!!.add("Service Desk Covering System")
        approvelist!!.add("Signage")
        approvelist!!.add("Pharmas Rack Left and Right")
        adapter= RectroReviewCategoryListAdapter(this, approvelist,this)
        activityReviewBinding.recyclerViewcategories.adapter=adapter


    }

    override fun onClick(position: Int, status: String) {
//        val intent= Intent(this,RetroPreviewImageActivity::class.java)
//        startActivity(intent)

    }


}
