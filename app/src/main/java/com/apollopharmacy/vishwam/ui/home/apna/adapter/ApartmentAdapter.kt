package com.apollopharmacy.vishwam.ui.home.apna.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApartmentsTableLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.ApartmentData

class ApartmentAdapter(
    val mContext: Context,
    private val apartmentData: ArrayList<ApartmentData>,
) : RecyclerView.Adapter<ApartmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val apartmentsTableLayoutBinding: ApartmentsTableLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.apartments_table_layout,
            parent,
            false
        )
        return ViewHolder(apartmentsTableLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apartmentsTableLayoutBinding.apartments.setText(apartmentData.get(position).apartments)
        holder.apartmentsTableLayoutBinding.noOfHouses.setText(apartmentData.get(position).noOfHouses)
        holder.apartmentsTableLayoutBinding.distance.setText(apartmentData.get(position).distance)
        holder.apartmentsTableLayoutBinding.types.setText(apartmentData.get(position).types)
    }

    override fun getItemCount(): Int {
        return apartmentData.size
    }

    class ViewHolder(val apartmentsTableLayoutBinding: ApartmentsTableLayoutBinding) :
        RecyclerView.ViewHolder(apartmentsTableLayoutBinding.root)
}