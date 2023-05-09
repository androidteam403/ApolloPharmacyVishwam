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
        if (items.chemist!!.isNotEmpty()) {
            holder.chemistLayoutBinding.name.setText(items.chemist)
        } else {
            holder.chemistLayoutBinding.name.setText("-")
        }
        if (items.organised != null) {
            if (items.organised!!.uid.toString().isNotEmpty() && items.organised!!.uid != null) {
                holder.chemistLayoutBinding.organized.setText(items.organised!!.uid.toString())
            } else {
                holder.chemistLayoutBinding.organized.setText("-")
            }
        } else {
            holder.chemistLayoutBinding.organized.setText("-")
        }
        if (items.orgAvgSale.toString().isNotEmpty()) {
            holder.chemistLayoutBinding.avgsale.setText(items.orgAvgSale.toString())
        } else {
            holder.chemistLayoutBinding.avgsale.setText("-")
        }
        if (items.unorganised != null) {
            if (items.unorganised!!.uid.toString().isNotEmpty() && items.unorganised!!.uid != null) {
                holder.chemistLayoutBinding.unorganized.setText(items.unorganised!!.uid)
            } else {
                holder.chemistLayoutBinding.unorganized.setText("-")
            }
        } else {
            holder.chemistLayoutBinding.unorganized.setText("-")
        }
        if (items.unorgAvgSale.toString().isNotEmpty()) {
            holder.chemistLayoutBinding.unorganisedAvgSale.setText(items.unorgAvgSale.toString())
        } else {
            holder.chemistLayoutBinding.unorganisedAvgSale.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return chemistData.size
    }

    class ViewHolder(val chemistLayoutBinding: ChemistAdapterLayoutBinding) :
        RecyclerView.ViewHolder(chemistLayoutBinding.root)
}