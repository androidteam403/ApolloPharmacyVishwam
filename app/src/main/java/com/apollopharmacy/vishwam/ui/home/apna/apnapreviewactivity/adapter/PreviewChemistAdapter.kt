package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ChemistAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class PreviewChemistAdapter(
    val mContext: Context,
    private val chemistData: ArrayList<SurveyDetailsList.Chemist>,

    ) : RecyclerView.Adapter<PreviewChemistAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val chemistLayoutBinding: ChemistAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.chemist_adapter_layout,
            parent,
            false
        )
        return ViewHolder(chemistLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items=chemistData.get(position)
        holder.chemistLayoutBinding.name.setText(items.chemist)
        holder.chemistLayoutBinding.organized.setText(items.organised!!.uid.toString())
        holder.chemistLayoutBinding.avgsale.setText(items.orgAvgSale.toString())
        holder.chemistLayoutBinding.unorganized.setText(items.unorganised!!.uid)
        holder.chemistLayoutBinding.unorganisedAvgSale.setText(items.unorgAvgSale.toString())
    }

    override fun getItemCount(): Int {
        return chemistData.size
    }

    class ViewHolder(val chemistLayoutBinding: ChemistAdapterLayoutBinding) :
        RecyclerView.ViewHolder(chemistLayoutBinding.root)
}