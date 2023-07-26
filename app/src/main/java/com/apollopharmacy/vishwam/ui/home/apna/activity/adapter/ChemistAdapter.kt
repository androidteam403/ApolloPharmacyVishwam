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
import com.apollopharmacy.vishwam.databinding.AdapterChemistListBinding
import com.apollopharmacy.vishwam.databinding.ImageDeleteConfirmDialogBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ChemistData
import java.text.DecimalFormat

class ChemistAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var chemistList: ArrayList<ChemistData>,
) : RecyclerView.Adapter<ChemistAdapter.ViewHolder>() {

    class ViewHolder(val adapterChemistListBinding: AdapterChemistListBinding) :
        RecyclerView.ViewHolder(adapterChemistListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterChemistListBinding = DataBindingUtil.inflate<AdapterChemistListBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_chemist_list,
            parent,
            false
        )
        return ViewHolder(adapterChemistListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (chemistList.get(position).chemist.isNotEmpty()) {
            holder.adapterChemistListBinding.chemistType.setText(chemistList.get(position).chemist)
        } else {
            holder.adapterChemistListBinding.chemistType.setText("-")
        }

        if (chemistList.get(position).organised.isNotEmpty()) {
            holder.adapterChemistListBinding.organisedText.setText(chemistList[position].organisedName)
        } else {
            holder.adapterChemistListBinding.organisedText.setText("-")
        }

        if (chemistList[position].organisedAvgSale.isNotEmpty()) {
            holder.adapterChemistListBinding.organisedAvgSaleText.setText(
                "\u20B9" + DecimalFormat("##,##,##0").format(chemistList[position].organisedAvgSale.toLong())
            )
        } else {
            holder.adapterChemistListBinding.organisedAvgSaleText.setText("-")
        }

        if (chemistList[position].unorganised.isNotEmpty()) {
            holder.adapterChemistListBinding.unorganisedText.setText(chemistList[position].unorganisedName)
        } else {
            holder.adapterChemistListBinding.unorganisedText.setText("-")
        }

        if (chemistList[position].unorganisedAvgSale.isNotEmpty()) {
            holder.adapterChemistListBinding.unorganisedAvgSaleText.setText(
                "\u20B9" + DecimalFormat("##,##,##0").format(chemistList[position].unorganisedAvgSale.toLong())
            )
        } else {
            holder.adapterChemistListBinding.unorganisedAvgSaleText.setText("-")
        }

        holder.adapterChemistListBinding.deleteChemist.setOnClickListener {
//            mCallback.onClickDeleteChemist(position)
            openConfirmDialog(position, chemistList[position])
        }
    }

    private fun openConfirmDialog(position: Int, chemistData: ChemistData) {
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
            mCallback.onClickDeleteChemist(position)
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return chemistList.size
    }
}