package com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcOrderLayoutBinding
import com.apollopharmacy.vishwam.databinding.QcPendingLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.PendingFragmentCallback

class QcPendingOrderDetailsAdapter(
    val mContext: Context,
    val imageClicklistner: QcListsCallback,

    var itemsList: List<QcItemListResponse.Item>,
    var pos: Int,
    var qcPendingList: ArrayList<QcListsResponse.Pending>,
    var pendingFragmentCallback: PendingFragmentCallback,
    var qcPendingListAdapter: QcPendingListAdapter,
    val pendingLayoutBinding: QcPendingLayoutBinding,
) :
    RecyclerView.Adapter<QcPendingOrderDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val orderLayoutBinding: QcOrderLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_order_layout,
                parent,
                false
            )
        return ViewHolder(orderLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var count: Int = 0
        val items = itemsList.get(position)
        if (items.qty == null) {

        } else {
            count = items.qty!!

        }
        if (items != null)
            if (items.qty == null) {
                holder.orderdetailsBinding.quantityText.setText("-")

            } else {
                holder.orderdetailsBinding.quantityText.setText(items.qty.toString())

            }
        if (items.remarks != null) {
            holder.orderdetailsBinding.reason.text = items.remarks.toString()
        } else {
            holder.orderdetailsBinding.reason.text = "Select"
        }
        holder.orderdetailsBinding.medicineName.setText(items.itemname)
        holder.orderdetailsBinding.categoryName.setText("- " + items.category)
        holder.orderdetailsBinding.price.setText(items.price.toString())



        holder.orderdetailsBinding.approveQtyText.setText(items.approvedqty.toString())
        if (items.approvedqty == 0) {
            holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.VISIBLE
            holder.orderdetailsBinding.reasonValueLayout.visibility = View.VISIBLE
        } else {
            holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.GONE
            holder.orderdetailsBinding.reasonValueLayout.visibility = View.GONE
        }


        holder.orderdetailsBinding.approveQtyText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length > 0) {
                    items.approvedqty = p0.toString().toInt()
                    if (items.approvedqty == 0) {
                        holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.VISIBLE
                        holder.orderdetailsBinding.reasonValueLayout.visibility = View.VISIBLE
                    } else {
                        holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.GONE
                        holder.orderdetailsBinding.reasonValueLayout.visibility = View.GONE
                    }
                    qcPendingListAdapter.setQcItemLists(items.orderno!!, pendingLayoutBinding)
                }
            }

        })


        holder.orderdetailsBinding.eyeImage.setOnClickListener {
            imageClicklistner.imageData(position,items.orderno.toString(),items.itemname.toString(),items.imageurls.toString())
        }


        holder.orderdetailsBinding.selectResonItem.setOnClickListener {
            pendingFragmentCallback.onClickReason(pos, position, items.orderno)
        }

        holder.orderdetailsBinding.subtract.setOnClickListener {

            if (count <= 0) {

            } else {
                count--;
                holder.orderdetailsBinding.approveQtyText.setText(count.toString())
            }

        }

        holder.orderdetailsBinding.add.setOnClickListener {
            count++;
            holder.orderdetailsBinding.approveQtyText.setText(count.toString())
        }

//        holder.orderdetailsBinding.approveQtyText.addTextChangedListener(object :TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//
//        })

    }

    fun subtractDiscount(discount: Double): Double {
        if (discount >= 1.00) {
            return discount - 1.00
        }
        return discount
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ViewHolder(val orderdetailsBinding: QcOrderLayoutBinding) :
        RecyclerView.ViewHolder(orderdetailsBinding.root)
}

