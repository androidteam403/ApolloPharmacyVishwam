package com.apollopharmacy.vishwam.ui.home.planogram.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterListBySiteIdPlanoBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.ListBySiteIdResponse
import com.apollopharmacy.vishwam.ui.home.planogram.fragment.PlanogramCallback

class ListBySiteIdAdapter(var context: Context?, var rows: List<ListBySiteIdResponse.Data.ListData.Row>?, var planogramCallback: PlanogramCallback) : RecyclerView.Adapter<ListBySiteIdAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ListBySiteIdAdapter.ViewHolder {
        val adapterListBySiteIdPlanoBinding: AdapterListBySiteIdPlanoBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_list_by_site_id_plano,
                parent,
                false
            )
        return ViewHolder(adapterListBySiteIdPlanoBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListBySiteIdAdapter.ViewHolder, position: Int) {
        var rowsList = rows!!.get(position)
        holder.adapterListBySiteIdPlanoBinding.storeId.text = rowsList.site!!.site.toString()
        holder.adapterListBySiteIdPlanoBinding.siteName.text = rowsList.branchName.toString()
        holder.adapterListBySiteIdPlanoBinding.lastVisitedDate.text = rowsList.date.toString()
        holder.adapterListBySiteIdPlanoBinding.executiveId.text = rowsList.site!!.executive!!.firstName + rowsList.site!!.executive!!.lastName
        holder.adapterListBySiteIdPlanoBinding.cardViewStore.setOnClickListener {
            planogramCallback.onClickContinue()
        }

    }

    override fun getItemCount(): Int {
    return rows!!.size
    }

    class ViewHolder(val adapterListBySiteIdPlanoBinding: AdapterListBySiteIdPlanoBinding) :
        RecyclerView.ViewHolder(adapterListBySiteIdPlanoBinding.root)
}