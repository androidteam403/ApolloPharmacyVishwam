package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyActivity
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),ApnaSurveyCallback{
    var adapter: ApnaSurveyAdapter? = null

    override val layoutRes: Int
        get() = R.layout.fragment_apna_survey

    override fun retrieveViewModel(): ApnaSurveylViewModel {
        return ViewModelProvider(this).get(ApnaSurveylViewModel::class.java)
    }

    override fun setup() {

        viewModel.getApnaSurveyList(this)
        MainActivity.mInstance.plusIconApna.setOnClickListener {
            val intent = Intent(activity, ApnaNewSurveyActivity::class.java)
            requireActivity().startActivity(intent)
        }

    }

    override fun onClick(position: Int, surveyListResponse: SurveyListResponse.Row) {

        val i = Intent(activity, ApnaPreviewActivity::class.java)
        i.putExtra("regionList", surveyListResponse)
        startActivityForResult(i, 210)
        startActivity(i)

    }

    override fun onSuccessgetSurveyDetails(value: SurveyListResponse) {

        adapter= ApnaSurveyAdapter(requireContext(),
            value.data!!.listData!!.rows as ArrayList<SurveyListResponse.Row>,this)
        viewBinding.recyclerViewapproval.adapter=adapter    }


}