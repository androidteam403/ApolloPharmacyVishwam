package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentQcDashboardBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter.*
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import java.util.stream.Collectors
import kotlin.streams.toList


class QcDashboard : BaseFragment<DashBoardViewModel, FragmentQcDashboardBinding>(),
    QcDashBoardCallback, MainActivityCallback {
    private var pendingCountResponseList = ArrayList<PendingCountResponse.Pendingcount>()
    private var designationsList = ArrayList<String>()
    private var dashboardHistoryList = ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>()
    private var distinctPendingCountResponseList = ArrayList<PendingCountResponse.Pendingcount>()
    var dashBaordAdapter: DashBaordAdapter? = null
    var rtomanagerAdapter: RtoManagerAdapter? = null
    var rtoexecutiveAdapter: RtoExecutiveAdapter? = null
    var rtoPendencyAdapter: RtoPendencyAdapter? = null
    var desig: String = ""
    var employeId: String = ""
    var isClick: Boolean = false
    var isFirstTime: Boolean = false
    var vishwamDependancyClicked: Boolean = true;
    var isVishwamPendingTab = true;
    var rtoSummaryAdapter: DashboardSummaryAdapter? = null

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
        MainActivity.mInstance.mainActivityCallback = this
        callApi()
        if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                .equals("GENERALMANAGER", true)
        ) {
            viewBinding.selectedStatus = 1
            viewBinding.searchLayout.visibility = View.VISIBLE
            viewBinding.headingLayout.visibility = View.GONE
            viewBinding.searchLayoutVishwam.visibility = View.GONE
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//            )
//            params.weight = 2.0f // Change the value as needed
//            viewBinding.rtopendency.setLayoutParams(params)
            viewBinding.rtopendency.visibility = View.GONE
            viewBinding.vishwampendency.visibility = View.GONE
            viewBinding.dashboardrecycleview.visibility = View.GONE
            viewBinding.rtodashboardrecycleview.visibility = View.VISIBLE
        } else {
            viewModel.getQcPendingList(
                Preferences.getToken(),
                Preferences.getAppLevelDesignationQCFail()
            )
            viewBinding.selectedStatus = 2
            viewBinding.searchLayout.visibility = View.GONE
            viewBinding.headingLayout.visibility = View.VISIBLE
            viewBinding.searchLayoutVishwam.visibility = View.VISIBLE
            viewBinding.rtopendency.visibility = View.VISIBLE
            viewBinding.vishwampendency.visibility = View.VISIBLE
            viewBinding.rtodashboardrecycleview.visibility = View.GONE
            viewBinding.dashboardrecycleview.visibility = View.VISIBLE

        }


        viewBinding.close.setOnClickListener {
            viewBinding.searchView.text!!.clear()
            viewBinding.searchView.setText("")
//            rtoPendencyAdapter?.rtoManagerAdapter!!.getFilter()!!.filter("")

            viewBinding.close.visibility = View.GONE
        }
        viewBinding.closesearchVishwam.setOnClickListener {
            viewBinding.searchViewVishwam.text!!.clear()
            viewBinding.searchViewVishwam.setText("")

            viewBinding.closesearchVishwam.visibility = View.GONE
        }
        viewBinding.searchViewVishwam.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        viewBinding.searchView.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        viewBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charText = s.toString()

                if (charText.replace(" ", "").startsWith("AP")) {
                    if (charText.length > 3) {
                        viewBinding.close.visibility = View.VISIBLE

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
                            viewBinding.rtodashboardrecycleview.visibility=View.GONE

                            viewBinding.noOrderFound.visibility = View.GONE

//                            rtoPendencyAdapter = context?.let { it1 ->
//                                dashboardHistoryList?.let { it2 ->
//                                    RtoPendencyAdapter(
//                                        it1, this, desig, it2, dashboardHierarchyList
//                                    )
//                                }
//                            }
                       rtoSummaryAdapter=     context?.let { DashboardSummaryAdapter(it,this@QcDashboard,desig, qcDashboardList,dashboardHierarchyList) }

                            viewBinding.summaryRecycleView.adapter =rtoSummaryAdapter
                        }

                    }


                } else {
                    if (charText.length > 3) {
                        viewBinding.rtodashboardrecycleview.visibility = View.GONE
                        viewBinding.close.visibility = View.VISIBLE


                        if (getHierarchyList.isNullOrEmpty()) {

                        } else {
                            qcList = getHierarchyList?.stream()
                                ?.filter { getHierarchyList: Getqcfailpendinghistoryforhierarchy.Pendingcount ->
                                    getHierarchyList.siteid?.contains(charText.replace(" ", ""))!!
                                }
                                ?.collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                        }







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
                    viewBinding.close.visibility = View.GONE
                    viewBinding.searchrecycleview.visibility = View.GONE

                }

            }


            override fun afterTextChanged(s: Editable?) {


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
                val sortedList = designationsList.sortedWith(compareByDescending {
                    it == Preferences.getAppLevelDesignationQCFail() // Put items matching the condition first
                })

// Convert the sortedList to an ArrayList
                val arrayList: ArrayList<String> = ArrayList(sortedList)
                distinctPendingCountResponseList =
                    pendingCountResponseList.distinctBy { it.empid } as ArrayList<PendingCountResponse.Pendingcount>
                dashBaordAdapter = context?.let { it1 ->
                    DashBaordAdapter(
                        it1, pendingCountResponseList, arrayList, distinctPendingCountResponseList
                    )
                }
                viewBinding.dashboardrecycleview.adapter = dashBaordAdapter


            } else {
                viewBinding.dashboardrecycleview.visibility = View.GONE

                viewBinding.noOrderFoundText.visibility = View.VISIBLE
            }


        }
        viewBinding.rtopendency.setOnClickListener {
            viewBinding.selectedStatus = 1
            viewBinding.searchLayout.visibility = View.VISIBLE
            viewBinding.searchLayoutVishwam.visibility = View.GONE

            isVishwamPendingTab = false
            if (getdashboardHistoryList.isNullOrEmpty()) {
                viewBinding.noOrderFoundText.visibility = View.VISIBLE

            } else {
                viewBinding.noOrderFoundText.visibility = View.GONE
                viewBinding.rtodashboardrecycleview.visibility = View.VISIBLE
            }

            viewBinding.dashboardrecycleview.visibility = View.GONE


        }
        viewBinding.vishwampendency.setOnClickListener {
            viewBinding.selectedStatus = 2
            viewBinding.searchLayout.visibility = View.GONE
            viewBinding.searchLayoutVishwam.visibility = View.VISIBLE
            isVishwamPendingTab = true
            if (pendingCountResponseList.isNullOrEmpty()) {
                viewBinding.noOrderFoundText.visibility = View.VISIBLE
            } else {
                viewBinding.noOrderFoundText.visibility = View.GONE
                viewBinding.dashboardrecycleview.visibility = View.VISIBLE


            }
            viewBinding.rtodashboardrecycleview.visibility = View.GONE


        }
        viewBinding.searchViewVishwam.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length >= 2) {
                    viewBinding.closesearchVishwam.visibility = View.VISIBLE

                    if (dashBaordAdapter != null) {
                        dashBaordAdapter!!.getFilter()!!.filter(editable)
                    }
                } else if (viewBinding.searchViewVishwam.getText().toString().equals("")) {
                    viewBinding.closesearchVishwam.visibility = View.GONE

                    if (dashBaordAdapter != null) {
                        dashBaordAdapter!!.getFilter()!!.filter("")
                    }
                } else {

                    if (dashBaordAdapter != null) {
                        dashBaordAdapter!!.getFilter()!!.filter("")
                    }
                }
            }
        })


    }


    fun callApi() {


        viewModel.getQcPendingDashboardHistoryList(
            Preferences.getToken(), Preferences.getAppLevelDesignationQCFail(), this
        )


    }

    override fun onClick(position: Int, designation: String, empId: String, click: Boolean) {

        showLoading()
        if (qcdashboardHistoryList != null) {
            desig = designation
            employeId = empId
            viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation, this)
        }
    }

    override fun notify(position: Int, click: Boolean) {

        rtoPendencyAdapter?.notifyDataSetChanged()
    }

    override fun onClickManagerDashBoard(position: Int, designation: String, empId: String) {
        showLoading()
        desig = designation
        employeId = empId
        viewModel.getQcPendingDashboardHistoryList(empId.split("-").get(0), designation, this)
        viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation, this)

    }

    override fun onClickManagerHierarchy(position: Int, hierarchyList:ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>){
        if (hierarchyList.get(position).isSearchClick){
            hierarchyList.get(position).setiisSearchClick(false)
        }else{
            hierarchyList.get(position).setiisSearchClick(true)

        }
        rtoSummaryAdapter!!.rtoManagerAdapter!!.notifyDataSetChanged()
    }

    override fun onClickExecutiveHierarchy(
        position: Int,
        hierarchyList: java.util.ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,
    ) {
        if (hierarchyList.get(position).searchexecutiveClick){
            hierarchyList.get(position).setsearchexecutiveClick(false)
        }else{
            hierarchyList.get(position).setsearchexecutiveClick(true)

        }
        rtoSummaryAdapter!!.rtoManagerAdapter!!.rtoExecutiveAdapter!!.notifyDataSetChanged()    }


    override fun onClickExecutive(position: Int, designation: String, empId: String) {
        showLoading()
        desig = designation

        employeId = empId




        viewModel.getQcPendingDashboardHistoryList(empId.split("-").get(0), designation, this)
        viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation, this)

    }

    override fun onSuccessDashboardHierarchyResponse(value: Getqcfailpendinghistoryforhierarchy) {
        hideLoading()
        val getqcfailpendinghistoryforhierarchy: Getqcfailpendinghistoryforhierarchy
        getqcfailpendinghistoryforhierarchy = value
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

        hierarchyList = dashboardHierarchyList.stream()
            .filter { dashboardHierarchyList: Getqcfailpendinghistoryforhierarchy ->
                dashboardHierarchyList.designation?.replace(
                    " ", ""
                ).equals("EXECUTIVE")
            }.collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy>


        getHierarchyList = hierarchyList.stream().flatMap { p -> p.pendingcount?.stream() }.collect(
            Collectors.toList()
        )

    }

    override fun onSuccessDashboardHistoryResponse(value: Getqcfailpendinghistorydashboard) {
        if (isFirstTime == false) {
            hideLoading()
            isFirstTime = true
        }

        val getqcfailpendinghistorydashboard: Getqcfailpendinghistorydashboard
        getqcfailpendinghistorydashboard = value
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
        rtoPendencyAdapter = context?.let { it1 ->
            dashboardHistoryList?.let { it2 ->
                RtoPendencyAdapter(
                    it1, this, desig, it2, dashboardHierarchyList
                )
            }
        }


        viewBinding.rtodashboardrecycleview.adapter = rtoPendencyAdapter
        rtoPendencyAdapter?.notifyDataSetChanged()

    }

    override fun onSearchClick(
        position: Int,
        list: java.util.ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,
    ) {
        if (list.get(position).isSearchClick){
            list.get(position).setisSearchClick(false)

        }else{
            list.get(position).setisSearchClick(true)

        }
        rtoSummaryAdapter!!.notifyDataSetChanged()
    }


    override fun onClickFilterIcon() {
    }

    override fun onClickSiteIdIcon() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 221) {
            if (resultCode == Activity.RESULT_OK) {

                for (i in dashboardHierarchyList.indices) {
                    for (j in dashboardHierarchyList.get(i).pendingcount!!.indices) {
                        if (dashboardHierarchyList.get(i).pendingcount!!.get(j).empid.equals(
                                employeId
                            )
                        ) {
                            dashboardHierarchyList.get(i).pendingcount!!.get(j).setisClick(false)
                            rtoPendencyAdapter!!.notifyDataSetChanged()
                        }
                    }

                }
            }
        }
    }

    override fun onClickQcFilterIcon() {
        isFirstTime = false
        showLoading()

        if (isVishwamPendingTab) {
            designationsList.clear()
            viewModel.getQcPendingList(
                Preferences.getToken(), Preferences.getAppLevelDesignationQCFail()
            )
        } else {
            viewBinding.rtodashboardrecycleview.visibility = View.VISIBLE
            viewBinding.summaryRecycleView.visibility = View.GONE
            dashboardHierarchyList.clear()
            dashboardHistoryList.clear()
            callApi()
        }
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
        submenus: java.util.ArrayList<MenuModel>?,
        position: Int,
    ) {
    }

    override fun onclickHelpIcon() {
        TODO("Not yet implemented")
    }

}



