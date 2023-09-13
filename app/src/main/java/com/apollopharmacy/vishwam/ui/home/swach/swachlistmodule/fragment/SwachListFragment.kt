package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.Preferences.getLoginJson
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.databinding.DialogFilterListBinding
import com.apollopharmacy.vishwam.databinding.FragmentSwachhListBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.dialog.DeleteSiteDialog
import com.apollopharmacy.vishwam.dialog.model.Dialog
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.ApproveListActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.adapter.PendingApprovedListAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.adapter.PendingListAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.adapter.SiteIdDisplayAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.SelectSiteActivityy
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.reviewratingactivity.RatingReviewActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SwachListFragment : BaseFragment<SwachListViewModel, FragmentSwachhListBinding>(),
    ComplaintListCalendarDialog.DateSelected, Dialog.DateSelecter,
    SwachhListCallback, MainActivityCallback, DeleteSiteDialog.OnSiteClickListener {
    var pendingAndApprovedList = ArrayList<PendingAndApproved>()

    var approveddList = ArrayList<PendingAndApproved>()

    //    var pendingApprovedListAdapter: PendingApprovedListAdapter? = null
    private var pendingApprovedListAdapter: PendingApprovedListAdapter? = null
    private lateinit var pendingListAdapter: PendingListAdapter

    //    var pendingListAdapter: PendingListAdapter? = null
    var isFromDateSelected: Boolean = false
    var fromDate = String()
    var toDate = String()
    var isApprovedTab: Boolean = true
    var startPageApproved: Int = 0
    var endPageNumApproved: Int = 10
    var isfilterClicked: Boolean = false
    var startPagePending: Int = 0
    var endPageNumPending: Int = 10
    var handler: Handler = Handler()
    var isPendingTab: Boolean = false
    var isdateFormatted: Boolean = false
    private var isLoadingApproved: Boolean = false
    private var isFirstTimeApproved: Boolean = true
    private var isLoadingPending: Boolean = false
    private var isFirstTimePending: Boolean = true
    var selectedSiteids: String? = null
    var day = 0
    var selectsiteIdList = java.util.ArrayList<String>()
    var userDesignation = ""
    var siteIdDisplayAdapter: SiteIdDisplayAdapter? = null
    var getApprovedList: List<GetpendingAndApprovedListResponse.GetApproved>? = null
    var getPendingList: List<GetpendingAndApprovedListResponse.GetPending>? = null
    var list: List<String> = ArrayList()
    var approvedList = ArrayList<PendingAndApproved>()
    private lateinit var dialog: Dialog
    lateinit var layoutManagerApproved: LinearLayoutManager
    lateinit var layoutManagerPending: LinearLayoutManager
    var pendingList = ArrayList<PendingAndApproved>()
    override val layoutRes: Int
        get() = R.layout.fragment_swachh_list
    var complaintListStatus: String = "Pending,Reshoot,Partially Approved"

    override fun retrieveViewModel(): SwachListViewModel {
        return ViewModelProvider(this).get(SwachListViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this
        viewBinding.callback = this
        viewBinding.userIdSwachlist.text = Preferences.getToken()
//        selectedSiteids= Preferences.getSiteId()

        layoutManagerPending = LinearLayoutManager(context)
        //attaches LinearLayoutManager with RecyclerView
        viewBinding.pendingListRecyclerview.layoutManager = layoutManagerPending
//
//
        layoutManagerApproved = LinearLayoutManager(context)
        //attaches LinearLayoutManager with RecyclerView
        viewBinding.approvedListRecyclerview.layoutManager = layoutManagerApproved

//
//        viewBinding.pendingListRecyclerview.layoutManager =
//            LinearLayoutManager(
//                context
//            )


//
//

//        val i = getIntent()
//        list = i.getSerializableExtra("selectsiteIdList") as List<String>
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        val currentDate: String = simpleDateFormat.format(Date())


        fromDate = simpleDateFormat.format(cal.time)
        toDate = currentDate


        val strDate = fromDate
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        val date = dateFormat.parse(strDate)
        val dateNewFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(date)
        viewBinding.fromDate.text = dateNewFormat


        val strDateToDate = toDate
        val dateFormatToDate = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        val dateToDate = dateFormatToDate.parse(strDateToDate)
        val dateNewFormatToDate = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(dateToDate)
        viewBinding.toDate.text = dateNewFormatToDate


        val loginJson = getLoginJson()
        var loginData: LoginDetails? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            loginData = gson.fromJson(loginJson, LoginDetails::class.java)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }

        userDesignation = Preferences.getAppLevelDesignationSwach()
//       Toast.makeText(context, ""+ userDesignation, Toast.LENGTH_SHORT).show()
        viewBinding.pullToRefreshApproved.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClickApproved()
        })

        viewBinding.pullToRefreshPending.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClickPending()
        })

        Utlis.showLoading(requireContext())
        selectedSiteids = TextUtils.join(",", selectsiteIdList)
        callAPI(startPageApproved, endPageNumApproved, isApprovedTab)

