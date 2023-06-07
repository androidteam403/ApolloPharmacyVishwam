package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anychart.data.View
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PreRetroPreviewLayoutBinding
import com.apollopharmacy.vishwam.databinding.TimelineLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.ApprovalReviewCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.PreRetroReviewingCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.util.Utlis
import org.apache.commons.lang3.text.WordUtils
import java.text.SimpleDateFormat
import java.util.stream.Collectors

class TimeLineListAdapter(
    val mContext: Context,
    var approveList: List<GetRetroPendingAndApproveResponse.Retro>,


    ) :

    RecyclerView.Adapter<TimeLineListAdapter.ViewHolder>() {

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
        holder.timelineLayoutBinding.uploadedDate.text =Utlis.convertRetroDate(approvedOrders.uploadedDate!!)
        if(approvedOrders.executiveApprovedBy.equals("null")||approvedOrders.executiveApprovedBy.isNullOrEmpty()){
            holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.executiveApproveBy.text=approvedOrders.executiveApprovedBy
            holder.timelineLayoutBinding.executiveApproveDate.text=Utlis.convertRetroDate(
                approvedOrders.executiveApprovedDate!!
            )
        }
        if(approvedOrders.executiveReshootBy.equals("null")||approvedOrders.executiveReshootBy.isNullOrEmpty()){
            holder.timelineLayoutBinding.executiveReshootByLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.executiveReshootDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.executiveReshootDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.executiveReshootByLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.executiveReshootBy.text=approvedOrders.executiveReshootBy
            holder.timelineLayoutBinding.executiveReshootDate.text=Utlis.convertRetroDate(
                approvedOrders.executiveReshootDate!!
            )
        }

        if(approvedOrders.managerApprovedBy.equals("null")||approvedOrders.managerApprovedBy.isNullOrEmpty()){
            holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.managerApproveDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.managerApproveLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.managerApproveBy.text=approvedOrders.managerApprovedBy
            holder.timelineLayoutBinding.managerApproveDate.text=Utlis.convertRetroDate(
                approvedOrders.managerApprovedDate!!
            )
        }
        if(approvedOrders.managerReshootBy!!.equals("null")||approvedOrders.managerReshootBy.toString().isNullOrEmpty()){
            holder.timelineLayoutBinding.managerReshootByLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.managerReshootDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.managerReshootDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.managerReshootByLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.managerReshootBy.text=approvedOrders.managerReshootBy.toString()
            holder.timelineLayoutBinding.managerReshootDate.text=Utlis.convertRetroDate(approvedOrders.managerReshootDate.toString()!!)
        }

        if(approvedOrders.gmApprovedBy.equals("null")||approvedOrders.gmApprovedBy.isNullOrEmpty()){
            holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.gmApproveLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.gmApproveDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.gmApproveBy.text=approvedOrders.gmApprovedBy
            holder.timelineLayoutBinding.gmApproveDate.text=Utlis.convertRetroDate(
                approvedOrders.gmApprovedDate!!
            )
        }
        if(approvedOrders.gmReshootBy!!.equals("null")||approvedOrders.gmReshootBy.toString().isNullOrEmpty()){
            holder.timelineLayoutBinding.gmReshootByLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.gmReshootDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.gmReshootByLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.gmReshootDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.gmReshootBy.text=approvedOrders.gmReshootBy.toString()
            holder.timelineLayoutBinding.gmReshootDate.text=Utlis.convertRetroDate(
                approvedOrders.gmReshootDate!!
            )
        }


        if(approvedOrders.ceoApprovedBy!!.equals("null")||approvedOrders.ceoApprovedBy.toString().isNullOrEmpty()){
            holder.timelineLayoutBinding.ceoApproveLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.ceoApproveDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.ceoApproveLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.ceoApproveDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.ceoApproveBy.text=approvedOrders.ceoApprovedBy.toString()
            holder.timelineLayoutBinding.ceoApproveDate.text=Utlis.convertRetroDate(
                approvedOrders.ceoApprovedDate.toString()!!
            )
        }
        if(approvedOrders.ceoReshootBy!!.equals("null")||approvedOrders.ceoReshootBy.toString().isNullOrEmpty()){
            holder.timelineLayoutBinding.ceoReshootByLayout.visibility=android.view.View.GONE
            holder.timelineLayoutBinding.ceoReshootDateLayout.visibility=android.view.View.GONE

        }
        else{
            holder.timelineLayoutBinding.ceoReshootDateLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.ceoReshootByLayout.visibility=android.view.View.VISIBLE
            holder.timelineLayoutBinding.ceoReshootBy.text=approvedOrders.ceoReshootBy.toString()
            holder.timelineLayoutBinding.ceoReshootDate.text=Utlis.convertRetroDate(
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