package com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcApprovedlayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse
import com.apollopharmacy.vishwam.util.Utlis

class QcApproveListAdapter(
    val mContext: Context,
    var approveList: List<QcListsResponse.Approved>,
    val imageClicklistner: QcListsCallback,
    var qcItemList: ArrayList<QcItemListResponse>,
    var statusList: ArrayList<ActionResponse>,
    var qcapproveList: ArrayList<QcListsResponse.Approved>,

    ) :

    RecyclerView.Adapter<QcApproveListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val adapterApproveListBinding: QcApprovedlayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_approvedlayout,
                parent,
                false
            )
        return ViewHolder(adapterApproveListBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)
        var orderId: String = ""

        var newPos: Int = 0



//        if (approvedOrders.dcCode == null) {
//            holder.adapterApproveListBinding.locationText.setText("-")
//
//        } else {
//            holder.adapterApproveListBinding.locationText.setText("-" + approvedOrders.dcCode)
//        }
        if (approvedOrders.storeid == null) {
            holder.adapterApproveListBinding.storeId.setText("-")

        } else {
            holder.adapterApproveListBinding.storeId.setText(approvedOrders.storeid)
        }
        if (approvedOrders.requesteddate == null) {
            holder.adapterApproveListBinding.postedDate.setText("-")

        } else {
            var reqDate = approvedOrders.requesteddate.toString()!!
                .substring(0,
                    Math.min(approvedOrders.requesteddate.toString()!!.length, 10))
            holder.adapterApproveListBinding.postedDate.text = Utlis.formatdate(reqDate)

//            holder.adapterApproveListBinding.reqDate.setText(approvedOrders.requesteddate.toString()!!.substring(0,
//                Math.min(approvedOrders.requesteddate.toString()!!.length, 10)))
        }

//        if (approvedOrders.approveddate == null) {
//            holder.adapterApproveListBinding.approveDateText.setText("-")
//
//        } else {
//            var approveddate = approvedOrders.approveddate.toString()!!
//                .substring(0,
//                    Math.min(approvedOrders.approveddate.toString()!!.length, 10))
//            holder.adapterApproveListBinding.approveDateText.text = Utlis.formatdate(approveddate)
//
//
////            holder.adapterApproveListBinding.approveDateText.setText(approvedOrders.approveddate.toString()!!
////                .substring(0,
////                    Math.min(approvedOrders.approveddate.toString()!!.length, 10)))
//        }
        if (approvedOrders.custname == null) {
            holder.adapterApproveListBinding.custName.setText("-")

        } else {
            holder.adapterApproveListBinding.custName.setText(approvedOrders.custname)
        }
        if (approvedOrders.mobileno == null) {
            holder.adapterApproveListBinding.custNumber.setText("-")

        } else {
            holder.adapterApproveListBinding.custNumber.setText(approvedOrders.mobileno)
        }
        if (approvedOrders.omsorderno == null) {
            holder.adapterApproveListBinding.orderid.setText("-")
        } else {
            holder.adapterApproveListBinding.orderid.setText(approvedOrders.omsorderno)

        }





        if (qcItemList.isNotEmpty()) {

            var item = QcItemListResponse()
            for (i in qcItemList) {
                if (i.orderno!!.equals(approveList.get(position).orderno)) {
                    orderId = i.orderno!!
                    item = i
                }
            }

//
//            holder.adapterApproveListBinding.recyclerView.adapter =
//                item.itemlist?.let {
//                    QcApprovedOrderDetailsAdapter(
//                        mContext,
//                        it,
//                        position,
//                        approveList,
//                        imageClicklistner,
//                        approvedOrders.omsorderno.toString()
//                    )
//                }
//            holder.adapterApproveListBinding.recyclerView.scrollToPosition(position)



            if (statusList.isNotEmpty()) {
                var itemstatus = ActionResponse()

                for (i in statusList) {
                    if (i.order!!.equals(approveList.get(position).orderno)) {
                        orderId = i.order!!

                        itemstatus = i
                    }
                }


                holder.adapterApproveListBinding.approvedByRecycleview.adapter =
                    itemstatus.hsitorydetails?.let {
                        StatusAdapter(
                            mContext,
                            itemstatus.hsitorydetails as ArrayList<ActionResponse.Hsitorydetail>)

                    }

            }


            var totalPrices = 0.0
            var discounts = 0.0

            if (item.itemlist?.isNotEmpty() == true) {

                for (k in item.itemlist!!) {
                    if (k.qty != null && k.price != null) {
                        totalPrices = totalPrices + ((k.qty.toString()).toDouble() * k.price!!)
                        discounts = discounts + k.discamount!!

//                        discounts = discounts + ((k.qty.toString()).toDouble() * k.discamount!!)
                    }

                }
            }


            if (totalPrices.toString().isNotEmpty()) {
                holder.adapterApproveListBinding.totalCost.setText(" " + String.format("%.2f",
                    totalPrices))

            } else {
                holder.adapterApproveListBinding.totalCost.setText("-")
            }

            if (discounts.toString().isNotEmpty()) {
                holder.adapterApproveListBinding.discountTotal.setText(" " + String.format("%.2f",
                    discounts))
            } else {
                holder.adapterApproveListBinding.discountTotal.setText("-")

            }


            var netPayment = totalPrices - discounts
            if (netPayment.toString().isNotEmpty()) {

                holder.adapterApproveListBinding.remainingPayment.setText(" " + String.format("%.2f",
                    netPayment))
            } else {
                holder.adapterApproveListBinding.remainingPayment.setText("-")
            }


        }








//        holder.itemView.setOnClickListener {
//            if (approvedOrders.isClick) {
//                approvedOrders.setisClick(false)
//                approvedOrders.orderno?.let { it1 ->
//                    imageClicklistner.notify(position, it1)
//                }
//            } else {
//                newPos = position
//                orderId = approvedOrders.orderno.toString()
//
//                approvedOrders.setisClick(true)
//                approvedOrders.orderno?.let { it1 ->
//                    imageClicklistner.orderno(position, it1)
//                }
//            }
//        }
        holder.adapterApproveListBinding.arrow.setOnClickListener {
            newPos = position
            orderId = approvedOrders.orderno.toString()

            approvedOrders.setisClick(true)
            approvedOrders.orderno?.let { it1 -> imageClicklistner.orderno(position, it1) }
        }
        holder.adapterApproveListBinding.arrowClose.setOnClickListener {
            approvedOrders.setisClick(false)
            approvedOrders.orderno?.let { it1 -> imageClicklistner.notify(position, it1) }

        }
//        if (approveList[position].isClick == true) {
//            holder.adapterApproveListBinding.approvedbylayout.visibility = View.VISIBLE
//            holder.adapterApproveListBinding.arrowClose.visibility = View.VISIBLE
//            holder.adapterApproveListBinding.arrow.visibility = View.GONE
//            holder.adapterApproveListBinding.extraData.visibility = View.VISIBLE
//            holder.adapterApproveListBinding.totalCostlayout.visibility = View.VISIBLE
//        }
//        else {
//            holder.adapterApproveListBinding.arrowClose.visibility = View.GONE
//            holder.adapterApproveListBinding.arrow.visibility = View.VISIBLE
//            holder.adapterApproveListBinding.extraData.visibility = View.GONE
//            holder.adapterApproveListBinding.totalCostlayout.visibility = View.GONE
//            holder.adapterApproveListBinding.approvedbylayout.visibility = View.GONE
//        }
    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val adapterApproveListBinding: QcApprovedlayoutBinding) :
        RecyclerView.ViewHolder(adapterApproveListBinding.root)

}