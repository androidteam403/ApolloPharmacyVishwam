package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterApartmentsListBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ApartmentData

class ApartmentTypeItemAdapter(
    var mContext: Context,
    var mCallBack: ApnaNewSurveyCallBack,
    var data: ArrayList<ApartmentData>,
): RecyclerView.Adapter<ApartmentTypeItemAdapter.ViewHolder>() {

    class ViewHolder(val adapterApartmentsListBinding: AdapterApartmentsListBinding) :
        RecyclerView.ViewHolder(adapterApartmentsListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterApartmentsListBinding = DataBindingUtil.inflate<AdapterApartmentsListBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_apartments_list,
            parent,
            false
        )
        return ViewHolder(adapterApartmentsListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterApartmentsListBinding.apartmentTypeText.text = data[position].apartmentType
        holder.adapterApartmentsListBinding.noOfHousesText.text = data[position].noOfHouses.toString()
        holder.adapterApartmentsListBinding.distanceText.text = data[position].distance.toString()

        holder.adapterApartmentsListBinding.deleteApartment.setOnClickListener {
            mCallBack.onClickApartmentDelete(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}