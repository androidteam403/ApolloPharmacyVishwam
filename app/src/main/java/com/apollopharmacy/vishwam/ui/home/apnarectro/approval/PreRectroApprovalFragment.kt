package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentApprovalPrerectroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter.RectroApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.PreRetroPreviewActivity


class PreRectroApprovalFragment() : BaseFragment<PreRectroApprovalViewModel, FragmentApprovalPrerectroBinding>(),PreRectroApprovalCallback{
    var adapter: RectroApproveListAdapter? = null
    private var fragmentName:String=""


    override val layoutRes: Int
        get() = R.layout.fragment_approval_prerectro

    override fun retrieveViewModel(): PreRectroApprovalViewModel {
        return ViewModelProvider(this).get(PreRectroApprovalViewModel::class.java)
    }

    override fun setup() {
        if(this.arguments?.getBoolean("fromPreRectroApproval") == true){
            fragmentName= "fromPreRectroApproval"
        }else if(this.arguments?.getBoolean("fromPostRectroApproval") == true){
            fragmentName= "fromPostRectroApproval"
        }else{
            fragmentName= "fromAfterCompletionApproval"
        }
        var approvelist= java.util.ArrayList<String>()
        approvelist.add("APPROVED")
        approvelist.add("PENDING")





        adapter= context?.let { RectroApproveListAdapter(it, approvelist,this) }
        viewBinding.recyclerViewapproval.adapter=adapter

    }

    override fun onClick(position: Int, status: String) {
        if(fragmentName.equals("fromPreRectroApproval")){
            val intent= Intent(context, PreRetroPreviewActivity::class.java)
            intent.putExtra("stage", "isPreRetroStage")
            intent.putExtra("fragmentName", "approvalFragment")
            startActivity(intent)
        }else if(fragmentName.equals("fromPostRectroApproval")){
            val intent= Intent(context, PreRetroPreviewActivity::class.java)
            intent.putExtra("stage", "isPostRetroStage")
            intent.putExtra("fragmentName", "approvalFragment")
            startActivity(intent)
        }else{
            val intent= Intent(context, PreRetroPreviewActivity::class.java)
            intent.putExtra("stage", "isAfterCompletionStage")
            intent.putExtra("fragmentName", "approvalFragment")
            startActivity(intent)
        }
         }


}