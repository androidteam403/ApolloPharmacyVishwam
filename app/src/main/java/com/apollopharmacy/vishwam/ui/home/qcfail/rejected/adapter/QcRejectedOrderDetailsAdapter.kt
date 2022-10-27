package com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcrejectorderdetailsBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse

class QcRejectedOrderDetailsAdapter(
    val mContext: Context,
    var itemsList: List<QcItemListResponse.Item>,
    var pos: Int,
    var qcApproveList: ArrayList<QcListsResponse.Reject>,
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


            holder.orderdetailsBinding.reason.setText(items.remarks)
            holder.orderdetailsBinding.quantityText.setText(items.qty.toString())
        holder.orderdetailsBinding.medicineName.setText(items.itemname)
        holder.orderdetailsBinding.categoryName.setText("- " + items.category)
        holder.orderdetailsBinding.price.setText(items.price.toString())
        holder.orderdetailsBinding.discountTotal.setText(items.discamount.toString())
        if (items.imageurls.toString().isNullOrEmpty()){
            holder.orderdetailsBinding.eyeImage.setColorFilter(R.color.dark_grey,android.graphics.PorterDuff.Mode.MULTIPLY)


        }
        else{
            holder.orderdetailsBinding.eyeImage.setOnClickListener {
                if (items.imageurls.toString().isNullOrEmpty()){
                    Toast.makeText(mContext,"No Image Urls", Toast.LENGTH_LONG)
                }
                else{
                    imageClicklistner.imageData(position,items.orderno.toString(),items.itemname.toString(),items.imageurls.toString())
                }

            }
        }

    }


    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ViewHolder(val orderdetailsBinding: QcrejectorderdetailsBinding) :
        RecyclerView.ViewHolder(orderdetailsBinding.root)
}

