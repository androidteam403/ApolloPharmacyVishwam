package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterGetCategoryDetailsBinding
import com.apollopharmacy.vishwam.databinding.AdapterQrRetroReviewBinding
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.adapter.GetCategoryDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.RetroQrReviewCallback


class ImagesAdapterRetroReviewQr(var mContext: Context, var callback: RetroQrReviewCallback) :
    RecyclerView.Adapter<ImagesAdapterRetroReviewQr.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterQrRetroReviewBinding =
            DataBindingUtil.inflate<AdapterQrRetroReviewBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_qr_retro_review,
                parent,
                false)
        return ViewHolder(adapterQrRetroReviewBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterQrRetroReviewBinding.rackNum.setText("Rack " + (position + 1))
    }

    override fun getItemCount(): Int {
        return 7;
    }

    class ViewHolder(var adapterQrRetroReviewBinding: AdapterQrRetroReviewBinding) :
        RecyclerView.ViewHolder(adapterQrRetroReviewBinding.root) {}

}