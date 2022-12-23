package com.apollopharmacy.vishwam.ui.home.drugmodule.model

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
import com.apollopharmacy.vishwam.databinding.DialogCustomBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView

class ItemTypeDialog: DialogFragment() {



        lateinit var viewBinding: DialogCustomBinding
        lateinit var abstractDialogClick: ItemTypeDialogClickListner
    var siteDataArrayList= ArrayList<ItemTypeDropDownResponse.Rows>()
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
            fun selectItemType(row: ItemTypeDropDownResponse.Rows)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            viewBinding = DialogCustomBinding.inflate(inflater, container, false)
            viewBinding.textHead.text = context?.resources?.getString(R.string.label_select_item_type)
            viewBinding.closeDialog.setOnClickListener { dismiss() }

            viewBinding.searchSite.visibility = View.VISIBLE
            var viewModel = ViewModelProviders.of(requireActivity())[ItemTypeViewModel::class.java]
            viewBinding.searchSiteText.setHint("Search Item Type ")
            viewBinding.searchSiteText.inputType = InputType.TYPE_CLASS_TEXT
            siteDataArrayList =
                arguments?.getSerializable(KEY_DATA) as ArrayList<ItemTypeDropDownResponse.Rows>
            viewModel.siteArrayList(siteDataArrayList)

            viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
                if (it.size == 0) {
                    viewBinding.siteNotAvailable.text = "Item type not available"
                    viewBinding.siteNotAvailable.visibility = View.VISIBLE
                    viewBinding.fieldRecyclerView.visibility = View.GONE
                } else {
                    viewBinding.siteNotAvailable.visibility = View.GONE
                    viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                    viewBinding.fieldRecyclerView.adapter =
                        ItemTypeRecycleView(it, object : OnSelectItemTypeListner {
                            override fun onSelected(row: ItemTypeDropDownResponse.Rows) {
                                abstractDialogClick = parentFragment as ItemTypeDialogClickListner

                                abstractDialogClick.selectItemType(row)
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
                onSelectedListner.onSelected(items)
            }
        }
    }

    interface OnSelectItemTypeListner {
        fun onSelected(row: ItemTypeDropDownResponse.Rows)
    }



