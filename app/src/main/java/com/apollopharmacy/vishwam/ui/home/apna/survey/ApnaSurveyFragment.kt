package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyActivity
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),ApnaSurveyCallback{
    var adapter: ApnaSurveyAdapter? = null

    override val layoutRes: Int
        get() = R.layout.fragment_apna_survey

    override fun retrieveViewModel(): ApnaSurveylViewModel {
        return ViewModelProvider(this).get(ApnaSurveylViewModel::class.java)
    }

    override fun setup() {
        var approvelist= java.util.ArrayList<SurveyListResponse>()

        viewModel.getApnaSurveyList()
        viewModel.getSurveyListResponse.observe(viewLifecycleOwner,{
            approvelist.add(it)

            adapter= ApnaSurveyAdapter(requireContext(),approvelist,this)
            viewBinding.recyclerViewapproval.adapter=adapter
        })






        MainActivity.mInstance.plusIconApna.setOnClickListener {
            val intent = Intent(activity, ApnaNewSurveyActivity::class.java)
            requireActivity().startActivity(intent)
        }

    }

    override fun onClick(position: Int, status: String) {

    }


}