//        val getpendingAndApprovedListRequest = GetpendingAndApprovedListRequest()
//        getpendingAndApprovedListRequest.empid = "APL49396"
//        getpendingAndApprovedListRequest.fromdate = "2022-08-02"
//        getpendingAndApprovedListRequest.todate = "2022-08-06"
//        getpendingAndApprovedListRequest.storeId = "16001"
//        getpendingAndApprovedListRequest.startpageno = 0
//        getpendingAndApprovedListRequest.endpageno = 100

        siteIdDisplayAdapter =
            SiteIdDisplayAdapter(context, selectsiteIdList, this)
        viewBinding.storeIdsRecyclerView.layoutManager =
            LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )


        viewBinding.storeId = Preferences.getSiteId()
//    userDesignation = "EXECUTIVE"
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
                            getApprovedList = getpendingAndApprovedListResponse.getApprovedList
                        }
                        else -> {}
                    }

                    when (getpendingAndApprovedListResponse.getPendingList != null) {
                        true -> {
                            getPendingList = getpendingAndApprovedListResponse.getPendingList
                        }
                        else -> {}
                    }

                    if (isApprovedTab && isLoadingApproved && pendingAndApprovedList.size != null && pendingAndApprovedList.size > 0) {
                        pendingAndApprovedList.removeAt(pendingAndApprovedList.size - 1)
                    } else if (!isApprovedTab && isLoadingPending && pendingList.size != null && pendingList.size > 0) {
                        pendingList.removeAt(pendingList.size - 1)
                    }
//                    pendingAndApprovedList.clear()
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

//                        if(pendingAndApprovedList.size>0){
//                            pendingApprovedListAdapter?.getData()
//                                ?.addAll(pendingAndApprovedList)
//                        }


                    }
