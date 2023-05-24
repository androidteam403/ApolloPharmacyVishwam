package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.RetroApprovalLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.PreRectroApprovalCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import org.apache.commons.lang3.text.WordUtils
import java.text.SimpleDateFormat

class RectroApproveListAdapter(
    val mContext: Context,
    var approveList: ArrayList<GetRetroPendingAndApproveResponse.Retro>,
    var uniqueApproveList: ArrayList<GetRetroPendingAndApproveResponse.Retro>,

    var subList: Map<String, List<GetRetroPendingAndApproveResponse.Retro>>,
    var approvePendingList: List<List<GetRetroPendingAndApproveResponse.Retro>>?,
    val mClicklistner: PreRectroApprovalCallback,


    ) :

    RecyclerView.Adapter<RectroApproveListAdapter.ViewHolder>() {

    var retroId: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val adapterApproveListBinding: RetroApprovalLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.retro_approval_layout,
                parent,
                false
            )
        return ViewHolder(adapterApproveListBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approvePendingList!!.get(position)

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        val lp1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        lp1.weight=1.9f
        lp.weight=1.1f
        holder.adapterApproveListBinding.preRetroStatusLayout.layoutParams=lp1
        holder.adapterApproveListBinding.preRetroLayout.layoutParams=lp
        holder.adapterApproveListBinding.postRetroStatusLayout.layoutParams=lp1
        holder.adapterApproveListBinding.postRetroLayout.layoutParams=lp
        holder.adapterApproveListBinding.approvalStatusLayout.layoutParams=lp1
        holder.adapterApproveListBinding.approvedStatusLayout.layoutParams=lp
      for (j in approvedOrders!!.indices) {
        if (approvedOrders!!.get(j).stage.equals("PRE-RETRO")){
            approvedOrders.filter { it.status!!.contains("Pending")|| it.status!!.contains("Reshoot") }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            val date = dateFormat.parse(approvedOrders!!.get(j).uploadedDate)
            val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
            holder.adapterApproveListBinding.transactionId.setText(approvedOrders.get(j).retroid)
            holder.adapterApproveListBinding.uploadby.setText(approvedOrders.get(j).uploadedBy)
            holder.adapterApproveListBinding.uploadon.setText(dateNewFormat)
            holder.adapterApproveListBinding.preRectroStage.setText(WordUtils.capitalizeFully(
                approvedOrders.get(j).stage!!.replace("-", " ")))
            holder.adapterApproveListBinding.preRectroStatus.setText(approvedOrders.get(j).status)
            holder.adapterApproveListBinding.storeId.setText(approvedOrders.get(j).store)

            holder.adapterApproveListBinding.preRectroLayout.setOnClickListener { mClicklistner.onClick(
                position,
                j,
                approvePendingList,
                approveList) }
            if (approvedOrders.get(j).status.equals("Pending")){
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.9f
                lp.weight=1.1f
                holder.adapterApproveListBinding.preRetroStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.preRetroLayout.layoutParams=lp
                holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(Color.parseColor(
                    "#feeedb"))
                holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))
                holder.adapterApproveListBinding.preRectroClockImage.visibility =View.VISIBLE
                holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.preRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders.get(j).status.equals("Approved")){
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.9f
                lp.weight=1.1f
                holder.adapterApproveListBinding.preRetroStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.preRetroLayout.layoutParams=lp

                holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(Color.parseColor(
                    "#EBF7ED"))
                holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#39B54A"))
                holder.adapterApproveListBinding.preRectroClockImage.visibility = View.GONE
                holder.adapterApproveListBinding.preRetroLiteGreenTick.visibility = View.GONE

                holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.VISIBLE
                holder.adapterApproveListBinding.preRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders!!.get(j).status.equals("Reshoot")) {
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.9f
                lp.weight=1.1f
                holder.adapterApproveListBinding.preRetroStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.preRetroLayout.layoutParams=lp
                holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(Color.parseColor(
                    "#feeedb"))
                holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))
                holder.adapterApproveListBinding.preRectroClockImage.visibility =View.VISIBLE
                holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.preRetroLiteGreenTick.visibility = View.GONE

                holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.preRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders.get(j).status.equals("Partially Approved")){
                holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(Color.parseColor(
                    "#EBF7ED"))
                holder.adapterApproveListBinding.preRetroLiteGreenTick.visibility = View.VISIBLE

                holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#5EBC5E"))
                holder.adapterApproveListBinding.preRectroClockImage.visibility = View.GONE
                holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.preRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }


        }
        else if (approvedOrders.get(j).stage.equals("POST-RETRO")){
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            val date = dateFormat.parse(approvedOrders.get(j).uploadedDate)
            val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
            holder.adapterApproveListBinding.transactionId.setText(approvedOrders.get(j).retroid)
            holder.adapterApproveListBinding.uploadby.setText(approvedOrders.get(j).uploadedBy)
            holder.adapterApproveListBinding.uploadon.setText(dateNewFormat)
            holder.adapterApproveListBinding.storeId.setText(approvedOrders.get(j).store)
            holder.adapterApproveListBinding.postRectroStage.setText(WordUtils.capitalizeFully(
                approvedOrders.get(j).stage!!.replace("-", " ")))
            holder.adapterApproveListBinding.postRectrostatus.setText(approvedOrders.get(j).status)
            holder.adapterApproveListBinding.postRectroLayout.setOnClickListener { mClicklistner.onClick(
                position,
                j,
                approvePendingList,
                approveList) }

            if (approvedOrders.get(j).status.equals("Pending")){

                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.9f
                lp.weight=1.1f
                holder.adapterApproveListBinding.postRetroStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.postRetroLayout.layoutParams=lp
                holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                    "#feeedb"))
                holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#f26522"))
                holder.adapterApproveListBinding.postRectroClockImage.visibility =View.VISIBLE
                holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.postRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders.get(j).status.equals("Approved")){
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.9f
                lp.weight=1.1f
                holder.adapterApproveListBinding.postRetroStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.postRetroLayout.layoutParams=lp
                holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                    "#EBF7ED"))
                holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#39B54A"))
                holder.adapterApproveListBinding.postRectroClockImage.visibility = View.GONE
                holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.VISIBLE
                holder.adapterApproveListBinding.postRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders.get(j).status.equals("Reshoot")) {
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.9f
                lp.weight=1.1f
                holder.adapterApproveListBinding.postRetroStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.postRetroLayout.layoutParams=lp
                holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                    "#feeedb"))
                holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#f26522"))
                holder.adapterApproveListBinding.postRectroClockImage.visibility =View.VISIBLE
                holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.postRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }

            else if (approvedOrders.get(j).status.equals("Partially Approved")){
                holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                    "#EBF7ED"))
                holder.adapterApproveListBinding.postRetroLiteGreenTick.visibility = View.VISIBLE

                holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#5EBC5E"))
                holder.adapterApproveListBinding.postRectroClockImage.visibility = View.GONE
                holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.postRectroStage.setTextColor(Color.parseColor("#FF000000"))
            }

        }
        else if (approvedOrders.get(j).stage.equals("AFTER-COMPLETION")){
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            val date = dateFormat.parse(approvedOrders.get(j).uploadedDate)
            val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
            holder.adapterApproveListBinding.transactionId.setText(approvedOrders.get(j).retroid)
            holder.adapterApproveListBinding.uploadby.setText(approvedOrders.get(j).uploadedBy)
            holder.adapterApproveListBinding.uploadon.setText(dateNewFormat)
            holder.adapterApproveListBinding.storeId.setText(approvedOrders.get(j).store)
            holder.adapterApproveListBinding.approveStage.setText(WordUtils.capitalizeFully(
                approvedOrders.get(
                    j).stage!!.replace("-", " ")))
            holder.adapterApproveListBinding.approveStatus.setText(approvedOrders.get(j).status)
            holder.adapterApproveListBinding.approveStatusLayout.setOnClickListener { mClicklistner.onClick(
                position,
                j,
                approvePendingList,
                approveList) }
            if (approvedOrders.get(j).status.equals("Pending")){

                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.1f
                lp.weight=1.9f
                holder.adapterApproveListBinding.approvedStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.approvalStatusLayout.layoutParams=lp
                holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(Color.parseColor(
                    "#feeedb"))
                holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))
                holder.adapterApproveListBinding.approveClockImage.visibility =View.VISIBLE
                holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders.get(j).status.equals("Approved")){
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.1f
                lp.weight=1.9f
                holder.adapterApproveListBinding.approvedStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.approvalStatusLayout.layoutParams=lp
                holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(Color.parseColor(
                    "#EBF7ED"))
                holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#39B54A"))
                holder.adapterApproveListBinding.approveClockImage.visibility = View.GONE
                holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.approveGreenImage.visibility = View.VISIBLE
                holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders.get(j).status.equals("Reshoot")) {
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                val lp1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp1.weight=1.1f
                lp.weight=1.9f
                holder.adapterApproveListBinding.approvedStatusLayout.layoutParams=lp1
                holder.adapterApproveListBinding.approvalStatusLayout.layoutParams=lp
                holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(Color.parseColor(
                    "#feeedb"))
                holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))
                holder.adapterApproveListBinding.approveClockImage.visibility =View.VISIBLE
                holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor("#FF000000"))
            }
            else if (approvedOrders.get(j).status.equals("Partially Approved")){
                holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(Color.parseColor(
                    "#EBF7ED"))
                holder.adapterApproveListBinding.approveLiteGreenTick.visibility = View.VISIBLE

                holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#5EBC5E"))
                holder.adapterApproveListBinding.approveClockImage.visibility = View.GONE
                holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor("#FF000000"))
            }

        }
    }
    }



    override fun getItemCount(): Int {

        return approvePendingList!!.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    class ViewHolder(val adapterApproveListBinding: RetroApprovalLayoutBinding) :
        RecyclerView.ViewHolder(adapterApproveListBinding.root)

}