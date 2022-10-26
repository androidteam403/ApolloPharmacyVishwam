package com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcrejectLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback

class QcRejectedListAdapter(
    val mContext: Context,
    var rejectList: ArrayList<Any>,
    val mListner: QcListsCallback,
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
        val orders = rejectList.get(position)

        holder.rejectListBinding.rejectedby.setText((orders as Map<String, String>).getValue("TRACKINSTATUSGDESCRIPTION"))
        holder.rejectListBinding.remarks.setText("Remarks - " + orders.getValue("REMARKSDESC"))
        holder.rejectListBinding.custName.setText((orders as Map<String, String>).getValue("CUSTNAME"))
        holder.rejectListBinding.custNumber.setText("- "+orders.getValue("MOBILENO"))
        holder.rejectListBinding.reqDate.setText(orders.getValue("REQUESTEDDATE").toString()
            .substring(0, Math.min(orders.getValue("REQUESTEDDATE").length, 10)))
        holder.rejectListBinding.locationText.setText(orders.getValue("DC_CODE"))
        holder.rejectListBinding.orderid.setText(orders.getValue("ORDERNO"))
        holder.rejectListBinding.rejectDate.setText(orders.getValue("REJECTEDDATE").toString().substring(0,Math.min(orders.getValue("REJECTEDDATE").length,10)))

        if (qcItemList.isNotEmpty()) {

            var item = QcItemListResponse()
            for (i in qcItemList) {
                if (i.orderno!!.equals((rejectList.get(position) as Map<String, String>).getValue(
                        "ORDERNO"))
                ) {
                    item = i
                }
            }
            var totalPrice = ArrayList<Double>()
            var discount = ArrayList<Double>()
            var qty = ArrayList<Double>()
            var TotalPrice = ArrayList<Double>()
            var Totaldiscount = ArrayList<Double>()
//        var k= items.itemlist?.size

            var totalPrices = 0.0
            var discounts = 0.0
            if (item.itemlist?.isNotEmpty() == true) {
                for (k in item.itemlist!!) {
                    if (!k.qty.toString().isNullOrEmpty() && !k.price.toString().isNullOrEmpty()) {

                        totalPrices = totalPrices + ((k.qty.toString()).toDouble() * k.price!!)
                        discounts = discounts + ((k.qty.toString()).toDouble() * k.discamount!!)
                    }

                }
                println("Total price===============================" + totalPrices)
                println("Discount price===============================" + discounts)
                holder.rejectListBinding.totalCost.setText(totalPrices.toString())
                holder.rejectListBinding.discountTotal.setText(discounts.toString())
                holder.rejectListBinding.remainingPayment.setText(String.format("%.2f",
                    (totalPrices) - discounts))
            }

            holder.rejectListBinding.recyclerView.adapter =
                item.itemlist?.let {
                   QcRejectedOrderDetailsAdapter(mContext,it,
                        position,
                        rejectList)
                }
            holder.rejectListBinding.recyclerView.scrollToPosition(position)
        }
        holder.rejectListBinding.arrow.setOnClickListener {
            mListner.orderno(position, orders.getValue("ORDERNO"))

            holder.rejectListBinding.approvedbylayout.visibility = View.VISIBLE
            holder.rejectListBinding.arrowClose.visibility = View.VISIBLE
            holder.rejectListBinding.arrow.visibility = View.GONE
            holder.rejectListBinding.extraData.visibility = View.VISIBLE
            holder.rejectListBinding.totalCostlayout.visibility = View.VISIBLE

        }
        holder.rejectListBinding.arrowClose.setOnClickListener {
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