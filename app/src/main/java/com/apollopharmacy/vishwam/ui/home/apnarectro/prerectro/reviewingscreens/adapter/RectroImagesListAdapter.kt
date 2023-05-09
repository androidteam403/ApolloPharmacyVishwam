package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PreRetroImagesLayoutBinding

class RectroImagesListAdapter(
    val mContext: Context,
    var approveList: ArrayList<String>,

    ) :

    RecyclerView.Adapter<RectroImagesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val preRetroImagesLayoutBinding: PreRetroImagesLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.pre_retro_images_layout,
                parent,
                false
            )
        return ViewHolder(preRetroImagesLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)





    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val  preRetroImagesLayoutBinding: PreRetroImagesLayoutBinding) :
        RecyclerView.ViewHolder(preRetroImagesLayoutBinding.root)

}