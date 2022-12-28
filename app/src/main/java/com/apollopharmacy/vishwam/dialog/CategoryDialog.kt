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

class CategoryDialog : DialogFragment() {

   /* lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: SubCategoryDialogClickListner

    init {
        setCancelable(false)
    }

    companion object {
        const val KEY_DATA_SUBCATEGORY = "data"
    }

    fun generateParsedData(
        data: ArrayList<DepartmentV2Response.CategoriesItem>,
        tag: String,
    ): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA_SUBCATEGORY, data)
        }
    }

    interface SubCategoryDialogClickListner {
        fun selectCategory(departmentDto: DepartmentV2Response.CategoriesItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        abstractDialogClick = parentFragment as SubCategoryDialogClickListner
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_category)
        viewBinding.closeDialog.setOnClickListener { dismiss() }
        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA_SUBCATEGORY) as ArrayList<DepartmentV2Response.CategoriesItem>
        viewBinding.fieldRecyclerView.adapter =
            SubCategoryRecyclerView(data, object : OnCategorySelected {
                override fun onSelected(data: DepartmentV2Response.CategoriesItem) {
                    abstractDialogClick.selectCategory(data)
                    dismiss()
                }
            })
        return viewBinding.root
    }
}

class SubCategoryRecyclerView(
    departmentListDto: ArrayList<DepartmentV2Response.CategoriesItem>,
    var onSelectedListner: OnCategorySelected,
) :
    SimpleRecyclerView<ViewListItemBinding, DepartmentV2Response.CategoriesItem>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(
        binding: ViewListItemBinding,
        items: DepartmentV2Response.CategoriesItem,
        position: Int,
    ) {
        binding.itemName.text = items.categoryName
        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnCategorySelected {
    fun onSelected(data: DepartmentV2Response.CategoriesItem)*/


    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: SubCategoryDialogClickListner

    init {
        setCancelable(false)
    }

    companion object {
        const val KEY_DATA_SUBCATEGORY = "data"
    }

    fun generateParsedData(
        data: ArrayList<ReasonmasterV2Response.TicketCategory>,
        tag: String,
    ): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA_SUBCATEGORY, data)
        }
    }

    interface SubCategoryDialogClickListner {
        fun selectCategory(departmentDto: ReasonmasterV2Response.TicketCategory)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        abstractDialogClick = parentFragment as SubCategoryDialogClickListner
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_category)
        viewBinding.closeDialog.setOnClickListener { dismiss() }
        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA_SUBCATEGORY) as ArrayList<ReasonmasterV2Response.TicketCategory>
        viewBinding.fieldRecyclerView.adapter =
            SubCategoryRecyclerView(data, object : OnCategorySelected {
                override fun onSelected(data: ReasonmasterV2Response.TicketCategory) {
                    abstractDialogClick.selectCategory(data)
                    dismiss()
                }
            })
        return viewBinding.root
    }
}

class SubCategoryRecyclerView(
    departmentListDto: ArrayList<ReasonmasterV2Response.TicketCategory>,
    var onSelectedListner: OnCategorySelected,
) :
    SimpleRecyclerView<ViewListItemBinding, ReasonmasterV2Response.TicketCategory>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(
        binding: ViewListItemBinding,
        items: ReasonmasterV2Response.TicketCategory,
        position: Int,
    ) {
        binding.itemName.text = items.name
        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnCategorySelected {
    fun onSelected(data: ReasonmasterV2Response.TicketCategory)
}