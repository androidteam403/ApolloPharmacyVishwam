package com.apollopharmacy.vishwam.ui.home.apna.survey.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApnaSurveyLayoutBinding
import com.apollopharmacy.vishwam.databinding.LoadingProgressLazyBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.ApnaSurveyCallback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class ApnaSurveyAdapter(
    val mContext: Context,
    var approveList: ArrayList<SurveyListResponse.Row>,
    val mClicklistner: ApnaSurveyCallback,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = -1;
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_PROGRESS) {
            val loadingProgressLazyBinding: LoadingProgressLazyBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.loading_progress_lazy,
                parent,
                false
            )
            return LoadingApnaSurveyViewHolder(loadingProgressLazyBinding)
        } else {
            val apnaSurveyLayoutBinding: ApnaSurveyLayoutBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.apna_survey_layout,
                    parent,
                    false
                )
            return ViewHolder(apnaSurveyLayoutBinding)
        }


        /*
                return when (viewType) {
                    VIEW_TYPE_DATA -> {//inflates row layout
                        val apnaSurveyLayoutBinding: ApnaSurveyLayoutBinding =
                            DataBindingUtil.inflate(
                                LayoutInflater.from(parent.context),
                                R.layout.apna_survey_layout,
                                parent,
                                false
                            )
                        return ViewHolder(apnaSurveyLayoutBinding)
                    }
                    GetStorePersonAdapter.VIEW_TYPE_PROGRESS -> {//inflates progressbar layout
                        val loadingProgressLazyBinding: LoadingProgressLazyBinding = DataBindingUtil.inflate(
                            LayoutInflater.from(mContext),
                            R.layout.loading_progress_lazy,
                            parent,
                            false)
                        return LoadingApnaSurveyViewHolder(loadingProgressLazyBinding)
                    }
                    else -> throw IllegalArgumentException("Different View type")
                }
        */


    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            onBindViewHolderItem(holder, position)
        }
    }

    fun onBindViewHolderItem(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)
        if (approvedOrders!!.status != null) {
            if (approvedOrders.status!!.name.equals("null") && approvedOrders.status!!.name.isNullOrEmpty()) {
                holder.apnaSurveyLayoutBinding.status.setText("-")
            } else {
                holder.apnaSurveyLayoutBinding.status.setText(approvedOrders.status!!.name)
            }
        }
//        holder.apnaSurveyLayoutBinding.storeId.setText(approvedOrders.surveyId)
        if (approvedOrders.id != null) {
            holder.apnaSurveyLayoutBinding.storeId.setText(approvedOrders.id)
        } else {
            holder.apnaSurveyLayoutBinding.storeId.setText("-")
        }

        var fName = ""
        var lName = ""
        if (approvedOrders.createdId!!.firstName != null)
            fName =
                approvedOrders.createdId!!.firstName!!

        if (approvedOrders.createdId!!.lastName != null) lName =
            approvedOrders.createdId!!.lastName!!
        holder.apnaSurveyLayoutBinding.surveyby.setText("$fName $lName")
        var locationName = ""
        var cityName = ""
        if (approvedOrders.location != null) {
            if (approvedOrders.location!!.name != null) locationName =
                approvedOrders.location!!.name!!
        }
//        if(approvedOrders.city!=null) {
//            if (approvedOrders.city!!.name != null) cityName = ", ${approvedOrders.city!!.name}"
//        }

//        if (approvedOrders.location!!.name != null) locationName = approvedOrders.location!!.name!!
//        if (approvedOrders.city!!.name != null) cityName = ", ${approvedOrders.city!!.name}"

//        holder.apnaSurveyLayoutBinding.location.setText("$locationName$cityName")
        holder.apnaSurveyLayoutBinding.location.setText("-")

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputDateFormat = SimpleDateFormat("d MMM, yyyy")
        holder.apnaSurveyLayoutBinding.surveystart.setText(
            outputDateFormat.format(
                inputDateFormat.parse(
                    approvedOrders.createdTime!!
                )!!
            )
        )

        if (approvedOrders.status != null) {
            if (approvedOrders.status!!.name != null) {
                if (approvedOrders.status!!.name.toString()
                        .isNotEmpty() && !approvedOrders.status!!.name.toString().equals("null")
                ) {
                    if (approvedOrders.status!!.name.toString().equals("Approved", true)) {
                        holder.apnaSurveyLayoutBinding.surveyEndedLayout.visibility = View.VISIBLE
                        holder.apnaSurveyLayoutBinding.surveyended.setText(
                            outputDateFormat.format(
                                inputDateFormat.parse(approvedOrders.modifiedTime!!)!!
                            )
                        )
                    } else {
                        holder.apnaSurveyLayoutBinding.surveyEndedLayout.visibility = View.GONE
                    }
                } else {
                    holder.apnaSurveyLayoutBinding.surveyEndedLayout.visibility = View.GONE
                }
            } else {
                holder.apnaSurveyLayoutBinding.surveyEndedLayout.visibility = View.GONE
            }
        }

