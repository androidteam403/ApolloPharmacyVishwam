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
import com.apollopharmacy.vishwam.dialog.ReasonsDialog
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcReasonList

class RejectReasonsDialog: DialogFragment() {



    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: ResaonDialogClickListner

    init {
        setCancelable(false)
    }

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: ArrayList<QcReasonList.Remarks>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }


    fun generateParsedDatafromreasons(data: ArrayList<String>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface ResaonDialogClickListner {
        fun selectReason(gst: String)
        fun reasonCode(reasonCode:String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_reason)
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<QcReasonList.Remarks>
        if (data.isNullOrEmpty()){
            viewBinding.siteNotAvailabletext.setText("Reject Reasons not available")
            viewBinding.fieldRecyclerView.visibility=View.GONE
            viewBinding.siteNotAvailable.visibility=View.VISIBLE

        }else{
            viewBinding.fieldRecyclerView.visibility=View.VISIBLE
            viewBinding.siteNotAvailable.visibility=View.GONE
            viewBinding.fieldRecyclerView.adapter =
                ReasonRecyclerViews(data, object : OnSelectionListner {
                    override fun onSelected(data: String) {
                        abstractDialogClick = activity as ResaonDialogClickListner

                        abstractDialogClick.selectReason(data)
                        dismiss()


                    }

                    override fun onSelectedcode(code: String) {
                        abstractDialogClick.reasonCode(code)

                    }
                })
        }

        return viewBinding.root
    }
}

class ReasonRecyclerViews(
    departmentListDto: ArrayList<QcReasonList.Remarks>,
    var onSelectedListner: OnSelectionListner,
) :
    SimpleRecyclerView<ViewListItemBinding, QcReasonList.Remarks>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(
        binding: ViewListItemBinding,
        items: QcReasonList.Remarks,
        position: Int,
    ) {
        binding.itemName.text = items.reasonbase

        binding.root.setOnClickListener {
            items.reasonbase?.let { it1 -> onSelectedListner.onSelected(it1) }
            items.reasonbase?.let { it1 -> onSelectedListner.onSelectedcode(it1) }

        }
    }
}

interface OnSelectionListner {
    fun onSelected(data: String)
    fun onSelectedcode(code:String)
}



