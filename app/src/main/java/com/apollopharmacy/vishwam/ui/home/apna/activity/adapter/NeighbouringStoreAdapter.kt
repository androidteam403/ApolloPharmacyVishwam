package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.LayoutNeighbouringStoreBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.NeighbouringLocationResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.NeighbouringStoreData

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (neighbouringList.get(position).location.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.location.setText(neighbouringList.get(position).location)
        } else {
            holder.layoutNeighbouringStoreBinding.location.setText("-")
        }

        if (neighbouringList.get(position).store.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.store.setText(neighbouringList.get(position).store)
        } else {
            holder.layoutNeighbouringStoreBinding.store.setText("-")
        }

        if (neighbouringList.get(position).rent.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.rent.setText(neighbouringList.get(position).rent)
        } else {
            holder.layoutNeighbouringStoreBinding.rent.setText("-")
        }

        if (neighbouringList.get(position).sales.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.sales.setText(neighbouringList.get(position).sales)
        } else {
            holder.layoutNeighbouringStoreBinding.sales.setText("-")
        }

        if (neighbouringList.get(position).sqFt.isNotEmpty()) {
            holder.layoutNeighbouringStoreBinding.sqFt.setText(neighbouringList.get(position).sqFt)
        } else {
            holder.layoutNeighbouringStoreBinding.sqFt.setText("-")
        }

        holder.layoutNeighbouringStoreBinding.close.setOnClickListener {
            mCallback.onClickNeighbouringStoreDelete(position)
        }

//        holder.layoutNeighbouringStoreBinding.locationText.text = neighbouringList[position].name
//
//        holder.layoutNeighbouringStoreBinding.storeText.addTextChangedListener(object :
//            TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                neighbouringList.get(position).store = s.toString()
//            }
//
//        })
//
//        holder.layoutNeighbouringStoreBinding.rentText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                neighbouringList.get(position).rent = s.toString()
//            }
//
//        })
//
//        holder.layoutNeighbouringStoreBinding.salesText.addTextChangedListener(object :
//            TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                neighbouringList.get(position).sales = s.toString()
//            }
//
//        })
//
//        holder.layoutNeighbouringStoreBinding.sqFtText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                neighbouringList.get(position).sqFt = s.toString()
//                mCallback.onDataChanged(neighbouringList)
//            }
//
//        })
    }

    override fun getItemCount(): Int {
        return neighbouringList.size
    }
}