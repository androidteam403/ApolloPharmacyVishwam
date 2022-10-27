package com.apollopharmacy.vishwam.ui.home.qcfail.model

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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.DialogSiteListBinding
import com.apollopharmacy.vishwam.databinding.QcDialogSiteListBinding
import com.apollopharmacy.vishwam.databinding.QcViewItemRowBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.util.Utils

class QcRegionDialog : DialogFragment() {


    val TAG = "RegionDialog"

    init {
        setCancelable(false)
    }

    lateinit var viewBinding: QcDialogSiteListBinding
    lateinit var abstractDialogClick: NewDialogSiteClickListner
    var regionDataArrayList = ArrayList<QcRegionList.Store>()
    lateinit var sitereCyclerView: RegionRecyclerView

    fun generateParsedData(data: ArrayList<QcRegionList.Store>): Bundle {
        return Bundle().apply {
            putSerializable(REGION_DATA, data)
        }
    }

    companion object {
        const val REGION_DATA = "data"
    }


    interface NewDialogSiteClickListner {


        fun selectSite(regionId: QcRegionList.Store)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        viewBinding = QcDialogSiteListBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProviders.of(requireActivity())[QcRegionViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                sitereCyclerView = RegionRecyclerView(it, object : OnSelectListnerSite {
                    override fun onSelected(data: QcRegionList.Store) {
                        abstractDialogClick.selectSite(data)
//                        dismiss()
                    }
                })
                viewBinding.fieldRecyclerView.adapter = sitereCyclerView
            }
        })


        viewBinding.closeDialog.setOnClickListener {
            regionDataArrayList.clear()
            dismiss()
        }
        abstractDialogClick = activity as NewDialogSiteClickListner


        regionDataArrayList =
            (arguments?.getSerializable(REGION_DATA) as? ArrayList<QcRegionList.Store>)!!

        viewBinding.searchSite.visibility = View.VISIBLE
        viewModel.qcRegionArrayList(regionDataArrayList)







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
                    viewModel.qcRegionArrayList(regionDataArrayList)
                }
            }
        })
        return viewBinding.root
    }
}

class RegionRecyclerView(
    departmentListDto: ArrayList<QcRegionList.Store>,
    var onSelectedListner: OnSelectListnerSite,
) :
    SimpleRecyclerView<QcViewItemRowBinding, QcRegionList.Store>(
        departmentListDto,
        R.layout.qc_view_item_row
    ) {
    override fun bindItems(
        binding: QcViewItemRowBinding,
        items: QcRegionList.Store,
        position: Int,
    ) {
        binding.itemName.text = "${items.siteid}, ${items.sitename}"


//        binding.root.setOnClickListener {
//            items.setisClick(true)
//            onSelectedListner.onSelected(items)
//        }
    }
}

interface OnSelectListnerSite {
    fun onSelected(data: QcRegionList.Store)
}

interface clickListener{
    fun onClick(list: ArrayList<QcRegionList.Store>, position: Int)
}

