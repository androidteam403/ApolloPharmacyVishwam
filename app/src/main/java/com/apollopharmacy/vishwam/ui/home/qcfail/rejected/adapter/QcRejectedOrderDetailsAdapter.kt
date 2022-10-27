package com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcrejectorderdetailsBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback

class QcRejectedOrderDetailsAdapter(
    val mContext: Context,
    var itemsList: List<QcItemListResponse.Item>,
    var pos: Int,
    var qcApproveList: ArrayList<Any>,
    val imageClicklistner: QcListsCallback,
) :
    RecyclerView.Adapter<QcRejectedOrderDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rejectedLayoutBinding: QcrejectorderdetailsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qcrejectorderdetails,
                parent,
                false
            )
        return ViewHolder(rejectedLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = itemsList.get(position)
        if (items != null)
            holder.orderdetailsBinding.quantityText.setText(items.qty.toString())
        holder.orderdetailsBinding.medicineName.setText(items.itemname)
        holder.orderdetailsBinding.categoryName.setText("- " + items.category)
        holder.orderdetailsBinding.price.setText(items.price.toString())
        holder.orderdetailsBinding.discountTotal.setText(items.discamount.toString())
        holder.orderdetailsBinding.eyeImage.setOnClickListener {
            imageClicklistner.imageData(position,
                items.orderno.toString(),
                items.itemname.toString(),
                items.imageurls.toString())
        }


    }


    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ViewHolder(val orderdetailsBinding: QcrejectorderdetailsBinding) :
        RecyclerView.ViewHolder(orderdetailsBinding.root)
}

