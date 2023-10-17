package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ImageDeleteConfirmDialogBinding
import com.apollopharmacy.vishwam.databinding.LayoutNeighbouringStoreBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.NeighbouringStoreData
import com.apollopharmacy.vishwam.util.Utils
import java.text.DecimalFormat

class NeighbouringStoreAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var neighbouringList: ArrayList<NeighbouringStoreData>,
) : RecyclerView.Adapter<NeighbouringStoreAdapter.ViewHolder>() {

    class ViewHolder(val layoutNeighbouringStoreBinding: LayoutNeighbouringStoreBinding) :
        RecyclerView.ViewHolder(layoutNeighbouringStoreBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutNeighbouringStoreBinding =
            DataBindingUtil.inflate<LayoutNeighbouringStoreBinding>(
                LayoutInflater.from(mContext),
                R.layout.layout_neighbouring_store,
                parent,
                false
            )
        return ViewHolder(layoutNeighbouringStoreBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (neighbouringList.get(position).location.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.location.setText(neighbouringList.get(position).locationName)
        } else {
            holder.layoutNeighbouringStoreBinding.location.setText("-")
        }

        if (neighbouringList.get(position).store.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.store.setText(neighbouringList.get(position).store)
        } else {
            holder.layoutNeighbouringStoreBinding.store.setText("-")
        }

        if (neighbouringList.get(position).rent.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.rent.setText(
                "\u20B9" + DecimalFormat("##,##,##0", Utils.symbols).format(neighbouringList[position].rent.toLong())
            )
        } else {
            holder.layoutNeighbouringStoreBinding.rent.setText("-")
        }

        if (neighbouringList.get(position).sales.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.sales.setText(
                neighbouringList[position].sales
            )
//            holder.layoutNeighbouringStoreBinding.sales.setText(
//                "\u20B9" + DecimalFormat("##,##,##0").format(neighbouringList[position].sales.toLong())
//            )
        } else {
            holder.layoutNeighbouringStoreBinding.sales.setText("-")
        }

        if (neighbouringList.get(position).sqFt.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.sqFt.setText(neighbouringList.get(position).sqFt)
        } else {
            holder.layoutNeighbouringStoreBinding.sqFt.setText("-")
        }

        holder.layoutNeighbouringStoreBinding.close.setOnClickListener {
//            mCallback.onClickNeighbouringStoreDelete(position)
            openConfirmDialog(position, neighbouringList[position])
        }
    }

    private fun openConfirmDialog(position: Int, neighbouringStoreData: NeighbouringStoreData) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imageDeleteConfirmDialogBinding =
            DataBindingUtil.inflate<ImageDeleteConfirmDialogBinding>(
                LayoutInflater.from(mContext),
                R.layout.image_delete_confirm_dialog,
                null,
                false
            )
        dialog.setContentView(imageDeleteConfirmDialogBinding.root)
        imageDeleteConfirmDialogBinding.noButton.setOnClickListener {
            dialog.dismiss()
        }
        imageDeleteConfirmDialogBinding.yesButton.setOnClickListener {
            mCallback.onClickNeighbouringStoreDelete(position)
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return neighbouringList.size
    }
}