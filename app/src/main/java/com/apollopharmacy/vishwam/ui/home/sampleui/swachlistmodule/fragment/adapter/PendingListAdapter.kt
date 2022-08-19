package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPendingListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.SwachListFragment
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.SwachhListCallback
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import java.text.SimpleDateFormat

class PendingListAdapter(
    val context: Context?,
    val pendingList: ArrayList<PendingAndApproved>,
    val mCallback: SwachhListCallback
) : RecyclerView.Adapter<PendingListAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val pendingListBinding: AdapterPendingListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_pending_list,
                parent,
                false
            )
        return ViewHolder(pendingListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pendingListBinding.model = pendingList.get(position)
//
//        if( pendingList.get(position).uploadedDate!="") {
//            val strDate = pendingList.get(position).uploadedDate
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
//            val date = dateFormat.parse(strDate)
//            val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
//            pendingList.get(position).uploadedDate = dateNewFormat.toString()
//        }


        holder.pendingListBinding.cardview
            .setOnClickListener {
                mCallback.onClickUpdate(
                    pendingList.get(position)
                )
            }

    }

    override fun getItemCount(): Int {
        return pendingList.size
    }



    class ViewHolder (val pendingListBinding: AdapterPendingListBinding) :
        RecyclerView.ViewHolder(pendingListBinding.root) {

    }


}