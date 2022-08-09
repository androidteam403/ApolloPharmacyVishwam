package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import android.content.Intent
import android.os.Build
import android.text.format.DateFormat.is24HourFormat
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentSwachhListBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.dialog.DatePickerDialog
import com.apollopharmacy.vishwam.dialog.model.Dialog
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.ApproveListActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter.PendingApprovedListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class SwachListFragment : BaseFragment<SwachListViewModel, FragmentSwachhListBinding>(),
    ComplaintListCalendarDialog.DateSelected, Dialog.DateSelecter,
    SwachhListCallback {
    var pendingAndApprovedList = ArrayList<PendingAndApproved>()
    var pendingApprovedListAdapter: PendingApprovedListAdapter? = null
    var isFromDateSelected: Boolean = false
    var fromDate = String()
    var toDate = String()
    var day = 0
    var getApprovedList: List<GetpendingAndApprovedListResponse.GetApproved>? = null
    var getPendingList: List<GetpendingAndApprovedListResponse.GetPending>? = null
    override val layoutRes: Int
        get() = R.layout.fragment_swachh_list

    override fun retrieveViewModel(): SwachListViewModel {
        return ViewModelProvider(this).get(SwachListViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {

        val simpleDateFormat=SimpleDateFormat("dd-MMM-yyy")
        val cal=Calendar.getInstance()
        cal.add(Calendar.DATE,-7)
        val currentDate:String=simpleDateFormat.format(Date())

        viewModel.getPendingAndApprovedListApiCall("APL49396",simpleDateFormat.format(cal.time),currentDate)
        viewBinding.fromDateToDate.setText(""+ simpleDateFormat.format(cal.time) +" to "+ currentDate)

//        val getpendingAndApprovedListRequest = GetpendingAndApprovedListRequest()
//        getpendingAndApprovedListRequest.empid = "APL49396"
//        getpendingAndApprovedListRequest.fromdate = "2022-08-02"
//        getpendingAndApprovedListRequest.todate = "2022-08-06"
//        getpendingAndApprovedListRequest.storeId = "16001"
//        getpendingAndApprovedListRequest.startpageno = 0
//        getpendingAndApprovedListRequest.endpageno = 100


        viewBinding.storeId = Preferences.getSiteId()


        viewBinding.calender.setOnClickListener {
            viewBinding.fromDateToDate.setText("")

            isFromDateSelected = true
            openDateDialog()


        }




        viewModel.getpendingAndApprovedListResponse.observe(viewLifecycleOwner, {
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
                            pendingAndApprovedList.add(pendingAndApproved)

                        }
                    }
                    if (getPendingList != null) {
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
                            pendingAndApprovedList.add(pendingAndApproved)
                        }
                    }
                    pendingApprovedListAdapter =
                        PendingApprovedListAdapter(context, pendingAndApprovedList, this)
                    viewBinding.pendingAndApprovedListRecyclerview.layoutManager =
                        LinearLayoutManager(
                            context
                        )
                    viewBinding.pendingAndApprovedListRecyclerview.adapter =
                        pendingApprovedListAdapter

                }
            }

        })
    }

    override fun onClickUpdate(pendingAndApproved: PendingAndApproved) {
        val intent = Intent(context, ApproveListActivity::class.java)
        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.fromDateToDate.text.toString(),
                    false,
                    viewBinding.fromDateToDate.text.toString()
                )
            }.show(childFragmentManager, "")
        } else {
            Dialog().apply {
                arguments = generateParsedData(
                    viewBinding.fromDateToDate.text.toString(),
                    false,
                    viewBinding.fromDateToDate.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }

    fun openDialog() {

        Dialog().apply {
            arguments = generateParsedData(
                viewBinding.fromDateToDate.text.toString(),
                false,
                viewBinding.fromDateToDate.text.toString()
            )
        }.show(childFragmentManager, "")

    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {


        fromDate = showingDate
        if (isFromDateSelected) {
            openDialog()
        }

    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {

    }

    override fun selectDate(dateSelected: String, showingDate: String) {
        toDate = showingDate

        if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
            pendingAndApprovedList.clear()

            viewBinding.fromDateToDate.setText(fromDate + " to " + toDate)
            viewModel.getPendingAndApprovedListApiCall("APL49396",fromDate,toDate)
            pendingApprovedListAdapter?.notifyDataSetChanged()

        }
    }
}


