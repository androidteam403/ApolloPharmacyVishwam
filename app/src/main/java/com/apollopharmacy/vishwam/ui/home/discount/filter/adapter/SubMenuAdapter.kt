package com.apollopharmacy.vishwam.ui.home.discount.filter.adapter

import android.accounts.AccountManagerCallback
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.discount.FilterData
import com.apollopharmacy.vishwam.databinding.PendencySitesBinding
import com.apollopharmacy.vishwam.databinding.RtoPendencySitesBinding
import com.apollopharmacy.vishwam.databinding.RtoSitesBinding
import com.apollopharmacy.vishwam.databinding.SubmenuFilterAdapterBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SubMenuAdapter(
    val mContext: Context,

    var filterData: List<FilterData>,
) :
    RecyclerView.Adapter<SubMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val submenuFilterAdapterBinding: SubmenuFilterAdapterBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.submenu_filter_adapter,
                parent,
                false
            )
        return ViewHolder(submenuFilterAdapterBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = filterData.get(position)
        var menuFilter = -1
        holder.submenuFilterAdapterBinding.menuTitle.setText(items.MenuTitle)


    }


    override fun getItemCount(): Int {
        return filterData.size
    }

    class ViewHolder(val submenuFilterAdapterBinding: SubmenuFilterAdapterBinding) :
        RecyclerView.ViewHolder(submenuFilterAdapterBinding.root)
}