//                    pendingList.clear()
                    if (getPendingList != null && userDesignation.equals("EXECUTIVE")) {
                        for (i in getPendingList!!) {
//                            "Pending,Reshoot,Partially Approved"
//                            val pendingAndApproved = PendingAndApproved()
//                            if (complaintListStatus.equals("Pending")) {
//                                if (i.status!!.equals("Pending")) {
//
//                                    pendingAndApproved.swachhid = i.swachhid
//                                    pendingAndApproved.storeId = i.storeId
//                                    pendingAndApproved.approvedBy = i.approvedBy
//                                    pendingAndApproved.reshootBy = i.reshootBy
//                                    pendingAndApproved.partiallyApprovedBy = i.partiallyApprovedBy
//                                    pendingAndApproved.approvedDate = i.approvedDate
//                                    pendingAndApproved.reshootDate = i.reshootDate
//                                    pendingAndApproved.partiallyApprovedDate =
//                                        i.partiallyApprovedDate
//                                    pendingAndApproved.isApproved = false
//                                    pendingAndApproved.uploadedBy = i.uploadedBy
//                                    pendingAndApproved.uploadedDate = i.uploadedDate
//                                    pendingAndApproved.status = i.status
//                                    pendingList.add(pendingAndApproved)
//                                }
//
//                            }
//                            if (complaintListStatus.equals("Reshoot")) {
//                                if (i.status!!.equals("Reshoot")) {
//
//                                    pendingAndApproved.swachhid = i.swachhid
//                                    pendingAndApproved.storeId = i.storeId
//                                    pendingAndApproved.approvedBy = i.approvedBy
//                                    pendingAndApproved.reshootBy = i.reshootBy
//                                    pendingAndApproved.partiallyApprovedBy = i.partiallyApprovedBy
//                                    pendingAndApproved.approvedDate = i.approvedDate
//                                    pendingAndApproved.reshootDate = i.reshootDate
//                                    pendingAndApproved.partiallyApprovedDate =
//                                        i.partiallyApprovedDate
//                                    pendingAndApproved.isApproved = false
//                                    pendingAndApproved.uploadedBy = i.uploadedBy
//                                    pendingAndApproved.uploadedDate = i.uploadedDate
//                                    pendingAndApproved.status = i.status
//                                    pendingList.add(pendingAndApproved)
//                                }
//                            }
//
//                            if (complaintListStatus.equals("Partially Approved")) {
//                                if (i.status!!.equals("Partially Approved")) {
//                                    pendingAndApproved.swachhid = i.swachhid
//                                    pendingAndApproved.storeId = i.storeId
//                                    pendingAndApproved.approvedBy = i.approvedBy
//                                    pendingAndApproved.reshootBy = i.reshootBy
//                                    pendingAndApproved.partiallyApprovedBy = i.partiallyApprovedBy
//                                    pendingAndApproved.approvedDate = i.approvedDate
//                                    pendingAndApproved.reshootDate = i.reshootDate
//                                    pendingAndApproved.partiallyApprovedDate =
//                                        i.partiallyApprovedDate
//                                    pendingAndApproved.isApproved = false
//                                    pendingAndApproved.uploadedBy = i.uploadedBy
//                                    pendingAndApproved.uploadedDate = i.uploadedDate
//                                    pendingAndApproved.status = i.status
//                                    pendingList.add(pendingAndApproved)
//                                }
//                            }
                            if (complaintListStatus.contains(i.status!!)) {
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
                            }


//                            pendingAndApprovedList.add(pendingAndApproved)
                        }

//                        pendingApprovedListAdapter?.getData()
//                            ?.addAll(approveddList)

                    }


                    if (isApprovedTab) {

                        viewBinding.approvedListButton.setBackgroundColor(Color.parseColor("#2582a1"))
                        viewBinding.pendingListButton.setBackgroundColor(Color.parseColor("#a9a9a9"))
//
                        if (pendingAndApprovedList != null && pendingAndApprovedList.size > 0) {

                            for (i in pendingAndApprovedList.indices) {
                                if (!pendingAndApprovedList.get(i).isDateformat!!) {
                                    if (pendingAndApprovedList.get(i).uploadedDate != "" && pendingAndApprovedList.get(
                                            i).uploadedDate != null
                                    ) {
                                        val strDate = pendingAndApprovedList.get(i).uploadedDate
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
                                        val date = dateFormat.parse(strDate)
                                        val dateNewFormat =
                                            SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.ENGLISH).format(date)
                                        pendingAndApprovedList.get(i).uploadedDate =
                                            dateNewFormat.toString()
                                        pendingAndApprovedList.get(i).isDateformat = true

                                    }
                                }

                            }
                        }

                        Utlis.hideLoading()
                        if (viewBinding.pullToRefreshApproved.isRefreshing) {
                            viewBinding.pullToRefreshApproved.isRefreshing = false
                        } else {
                            if (isLoadingApproved) {
                                hideLoading()
//                                pendingApprovedListAdapter?.getData()
//                                    ?.addAll(pendingAndApprovedList)
                                pendingApprovedListAdapter?.notifyDataSetChanged()


                                if (pendingApprovedListAdapter?.getData() != null && pendingApprovedListAdapter?.getData()?.size!! > 0) {
                                    viewBinding.approvedListRecyclerview.visibility = View.VISIBLE
                                    viewBinding.noOrdersFound.visibility = View.GONE
                                    viewBinding.pullToRefreshPending.visibility = View.GONE
                                    viewBinding.pullToRefreshApproved.visibility = View.VISIBLE

                                } else {
                                    viewBinding.approvedListRecyclerview.visibility = View.GONE
                                    viewBinding.pullToRefreshPending.visibility = View.GONE
                                    viewBinding.pullToRefreshApproved.visibility = View.GONE
                                    viewBinding.noOrdersFound.visibility = View.VISIBLE
//                                    Toast.makeText(context, "pendingApprovedListAdapter?.getData()?.size!!", Toast.LENGTH_SHORT).show()
                                }
                                isLoadingApproved = false
                            } else {
                                if (pendingAndApprovedList != null && pendingAndApprovedList?.size!! > 0) {
                                    viewBinding.noOrdersFound.visibility = View.GONE
                                    viewBinding.pendingListRecyclerview.visibility = View.GONE
                                    viewBinding.approvedListRecyclerview.visibility = View.VISIBLE


                                    viewBinding.pullToRefreshPending.visibility = View.GONE
                                    viewBinding.pullToRefreshApproved.visibility = View.VISIBLE

//                                    pendingApprovedListAdapter = PendingApprovedListAdapter(context, pendingAndApprovedList, this)
//                                    viewBinding.approvedListRecyclerview.layoutManager = LinearLayoutManager(context)
//                                    viewBinding.approvedListRecyclerview.adapter =
//                                        pendingApprovedListAdapter
                                    pendingApprovedListAdapter =
                                        PendingApprovedListAdapter(context,
                                            pendingAndApprovedList,
                                            this
                                        )
//                                   layoutManagerApproved = LinearLayoutManager(ViswamApp.context)
                                    viewBinding.approvedListRecyclerview.layoutManager =
                                        layoutManagerApproved
                                    viewBinding.approvedListRecyclerview.adapter =
                                        pendingApprovedListAdapter
                                } else {
                                    viewBinding.approvedListRecyclerview.visibility = View.GONE
                                    viewBinding.pullToRefreshPending.visibility = View.GONE
                                    viewBinding.pullToRefreshApproved.visibility = View.GONE
                                    viewBinding.noOrdersFound.visibility = View.VISIBLE
                                }


                            }
                        }


