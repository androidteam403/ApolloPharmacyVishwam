package com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterAreasToFocusonBinding

class AreasToFocusOnAdapter(
    var applicationContext: Context,
    var areasList: ArrayList<String>,
    var focusOnName: String,
) :
    RecyclerView.Adapter<AreasToFocusOnAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AreasToFocusOnAdapter.ViewHolder {
        val adapterAreasToFocusonBinding: AdapterAreasToFocusonBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_areas_to_focuson,
                parent,
                false
            )
        return ViewHolder(adapterAreasToFocusonBinding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: AreasToFocusOnAdapter.ViewHolder, position: Int) {
        var areasLists = areasList.get(position)
        holder.areasToFocusOnBinding.areasText.setText(areasLists)
        if(focusOnName.equals("Areas to Focus on")){
            holder.areasToFocusOnBinding.areasText.background = (applicationContext.getDrawable(R.drawable.purple_bg));
        }else{
            holder.areasToFocusOnBinding.areasText.background = (applicationContext.getDrawable(R.drawable.dark_blue_bg));
        }
    }

    override fun getItemCount(): Int {
        return areasList.size
    }


    class ViewHolder(val areasToFocusOnBinding: AdapterAreasToFocusonBinding) :
        RecyclerView.ViewHolder(areasToFocusOnBinding.root)
}