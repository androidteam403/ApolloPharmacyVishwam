package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentApprovalPrerectroBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter.RectroApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.apnasiteIdselect.ApnaSelectSiteActivityy
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.ApprovalPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendindAndApproverequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.util.Utlis
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.stream.Collectors


class PreRectroApprovalFragment() :
    BaseFragment<PreRectroApprovalViewModel, FragmentApprovalPrerectroBinding>(),MainActivityCallback,
    PreRectroApprovalCallback {
    var adapter: RectroApproveListAdapter? = null
    private var fragmentName: String = ""
    var currentDate = String()
    var fromDate = String()
    var isApiHit: Boolean = false
    var isRatingApiHit: Boolean = false
    var selectsiteIdList = ArrayList<String>()

    override val layoutRes: Int
        get() = R.layout.fragment_approval_prerectro

    override fun retrieveViewModel(): PreRectroApprovalViewModel {
        return ViewModelProvider(this)[PreRectroApprovalViewModel::class.java]
    }

    override fun setup() {
        showLoading()
//        Preferences.savingToken("APL48627")
//        Preferences.setAppLevelDesignationApnaRetro("EXECUTIVE")
        MainActivity.mInstance.mainActivityCallback = this
        viewBinding.pullToRefreshApproved.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClickApproved()
        })

        var getRetroPendindAndApproverequest = GetRetroPendindAndApproverequest()
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        currentDate = simpleDateFormat.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        fromDate = simpleDateFormat.format(cal.time)
        getRetroPendindAndApproverequest.empid = Preferences.getToken()
        getRetroPendindAndApproverequest.storeid =Preferences.getRectroSiteId()
        getRetroPendindAndApproverequest.fromdate = fromDate
        getRetroPendindAndApproverequest.todate = currentDate

        viewModel.getRectroApprovalList(getRetroPendindAndApproverequest, this)
        if (this.arguments?.getBoolean("fromPreRectroApproval") == true) {
            fragmentName = "fromPreRectroApproval"
        } else if (this.arguments?.getBoolean("fromPostRectroApproval") == true) {
            fragmentName = "fromPostRectroApproval"
        } else {
            fragmentName = "fromAfterCompletionApproval"
        }


    }

    override fun onClick(position: Int,  subPos:Int,status: List<List<GetRetroPendingAndApproveResponse.Retro>>?,approvePendingList:ArrayList<GetRetroPendingAndApproveResponse.Retro>,newStatus:String) {
        val intent = Intent(context, ApprovalPreviewActivity::class.java)
        intent.putExtra("stage", status!![position][subPos].stage)
        intent.putExtra("status", status!!.get(position)[subPos].status)
        intent.putExtra("newStatus", newStatus)
        intent.putExtra("site", status!!.get(position)[subPos].store)
        intent.putExtra("approvePendingList",approvePendingList)
        intent.putExtra("uploadBy", status!![position][subPos].uploadedBy)
        intent.putExtra("uploadOn", status!![position][subPos].uploadedDate)
        intent.putExtra("retroId", status!!.get(position).get(subPos).retroid)
        startActivityForResult(intent, 221)


    }
    fun submitClickApproved() {


        if (!viewBinding.pullToRefreshApproved.isRefreshing)
            Utlis.showLoading(requireContext())

        var getRetroPendindAndApproverequest = GetRetroPendindAndApproverequest()
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        currentDate = simpleDateFormat.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        fromDate = simpleDateFormat.format(cal.time)
        getRetroPendindAndApproverequest.empid = Preferences.getToken()
        getRetroPendindAndApproverequest.storeid =Preferences.getRectroSiteId()
        getRetroPendindAndApproverequest.fromdate = fromDate
        getRetroPendindAndApproverequest.todate = currentDate

        viewModel.getRectroApprovalList(getRetroPendindAndApproverequest, this)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 221) {
            isApiHit = data?.getBooleanExtra("isApiHit", false) as Boolean
            isRatingApiHit=data?.getBooleanExtra("isRatingApiHit", false) as Boolean
            if (isApiHit||isRatingApiHit) {
                showLoading()
                var getRetroPendindAndApproverequest = GetRetroPendindAndApproverequest()
                val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                currentDate = simpleDateFormat.format(Date())

                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -7)
                fromDate = simpleDateFormat.format(cal.time)
                getRetroPendindAndApproverequest.empid = Preferences.getToken()
                getRetroPendindAndApproverequest.storeid = Preferences.getRectroSiteId()
                getRetroPendindAndApproverequest.fromdate = fromDate
                getRetroPendindAndApproverequest.todate = currentDate

                viewModel.getRectroApprovalList(getRetroPendindAndApproverequest, this)
            }
        }


        if (requestCode==721){
            selectsiteIdList= data?.getStringArrayListExtra("selectsiteIdList") as ArrayList<String>
            if (selectsiteIdList!=null){
                showLoading()
                var getRetroPendindAndApproverequest = GetRetroPendindAndApproverequest()
                val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                currentDate = simpleDateFormat.format(Date())

                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -7)
                fromDate = simpleDateFormat.format(cal.time)
                getRetroPendindAndApproverequest.empid = Preferences.getToken()
                getRetroPendindAndApproverequest.storeid = selectsiteIdList.toString().replace("[","").replace("]","").replace(" ","")
                getRetroPendindAndApproverequest.fromdate = fromDate
                getRetroPendindAndApproverequest.todate = currentDate

                viewModel.getRectroApprovalList(getRetroPendindAndApproverequest, this)

            }
        }
    }

    override fun onSuccessRetroApprovalList(getStorePendingApprovedList: GetRetroPendingAndApproveResponse) {
        if (viewBinding.pullToRefreshApproved.isRefreshing) {
//            Toast.makeText(context, "Refresh", Toast.LENGTH_LONG).show()
            viewBinding.pullToRefreshApproved.isRefreshing = false
        }
        hideLoading()

        if (getStorePendingApprovedList.retrolist.isNullOrEmpty()){
           viewBinding.recyclerViewapproval.visibility= View.GONE
            viewBinding.emptyList.visibility= View.VISIBLE

        }else{
            viewBinding.recyclerViewapproval.visibility= View.VISIBLE
            viewBinding.emptyList.visibility= View.GONE

        }
        var list: java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>? = null
        var list1: java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>? = null
        var getPendingApproveList: java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>? = null


        val retroIdsGroupedList: Map<String, List<GetRetroPendingAndApproveResponse.Retro>> =
            getStorePendingApprovedList.retrolist!!.stream()
                .collect(Collectors.groupingBy { w -> w.retroid })
//           getStorePendingApprovedList.getList.clear()

        var getStorePendingApprovedListDummys =
            ArrayList<ArrayList<GetRetroPendingAndApproveResponse.Retro>>()

        for (entry in retroIdsGroupedList.entries) {
            getStorePendingApprovedListDummys.addAll(listOf(entry.value as java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>))
        }

        getStorePendingApprovedList.groupByRetrodList =
            getStorePendingApprovedListDummys as List<MutableList<GetRetroPendingAndApproveResponse.Retro>>?

        list =
            getStorePendingApprovedList.retrolist as java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>?
        list1 =
            list!!.distinctBy { it.retroid } as java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>

        val sortedList = getStorePendingApprovedList.groupByRetrodList.orEmpty()
            .sortedByDescending { it.firstOrNull()?.uploadedDate }

        adapter = context?.let {
            RectroApproveListAdapter(it, list, list1, retroIdsGroupedList, sortedList, this)
        }

        viewBinding.recyclerViewapproval.adapter = adapter

    }




    override fun onFailureRetroApprovalList(value: GetRetroPendingAndApproveResponse) {
        hideLoading()
        Toast.makeText(context,value.message,Toast.LENGTH_LONG).show()
    }





    override fun onClickFilterIcon() {

    }

    override fun onClickSiteIdIcon() {

        val i = Intent(context, ApnaSelectSiteActivityy::class.java)
        startActivityForResult(i, 721)
    }

    override fun onClickQcFilterIcon() {

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

    }

    override fun onClickSubmenuItem(
        menuName: String?,
        submenus: java.util.ArrayList<MenuModel>?,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onclickHelpIcon() {
        TODO("Not yet implemented")
    }


}