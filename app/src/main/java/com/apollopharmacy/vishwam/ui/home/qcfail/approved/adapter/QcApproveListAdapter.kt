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
    var approveList: ArrayList<QcListsResponse.Approved>,
    val imageClicklistner: QcListsCallback,
    var qcItemList: ArrayList<QcItemListResponse>,
    var statusList: ArrayList<ActionResponse.Hsitorydetail>,
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



        holder.adapterApproveListBinding.approvebyText.setText(approvedOrders.trackinstatusgdescription)



        if (approvedOrders.dcCode == null) {
            holder.adapterApproveListBinding.locationText.setText("-")

        } else {
            holder.adapterApproveListBinding.locationText.setText("-" + approvedOrders.dcCode)
        }
        if (approvedOrders.storeid == null) {
            holder.adapterApproveListBinding.storeId.setText("-")

        } else {
            holder.adapterApproveListBinding.storeId.setText(approvedOrders.storeid)
        }
        if (approvedOrders.requesteddate == null) {
            holder.adapterApproveListBinding.reqDate.setText("-")

        } else {
            var reqDate = approvedOrders.requesteddate.toString()!!
                .substring(0,
                    Math.min(approvedOrders.requesteddate.toString()!!.length, 10))
            holder.adapterApproveListBinding.reqDate.text = Utlis.formatdate(reqDate)

//            holder.adapterApproveListBinding.reqDate.setText(approvedOrders.requesteddate.toString()!!.substring(0,
//                Math.min(approvedOrders.requesteddate.toString()!!.length, 10)))
        }

        if (approvedOrders.approveddate == null) {
            holder.adapterApproveListBinding.approveDateText.setText("-")

        } else {
            var approveddate = approvedOrders.requesteddate.toString()!!
                .substring(0,
                    Math.min(approvedOrders.requesteddate.toString()!!.length, 10))
            holder.adapterApproveListBinding.approveDateText.text = Utlis.formatdate(approveddate)


//            holder.adapterApproveListBinding.approveDateText.setText(approvedOrders.approveddate.toString()!!
//                .substring(0,
//                    Math.min(approvedOrders.approveddate.toString()!!.length, 10)))
        }
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
        if (approvedOrders.orderno == null) {
            holder.adapterApproveListBinding.orderid.setText("-")
        } else {
            holder.adapterApproveListBinding.orderid.setText(approvedOrders.orderno)

        }

        if (qcItemList.isNotEmpty()) {

            var item = QcItemListResponse()
            for (i in qcItemList) {
                if (i.orderno!!.equals(approveList.get(position).orderno)) {
                    item = i
                }
            }
            var totalPrice = ArrayList<Double>()
            var discount = ArrayList<Double>()
            var qty = ArrayList<Double>()
            var TotalPrice = ArrayList<Double>()

            var Totaldiscount = ArrayList<Double>()
            var totalDiscount: Int
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
                        qt = k.qty!!.toDouble()
                        qty.add(qt)

                    }


                }
            }

            for (i in qty.indices) {
                for (j in totalPrice.indices) {
                    if (i.equals(j)) {
                        TotalPrice.add(qty[i] * totalPrice[j])
                    }
                }
            }

            for (i in qty.indices) {
                for (j in discount.indices) {
                    if (i.equals(j)) {
                        Totaldiscount.add(qty[i] * discount[j])
                    }
                }
            }

            if (totalPrice.isNotEmpty()) {
                holder.adapterApproveListBinding.totalCost.setText(TotalPrice.sum().toString())
            }

            if (discount.isNotEmpty()) {
                holder.adapterApproveListBinding.discountTotal.setText((Totaldiscount.sum()).toString())
            }
            if (qty.isNotEmpty()) {
                holder.adapterApproveListBinding.remainingPayment.setText(String.format("%.2f",
                    (TotalPrice.sum()) - Totaldiscount.sum()))
            }

            holder.adapterApproveListBinding.recyclerView.adapter =
                item.itemlist?.let {
                    QcApprovedOrderDetailsAdapter(mContext,
                        it, position, approveList, imageClicklistner)
                }
            holder.adapterApproveListBinding.recyclerView.scrollToPosition(position)
        }




        holder.adapterApproveListBinding.arrow.setOnClickListener {
            approvedOrders.setisClick(true)
            approvedOrders.orderno?.let { it1 -> imageClicklistner.orderno(position, it1) }
        }
        holder.adapterApproveListBinding.arrowClose.setOnClickListener {
            approvedOrders.setisClick(false)
            approvedOrders.orderno?.let { it1 -> imageClicklistner.notify(position, it1) }

        }
        if (approveList[position].isClick == true) {
            holder.adapterApproveListBinding.approvedbylayout.visibility = View.VISIBLE
            holder.adapterApproveListBinding.arrowClose.visibility = View.VISIBLE
            holder.adapterApproveListBinding.arrow.visibility = View.GONE
            holder.adapterApproveListBinding.extraData.visibility = View.VISIBLE
            holder.adapterApproveListBinding.totalCostlayout.visibility = View.VISIBLE
        } else {
            holder.adapterApproveListBinding.arrowClose.visibility = View.GONE
            holder.adapterApproveListBinding.arrow.visibility = View.VISIBLE
            holder.adapterApproveListBinding.extraData.visibility = View.GONE
            holder.adapterApproveListBinding.totalCostlayout.visibility = View.GONE
            holder.adapterApproveListBinding.approvedbylayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return approveList.size
    }

    class ViewHolder(val adapterApproveListBinding: QcApprovedlayoutBinding) :
        RecyclerView.ViewHolder(adapterApproveListBinding.root)

}