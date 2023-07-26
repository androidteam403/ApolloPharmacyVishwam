package com.apollopharmacy.vishwam.ui.home.dashboard

import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentCeoDashboardBinding


class CeoDashboardFragment : BaseFragment<CeoDashboardViewModel, FragmentCeoDashboardBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_ceo_dashboard

    override fun retrieveViewModel(): CeoDashboardViewModel {
        return ViewModelProvider(this).get(CeoDashboardViewModel::class.java)
    }

    override fun setup() {
//        val pie = AnyChart.pie()
//
        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("John", 10000))
        data.add(ValueDataEntry("Jake", 12000))
        data.add(ValueDataEntry("Peter", 18000))

//        val anyChartView = findViewById(R.id.any_chart_view) as AnyChartView
//        viewBinding.anyChartView.setChart(pie) as AnyChartView
    }
}