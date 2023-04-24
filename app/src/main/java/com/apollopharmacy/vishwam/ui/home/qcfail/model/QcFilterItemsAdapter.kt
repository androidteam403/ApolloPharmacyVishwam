package com.apollopharmacy.vishwam.ui.home.qcfail.model

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcApprovedlayoutBinding
import com.apollopharmacy.vishwam.databinding.QcFilterItemAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter.QcApproveListAdapter

class QcFilterItemsAdapter(
    val mContext: Context,
    var itemsList: ArrayList<String>,
    var itemList: ArrayList<MainMenuList>,
    val clicklistner: QcFilterListCallBacks,
) :
    RecyclerView.Adapter<QcFilterItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {

        val qcFilterItemAdapterLayoutBinding: QcFilterItemAdapterLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_filter_item_adapter_layout,
                parent,
                false
            )
        return ViewHolder(qcFilterItemAdapterLayoutBinding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val items=itemList.get(position)
        holder.qcFilterItemAdapterLayoutBinding.menuTitle.setText(items.name)

        holder.itemView.setOnClickListener{
            clicklistner.clickMenu(position,itemList)
        }

        if (itemList[position].isClicked==false){
            holder.qcFilterItemAdapterLayoutBinding.root.setBackgroundResource(R.color.qc_color)
        }
        else if (itemList[position].isClicked==true){
            holder.qcFilterItemAdapterLayoutBinding.root.setBackgroundResource(R.color.white)

        }


//
//        if (menuFilter == position) {
//            holder.qcFilterItemAdapterLayoutBinding.root.setBackgroundResource(R.color.white)
//        } else {
//            holder.qcFilterItemAdapterLayoutBinding.root.setBackgroundResource(R.color.qc_color)
//        }
//        holder.qcFilterItemAdapterLayoutBinding.root.setOnClickListener {
//            if (menuFilter == position) {
//                holder.qcFilterItemAdapterLayoutBinding.root.setBackgroundResource(R.color.white)
//            } else {
//                holder.qcFilterItemAdapterLayoutBinding.root.setBackgroundResource(R.color.faded_click)
//                clicklistner.clickMenu(position,itemsList)
//                menuFilter = position
//                notifyDataSetChanged()
//            }
//        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val qcFilterItemAdapterLayoutBinding: QcFilterItemAdapterLayoutBinding) :
        RecyclerView.ViewHolder(qcFilterItemAdapterLayoutBinding.root)

}