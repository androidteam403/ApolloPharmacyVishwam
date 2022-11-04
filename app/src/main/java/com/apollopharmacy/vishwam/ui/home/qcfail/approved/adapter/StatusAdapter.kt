package com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcStatusLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis

class StatusAdapter(

    val mContext: Context,
    var statusList: ArrayList<ActionResponse.Hsitorydetail>,
) :

    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val qcStatusLayoutBinding: QcStatusLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_status_layout,
                parent,
                false
            )
        return ViewHolder(qcStatusLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val status = statusList.get(position)
        var orderId: String = ""
        var approveddate = Utlis.formatdate(status.actiondate.toString()!!
            .substring(0,
                Math.min(status.actiondate.toString()!!.length, 10)))
        holder.statusLayoutBinding.date.text = Utlis.formatdate(approveddate)
        holder.statusLayoutBinding.statusStaffDetails.setText(status.actionby)
        holder.statusLayoutBinding.statusBy.setText(status.status+" ON "+approveddate+" BY "+status.actionby)

    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    class ViewHolder(val statusLayoutBinding: QcStatusLayoutBinding) :
        RecyclerView.ViewHolder(statusLayoutBinding.root)

}

