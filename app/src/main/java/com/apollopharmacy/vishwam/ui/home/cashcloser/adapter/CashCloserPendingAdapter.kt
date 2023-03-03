package com.apollopharmacy.vishwam.ui.home.cashcloser.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcCashCloserLayoutBinding
import com.apollopharmacy.vishwam.ui.home.cashcloser.CashCloserFragmentCallback
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashCloserList
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.ImageData

class CashCloserPendingAdapter(
    val mContext: Context,
    val list: ArrayList<CashCloserList>,
    val mCallback: CashCloserFragmentCallback,
) :
    RecyclerView.Adapter<CashCloserPendingAdapter.ViewHolder>() {

    var imagesAdapter: ImagesAdapter? = null
    var filteredImags: ArrayList<ImageData>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cashCloserLayoutBinding: QcCashCloserLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_cash_closer_layout,
                parent,
                false
            )
        return ViewHolder(cashCloserLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cashCloserLayoutBinding.siteId.text = list[position].siteId
        holder.cashCloserLayoutBinding.date.text = list[position].date
        holder.cashCloserLayoutBinding.status.text = list[position].status

        if (list[position].isExpanded) {
            holder.cashCloserLayoutBinding.arrow.visibility = View.GONE
            holder.cashCloserLayoutBinding.arrowClose.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.extraData.visibility = View.VISIBLE
        } else {
            holder.cashCloserLayoutBinding.arrow.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.arrowClose.visibility = View.GONE
            holder.cashCloserLayoutBinding.extraData.visibility = View.GONE
        }

        holder.cashCloserLayoutBinding.arrow.setOnClickListener {
            mCallback.headrItemClickListener(list[position].siteId!!, position)

        }
        holder.cashCloserLayoutBinding.arrowClose.setOnClickListener {
            mCallback.headrItemClickListener(list[position].siteId!!, position)
        }


        imagesAdapter =
            ImagesAdapter(mContext, list[position].imageList as ArrayList<ImageData>, mCallback)
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        holder.cashCloserLayoutBinding.recyclerViewImages.adapter = imagesAdapter
        holder.cashCloserLayoutBinding.recyclerViewImages.layoutManager = layoutManager
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val cashCloserLayoutBinding: QcCashCloserLayoutBinding) :
        RecyclerView.ViewHolder(cashCloserLayoutBinding.root)
}