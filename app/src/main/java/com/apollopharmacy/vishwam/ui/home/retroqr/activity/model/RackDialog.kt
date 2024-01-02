package com.apollopharmacy.vishwam.ui.home.retroqr.activity.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DialogCustomBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView

class RackDialog: DialogFragment() {



    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: GstDialogClickListner

    init {
        setCancelable(false)
    }

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: ArrayList<String>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }


    fun generateParsedDatafromreasons(data: ArrayList<String>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface GstDialogClickListner {
        fun selectGST(gst: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        viewBinding.textHead.text = "Select Rack"
//        viewBinding.headerLayout.setBackgroundColor(Color.parseColor("#f7941d"))
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<String>
        viewBinding.fieldRecyclerView.adapter =
            CustomRecyclerViews(data, object : OnSelectListner {
                override fun onSelected(data: String) {
                    abstractDialogClick = activity as GstDialogClickListner

                    abstractDialogClick.selectGST(data)
                    dismiss()


                }
            })
        return viewBinding.root
    }
}

class CustomRecyclerViews(
    departmentListDto: ArrayList<String>,
    var onSelectedListner: OnSelectListner,
) :
    SimpleRecyclerView<ViewListItemBinding, String>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(
        binding: ViewListItemBinding,
        items: String,
        position: Int,
    ) {
        binding.itemName.text = items

        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSelectListner {
    fun onSelected(data: String)
}



