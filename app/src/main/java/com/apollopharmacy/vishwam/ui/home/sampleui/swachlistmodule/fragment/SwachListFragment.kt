package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentSwachhListBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.dialog.model.Dialog
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.ApproveListActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter.PendingApprovedListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import java.text.SimpleDateFormat
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

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {
        viewBinding.callback = this
        viewBinding.userIdSwachlist.text=Preferences.getToken()
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyy")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        val currentDate: String = simpleDateFormat.format(Date())


        fromDate = simpleDateFormat.format(cal.time)
        toDate = currentDate

        viewBinding.fromDate.text = fromDate
        viewBinding.toDate.text = toDate

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

    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.fromDate.text.toString(),
                    false,
                    viewBinding.fromDate.text.toString()
                )
            }.show(childFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.toDate.text.toString(),
                    true,
                    viewBinding.fromDate.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            if (Utils.getDateDifference(showingDate, toDate) > 0) {
                viewBinding.fromDate.setText(showingDate)
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
                viewBinding.toDate.setText(showingDate)
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


