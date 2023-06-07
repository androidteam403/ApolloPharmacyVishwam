package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.annotation.SuppressLint
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
) : RecyclerView.Adapter<ApartmentsPreviewAdapter.ViewHolder>() {

    class ViewHolder(val adapterApartmentsListPreviewBinding: AdapterApartmentsListPreviewBinding) :
        RecyclerView.ViewHolder(adapterApartmentsListPreviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterApartmentsListPreviewBinding =
            DataBindingUtil.inflate<AdapterApartmentsListPreviewBinding>(
                LayoutInflater.from(mContext),
                R.layout.adapter_apartments_list_preview,
                parent,
                false
            )
        return ViewHolder(adapterApartmentsListPreviewBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data.get(position).type!!.uid!!.isNotEmpty()) {
            holder.adapterApartmentsListPreviewBinding.type.setText(data[position].type!!.uid)
        } else {
            holder.adapterApartmentsListPreviewBinding.type.setText("-")
        }

        if (data.get(position).apartments!!.isNotEmpty()) {
            holder.adapterApartmentsListPreviewBinding.name.setText(data[position].apartments)
        } else {
            holder.adapterApartmentsListPreviewBinding.name.setText("-")
        }

        if (data.get(position).noHouses!!.isNotEmpty()) {
            holder.adapterApartmentsListPreviewBinding.noOfHouses.setText(data[position].noHouses)
        } else {
            holder.adapterApartmentsListPreviewBinding.noOfHouses.setText("-")
        }

        if (data.get(position).distance!! > 0) {
            holder.adapterApartmentsListPreviewBinding.distance.setText(data[position].distance.toString() + " m")
        } else {
            holder.adapterApartmentsListPreviewBinding.distance.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}