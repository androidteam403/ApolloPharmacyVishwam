package com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPlanogramSubcategoryBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramActivityCallback
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse
import com.tooltip.Tooltip

class PlanogramSubCategoryAdapter(
    var questionsList: ArrayList<PlanogramSurveyQuestionsListResponse.Questions>,
    var applicationContext: Context,
    var planogramCateoryCallback: PlanogramActivityCallback,
) : RecyclerView.Adapter<PlanogramSubCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlanogramSubCategoryAdapter.ViewHolder {
        val adapterPlanogramSubCategoryBinding: AdapterPlanogramSubcategoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_planogram_subcategory,
                parent,
                false
            )
        return ViewHolder(adapterPlanogramSubCategoryBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlanogramSubCategoryAdapter.ViewHolder, position: Int) {
        var question = questionsList.get(position)
        holder.adapterPlanogramSubCategoryBinding.question.text = question.name
        holder.adapterPlanogramSubCategoryBinding.infoButton.setOnClickListener {
            Tooltip.Builder(holder.adapterPlanogramSubCategoryBinding.infoButton, R.style.Tooltip)
                .setCancelable(true)
                .setDismissOnClick(false)
                .setCornerRadius(20f)
                .setText(question.hintText)
                .build().show()
        }
    }

    override fun getItemCount(): Int {
        return questionsList.size//data.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class ViewHolder(val adapterPlanogramSubCategoryBinding: AdapterPlanogramSubcategoryBinding) :
        RecyclerView.ViewHolder(adapterPlanogramSubCategoryBinding.root)
}