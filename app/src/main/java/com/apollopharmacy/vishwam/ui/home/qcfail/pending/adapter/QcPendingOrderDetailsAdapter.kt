package com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcApprovedorderdetailsBinding
import com.apollopharmacy.vishwam.databinding.QcOrderLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse

class QcPendingOrderDetailsAdapter(
    val mContext: Context,
    var itemsList: List<QcItemListResponse.Item>,
    var pos: Int,
    var qcPendingList: ArrayList<QcListsResponse.Pending>,
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
        val items = itemsList.get(position)
        if (items != null)
            holder.orderdetailsBinding.quantityText.setText(items.qty.toString())
        holder.orderdetailsBinding.medicineName.setText(items.itemname)
        holder.orderdetailsBinding.categoryName.setText("- " + items.category)
        holder.orderdetailsBinding.price.setText(items.price.toString())
    }


    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ViewHolder(val orderdetailsBinding: QcOrderLayoutBinding) :
        RecyclerView.ViewHolder(orderdetailsBinding.root)
}

