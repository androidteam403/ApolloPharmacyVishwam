package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.LayoutNeighbouringStorePreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class NeighbouringStorePreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.NeighboringStore>,
): RecyclerView.Adapter<NeighbouringStorePreviewAdapter.ViewHolder>() {
    class ViewHolder(val layoutNeighbouringStorePreviewBinding: LayoutNeighbouringStorePreviewBinding) :
        RecyclerView.ViewHolder(layoutNeighbouringStorePreviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutNeighbouringStorePreviewBinding = DataBindingUtil.inflate<LayoutNeighbouringStorePreviewBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_neighbouring_store_preview,
            parent,
            false
        )
        return ViewHolder(layoutNeighbouringStorePreviewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data[position].location!!.uid != null) {
            holder.layoutNeighbouringStorePreviewBinding.locationText.setText(data[position].location!!.uid)
        } else {
            holder.layoutNeighbouringStorePreviewBinding.locationText.setText("")
        }
        if (data[position].store != null) {
            holder.layoutNeighbouringStorePreviewBinding.storeText.setText(data[position].store)
        } else {
            holder.layoutNeighbouringStorePreviewBinding.storeText.setText("")
        }
        if (data[position].rent != null) {
            holder.layoutNeighbouringStorePreviewBinding.rentText.setText(data[position].rent.toString())
        } else {
            holder.layoutNeighbouringStorePreviewBinding.rentText.setText("")
        }
        if (data[position].sales != null) {
            holder.layoutNeighbouringStorePreviewBinding.salesText.setText(data[position].sales.toString())
        } else {
            holder.layoutNeighbouringStorePreviewBinding.salesText.setText("")
        }
        if (data[position].sqft != null) {
            holder.layoutNeighbouringStorePreviewBinding.sqFtText.setText(data[position].sqft.toString())
        } else {
            holder.layoutNeighbouringStorePreviewBinding.sqFtText.setText("")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}