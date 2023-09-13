package com.apollopharmacy.vishwam.ui.home.drugmodule.model

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

class QcListSizeDialog: DialogFragment() {



    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: GstDialogClickListners

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

    interface GstDialogClickListners {
        fun selectListSize(listSize: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)

        viewBinding.textHead.text = "Select Rows"
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<String>
        viewBinding.fieldRecyclerView.adapter =
            CustomRecyclerViewss(data, object : OnSelectListners {
                override fun onSelected(data: String) {

                    abstractDialogClick =requireParentFragment() as GstDialogClickListners
                    abstractDialogClick.selectListSize(data)
                    dismiss()


                }
            })
        return viewBinding.root
    }
}

class CustomRecyclerViewss(
    departmentListDto: ArrayList<String>,
    var onSelectedListner: OnSelectListners,
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

interface OnSelectListners {
    fun onSelected(data: String)
}