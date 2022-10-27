package com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcPendingLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.PendingFragmentCallback

class QcPendingListAdapter(
    val mContext: Context,
    var pendingList: ArrayList<QcListsResponse.Pending>,
    val imageClicklistner: QcListsCallback,
    var qcItemList: ArrayList<QcItemListResponse>,
    var pendingFragmentCallback: PendingFragmentCallback,
) :

    RecyclerView.Adapter<QcPendingListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val pendingLayoutBinding: QcPendingLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_pending_layout,
                parent,
                false
            )
        return ViewHolder(pendingLayoutBinding)

    }

    fun setQcItemLists(orderId: String, pendingLayoutBinding: QcPendingLayoutBinding) {
//        for (i in qcItemList){
//            if (i.orderno.equals(itemsList))
//        }
//        this.qcItemList = qcItemLists
        var item = QcItemListResponse()
        for (i in qcItemList) {
            if (i.orderno!!.equals(orderId)) {
                item = i
            }
        }
        if (item.itemlist != null && item.itemlist!!.size > 0) {
            var isAllApproveQtyZero = true
            for (i in item.itemlist!!) {
                if (i.approvedqty != 0) {
                    isAllApproveQtyZero = false
                }
            }
            if (isAllApproveQtyZero) {
                pendingLayoutBinding.rejectClick.alpha = 1F
                pendingLayoutBinding.rejectClick.isEnabled = true


                pendingLayoutBinding.acceptClick.alpha = 0.5F
                pendingLayoutBinding.acceptClick.isEnabled = false
            } else {
                pendingLayoutBinding.rejectClick.alpha = 0.5F
                pendingLayoutBinding.rejectClick.isEnabled = false

                pendingLayoutBinding.acceptClick.alpha = 1F
                pendingLayoutBinding.acceptClick.isEnabled = true
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pendingOrders = pendingList.get(position)


        if (pendingOrders.dcCode == null) {
            holder.pendingLayoutBinding.locationText.setText("-")

        } else {
            holder.pendingLayoutBinding.locationText.setText("-" + pendingOrders.dcCode)
        }
        if (pendingOrders.storeid == null) {
            holder.pendingLayoutBinding.storeId.setText("-")

        } else {
            holder.pendingLayoutBinding.storeId.setText(pendingOrders.storeid)
        }
        if (pendingOrders.qcfaildate == null) {
            holder.pendingLayoutBinding.reqDate.setText("-")

        } else {
            holder.pendingLayoutBinding.reqDate.setText(pendingOrders.qcfaildate.toString()!!
                .substring(0,
                    Math.min(pendingOrders.qcfaildate.toString()!!.length, 10)))
        }

        if (pendingOrders.requesteddate == null) {
            holder.pendingLayoutBinding.postedDate.setText("-")

        } else {
            holder.pendingLayoutBinding.postedDate.setText(pendingOrders.requesteddate.toString()!!
                .substring(0,
                    Math.min(pendingOrders.requesteddate.toString()!!.length, 10)))
        }
        if (pendingOrders.custname == null) {
            holder.pendingLayoutBinding.custName.setText("-")

        } else {
            holder.pendingLayoutBinding.custName.setText(pendingOrders.custname)
        }
        if (pendingOrders.mobileno == null) {
            holder.pendingLayoutBinding.custNumber.setText("-")

        } else {
            holder.pendingLayoutBinding.custNumber.setText(pendingOrders.mobileno)
        }
        if (pendingOrders.orderno == null) {
            holder.pendingLayoutBinding.orderid.setText("-")
        } else {
            holder.pendingLayoutBinding.orderid.setText(pendingOrders.orderno)

        }
        var item = QcItemListResponse()
        if (qcItemList.isNotEmpty()) {


            for (i in qcItemList) {
                if (i.orderno!!.equals(pendingList.get(position).orderno)) {
                    item = i
                }
            }
            var totalPrice = ArrayList<Double>()
            var discount = ArrayList<Double>()
            var qty = ArrayList<Int>()
            var TotalPrice = ArrayList<Double>()
            var Totaldiscount = ArrayList<Double>()


            var totalPrices = 0.0
            var discounts = 0.0

            if (item.itemlist?.isNotEmpty() == true) {
//                for (k in item.itemlist!!) {
//                    var items: Double
//                    var disc: Double
//                    var qt: Int
//
//                    items = (k.price!!)
//                    totalPrice.add(items)
//                    disc = k.discamount!!
//                    discount.add(disc)
//                    if (k.qty !== null) {
//                        qt = k.qty!!
//                        qty.add(qt)
//
//                    }
//
//
//                }

                for (k in item.itemlist!!) {
                    if (k.qty != null && k.price != null) {
                        totalPrices = totalPrices + ((k.qty.toString()).toDouble() * k.price!!)
                        discounts = discounts + ((k.qty.toString()).toDouble() * k.discamount!!)
                    }

                }
            }
//            for (i in qty.indices) {
//                for (j in totalPrice.indices) {
//                    if (i.equals(j)) {
//                        TotalPrice.add(qty[i] * totalPrice[j])
//                    }
//                }
//            }
//
//            for (i in qty.indices) {
//                for (j in discount.indices) {
//                    if (i.equals(j)) {
//                        Totaldiscount.add(qty[i] * discount[j])
//                    }
//                }
//            }

            if (totalPrices.toString().isNotEmpty()) {
                holder.pendingLayoutBinding.totalCost.setText(totalPrices.toString())
            } else {
                holder.pendingLayoutBinding.totalCost.setText("-")
            }

            if (discounts.toString().isNotEmpty()) {
                holder.pendingLayoutBinding.discountTotal.setText(discounts.toString())
            } else {
                holder.pendingLayoutBinding.discountTotal.setText("-")

            }

            var netPayment = totalPrices - discounts
            if (netPayment.toString().isNotEmpty()) {

                holder.pendingLayoutBinding.remainingPayment.setText(String.format("%.2f",
                    netPayment))
            } else {
                holder.pendingLayoutBinding.remainingPayment.setText("-")
            }
//            if (TotalPrice.isNotEmpty()) {
//                holder.pendingLayoutBinding.totalCost.setText(TotalPrice.sum().toString())
//            } else {
//                holder.pendingLayoutBinding.totalCost.setText("-")
//            }
//
//            if (Totaldiscount.isNotEmpty()) {
//                holder.pendingLayoutBinding.discountTotal.setText((Totaldiscount.sum()).toString())
//            } else {
//                holder.pendingLayoutBinding.discountTotal.setText("-")
//
//            }
//            if (qty.isNotEmpty()) {
//
//                holder.pendingLayoutBinding.remainingPayment.setText(String.format("%.2f",
//                    (TotalPrice.sum()) - Totaldiscount.sum()))
//            } else {
//                holder.pendingLayoutBinding.remainingPayment.setText("-")
//            }

            if (item.itemlist != null && item.itemlist!!.size > 0) {
                var isAllApproveQtyZero = true
                for (i in item.itemlist!!) {
                    if (i.approvedqty != 0) {
                        isAllApproveQtyZero = false
                    }
                }
                if (isAllApproveQtyZero) {
                    holder.pendingLayoutBinding.rejectClick.alpha = 1F
                    holder.pendingLayoutBinding.rejectClick.isEnabled = true


                    holder.pendingLayoutBinding.acceptClick.alpha = 0.5F
                    holder.pendingLayoutBinding.acceptClick.isEnabled = false
                } else {
                    holder.pendingLayoutBinding.rejectClick.alpha = 0.5F
                    holder.pendingLayoutBinding.rejectClick.isEnabled = false

                    holder.pendingLayoutBinding.acceptClick.alpha = 1F
                    holder.pendingLayoutBinding.acceptClick.isEnabled = true
                }
            }
            var qcPendingListAdapter: QcPendingListAdapter

            holder.pendingLayoutBinding.recyclerView.adapter =
                item.itemlist?.let {
                    QcPendingOrderDetailsAdapter(mContext,imageClicklistner,
                        it,
                        position,
                        pendingList,
                        pendingFragmentCallback,
                        this,
                        holder.pendingLayoutBinding)
                }
            holder.pendingLayoutBinding.recyclerView.scrollToPosition(position)
        }
        holder.pendingLayoutBinding.acceptClick.setOnClickListener {

            imageClicklistner.accept(position,
                pendingOrders.orderno.toString(),
                holder.pendingLayoutBinding.writeRemarks.text.toString(), item.itemlist!!,
                pendingOrders.storeid!!)
        }

        holder.pendingLayoutBinding.rejectClick.setOnClickListener {
            imageClicklistner.reject(position,
                pendingOrders.orderno.toString(),
                holder.pendingLayoutBinding.writeRemarks.text.toString(), item.itemlist!!,
                pendingOrders.storeid!!)
        }

        if (pendingList[position].isItemChecked) {
            holder.pendingLayoutBinding.checkBox.setImageResource(R.drawable.qcright)
        } else {
            holder.pendingLayoutBinding.checkBox.setImageResource(R.drawable.qc_checkbox)
        }


        holder.pendingLayoutBinding.checkBox.setOnClickListener {
            imageClicklistner.isChecked(pendingList, position)
        }

        holder.pendingLayoutBinding.arrow.setOnClickListener {
            pendingOrders.orderno?.let { imageClicklistner.orderno(position, it) }
            holder.pendingLayoutBinding.arrowClose.visibility = View.VISIBLE
            holder.pendingLayoutBinding.arrow.visibility = View.GONE
            holder.pendingLayoutBinding.extraData.visibility = View.VISIBLE
        }
        holder.pendingLayoutBinding.arrowClose.setOnClickListener {
            holder.pendingLayoutBinding.arrowClose.visibility = View.GONE
            holder.pendingLayoutBinding.arrow.visibility = View.VISIBLE
            holder.pendingLayoutBinding.extraData.visibility = View.GONE

        }

    }

    override fun getItemCount(): Int {
        return pendingList.size
    }

    class ViewHolder(val pendingLayoutBinding: QcPendingLayoutBinding) :
        RecyclerView.ViewHolder(pendingLayoutBinding.root)

}