//
//
//
//
//
//                        if (pendingAndApprovedList != null && pendingAndApprovedList.size > 0) {
//                            viewBinding.noOrdersFound.visibility = View.GONE
//
//                            for (i in pendingAndApprovedList.indices) {
//                                if (pendingAndApprovedList.get(i).uploadedDate != "") {
//                                    val strDate = pendingAndApprovedList.get(i).uploadedDate
//                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
//                                    val date = dateFormat.parse(strDate)
//                                    val dateNewFormat =
//                                        SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
//                                    pendingAndApprovedList.get(i).uploadedDate =
//                                        dateNewFormat.toString()
//
//                                }
//                            }
//                            viewBinding.pendingListRecyclerview.visibility = View.GONE
//                            viewBinding.approvedListRecyclerview.visibility = View.VISIBLE
//                            pendingApprovedListAdapter = PendingApprovedListAdapter(context, pendingAndApprovedList, this, loginData.EMPNAME)
//                            viewBinding.approvedListRecyclerview.layoutManager = LinearLayoutManager(context)
//                            viewBinding.approvedListRecyclerview.adapter =
//                                pendingApprovedListAdapter
//                        } else {
//                            viewBinding.pendingListRecyclerview.visibility = View.GONE
//                            viewBinding.approvedListRecyclerview.visibility = View.GONE
//                            viewBinding.noOrdersFound.visibility = View.VISIBLE
//                        }
                    } else {

                        viewBinding.approvedListButton.setBackgroundColor(Color.parseColor("#a9a9a9"))
                        viewBinding.pendingListButton.setBackgroundColor(Color.parseColor("#2582a1"))
                        if (pendingList != null && pendingList.size > 0) {
                            for (i in pendingList.indices) {
                                if (pendingList.get(i).isDateformat == false) {
                                    if (pendingList.get(i).uploadedDate != "" && pendingList.get(i).uploadedDate != null) {
                                        val strDate = pendingList.get(i).uploadedDate
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
                                        val date = dateFormat.parse(strDate)
                                        val dateNewFormat =
                                            SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.ENGLISH).format(date)
                                        pendingList.get(i).uploadedDate = dateNewFormat.toString()
                                        pendingList.get(i).isDateformat = true

                                    }
                                }

                            }
                        }

