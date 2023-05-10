package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentApprovalPrerectroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter.RectroApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.ApprovalPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendindAndApproverequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.PreRetroPreviewActivity
import kotlinx.android.synthetic.main.retro_approval_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class PreRectroApprovalFragment() :
    BaseFragment<PreRectroApprovalViewModel, FragmentApprovalPrerectroBinding>(),
    PreRectroApprovalCallback {
    var adapter: RectroApproveListAdapter? = null
    private var fragmentName: String = ""
    var currentDate = String()
    var fromDate = String()

    override val layoutRes: Int
        get() = R.layout.fragment_approval_prerectro

    override fun retrieveViewModel(): PreRectroApprovalViewModel {
        return ViewModelProvider(this).get(PreRectroApprovalViewModel::class.java)
    }

    override fun setup() {

        showLoading()
        var getRetroPendindAndApproverequest = GetRetroPendindAndApproverequest()
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        currentDate = simpleDateFormat.format(Date())

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        fromDate = simpleDateFormat.format(cal.time)
        getRetroPendindAndApproverequest.empid = "APL48627"
        getRetroPendindAndApproverequest.storeid = "16001"
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

    override fun onClick(position: Int, status: GetRetroPendingAndApproveResponse.Retro) {
        val intent = Intent(context, ApprovalPreviewActivity::class.java)
        intent.putExtra("stage", status.stage)
        intent.putExtra("status", status.status)
        intent.putExtra("site", status.store)

        intent.putExtra("retroId", status.retroid)
        startActivity(intent)

    }

    override fun onSuccessRetroApprovalList(getStorePendingApprovedList: GetRetroPendingAndApproveResponse) {
        hideLoading()
        var list: java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>? = null
        var list1: java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>? = null
        var getPendingApproveList :java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>? = null


//        list1 =
//            list!!.distinctBy { it.retroid } as ArrayList<GetRetroPendingAndApproveResponse.Retro>


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
        getStorePendingApprovedList.Retro().retroSublist =
            getStorePendingApprovedListDummys as ArrayList<GetRetroPendingAndApproveResponse.Retro>
        list =
            getStorePendingApprovedList.retrolist as java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>?
        list1 =
            list!!.distinctBy { it.retroid} as java.util.ArrayList<GetRetroPendingAndApproveResponse.Retro>



//        adapter =
//            ListAdapter(getStorePendingApprovedList.groupByRetrodList, requireContext(), this)
//        val layoutManager = LinearLayoutManager(ViswamApp.context)
//        viewBinding.listRecyclerView.layoutManager = layoutManager
//        viewBinding.listRecyclerView.itemAnimator =
//            DefaultItemAnimator()
//        viewBinding.listRecyclerView.adapter = listAdapter

        adapter = context?.let { RectroApproveListAdapter(it,list1,retroIdsGroupedList,this) }
        viewBinding.recyclerViewapproval.adapter = adapter

    }

    override fun onFailureRetroApprovalList(value: GetRetroPendingAndApproveResponse) {
    }


}