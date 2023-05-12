package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterChemistListBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ChemistData

class ChemistAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var chemistList: ArrayList<ChemistData>,
) : RecyclerView.Adapter<ChemistAdapter.ViewHolder>() {

    class ViewHolder(val adapterChemistListBinding: AdapterChemistListBinding) :
        RecyclerView.ViewHolder(adapterChemistListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterChemistListBinding = DataBindingUtil.inflate<AdapterChemistListBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_chemist_list,
            parent,
            false
        )
        return ViewHolder(adapterChemistListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterChemistListBinding.chemistType.text = chemistList[position].chemist
        holder.adapterChemistListBinding.organisedText.text = chemistList[position].organised
        holder.adapterChemistListBinding.organisedAvgSaleText.text = chemistList[position].organisedAvgSale
        holder.adapterChemistListBinding.unorganisedText.text = chemistList[position].unorganised
        holder.adapterChemistListBinding.unorganisedAvgSaleText.text = chemistList[position].unorganisedAvgSale

        holder.adapterChemistListBinding.deleteChemist.setOnClickListener {
            mCallback.onClickDeleteChemist(position)
        }
    }

    override fun getItemCount(): Int {
        return chemistList.size
    }
}