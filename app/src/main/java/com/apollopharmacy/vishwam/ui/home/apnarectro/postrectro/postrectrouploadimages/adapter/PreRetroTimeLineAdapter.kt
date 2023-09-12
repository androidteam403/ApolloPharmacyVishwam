package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.TimelineLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter.ApprovalImagesListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListRes
import com.apollopharmacy.vishwam.util.Utlis
import org.apache.commons.lang3.text.WordUtils

    class PreRetroTimeLineAdapter(
        val mContext: Context,
        var approveList: List<GetStorePendingAndApprovedListRes.Get>,


        ) :

        RecyclerView.Adapter<PreRetroTimeLineAdapter.ViewHolder>() {

        var adapter: ApprovalImagesListAdapter? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val timelineLayoutBinding: TimelineLayoutBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.timeline_layout,
                    parent,
                    false
                )
            return ViewHolder(timelineLayoutBinding)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val approvedOrders = approveList.get(position)
            holder.timelineLayoutBinding.stage.text = WordUtils.capitalizeFully(approvedOrders.stage!!.replace("-", " "))
            holder.timelineLayoutBinding.uploadBy.text = approvedOrders.uploadedBy
            holder.timelineLayoutBinding.uploadedDate.text = Utlis.convertRetroDate(approvedOrders.uploadedDate!!)
            if(approvedOrders.executiveApprovedBy==null){
                holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.executiveApproveBy.text=approvedOrders.executiveApprovedBy.toString()
                holder.timelineLayoutBinding.executiveApproveDate.text= Utlis.convertRetroDate(
                    approvedOrders.executiveApprovedDate.toString()!!
                )
            }
            if(approvedOrders.executiveReshootBy==null){
                holder.timelineLayoutBinding.executiveReshootByLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.executiveReshootDateLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.executiveReshootDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.executiveReshootByLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.executiveReshootBy.text=approvedOrders.executiveReshootBy.toString()
                holder.timelineLayoutBinding.executiveReshootDate.text= Utlis.convertRetroDate(
                    approvedOrders.executiveReshootDate.toString()!!
                )
            }

            if(approvedOrders.managerApprovedBy==null){
                holder.timelineLayoutBinding.managerApproveLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.managerApproveDateLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.managerApproveDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.managerApproveLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.managerApproveBy.text=approvedOrders.managerApprovedBy.toString()
                holder.timelineLayoutBinding.managerApproveDate.text= Utlis.convertRetroDate(
                    approvedOrders.managerApprovedDate.toString()!!
                )
            }
            if(approvedOrders.managerReshootBy==null){
                holder.timelineLayoutBinding.managerReshootByLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.managerReshootDateLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.managerReshootDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.managerReshootByLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.managerReshootBy.text=approvedOrders.managerReshootBy.toString()
                holder.timelineLayoutBinding.managerReshootDate.text= Utlis.convertRetroDate(approvedOrders.managerReshootDate.toString()!!)
            }

            if(approvedOrders.gmApprovedBy==null){
                holder.timelineLayoutBinding.gmApproveDateLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.gmApproveLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.gmApproveLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.gmApproveDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.gmApproveBy.text=approvedOrders.gmApprovedBy.toString()
                holder.timelineLayoutBinding.gmApproveDate.text= Utlis.convertRetroDate(
                    approvedOrders.gmApprovedDate.toString()!!
                )
            }
            if(approvedOrders.gmReshootBy==null){
                holder.timelineLayoutBinding.gmReshootByLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.gmReshootDateLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.gmReshootByLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.gmReshootDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.gmReshootBy.text=approvedOrders.gmReshootBy.toString()
                holder.timelineLayoutBinding.gmReshootDate.text= Utlis.convertRetroDate(
                    approvedOrders.gmReshootDate.toString()!!
                )
            }


            if(approvedOrders.ceoApprovedBy==null){
                holder.timelineLayoutBinding.ceoApproveLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.ceoApproveDateLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.ceoApproveLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.ceoApproveDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.ceoApproveBy.text=approvedOrders.ceoApprovedBy.toString()
                holder.timelineLayoutBinding.ceoApproveDate.text= Utlis.convertRetroDate(
                    approvedOrders.ceoApprovedDate.toString()!!
                )
            }
            if(approvedOrders.ceoReshootBy==null){
                holder.timelineLayoutBinding.ceoReshootByLayout.visibility=android.view.View.GONE
                holder.timelineLayoutBinding.ceoReshootDateLayout.visibility=android.view.View.GONE

            }
            else{
                holder.timelineLayoutBinding.ceoReshootDateLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.ceoReshootByLayout.visibility=android.view.View.VISIBLE
                holder.timelineLayoutBinding.ceoReshootBy.text=approvedOrders.ceoReshootBy.toString()
                holder.timelineLayoutBinding.ceoReshootDate.text= Utlis.convertRetroDate(
                    approvedOrders.ceoReshootDate.toString()!!
                )
            }


        }

        override fun getItemCount(): Int {

            return approveList.size
        }

        class ViewHolder(val timelineLayoutBinding: TimelineLayoutBinding) :
            RecyclerView.ViewHolder(timelineLayoutBinding.root)

    }