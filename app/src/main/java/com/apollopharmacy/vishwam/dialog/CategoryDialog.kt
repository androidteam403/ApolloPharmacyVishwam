package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.databinding.DialogCustomBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.model.CategotyViewModel
import com.apollopharmacy.vishwam.dialog.model.DepartmentViewModel

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
    var siteDataArrayList= ArrayList<ReasonmasterV2Response.TicketCategory>()
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
        viewBinding.searchSite.visibility = View.VISIBLE
        var viewModel = ViewModelProviders.of(requireActivity())[CategotyViewModel::class.java]
        viewBinding.searchSiteText.setHint("Search Category name")
        viewBinding.searchSiteText.inputType = InputType.TYPE_CLASS_TEXT
        siteDataArrayList =
            arguments?.getSerializable(KEY_DATA_SUBCATEGORY) as ArrayList<ReasonmasterV2Response.TicketCategory>
        viewModel.siteArrayList(siteDataArrayList)

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.text = "Category not available"
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.adapter =
                    SubCategoryRecyclerView(it, object : OnCategorySelected {
                        override fun onSelected(data: ReasonmasterV2Response.TicketCategory) {
                            abstractDialogClick.selectCategory(data)
                            dismiss()
                        }
                    })
            }
        })

        viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var textChanged = s.toString().trim()
                if (s.toString().length > 1) {
                    viewModel.filterDataBySiteId(textChanged)
                } else {
                    viewModel.siteArrayList(siteDataArrayList)
                }
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