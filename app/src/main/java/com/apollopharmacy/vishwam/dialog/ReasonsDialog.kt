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
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.dialog.model.ReasonViewModel
import com.apollopharmacy.vishwam.dialog.model.SubCategoryViewModel

class ReasonsDialog : DialogFragment() {
    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: ReasonsDialogClickListner
    var siteDataArrayList= ArrayList<ReasonmasterV2Response.Row>()
    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(
        data: ArrayList<ReasonmasterV2Response.Row>,
        tag: String,
    ): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface ReasonsDialogClickListner {
        fun selectReasons(departmentDto: ReasonmasterV2Response.Row)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_reasonlist_list)
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        viewBinding.searchSite.visibility = View.GONE
        abstractDialogClick = parentFragment as ReasonsDialogClickListner
        viewBinding.searchSite.visibility = View.VISIBLE
        var viewModel = ViewModelProviders.of(requireActivity())[ReasonViewModel::class.java]
        viewBinding.searchSiteText.setHint("Search Reason Name")
        viewBinding.searchSiteText.inputType = InputType.TYPE_CLASS_TEXT
        siteDataArrayList =
            arguments?.getSerializable(KEY_DATA) as ArrayList<ReasonmasterV2Response.Row>
        viewModel.siteArrayList(siteDataArrayList)

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailabletext.text = "Reason not available"
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.adapter =
                    ReasonsRecycler(it, object : OnSubSelectedListner {
                        override fun onSelected(data: ReasonmasterV2Response.Row) {
                            abstractDialogClick.selectReasons(data)
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


    class ReasonsRecycler(
        departmentListDto: ArrayList<ReasonmasterV2Response.Row>,
        var onSelectedListner: OnSubSelectedListner,
    ) :
        SimpleRecyclerView<ViewItemRowBinding, ReasonmasterV2Response.Row>(
            departmentListDto,
            R.layout.view_item_row
        ) {
        override fun bindItems(
            binding: ViewItemRowBinding,
            items: ReasonmasterV2Response.Row,
            position: Int,
        ) {
            binding.itemName.text = items.name

            binding.root.setOnClickListener {
                onSelectedListner.onSelected(items)
            }
        }
    }

    interface OnSubSelectedListner {
        fun onSelected(data: ReasonmasterV2Response.Row)
    }
}
