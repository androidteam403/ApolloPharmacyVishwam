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
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView

class ItemTypeDialog: DialogFragment() {



        lateinit var viewBinding: DialogCustomBinding
        lateinit var abstractDialogClick: ItemTypeDialogClickListner

        init {
            setCancelable(false)
        }

        companion object {
            const val KEY_DATA = "data"
        }

        fun generateParsedData(data: ArrayList<ItemTypeDropDownResponse.Rows>): Bundle {
            return Bundle().apply {
                putSerializable(KEY_DATA, data)
            }
        }


        fun generateParsedDatafromreasons(data: ArrayList<String>): Bundle {
            return Bundle().apply {
                putSerializable(KEY_DATA, data)
            }
        }

        interface ItemTypeDialogClickListner {
            fun selectItemType(itemType: String)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            viewBinding = DialogCustomBinding.inflate(inflater, container, false)
            viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_item_type)
            viewBinding.closeDialog.setOnClickListener { dismiss() }

            viewBinding.searchSite.visibility = View.GONE
            var data =
                arguments?.getSerializable(KEY_DATA) as ArrayList<ItemTypeDropDownResponse.Rows>
            viewBinding.fieldRecyclerView.adapter =
                ItemTypeRecycleView(data, object : OnSelectItemTypeListner {
                    override fun onSelected(data: String) {
                        abstractDialogClick = parentFragment as ItemTypeDialogClickListner

                        abstractDialogClick.selectItemType(data)
                        dismiss()


                    }
                })
            return viewBinding.root
        }
    }

    class ItemTypeRecycleView(
        departmentListDto: ArrayList<ItemTypeDropDownResponse.Rows>,
        var onSelectedListner: OnSelectItemTypeListner,
    ) :
        SimpleRecyclerView<ViewListItemBinding, ItemTypeDropDownResponse.Rows>(
            departmentListDto,
            R.layout.view_list_item
        ) {
        override fun bindItems(
            binding: ViewListItemBinding,
            items: ItemTypeDropDownResponse.Rows,
            position: Int,
        ) {
            binding.itemName.text = items.name

            binding.root.setOnClickListener {
                onSelectedListner.onSelected(items.name)
            }
        }
    }

    interface OnSelectItemTypeListner {
        fun onSelected(data: String)
    }



