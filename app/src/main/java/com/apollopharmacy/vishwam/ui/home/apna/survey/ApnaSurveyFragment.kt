package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),ApnaSurveyCallback{
    var adapter: ApnaSurveyAdapter? = null

    override val layoutRes: Int
        get() = R.layout.fragment_apna_survey

    override fun retrieveViewModel(): ApnaSurveylViewModel {
        return ViewModelProvider(this).get(ApnaSurveylViewModel::class.java)
    }

    override fun setup() {
        var approvelist= java.util.ArrayList<String>()
        approvelist!!.add("APPROVED")
        approvelist!!.add("PENDING")





        adapter= context?.let { ApnaSurveyAdapter(it, approvelist!!,this) }
        viewBinding.recyclerViewapproval.adapter=adapter

    }

    override fun onClick(position: Int, status: String) {

    }


}