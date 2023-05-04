package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PreRetroImagesLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.ApprovalReviewCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.bumptech.glide.Glide

class ApprovalImagesListAdapter(
    val mContext: Context,
    var stage: String,
    var approveList: List<List<GetImageUrlResponse.ImageUrl>>,
    val mclickistener: ApprovalReviewCallback,
    var categoryPos: Int,
    var categoryName: String,


    ) :

    RecyclerView.Adapter<ApprovalImagesListAdapter.ViewHolder>() {


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
        if (stage.toLowerCase().contains("pre")) {
        for (j in approvedOrders.indices) {
            if (approvedOrders.get(j).stage.equals("1")){
                holder.itemView.setOnClickListener {
                    mclickistener.onClickItemView(position,
                        approveList as ArrayList<List<GetImageUrlResponse.ImageUrl>>,
                        categoryPos,
                        categoryName,
                        approvedOrders.get(j).url!!)

                }
            }


        }
        }else{
            holder.itemView.setOnClickListener {
                mclickistener.onClickItemView(position,
                    approveList as ArrayList<List<GetImageUrlResponse.ImageUrl>>,
                    categoryPos,
                    categoryName,
                    "")

            }
        }

        for (i in approvedOrders.indices) {
            if (stage.toLowerCase().contains("pre") && approvedOrders[i].stage.equals("1")) {
                Glide.with(mContext).load(approvedOrders.get(i).url)
                    .placeholder(R.drawable.thumbnail_image)
                    .into(holder.preRetroImagesLayoutBinding.image)
                if (approvedOrders.get(i).status!!.toLowerCase().contains("0")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.material_amber_accent_700)
                }
                else if (approvedOrders.get(i).status!!.toLowerCase().contains("1")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.greenn)
                }
                else if (approvedOrders.get(i).status!!.toLowerCase().contains("2")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.color_red)
                }

            } else if (stage.toLowerCase().contains("aft") && approvedOrders[i].stage.equals("3")) {
                Glide.with(mContext).load(approvedOrders.get(i).url)
                    .placeholder(R.drawable.thumbnail_image)
                    .into(holder.preRetroImagesLayoutBinding.image)
                if (approvedOrders.get(i).status!!.toLowerCase().contains("0")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.material_amber_accent_700)
                }
                else if (approvedOrders.get(i).status!!.toLowerCase().contains("1")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.greenn)
                }
                else if (approvedOrders.get(i).status!!.toLowerCase().contains("2")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.color_red)
                }
            } else if (stage.toLowerCase()
                    .contains("post") && approvedOrders[i].stage.equals("2")
            ) {
                Glide.with(mContext).load(approvedOrders.get(i).url)
                    .placeholder(R.drawable.thumbnail_image)
                    .into(holder.preRetroImagesLayoutBinding.image)
                if (approvedOrders.get(i).status!!.toLowerCase().contains("0")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.material_amber_accent_700)
                }
                else if (approvedOrders.get(i).status!!.toLowerCase().contains("1")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.greenn)
                }
                else if (approvedOrders.get(i).status!!.toLowerCase().contains("2")) {
                    holder.preRetroImagesLayoutBinding.imagetick.imageTintList =
                        ContextCompat.getColorStateList(
                            mContext,
                            R.color.color_red)
                }
            }

        }


    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val preRetroImagesLayoutBinding: PreRetroImagesLayoutBinding) :
        RecyclerView.ViewHolder(preRetroImagesLayoutBinding.root)

}