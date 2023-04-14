package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.content.Intent
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.DialogAcceptQcBinding
import com.apollopharmacy.vishwam.databinding.DialogQuickGoBinding
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),ApnaSurveyCallback,
    MainActivityCallback {
    var adapter: ApnaSurveyAdapter? = null

    override val layoutRes: Int
        get() = R.layout.fragment_apna_survey

    override fun retrieveViewModel(): ApnaSurveylViewModel {
        return ViewModelProvider(this).get(ApnaSurveylViewModel::class.java)
    }

    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this

        var approvelist= java.util.ArrayList<String>()
        approvelist!!.add("APPROVED")
        approvelist!!.add("PENDING")





        adapter= context?.let { ApnaSurveyAdapter(it, approvelist!!,this) }
        viewBinding.recyclerViewapproval.adapter=adapter

    }

    override fun onClick(position: Int, status: String) {

    }

    override fun onClickFilterIcon() {
    }

    override fun onClickSiteIdIcon() {
        val dialogBinding: DialogQuickGoBinding? =
            DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                R.layout.dialog_quick_go,
                null,
                false)
        val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
    }

    override fun onClickQcFilterIcon() {

    }


}