package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterHospitalsListBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.HospitalData

class HospitalsAdapter(
    var mContext: Context,
    var mCallBack: ApnaNewSurveyCallBack,
    var hospitalsList: ArrayList<HospitalData>,
): RecyclerView.Adapter<HospitalsAdapter.ViewHolder>() {

    class ViewHolder(val adapterHospitalsListBinding: AdapterHospitalsListBinding) :
        RecyclerView.ViewHolder(adapterHospitalsListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterHospitalsListBinding = DataBindingUtil.inflate<AdapterHospitalsListBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_hospitals_list,
            parent,
            false
        )
        return ViewHolder(adapterHospitalsListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.adapterHospitalsListBinding.hospitalName.text = hospitalsList[position].hospitalName
        holder.adapterHospitalsListBinding.hospitalName.text = hospitalsList[position].hospitalName
        holder.adapterHospitalsListBinding.beds.text = hospitalsList[position].beds.toString()
        holder.adapterHospitalsListBinding.speciality.text = hospitalsList[position].speciality
        holder.adapterHospitalsListBinding.noOfOpd.text = hospitalsList[position].noOfOpd.toString()
        holder.adapterHospitalsListBinding.occupancy.text = hospitalsList[position].occupancy.toString()

        holder.adapterHospitalsListBinding.deleteHospital.setOnClickListener {
            mCallBack.onClickDeleteHospital(position)
        }
    }

    override fun getItemCount(): Int {
        return hospitalsList.size
    }
}