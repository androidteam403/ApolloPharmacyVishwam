package com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPlanogramCategoryBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramActivityCallback
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse

class PlanogramCateoryAdapter(
    var rowsList: ArrayList<PlanogramSurveyQuestionsListResponse.Rows>,
    var applicationContext: Context,
    var planogramActivityCallback: PlanogramActivityCallback,
) : RecyclerView.Adapter<PlanogramCateoryAdapter.ViewHolder>() {
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
        var row = rowsList.get(position)
        holder.adapterPlanogramCategoryBinding.categoryName.text = "${row.name}"
        holder.adapterPlanogramCategoryBinding.categoryLayout.setBackgroundColor(
            Color.parseColor(
                row.categoryType!!.color
            )
        )

        if (row.isExpanded) {
            subCategoryPlanogramAdapter =
                PlanogramSubCategoryAdapter(
                    row.questions!!,
                    applicationContext,
                    planogramActivityCallback,
                    row.name,
                    position
                )
            holder.adapterPlanogramCategoryBinding.subCategoryPlanogramRecyclerView.setLayoutManager(
                LinearLayoutManager(applicationContext)
            )
            holder.adapterPlanogramCategoryBinding.subCategoryPlanogramRecyclerView.setAdapter(
                subCategoryPlanogramAdapter
            )

            holder.adapterPlanogramCategoryBinding.recyclerViewButtonLayout.visibility =
                android.view.View.VISIBLE
        } else {
            holder.adapterPlanogramCategoryBinding.recyclerViewButtonLayout.visibility =
                android.view.View.GONE
        }

        holder.itemView.setOnClickListener {
            row.isExpanded = !row.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return rowsList.size
    }

    class ViewHolder(val adapterPlanogramCategoryBinding: AdapterPlanogramCategoryBinding) :
        RecyclerView.ViewHolder(adapterPlanogramCategoryBinding.root)
}