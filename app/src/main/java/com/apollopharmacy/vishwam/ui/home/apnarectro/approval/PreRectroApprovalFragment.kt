package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentApprovalPrerectroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.activity.PreRetroPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter.RectroApproveListAdapter


class PreRectroApprovalFragment() : BaseFragment<PreRectroApprovalViewModel, FragmentApprovalPrerectroBinding>(),PreRectroApprovalCallback{
    var adapter: RectroApproveListAdapter? = null

    override val layoutRes: Int
        get() = R.layout.fragment_approval_prerectro

    override fun retrieveViewModel(): PreRectroApprovalViewModel {
        return ViewModelProvider(this).get(PreRectroApprovalViewModel::class.java)
    }

    override fun setup() {
        var approvelist= java.util.ArrayList<String>()
        approvelist!!.add("APPROVED")
        approvelist!!.add("PENDING")





        adapter= context?.let { RectroApproveListAdapter(it, approvelist!!,this) }
        viewBinding.recyclerViewapproval.adapter=adapter

    }

    override fun onClick(position: Int, status: String) {
        val intent= Intent(context,PreRetroPreviewActivity::class.java)
        startActivity(intent)    }


}