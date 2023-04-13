package com.apollopharmacy.vishwam.ui.home.apna.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ChemistTableLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.ChemistData

class ChemistAdapter(
    val mContext: Context,
    private val chemistData: ArrayList<ChemistData>,

    ) : RecyclerView.Adapter<ChemistAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val chemistTableLayoutBinding: ChemistTableLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.chemist_table_layout,
            parent,
            false
        )
        return ViewHolder(chemistTableLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chemistTableLayoutBinding.chemist.setText(chemistData.get(position).chemist)
        holder.chemistTableLayoutBinding.organised.setText(chemistData.get(position).organised)
        holder.chemistTableLayoutBinding.organisedAvgSale.setText(chemistData.get(position).organisedAvgSale)
        holder.chemistTableLayoutBinding.unorganised.setText(chemistData.get(position).unorganised)
        holder.chemistTableLayoutBinding.unorganisedAvgSale.setText(chemistData.get(position).unorganisedAvgSale)
    }

    override fun getItemCount(): Int {
        return chemistData.size
    }

    class ViewHolder(val chemistTableLayoutBinding: ChemistTableLayoutBinding) :
        RecyclerView.ViewHolder(chemistTableLayoutBinding.root)
}