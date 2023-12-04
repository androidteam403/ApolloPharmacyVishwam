package com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.View
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
import com.apollopharmacy.vishwam.ui.home.IOnBackPressed
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.XYMarkerView
import com.apollopharmacy.vishwam.ui.home.dashboard.adapter.DashboardAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard.ceodashboardcalenderdialog.CeoDashboardCalenderDialog
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.DashboardDetailsActivity
import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse
import com.apollopharmacy.vishwam.util.Utils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import lecho.lib.hellocharts.model.SliceValue
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import java.util.*


class CeoDashboardFragment : BaseFragment<CeoDashboardViewModel, FragmentCeoDashboardBinding>(),
    CeoDashboardCallback, CeoDashboardCalenderDialog.DateSelected, IOnBackPressed,
    MainActivityCallback {
    private var chart: AnyChartView? = null
    var dashboardAdapter: DashboardAdapter? = null
    private var count = listOf(257, 321, 142)
    var highlightedEntries = java.util.ArrayList<Int>()

    var countApiCall: Int = 0
    val data: MutableList<String> = ArrayList()
    private val lists = listOf("Pending", "Approved", "Reject")
    var pieData: List<SliceValue> = ArrayList()
    var empIdList = java.util.ArrayList<String>()
    public var isEmpty: Boolean = false
    var ticketCountsByStatsuRoleResponses: TicketCountsByStatusRoleResponse? = null
    var statusRoleResponseList = ArrayList<TicketCountsByStatusRoleResponse>()
    var fromDate: String = ""
    var toDate: String = ""
    var isFromDateSelected: Boolean = false
    var empId: String = ""

    override val layoutRes: Int
        get() = R.layout.fragment_ceo_dashboard

    override fun retrieveViewModel(): CeoDashboardViewModel {
        return ViewModelProvider(this).get(CeoDashboardViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        MainActivity.mInstance.mainActivityCallback = this

        empDetailsMobile()
        val data: MutableList<Float> = ArrayList()
        data.add((10.0f))
        data.add((20.0f))
        data.add((70f))
        if (Preferences.getRoleForCeoDashboard().equals("ceo")) {
//            viewBinding.dashboardName.setText("CEO Dashboard")
            viewBinding.dashboardName.setText("Dashboard")
            viewBinding.nameOfTheStore.setText("Region Head")
        } else if (Preferences.getRoleForCeoDashboard().equals("region_head")) {
//            viewBinding.dashboardName.setText("Region Head Dashboard")
            viewBinding.dashboardName.setText("Dashboard")
            viewBinding.nameOfTheStore.setText("Store Manager")
        } else if (Preferences.getRoleForCeoDashboard().equals("store_manager")) {
//            viewBinding.dashboardName.setText("Store Manager Dashboard")
            viewBinding.dashboardName.setText("Dashboard")
            viewBinding.nameOfTheStore.setText("Store Excecutive")
        } else if (Preferences.getRoleForCeoDashboard()
                .equals("store_executive") || Preferences.getRoleForCeoDashboard().equals(null)
        ) {
//            viewBinding.dashboardName.setText("Store Executive Dashboard")
            viewBinding.dashboardName.setText("Dashboard")
            viewBinding.nameOfTheStore.setText("Store List")
        } else if (Preferences.getRoleForCeoDashboard().equals("store_supervisor")) {
            viewBinding.dashboardName.setText("Store Supervisor Dashboard")
        }
        fromDate = Utils.getConvertedDateFormatddmmmyyyy(Utils.getFirstDateOfCurrentMonth())
        toDate = Utils.getConvertedDateFormatddmmmyyyy(Utils.getCurrentDateCeoDashboard())
        viewBinding.fromDate.setText(fromDate)
        viewBinding.toDate.setText(toDate)

//        configChartView()
        viewBinding.greaterThan2.setText("<2")
        viewBinding.greaterThan8.setText(">8")

        showLoading()
        viewModel.getTicketListByCountApi(
            this,
            Utils.getFirstDateOfCurrentMonth(),
            Utils.getCurrentDateCeoDashboard(),
            Preferences.getValidatedEmpId(), ""//"APL67949"
        )



        if (countApiCall == 0) {
            viewBinding.fromDate.isClickable = true
            viewBinding.toDate.isClickable = true
            viewBinding.apply.isClickable = true

            viewBinding.apply.setBackgroundColor(Color.parseColor("#4b9e2e"))
            /* MainActivity.mInstance.backArrow.visibility = View.GONE
             MainActivity.mInstance.openDrawer.visibility = View.VISIBLE*/

        } else {
            viewBinding.fromDate.isClickable = false
            viewBinding.toDate.isClickable = false
            viewBinding.apply.isClickable = false
            viewBinding.apply.setBackgroundColor(Color.parseColor("#a6a6a6"))

            /* MainActivity.mInstance.openDrawer.visibility = View.GONE
             MainActivity.mInstance.backArrow.visibility = View.VISIBLE*/

        }


        /*
                MainActivity.mInstance.backArrow.setOnClickListener {
                    onBackPressed()
                    */
        /* countApiCall--
                     if (countApiCall == 0) {
                         for (i in statusRoleResponseList.indices) {

                             if (Preferences.getValidatedEmpId()
                                     .equals(statusRoleResponseList.get(i).empId)
                             ) {
                                 ticketCountsByStatsuRoleResponses = statusRoleResponseList.get(i)
                                 callAdapter()
                             }

                         }

                     } else {
                         for (i in empIdList.indices) {
                             if (empId.equals(empIdList.get(i))) {
                                 empIdList.removeAt(i)
                             }


                         }
                     }





                     if (empIdList.size > 0 && countApiCall != 0) {
         //            showLoading()
                         val lastIndex = empIdList.size - 1

                         for (i in statusRoleResponseList.indices) {
                             if (empIdList.get(lastIndex).equals(statusRoleResponseList.get(i).getEmpId())) {
                                 ticketCountsByStatsuRoleResponses = statusRoleResponseList.get(i)
                                 callAdapter()
                             }
                         }

                         empIdList.removeAt(lastIndex)
                     }*//*

        }
*/
    }

//        viewModel.getTicketListByCountApi(this, "2023-01-01", "2023-08-02", "APL48627")//EX100011//Preferences.getValidatedEmpId()

    private fun createChart(chartData: ArrayList<PieEntry>, valuesList: ArrayList<String>) {
        val pieEntry = chartData.map { PieEntry(it.value, it.label) }
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

        viewBinding.pieChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {

                creatChartLabels(colors, chartData, (e as PieEntry).label, e!!.y)

                val selectedValue = e?.y?.toInt() ?: 0
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                val mv = XYMarkerView(
                    requireContext(),
                    IndexAxisValueFormatter(valuesList)
                )
                mv.chartView = viewBinding.pieChart // For bounds control


                viewBinding.pieChart.marker = mv

            }

            override fun onNothingSelected() {
                creatChartLabels(colors, chartData, "", 10f)

            }

        })





        viewBinding.pieChart.isDrawHoleEnabled = false
        viewBinding.pieChart.setDrawSliceText(false)
        viewBinding.pieChart.description = Description().apply { text = "" }
        viewBinding.pieChart.notifyDataSetChanged()
        viewBinding.pieChart.invalidate()
        viewBinding.pieChart.animateY(1400, Easing.EaseInOutQuad);
        viewBinding.pieChart.legend.isEnabled = false
        creatChartLabels(colors, chartData, "", 9999999.0f)
    }

    private fun creatChartLabels(
        colors: List<Int>,
        chartData: ArrayList<PieEntry>,
        label: String,
        value: Float,
    ) {


        viewBinding.chartLabels.removeAllViews()
        for (i in chartData.indices) {
            val view =
                com.apollopharmacy.vishwam.databinding.LegendLayoutBinding.inflate(layoutInflater)
            view.legendBox.setBackgroundColor(colors[i])
            if (label.isNotEmpty()) {
                if (label.equals(chartData.get(i).label) && value == chartData.get(i).y) {

                    val desiredWidthInDp = 100 //14
                    val desiredHeightInDp = 50 //14

                    val density = resources.displayMetrics.density
                    val desiredWidthInPx = (desiredWidthInDp * density).toInt()
                    val desiredHeightInPx = (desiredHeightInDp * density).toInt()

                    val layoutParams = view.legendBox.layoutParams
                    layoutParams.width = desiredWidthInPx
                    layoutParams.height = desiredHeightInPx
                    view.legendBox.layoutParams = layoutParams
                    view.legendBox.setText("123")
                    view.legendBox.setTextColor(Color.WHITE)
                    val textColor = Color.BLACK // Use any color you prefer

                    view.legendTitle.setTextColor(textColor)


                    view.legendTitle.textSize = 14F

                } else {
                    view.legendTitle.textSize = 12F
                    view.legendTitle.setTextColor(view.legendTitle.currentTextColor)
                    val desiredWidthInDp = 80 //10
                    val desiredHeightInDp = 40 //10

                    val density = resources.displayMetrics.density
                    val desiredWidthInPx = (desiredWidthInDp * density).toInt()
                    val desiredHeightInPx = (desiredHeightInDp * density).toInt()

                    val layoutParams = view.legendBox.layoutParams
                    layoutParams.width = desiredWidthInPx
                    layoutParams.height = desiredHeightInPx
                    view.legendBox.layoutParams = layoutParams
                    view.legendBox.setText("123")
                    view.legendBox.setTextColor(Color.WHITE)
                }
            }
            view.legendTitle.text = chartData[i].label
            view.legendTitle.setOnClickListener {
                for (j in chartData.indices) {
                    if (chartData[j].label == view.legendTitle.text.toString()) {

                        if (highlightedEntries.contains(j)) {
                            highlightedEntries.remove(j)
                            view.legendTitle.textSize = 12F



                            view.legendTitle.setTextColor(Color.parseColor("#808080"))
                            val desiredWidthInDp = 80 //10
                            val desiredHeightInDp = 30 //10

                            val density = resources.displayMetrics.density
                            val desiredWidthInPx = (desiredWidthInDp * density).toInt()
                            val desiredHeightInPx = (desiredHeightInDp * density).toInt()

                            val layoutParams = view.legendBox.layoutParams
                            layoutParams.width = desiredWidthInPx
                            layoutParams.height = desiredHeightInPx
                            view.legendBox.layoutParams = layoutParams
                            view.legendBox.setText("123")
                            view.legendBox.setTextColor(Color.WHITE)
                        } else {
                            highlightedEntries.add(j)
                        }
                    }
                }

                // Unhighlight all values first
                viewBinding.pieChart.highlightValue(null)

                // Then highlight the selected entries
                for (highlightedIndex in highlightedEntries) {
                    viewBinding.pieChart.highlightValue(highlightedIndex.toFloat(), 0)
                }

            }

            view.legendBox.setText("123")
            view.legendBox.animate()
            viewBinding.chartLabels.addView(view.root)
        }
    }

    fun getDefaultTextColor(context: Context): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
        return typedValue.data
    }

    override fun onClickRightArrow(row: TicketCountsByStatusRoleResponse.Data.ListData.Row) {
        val intent = Intent(ViswamApp.context, DashboardDetailsActivity::class.java)
        intent.putExtra("SELECTED_ITEM", row)
        intent.putExtra("FROM_DATE", viewBinding.fromDate.text.toString())
        intent.putExtra("TO_DATE", viewBinding.toDate.text.toString())
        startActivity(intent)
        /* if (!role.equals("store_executive")) {

         }*/
    }

    override fun onClickEmployee(employee: String, roleCode: String) {
        empId = employee

        /* if (countApiCall < 0) {
             countApiCall = 0

         }

         if (empIdList.contains(employee)) {

         } else {
             countApiCall++

             empIdList.add(employee)
         }*/
        /*if (statusRoleResponseList.filter { it.empId.equals(employee) && !empId.equals(Preferences.getValidatedEmpId())}.size > 0) {
            for (i in statusRoleResponseList.indices) {
                if (employee.equals(statusRoleResponseList.get(i).getEmpId())) {
                    ticketCountsByStatsuRoleResponses = statusRoleResponseList.get(i)
                    callAdapter()
                }
            }
        } else {*/
        showLoading()
        viewModel.getTicketListByCountApi(
            this,
            Utils.getConvertedDateFormatyyyymmdd(viewBinding.fromDate.text.toString()),
            Utils.getConvertedDateFormatyyyymmdd(viewBinding.toDate.text.toString()),
            employee, roleCode//"APL67949"
        )
//        }

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


    fun callAdapter() {
        if (statusRoleResponseList.size == 1) {
            viewBinding.fromDate.isClickable = true
            viewBinding.toDate.isClickable = true
            viewBinding.fromDate.isEnabled = true
            viewBinding.apply.setBackgroundColor(Color.parseColor("#4b9e2e"))

            viewBinding.toDate.isEnabled = true
            viewBinding.apply.isClickable = true
            /* MainActivity.mInstance.backArrow.visibility = View.GONE
             MainActivity.mInstance.openDrawer.visibility = View.VISIBLE*/

        } else {
            viewBinding.fromDate.isClickable = false
            viewBinding.toDate.isClickable = false
            viewBinding.fromDate.isEnabled = false
            viewBinding.toDate.isEnabled = false
            viewBinding.apply.isClickable = false
            viewBinding.apply.setBackgroundColor(Color.parseColor("#a6a6a6"))

            /* MainActivity.mInstance.openDrawer.visibility = View.GONE
             MainActivity.mInstance.backArrow.visibility = View.VISIBLE*/

        }

        if (ticketCountsByStatsuRoleResponses != null && ticketCountsByStatsuRoleResponses!!.data != null && ticketCountsByStatsuRoleResponses!!.data.listData != null && ticketCountsByStatsuRoleResponses!!.data.listData.rows.size > 0) {
            ticketCountsByStatsuRoleResponses!!.data.listData.rows.sortBy { it.name }



            if (ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!.get(0).roleCode.isNullOrEmpty()) {
                viewBinding.nameOfTheStore.text = "Stores"
                viewBinding.dashboardName.setText("Stores Summary")

            } else {

                viewBinding.dashboardName.setText(
                    WordUtils.capitalize(
                        ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!.get(0).roleCode.replace(
                            "_",
                            " "
                        )
                    ) + " Summary"
                )

//                if (ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!.get(0).roleCode.uppercase()
//                        .contains("REGION")
//                ) {
//                    viewBinding.dashboardName.setText("Ceo Dashboard")
//                } else if (ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!.get(0).roleCode.uppercase()
//                        .contains("MANAGER")
//                ) {
//                    viewBinding.dashboardName.setText("RegionHead Dashboard")
//                } else if (ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!.get(0).roleCode.uppercase()
//                        .contains("EXECUTIVE")
//                ) {
//                    viewBinding.dashboardName.setText("Executive Dashboard")
//                }

                viewBinding.nameOfTheStore.text = StringUtils.capitalize(
                    ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!.get(0).roleCode.replace(
                        "_",
                        " "
                    )
                )

            }


            dashboardAdapter =
                DashboardAdapter(this, ticketCountsByStatsuRoleResponses!!.data!!.listData!!.rows!!)
            var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewBinding.dashboardCeoRecyclerview.layoutManager = layoutManager
            viewBinding.dashboardCeoRecyclerview.adapter = dashboardAdapter


            viewBinding.monitoringReportLayout.visibility = View.VISIBLE
            viewBinding.recyclerView.visibility = View.VISIBLE
            viewBinding.noListFound.visibility = View.GONE


            viewBinding.dashboardName.visibility = View.VISIBLE
            viewBinding.pieChart.visibility = View.VISIBLE
            viewBinding.chartLabels.visibility = View.VISIBLE


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

            var valuesList = ArrayList<String>()
            valuesList.add(sumOfClosed.toString())
            valuesList.add(sumOfLessThan2.toString())
            valuesList.add(sumOfthreeToEight.toString())
            valuesList.add(sumOfgreaterThan8.toString())
            valuesList.add(sumOfrejected.toString())
            valuesList.add(sumOfpending.toString())
            createChart(sumOfList, valuesList)


        } else {
            viewBinding.monitoringReportLayout.visibility = View.GONE
            viewBinding.recyclerView.visibility = View.GONE
            viewBinding.noListFound.visibility = View.VISIBLE

            viewBinding.dashboardName.visibility = View.GONE
            viewBinding.pieChart.visibility = View.GONE
            viewBinding.chartLabels.visibility = View.GONE
        }

    }


    override fun onSuccessgetTicketListByCountApi(ticketCountsByStatsuRoleResponse: TicketCountsByStatusRoleResponse) {
        var closed: Int = 0
        var lessthanTwo: Int = 0
        var threetoEight: Int = 0
        var greaterthanEight: Int = 0
        var rejected: Int = 0
        var pending: Int = 0
        var total: Int = 0
        if (ticketCountsByStatsuRoleResponse != null
            && ticketCountsByStatsuRoleResponse!!.data != null
            && ticketCountsByStatsuRoleResponse!!.data!!.listData != null
            && ticketCountsByStatsuRoleResponse!!.data!!.listData!!.rows != null
            && ticketCountsByStatsuRoleResponse!!.data!!.listData!!.rows!!.size > 0
        ) {
            for (i in ticketCountsByStatsuRoleResponse!!.data!!.listData!!.rows!!) {
                closed = closed + i.closed
                lessthanTwo = lessthanTwo + i.lessThan2
                threetoEight = threetoEight + i.get3To8()
                greaterthanEight = greaterthanEight + i.greaterThan8
                var pendingRow: Int = i.lessThan2 + i.get3To8() + i.greaterThan8
                rejected = rejected + i.rejected
                pending = pending + pendingRow
                total = total + i.closed + i.rejected + pendingRow
            }
        }
        viewBinding.closedCard.text = "$closed"
        viewBinding.lessthanTwoCard.text = "$lessthanTwo"
        viewBinding.threeEightCard.text = "$threetoEight"
        viewBinding.greaterthanEightCard.text = "$greaterthanEight"
        viewBinding.rejectedCard.text = "$rejected"
        viewBinding.pendingCard.text = "$pending"
        viewBinding.totalCard.text = "$total"
//        resetChart()
        /* if (empId.isNullOrEmpty()) {
             ticketCountsByStatsuRoleResponse.setEmpId(Preferences.getValidatedEmpId())

         } else {
             ticketCountsByStatsuRoleResponse.setEmpId(empId)

         }*/
        ticketCountsByStatsuRoleResponses = ticketCountsByStatsuRoleResponse
        statusRoleResponseList.add(ticketCountsByStatsuRoleResponse)
        /*  for (i in statusRoleResponseList.indices) {
              if (empId.isNullOrEmpty() && statusRoleResponseList.size == 1) {
                  ticketCountsByStatsuRoleResponses = statusRoleResponseList.get(i)
              } else if (empId.equals(statusRoleResponseList.get(i).getEmpId())) {
                  ticketCountsByStatsuRoleResponses = statusRoleResponseList.get(i)

              }
          }*/

        callAdapter()
        hideLoading()
    }

    private fun resetChart() {
        viewBinding.pieChart.data?.clearValues()
        viewBinding.pieChart.clear()
        viewBinding.pieChart.invalidate()
        viewBinding.pieChart.notifyDataSetChanged()
    }

    override fun onFailuregetTicketListByCountApi(value: TicketCountsByStatusRoleResponse) {
//        Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()

        viewBinding.recyclerView.visibility = View.GONE
        viewBinding.noListFound.visibility = View.VISIBLE
        viewBinding.dashboardName.visibility = View.GONE
        viewBinding.pieChart.visibility = View.GONE
        viewBinding.chartLabels.visibility = View.GONE
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

    override fun onClickFromDate() {
        isFromDateSelected = true
        openDateDialog()
    }

    override fun onClickToDate() {
        isFromDateSelected = false
        openDateDialog()
    }

    fun openDateDialog() {
        if (isFromDateSelected) {
            CeoDashboardCalenderDialog().apply {
                arguments = generateParsedData(
                    viewBinding.fromDate.text.toString(),
                    false,
                    viewBinding.fromDate.text.toString(),
                    viewBinding.toDate.text.toString()
                )
            }.show(childFragmentManager, "")
        } else {
            CeoDashboardCalenderDialog().apply {
                arguments = generateParsedData(
                    viewBinding.toDate.text.toString(),
                    true,
                    viewBinding.fromDate.text.toString(),
                    viewBinding.toDate.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }

    override fun onClickApplyDate() {
        showLoading()
        if (statusRoleResponseList != null && statusRoleResponseList.size == 1) {
            for (i in statusRoleResponseList.indices) {
                if (statusRoleResponseList[i].empId.equals(Preferences.getValidatedEmpId())) {
                    statusRoleResponseList.removeAt(i)
                }
            }
            viewModel.getTicketListByCountApi(
                this,
                Utils.getConvertedDateFormatyyyymmdd(viewBinding.fromDate.text.toString()),
                Utils.getConvertedDateFormatyyyymmdd(viewBinding.toDate.text.toString()),
                Preferences.getValidatedEmpId(), ""//"APL67949"
            )
        }


        /*if (empId.isNullOrEmpty() && statusRoleResponseList.size > 0) {
            if (Utils.getFirstDateOfCurrentMonth()
                    .equals(Utils.getConvertedDateFormatyyyymmdd(viewBinding.fromDate.text.toString())) && Utils.getCurrentDateCeoDashboard()
                    .equals(Utils.getConvertedDateFormatyyyymmdd(viewBinding.toDate.text.toString()))
            ) {
                hideLoading()
                callAdapter()
            } else if (statusRoleResponseList.isNotEmpty()) {
                for (i in statusRoleResponseList.indices) {
                    if (statusRoleResponseList[i].empId.equals(Preferences.getValidatedEmpId())) {
                        statusRoleResponseList.removeAt(i)
                    }
                }
                viewModel.getTicketListByCountApi(
                    this,
                    Utils.getConvertedDateFormatyyyymmdd(viewBinding.fromDate.text.toString()),
                    Utils.getConvertedDateFormatyyyymmdd(viewBinding.toDate.text.toString()),
                    Preferences.getValidatedEmpId(), ""//"APL67949"
                )
            }
        } else {
            viewModel.getTicketListByCountApi(
                this,
                Utils.getConvertedDateFormatyyyymmdd(viewBinding.fromDate.text.toString()),
                Utils.getConvertedDateFormatyyyymmdd(viewBinding.toDate.text.toString()),
                Preferences.getValidatedEmpId(), ""//"APL67949"
            )
        }*/
    }

    override fun onCLickRowtoShowTickets(row: TicketCountsByStatusRoleResponse.Data.ListData.Row) {
        MainActivity.mInstance.displaySelectedScreenFromCeoDashboard("DASHBOARD_TICKET_LIST", row)
    }

    override fun selectedDateTo(
        dateSelected: String,
        showingDate: String,
        toDateFormatted: String,
    ) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            viewBinding.fromDate.setText(showingDate)
            viewBinding.toDate.setText(toDateFormatted)
            val fromDate = viewBinding.fromDate.text.toString()
            val toDate = viewBinding.toDate.text.toString()
            if (Utils.getDateDifference(toDate, Utils.getCurrentDate()) == 0) {
                viewBinding.toDate.setText(Utils.getCurrentDate())
            }

        } else {
            viewBinding.toDate.setText(showingDate)
        }
        /* showLoading()
         viewModel.getTicketListByCountApi(
             this,
             Utils.getConvertedDateFormatyyyymmdd(viewBinding.fromDate.text.toString()),
             Utils.getConvertedDateFormatyyyymmdd(viewBinding.toDate.text.toString()),
             Preferences.getValidatedEmpId()//"APL67949"
         )*/
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {

    }

    override fun onBackPressed(): Boolean {
        if (statusRoleResponseList.size == 1) {
            return false
        } else if (statusRoleResponseList.size > 1) {
            statusRoleResponseList.remove(statusRoleResponseList.get(statusRoleResponseList.size - 1))
            ticketCountsByStatsuRoleResponses =
                statusRoleResponseList.get(statusRoleResponseList.size - 1)
            callAdapter()
            return true
        } else {
            return false
        }


        /*  countApiCall--
          if (countApiCall == 0) {
              for (i in statusRoleResponseList.indices) {

                  if (Preferences.getValidatedEmpId().equals(statusRoleResponseList.get(i).empId)) {
                      ticketCountsByStatsuRoleResponses = statusRoleResponseList.get(i)
                      callAdapter()
                  }

              }
          } else {
              for (i in empIdList.indices) {
                  if (empId.equals(empIdList.get(i))) {
                      empIdList.removeAt(i)
                  }


              }
          }





          if (empIdList.size > 0 && countApiCall != 0) {
  //            showLoading()
              val lastIndex = empIdList.size - 1

              for (i in statusRoleResponseList.indices) {
                  if (empIdList.get(lastIndex).equals(statusRoleResponseList.get(i).getEmpId())) {
                      ticketCountsByStatsuRoleResponses = statusRoleResponseList.get(i)
                      callAdapter()
                  }
              }

              empIdList.removeAt(lastIndex)
              return true
          } else if (countApiCall < 0) {
              return false
          }
          return true*/
    }

    override fun onClickFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onSelectApprovedFragment(listSize: String?) {
        TODO("Not yet implemented")
    }

    override fun onSelectRejectedFragment() {
        TODO("Not yet implemented")
    }

    override fun onSelectPendingFragment() {
        TODO("Not yet implemented")
    }

    override fun onClickSpinnerLayout() {
        TODO("Not yet implemented")
    }

    override fun onClickSubmenuItem(
        menuName: String?,
        submenus: ArrayList<MenuModel>?,
        position: Int,
    ) {

    }

    override fun onclickHelpIcon() {
        TODO("Not yet implemented")
    }
}