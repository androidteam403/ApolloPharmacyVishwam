package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PreRetroPreviewLayoutBinding
import com.apollopharmacy.vishwam.databinding.TimelineLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.ApprovalReviewCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.PreRetroReviewingCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        val date = dateFormat.parse(approvedOrders.approvedDate)
        val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
        val uploaddDate = dateFormat.parse(approvedOrders.uploadedDate)
        val uploadDateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(uploaddDate)
        val reshootDate = dateFormat.parse(approvedOrders.reshootDate)
        val reshootDateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(reshootDate)

        val partialapproveDate = dateFormat.parse(approvedOrders.partiallyApprovedDate)
        val partialapprovetDateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(partialapproveDate)

        holder.timelineLayoutBinding.approveBy.setText(approvedOrders.approvedBy)
        holder.timelineLayoutBinding.approveDate.setText(dateNewFormat)
        holder.timelineLayoutBinding.partiallyApprovedBy.setText(approvedOrders.partiallyApprovedBy)
holder.timelineLayoutBinding.stage.setText(WordUtils.capitalizeFully(approvedOrders.stage!!.replace("-"," ")))
        holder.timelineLayoutBinding.reshootBy.setText(approvedOrders.reshootBy)
        holder.timelineLayoutBinding.uploadBy.setText(approvedOrders.uploadedBy)
        holder.timelineLayoutBinding.reshootBy.setText(reshootDateNewFormat)
        holder.timelineLayoutBinding.partiallyApprovedDate.setText(partialapprovetDateNewFormat)

        holder.timelineLayoutBinding.uploadedDate.setText(uploadDateNewFormat)

    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val timelineLayoutBinding: TimelineLayoutBinding) :
        RecyclerView.ViewHolder(timelineLayoutBinding.root)

}