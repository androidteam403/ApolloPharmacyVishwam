package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.LayoutNeighbouringStorePreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import java.text.DecimalFormat

class NeighbouringStorePreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.NeighboringStore>,
) : RecyclerView.Adapter<NeighbouringStorePreviewAdapter.ViewHolder>() {
    class ViewHolder(val layoutNeighbouringStorePreviewBinding: LayoutNeighbouringStorePreviewBinding) :
        RecyclerView.ViewHolder(layoutNeighbouringStorePreviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutNeighbouringStorePreviewBinding =
            DataBindingUtil.inflate<LayoutNeighbouringStorePreviewBinding>(
                LayoutInflater.from(mContext),
                R.layout.layout_neighbouring_store_preview,
                parent,
                false
            )
        return ViewHolder(layoutNeighbouringStorePreviewBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data[position].location!!.name != null) {
            holder.layoutNeighbouringStorePreviewBinding.neighborLocation.setText(data[position].location!!.name)
        } else {
            holder.layoutNeighbouringStorePreviewBinding.neighborLocation.setText("-")
        }
        if (data[position].store != null) {
            holder.layoutNeighbouringStorePreviewBinding.store.setText(data[position].store)
        } else {
            holder.layoutNeighbouringStorePreviewBinding.store.setText("-")
        }
        if (data[position].rent != null) {
            holder.layoutNeighbouringStorePreviewBinding.rent.setText("\u20B9" + DecimalFormat("##,##,##0").format(
                data[position].rent!!.toLong()))
        } else {
            holder.layoutNeighbouringStorePreviewBinding.rent.setText("-")
        }
        if (data[position].sales != null) {
            holder.layoutNeighbouringStorePreviewBinding.sales.setText(data.get(position).sales!!.toString())
//            holder.layoutNeighbouringStorePreviewBinding.sales.setText("\u20B9" + DecimalFormat("##,##,##0").format(
//                data[position].sales!!.toLong()))
        } else {
            holder.layoutNeighbouringStorePreviewBinding.sales.setText("-")
        }
        if (data[position].sqft != null) {
            holder.layoutNeighbouringStorePreviewBinding.sqft.setText(data[position].sqft.toString())
        } else {
            holder.layoutNeighbouringStorePreviewBinding.sqft.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}