package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.VideoAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaNewPreviewCallBack
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class PreviewVideoAdapter(
    val mContext: Context,
    private val videoList: List<SurveyDetailsList.Video>,
    val videoClicklistner: ApnaNewPreviewCallBack,
    val isMobileCreated: Boolean,
    val videoListWeb: ArrayList<SurveyDetailsList.Video>,
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
        if (isMobileCreated) {
            val video = videoList.get(position)
            holder.videoAdapterLayoutBinding.image.stopPlayback()
            holder.videoAdapterLayoutBinding.image.setVideoURI(Uri.parse(video.url))
            holder.videoAdapterLayoutBinding.eyeImageRes.setOnClickListener {
                videoClicklistner.onClick(position, video.url!!)
            }
        } else {
            val video = videoListWeb.get(position)
            holder.videoAdapterLayoutBinding.image.stopPlayback()
            holder.videoAdapterLayoutBinding.image.setVideoURI(Uri.parse(video.url))
            holder.videoAdapterLayoutBinding.eyeImageRes.setOnClickListener {
                videoClicklistner.onClick(position, video.url!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(val videoAdapterLayoutBinding: VideoAdapterLayoutBinding) :
        RecyclerView.ViewHolder(videoAdapterLayoutBinding.root)
}