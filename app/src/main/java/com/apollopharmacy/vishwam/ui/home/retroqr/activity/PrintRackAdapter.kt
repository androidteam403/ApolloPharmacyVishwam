package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.LayoutPrintRackBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails

class PrintRackAdapter(
    var mContext: Context,
    var reviewImagesList: ArrayList<StoreWiseRackDetails.StoreDetail>
) :
    RecyclerView.Adapter<PrintRackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutPrintRackBinding = DataBindingUtil.inflate<LayoutPrintRackBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_print_rack,
            parent,
            false
        )
        return ViewHolder(layoutPrintRackBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storeDetails = reviewImagesList.get(position)
        holder.layoutPrintRackBinding.model = storeDetails
        if (storeDetails.isRackSelected)
            holder.layoutPrintRackBinding.checkUncheckImage.setImageResource(R.drawable.retroqr_green_check_mark_icon)
        else
            holder.layoutPrintRackBinding.checkUncheckImage.setImageResource(R.drawable.qc_checkbox)
        holder.itemView.setOnClickListener {
            storeDetails.isRackSelected = !storeDetails.isRackSelected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return reviewImagesList.size
    }

    class ViewHolder(var layoutPrintRackBinding: LayoutPrintRackBinding) :
        RecyclerView.ViewHolder(layoutPrintRackBinding.root)
}