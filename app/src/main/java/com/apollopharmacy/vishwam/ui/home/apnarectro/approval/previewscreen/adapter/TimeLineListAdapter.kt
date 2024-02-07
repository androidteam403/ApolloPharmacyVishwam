package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.TimeLineDupLayoutBinding
import com.apollopharmacy.vishwam.databinding.TimelineLayoutBinding
import com.apollopharmacy.vishwam.util.Utlis

class TimeLineListAdapter(
    val mContext: Context,
    var approveList: List<DateLabelPair>,


    ) :

    RecyclerView.Adapter<TimeLineListAdapter.ViewHolder>() {

    var adapter: ApprovalImagesListAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val timelineLayoutBinding: TimeLineDupLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.time_line_dup_layout,
                parent,
                false
            )
        return ViewHolder(timelineLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val approvedOrders = approveList.get(position)

        holder.timelineLayoutBinding.byLabel.text = approvedOrders.byLabel
        holder.timelineLayoutBinding.byValue.text = approvedOrders.by
        holder.timelineLayoutBinding.dateLabel.text = approvedOrders.dateLabel
        holder.timelineLayoutBinding.dateValue.text = approvedOrders.date
//
////        holder.timelineLayoutBinding.stage.text = WordUtils.capitalizeFully(approvedOrders.stage!!.replace("-", " "))
//
//
//        val dateLabelPairs = mutableListOf<DateLabelPair>()
//        fun addDateLabelPair(date: String?, dateLabel: String, by: String?, byLabel:String?) {
//            if (!date.isNullOrEmpty() && date != "null" && !by.isNullOrEmpty() && by != "null") {
//                val parsedDate = date?.let { Utlis.convertRetroDate(it) }
//                if (parsedDate != null && !by.isNullOrEmpty() && by != "null") {
//                    dateLabelPairs.add(DateLabelPair(parsedDate, dateLabel, by, byLabel))
//                }
//            }
//
//
//
//        }
//        addDateLabelPair(approvedOrders.uploadedDate, "Uploaded Date", approvedOrders.uploadedBy, "Uploaded By :")
//        addDateLabelPair(approvedOrders.executiveApprovedDate, "Executive Approved Date", approvedOrders.executiveApprovedBy, "Executive Approved By :  ")
//        addDateLabelPair(approvedOrders.executiveReshootDate, "Executive Reshoot Date", approvedOrders.executiveReshootBy, "Executive Reshoot By :  ")
//        addDateLabelPair(approvedOrders.managerApprovedDate, "Manager Approved Date", approvedOrders.managerApprovedBy, "Manager Approved By :  ")
//        addDateLabelPair(approvedOrders.managerReshootDate.toString(), "Manager Reshoot Date", approvedOrders.managerReshootBy.toString(), "Manager Reshoot By :  ")
//        addDateLabelPair(approvedOrders.gmApprovedDate, "GM Approved Date", approvedOrders.gmApprovedBy, "GM Approved By :  ")
//        addDateLabelPair(approvedOrders.gmReshootDate, "GM Reshoot Date", approvedOrders.gmReshootBy.toString(), "GM Reshoot By :  ")
//        addDateLabelPair(approvedOrders.ceoApprovedDate.toString(), "CEO Approved Date", approvedOrders.ceoApprovedBy.toString(), "CEO Approved By :  ")
//        addDateLabelPair(approvedOrders.ceoReshootDate.toString(), "CEO Reshoot Date", approvedOrders.ceoReshootBy.toString(), "CEO Reshoot By :  ")
//
////        approvedOrders.uploadedDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it), "Uploaded Date", approvedOrders.uploadedBy)) }
////        approvedOrders.executiveApprovedDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it), "Executive Approved Date", approvedOrders.executiveApprovedBy)) }
////        approvedOrders.executiveReshootDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it), "Executive Reshoot Date", approvedOrders.executiveReshootBy)) }
////        approvedOrders.managerApprovedDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it), "Manager Approved Date", approvedOrders.managerApprovedBy)) }
////        approvedOrders.managerReshootDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it.toString()), "Manager Reshoot Date", approvedOrders.managerReshootBy.toString())) }
////        approvedOrders.gmApprovedDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it), "General Manager Approved Date", approvedOrders.gmApprovedBy)) }
////        approvedOrders.gmReshootDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it), "General Manager Reshoot Date", approvedOrders.gmReshootBy.toString())) }
////        approvedOrders.ceoApprovedDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it.toString()), "Ceo Approved Date", approvedOrders.ceoApprovedBy.toString())) }
////        approvedOrders.ceoReshootDate?.let { dateLabelPairs.add(DateLabelPair(Utlis.convertRetroDate(it.toString()), "Ceo Reshoot Date", approvedOrders.ceoReshootBy.toString())) }
//
//        val sortedDateLabelPairs = dateLabelPairs.sortedBy { it.date }
//
//
//        sortedDateLabelPairs.forEach { pair ->
//            holder.timelineLayoutBinding.date
//
//            // For date and its label
//            if (pair.date != null && pair.dateLabel != null) {
//                var dateText = "${pair.dateLabel}: ${pair.date}"
//                container.addView(createStyledTextView(context, dateText, pair.dateLabel.length))
//            }
//
//            // For 'by' and its label
//            if (pair.by != null && pair.byLabel != null) {
//                var byText = "${pair.byLabel}: ${pair.by}"
//                container.addView(createStyledTextView(context, byText, pair.byLabel.length))
//            }
//        }
//
//        // Utility function to create TextView with styled text
//
//
//
//
//
//
//
//
//
//
//        //        holder.timelineLayoutBinding.uploadBy.text = approvedOrders.uploadedBy
////        holder.timelineLayoutBinding.uploadedDate.text =Utlis.convertRetroDate(approvedOrders.uploadedDate!!)
////        if(approvedOrders.executiveApprovedBy.toString().contains("null")||approvedOrders.executiveApprovedBy.isNullOrEmpty()){
////            holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.executiveApproveDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.executiveApproveByLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.executiveApproveBy.text=approvedOrders.executiveApprovedBy
////            if(approvedOrders.executiveApprovedDate!=null){
////                holder.timelineLayoutBinding.executiveApproveDate.text=Utlis.convertRetroDate(
////                    approvedOrders.executiveApprovedDate!!
////                )
////            }else{
////                holder.timelineLayoutBinding.executiveApproveDate.text = "-"
////            }
////
////        }
////        if(approvedOrders.executiveReshootBy.toString().contains("null")||approvedOrders.executiveReshootBy.isNullOrEmpty()){
////            holder.timelineLayoutBinding.executiveReshootByLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.executiveReshootDateLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.executiveReshootDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.executiveReshootByLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.executiveReshootBy.text=approvedOrders.executiveReshootBy
////            if(approvedOrders.executiveReshootDate!=null){
////                holder.timelineLayoutBinding.executiveReshootDate.text=Utlis.convertRetroDate(
////                    approvedOrders.executiveReshootDate!!
////                )
////            }else{
////                holder.timelineLayoutBinding.executiveReshootDate.text="-"
////            }
////
////        }
////
////        if(approvedOrders.managerApprovedBy.toString().contains("null")||approvedOrders.managerApprovedBy.isNullOrEmpty()){
////            holder.timelineLayoutBinding.managerApproveLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.managerApproveDateLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.managerApproveDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.managerApproveLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.managerApproveBy.text=approvedOrders.managerApprovedBy
////            if(approvedOrders.managerApprovedDate!=null){
////                holder.timelineLayoutBinding.managerApproveDate.text=Utlis.convertRetroDate(
////                    approvedOrders.managerApprovedDate!!
////                )
////            }else{
////                holder.timelineLayoutBinding.managerApproveDate.text = "-"
////            }
////
////        }
////        if(approvedOrders.managerReshootBy.toString().contains("null")||approvedOrders.managerReshootBy.toString().isNullOrEmpty()){
////            holder.timelineLayoutBinding.managerReshootByLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.managerReshootDateLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.managerReshootDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.managerReshootByLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.managerReshootBy.text=approvedOrders.managerReshootBy.toString()
////            holder.timelineLayoutBinding.managerReshootDate.text=Utlis.convertRetroDate(approvedOrders.managerReshootDate.toString()!!)
////        }
////
////        if(approvedOrders.gmApprovedBy.isNullOrEmpty()){
////            holder.timelineLayoutBinding.gmApproveDateLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.gmApproveLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.gmApproveLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.gmApproveDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.gmApproveBy.text=approvedOrders.gmApprovedBy
////            if(approvedOrders.gmApprovedDate!=null){
////                holder.timelineLayoutBinding.gmApproveDate.text=Utlis.convertRetroDate(
////                    approvedOrders.gmApprovedDate!!
////                )
////            }else{
////                holder.timelineLayoutBinding.gmApproveDate.text="-"
////            }
////
////        }
////        if(approvedOrders.gmReshootBy.toString().contains("null")||approvedOrders.gmReshootBy.toString().isNullOrEmpty()){
////            holder.timelineLayoutBinding.gmReshootByLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.gmReshootDateLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.gmReshootByLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.gmReshootDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.gmReshootBy.text=approvedOrders.gmReshootBy.toString()
////            if(approvedOrders.gmReshootDate!=null){
////                holder.timelineLayoutBinding.gmReshootDate.text=Utlis.convertRetroDate(
////                    approvedOrders.gmReshootDate!!
////                )
////            }else{
////                holder.timelineLayoutBinding.gmReshootDate.text="-"
////
////            }
////
////        }
////
////
////        if(approvedOrders.ceoApprovedBy.toString().contains("null")||approvedOrders.ceoApprovedBy.toString().isNullOrEmpty()){
////            holder.timelineLayoutBinding.ceoApproveLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.ceoApproveDateLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.ceoApproveLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.ceoApproveDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.ceoApproveBy.text=approvedOrders.ceoApprovedBy.toString()
////            if(approvedOrders.ceoApprovedDate!=null){
////                holder.timelineLayoutBinding.ceoApproveDate.text=Utlis.convertRetroDate(
////                    approvedOrders.ceoApprovedDate.toString()!!
////                )
////            }
////
////        }
////        if(approvedOrders.ceoReshootBy.toString() == "null" ||approvedOrders.ceoReshootBy.toString().isNullOrEmpty()){
////            holder.timelineLayoutBinding.ceoReshootByLayout.visibility=android.view.View.GONE
////            holder.timelineLayoutBinding.ceoReshootDateLayout.visibility=android.view.View.GONE
////
////        }
////        else{
////            holder.timelineLayoutBinding.ceoReshootDateLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.ceoReshootByLayout.visibility=android.view.View.VISIBLE
////            holder.timelineLayoutBinding.ceoReshootBy.text=approvedOrders.ceoReshootBy.toString()
////            if(approvedOrders.ceoReshootDate!=null){
////                holder.timelineLayoutBinding.ceoReshootDate.text=Utlis.convertRetroDate(
////                    approvedOrders.ceoReshootDate.toString()!!
////                )
////            }else{
////                holder.timelineLayoutBinding.ceoReshootDate.text = "-"
////            }
////
////        }


    }

//    fun createStyledTextView(context: Context, text: String, labelLength: Int): TextView {
//        var spannableString = SpannableString(text).apply {
//            // Apply 12.5sp size to the label
//            setSpan(AbsoluteSizeSpan(12.5.toInt(), true), 0, labelLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            // Apply 12sp size and black color to the value
//            setSpan(AbsoluteSizeSpan(12, true), labelLength, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setSpan(ForegroundColorSpan(Color.BLACK), labelLength, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
//
//        return TextView(context).apply {
//            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            text = spannableString.toString()
//            // Here you might want to apply a default font family, given the limitation
//            typeface = Typeface.DEFAULT
//        }
//    }

    data class DateLabelPair(
        var date: String?,
        val dateLabel: String,
        val by: String?,
        val byLabel: String?
    )


    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val timelineLayoutBinding: TimeLineDupLayoutBinding) :
        RecyclerView.ViewHolder(timelineLayoutBinding.root)

}