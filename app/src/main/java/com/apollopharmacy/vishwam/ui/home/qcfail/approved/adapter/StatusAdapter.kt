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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val status = statusList.get(position)
        var time = status.actiondate.toString()!!
            .substring(11,
                Math.min(status.actiondate.toString()!!.length, 19))
        val utc = TimeZone.getTimeZone("UTC")
        val inputFormat: DateFormat = SimpleDateFormat("HH:mm:ss",
            Locale.ENGLISH)
        inputFormat.timeZone = utc
        val outputFormat: DateFormat = SimpleDateFormat("hh:mm aa",
            Locale.ENGLISH)
        outputFormat.timeZone = utc
        val dateInput = inputFormat.parse(time)
        val output = outputFormat.format(dateInput)



        var orderId: String = ""
//        val simpleDateFormat12 = SimpleDateFormat("hh.mm aa")
//        val simpleDateFormat24 = SimpleDateFormat("HH:mm")
//        var cal = Date()
////        cal=simpleDateFormat12.parse(time)
        var approveddate = Utlis.formatdate(status.actiondate.toString()!!
            .substring(0,
                Math.min(status.actiondate.toString()!!.length, 10)))

//        holder.statusLayoutBinding.date.text = Utlis.formatdate(approveddate)
        holder.statusLayoutBinding.date.setText(approveddate + " " +output )
        holder.statusLayoutBinding.statusBy.setText(status.status + " (" + status.actionby + ") on ")




    }

    override fun getItemCount(): Int {
        return statusList.size
    }
    fun convert12(str: String) {
// Get Hours
        val h1 = str[0].toInt() - '0'.toInt()
        val h2 = str[1].toInt() - '0'.toInt()
        var hh = h1 * 10 + h2

        // Finding out the Meridien of time
        // ie. AM or PM
        val Meridien: String
        Meridien = if (hh < 12) {
            "AM"
        } else "PM"
        hh %= 12

        // Handle 00 and 12 case separately
        if (hh == 0) {
            print("12")

            // Printing minutes and seconds
            for (i in 2..7) {
                print(str[i])
            }
        } else {
            print(hh)
            // Printing minutes and seconds
            for (i in 2..7) {
                print(Meridien)
            }
        }
    }

    class ViewHolder(val statusLayoutBinding: QcStatusLayoutBinding) :
        RecyclerView.ViewHolder(statusLayoutBinding.root)

}

