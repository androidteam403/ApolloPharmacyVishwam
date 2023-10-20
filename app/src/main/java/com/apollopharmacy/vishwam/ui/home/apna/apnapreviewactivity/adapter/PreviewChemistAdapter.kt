package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ChemistAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.apollopharmacy.vishwam.util.Utils
import java.text.DecimalFormat

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = chemistData.get(position)
        if (items.chemist != null) {
            if (items.chemist!!.isNotEmpty()) {
                holder.chemistLayoutBinding.name.setText(items.chemist)
            } else {
                holder.chemistLayoutBinding.name.setText("-")
            }
        } else {
            holder.chemistLayoutBinding.name.setText("-")
        }
        if (items.organised != null) {
            if (items.organised!!.name.toString().isNotEmpty() && items.organised!!.name != null) {
                holder.chemistLayoutBinding.organized.setText(items.organised!!.name.toString())
            } else {
                holder.chemistLayoutBinding.organized.setText("-")
            }
        } else {
            holder.chemistLayoutBinding.organized.setText("-")
        }
        if(items!=null && items.orgAvgSale!=null){
            if (items.orgAvgSale.toString().isNotEmpty()) {
                holder.chemistLayoutBinding.avgsale.setText("\u20B9" + DecimalFormat("##,##,##0", Utils.symbols).format(
                    items.orgAvgSale!!.toLong()))
            } else {
                holder.chemistLayoutBinding.avgsale.setText("-")
            }
        }

        if (items.unorganised != null) {
            if (items.unorganised!!.name.toString()
                    .isNotEmpty() && items.unorganised!!.name != null
            ) {
                holder.chemistLayoutBinding.unorganized.setText(items.unorganised!!.name)
            } else {
                holder.chemistLayoutBinding.unorganized.setText("-")
            }
        } else {
            holder.chemistLayoutBinding.unorganized.setText("-")
        }
        if (items.unorgAvgSale.toString().isNotEmpty()) {
            holder.chemistLayoutBinding.unorganisedAvgSale.setText("\u20B9" + DecimalFormat("##,##,##0", Utils.symbols).format(
                items.unorgAvgSale!!.toLong()))
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