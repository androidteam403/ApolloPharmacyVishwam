package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.discount.FilterData
import com.apollopharmacy.vishwam.databinding.DialogCustomBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding

class CustomFilterDialog : DialogFragment() {

    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: AbstractDialogClickListner

    init {
        setCancelable(false)
    }

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: ArrayList<FilterData>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface AbstractDialogClickListner {
        fun selectedItem(departmentDto: FilterData)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_search_by)
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        abstractDialogClick = parentFragment as AbstractDialogClickListner
        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<FilterData>
        viewBinding.fieldRecyclerView.adapter =
            CustomFilterRecyclerView(data, object : OnFilterListner {
                override fun onSelected(data: FilterData) {
                    abstractDialogClick.selectedItem(data)
                    dismiss()
                }
            })
        return viewBinding.root
    }
}

class CustomFilterRecyclerView(
    departmentListDto: ArrayList<FilterData>,
    var onFilterListner: OnFilterListner,
) :
    SimpleRecyclerView<ViewListItemBinding, FilterData>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(
        binding: ViewListItemBinding,
        items: FilterData,
        position: Int,
    ) {
        binding.itemName.text = items.MenuTitle

        binding.root.setOnClickListener {
            onFilterListner.onSelected(items)
        }
    }
}

interface OnFilterListner {
    fun onSelected(data: FilterData)
}
