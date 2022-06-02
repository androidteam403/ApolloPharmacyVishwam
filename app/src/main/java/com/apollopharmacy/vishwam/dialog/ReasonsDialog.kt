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
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding

class ReasonsDialog : DialogFragment() {
    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: ReasonsDialogClickListner

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

        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<ReasonmasterV2Response.Row>
        viewBinding.fieldRecyclerView.adapter =
            ReasonsRecycler(data, object : OnSubSelectedListner {
                override fun onSelected(data: ReasonmasterV2Response.Row) {
                    abstractDialogClick.selectReasons(data)
                    dismiss()
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
