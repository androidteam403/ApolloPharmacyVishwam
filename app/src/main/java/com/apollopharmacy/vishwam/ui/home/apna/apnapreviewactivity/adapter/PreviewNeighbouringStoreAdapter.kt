package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApartmentAdapterLayoutBinding
import com.apollopharmacy.vishwam.databinding.HospitalAdapterLayoutBinding
import com.apollopharmacy.vishwam.databinding.NeighbourStoreAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class PreviewNeighbouringStoreAdapter(
    val mContext: Context,
    private val listData: ArrayList<SurveyDetailsList.NeighboringStore>,

    ) : RecyclerView.Adapter<PreviewNeighbouringStoreAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val neighbourStoreAdapterLayoutBinding: NeighbourStoreAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.neighbour_store_adapter_layout,
            parent,
            false
        )
        return ViewHolder(neighbourStoreAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items=listData.get(position)
        holder.neighbourStoreAdapterLayoutBinding.rent.setText(items.rent.toString())

        holder.neighbourStoreAdapterLayoutBinding.name.setText("Neighbouring Store"+ items.uid )

        holder.neighbourStoreAdapterLayoutBinding.sqft.setText(items.sqft!!.toString())

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(val neighbourStoreAdapterLayoutBinding: NeighbourStoreAdapterLayoutBinding) :
        RecyclerView.ViewHolder(neighbourStoreAdapterLayoutBinding.root)
}