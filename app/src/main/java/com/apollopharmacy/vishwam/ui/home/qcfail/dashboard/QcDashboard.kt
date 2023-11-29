package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import android.app.Activity
import android.content.Intent
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
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter.*
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class QcDashboard : BaseFragment<DashBoardViewModel, FragmentQcDashboardBinding>(),
    QcDashBoardCallback, MainActivityCallback {
    private var pendingCountResponseList = ArrayList<PendingCountResponse.Pendingcount>()
    private var designationsList = ArrayList<String>()
    private var dashboardHistoryList = ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>()
    private var distinctPendingCountResponseList = ArrayList<PendingCountResponse.Pendingcount>()
    var dashBaordAdapter: DashBaordAdapter? = null

    var rtoPendencyAdapter: RtoPendencyAdapter? = null
    var desig: String = ""
    var employeId: String = ""
    var isClick: Boolean = false
    var isDataFetched: Boolean = false
    var vishwamDependancyClicked: Boolean = true;
    var isVishwamPendingTab = true;

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
        viewBinding.selectedStatus = 2

//        if(isVishwamPendingTab){
        viewModel.getQcPendingList(
            Preferences.getToken(),
            Preferences.getAppLevelDesignationQCFail()
        )
//        }


        viewBinding.searchView.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))



        viewBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charText = s.toString()

                if (charText.isNotEmpty() && charText.firstOrNull()?.isLetter() == true) {
                    if (charText.length > 3) {
                        viewBinding.rtodashboardrecycleview.visibility = View.GONE
                        viewBinding.noOrderFound.visibility = View.GONE


                        if (dashboardHistoryList.isNullOrEmpty()) {

                        } else {

                            qcDashboardList = dashboardHistoryList.distinctBy { it.empid }.stream()
                                .filter { dashboardHistoryList: Getqcfailpendinghistorydashboard.Pendingcount ->
                                    dashboardHistoryList.empid?.contains(
                                        charText.replace(
                                            " ",
                                            ""
                                        ).toUpperCase().replace(" ", "")
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

            }

        })



        viewModel.qcPendingCountList.observe(viewLifecycleOwner) {
//            Toast.makeText(context, "VishwamPendency refreshed", Toast.LENGTH_SHORT).show()
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
                distinctPendingCountResponseList = pendingCountResponseList.distinctBy { it.empid } as ArrayList<PendingCountResponse.Pendingcount>
                    dashBaordAdapter=context?.let { it1 ->
                        DashBaordAdapter(
                            it1, pendingCountResponseList,
                            arrayList, distinctPendingCountResponseList
                        )
                    }
                viewBinding.dashboardrecycleview.adapter =dashBaordAdapter


            } else {
                viewBinding.dashboardrecycleview.visibility = View.GONE

                viewBinding.noOrderFoundText.visibility = View.VISIBLE
            }


        }


        viewModel.qcPendingHierarchyHistoryList.observe(viewLifecycleOwner) {
            hideLoading()

            if (it.status == true) {

                for (i in it.pendingcount!!.indices) {
                    if (it.pendingcount!!.get(i).empid!!.contains("-")) {
                        viewModel.getQcPendingDashboardHistoryList(
                            it.pendingcount!!.get(i).empid!!.split("-").get(0),
                            it.pendingcount!!.get(i).designation!!
                        )
                    }
                }


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
//            Toast.makeText(context, "RTO refreshed", Toast.LENGTH_SHORT).show()
            if (it.status == true) {
//                Toast.makeText(context, "Refresh done", Toast.LENGTH_SHORT).show()
                if (desig.equals("EXECUTIVE")) {
                    val intent = Intent(context, QcDashboardActivity::class.java)
                    intent.putExtra("empId", employeId)
                    intent.putExtra(
                        "dashboardList",
                        it.pendingcount as ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>
                    )
                    intent.putExtra("designation", desig)
                    startActivityForResult(intent, 221)
                } else {


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

        }

        viewBinding.rtopendency.setOnClickListener {
            viewBinding.selectedStatus = 1
            viewBinding.searchLayout.visibility=View.VISIBLE
            viewBinding.searchLayoutVishwam.visibility=View.GONE

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
            viewBinding.searchLayout.visibility=View.GONE
            viewBinding.searchLayoutVishwam.visibility=View.VISIBLE
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
                    if (dashBaordAdapter != null) {
                        dashBaordAdapter!!.getFilter()!!.filter(editable)
                    }
                } else if (viewBinding.searchViewVishwam.getText().toString()
                        .equals("")
                ) {
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
            Preferences.getToken(),
            Preferences.getAppLevelDesignationQCFail()
        )


    }

    override fun onClick(position: Int, designation: String, empId: String, click: Boolean) {

        showLoading()
        if (qcdashboardHistoryList != null) {
            desig = designation
            employeId = empId
            if (desig.equals("EXECUTIVE")) {
                viewModel.getQcPendingDashboardHistoryList(empId.split("-").get(0), designation)

            }
            else{
                viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation)
            }


//            viewModel.getQcPendingHierarchyHistoryList(empId, designation)
        }


    }

    override fun notify(position: Int, click: Boolean) {

        rtoPendencyAdapter?.notifyDataSetChanged()
    }

    override fun onClickManagerDashBoard(position: Int, designation: String, empId: String) {
        showLoading()
        desig = designation

        employeId = empId
//        if (desig.equals("EXECUTIVE")){
//            val intent=Intent(context,QcDashboardActivity::class.java)
//            intent.putExtra("empId", employeId)
//            intent.putExtra("designation", desig)
//            startActivityForResult(intent, 221)        }

//        viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation)

        viewModel.getQcPendingDashboardHistoryList(empId.split("-").get(0), designation)

    }

    override fun onClickManagerHierarchy(position: Int, designation: String, empId: String) {
        TODO("Not yet implemented")
    }


    override fun onClickExecutive(position: Int, designation: String, empId: String) {
        showLoading()
        desig = designation

        employeId = empId



        if (desig.equals("EXECUTIVE")) {
            viewModel.getQcPendingDashboardHistoryList(empId.split("-").get(0), designation)

        }
        else{
            viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation)
        }
//        viewModel.getQcPendingDashboardHistoryList(empId.split("-").get(0), designation)

    }

    override fun onClickFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
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
        showLoading()

        if (isVishwamPendingTab) {
            designationsList.clear()
            viewModel.getQcPendingList(
                Preferences.getToken(),
                Preferences.getAppLevelDesignationQCFail()
            )
        } else {
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



