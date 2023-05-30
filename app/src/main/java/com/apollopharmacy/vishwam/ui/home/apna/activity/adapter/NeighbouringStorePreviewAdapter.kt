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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data[position].location!!.uid != null) {
            holder.layoutNeighbouringStorePreviewBinding.name.setText("Neighbouring Store " + "(" + data[position].location!!.uid + ")")
        } else {
            holder.layoutNeighbouringStorePreviewBinding.name.setText("Neighbouring Store " + "(" + "-" + ")")
        }
        if (data[position].store != null) {
            holder.layoutNeighbouringStorePreviewBinding.store.setText(data[position].store)
        } else {
            holder.layoutNeighbouringStorePreviewBinding.store.setText("-")
        }
        if (data[position].rent != null) {
            holder.layoutNeighbouringStorePreviewBinding.rent.setText(data[position].rent.toString())
        } else {
            holder.layoutNeighbouringStorePreviewBinding.rent.setText("-")
        }
        if (data[position].sales != null) {
            holder.layoutNeighbouringStorePreviewBinding.sales.setText(data[position].sales.toString())
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