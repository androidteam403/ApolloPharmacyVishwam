package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.RetroApprovalLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.PreRectroApprovalCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import java.text.SimpleDateFormat
import java.util.stream.Collectors

class RectroApproveListAdapter(
    val mContext: Context,
    var approveList: ArrayList<GetRetroPendingAndApproveResponse.Retro>,
    var subList: Map<String, List<GetRetroPendingAndApproveResponse.Retro>>,
    val mClicklistner: PreRectroApprovalCallback,

    ) :

    RecyclerView.Adapter<RectroApproveListAdapter.ViewHolder>() {


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
        val approvedOrders = approveList.get(position)

//        holder.adapterApproveListBinding.ratinglayout.visibility = View.VISIBLE

        holder.adapterApproveListBinding.transactionId.setText(approvedOrders.retroid)
        holder.adapterApproveListBinding.storeId.setText(approvedOrders.store)
        holder.adapterApproveListBinding.uploadby.setText(approvedOrders.uploadedBy)

        val frmt = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        val date = frmt.parse(approvedOrders.uploadedDate)
        val newFrmt = SimpleDateFormat("dd MMM, yyy - hh:mm a").format(date)
        holder.adapterApproveListBinding.uploadon.setText(newFrmt.toString())





        if (subList.containsKey(approvedOrders.retroid)) {


            for (i in subList.getValue("" + approvedOrders.retroid).indices) {
                if (subList.getValue("" + approvedOrders.retroid).get(i).stage!!.equals("PRE-RETRO")
                ) {

                    holder.adapterApproveListBinding.preRectroStage.setText(WordUtils.capitalizeFully(
                        subList.getValue("" + approvedOrders.retroid).get(i).stage!!.replace("-", " ")))
                    holder.adapterApproveListBinding.preRectroStatus.setText(StringUtils.capitalize(
                        subList.getValue("" + approvedOrders.retroid)
                            .get(i).status))
                    if (subList.getValue("" + approvedOrders.retroid).get(i).status!!.toLowerCase()
                            .contains("pen")
                    ) {
                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(Color.parseColor(
                            "#feeedb"))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor(
                            "#f26522"))
                        holder.adapterApproveListBinding.preRectroClockImage.visibility =
                            View.VISIBLE
                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                        holder.adapterApproveListBinding.preRectroStage.setTextColor(Color.parseColor(
                            "#FF000000"))

                    } else if (subList.getValue("" + approvedOrders.retroid)
                            .get(i).status!!.toLowerCase().contains("app")
                    ) {
                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(Color.parseColor(
                            "#EBF7ED"))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor(
                            "#39B54A"))
                        holder.adapterApproveListBinding.preRectroClockImage.visibility = View.GONE
                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.preRectroGreenImage.visibility =
                            View.VISIBLE
                        holder.adapterApproveListBinding.preRectroStage.setTextColor(Color.parseColor(
                            "#FF000000"))


                    } else if (subList.getValue("" + approvedOrders.retroid)
                            .get(i).status!!.toLowerCase().contains("res")
                    ) {
                        holder.adapterApproveListBinding.preRectroLayout.setBackgroundColor(Color.parseColor(
                            "#feeedb"))
                        holder.adapterApproveListBinding.preRectroStatus.setTextColor(Color.parseColor(
                            "#f26522"))
                        holder.adapterApproveListBinding.preRectroClockImage.visibility =
                            View.VISIBLE
                        holder.adapterApproveListBinding.preRetroGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.preRectroGreenImage.visibility = View.GONE
                    }

                    holder.adapterApproveListBinding.preRectroLayout.setOnClickListener {
                        mClicklistner.onClick(i, subList.getValue(""+approvedOrders.retroid))



                    }


                }
            }


            for (j in subList.getValue("" + approvedOrders.retroid).indices) {

                if (subList.getValue("" + approvedOrders.retroid)
                        .get(j).stage!!.toLowerCase()
                        .contains("post")
                ) {
                    if (subList.getValue("" + approvedOrders.retroid).get(j).status!!.toLowerCase()
                            .contains("pen")
                    ) {
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                            "#feeedb"))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor(
                            "#f26522"))
                        holder.adapterApproveListBinding.postRectroStage.setTextColor(Color.parseColor(
                            "#FF000000"))
                        holder.adapterApproveListBinding.postRectroClockImage.visibility =
                            View.VISIBLE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE

                    } else if (subList.getValue("" + approvedOrders.retroid)
                            .get(j).status!!.toLowerCase().contains("app")
                    ) {
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                            "#EBF7ED"))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor(
                            "#39B54A"))
                        holder.adapterApproveListBinding.postRectroStage.setTextColor(Color.parseColor(
                            "#FF000000"))
                        holder.adapterApproveListBinding.postRectroClockImage.visibility = View.GONE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.postRectroGreenImage.visibility =
                            View.VISIBLE
                    } else if (subList.getValue("" + approvedOrders.retroid)
                            .get(j).status!!.toLowerCase().contains("res")
                    ) {
                        holder.adapterApproveListBinding.postRectroLayout.setBackgroundColor(Color.parseColor(
                            "#feeedb"))
                        holder.adapterApproveListBinding.postRectrostatus.setTextColor(Color.parseColor(
                            "#f26522"))
                        holder.adapterApproveListBinding.postRectroClockImage.visibility =
                            View.VISIBLE
                        holder.adapterApproveListBinding.postRetroGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.postRectroGreenImage.visibility = View.GONE
                    }

                    holder.adapterApproveListBinding.postRectroLayout.setOnClickListener {
                        mClicklistner.onClick(j, subList.getValue(""+approvedOrders.retroid))

                    }



                    holder.adapterApproveListBinding.postRectroStage.setText(WordUtils.capitalizeFully(
                        subList.getValue("" + approvedOrders.retroid)
                            .get(j).stage!!.replace("-", " ")))

                    holder.adapterApproveListBinding.postRectrostatus.setText(StringUtils.capitalize(
                        subList.getValue("" + approvedOrders.retroid)
                            .get(j).status))

                }
            }
            for (k in subList.getValue("" + approvedOrders.retroid).indices) {

                if (subList.getValue("" + approvedOrders.retroid)
                        .get(k).stage!!.toLowerCase()
                        .contains("aft")
                ) {
                    if (subList.getValue("" + approvedOrders.retroid).get(k).status!!.toLowerCase()
                            .contains("pen")
                    ) {
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(
                            Color.parseColor("#feeedb"))
                        holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor(
                            "#f26522"))
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE

                        holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor(
                            "#FF000000"))

                    } else if (subList.getValue("" + approvedOrders.retroid)
                            .get(k).status!!.toLowerCase().contains("app")
                    ) {
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(
                            Color.parseColor("#EBF7ED"))
                        holder.adapterApproveListBinding.approveStatus.setTextColor(Color.parseColor(
                            "#39B54A"))
                        holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor(
                            "#FF000000"))
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.GONE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.VISIBLE
                    } else if (subList.getValue("" + approvedOrders.retroid)
                            .get(k).status!!.toLowerCase().contains("res")
                    ) {
                        holder.adapterApproveListBinding.approveStatusLayout.setBackgroundColor(
                            Color.parseColor("#feeedb"))
                        holder.adapterApproveListBinding.approveStage.setTextColor(Color.parseColor(
                            "#f26522"))
                        holder.adapterApproveListBinding.approveClockImage.visibility = View.VISIBLE
                        holder.adapterApproveListBinding.approveGreyTick.visibility = View.GONE

                        holder.adapterApproveListBinding.approveGreenImage.visibility = View.GONE
                    }

                    holder.adapterApproveListBinding.approveStatusLayout.setOnClickListener {
                        mClicklistner.onClick(k, subList.getValue(""+approvedOrders.retroid))

                    }

                    holder.adapterApproveListBinding.approveStage.setText(WordUtils.capitalizeFully(
                        subList.getValue("" + approvedOrders.retroid)
                            .get(k).stage!!.replace("-", " ")))

                    holder.adapterApproveListBinding.approveStatus.setText(StringUtils.capitalize(
                        subList.getValue("" + approvedOrders.retroid)
                            .get(k).status))


                }
            }
        }

    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val adapterApproveListBinding: RetroApprovalLayoutBinding) :
        RecyclerView.ViewHolder(adapterApproveListBinding.root)

}