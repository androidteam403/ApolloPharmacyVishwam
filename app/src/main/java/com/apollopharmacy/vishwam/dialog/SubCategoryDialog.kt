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
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding

class SubCategoryDialog : DialogFragment() {

    /*  lateinit var viewBinding: DialogCustomBinding
      lateinit var abstractDialogClick: SubSubCategoryDialogClickListner

      companion object {
          const val KEY_DATA = "data"
      }

      fun generateParsedData(
          data: ArrayList<DepartmentV2Response.SubcategoryItem>,
          tag: String
      ): Bundle {
          return Bundle().apply {
              putSerializable(KEY_DATA, data)
          }
      }

      interface SubSubCategoryDialogClickListner {
          fun selectSubCategory(departmentDto: DepartmentV2Response.SubcategoryItem)
      }

      override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
      ): View? {
          dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          viewBinding = DialogCustomBinding.inflate(inflater, container, false)
          viewBinding.textHead.text = context?.resources?.getString(R.string.label_sub_category_list)
          viewBinding.closeDialog.setOnClickListener { dismiss() }

          viewBinding.searchSite.visibility = View.GONE
          abstractDialogClick = parentFragment as SubSubCategoryDialogClickListner

          var data =
              arguments?.getSerializable(KEY_DATA) as ArrayList<DepartmentV2Response.SubcategoryItem>
          viewBinding.fieldRecyclerView.adapter =
              SubCategoryRecycler(data, object : OnSubSelectedListner {
                  override fun onSelected(data: DepartmentV2Response.SubcategoryItem) {
                      abstractDialogClick.selectSubCategory(data)
                      dismiss()
                  }
              })
          return viewBinding.root
      }
  }

  class SubCategoryRecycler(
      departmentListDto: ArrayList<DepartmentV2Response.SubcategoryItem>,
      var onSelectedListner: OnSubSelectedListner
  ) :
      SimpleRecyclerView<ViewItemRowBinding, DepartmentV2Response.SubcategoryItem>(
          departmentListDto,
          R.layout.view_item_row
      ) {
      override fun bindItems(
          binding: ViewItemRowBinding,
          items: DepartmentV2Response.SubcategoryItem,
          position: Int
      ) {
          binding.itemName.text = items.subCategoryName

          binding.root.setOnClickListener {
              onSelectedListner.onSelected(items)
          }
      }
  }

  interface OnSubSelectedListner {
      fun onSelected(data: DepartmentV2Response.SubcategoryItem)*/


    lateinit var viewBinding: DialogCustomBinding
    lateinit var abstractDialogClick: SubSubCategoryDialogClickListner

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(
        data: ArrayList<ReasonmasterV2Response.TicketSubCategory>,
        tag: String,
    ): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface SubSubCategoryDialogClickListner {
        fun selectSubCategory(departmentDto: ReasonmasterV2Response.TicketSubCategory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DialogCustomBinding.inflate(inflater, container, false)
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_sub_category_list)
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        viewBinding.searchSite.visibility = View.GONE
        abstractDialogClick = parentFragment as SubSubCategoryDialogClickListner

        var data =
            arguments?.getSerializable(KEY_DATA) as ArrayList<ReasonmasterV2Response.TicketSubCategory>
        viewBinding.fieldRecyclerView.adapter =
            SubCategoryRecycler(data, object : OnSubSelectedListner {
                override fun onSelected(data: ReasonmasterV2Response.TicketSubCategory) {
                    abstractDialogClick.selectSubCategory(data)
                    dismiss()
                }
            })
        return viewBinding.root
    }
}

class SubCategoryRecycler(
    departmentListDto: ArrayList<ReasonmasterV2Response.TicketSubCategory>,
    var onSelectedListner: OnSubSelectedListner,
) :
    SimpleRecyclerView<ViewItemRowBinding, ReasonmasterV2Response.TicketSubCategory>(
        departmentListDto,
        R.layout.view_item_row
    ) {
    override fun bindItems(
        binding: ViewItemRowBinding,
        items: ReasonmasterV2Response.TicketSubCategory,
        position: Int,
    ) {
        binding.itemName.text = items.name

        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSubSelectedListner {
    fun onSelected(data: ReasonmasterV2Response.TicketSubCategory)
}