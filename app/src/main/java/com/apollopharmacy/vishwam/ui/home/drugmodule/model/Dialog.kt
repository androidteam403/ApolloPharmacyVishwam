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
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.CategoryViewModel

class Dialog : DialogFragment() {

    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: DialogClickListner
    var siteDataArrayList= ArrayList<ReasonmasterV2Response.TicketSubCategory>()
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

        viewBinding.searchSite.visibility = View.VISIBLE
        var viewModel = ViewModelProviders.of(requireActivity())[CategoryViewModel::class.java]
        viewBinding.searchSite.setHint("Search Category Name")
        viewBinding.searchSiteText.inputType = InputType.TYPE_CLASS_TEXT
        siteDataArrayList =
            arguments?.getSerializable(KEY_DATA) as ArrayList<ReasonmasterV2Response.TicketSubCategory>
        viewModel.siteArrayList(siteDataArrayList)

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailabletext.text = "Category not available"
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.adapter =
                    CustomRecyclerViews(it, object : OnSelectListner{
                        override fun onSelected(data:  ReasonmasterV2Response.TicketSubCategory) {
                            abstractDialogClick = parentFragment as DialogClickListner
                            abstractDialogClick.selectDepartment(data)
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
