package com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPlanogramCategoryBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramActivityCallback
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramCatList
import com.tomergoldst.tooltips.ToolTipsManager

class PlanogramCateoryAdapter(
    var applicationContext: Context,
    var data: MutableList<PlanogramCatList.CatList>,
    var planogramActivityCallback: PlanogramActivityCallback,
    var toolTipsManager: ToolTipsManager,
    var toolTipsManagerCallback: ToolTipsManager.TipListener,
) : RecyclerView.Adapter<PlanogramCateoryAdapter.ViewHolder>() {
    var subCatRecyclerViewShowing: Boolean = false
    lateinit var subCategoryPlanogramAdapter: PlanogramSubCategoryAdapter
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlanogramCateoryAdapter.ViewHolder {
        val adapterPlanogramCategoryBinding: AdapterPlanogramCategoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_planogram_category,
                parent,
                false
            )
        return ViewHolder(adapterPlanogramCategoryBinding)
    }


    override fun onBindViewHolder(holder: PlanogramCateoryAdapter.ViewHolder, position: Int) {
        var categoryList = data.get(position)
        holder.adapterPlanogramCategoryBinding.categoryName.setText(categoryList.name)
        holder.adapterPlanogramCategoryBinding.applyBgLayout.setBackgroundColor(
            Color.parseColor(
                categoryList.colorCode!!
            )
        )

        holder.adapterPlanogramCategoryBinding.categoryLayout.setOnClickListener {
            if (!subCatRecyclerViewShowing) {
                subCatRecyclerViewShowing = true
            } else {
                subCatRecyclerViewShowing = false
            }
            if (subCatRecyclerViewShowing) {

                holder.adapterPlanogramCategoryBinding.recyclerViewButtonLayout.visibility =
                    View.VISIBLE
                subCategoryPlanogramAdapter =
                    PlanogramSubCategoryAdapter(applicationContext, data, planogramActivityCallback, toolTipsManager, toolTipsManagerCallback)
                holder.adapterPlanogramCategoryBinding.subCategoryPlanogramRecyclerView.setLayoutManager(
                    LinearLayoutManager(applicationContext)
                )
                holder.adapterPlanogramCategoryBinding.subCategoryPlanogramRecyclerView.setAdapter(
                    subCategoryPlanogramAdapter
                )

            } else {
                holder.adapterPlanogramCategoryBinding.recyclerViewButtonLayout.visibility =
                    View.GONE
            }


        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val adapterPlanogramCategoryBinding: AdapterPlanogramCategoryBinding) :
        RecyclerView.ViewHolder(adapterPlanogramCategoryBinding.root)
}