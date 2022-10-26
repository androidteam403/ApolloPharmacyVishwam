package com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcApprovedlayoutBinding
import com.apollopharmacy.vishwam.databinding.QcPendingLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse

class QcPendingListAdapter(
    val mContext: Context,
    var pendingList: ArrayList<QcListsResponse.Pending>,
    val imageClicklistner: QcListsCallback,
    var qcItemList: ArrayList<QcItemListResponse>,

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pendingOrders=pendingList.get(position)


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
            holder.pendingLayoutBinding.reqDate.setText(pendingOrders.qcfaildate.toString()!!.substring(0,
                Math.min(pendingOrders.qcfaildate.toString()!!.length, 10)))            }

        if (pendingOrders.requesteddate == null) {
            holder.pendingLayoutBinding.postedDate.setText("-")

        } else {
            holder.pendingLayoutBinding.postedDate.setText(pendingOrders.requesteddate.toString()!!.substring(0,
                Math.min(pendingOrders.requesteddate.toString()!!.length, 10)))            }
        if (pendingOrders.custname==null){
            holder.pendingLayoutBinding.custName.setText("-")

        }else{
            holder.pendingLayoutBinding.custName.setText(pendingOrders.custname) }
        if (pendingOrders.mobileno==null){
            holder.pendingLayoutBinding.custNumber.setText("-")

        }else{
            holder.pendingLayoutBinding.custNumber.setText(pendingOrders.mobileno) }
        if (pendingOrders.orderno==null){
            holder.pendingLayoutBinding.orderid.setText("-")
        }else{
            holder.pendingLayoutBinding.orderid.setText(pendingOrders.orderno)

        }

        if (qcItemList.isNotEmpty()) {

            var item = QcItemListResponse()
            for (i in qcItemList) {
                if (i.orderno!!.equals(pendingList.get(position).orderno)) {
                    item = i
                }
            }
            var totalPrice = ArrayList<Double>()
            var discount = ArrayList<Double>()
            var qty = ArrayList<Double>()
            var TotalPrice = ArrayList<Double>()
            var Totaldiscount = ArrayList<Double>()
            if (item.itemlist?.isNotEmpty() == true) {
                for (k in item.itemlist!!) {
                    var items: Double
                    var disc: Double
                    var qt: Double

                    items = (k.price!!)
                    totalPrice.add(items)
                    disc = k.discamount!!
                    discount.add(disc)
                    if (k.qty !== null) {
                        qt = k.qty as Double
                        qty.add(qt)

                    }


                }
            }
            for (i in qty.indices){
                for (j in totalPrice.indices){
                    if(i.equals(j)) {
                        TotalPrice.add(qty[i] * totalPrice[j])
                    }
                }
            }

            for (i in qty.indices){
                for (j in discount.indices){
                    if(i.equals(j)) {
                        Totaldiscount.add(qty[i] * discount[j])
                    }
                }
            }

            if (TotalPrice.isNotEmpty()) {
                holder.pendingLayoutBinding.totalCost.setText(TotalPrice.sum().toString())
            }else{
                holder.pendingLayoutBinding.totalCost.setText("-")
            }

            if (Totaldiscount.isNotEmpty()) {
                holder.pendingLayoutBinding.discountTotal.setText((Totaldiscount.sum()).toString())
            }
            else{
                holder.pendingLayoutBinding.discountTotal.setText("-")

            }
            if (qty.isNotEmpty()) {

                holder.pendingLayoutBinding.remainingPayment.setText(String.format("%.2f",
                    (TotalPrice.sum() ) - Totaldiscount.sum()))
            }
            else{
                holder.pendingLayoutBinding.remainingPayment.setText("-")
            }

            holder.pendingLayoutBinding.recyclerView.adapter =
                item.itemlist?.let {
                    QcPendingOrderDetailsAdapter(mContext,
                        it,position,pendingList)
                }
            holder.pendingLayoutBinding.recyclerView.scrollToPosition(position)
        }
        holder.pendingLayoutBinding.acceptClick.setOnClickListener {

            imageClicklistner.accept(position,
                pendingOrders.orderno.toString(),
                holder.pendingLayoutBinding.writeRemarks.text.toString())
        }

        holder.pendingLayoutBinding.rejectClick.setOnClickListener {
            imageClicklistner.reject(position,
                pendingOrders.orderno.toString(),
                holder.pendingLayoutBinding.writeRemarks.text.toString())
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