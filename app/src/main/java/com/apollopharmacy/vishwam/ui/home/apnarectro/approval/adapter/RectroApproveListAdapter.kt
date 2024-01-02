package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
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

        for (j in approvedOrders!!.indices) {
            if (approvedOrders!![j].stage.equals("PRE-RETRO")) {


                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                val date = dateFormat.parse(approvedOrders!!.get(j).uploadedDate!!)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.adapterApproveListBinding.transactionId.text = approvedOrders.get(j).retroid
                holder.adapterApproveListBinding.uploadby.text = approvedOrders.get(j).uploadedBy
                holder.adapterApproveListBinding.uploadon.text = dateNewFormat
                holder.adapterApproveListBinding.preRectroStage.setText(WordUtils.capitalizeFully(
                    approvedOrders[j].stage!!.replace("-", " ")))
                holder.adapterApproveListBinding.preRectroStatus.setText(approvedOrders[j].status)
                holder.adapterApproveListBinding.storeId.text =
                    approvedOrders[j].store!!.split("-").get(0)
                holder.adapterApproveListBinding.storeName.text =
                    approvedOrders[j].store!!.split("-").get(1)

                holder.adapterApproveListBinding.preRectroLayout.setOnClickListener {
                    mClicklistner.onClick(position, j, approvePendingList, approveList,holder.adapterApproveListBinding.preRectroStatus.text.toString())
                }

                if (approvedOrders.get(j).status.equals("Pending")) {
                    holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                    holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))
                    holder.adapterApproveListBinding.preRectroClockImage.visibility = View.VISIBLE
                    holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.preRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                }
                else if (approvedOrders.get(j).status.equals("Approved")) {
                    if (approvedOrders.get(j).hierarchystatus.equals("AWAITING APPROVAL FROM EXECUTIVE")){
                        holder.adapterApproveListBinding.preRectroStatus.setText("Pending at Executive")
                        holder.adapterApproveListBinding.preRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                 else   if (approvedOrders.get(j).hierarchystatus.equals("AWAITING APPROVAL FROM MANAGER")){
                        holder.adapterApproveListBinding.preRectroStatus.setText("Pending at Manager")
                        holder.adapterApproveListBinding.preRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else  if (approvedOrders.get(j).hierarchystatus!!.replace(" ","").equals("AWAITINGAPPROVALFROMGENERALMANAGER")){
                        holder.adapterApproveListBinding.preRectroStatus.setText("Pending at GM")
                        holder.adapterApproveListBinding.preRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else  if (approvedOrders.get(j).hierarchystatus!!.replace(" ","").contains("AWAITINGAPPROVALFROMCEO")){
                        holder.adapterApproveListBinding.preRectroStatus.setText("Pending at CEO")
                        holder.adapterApproveListBinding.preRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else{
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#39B54A"))
                        holder.adapterApproveListBinding.preRectroStatus.setText("Approved")

                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_approved))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#39B54A"))
                        holder.adapterApproveListBinding.preRectroClockImage.visibility = View.GONE
                        holder.adapterApproveListBinding.preRetroLiteGreenTick.visibility = View.GONE

                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.preRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))

                    }

                }
                else if (approvedOrders!!.get(j).status.equals("Reshoot")) {
//                    val lp = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT)
//                    val lp1 = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT)
//                    lp1.weight = 1.9f
//                    lp.weight = 1.1f
//                    holder.adapterApproveListBinding.preRetroStatusLayout.layoutParams = lp1
//                    holder.adapterApproveListBinding.preRetroLayout.layoutParams = lp
                    holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_reshoot))
                    holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#f26522"))
                    holder.adapterApproveListBinding.preRectroClockImage.visibility = View.VISIBLE
                    holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.preRetroLiteGreenTick.visibility = View.GONE

                    holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.preRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                }
                else if (approvedOrders.get(j).status.equals("Partially Approved")) {
                    holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_partially_approved))
                    holder.adapterApproveListBinding.preRetroLiteGreenTick.visibility = View.VISIBLE

                    holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor("#5EBC5E"))
                    holder.adapterApproveListBinding.preRectroClockImage.visibility = View.GONE
                    holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.preRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                }


            }
            else if (approvedOrders.get(j).stage.equals("POST-RETRO")) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                val date = dateFormat.parse(approvedOrders.get(j).uploadedDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.adapterApproveListBinding.transactionId.setText(approvedOrders.get(j).retroid)
                holder.adapterApproveListBinding.uploadby.setText(approvedOrders.get(j).uploadedBy)
                holder.adapterApproveListBinding.uploadon.setText(dateNewFormat)
                holder.adapterApproveListBinding.storeId.text =
                    approvedOrders[j].store!!.split("-").get(0)
                holder.adapterApproveListBinding.storeName.text =
                    approvedOrders[j].store!!.split("-").get(1)
                holder.adapterApproveListBinding.postRectroStage.setText(WordUtils.capitalizeFully(
                    approvedOrders.get(j).stage!!.replace("-", " ")))
                holder.adapterApproveListBinding.postRectrostatus.setText(approvedOrders.get(j).status)
                holder.adapterApproveListBinding.postRectroLayout.setOnClickListener {
                    mClicklistner.onClick(
                        position,
                        j,
                        approvePendingList,
                        approveList,holder.adapterApproveListBinding.postRectrostatus.text.toString())
                }

                if (approvedOrders.get(j).status.equals("Pending")) {

                    holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                        "#feeedb"))
                    holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor(
                        "#f26522"))
                    holder.adapterApproveListBinding.postRectroClockImage.visibility = View.VISIBLE
                    holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.postRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                }
                else if (approvedOrders.get(j).status.equals("Approved")) {

                    if (approvedOrders.get(j).hierarchystatus.equals("AWAITING APPROVAL FROM EXECUTIVE")){
                        holder.adapterApproveListBinding.postRectrostatus.setText("Pending at Executive")
                        holder.adapterApproveListBinding.postRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else   if (approvedOrders.get(j).hierarchystatus.equals("AWAITING APPROVAL FROM MANAGER")){
                        holder.adapterApproveListBinding.postRectrostatus.setText("Pending at Manager")
                        holder.adapterApproveListBinding.postRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else  if (approvedOrders.get(j).hierarchystatus!!.replace(" ","").equals("AWAITINGAPPROVALFROMGENERALMANAGER")){
                        holder.adapterApproveListBinding.postRectrostatus.setText("Pending at GM")
                        holder.adapterApproveListBinding.postRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else  if (approvedOrders.get(j).hierarchystatus!!.replace(" ","").contains("AWAITINGAPPROVALFROMCEO")){
                        holder.adapterApproveListBinding.postRectrostatus.setText("Pending at CEO")
                        holder.adapterApproveListBinding.postRectroClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else{
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                            "#EBF7ED"))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor(
                            "#39B54A"))
                        holder.adapterApproveListBinding.postRectroClockImage.visibility = View.GONE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.postRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))

                    }


                } else if (approvedOrders.get(j).status.equals("Reshoot")) {

                    holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                        "#feeedb"))
                    holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor(
                        "#f26522"))
                    holder.adapterApproveListBinding.postRectroClockImage.visibility = View.VISIBLE
                    holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.postRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                } else if (approvedOrders.get(j).status.equals("Partially Approved")) {
                    holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                        "#EBF7ED"))
                    holder.adapterApproveListBinding.postRetroLiteGreenTick.visibility =
                        View.VISIBLE

                    holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor(
                        "#5EBC5E"))
                    holder.adapterApproveListBinding.postRectroClockImage.visibility = View.GONE
                    holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.postRectroStage.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                }

            } else if (approvedOrders.get(j).stage.equals("AFTER-COMPLETION")) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                val date = dateFormat.parse(approvedOrders.get(j).uploadedDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.adapterApproveListBinding.transactionId.setText(approvedOrders.get(j).retroid)
                holder.adapterApproveListBinding.uploadby.setText(approvedOrders.get(j).uploadedBy)
                holder.adapterApproveListBinding.uploadon.setText(dateNewFormat)
                holder.adapterApproveListBinding.storeId.text =
                    approvedOrders[j].store!!.split("-").get(0)
                holder.adapterApproveListBinding.storeName.text =
                    approvedOrders[j].store!!.split("-").get(1)
                holder.adapterApproveListBinding.approveStage.setText(WordUtils.capitalizeFully(
                    approvedOrders.get(
                        j).stage!!.replace("-", " ")))
                holder.adapterApproveListBinding.approveStatus.setText(approvedOrders.get(j).status)
                holder.adapterApproveListBinding.approveStatusLayout.setOnClickListener {
                    mClicklistner.onClick(
                        position,
                        j,
                        approvePendingList,
                        approveList,holder.adapterApproveListBinding.approveStatus.text.toString())
                }
                if (approvedOrders.get(j).status.equals("Pending")) {

                    holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(Color.parseColor(
                        "#feeedb"))
                    holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))
                    holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                    holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor("#FF000000"))
                } else if (approvedOrders.get(j).status.equals("Approved")) {


                    if (approvedOrders.get(j).hierarchystatus.equals("AWAITING APPROVAL FROM EXECUTIVE")){
                        holder.adapterApproveListBinding.approveStatus.setText("Pending at Executive")
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else   if (approvedOrders.get(j).hierarchystatus.equals("AWAITING APPROVAL FROM MANAGER")){
                        holder.adapterApproveListBinding.approveStatus.setText("Pending at Manager")
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else  if (approvedOrders.get(j).hierarchystatus!!.replace(" ","").equals("AWAITINGAPPROVALFROMGENERALMANAGER")){
                        holder.adapterApproveListBinding.approveStatus.setText("Pending at GM")
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else  if (approvedOrders.get(j).hierarchystatus!!.replace(" ","").contains("AWAITINGAPPROVALFROMCEO")){
                        holder.adapterApproveListBinding.approveStatus.setText("Pending at CEO")
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.prerectro_layout_bg_pending))
                        holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))

                    }
                    else{
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(Color.parseColor(
                            "#EBF7ED"))
                        holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#39B54A"))
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.GONE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor("#FF000000"))
                    }




                } else if (approvedOrders.get(j).status.equals("Reshoot")) {
                    holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(Color.parseColor(
                        "#feeedb"))
                    holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor("#f26522"))
                    holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                    holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE
                    holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                    holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor("#FF000000"))
                } else if (approvedOrders.get(j).status.equals("Partially Approved")) {
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