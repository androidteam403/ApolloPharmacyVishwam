package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterApartmentsListPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class ApartmentsPreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.Apartment>,
): RecyclerView.Adapter<ApartmentsPreviewAdapter.ViewHolder>() {

    class ViewHolder(val adapterApartmentsListPreviewBinding: AdapterApartmentsListPreviewBinding) :
        RecyclerView.ViewHolder(adapterApartmentsListPreviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterApartmentsListPreviewBinding = DataBindingUtil.inflate<AdapterApartmentsListPreviewBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_apartments_list_preview,
            parent,
            false
        )
        return ViewHolder(adapterApartmentsListPreviewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterApartmentsListPreviewBinding.apartmentTypeText.setText(data[position].type!!.uid)
        holder.adapterApartmentsListPreviewBinding.apartmentsText.setText(data[position].apartments)
        holder.adapterApartmentsListPreviewBinding.noOfHousesText.setText(data[position].noHouses)
        holder.adapterApartmentsListPreviewBinding.distanceText.setText(data[position].distance.toString())
    }

    override fun getItemCount(): Int {
        return data.size
    }
}