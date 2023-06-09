package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.NeighbourStoreAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import java.text.DecimalFormat

class PreviewNeighbouringStoreAdapter(
    val mContext: Context,
    private val listData: ArrayList<SurveyDetailsList.NeighboringStore>,
) : RecyclerView.Adapter<PreviewNeighbouringStoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val neighbourStoreAdapterLayoutBinding: NeighbourStoreAdapterLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.neighbour_store_adapter_layout,
                parent,
                false
            )
        return ViewHolder(neighbourStoreAdapterLayoutBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = listData.get(position)
        holder.neighbourStoreAdapterLayoutBinding.rent.setText("\u20B9" + DecimalFormat("##,##,##0").format(
            items.rent.toLong()))

        holder.neighbourStoreAdapterLayoutBinding.neighborLocation.setText(items.location!!.name)

        holder.neighbourStoreAdapterLayoutBinding.sqft.setText(items.sqft!!.toString())
        holder.neighbourStoreAdapterLayoutBinding.store.setText(items.store.toString())
        if (items.sales != null) {
            holder.neighbourStoreAdapterLayoutBinding.sales.setText(items.sales!!.toString())
//            holder.neighbourStoreAdapterLayoutBinding.sales.setText("\u20B9" + DecimalFormat("##,##,##0").format(
//                items.sales.toLong()))
        } else {
            holder.neighbourStoreAdapterLayoutBinding.sales.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(val neighbourStoreAdapterLayoutBinding: NeighbourStoreAdapterLayoutBinding) :
        RecyclerView.ViewHolder(neighbourStoreAdapterLayoutBinding.root)
}