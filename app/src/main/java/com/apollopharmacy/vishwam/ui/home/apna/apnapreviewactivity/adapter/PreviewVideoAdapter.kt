package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApartmentAdapterLayoutBinding
import com.apollopharmacy.vishwam.databinding.HospitalAdapterLayoutBinding
import com.apollopharmacy.vishwam.databinding.VideoAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaNewPreviewCallBack
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback

class PreviewVideoAdapter(
    val mContext: Context,
    private val ListData: ArrayList<SurveyDetailsList.VideoMb>,
    private val videoListData: ArrayList<String>,
    val videoClicklistner: ApnaNewPreviewCallBack,

    ) : RecyclerView.Adapter<PreviewVideoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val videoAdapterLayoutBinding: VideoAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.video_adapter_layout,
            parent,
            false
        )
        return ViewHolder(videoAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items=videoListData.get(position)
        holder.videoAdapterLayoutBinding.image.setVideoURI(Uri.parse(items))
        holder.videoAdapterLayoutBinding.eyeImageRes.setOnClickListener {
            videoClicklistner.onClick(position,items)
        }

    }

    override fun getItemCount(): Int {
        return videoListData.size
    }

    class ViewHolder(val videoAdapterLayoutBinding: VideoAdapterLayoutBinding) :
        RecyclerView.ViewHolder(videoAdapterLayoutBinding.root)
}