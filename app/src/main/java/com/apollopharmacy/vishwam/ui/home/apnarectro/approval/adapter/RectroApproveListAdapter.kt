package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.RetroApprovalLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.PreRectroApprovalCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback

class RectroApproveListAdapter(
    val mContext: Context,
    var approveList: ArrayList<String>,
    val mClicklistner: PreRectroApprovalCallback,

    ) :

    RecyclerView.Adapter<RectroApproveListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val adapterApproveListBinding: RetroApprovalLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.retro_approval_layout,
                parent,
                false
            )
        return ViewHolder(adapterApproveListBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)
        if(approvedOrders.equals("APPROVED")){
            holder.adapterApproveListBinding.ratinglayout.visibility= View.VISIBLE
            holder.adapterApproveListBinding.stage.setText("After Completion")
            holder.adapterApproveListBinding.status.setText("APPROVED")
            holder.adapterApproveListBinding.status.setTextColor(Color.parseColor("#39B54A"))

            holder.adapterApproveListBinding.statusLayout.setBackgroundResource(R.drawable.retro_background_approval)


        }else if(approvedOrders.equals("PENDING")){
            holder.adapterApproveListBinding.ratinglayout.visibility= View.GONE
            holder.adapterApproveListBinding.stage.setText("Post Retro")
            holder.adapterApproveListBinding.status.setText("PENDING")
            holder.adapterApproveListBinding.status.setTextColor(Color.parseColor("#f26522"))

            holder.adapterApproveListBinding.statusLayout.setBackgroundResource(R.drawable.retro_background_pending)

        }

        holder.itemView.setOnClickListener {
            mClicklistner.onClick(position,approvedOrders)
        }





        }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val adapterApproveListBinding: RetroApprovalLayoutBinding) :
        RecyclerView.ViewHolder(adapterApproveListBinding.root)

}