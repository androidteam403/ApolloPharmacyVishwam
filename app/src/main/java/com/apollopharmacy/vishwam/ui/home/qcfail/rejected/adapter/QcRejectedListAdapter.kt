package com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcrejectLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter.QcApprovedOrderDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse
import com.apollopharmacy.vishwam.util.Utlis

class QcRejectedListAdapter(
    val mContext: Context,
    val imageClicklistner: QcListsCallback,

    var rejectList: ArrayList<QcListsResponse.Reject>,
    var qcItemList: ArrayList<QcItemListResponse>,

    ) :

    RecyclerView.Adapter<QcRejectedListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val adapterApproveListBinding: QcrejectLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qcreject_layout,
                parent,
                false
            )
        return ViewHolder(adapterApproveListBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = rejectList.get(position)
        var orderId: String=""


        holder.rejectListBinding.remarks.setText(approvedOrders.remarksdesc)
        holder.rejectListBinding.rejectedby.setText(approvedOrders.trackinstatusgdescription)



        if (approvedOrders.dcCode == null) {
            holder.rejectListBinding.locationText.setText("-")

        } else {
            holder.rejectListBinding.locationText.setText("-" + approvedOrders.dcCode)
        }
        if (approvedOrders.storeid == null) {
            holder.rejectListBinding.storeId.setText("-")

        } else {
            holder.rejectListBinding.storeId.setText(approvedOrders.storeid)
        }
        if (approvedOrders.requesteddate == null) {
            holder.rejectListBinding.reqDate.setText("-")

        } else {
            var reqDate = approvedOrders.requesteddate.toString()!!
                .substring(0,
                    Math.min(approvedOrders.requesteddate.toString()!!.length, 10))

            holder.rejectListBinding.reqDate.text = Utlis.formatdate(reqDate)

        }

        if (approvedOrders.approveddate == null) {
            holder.rejectListBinding.rejectDate.setText("-")

        } else {
            var rejDate = approvedOrders.rejecteddate.toString()!!
                .substring(0,
                    Math.min(approvedOrders.rejecteddate.toString()!!.length, 10))

            holder.rejectListBinding.rejectDate.text = Utlis.formatdate(rejDate)


        }
        if (approvedOrders.custname == null) {
            holder.rejectListBinding.custName.setText("-")

        } else {
            holder.rejectListBinding.custName.setText(approvedOrders.custname)
        }
        if (approvedOrders.mobileno == null) {
            holder.rejectListBinding.custNumber.setText("-")

        } else {
            holder.rejectListBinding.custNumber.setText(approvedOrders.mobileno)
        }
        if (approvedOrders.orderno == null) {
            holder.rejectListBinding.orderid.setText("-")
        } else {
            holder.rejectListBinding.orderid.setText(approvedOrders.orderno)

        }

        if (qcItemList.isNotEmpty()) {

            var item = QcItemListResponse()
            for (i in qcItemList) {
                if (i.orderno!!.equals(rejectList.get(position).orderno)) {
                    item = i
                    orderId= i.orderno!!
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
                holder.rejectListBinding.totalCost.setText(" "+totalPrices.toString())
            } else {
                holder.rejectListBinding.totalCost.setText("-")
            }

            if (discounts.toString().isNotEmpty()) {
                holder.rejectListBinding.discountTotal.setText(" "+discounts.toString())
            } else {
                holder.rejectListBinding.discountTotal.setText("-")

            }

            var netPayment = totalPrices - discounts
            if (netPayment.toString().isNotEmpty()) {

                holder.rejectListBinding.remainingPayment.setText(" "+String.format("%.2f",
                    netPayment))
            } else {
                holder.rejectListBinding.remainingPayment.setText("-")
            }

            holder.rejectListBinding.recyclerView.adapter =
                item.itemlist?.let {
                    QcRejectedOrderDetailsAdapter(mContext,
                        it, position, rejectList, imageClicklistner,orderId)
                }
            holder.rejectListBinding.recyclerView.scrollToPosition(position)
        }




        holder.rejectListBinding.arrow.setOnClickListener {
            approvedOrders.setisClick(true)
            approvedOrders.orderno?.let { it1 -> imageClicklistner.orderno(position, it1) }
        }
        holder.rejectListBinding.arrowClose.setOnClickListener {
            approvedOrders.setisClick(false)
            approvedOrders.orderno?.let { it1 -> imageClicklistner.notify(position, it1) }

        }
        if (rejectList[position].isClick == true) {
            holder.rejectListBinding.approvedbylayout.visibility = View.VISIBLE
            holder.rejectListBinding.arrowClose.visibility = View.VISIBLE
            holder.rejectListBinding.arrow.visibility = View.GONE
            holder.rejectListBinding.extraData.visibility = View.VISIBLE
            holder.rejectListBinding.totalCostlayout.visibility = View.VISIBLE
        } else {
            holder.rejectListBinding.arrowClose.visibility = View.GONE
            holder.rejectListBinding.arrow.visibility = View.VISIBLE
            holder.rejectListBinding.extraData.visibility = View.GONE
            holder.rejectListBinding.totalCostlayout.visibility = View.GONE
            holder.rejectListBinding.approvedbylayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return rejectList.size
    }

    class ViewHolder(val rejectListBinding: QcrejectLayoutBinding) :
        RecyclerView.ViewHolder(rejectListBinding.root)

}