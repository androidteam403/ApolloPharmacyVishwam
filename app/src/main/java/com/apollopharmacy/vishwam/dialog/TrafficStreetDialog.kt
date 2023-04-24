package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DialogTrafficStreetBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding

class TrafficStreetDialog : DialogFragment() {
    lateinit var trafficStreetBinding: DialogTrafficStreetBinding
    var trafficStreetData = ArrayList<String>()
    lateinit var abstractDialogClickListener: AbstractDialogClickListener

    init {
        isCancelable = false
    }

    companion object {
        const val KEY_DATA = "data"
    }

    interface AbstractDialogClickListener {
        fun selectTrafficStreet(trafficStreetData: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        trafficStreetBinding = DialogTrafficStreetBinding.inflate(inflater, container, false)
        trafficStreetBinding.closeDialog.setOnClickListener {
            dismiss()
        }

        if (parentFragment != null) {
            abstractDialogClickListener = parentFragment as AbstractDialogClickListener
        }

        trafficStreetData.add("Low")
        trafficStreetData.add("Medium")
        trafficStreetData.add("High")
        trafficStreetData.add("Very High")

        trafficStreetBinding.trafficStreetRcv.adapter =
            CustomRecyclerView(trafficStreetData, object :OnSelectedListener {
                override fun onSelected(data: String) {
                    abstractDialogClickListener.selectTrafficStreet(data)
                    dismiss()
                }

            })

        return trafficStreetBinding.root
    }

    class CustomRecyclerView(
        var trafficStreetData: ArrayList<String>,
        var onSelectedListener: OnSelectedListener,
    ) : SimpleRecyclerView<ViewListItemBinding, String>(
        trafficStreetData,
        R.layout.view_list_item
    ) {
        override fun bindItems(
            binding: ViewListItemBinding,
            items: String,
            position: Int,
        ) {
            binding.itemName.text = items
            binding.root.setOnClickListener {
                onSelectedListener.onSelected(items)
            }
        }

    }
}

interface OnSelectedListener {
    fun onSelected(data: String)
}