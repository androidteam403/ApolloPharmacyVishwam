package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterHospitalsListPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class HospitalsPreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.Hospital>,
): RecyclerView.Adapter<HospitalsPreviewAdapter.ViewHolder>() {

    class ViewHolder(val adapterHospitalsListPreviewBinding: AdapterHospitalsListPreviewBinding) :
        RecyclerView.ViewHolder(adapterHospitalsListPreviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterHospitalsListPreviewBinding = DataBindingUtil.inflate<AdapterHospitalsListPreviewBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_hospitals_list_preview,
            parent,
            false
        )
        return ViewHolder(adapterHospitalsListPreviewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterHospitalsListPreviewBinding.speciality.setText(data[position].speciality!!.uid)
        holder.adapterHospitalsListPreviewBinding.name.setText(data[position].hospitals)
        holder.adapterHospitalsListPreviewBinding.beds.setText(data[position].beds)
        holder.adapterHospitalsListPreviewBinding.occupancy.setText(data[position].occupancy)
        holder.adapterHospitalsListPreviewBinding.noOfOpd.setText(data[position].noOpd)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}