package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.charts.Pie
import com.anychart.graphics.vector.SolidFill
import com.apollopharmacy.vishw.PendingFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentDashboardBinding
import com.apollopharmacy.vishwam.databinding.QcFragmentDashboardBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.ApprovedFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter.DashboardAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.RejectedFragment
import lecho.lib.hellocharts.model.SliceValue
import java.util.*
import java.util.prefs.Preferences
import kotlin.collections.ArrayList


class QcDashboardFragment : BaseFragment<DashBoardViewModel, QcFragmentDashboardBinding>() {

    override val layoutRes: Int
        get() = R.layout.qc_fragment_dashboard

    override fun retrieveViewModel(): DashBoardViewModel {
        return ViewModelProvider(this).get(DashBoardViewModel::class.java)
    }

    override fun setup() {
        showLoading()
        viewModel.getQcPendingList(com.apollopharmacy.vishwam.data.Preferences.getToken(),
            com.apollopharmacy.vishwam.data.Preferences.getAppLevelDesignationQCFail())


        viewModel.qcPendingCountList.observe(viewLifecycleOwner,{
            hideLoading()
            if (!it.pendingcount.isNullOrEmpty()) {
                viewBinding.dashboardrecycleview.adapter = context?.let { it1 ->
                    DashboardAdapter(it1,
                        it.pendingcount as ArrayList<PendingCountResponse.Pendingcount>)
                }
            }
        })

    }






}