package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.QcFragmentDashboardBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter.DashBaordAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import java.util.*
import java.util.stream.Collectors


class QcDashboardFragment : BaseFragment<DashBoardViewModel, QcFragmentDashboardBinding>() {

    override val layoutRes: Int
        get() = R.layout.qc_fragment_dashboard
    private var pendingCountResponseList = ArrayList<PendingCountResponse.Pendingcount>()
    private var designationsList = ArrayList<String>()


    override fun retrieveViewModel(): DashBoardViewModel {
        return ViewModelProvider(this).get(DashBoardViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun setup() {
        showLoading()
        viewModel.getQcPendingList(Preferences.getToken(),
            Preferences.getAppLevelDesignationQCFail())

        viewModel.qcPendingCountList.observe(viewLifecycleOwner, {
            hideLoading()
            if (it.status == true) {
                if (!it.pendingcount.isNullOrEmpty()) {
                    pendingCountResponseList =
                        it.pendingcount as ArrayList<PendingCountResponse.Pendingcount>
                    val designations: List<String> = pendingCountResponseList.stream()
                        .map<String>(PendingCountResponse.Pendingcount::designation).distinct()
                        .collect(Collectors.toList()).reversed()

                    for (i in designations.indices) {
                        designationsList.add(designations.get(i))
                    }


                    viewBinding.dashboardrecycleview.adapter =
                        context?.let { it1 ->
                            DashBaordAdapter(it1, pendingCountResponseList,
                                designationsList)
                        }

                }
            }

        })


    }


}