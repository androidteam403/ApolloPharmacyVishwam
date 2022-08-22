package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.Preferences.getLoginJson
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.databinding.FragmentSwachhListBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.dialog.model.Dialog
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.ApproveListActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter.PendingApprovedListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter.PendingListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SwachListFragment : BaseFragment<SwachListViewModel, FragmentSwachhListBinding>(),
    ComplaintListCalendarDialog.DateSelected, Dialog.DateSelecter,
    SwachhListCallback {
    var pendingAndApprovedList = ArrayList<PendingAndApproved>()
    var pendingApprovedListAdapter: PendingApprovedListAdapter? = null
    var pendingListAdapter: PendingListAdapter? = null
    var isFromDateSelected: Boolean = false
    var fromDate = String()
    var toDate = String()
    var day = 0
    var userDesignation = ""
    var getApprovedList: List<GetpendingAndApprovedListResponse.GetApproved>? = null
    var getPendingList: List<GetpendingAndApprovedListResponse.GetPending>? = null

    var approvedList = ArrayList<PendingAndApproved>()

    var pendingList = ArrayList<PendingAndApproved>()
    override val layoutRes: Int
        get() = R.layout.fragment_swachh_list

    override fun retrieveViewModel(): SwachListViewModel {
        return ViewModelProvider(this).get(SwachListViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {
        viewBinding.callback = this
        viewBinding.userIdSwachlist.text = Preferences.getToken()
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        val currentDate: String = simpleDateFormat.format(Date())


        fromDate = simpleDateFormat.format(cal.time)
        toDate = currentDate



        val strDate = fromDate
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy");
        val date = dateFormat.parse(strDate)
        val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
        viewBinding.fromDate.text = dateNewFormat

        if(MainActivity.siteIdScreen==true){

        }



        val strDateToDate = toDate
        val dateFormatToDate = SimpleDateFormat("dd-MMM-yyyy");
        val dateToDate = dateFormatToDate.parse(strDateToDate)
        val dateNewFormatToDate = SimpleDateFormat("dd MMM, yyyy").format(dateToDate)
        viewBinding.toDate.text = dateNewFormatToDate



        val loginJson = getLoginJson()
        var loginData: LoginDetails? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            loginData = gson.fromJson(loginJson, LoginDetails::class.java)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        userDesignation = loginData?.APPLEVELDESIGNATION!!

        Utlis.showLoading(requireContext())
        viewModel.getPendingAndApprovedListApiCall(
            Preferences.getValidatedEmpId(),
            fromDate,
            toDate
        )
//        val getpendingAndApprovedListRequest = GetpendingAndApprovedListRequest()
//        getpendingAndApprovedListRequest.empid = "APL49396"
//        getpendingAndApprovedListRequest.fromdate = "2022-08-02"
//        getpendingAndApprovedListRequest.todate = "2022-08-06"
//        getpendingAndApprovedListRequest.storeId = "16001"
//        getpendingAndApprovedListRequest.startpageno = 0
//        getpendingAndApprovedListRequest.endpageno = 100


        viewBinding.storeId = Preferences.getSiteId()
        userDesignation = "EXECUTIVE"
        if (userDesignation.equals("EXECUTIVE")) {
            viewBinding.tabsforexecutive.visibility = View.VISIBLE
        } else {
            viewBinding.tabsforexecutive.visibility = View.GONE
        }

        viewModel.getpendingAndApprovedListResponse.observe(viewLifecycleOwner, {
            Utlis.hideLoading()
            val getpendingAndApprovedListResponse: GetpendingAndApprovedListResponse
            getpendingAndApprovedListResponse = it

            when (getpendingAndApprovedListResponse.status) {
                true -> {
                    when (getpendingAndApprovedListResponse.getApprovedList != null) {
                        true -> {
                            getApprovedList = getpendingAndApprovedListResponse.getApprovedList!!
                        }
                    }
                    when (getpendingAndApprovedListResponse.getPendingList != null) {
                        true -> {
                            getPendingList = getpendingAndApprovedListResponse.getPendingList!!
                        }
                    }
                    pendingAndApprovedList.clear()
                    if (getApprovedList != null) {

                        for (i in getApprovedList!!) {

                            val pendingAndApproved = PendingAndApproved()
                            pendingAndApproved.swachhid = i.swachhid
                            pendingAndApproved.storeId = i.storeId
                            pendingAndApproved.approvedBy = i.approvedBy
                            pendingAndApproved.reshootBy = i.reshootBy
                            pendingAndApproved.partiallyApprovedBy = i.partiallyApprovedBy
                            pendingAndApproved.approvedDate = i.approvedDate
                            pendingAndApproved.reshootDate = i.reshootDate
                            pendingAndApproved.partiallyApprovedDate = i.partiallyApprovedDate
                            pendingAndApproved.isApproved = true
                            pendingAndApproved.uploadedBy = i.uploadedBy
                            pendingAndApproved.uploadedDate = i.uploadedDate
                            pendingAndApproved.status = i.status
//                            approvedList.add(pendingAndApproved)
                            pendingAndApprovedList.add(pendingAndApproved)


                        }
                    }

                    if (getPendingList != null && userDesignation.equals("EXECUTIVE")) {
                        for (i in getPendingList!!) {
                            val pendingAndApproved = PendingAndApproved()
                            pendingAndApproved.swachhid = i.swachhid
                            pendingAndApproved.storeId = i.storeId
                            pendingAndApproved.approvedBy = i.approvedBy
                            pendingAndApproved.reshootBy = i.reshootBy
                            pendingAndApproved.partiallyApprovedBy = i.partiallyApprovedBy
                            pendingAndApproved.approvedDate = i.approvedDate
                            pendingAndApproved.reshootDate = i.reshootDate
                            pendingAndApproved.partiallyApprovedDate = i.partiallyApprovedDate
                            pendingAndApproved.isApproved = false
                            pendingAndApproved.uploadedBy = i.uploadedBy
                            pendingAndApproved.uploadedDate = i.uploadedDate
                            pendingAndApproved.status = i.status
                            pendingList.add(pendingAndApproved)
//                            pendingAndApprovedList.add(pendingAndApproved)
                        }
                    }


                    for (i in pendingAndApprovedList.indices) {
                        if (pendingAndApprovedList.get(i).uploadedDate != "") {
                            val strDate = pendingAndApprovedList.get(i).uploadedDate
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                            val date = dateFormat.parse(strDate)
                            val dateNewFormat =
                                SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                            pendingAndApprovedList.get(i).uploadedDate = dateNewFormat.toString()
                        }
                    }

                    pendingApprovedListAdapter =
                        PendingApprovedListAdapter(context, pendingAndApprovedList, this)
                    viewBinding.approvedListRecyclerview.layoutManager =
                        LinearLayoutManager(
                            context
                        )
                    viewBinding.approvedListRecyclerview.adapter =
                        pendingApprovedListAdapter

                }
            }

        })
    }

    override fun onClickUpdate(pendingAndApproved: PendingAndApproved) {
        val intent = Intent(context, ApproveListActivity::class.java)
        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
        startActivityForResult(intent, ApproveListActivity().APPROVE_LIST_ACTIVITY)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickFromDate() {
        isFromDateSelected = true
        openDateDialog()
    }

    override fun onClickToDate() {
        isFromDateSelected = false
        openDateDialog()
    }

    override fun onClickSearch() {
        Utlis.showLoading(requireContext())
        viewModel.getPendingAndApprovedListApiCall(
            Preferences.getValidatedEmpId(),
            fromDate,
            toDate
        )
    }

    override fun onClickApproved() {
        viewBinding.approvedListButton.setBackgroundColor(Color.parseColor("#D3D3D3"))
        viewBinding.pendingListButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
        if (pendingList != null && pendingAndApprovedList.size > 0) {
            viewBinding.noOrdersFound.visibility = View.GONE
            viewBinding.pendingListRecyclerview.visibility = View.GONE
            viewBinding.approvedListRecyclerview.visibility = View.VISIBLE
            pendingApprovedListAdapter =
                PendingApprovedListAdapter(context, pendingAndApprovedList, this)
            viewBinding.approvedListRecyclerview.layoutManager =
                LinearLayoutManager(
                    context
                )
            viewBinding.approvedListRecyclerview.adapter =
                pendingApprovedListAdapter
        } else {
            viewBinding.pendingListRecyclerview.visibility = View.GONE
            viewBinding.noOrdersFound.visibility = View.VISIBLE
        }

    }

    override fun onClickPending() {
        viewBinding.approvedListButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
        viewBinding.pendingListButton.setBackgroundColor(Color.parseColor("#D3D3D3"))
        if (pendingList != null && pendingList.size > 0) {
            for (i in pendingList.indices) {
                if (pendingList.get(i).uploadedDate != "") {
                    val strDate = pendingList.get(i).uploadedDate
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                    val date = dateFormat.parse(strDate)
                    val dateNewFormat =
                        SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                    pendingList.get(i).uploadedDate = dateNewFormat.toString()
                }
            }
            viewBinding.approvedListRecyclerview.visibility = View.GONE
            viewBinding.pendingListRecyclerview.visibility = View.VISIBLE
            pendingListAdapter =
                PendingListAdapter(context, pendingList, this)
            viewBinding.pendingListRecyclerview.layoutManager =
                LinearLayoutManager(
                    context
                )
            viewBinding.pendingListRecyclerview.adapter =
                pendingListAdapter
        } else {
            viewBinding.approvedListRecyclerview.visibility = View.GONE
            viewBinding.noOrdersFound.visibility = View.VISIBLE

        }


    }

    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    fromDate,
                    false,
                    fromDate
                )
            }.show(childFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    toDate,
                    true,
                    fromDate
                )
            }.show(childFragmentManager, "")
        }
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            if (Utils.getDateDifference(showingDate, toDate) > 0) {

                val strDate = showingDate
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
                viewBinding.fromDate.text = dateNewFormat
//                viewBinding.fromDate.setText(showingDate)
                this.fromDate = showingDate
            } else {
                Toast.makeText(
                    requireContext(),
                    "From date should be less than to date",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            if (Utils.getDateDifference(fromDate, showingDate) > 0) {

                val strDate = showingDate
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
                viewBinding.toDate.text = dateNewFormat

//                viewBinding.toDate.setText(showingDate)
                toDate = showingDate
            } else {
                Toast.makeText(
                    requireContext(),
                    "To date should be greater than from date",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
//        fromDate = showingDate
//        viewBinding.fromDate.text = fromDate
    }

    override fun selectDate(dateSelected: String, showingDate: String) {
//        toDate = showingDate
//
//        if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
//            pendingAndApprovedList.clear()
//
//            viewBinding.fromDate.setText(fromDate)
//            viewBinding.toDate.text = toDate
//            Utlis.showLoading(requireContext())
//            viewModel.getPendingAndApprovedListApiCall(
//                Preferences.getValidatedEmpId(),
//                fromDate,
//                toDate
//            )
//            pendingApprovedListAdapter?.notifyDataSetChanged()

//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApproveListActivity().APPROVE_LIST_ACTIVITY) {
                Utlis.showLoading(requireContext())
                viewModel.getPendingAndApprovedListApiCall(
                    Preferences.getValidatedEmpId(),
                    fromDate,
                    toDate
                )
            }
        }
    }
}


