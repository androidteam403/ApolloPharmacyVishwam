package com.apollopharmacy.vishwam.ui.home.apna.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.HospitalsTableLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.ApartmentData
import com.apollopharmacy.vishwam.ui.home.apna.model.HospitalData

class HospitalAdapter(
    val mContext: Context,
    private val hospitalData: ArrayList<HospitalData>,
): RecyclerView.Adapter<HospitalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hospitalsTableLayoutBinding: HospitalsTableLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.hospitals_table_layout,
            parent,
            false
        )
        return ViewHolder(hospitalsTableLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hospitalsTableLayoutBinding.hospitalName.setText(hospitalData.get(position).hospital)
        holder.hospitalsTableLayoutBinding.beds.setText(hospitalData.get(position).beds)
        holder.hospitalsTableLayoutBinding.speciality.setText(hospitalData.get(position).speciality)
        holder.hospitalsTableLayoutBinding.noOfOpd.setText(hospitalData.get(position).noOfOpd)
        holder.hospitalsTableLayoutBinding.occupancy.setText(hospitalData.get(position).occupancy)
        holder.hospitalsTableLayoutBinding.speciality1.setText(hospitalData.get(position).speciality1)
    }

    override fun getItemCount(): Int {
        return hospitalData.size
    }

    class ViewHolder(val hospitalsTableLayoutBinding: HospitalsTableLayoutBinding):
        RecyclerView.ViewHolder(hospitalsTableLayoutBinding.root)
}