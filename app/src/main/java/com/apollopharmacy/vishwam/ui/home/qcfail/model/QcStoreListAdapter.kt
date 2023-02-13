package com.apollopharmacy.vishwam.ui.home.qcfail.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.SubmenuFilterAdapterBinding

class QcStoreListAdapter (
    val mContext: Context,
    var storeList:  ArrayList<QcStoreList.Store>,
    val clicklistner: QcFilterListCallBacks,
) :
    RecyclerView.Adapter<QcStoreListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val qcFilterItemAdapterLayoutBinding: SubmenuFilterAdapterBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.submenu_filter_adapter,
                parent,
                false
            )
        return ViewHolder(qcFilterItemAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items=storeList.get(position)
        holder.qcFilterItemAdapterLayoutBinding.menuTitle.setText(items.siteid+" - "+items.sitename)


    }

    override fun getItemCount(): Int {
        return storeList.size
    }
    class ViewHolder(val qcFilterItemAdapterLayoutBinding: SubmenuFilterAdapterBinding) :
        RecyclerView.ViewHolder(qcFilterItemAdapterLayoutBinding.root)



}