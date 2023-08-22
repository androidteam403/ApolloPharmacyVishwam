package com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter

import android.annotation.SuppressLint
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
    var categoryName: String?,
    var categoryPosition: Int,
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

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlanogramSubCategoryAdapter.ViewHolder, position: Int) {
        var question = questionsList.get(position)
        holder.adapterPlanogramSubCategoryBinding.question.text = question.name
        questionsList.get(position).categoryName=categoryName
        if (!question.value.isNullOrEmpty()) {
            holder.adapterPlanogramSubCategoryBinding.progressName.text = question.value
            if (questionsList.get(position).value.equals("NA")) {
                holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_na_no_plano)
                holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_na_planogram)
                holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
                holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                    applicationContext.resources.getColor(R.color.dark_gray)
                )

            } else if (questionsList.get(position).value.equals("NO")) {
                holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_no_planogram)//
                holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                    applicationContext.getDrawable(R.drawable.no_bg_plano)
                holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
                holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                    applicationContext.resources.getColor(R.color.red_no_plano)
                )
            } else if (questionsList.get(position).value.equals("YES")) {
                holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_plano)
                holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_no_stroke_plano)
                holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_layout_plano)
                holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                    applicationContext.resources.getColor(R.color.greenY)
                )

            }
        }

        holder.adapterPlanogramSubCategoryBinding.infoButton.setOnClickListener {
            Tooltip.Builder(holder.adapterPlanogramSubCategoryBinding.infoButton, R.style.Tooltip)
                .setCancelable(true)
                .setDismissOnClick(false)
                .setCornerRadius(20f)
                .setText(question.hintText)
                .build().show()
        }

        holder.adapterPlanogramSubCategoryBinding.naLayout.setOnClickListener {
            holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                applicationContext.getDrawable(R.drawable.bg_na_no_plano)
            holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                applicationContext.getDrawable(R.drawable.bg_na_planogram)
            holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
            holder.adapterPlanogramSubCategoryBinding.progressName.text = "NA"
            holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                applicationContext.resources.getColor(
                    R.color.dark_gray
                )
            )
            questionsList.get(position).value = "NA"
            planogramCateoryCallback.checkAreasToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.checkCategoriesToFocusOn(questionsList, categoryPosition)
        }
        holder.adapterPlanogramSubCategoryBinding.noLayout.setOnClickListener {
            holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                applicationContext.getDrawable(R.drawable.bg_no_planogram)//
            holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                applicationContext.getDrawable(R.drawable.no_bg_plano)
            holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
            holder.adapterPlanogramSubCategoryBinding.progressName.text = "NO"
            holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                applicationContext.resources.getColor(
                    R.color.red_no_plano
                )
            )
            questionsList.get(position).value = "NO"
            planogramCateoryCallback.checkAreasToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.checkCategoriesToFocusOn(questionsList, categoryPosition)
        }
        holder.adapterPlanogramSubCategoryBinding.yesLayout.setOnClickListener {
            holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_plano)
            holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                applicationContext.getDrawable(R.drawable.bg_no_stroke_plano)
            holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_layout_plano)
            holder.adapterPlanogramSubCategoryBinding.progressName.text = "YES"
            holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                applicationContext.resources.getColor(
                    R.color.greenY
                )
            )
            questionsList.get(position).value = "YES"
            planogramCateoryCallback.checkAreasToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.checkCategoriesToFocusOn(questionsList, categoryPosition)
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