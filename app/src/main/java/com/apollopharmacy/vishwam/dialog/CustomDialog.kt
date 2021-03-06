package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.DepartmentV2Response
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.databinding.DialogCustomBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding

class CustomDialog : DialogFragment() {

    /* lateinit var viewBinding: DialogCustomBinding
     lateinit var abstractDialogClick: AbstractDialogClickListner

     init {
         setCancelable(false)
     }

     companion object {
         const val KEY_DATA = "data"
     }

     fun generateParsedData(data: ArrayList<DepartmentV2Response.DepartmentListItem>): Bundle {
         return Bundle().apply {
             putSerializable(KEY_DATA, data)
         }
     }


     fun generateParsedDatafromreasons(data: ArrayList<ReasonmasterV2Response.Department>): Bundle {
         return Bundle().apply {
             putSerializable(KEY_DATA, data)
         }
     }

     interface AbstractDialogClickListner {
         fun selectDepartment(departmentDto: DepartmentV2Response.DepartmentListItem)
     }

     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?,
     ): View? {
         dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         viewBinding = DialogCustomBinding.inflate(inflater, container, false)
         viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_department)
         viewBinding.closeDialog.setOnClickListener { dismiss() }

         abstractDialogClick = parentFragment as AbstractDialogClickListner
         viewBinding.searchSite.visibility = View.GONE
         var data =
             arguments?.getSerializable(KEY_DATA) as ArrayList<DepartmentV2Response.DepartmentListItem>
         viewBinding.fieldRecyclerView.adapter =
             CustomRecyclerView(data, object : OnSelectedListner {
                 override fun onSelected(data: DepartmentV2Response.DepartmentListItem) {
                     abstractDialogClick.selectDepartment(data)
                     dismiss()
                 }
             })
         return viewBinding.root
     }
 }

 class CustomRecyclerView(
     departmentListDto: ArrayList<DepartmentV2Response.DepartmentListItem>,
     var onSelectedListner: OnSelectedListner,
 ) :
     SimpleRecyclerView<ViewListItemBinding, DepartmentV2Response.DepartmentListItem>(
         departmentListDto,
         R.layout.view_list_item
     ) {
     override fun bindItems(
         binding: ViewListItemBinding,
         items: DepartmentV2Response.DepartmentListItem,
         position: Int,
     ) {
         binding.itemName.text = items.departmentName

         binding.root.setOnClickListener {
             onSelectedListner.onSelected(items)
         }
     }
 }*/

/*interface OnSelectedListner {
    fun onSelected(data: DepartmentV2Response.DepartmentListItem)*/


    //changed code for new Response................
    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: AbstractDialogClickListner

    init {
        setCancelable(false)
    }

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: ArrayList<ReasonmasterV2Response.Department>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }


    fun generateParsedDatafromreasons(data: ArrayList<ReasonmasterV2Response.Department>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface AbstractDialogClickListner {
        fun selectDepartment(departmentDto: ReasonmasterV2Response.Department)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_department)
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        abstractDialogClick = parentFragment as AbstractDialogClickListner
        viewBinding.searchSite.visibility = View.GONE
        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<ReasonmasterV2Response.Department>
        viewBinding.fieldRecyclerView.adapter =
            CustomRecyclerView(data, object : OnSelectedListner {
                override fun onSelected(data: ReasonmasterV2Response.Department) {
                    abstractDialogClick.selectDepartment(data)
                    dismiss()


                }
            })
        return viewBinding.root
    }
}

class CustomRecyclerView(
    departmentListDto: ArrayList<ReasonmasterV2Response.Department>,
    var onSelectedListner: OnSelectedListner,
) :
    SimpleRecyclerView<ViewListItemBinding, ReasonmasterV2Response.Department>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(
        binding: ViewListItemBinding,
        items: ReasonmasterV2Response.Department,
        position: Int,
    ) {
        binding.itemName.text = items.name

        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSelectedListner {
    fun onSelected(data: ReasonmasterV2Response.Department)
}