//        holder.apnaSurveyLayoutBinding.surveystart.setText(approvedOrders.createdTime)
//        holder.apnaSurveyLayoutBinding.surveyended.setText(approvedOrders.modifiedTime)


        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        try {
            val date1 = simpleDateFormat.parse(approvedOrders.createdTime)
            val date2 = simpleDateFormat.parse(approvedOrders.modifiedTime)
            printDifference(date1, date2)
//            holder.apnaSurveyLayoutBinding.timeTaken.setText(printDifference(date1, date2))

            if (approvedOrders.status != null) {
                if (approvedOrders.status!!.name != null) {
                    if (approvedOrders.status!!.name.toString()
                            .isNotEmpty() && !approvedOrders.status!!.name.toString().equals("null")
                    ) {
                        if (approvedOrders.status!!.name.toString().equals("Approved", true)) {
                            holder.apnaSurveyLayoutBinding.timeTakenLayout.visibility = View.VISIBLE
                            holder.apnaSurveyLayoutBinding.timeTaken.setText(
                                printDifference(
                                    date1,
                                    date2
                                )
                            )
                        } else {
                            holder.apnaSurveyLayoutBinding.timeTakenLayout.visibility = View.GONE
                        }
                    } else {
                        holder.apnaSurveyLayoutBinding.timeTakenLayout.visibility = View.GONE
                    }
                } else {
                    holder.apnaSurveyLayoutBinding.timeTakenLayout.visibility = View.GONE
                }
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.apnaSurveyLayoutBinding.status.setTextColor(Color.parseColor(approvedOrders.status!!.textColor))//approvedOrders.status!!.textColor
        if (approvedOrders.status != null) {
            if (approvedOrders.status!!.name != null) {
                if (approvedOrders.status!!.name.toString()
                        .isNotEmpty() && !approvedOrders.status!!.name.toString().equals("null")
                ) {
                    if (approvedOrders.status!!.name.toString().equals("New", true)) {
                        holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.apna_project_actionbar_color)
                        )
                    } else {
                        holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundColor(
                            Color.parseColor(
                                approvedOrders.status!!.backgroundColor
                            )
                        )
                    }
                } else {
                }
            } else {
            }
        }
//        holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundColor(Color.parseColor(
//            approvedOrders.status!!.backgroundColor))

//        if (approvedOrders.status!=null) {
//            if (approvedOrders.status!!.other!=null) {
//
//                if (approvedOrders.status!!.other!!.color.toString()
//                        .isNullOrEmpty() || approvedOrders.status!!.other!!.color.toString().equals(
//                        "null")
//                ) {
//
//                } else {
//                    holder.apnaSurveyLayoutBinding.status.setTextColor(Color.parseColor(
//                        approvedOrders.status!!.other!!.color))
//
//                }
//            }
//        }

//        if (approvedOrders.status!=null) {
//            if (approvedOrders.status!!.icon!=null) {
//
//            if (approvedOrders.status!!.icon.equals("null") || approvedOrders.status!!.icon.isNullOrEmpty()) {
//
//            } else {
//                holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundColor(Color.parseColor(
//                    approvedOrders.status!!.icon))
//            }
//            }
//        }
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
        return String.format(
            "%02d:%02d:%02d",
            elapsedHours, elapsedMinutes, elapsedSeconds
        )
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = approveList?.get(position)
        //if data is load, returns PROGRESSBAR viewtype.
        return if (viewtype!!.isLoading != null && viewtype.isLoading.equals("YES")) {
            VIEW_TYPE_PROGRESS
        } else position

    }

    override fun getItemCount(): Int {
        return approveList.size
    }

//    fun setData(data: ArrayList<SurveyListResponse.Row?>) {
//        this.approveList = data
//        notifyDataSetChanged()
//    }

    fun getData(): ArrayList<SurveyListResponse.Row> {
        return approveList
    }

    fun filter(filteredList: ArrayList<SurveyListResponse.Row>) {
        this.approveList = filteredList
        notifyDataSetChanged()
    }

    class ViewHolder(val apnaSurveyLayoutBinding: ApnaSurveyLayoutBinding) :
        RecyclerView.ViewHolder(apnaSurveyLayoutBinding.root)

    class LoadingApnaSurveyViewHolder(loadingProgressLazyBinding: LoadingProgressLazyBinding) :
        RecyclerView.ViewHolder(loadingProgressLazyBinding.root)
}