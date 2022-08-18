package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.databinding.DialogCustomBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding

class Dialog : DialogFragment() {

    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: DialogClickListner

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

    interface DialogClickListner {
        fun selectDepartment(departmentDto: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_category)
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<String>
        viewBinding.fieldRecyclerView.adapter =
            CustomRecyclerViews(data, object : OnSelectListner {
                override fun onSelected(data: String) {
                    abstractDialogClick = parentFragment as DialogClickListner

                    abstractDialogClick.selectDepartment(data)
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
