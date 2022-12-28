package com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcStatusLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class StatusAdapter(
    val context: Context,
    val actionHistoryList: ArrayList<ActionResponse.Hsitorydetail>,
) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val qcStatusLayoutBinding: QcStatusLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.qc_status_layout,
            parent,
            false
        )
        return ViewHolder(qcStatusLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val status = actionHistoryList.get(position).status
        val actionDate = actionHistoryList.get(position).actiondate
        val actionBy = actionHistoryList.get(position).actionby

        holder.qcStatusLayoutBinding.statusBy.setText(status)
        holder.qcStatusLayoutBinding.actionBy.setText("(" + actionBy + ")")
        holder.qcStatusLayoutBinding.date.setText(actionDate)
    }

    override fun getItemCount(): Int {
        return actionHistoryList.size
    }

    class ViewHolder(val qcStatusLayoutBinding: QcStatusLayoutBinding) :
        RecyclerView.ViewHolder(qcStatusLayoutBinding.root)
}

