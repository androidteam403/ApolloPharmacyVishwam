package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.HospitalAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class PreviewHospitalAdapter(
    val mContext: Context,
    private val hospitalListData: ArrayList<SurveyDetailsList.Hospital>,

    ) : RecyclerView.Adapter<PreviewHospitalAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hospitalAdapterLayoutBinding: HospitalAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.hospital_adapter_layout,
            parent,
            false
        )
        return ViewHolder(hospitalAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items=hospitalListData.get(position)
        holder.hospitalAdapterLayoutBinding.name.setText(items.hospitals)
        holder.hospitalAdapterLayoutBinding.beds.setText(items.beds.toString())
        holder.hospitalAdapterLayoutBinding.multispeciality.setText(items.speciality!!.uid.toString())
        holder.hospitalAdapterLayoutBinding.noOfOpd.setText(items.noOpd.toString())
        holder.hospitalAdapterLayoutBinding.occupancy.setText(items.occupancy.toString())
    }

    override fun getItemCount(): Int {
        return hospitalListData.size
    }

    class ViewHolder(val hospitalAdapterLayoutBinding: HospitalAdapterLayoutBinding) :
        RecyclerView.ViewHolder(hospitalAdapterLayoutBinding.root)
}