//
                        Utlis.hideLoading()
                        if (viewBinding.pullToRefreshPending.isRefreshing) {
                            viewBinding.pullToRefreshPending.isRefreshing = false
                        } else {

                            if (isLoadingPending == true) {
//                               pendingList.removeAt(pendingList.size-1)

                                hideLoading()

//                                pendingListAdapter.getData()
//                                    .addAll(pendingList)
                                pendingListAdapter.notifyDataSetChanged()

                                if (pendingListAdapter?.getData() != null && pendingListAdapter.getData().size > 0) {
                                    viewBinding.pendingListRecyclerview.visibility = View.VISIBLE
                                    viewBinding.noOrdersFound.visibility = View.GONE

                                    viewBinding.pullToRefreshApproved.visibility = View.GONE
                                    viewBinding.pullToRefreshPending.visibility = View.VISIBLE
                                } else {
                                    viewBinding.pendingListRecyclerview.visibility = View.GONE
                                    viewBinding.pullToRefreshApproved.visibility = View.GONE
                                    viewBinding.pullToRefreshPending.visibility = View.GONE
                                    viewBinding.noOrdersFound.visibility = View.VISIBLE
                                }
                                isLoadingPending = false
                            } else {
                                if (pendingList.size > 0) {
                                    viewBinding.approvedListRecyclerview.visibility = View.GONE
                                    viewBinding.noOrdersFound.visibility = View.GONE
                                    viewBinding.pendingListRecyclerview.visibility = View.VISIBLE

                                    viewBinding.pullToRefreshApproved.visibility = View.GONE
                                    viewBinding.pullToRefreshPending.visibility = View.VISIBLE

                                    pendingListAdapter =
                                        PendingListAdapter(context, pendingList, this)
                                    viewBinding.pendingListRecyclerview.layoutManager =
                                        layoutManagerPending
                                    //LinearLayoutManager(context)
                                    viewBinding.pendingListRecyclerview.adapter =
                                        pendingListAdapter


//                                    pendingListAdapter =
//                                        PendingListAdapter(context, pendingList, this)
//                                    viewBinding.pendingListRecyclerview.layoutManager =
//                                        LinearLayoutManager(
//                                            context
//                                        )
//                                    viewBinding.pendingListRecyclerview.adapter =
//                                        pendingListAdapter

//                                    pendingListAdapter = PendingListAdapter(context,
//                                        pendingList,
//                                        this)
//                                    layoutManagerPending = LinearLayoutManager(ViswamApp.context)
//                                    viewBinding.pendingListRecyclerview.layoutManager =
//                                        layoutManagerPending
//                                    viewBinding.pendingListRecyclerview.itemAnimator =
//                                        DefaultItemAnimator()
//                                    viewBinding.pendingListRecyclerview.adapter =
//                                        pendingListAdapter
////

//
////
////                                    pendingListAdapter =
////                                        PendingListAdapter(context, pendingList, this, loginData.EMPNAME)
////                                    layoutManagerPending = LinearLayoutManager(ViswamApp.context)
////                                    viewBinding.pendingListRecyclerview.layoutManager = layoutManagerPending
////                                    viewBinding.pendingListRecyclerview.adapter = pendingListAdapter
                                } else {
                                    viewBinding.pendingListRecyclerview.visibility = View.GONE
                                    viewBinding.approvedListRecyclerview.visibility = View.GONE
                                    viewBinding.pullToRefreshApproved.visibility = View.GONE
                                    viewBinding.pullToRefreshPending.visibility = View.GONE
                                    viewBinding.noOrdersFound.visibility = View.VISIBLE

                                }


                            }
                        }


