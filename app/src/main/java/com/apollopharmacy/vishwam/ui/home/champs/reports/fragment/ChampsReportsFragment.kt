package com.apollopharmacy.vishwam.ui.home.champs.reports.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentChampsReportsBinding
import com.apollopharmacy.vishwam.ui.home.champs.reports.activity.champssummaryreports.ChampsSummaryReportsActivity
import com.apollopharmacy.vishwam.ui.home.champs.reports.activity.champssurveyreports.ChampsSurveyReportsActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetailsactivity.SurveyDetailsActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.SwachListViewModel

class ChampsReportsFragment : BaseFragment<ChampsReportsViewModel, FragmentChampsReportsBinding>(),
    ChampsReportsFragmentCallBack {

    override val layoutRes: Int
        get() = R.layout.fragment_champs_reports

    override fun retrieveViewModel(): ChampsReportsViewModel {
        return ViewModelProvider(this).get(ChampsReportsViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback=this


    }

    override fun onClickChampsSurveyReport() {
        val intent = Intent(context, ChampsSurveyReportsActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSurveySummaryReport() {
        val intent = Intent(context, ChampsSummaryReportsActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickRegionWiseAnalysisReport() {

    }
}