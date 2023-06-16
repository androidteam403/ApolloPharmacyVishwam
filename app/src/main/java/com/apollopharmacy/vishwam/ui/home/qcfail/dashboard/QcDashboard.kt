package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentQcDashboardBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter.*
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class QcDashboard : BaseFragment<DashBoardViewModel, FragmentQcDashboardBinding>(),
    QcDashBoardCallback {
    private var pendingCountResponseList = ArrayList<PendingCountResponse.Pendingcount>()
    private var designationsList = ArrayList<String>()
    private var dashboardHistoryList = ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>()

    var rtoPendencyAdapter: RtoPendencyAdapter? = null
    var desig: String = ""
    var employeId: String = ""
    var isClick: Boolean = false
    var isDataFetched: Boolean = false

    var dashboardHierarchyList = ArrayList<Getqcfailpendinghistoryforhierarchy>()
    var getdashboardHierarchyList: List<Getqcfailpendinghistoryforhierarchy>? = null
    var hierarchyList = ArrayList<Getqcfailpendinghistoryforhierarchy>()
    var getHierarchyList: List<Getqcfailpendinghistoryforhierarchy.Pendingcount>? = null
    var qcList = ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()
    var qcDashboardList = ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>()

    var qcdashboardHistoryList = ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>()
    var getdashboardHistoryList: List<Getqcfailpendinghistorydashboard.Pendingcount>? = null

    override val layoutRes: Int
        get() = R.layout.fragment_qc_dashboard

    override fun retrieveViewModel(): DashBoardViewModel {
        return ViewModelProvider(this).get(DashBoardViewModel::class.java)
    }

    override fun setup() {
        showLoading()
//
//        Preferences.savingToken("APL49380")
//        Preferences.setAppLevelDesignationQCFail("GENERAL MANAGER")

        callApi()

        viewModel.getQcPendingList(
            Preferences.getToken(),
            Preferences.getAppLevelDesignationQCFail()
        )

        viewBinding.searchView.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))



        viewBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charText = s.toString()

                if (charText.replace(" ", "").startsWith("AP")) {
                    if (charText.length > 3) {
                        viewBinding.rtodashboardrecycleview.visibility = View.GONE
                        viewBinding.noOrderFound.visibility = View.GONE

                        viewBinding.close.visibility = View.GONE
                        viewBinding.closeArrow.visibility = View.VISIBLE

                        if (dashboardHistoryList.isNullOrEmpty()) {

                        } else {

                            qcDashboardList =
                                dashboardHistoryList.distinctBy { it.empid }.stream()
                                    .filter { dashboardHistoryList: Getqcfailpendinghistorydashboard.Pendingcount ->
                                        dashboardHistoryList.empid?.contains(
                                            charText.replace(
                                                " ",
                                                ""
                                            ).toUpperCase()
                                                .replace(" ", "")
                                        )!!
                                    }
                                    .collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>

                        }


                        if (qcDashboardList.isNullOrEmpty()) {
                            viewBinding.noOrderFound.visibility = View.VISIBLE
                            viewBinding.summaryRecycleView.visibility = View.GONE
                        } else {
                            viewBinding.summaryRecycleView.visibility = View.VISIBLE

                            viewBinding.noOrderFound.visibility = View.GONE


                            viewBinding.summaryRecycleView.adapter =
                                context?.let { DashboardSummaryAdapter(it, qcDashboardList) }
                        }

                    }


                } else {
                    if (charText.length > 3) {
                        viewBinding.rtodashboardrecycleview.visibility = View.GONE


                        if (getHierarchyList.isNullOrEmpty()) {

                        } else {
                            qcList = getHierarchyList?.stream()
                                ?.filter { getHierarchyList: Getqcfailpendinghistoryforhierarchy.Pendingcount ->
                                    getHierarchyList.siteid?.contains(charText.replace(" ", ""))!!
                                }
                                ?.collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                        }




                        viewBinding.close.visibility = View.GONE
                        viewBinding.closeArrow.visibility = View.VISIBLE



                        if (qcList.isNullOrEmpty()) {
                            viewBinding.noOrderFound.visibility = View.VISIBLE

                            viewBinding.searchrecycleview.visibility = View.GONE


                        } else {
                            viewBinding.searchrecycleview.visibility = View.VISIBLE
                            viewBinding.noOrderFound.visibility = View.GONE
                            viewBinding.searchrecycleview.adapter = context?.let {
                                DashboardSearchSitesAdapter(
                                    it,
                                    qcDashboardList,
                                    qcList
                                )
                            }

                        }


                    }
                }

                if (charText.length < 3) {
                    viewBinding.noOrderFound.visibility = View.GONE
                    viewBinding.rtodashboardrecycleview.visibility = View.VISIBLE
                    viewBinding.summaryRecycleView.visibility = View.GONE

                    viewBinding.searchrecycleview.visibility = View.GONE

                }

            }


            override fun afterTextChanged(s: Editable?) {
                viewBinding.closeArrow.setOnClickListener {
                    viewBinding.searchView.setText("")
                    viewBinding.close.visibility = View.VISIBLE

                    viewBinding.noOrderFound.visibility = View.GONE

                    viewBinding.closeArrow.visibility = View.GONE
                }

            }

        })



        viewModel.qcPendingCountList.observe(viewLifecycleOwner) {

            hideLoading()

            designationsList.clear()
            pendingCountResponseList.clear()

            if (it.status == true) {


                viewBinding.dashboardrecycleview.visibility = View.VISIBLE

                if (!it.pendingcount.isNullOrEmpty()) {
                    pendingCountResponseList =
                        it.pendingcount as ArrayList<PendingCountResponse.Pendingcount>
                }

                val designations: List<String> = pendingCountResponseList.stream()
                    .map<String>(PendingCountResponse.Pendingcount::designation).distinct()
                    .collect(Collectors.toList()).reversed()

                for (i in designations.indices) {
                    designationsList.add(designations.get(i))
                }

                viewBinding.dashboardrecycleview.adapter =
                    context?.let { it1 ->
                        DashBaordAdapter(
                            it1, pendingCountResponseList,
                            designationsList
                        )
                    }


            } else {
                viewBinding.dashboardrecycleview.visibility = View.GONE

                viewBinding.noOrderFoundText.visibility = View.VISIBLE
            }


        }


        viewModel.qcPendingHierarchyHistoryList.observe(viewLifecycleOwner) {
            hideLoading()

            if (it.status == true) {


                val getqcfailpendinghistoryforhierarchy: Getqcfailpendinghistoryforhierarchy
                getqcfailpendinghistoryforhierarchy = it
                getdashboardHierarchyList = listOf(getqcfailpendinghistoryforhierarchy)
                for (i in getdashboardHierarchyList!!) {
                    val items = Getqcfailpendinghistoryforhierarchy()
                    items.setemployeId(employeId)
                    items.setemplId(i.pendingcount!![0].empid!!)
                    items.setdesignation(desig)
                    items.pendingcount = i.pendingcount

                    dashboardHierarchyList.add(items)
                    rtoPendencyAdapter?.notifyDataSetChanged()

                }

            }


//            dashboardHierarchyList.stream().flatMap { p -> p.pendingcount?.stream()?.filter { x -> x.rtocount.toString().equals("0") } }.collect(
//                Collectors.toList()
//            )

//            colors.stream()
//                .filter { x -> x != null } // or `Objects::nonNull`
//                .collect(Collectors.toList())


            hierarchyList = dashboardHierarchyList.stream()
                .filter { dashboardHierarchyList: Getqcfailpendinghistoryforhierarchy ->
                    dashboardHierarchyList.designation?.replace(
                        " ",
                        ""
                    )
                        .equals("EXECUTIVE")
                }
                .collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy>


            getHierarchyList =
                hierarchyList.stream().flatMap { p -> p.pendingcount?.stream() }.collect(
                    Collectors.toList()
                )




            rtoPendencyAdapter?.notifyDataSetChanged()

        }

        viewModel.qcPendingDashboardHistoryList.observe(viewLifecycleOwner) {
            hideLoading()
            if (it.status == true) {


                val getqcfailpendinghistorydashboard: Getqcfailpendinghistorydashboard
                getqcfailpendinghistorydashboard = it
                getdashboardHistoryList = getqcfailpendinghistorydashboard.pendingcount





                for (i in getdashboardHistoryList!!) {

                    val items = Getqcfailpendinghistorydashboard.Pendingcount()
                    items.designation = i.designation
                    items.empid = i.empid
                    items.rtocount = i.rtocount
                    items.rtoamount = i.rtoamount
                    items.rrtocount = i.rrtocount
                    items.rrtoamount = i.rrtoamount
                    items.setfetched(true)
                    dashboardHistoryList!!.add(items)
                    rtoPendencyAdapter?.notifyDataSetChanged()


                }



                rtoPendencyAdapter =
                    context?.let { it1 ->
                        dashboardHistoryList?.let { it2 ->
                            RtoPendencyAdapter(
                                it1,
                                this, desig,
                                it2,
                                dashboardHierarchyList
                            )
                        }
                    }


                viewBinding.rtodashboardrecycleview.adapter = rtoPendencyAdapter
                rtoPendencyAdapter?.notifyDataSetChanged()
            }

        }

        viewBinding.rtopendency.setOnClickListener {
            if (getdashboardHistoryList.isNullOrEmpty()) {
                viewBinding.noOrderFoundText.visibility = View.VISIBLE

            } else {
                viewBinding.noOrderFoundText.visibility = View.GONE
                viewBinding.rtodashboardrecycleview.visibility = View.VISIBLE
                viewBinding.searchLayout.visibility = View.VISIBLE
            }

            viewBinding.dashboardrecycleview.visibility = View.GONE

            viewBinding.rtopendency.setBackgroundDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(it1, R.drawable.qc_rtopendency)
            })
            viewBinding.vishwampendency.setBackgroundDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(it1, R.drawable.qc_pendency)
            })

            viewBinding.vishwampendency.setTextColor(Color.parseColor("#FF000000"))


        }

        viewBinding.vishwampendency.setOnClickListener {
            if (pendingCountResponseList.isNullOrEmpty()) {
                viewBinding.noOrderFoundText.visibility = View.VISIBLE
            } else {
                viewBinding.noOrderFoundText.visibility = View.GONE
                viewBinding.dashboardrecycleview.visibility = View.VISIBLE
                viewBinding.searchLayout.visibility = View.GONE


            }
            viewBinding.rtodashboardrecycleview.visibility = View.GONE

            viewBinding.vishwampendency.setBackgroundDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(it1, R.drawable.qc_rtopendency)
            })
            viewBinding.rtopendency.setBackgroundDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(it1, R.drawable.qc_pendency)
            })


            viewBinding.vishwampendency.setTextColor(Color.parseColor("#FF000000"))


        }


    }


    fun callApi() {
//        showLoading()

        viewModel.getQcPendingDashboardHistoryList(
            Preferences.getToken(),
            Preferences.getAppLevelDesignationQCFail()
        )


    }

    override fun onClick(position: Int, designation: String, empId: String, click: Boolean) {

        showLoading()
        if (qcdashboardHistoryList != null) {
            desig = designation
            employeId = empId


            viewModel.getQcPendingHierarchyHistoryList(empId, designation)
        }


    }

    override fun notify(position: Int, click: Boolean) {

        rtoPendencyAdapter?.notifyDataSetChanged()
    }

    override fun onClickManagerDashBoard(position: Int, designation: String, empId: String) {
        showLoading()
        desig = designation

        employeId = empId


        viewModel.getQcPendingDashboardHistoryList(empId, designation)

        viewModel.getQcPendingHierarchyHistoryList(empId, designation)
    }

    override fun onClickManagerHierarchy(position: Int, designation: String, empId: String) {
        TODO("Not yet implemented")
    }


    override fun onClickExecutive(position: Int, designation: String, empId: String) {
        showLoading()
        desig = designation

        employeId = empId



        viewModel.getQcPendingHierarchyHistoryList(empId, designation)

        viewModel.getQcPendingDashboardHistoryList(empId, designation)

    }

}



