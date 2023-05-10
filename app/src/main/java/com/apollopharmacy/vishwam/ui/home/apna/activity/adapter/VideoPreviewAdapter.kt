package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.VideoAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaSurveyPreviewCallback
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class VideoPreviewAdapter(
    val mContext: Context,
    val mCallback: ApnaSurveyPreviewCallback,
    private val videoList: List<SurveyCreateRequest.VideoMb.Video>,

    ) : RecyclerView.Adapter<VideoPreviewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val videoAdapterLayoutBinding: VideoAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.video_adapter_layout,
            parent,
            false)
        return ViewHolder(videoAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videoList.get(position)
        holder.videoAdapterLayoutBinding.image.setVideoURI(Uri.parse(video.url))

        holder.videoAdapterLayoutBinding.eyeImageRes.setOnClickListener {
            mCallback.onClickPlay(video.url.toString())
        }

    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(val videoAdapterLayoutBinding: VideoAdapterLayoutBinding) :
        RecyclerView.ViewHolder(videoAdapterLayoutBinding.root)
}