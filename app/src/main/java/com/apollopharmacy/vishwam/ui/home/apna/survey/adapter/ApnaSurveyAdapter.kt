package com.apollopharmacy.vishwam.ui.home.apna.survey.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApnaSurveyLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.ApnaSurveyCallback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ApnaSurveyAdapter(
    val mContext: Context,
    var approveList: ArrayList<SurveyListResponse.Row>,
    val mClicklistner: ApnaSurveyCallback,

    ) :

    RecyclerView.Adapter<ApnaSurveyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val apnaSurveyLayoutBinding: ApnaSurveyLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.apna_survey_layout,
                parent,
                false
            )
        return ViewHolder(apnaSurveyLayoutBinding)

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)
        if (approvedOrders.status!=null) {
            if (approvedOrders.status!!.name.equals("null") && approvedOrders.status!!.name.isNullOrEmpty()) {
                holder.apnaSurveyLayoutBinding.status.setText("-")

            } else {
                holder.apnaSurveyLayoutBinding.status.setText(approvedOrders.status!!.name)

            }
        }
        holder.apnaSurveyLayoutBinding.storeId.setText(approvedOrders.surveyId)
        holder.apnaSurveyLayoutBinding.surveyby.setText(approvedOrders.createdId!!.firstName+" "+approvedOrders.createdId!!.lastName)
        holder.apnaSurveyLayoutBinding.storeName.setText(approvedOrders.location!!.name + " , " + approvedOrders.city!!.name)

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputDateFormat = SimpleDateFormat("dd MMM, yyy hh:mm a")
        holder.apnaSurveyLayoutBinding.surveystart.setText(outputDateFormat.format(inputDateFormat.parse(approvedOrders.createdTime!!)!!))
        holder.apnaSurveyLayoutBinding.surveyended.setText(outputDateFormat.format(inputDateFormat.parse(approvedOrders.modifiedTime!!)!!))

//        holder.apnaSurveyLayoutBinding.surveystart.setText(approvedOrders.createdTime)
//        holder.apnaSurveyLayoutBinding.surveyended.setText(approvedOrders.modifiedTime)


        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeTakenInputFormat = SimpleDateFormat("HH:mm:ss")
        val timeTakenOutputFormat = SimpleDateFormat("HH:mm:ss")

        try {
            val date1 = simpleDateFormat.parse(approvedOrders.createdTime)
            val date2 = simpleDateFormat.parse(approvedOrders.modifiedTime)
            printDifference(date1, date2)
            holder.apnaSurveyLayoutBinding.timeTaken.setText(timeTakenOutputFormat.format(timeTakenInputFormat.parse(printDifference(date1, date2))))

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (approvedOrders.status!=null) {
            if (approvedOrders.status!!.other!=null) {

                if (approvedOrders.status!!.other!!.color.toString()
                        .isNullOrEmpty() || approvedOrders.status!!.other!!.color.toString().equals(
                        "null")
                ) {

                } else {
                    holder.apnaSurveyLayoutBinding.status.setTextColor(Color.parseColor(
                        approvedOrders.status!!.other!!.color))

                }
            }
        }

        if (approvedOrders.status!=null) {
            if (approvedOrders.status!!.icon!=null) {

            if (approvedOrders.status!!.icon.equals("null") || approvedOrders.status!!.icon.isNullOrEmpty()) {

            } else {
                holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundColor(Color.parseColor(
                    approvedOrders.status!!.icon))
            }
            }
        }
//        holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundColor(Color.parseColor(
//            approvedOrders.status!!.icon))
//        if(approvedOrders.equals("APPROVED")){
//            holder.apnaSurveyLayoutBinding.status.setText("Completed")
//            holder.apnaSurveyLayoutBinding.status.setTextColor(Color.parseColor("#39B54A"))
//
//            holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundResource(R.drawable.retro_background_approval)
//
//
//        }else if(approvedOrders.equals("PENDING")){
//            holder.apnaSurveyLayoutBinding.status.setText("In Progress(3/10)")
//            holder.apnaSurveyLayoutBinding.status.setTextColor(Color.parseColor("#f26522"))
//
//            holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundResource(R.drawable.retro_background_pending)
//
//        }

        holder.itemView.setOnClickListener {
            mClicklistner.onClick(position, approvedOrders)
        }


    }

    private fun printDifference(startDate: Date, endDate: Date): String {

        //milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        return "$elapsedHours:$elapsedMinutes:$elapsedSeconds"

    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val apnaSurveyLayoutBinding: ApnaSurveyLayoutBinding) :
        RecyclerView.ViewHolder(apnaSurveyLayoutBinding.root)

}