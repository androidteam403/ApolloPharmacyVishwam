package com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.QcApprovedorderdetailsBinding
import com.apollopharmacy.vishwam.databinding.QcOrderLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import kotlinx.android.synthetic.main.qc_order_layout.*

class QcPendingOrderDetailsAdapter(
    val mContext: Context,
    var itemsList: List<QcItemListResponse.Item>,
    var pos: Int,
    var qcPendingList: ArrayList<QcListsResponse.Pending>,
    val imageClicklistner: QcListsCallback,
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
        var count:Double=0.0
        val items = itemsList.get(position)
        if (items.qty==null){

        }else{
             count= items.qty as Double

        }


        if (items != null)
            if (items.qty==null){
                holder.orderdetailsBinding.quantityText.setText("-")

            }else{
                holder.orderdetailsBinding.quantityText.setText(items.qty.toString())

            }
        holder.orderdetailsBinding.medicineName.setText(items.itemname)
        holder.orderdetailsBinding.categoryName.setText("- " + items.category)
        holder.orderdetailsBinding.price.setText(items.price.toString())
        holder.orderdetailsBinding.approveQtyText.setText(items.qty.toString())


        holder.orderdetailsBinding.priceLayout.setOnClickListener {
//            imageClicklistner.notify(position,items.imageurls.toString())
            imageClicklistner.imageData(position,items.orderno.toString(),items.itemname.toString(),items.imageurls.toString())

        }

        holder.orderdetailsBinding.subtract.setOnClickListener {

            if (count<=0){

            }else{
                count--;
                holder.orderdetailsBinding.approveQtyText.setText(count.toString())
            }

        }

        holder.orderdetailsBinding.add.setOnClickListener {
            count++;
            holder.orderdetailsBinding.approveQtyText.setText(count.toString())
        }


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

