package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentSwachhListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter.PendingApprovedListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.ApproveListActivity

class SwachListFragment : BaseFragment<SwachListViewModel, FragmentSwachhListBinding>(),
    SwachhListCallback {
    var pendingAndApprovedList = ArrayList<PendingAndApproved>()
    var pendingApprovedListAdapter: PendingApprovedListAdapter? = null

    override val layoutRes: Int
        get() = R.layout.fragment_swachh_list

    override fun retrieveViewModel(): SwachListViewModel {
        return ViewModelProvider(this).get(SwachListViewModel::class.java)
    }

    override fun setup() {
        viewModel.getPendingAndApprovedListApiCall()
        viewBinding.storeId = Preferences.getSiteId()
        viewModel.getpendingAndApprovedListResponse.observe(viewLifecycleOwner, {
            val getpendingAndApprovedListResponse: GetpendingAndApprovedListResponse
            getpendingAndApprovedListResponse = it
            var getApprovedList: List<GetpendingAndApprovedListResponse.GetApproved>? = null
            var getPendingList: List<GetpendingAndApprovedListResponse.GetPending>? = null
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
                        for (i in getApprovedList) {
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
                            pendingAndApprovedList.add(pendingAndApproved)

                        }
                    }
                    if (getPendingList != null) {
                        for (i in getPendingList) {
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
}