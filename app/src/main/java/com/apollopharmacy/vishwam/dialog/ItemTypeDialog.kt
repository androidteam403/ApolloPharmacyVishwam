package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.DialogItemTypeBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.model.ItemViewModel
import com.apollopharmacy.vishwam.dialog.model.SiteViewModel
import com.apollopharmacy.vishwam.util.Utils

class ItemTypeDialog : DialogFragment() {
    val TAG = "ItemTypeDialog"

    init {
        setCancelable(false)
    }

    val items = ArrayList<String>()
    lateinit var abstractDialogClick: AbstractDialogItemClickListner
    lateinit var viewBinding: DialogItemTypeBinding
    lateinit var itemTypeRecyclerView: ItemTypeRecyclerView

    interface AbstractDialogItemClickListner {
        fun selectItem(item: String)
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
        viewBinding = DialogItemTypeBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProviders.of(requireActivity())[ItemViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE
        viewBinding.closeDialog.setOnClickListener {
            items.clear()
            dismiss()
        }
        abstractDialogClick = parentFragment as AbstractDialogItemClickListner
        items.clear()
        viewModel.itemArrayList(items)

        viewModel.fixedArrayList.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.itemTypeRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.itemTypeRecyclerView.visibility = View.VISIBLE
                itemTypeRecyclerView = ItemTypeRecyclerView(it, object : OnSelectedListnerItemType {
                    override fun onSelected(data: String) {
                        abstractDialogClick.selectItem(data)
                        dismiss()
                    }
                })
                viewBinding.itemTypeRecyclerView.adapter = itemTypeRecyclerView
            }
        })

        items.add("powder")
        items.add("capsule")
        items.add("inhaler")

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
                    viewModel.filterItem(textChanged)
                } else {
                    viewModel.itemArrayList(items)
                }
            }

        })

        return viewBinding.root
    }
}

class ItemTypeRecyclerView(
    data: ArrayList<String>,
    var onSelectedListner: OnSelectedListnerItemType
) :
    SimpleRecyclerView<ViewListItemBinding, String>(
        data,
        R.layout.view_list_item
    ) {
    override fun bindItems(binding: ViewListItemBinding, items: String, position: Int) {
        binding.itemName.text = items

        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSelectedListnerItemType {
    fun onSelected(data: String)
}
