package com.apollopharmacy.vishwam.ui.home.dashboard.managerdashboard

import android.app.FragmentManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.apollopharmacy.vishwam.databinding.FragmentManagerDashboardBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.adapter.DashboardAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard.CeoDashboardViewModel
import com.apollopharmacy.vishwam.ui.home.dashboard.managerdashboard.adapter.ManagerAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.ApprovedFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.RejectedFragment
import lecho.lib.hellocharts.model.SliceValue
import java.util.ArrayList

class ManagerDashboardFragment : BaseFragment<ManagerDashboardViewModel, com.apollopharmacy.vishwam.databinding.FragmentManagerDashboardBinding>(){
    private var chart: AnyChartView? = null
    var managerAdapter: ManagerAdapter? = null
    private val count = listOf(257, 321, 142)
    private val lists = listOf("Pending", "Approved", "Reject")
    var pieData: List<SliceValue> = ArrayList()

    override val layoutRes: Int
        get() = R.layout.fragment_manager_dashboard

    override fun retrieveViewModel(): ManagerDashboardViewModel {
        return ViewModelProvider(this).get(ManagerDashboardViewModel::class.java)
    }

    override fun setup() {
        configChartView()
        var names = ArrayList<String>()

        names.add("5")
        names.add("8")
        names.add("12")

        managerAdapter = ManagerAdapter()
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.dashboardManagerRecyclerview.layoutManager = layoutManager
        viewBinding.dashboardManagerRecyclerview.adapter = managerAdapter
    }

    private fun configChartView() {


        val pie: Pie = AnyChart.pie()
        var names = ArrayList<String>()
        pie.palette().itemAt(2, SolidFill("#c7c7c7", 1
        )
        )
        pie.palette().itemAt(1, SolidFill("#FF6D6A", 1
        )
        )
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