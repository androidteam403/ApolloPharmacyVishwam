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

    fun generateParsedData(data: ArrayList<ReasonmasterV2Response.TicketSubCategory>): Bundle {
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
        fun selectDepartment(departmentDto: ReasonmasterV2Response.TicketSubCategory)
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
            arguments?.getSerializable(KEY_DATA) as ArrayList<ReasonmasterV2Response.TicketSubCategory>
        viewBinding.fieldRecyclerView.adapter =
            CustomRecyclerViews(data, object : OnSelectListner {
                override fun onSelected(data: ReasonmasterV2Response.TicketSubCategory) {
                    abstractDialogClick = parentFragment as DialogClickListner

                    abstractDialogClick.selectDepartment(data)
                    dismiss()


                }
            })
        return viewBinding.root
    }


}

class CustomRecyclerViews(
    departmentListDto: ArrayList<ReasonmasterV2Response.TicketSubCategory>,
    var onSelectedListner: OnSelectListner,
) :
    SimpleRecyclerView<ViewListItemBinding, ReasonmasterV2Response.TicketSubCategory>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(
        binding: ViewListItemBinding,
        items: ReasonmasterV2Response.TicketSubCategory,
        position: Int,
    ) {
        binding.itemName.text = items.name

        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSelectListner {
    fun onSelected(data: ReasonmasterV2Response.TicketSubCategory)
}