//                        if (pendingList != null && pendingList.size > 0) {
//                            viewBinding.approvedListRecyclerview.visibility = View.GONE
//                            viewBinding.pendingListRecyclerview.visibility = View.VISIBLE
//                            viewBinding.noOrdersFound.visibility = View.GONE
//                            viewBinding.noOrdersFound.visibility = View.GONE
//
//                            for (i in pendingList.indices) {
//                                if (pendingList.get(i).uploadedDate != "") {
//                                    val strDate = pendingList.get(i).uploadedDate
//                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
//                                    val date = dateFormat.parse(strDate)
//                                    val dateNewFormat =
//                                        SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
//                                    pendingList.get(i).uploadedDate = dateNewFormat.toString()
//
//                                }
//                            }
//
//
//
//                            pendingListAdapter =
//                                PendingListAdapter(context, pendingList, this, loginData.EMPNAME)
//                            viewBinding.pendingListRecyclerview.layoutManager =
//                                LinearLayoutManager(
//                                    context
//                                )
//                            viewBinding.pendingListRecyclerview.adapter =
//                                pendingListAdapter
//                        } else {
//                            viewBinding.pendingListRecyclerview.visibility = View.GONE
//                            viewBinding.approvedListRecyclerview.visibility = View.GONE
//                            viewBinding.noOrdersFound.visibility = View.VISIBLE
//
//                        }
                    }


                }
                else -> {}
            }

        })

        addScrollerListener()
    }

    fun submitClickApproved() {
        pendingAndApprovedList.clear()
        pendingList.clear()
        isApprovedTab = true
        startPageApproved = 0
        endPageNumApproved = 10

        if (!viewBinding.pullToRefreshApproved.isRefreshing)
            Utlis.showLoading(requireContext())

        callAPI(startPageApproved, endPageNumApproved, isApprovedTab)


    }

    fun submitClickPending() {
        pendingAndApprovedList.clear()
        pendingList.clear()
        isApprovedTab = false
        startPagePending = 0
        endPageNumPending - 10

        if (!viewBinding.pullToRefreshPending.isRefreshing)
            Utlis.showLoading(requireContext())

        callAPI(startPagePending, endPageNumPending, isApprovedTab)


    }

    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        viewBinding.approvedListRecyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoadingApproved && !isFirstTimeApproved) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.
                    if (layoutManagerApproved.findLastCompletelyVisibleItemPosition() == pendingApprovedListAdapter?.getData()?.size!! - 1) {
                        loadMoreApproved()
                    }
                }
            }
        })

        viewBinding.pendingListRecyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoadingPending && !isFirstTimePending) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.
                    if (layoutManagerPending.findLastCompletelyVisibleItemPosition() == pendingListAdapter?.getData()?.size!! - 1) {
                        loadMorePending()
                    }
                }
            }
        })
    }

    private fun loadMoreApproved() {
        isApprovedTab = true
        //notify adapter using Handler.post() or RecyclerView.post()
        handler.post(Runnable
        {
            if (pendingApprovedListAdapter?.getData()!!.size >= 10) {
                var approvedEmptyObject = PendingAndApproved()
                approvedEmptyObject.swachhid = null
                pendingAndApprovedList.add(approvedEmptyObject)
                pendingApprovedListAdapter?.notifyDataSetChanged()


//            viewBinding.approvedListRecyclerview.scrollToPosition(pendingAndApprovedList.size-1)

                isLoadingApproved = true
                startPageApproved = startPageApproved + 10
//                endPageNumApproved = endPageNumApproved + 10
                callAPI(startPageApproved, endPageNumApproved, isApprovedTab)
            }


        })
    }

    private fun loadMorePending() {
        isApprovedTab = false
        //notify adapter using Handler.post() or RecyclerView.post()
        handler.post(Runnable
        {
            if (pendingListAdapter.getData().size >= 10) {
                var pendingEmptyObject = PendingAndApproved()
                pendingEmptyObject.swachhid = null
                pendingList.add(pendingEmptyObject)
                pendingListAdapter?.notifyDataSetChanged()


//           viewBinding.pendingListRecyclerview.scrollToPosition(pendingList.size-1)

                isLoadingPending = true
                startPagePending = startPagePending + 10
//                endPageNumPending = endPageNumPending + 10
                callAPI(startPagePending, endPageNumPending, isApprovedTab)
            }
//            else if( isfilterClicked){
//               isLoadingPending = true
//                startPagePending = startPagePending + 10
//                callAPI(startPagePending, endPageNumPending, isApprovedTab)
//            }


        })
    }

    override fun onClickUpdate(pendingAndApproved: PendingAndApproved, isApprovedAdapter: Boolean) {
        val intent = Intent(context, ApproveListActivity::class.java)
        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
        intent.putExtra("isApprovedAdapter", isApprovedAdapter)
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
        selectedSiteids = TextUtils.join(",", selectsiteIdList)
        selectedSiteids = TextUtils.join(",", selectsiteIdList)
        if (isApprovedTab) {
            pendingAndApprovedList.clear()
            startPageApproved = 0
            endPageNumApproved = 10
            callAPI(startPageApproved, endPageNumApproved, isApprovedTab)
        } else {
            pendingList.clear()
            startPagePending = 0
            endPageNumPending = 10
            callAPI(startPagePending, endPageNumPending, isApprovedTab)
        }


    }

    override fun onClickApproved() {

        if (!isApprovedTab) {
            isApprovedTab = true
            startPageApproved = 0
            endPageNumApproved = 10
            pendingAndApprovedList.clear()

            selectedSiteids = TextUtils.join(",", selectsiteIdList)
            callAPI(startPageApproved, endPageNumApproved, isApprovedTab)
        }


    }

    fun callAPI(
        startPageNo: Int,
        endpageno: Int,
        isApprovedAdapter: Boolean,
    ) {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            if (isApprovedAdapter) {
                isFirstTimeApproved = false
            } else {
                isFirstTimePending = false
            }

//

            this.selectedSiteids = TextUtils.join(",", selectsiteIdList)
            if (isApprovedAdapter) {
                if (!isLoadingApproved) {

                    Utlis.showLoading(requireContext())

                }

                val strFromDate = fromDate
                val dateFormatFromDate = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                val dateFromDate = dateFormatFromDate.parse(strFromDate)
                val dateNewFormatFromDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateFromDate)
                val fromDateNew=dateNewFormatFromDate

                val strToDate = toDate
                val dateFormatToDate = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                val dateToDate = dateFormatToDate.parse(strToDate)
                val dateNewToFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateToDate)
                val toDateNew=dateNewToFormat
                viewModel.getPendingAndApprovedListApiCall(
                    Preferences.getValidatedEmpId(),
                    fromDateNew,
                    toDateNew, selectedSiteids, startPageNo, endpageno
                )

            } else {
                if (!isLoadingPending) {
                    Utlis.showLoading(requireContext())

                }
                val strFromDate = fromDate
                val dateFormatFromDate = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                val dateFromDate = dateFormatFromDate.parse(strFromDate)
                val dateNewFormatFromDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateFromDate)
                val fromDateNew=dateNewFormatFromDate

                val strToDate = toDate
                val dateFormatToDate = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                val dateToDate = dateFormatToDate.parse(strToDate)
                val dateNewToFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateToDate)
                val toDateNew=dateNewToFormat
                viewModel.getPendingAndApprovedListApiCall(
                    Preferences.getValidatedEmpId(),
                    fromDateNew,
                    toDateNew, selectedSiteids, startPageNo, endpageno
                )

            }
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onClickPending() {
        complaintListStatus = "Pending,Reshoot,Partially Approved"
        if (isApprovedTab) {
            isApprovedTab = false
            startPagePending = 0
            endPageNumPending = 10
            pendingList.clear()
            selectedSiteids = TextUtils.join(",", selectsiteIdList)
            callAPI(startPagePending, endPageNumPending, isApprovedTab)

        }


    }

    override fun onClickCrossButton(deleteSiteId: String, position: Int) {


        DeleteSiteDialog().apply {
            arguments =
                DeleteSiteDialog().generateParsedData(deleteSiteId)
        }.show(childFragmentManager, "")


//        siteIdDisplayAdapter?.notifyDataSetChanged()

    }

    override fun onClickReview(swachhid: String?, storeId: String?) {
        val intent = Intent(context, RatingReviewActivity::class.java)
        intent.putExtra("swachhid", swachhid)
        intent.putExtra("storeId", storeId)
        intent.putExtra("userDesignation", userDesignation)
        startActivity(intent)
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
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(date)
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
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(date)
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
                selectedSiteids = TextUtils.join(",", selectsiteIdList)
                if (isApprovedTab) {
                    startPageApproved = 0
                    endPageNumApproved = 10
                    pendingAndApprovedList.clear()
                    callAPI(startPageApproved, endPageNumApproved, isApprovedTab)

                } else {
                    startPagePending = 0
                    endPageNumPending = 10
                    pendingList.clear()
                    callAPI(startPagePending, endPageNumPending, isApprovedTab)
                }

            } else if (requestCode == 887) {
                Utlis.showLoading(requireContext())

                selectsiteIdList = data?.getStringArrayListExtra("selectsiteIdList")!!
                siteIdDisplayAdapter =
                    SiteIdDisplayAdapter(context, selectsiteIdList, this)
                viewBinding.storeIdsRecyclerView.layoutManager =
                    LinearLayoutManager(
                        context, LinearLayoutManager.HORIZONTAL,
                        false
                    )
                viewBinding.storeIdsRecyclerView.adapter = siteIdDisplayAdapter

                viewBinding.storeIdsRecyclerView.adapter = siteIdDisplayAdapter



                selectedSiteids = TextUtils.join(",", selectsiteIdList)
                if (isApprovedTab) {
                    startPageApproved = 0
                    endPageNumApproved = 10
                    pendingAndApprovedList.clear()
                    callAPI(startPageApproved, endPageNumApproved, isApprovedTab)

                } else {
                    startPagePending = 0
                    endPageNumPending = 10
                    pendingList.clear()
                    callAPI(startPagePending, endPageNumPending, isApprovedTab)
                }

//                   Toast.makeText(context, ""+ selectedSiteids, Toast.LENGTH_SHORT).show()


            }
        }
    }

