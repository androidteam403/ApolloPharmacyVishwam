package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApartmentAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class PreviewApartmentAdapter(
    val mContext: Context,
    private val hospitalListData: ArrayList<SurveyDetailsList.Apartment>,
) : RecyclerView.Adapter<PreviewApartmentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val apartmentAdapterLayoutBinding: ApartmentAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.apartment_adapter_layout,
            parent,
            false
        )
        return ViewHolder(apartmentAdapterLayoutBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = hospitalListData.get(position)
        if (items.apartments!!.isNotEmpty()) {
            holder.apartmentAdapterLayoutBinding.name.setText(items.apartments)
        } else {
            holder.apartmentAdapterLayoutBinding.name.setText("-")
        }
        if (items.noHouses.toString().isNotEmpty()) {
            holder.apartmentAdapterLayoutBinding.noOfHouses.setText(items.noHouses.toString())
        } else {
            holder.apartmentAdapterLayoutBinding.noOfHouses.setText("-")
        }
        if (items.distance.toString().isNotEmpty()) {
            holder.apartmentAdapterLayoutBinding.distance.setText(items.distance!!.toString() + " m")
        } else {
            holder.apartmentAdapterLayoutBinding.distance.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return hospitalListData.size
    }

    class ViewHolder(val apartmentAdapterLayoutBinding: ApartmentAdapterLayoutBinding) :
        RecyclerView.ViewHolder(apartmentAdapterLayoutBinding.root)
}