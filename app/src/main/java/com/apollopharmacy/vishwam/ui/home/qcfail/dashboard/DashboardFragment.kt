package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import android.view.View
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
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.ApprovedFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.RejectedFragment
import lecho.lib.hellocharts.model.SliceValue
import java.util.*


class DashboardFragment : BaseFragment<DashBoardViewModel, FragmentDashboardBinding>() {
    private var chart: AnyChartView? = null
    private val count = listOf(257, 321, 142)
    private val lists = listOf("Pending", "Approved", "Reject")
    var pieData: List<SliceValue> = ArrayList()

    override val layoutRes: Int
        get() = R.layout.fragment_dashboard

    override fun retrieveViewModel(): DashBoardViewModel {
        return ViewModelProvider(this).get(DashBoardViewModel::class.java)
    }

    override fun setup() {

        val userData = LoginRepo.getProfile()
        if (userData != null) {
            viewBinding.empname.setText(userData.EMPNAME)
            viewBinding.empid.setText(userData.EMPID)
        }

//

        configChartView()
        var names = ArrayList<String>()

        names.add("5")
        names.add("8")
        names.add("12")
//        viewBinding.dashboardrecycleview.adapter = context?.let { DashboardAdapter(it, names) }

    }

    private fun configChartView() {


        val pie: Pie = AnyChart.pie()
        var names = ArrayList<String>()
        pie.palette().itemAt(2, SolidFill("#c7c7c7", 1
        ))
        pie.palette().itemAt(1, SolidFill("#FF6D6A", 1
        ))
        val dataPieChart: MutableList<DataEntry> = mutableListOf()
        for (index in count.indices) {
            dataPieChart.add(ValueDataEntry(lists.elementAt(index), count.elementAt(index)))
        }
        pie.labels().format("{%value}")

        pie.animation(true, 800)
        pie.data(dataPieChart)

        pie.setOnClickListener(object :
            ListenersInterface.OnClickListener(arrayOf("x", "value")) {
            override fun onClick(event: Event) {
                event.getData().get("x")
                event.getData().get("value")

                if (event.getData().get("x").equals("Approved")) {
                    //APL49396


//                    findNavController().navigate(R.id.action_dashboardFragment_to_approvedFragment)
//
                    val fragment: Fragment = ApprovedFragment()
                    val fragmentManager = activity!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
//                    fragmentTransaction.replace(R.id.fragment_container, fragment)
                    fragmentTransaction.addToBackStack(null)
//                    fragmentTransaction.setReorderingAllowed(true)
//                    fragmentTransaction.isAddToBackStackAllowed
                    fragmentTransaction.commit()
//                    MainActivity.mInstance.close()


                } else if (event.getData().get("x").equals("Pending")) {
                    val fragment: Fragment = PendingFragment()
                    val fragmentManager = activity!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
//                    fragmentTransaction.replace(R.id.fragment_container, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                } else if (event.getData().get("x").equals("Reject")) {
                    val fragmentManager = activity!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
//                    fragmentTransaction.replace(R.id.fragment_container, RejectedFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }
        })
        viewBinding.piechart.setChart(pie)

    }


}