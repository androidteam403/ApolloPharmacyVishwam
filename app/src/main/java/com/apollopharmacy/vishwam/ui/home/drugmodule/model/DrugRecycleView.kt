package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class DrugRecycleView<VB : ViewDataBinding, M>(
    private val items: List<M>,
    @LayoutRes private val layoutId: Int
) : RecyclerView.Adapter<DrugRecycleView<VB, M>.Holder>() {



    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<VB>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItem(position: Int): M {
        return items[position]
    }


    inner class Holder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: M) {
            bindItems(binding, items, adapterPosition)
        }
    }

    abstract fun bindItems(binding: VB, items: M, position: Int)

}