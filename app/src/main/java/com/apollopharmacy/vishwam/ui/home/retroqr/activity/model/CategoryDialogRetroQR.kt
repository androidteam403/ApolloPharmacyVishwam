package com.apollopharmacy.vishwam.ui.home.retroqr.activity.model

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
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.CategoryDetailsResponse

class CategoryDialogRetroQR: DialogFragment() {



        lateinit var viewBinding: DialogCustomBinding
        lateinit var abstractDialogClick: categoryDialogClickListner

        init {
            setCancelable(false)
        }

        companion object {
            const val KEY_DATA = "data"
        }

        fun generateParsedData(data: ArrayList<CategoryDetailsResponse.CategoryDetail>): Bundle {
            return Bundle().apply {
                putSerializable(KEY_DATA, data)
            }
        }


        fun generateParsedDatafromreasons(data: ArrayList<CategoryDetailsResponse.CategoryDetail>): Bundle {
            return Bundle().apply {
                putSerializable(KEY_DATA, data)
            }
        }

        interface categoryDialogClickListner {
            fun selectCategory(category: CategoryDetailsResponse.CategoryDetail)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)
            viewBinding = DialogCustomBinding.inflate(inflater, container, false)
            viewBinding.textHead.text = "Select Category"
            viewBinding.siteNotAvailabletext.text="No Items Found"
            viewBinding.closeDialog.setOnClickListener { dismiss() }

            viewBinding.searchSite.visibility = View.GONE
            var data =
                arguments?.getSerializable(KEY_DATA) as ArrayList<CategoryDetailsResponse.CategoryDetail>
            viewBinding.fieldRecyclerView.adapter =
                CustomRecyclerViewQr(data, object : OnSelectListnerQr {
                    override fun onSelected(data: CategoryDetailsResponse.CategoryDetail) {
                        abstractDialogClick = activity as categoryDialogClickListner

                        abstractDialogClick.selectCategory(data)
                        dismiss()


                    }
                })
            return viewBinding.root
        }
    }

    class CustomRecyclerViewQr(
        departmentListDto: ArrayList<CategoryDetailsResponse.CategoryDetail>,
        var onSelectedListner: OnSelectListnerQr,
    ) :
        SimpleRecyclerView<ViewListItemBinding, CategoryDetailsResponse.CategoryDetail>(
            departmentListDto,
            R.layout.view_list_item
        ) {
        override fun bindItems(
            binding: ViewListItemBinding,
            items: CategoryDetailsResponse.CategoryDetail,
            position: Int,
        ) {
            binding.itemName.text = items.categoryname

            binding.root.setOnClickListener {
                onSelectedListner.onSelected(items)
            }
        }
    }

    interface OnSelectListnerQr {
        fun onSelected(data: CategoryDetailsResponse.CategoryDetail)
    }



