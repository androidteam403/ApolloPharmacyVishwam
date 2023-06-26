package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.HospitalAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class HospitalsPreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.Hospital>,
) : RecyclerView.Adapter<HospitalsPreviewAdapter.ViewHolder>() {

    class ViewHolder(val hospitalAdapterLayoutBinding: HospitalAdapterLayoutBinding) :
        RecyclerView.ViewHolder(hospitalAdapterLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hospitalAdapterLayoutBinding = DataBindingUtil.inflate<HospitalAdapterLayoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.hospital_adapter_layout,
            parent,
            false
        )
        return ViewHolder(hospitalAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data.get(position).speciality!!.uid!!.isNotEmpty()) {
            holder.hospitalAdapterLayoutBinding.speciality.setText(data[position].speciality!!.uid)
        } else {
            holder.hospitalAdapterLayoutBinding.speciality.setText("-")
        }

        if (data.get(position).hospitals!!.isNotEmpty()) {
            holder.hospitalAdapterLayoutBinding.name.setText(data[position].hospitals)
        } else {
            holder.hospitalAdapterLayoutBinding.name.setText("-")
        }

        if (data.get(position).beds!!.isNotEmpty()) {
            holder.hospitalAdapterLayoutBinding.beds.setText(data[position].beds)
        } else {
            holder.hospitalAdapterLayoutBinding.beds.setText("-")
        }

        if (data.get(position).occupancy!!.isNotEmpty()) {
            holder.hospitalAdapterLayoutBinding.occupancy.setText(data[position].occupancy)
        } else {
            holder.hospitalAdapterLayoutBinding.occupancy.setText("-")
        }

        if (data.get(position).noOpd!!.isNotEmpty()) {
            holder.hospitalAdapterLayoutBinding.noOfOpd.setText(data[position].noOpd)
        } else {
            holder.hospitalAdapterLayoutBinding.noOfOpd.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}