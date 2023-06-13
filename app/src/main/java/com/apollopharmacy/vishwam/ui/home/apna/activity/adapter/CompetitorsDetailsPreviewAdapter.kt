package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ChemistAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import java.text.DecimalFormat

class CompetitorsDetailsPreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.Chemist>,
) : RecyclerView.Adapter<CompetitorsDetailsPreviewAdapter.ViewHolder>() {

    class ViewHolder(val chemistAdapterLayoutBinding: ChemistAdapterLayoutBinding) :
        RecyclerView.ViewHolder(chemistAdapterLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val chemistAdapterLayoutBinding = DataBindingUtil.inflate<ChemistAdapterLayoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.chemist_adapter_layout,
            parent,
            false
        )
        return ViewHolder(chemistAdapterLayoutBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chemistAdapterLayoutBinding.name.setText(data[position].chemist)
        holder.chemistAdapterLayoutBinding.organized.setText(data[position].organised!!.uid)
        holder.chemistAdapterLayoutBinding.avgsale.setText("\u20B9" + DecimalFormat("##,##,##0").format(
            data[position].orgAvgSale!!.toLong()))
        holder.chemistAdapterLayoutBinding.unorganized.setText(data[position].unorganised!!.uid)
        holder.chemistAdapterLayoutBinding.unorganisedAvgSale.setText("\u20B9" + DecimalFormat("##,##,##0").format(
            data[position].unorgAvgSale!!.toLong()))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}