package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.attendance.AttendanceHistoryRes
import com.apollopharmacy.vishwam.databinding.ViewHistoryItemBinding
import com.apollopharmacy.vishwam.ui.home.adrenalin.history.HistorryImageClickListener
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ImageClickListener
import com.apollopharmacy.vishwam.util.Utils
import org.apache.commons.lang3.StringUtils
import java.util.*
import kotlin.collections.ArrayList

class AttendenceHistoryAdapter(

    val mContext: Context,
    var attendenceHistoryList: ArrayList<AttendanceHistoryRes>,
    val imageClickListener: HistorryImageClickListener,
) :

    RecyclerView.Adapter<AttendenceHistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewHistoryItemBinding: ViewHistoryItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.view_history_item,
                parent,
                false
            )
        return ViewHolder(viewHistoryItemBinding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = attendenceHistoryList.get(position)
        holder.viewHistoryItemBinding.signInTime.text= Utils.getHistoryCustomDateNew(item.signInDate)+" - "+Utils.getHistoryTimeFormat(item.signInDate)
        holder.viewHistoryItemBinding.sno.text=(position+1).toString()
        if (item.signOutDate.isNullOrEmpty()) {
            holder.viewHistoryItemBinding.signOutTime.text= "-"
        } else {
            holder.viewHistoryItemBinding.signOutTime.text= Utils.getHistoryCustomDateNew(item.signOutDate)+" - "+Utils.getHistoryTimeFormat(item.signOutDate)
        }



        if (item.duration.isNullOrEmpty()) {
            holder.viewHistoryItemBinding.durationTexthrs.text = "  -"
            holder.viewHistoryItemBinding.durationTextminutes.text = "  -"
            holder.viewHistoryItemBinding.durationTextsecs.text = "  -"

        } else {
            holder.viewHistoryItemBinding.durationTexthrs.text = StringUtils.substringBefore( Utils.getHistoryDurationTimeFormat(item.duration),"H")
            holder.viewHistoryItemBinding.durationTextminutes.text = StringUtils.split(Utils.getHistoryDurationTimeFormat(item.duration)," ").get(1).replace("M","")
            holder.viewHistoryItemBinding.durationTextsecs.text =StringUtils.split(Utils.getHistoryDurationTimeFormat(item.duration)," ").get(2).replace("S","")       }
    }

    override fun getItemCount(): Int {
        return attendenceHistoryList.size

    }

    class ViewHolder(val viewHistoryItemBinding: ViewHistoryItemBinding) :
        RecyclerView.ViewHolder(viewHistoryItemBinding.root)

}

