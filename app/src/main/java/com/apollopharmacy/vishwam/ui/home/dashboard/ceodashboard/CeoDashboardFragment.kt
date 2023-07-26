package com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anychart.AnyChartView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.databinding.FragmentCeoDashboardBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.adapter.DashboardAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.DashboardDetailsActivity
import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse
import com.apollopharmacy.vishwam.util.Utils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import lecho.lib.hellocharts.model.SliceValue
import java.util.*


class CeoDashboardFragment : BaseFragment<CeoDashboardViewModel, FragmentCeoDashboardBinding>(),
    CeoDashboardCallback {
    private var chart: AnyChartView? = null
    var dashboardAdapter: DashboardAdapter? = null
    private val count = listOf(257, 321, 142)
    val data: MutableList<String> = ArrayList()
    private val lists = listOf("Pending", "Approved", "Reject")
    var pieData: List<SliceValue> = ArrayList()
    override val layoutRes: Int
        get() = R.layout.fragment_ceo_dashboard

    override fun retrieveViewModel(): CeoDashboardViewModel {
        return ViewModelProvider(this).get(CeoDashboardViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        empDetailsMobile()
        val data: MutableList<Float> = ArrayList()
        data.add((10.0f))
        data.add((20.0f))
        data.add((70f))
        if (Preferences.getRoleForCeoDashboard().equals("ceo")) {
            viewBinding.dashboardName.setText("CEO Dashboard")
            viewBinding.nameOfTheStore.setText("Regional Head")
        } else if (Preferences.getRoleForCeoDashboard().equals("regional_head")) {
            viewBinding.dashboardName.setText("Regional Head Dashboard")
            viewBinding.nameOfTheStore.setText("Store Manager")
        } else if (Preferences.getRoleForCeoDashboard().equals("store_manager")) {
            viewBinding.dashboardName.setText("Store Manager Dashboard")
            viewBinding.nameOfTheStore.setText("Store Excecutive")
        } else if (Preferences.getRoleForCeoDashboard().equals("store_executive")) {
            viewBinding.dashboardName.setText("Store Executive Dashboard")
            viewBinding.nameOfTheStore.setText("Store List")
        } else if (Preferences.getRoleForCeoDashboard().equals("store_supervisor")) {
            viewBinding.dashboardName.setText("Store Supervisor Dashboard")
        }

//        configChartView()
        viewBinding.greaterThan2.setText("<2")
        viewBinding.greaterThan8.setText(">8")
//        var names = ArrayList<String>()
//
//        names.add("5")
//        names.add("8")
//        names.add("12")

        showLoading()
        viewModel.getTicketListByCountApi(
            this, Utils.getFirstDateOfCurrentMonth(), Utils.getCurrentDateCeoDashboard(), "SM1001"
        )

//        viewModel.getTicketListByCountApi(this, "2023-06-05", "2023-06-30", "Srilekha")//EX100011//Preferences.getValidatedEmpId()
    }

    private fun createChart(chartData: ArrayList<PieEntry>) {
        val pieEntry = chartData.map { PieEntry(it.value, it.value.toString()) }
        val rnd = Random()
        val colors = mutableListOf<Int>()
        for (i in chartData.indices) {
            colors.add(requireContext().getColor(R.color.blueee))
            colors.add(Color.parseColor("#f4b25e"))
            colors.add(Color.parseColor("#f18962"))
            colors.add(Color.parseColor("#d6d6d6"))
            colors.add(requireContext().getColor(R.color.greenn))
            colors.add(Color.parseColor("#6abcec"))
//            colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
        }
        val dataSet = PieDataSet(pieEntry, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 7f
        dataSet.setDrawValues(false)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        viewBinding.pieChart.data = PieData(dataSet)
        viewBinding.pieChart.isDrawHoleEnabled = false
        viewBinding.pieChart.description = Description().apply { text = "" }
        viewBinding.pieChart.invalidate()
        viewBinding.pieChart.animateY(1400, Easing.EaseInOutQuad);
        viewBinding.pieChart.legend.isEnabled = false
        creatChartLabels(colors, chartData)
    }

    private fun creatChartLabels(colors: List<Int>, chartData: ArrayList<PieEntry>) {
        for (i in chartData.indices) {
            val view =
                com.apollopharmacy.vishwam.databinding.LegendLayoutBinding.inflate(layoutInflater)
            view.legendBox.setBackgroundColor(colors[i])
            view.legendTitle.text = chartData[i].label
            viewBinding.chartLabels.addView(view.root)
        }
    }

//    private fun configChartView() {
//
//
//        val pie: Pie = AnyChart.pie()
//        var names = ArrayList<String>()
//        pie.palette().itemAt(
//            2, SolidFill(
//                "#c7c7c7", 1
//            )
//        )
//        pie.palette().itemAt(
//            1, SolidFill(
//                "#FF6D6A", 1
//            )
//        )
//        val dataPieChart: MutableList<DataEntry> = mutableListOf()
//        for (index in count.indices) {
//            dataPieChart.add(ValueDataEntry(lists.elementAt(index), count.elementAt(index)))
//        }
//        pie.labels().format("{%value}")
//
//        pie.animation(true, 800)
//        pie.data(dataPieChart)
//
//        pie.setOnClickListener(object :
//            ListenersInterface.OnClickListener(arrayOf("x", "value")) {
//            override fun onClick(event: Event) {
//                event.getData().get("x")
//                event.getData().get("value")
//
//                if (event.getData().get("x").equals("Approved")) {
//                    //APL49396
//
//
////                    findNavController().navigate(R.id.action_dashboardFragment_to_approvedFragment)
////
//                    val fragment: Fragment = ApprovedFragment()
//                    val fragmentManager = activity!!.supportFragmentManager
//                    val fragmentTransaction = fragmentManager.beginTransaction()
//                    fragmentTransaction.replace(R.id.fragment_container, fragment)
//                    fragmentTransaction.addToBackStack(null)
////                    fragmentTransaction.setReorderingAllowed(true)
////                    fragmentTransaction.isAddToBackStackAllowed
//                    fragmentTransaction.commit()
////                    MainActivity.mInstance.close()
//
//
//                } else if (event.getData().get("x").equals("Pending")) {
//                    val fragment: Fragment = PendingFragment()
//                    val fragmentManager = activity!!.supportFragmentManager
//                    val fragmentTransaction = fragmentManager.beginTransaction()
//                    fragmentTransaction.replace(R.id.fragment_container, fragment)
//                    fragmentTransaction.addToBackStack(null)
//                    fragmentTransaction.commit()
//
//                } else if (event.getData().get("x").equals("Reject")) {
//                    val fragmentManager = activity!!.supportFragmentManager
//                    val fragmentTransaction = fragmentManager.beginTransaction()
//                    fragmentTransaction.replace(R.id.fragment_container, RejectedFragment())
//                    fragmentTransaction.addToBackStack(null)
//                    fragmentTransaction.commit()
//                }
//            }
//        })
//        viewBinding.piechart.setChart(pie)
//
//    }

    override fun onClickRightArrow(row: TicketCountsByStatusRoleResponse.Data.ListData.Row) {
        if (!role.equals("store_executive")) {
            val intent = Intent(ViswamApp.context, DashboardDetailsActivity::class.java)
            intent.putExtra("SELECTED_ITEM", row)
            startActivity(intent)
        }
    }

    var role = ""
    private fun empDetailsMobile() {
        var empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()
        var employeeDetailsResponse: EmployeeDetailsResponse? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(
                empDetailsResponse, EmployeeDetailsResponse::class.java
            )

        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        if (employeeDetailsResponse != null && employeeDetailsResponse!!.data != null && employeeDetailsResponse!!.data!!.role != null && employeeDetailsResponse!!.data!!.role!!.code != null) {
            role = employeeDetailsResponse!!.data!!.role!!.code!!
        }
    }

    var ticketCountsByStatsuRoleResponses: TicketCountsByStatusRoleResponse? = null
    override fun onSuccessgetTicketListByCountApi(ticketCountsByStatsuRoleResponse: TicketCountsByStatusRoleResponse) {

        ticketCountsByStatsuRoleResponses = ticketCountsByStatsuRoleResponse
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.name }
            dashboardAdapter = DashboardAdapter(
                this, ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!
            )
            var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewBinding.dashboardCeoRecyclerview.layoutManager = layoutManager
            viewBinding.dashboardCeoRecyclerview.adapter = dashboardAdapter
            var sumOfClosed = 0f
            var sumOfLessThan2 = 0f
            var sumOfthreeToEight = 0f
            var sumOfgreaterThan8 = 0f
            var sumOfrejected = 0f
            var sumOfpending = 0f
            for (i in ticketCountsByStatsuRoleResponses!!.data.listData.rows) {
                sumOfClosed = sumOfClosed + i.closed!!.toFloat()
            }
            for (i in ticketCountsByStatsuRoleResponses!!.data.listData.rows) {
                sumOfLessThan2 = sumOfLessThan2 + i.lessThan2!!.toFloat()
            }
            for (i in ticketCountsByStatsuRoleResponses!!.data.listData.rows) {
                sumOfthreeToEight = sumOfthreeToEight + i.get3To8()!!.toFloat()
            }
            for (i in ticketCountsByStatsuRoleResponses!!.data.listData.rows) {
                sumOfgreaterThan8 = sumOfgreaterThan8 + i.greaterThan8!!.toFloat()
            }
            for (i in ticketCountsByStatsuRoleResponses!!.data.listData.rows) {
                sumOfrejected = sumOfrejected + i.rejected!!.toFloat()
            }
            for (i in ticketCountsByStatsuRoleResponses!!.data.listData.rows) {
                sumOfpending = sumOfpending + i.pending!!.toFloat()
            }
            var sumOfList: ArrayList<PieEntry> = ArrayList()
            sumOfList.add(PieEntry(sumOfClosed, "Closed"))
            sumOfList.add(PieEntry(sumOfLessThan2, "<2"))
            sumOfList.add(PieEntry(sumOfthreeToEight, "3 to 8"))
            sumOfList.add(PieEntry(sumOfgreaterThan8, ">8"))
            sumOfList.add(PieEntry(sumOfrejected, "Rejeted"))
            sumOfList.add(PieEntry(sumOfpending, "Pending"))
            createChart(sumOfList)

        } else {
            viewBinding.recyclerView.visibility = View.GONE
            viewBinding.noListFound.visibility = View.VISIBLE
        }

        hideLoading()
    }

    override fun onFailuregetTicketListByCountApi(value: TicketCountsByStatusRoleResponse) {
        Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()
        viewBinding.recyclerView.visibility = View.GONE
        viewBinding.noListFound.visibility = View.VISIBLE
        hideLoading()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickStores() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.storesDownArrow.isVisible) {
                viewBinding.storesDownArrow.visibility = View.GONE
                viewBinding.storesUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.name }
            } else {
                viewBinding.storesDownArrow.visibility = View.VISIBLE
                viewBinding.storesUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.name }
            }
            viewBinding.closedDownArrow.visibility = View.GONE
            viewBinding.closedUpArrow.visibility = View.GONE
            viewBinding.lessThanTwoUpArrow.visibility = View.GONE
            viewBinding.lessThanTwoDownArrow.visibility = View.GONE
            viewBinding.threeToEightUpArrow.visibility = View.GONE
            viewBinding.threeToEightDownArrow.visibility = View.GONE
            viewBinding.greaterThanEightUpArrow.visibility = View.GONE
            viewBinding.greayerThanEightDownArrow.visibility = View.GONE
            viewBinding.rejectedUpArrow.visibility = View.GONE
            viewBinding.rejectedDownArrow.visibility = View.GONE
            viewBinding.pendingDownArrow.visibility = View.GONE
            viewBinding.pendingUpArrow.visibility = View.GONE
            viewBinding.totalDownArrow.visibility = View.GONE
            viewBinding.totalUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickClosed() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.closedDownArrow.isVisible) {
                viewBinding.closedDownArrow.visibility = View.GONE
                viewBinding.closedUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.closed }
            } else {
                viewBinding.closedDownArrow.visibility = View.VISIBLE
                viewBinding.closedUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.closed }
            }
            viewBinding.storesDownArrow.visibility = View.GONE
            viewBinding.storesUpArrow.visibility = View.GONE
            viewBinding.lessThanTwoUpArrow.visibility = View.GONE
            viewBinding.lessThanTwoDownArrow.visibility = View.GONE
            viewBinding.threeToEightUpArrow.visibility = View.GONE
            viewBinding.threeToEightDownArrow.visibility = View.GONE
            viewBinding.greaterThanEightUpArrow.visibility = View.GONE
            viewBinding.greayerThanEightDownArrow.visibility = View.GONE
            viewBinding.rejectedUpArrow.visibility = View.GONE
            viewBinding.rejectedDownArrow.visibility = View.GONE
            viewBinding.pendingDownArrow.visibility = View.GONE
            viewBinding.pendingUpArrow.visibility = View.GONE
            viewBinding.totalDownArrow.visibility = View.GONE
            viewBinding.totalUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickLesssThanTwo() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.lessThanTwoDownArrow.isVisible) {
                viewBinding.lessThanTwoDownArrow.visibility = View.GONE
                viewBinding.lessThanTwoUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.lessThan2 }
            } else {
                viewBinding.lessThanTwoDownArrow.visibility = View.VISIBLE
                viewBinding.lessThanTwoUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.lessThan2 }
            }
            viewBinding.storesDownArrow.visibility = View.GONE
            viewBinding.storesUpArrow.visibility = View.GONE
            viewBinding.closedUpArrow.visibility = View.GONE
            viewBinding.closedDownArrow.visibility = View.GONE
            viewBinding.threeToEightUpArrow.visibility = View.GONE
            viewBinding.threeToEightDownArrow.visibility = View.GONE
            viewBinding.greaterThanEightUpArrow.visibility = View.GONE
            viewBinding.greayerThanEightDownArrow.visibility = View.GONE
            viewBinding.rejectedUpArrow.visibility = View.GONE
            viewBinding.rejectedDownArrow.visibility = View.GONE
            viewBinding.pendingDownArrow.visibility = View.GONE
            viewBinding.pendingUpArrow.visibility = View.GONE
            viewBinding.totalDownArrow.visibility = View.GONE
            viewBinding.totalUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickThreetoEight() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.threeToEightDownArrow.isVisible) {
                viewBinding.threeToEightDownArrow.visibility = View.GONE
                viewBinding.threeToEightUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.get3To8() }
            } else {
                viewBinding.threeToEightDownArrow.visibility = View.VISIBLE
                viewBinding.threeToEightUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.get3To8() }
            }
            viewBinding.storesDownArrow.visibility = View.GONE
            viewBinding.storesUpArrow.visibility = View.GONE
            viewBinding.closedUpArrow.visibility = View.GONE
            viewBinding.closedDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoUpArrow.visibility = View.GONE
            viewBinding.greaterThanEightUpArrow.visibility = View.GONE
            viewBinding.greayerThanEightDownArrow.visibility = View.GONE
            viewBinding.rejectedUpArrow.visibility = View.GONE
            viewBinding.rejectedDownArrow.visibility = View.GONE
            viewBinding.pendingDownArrow.visibility = View.GONE
            viewBinding.pendingUpArrow.visibility = View.GONE
            viewBinding.totalDownArrow.visibility = View.GONE
            viewBinding.totalUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickGreaterThanEight() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.greayerThanEightDownArrow.isVisible) {
                viewBinding.greayerThanEightDownArrow.visibility = View.GONE
                viewBinding.greaterThanEightUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.greaterThan8 }
            } else {
                viewBinding.greayerThanEightDownArrow.visibility = View.VISIBLE
                viewBinding.greaterThanEightUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.greaterThan8 }
            }
            viewBinding.storesDownArrow.visibility = View.GONE
            viewBinding.storesUpArrow.visibility = View.GONE
            viewBinding.closedUpArrow.visibility = View.GONE
            viewBinding.closedDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoUpArrow.visibility = View.GONE
            viewBinding.threeToEightDownArrow.visibility = View.GONE
            viewBinding.threeToEightUpArrow.visibility = View.GONE
            viewBinding.rejectedUpArrow.visibility = View.GONE
            viewBinding.rejectedDownArrow.visibility = View.GONE
            viewBinding.pendingDownArrow.visibility = View.GONE
            viewBinding.pendingUpArrow.visibility = View.GONE
            viewBinding.totalDownArrow.visibility = View.GONE
            viewBinding.totalUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickRejected() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.rejectedDownArrow.isVisible) {
                viewBinding.rejectedDownArrow.visibility = View.GONE
                viewBinding.rejectedUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.rejected }
            } else {
                viewBinding.rejectedDownArrow.visibility = View.VISIBLE
                viewBinding.rejectedUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.rejected }
            }
            viewBinding.storesDownArrow.visibility = View.GONE
            viewBinding.storesUpArrow.visibility = View.GONE
            viewBinding.closedUpArrow.visibility = View.GONE
            viewBinding.closedDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoUpArrow.visibility = View.GONE
            viewBinding.threeToEightDownArrow.visibility = View.GONE
            viewBinding.threeToEightUpArrow.visibility = View.GONE
            viewBinding.greaterThanEightUpArrow.visibility = View.GONE
            viewBinding.greayerThanEightDownArrow.visibility = View.GONE
            viewBinding.pendingDownArrow.visibility = View.GONE
            viewBinding.pendingUpArrow.visibility = View.GONE
            viewBinding.totalDownArrow.visibility = View.GONE
            viewBinding.totalUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickPending() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.pendingDownArrow.isVisible) {
                viewBinding.pendingDownArrow.visibility = View.GONE
                viewBinding.pendingUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.pending }
            } else {
                viewBinding.pendingDownArrow.visibility = View.VISIBLE
                viewBinding.pendingUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.pending }
            }
            viewBinding.storesDownArrow.visibility = View.GONE
            viewBinding.storesUpArrow.visibility = View.GONE
            viewBinding.closedUpArrow.visibility = View.GONE
            viewBinding.closedDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoUpArrow.visibility = View.GONE
            viewBinding.threeToEightDownArrow.visibility = View.GONE
            viewBinding.threeToEightUpArrow.visibility = View.GONE
            viewBinding.greaterThanEightUpArrow.visibility = View.GONE
            viewBinding.greayerThanEightDownArrow.visibility = View.GONE
            viewBinding.rejectedUpArrow.visibility = View.GONE
            viewBinding.rejectedDownArrow.visibility = View.GONE
            viewBinding.totalDownArrow.visibility = View.GONE
            viewBinding.totalUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()
    }

    override fun onClickTotal() {
        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            if (viewBinding.totalDownArrow.isVisible) {
                viewBinding.totalDownArrow.visibility = View.GONE
                viewBinding.totalUpArrow.visibility = View.VISIBLE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortByDescending { it.total }
            } else {
                viewBinding.totalDownArrow.visibility = View.VISIBLE
                viewBinding.totalUpArrow.visibility = View.GONE
                ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.total }
            }
            viewBinding.storesDownArrow.visibility = View.GONE
            viewBinding.storesUpArrow.visibility = View.GONE
            viewBinding.closedUpArrow.visibility = View.GONE
            viewBinding.closedDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoDownArrow.visibility = View.GONE
            viewBinding.lessThanTwoUpArrow.visibility = View.GONE
            viewBinding.threeToEightDownArrow.visibility = View.GONE
            viewBinding.threeToEightUpArrow.visibility = View.GONE
            viewBinding.greaterThanEightUpArrow.visibility = View.GONE
            viewBinding.greayerThanEightDownArrow.visibility = View.GONE
            viewBinding.rejectedUpArrow.visibility = View.GONE
            viewBinding.rejectedDownArrow.visibility = View.GONE
            viewBinding.pendingDownArrow.visibility = View.GONE
            viewBinding.pendingUpArrow.visibility = View.GONE
        }
        dashboardAdapter!!.notifyDataSetChanged()
    }
}