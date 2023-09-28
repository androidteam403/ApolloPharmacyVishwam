package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.LayoutPrintRackBinding

class PrintRackAdapter(var mContext: Context) :
    RecyclerView.Adapter<PrintRackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutPrintRackBinding = DataBindingUtil.inflate<LayoutPrintRackBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_print_rack,
            parent,
            false
        )
        return ViewHolder(layoutPrintRackBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 15
    }

    class ViewHolder(layoutPrintRackBinding: LayoutPrintRackBinding) :
        RecyclerView.ViewHolder(layoutPrintRackBinding.root)
}