package com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcApprovedorderdetailsBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse

class QcApprovedOrderDetailsAdapter(
    val mContext: Context,
    var itemsList: List<QcItemListResponse.Item>,
    var pos: Int,
    var qcApproveList: ArrayList<QcListsResponse.Approved>,
    val imageClicklistner: QcListsCallback,

    ) :
    RecyclerView.Adapter<QcApprovedOrderDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val approvedorderdetailsBinding: QcApprovedorderdetailsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_approvedorderdetails,
                parent,
                false
            )
        return ViewHolder(approvedorderdetailsBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = itemsList.get(position)
        if (items != null)
            holder.approvedorderdetailsBinding.quantityText.setText(items.qty.toString())
        holder.approvedorderdetailsBinding.medicineName.setText(items.itemname)
        holder.approvedorderdetailsBinding.categoryName.setText("- " + items.category)
        holder.approvedorderdetailsBinding.price.setText(items.price.toString())
        holder.approvedorderdetailsBinding.eyeImage.setOnClickListener {
            imageClicklistner.imageData(position,items.orderno.toString(),items.itemname.toString(),items.imageurls.toString())
        }
    }


    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ViewHolder(val approvedorderdetailsBinding: QcApprovedorderdetailsBinding) :
        RecyclerView.ViewHolder(approvedorderdetailsBinding.root)
}