//    override fun onClickFilterIcon() {
//        val listStatusFilterDialog = context?.let { android.app.Dialog(it) }
//        val dialogFilterListBinding: DialogFilterListBinding =
//            DataBindingUtil.inflate(
//                LayoutInflater.from(context), R.layout.dialog_filter_list, null, false)
//        listStatusFilterDialog!!.setContentView(dialogFilterListBinding.root)
//        listStatusFilterDialog.getWindow()
//            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        dialogFilterListBinding.closeDialog.setOnClickListener {
//            listStatusFilterDialog.dismiss()
//        }
//
//        if (this.complaintListStatus.contains("Pending")) {
//            dialogFilterListBinding.isPendingChecked = true
//
//        } else {
//            dialogFilterListBinding.isPendingChecked = false
//
//        }
//        if (this.complaintListStatus.contains("Reshoot")) {
//            dialogFilterListBinding.isReshootChecked = true
//        } else {
//            dialogFilterListBinding.isReshootChecked = false
//        }
//        if (this.complaintListStatus.contains("Partially Approved")) {
//
//            dialogFilterListBinding.isPartiallyApprovedChecked = true
//        } else {
//
//            dialogFilterListBinding.isPartiallyApprovedChecked = false
//        }
//
//
//        submitButtonEnable(dialogFilterListBinding)
//
//
//
//        dialogFilterListBinding.partialyApprovedStatus.setOnCheckedChangeListener { compoundButton, b ->
//            submitButtonEnable(dialogFilterListBinding)
//        }
//        dialogFilterListBinding.pendingStatus.setOnCheckedChangeListener { compoundButton, b ->
//            submitButtonEnable(dialogFilterListBinding)
//        }
//        dialogFilterListBinding.reshootStatus.setOnCheckedChangeListener { compoundButton, b ->
//            submitButtonEnable(dialogFilterListBinding)
//        }
//
//
////        var complaintListStatusTemp = this.complaintListStatus
////        dialogComplaintListFilterBinding.status = complaintListStatusTemp
//
////        dialogComplaintListFilterBinding.statusRadioGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
////            if (i == R.id.new_status) {
////                complaintListStatusTemp = "new"
////            } else if (i == R.id.in_progress_status) {
////                complaintListStatusTemp = "inprogress"
////            } else if (i == R.id.resolved_status) {
////                complaintListStatusTemp = "solved"
////            } else if (i == R.id.reopen_status) {
////                complaintListStatusTemp = "reopened"
////            } else if (i == R.id.closed_status) {
////                complaintListStatusTemp = "closed"
////            }
////        }
//
//
//        dialogFilterListBinding.submit.setOnClickListener {
//            isfilterClicked=true
////            this.complaintListStatus = complaintListStatusTemp
//            this.complaintListStatus = ""
//            if (dialogFilterListBinding.pendingStatus.isChecked) {
//                this.complaintListStatus = "Pending"
//            }
//
//            if (dialogFilterListBinding.reshootStatus.isChecked) {
//                if (this.complaintListStatus.isEmpty()) {
//                    this.complaintListStatus = "Reshoot"
//                } else {
//                    this.complaintListStatus = "${this.complaintListStatus},Reshoot"
//                }
//            }
//            if (dialogFilterListBinding.partialyApprovedStatus.isChecked) {
//                if (this.complaintListStatus.isEmpty()) {
//                    this.complaintListStatus = "Partially Approved"
//                } else {
//                    this.complaintListStatus = "${this.complaintListStatus},Partially Approved"
//                }
//            }
//
////            complaintListStatus.length
//
//
//            if (listStatusFilterDialog != null && listStatusFilterDialog.isShowing) {
//                listStatusFilterDialog.dismiss()
//                startPagePending = 0
//                endPageNumPending = 10
//                startPageApproved = 0
//                endPageNumApproved = 10
//                pendingList.clear()
//                pendingAndApprovedList.clear()
//                callAPI(startPagePending, endPageNumApproved, isApprovedTab)
//
//
//            }
//        }
//        listStatusFilterDialog.show()
//    }

    fun submitButtonEnable(dialogFilterListBinding: DialogFilterListBinding) {
        if (!dialogFilterListBinding.partialyApprovedStatus.isChecked
            && !dialogFilterListBinding.pendingStatus.isChecked
            && !dialogFilterListBinding.reshootStatus.isChecked
        ) {
            dialogFilterListBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogFilterListBinding.isSubmitEnable = false
        } else {
            dialogFilterListBinding.submit.setBackgroundResource(R.drawable.yellow_drawable)
            dialogFilterListBinding.isSubmitEnable = true
        }
    }

    override fun onClickFilterIcon() {

    }


    override fun onClickSiteIdIcon() {
        val intent = Intent(context, SelectSiteActivityy::class.java)
        intent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
        startActivityForResult(intent, 887)
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickSpinnerLayout() {
        TODO("Not yet implemented")
    }


//    override fun deleteSite(siteDataItem: StoreListItem) {
//
//    }

    override fun deleteSite(siteDataItem: String) {
        selectsiteIdList.remove(siteDataItem)


        selectedSiteids = TextUtils.join(",", selectsiteIdList)
        siteIdDisplayAdapter?.notifyDataSetChanged()

        if (isApprovedTab) {
            startPageApproved = 0
            endPageNumApproved = 10
            pendingAndApprovedList.clear()
            callAPI(startPageApproved, endPageNumApproved, isApprovedTab)

        } else {
            startPagePending = 0
            endPageNumPending = 10
            pendingList.clear()
            callAPI(startPagePending, endPageNumPending, isApprovedTab)
        }

    }

    override fun doNotDeleteSite() {

    }
}


