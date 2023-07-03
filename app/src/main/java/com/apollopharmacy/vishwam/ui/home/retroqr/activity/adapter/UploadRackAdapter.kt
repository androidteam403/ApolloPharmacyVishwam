package com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.UploadRackLayoutBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.RetroQrUploadCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.ImageDto
import java.io.File

class UploadRackAdapter(
    var mContext: Context,
    var mCallback: RetroQrUploadCallback,
) : RecyclerView.Adapter<UploadRackAdapter.ViewHolder>() {

    class ViewHolder(val uploadRackLayoutBinding: UploadRackLayoutBinding) :
        RecyclerView.ViewHolder(uploadRackLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val uploadRackLayoutBinding = DataBindingUtil.inflate<UploadRackLayoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.upload_rack_layout,
            parent,
            false
        )
        return ViewHolder(uploadRackLayoutBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rackCount = position + 1
        holder.uploadRackLayoutBinding.rackCount.text = "Rack $rackCount"
    }

    override fun getItemCount(): Int {
        return 25
    }
}