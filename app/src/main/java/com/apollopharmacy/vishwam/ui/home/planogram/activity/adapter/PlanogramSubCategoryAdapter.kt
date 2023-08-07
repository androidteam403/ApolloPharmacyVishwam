package com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPlanogramSubcategoryBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramActivityCallback
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramCatList
import com.tomergoldst.tooltips.ToolTip
import com.tomergoldst.tooltips.ToolTipsManager

class PlanogramSubCategoryAdapter(
    var applicationContext: Context,
    var data: MutableList<PlanogramCatList.CatList>,
    var planogramCateoryCallback: PlanogramActivityCallback,
    var toolTipsManager: ToolTipsManager,
    var toolTipsManagerCallback: ToolTipsManager.TipListener,
) : RecyclerView.Adapter<PlanogramSubCategoryAdapter.ViewHolder>() {
        var  tooltipView:ImageView?=null;
    var relativeLayout:RelativeLayout?=null
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
        var data = data.get(position)
        tooltipView = holder.adapterPlanogramSubCategoryBinding.infoButton
        relativeLayout=holder.adapterPlanogramSubCategoryBinding.relativeLayout
        holder.adapterPlanogramSubCategoryBinding.infoButton.setOnClickListener{
//           planogramCateoryCallback.showTillTop()
            toolTipsManager= ToolTipsManager(toolTipsManagerCallback);
            displayToolTip(ToolTip.POSITION_RIGHT_TO, ToolTip.ALIGN_CENTER);

        }
         }

    @SuppressLint("ResourceType")
    private fun displayToolTip(positionRightTo: Int, alignCenter: Int) {
//        toolTipsManager.findAndDismiss(tooltipView!!);
        val builder = ToolTip.Builder(applicationContext, tooltipView!!, relativeLayout!!, "Hi", positionRightTo)
        builder.setBackgroundColor(Color.WHITE);
        builder.setTextAppearance(Color.BLACK)
        toolTipsManager.show(builder.build());
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    class ViewHolder(val adapterPlanogramSubCategoryBinding: AdapterPlanogramSubcategoryBinding) :
        RecyclerView.ViewHolder(adapterPlanogramSubCategoryBinding.root)
}