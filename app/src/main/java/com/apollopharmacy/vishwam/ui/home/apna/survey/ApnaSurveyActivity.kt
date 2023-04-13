package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter

class ApnaSurveyActivity : AppCompatActivity(), ApnaSurveyCallback {
    lateinit var fragmentApnaSurveyBinding: FragmentApnaSurveyBinding

    var adapter: ApnaSurveyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentApnaSurveyBinding = DataBindingUtil.setContentView(this, R.layout.fragment_apna_survey)
        setUp()
    }

    private fun setUp() {
        var approvelist= java.util.ArrayList<String>()
        approvelist!!.add("APPROVED")
        approvelist!!.add("PENDING")
        adapter= ApnaSurveyAdapter(this,approvelist,this)
        fragmentApnaSurveyBinding.recyclerViewapproval.adapter=adapter


    }

    override fun onClick(position: Int, status: String) {


    }


}
