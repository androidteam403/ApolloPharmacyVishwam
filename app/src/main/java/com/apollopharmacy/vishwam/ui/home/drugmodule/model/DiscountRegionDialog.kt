package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.DialogSiteListBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.dialog.model.DiscountRegionViewModel
import com.apollopharmacy.vishwam.util.Utils

class DiscountRegionDialog : DialogFragment(){



    val TAG = "SiteDialog"

    init {
        setCancelable(false)
    }

    lateinit var viewBinding: DialogSiteListBinding
    lateinit var abstractDialogClick: NewDialogSiteClickListner
    var siteDataArrayList= ArrayList<PendingOrder.PENDINGLISTItem>()
    lateinit var sitereCyclerView: DiscountRegionRecycleView

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: ArrayList<PendingOrder.PENDINGLISTItem>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface NewDialogSiteClickListner {
        fun selectRegion(departmentDto: PendingOrder.PENDINGLISTItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        viewBinding = DialogSiteListBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProviders.of(requireActivity())[DiscountRegionViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE
        viewBinding.siteNotAvailable.setText("Region Not Available")
        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                sitereCyclerView = DiscountRegionRecycleView(it, object : OnSelectDiscountRegion {
                    override fun onSelected(data: PendingOrder.PENDINGLISTItem) {
                        abstractDialogClick.selectRegion(data)
                        dismiss()
                    }
                })
                viewBinding.fieldRecyclerView.adapter = sitereCyclerView
            }
        })


        viewBinding.closeDialog.setOnClickListener {
            siteDataArrayList.clear()
            dismiss()
        }

        siteDataArrayList = arguments?.getSerializable(KEY_DATA) as ArrayList<PendingOrder.PENDINGLISTItem>
        viewBinding.searchSite.visibility = View.VISIBLE
        viewModel.siteArrayList(siteDataArrayList)
//            abstractDialogClick = parentFragment as NewDialogSiteClickListner

        abstractDialogClick = activity as NewDialogSiteClickListner

        viewBinding.textHead.setText("Search Region Id ")

        viewBinding.searchSite.setHint("Search Region Id or Region Name")



        viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Utils.printMessage(TAG, "Before Text Changed :: " + s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Utils.printMessage(TAG, "On Text Changed" + s.toString())
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

class DiscountRegionRecycleView(
    departmentListDto: ArrayList<PendingOrder.PENDINGLISTItem>, var onSelectedListner: OnSelectDiscountRegion
) :
    SimpleRecyclerView<ViewListItemBinding, PendingOrder.PENDINGLISTItem>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(binding: ViewListItemBinding, items: PendingOrder.PENDINGLISTItem, position: Int) {
        binding.itemName.text = "${items.DCCODE}, ${items.DCNAME}"
        binding.root.setOnClickListener {

            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSelectDiscountRegion {
    fun onSelected(data: PendingOrder.PENDINGLISTItem)
}

