package com.apollopharmacy.vishwam.dialog

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DialogSearchArticleBinding
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.dialog.model.Row
import com.apollopharmacy.vishwam.dialog.model.TransactionPOSModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Data
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FetchItemModel
import com.apollopharmacy.vishwam.util.Utils

class SearchManagerDialog(var transactionPOSModel: Data) : DialogFragment() {

    lateinit var abstractDialogClick: OnTransactionSearchManagerListnier
    lateinit var viewBinding: DialogSearchArticleBinding
    val TAG = "SearchArticleCodeDialog"

    init {
        setCancelable(false)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        );

        viewBinding = DialogSearchArticleBinding.inflate(inflater, container, false)
        viewBinding.closeDialog.setOnClickListener { dismiss() }
        viewBinding.textHead.text = "Select Manager"
        viewBinding.searchSiteText.setHint("Search Manager")
        viewBinding.searchSite.visibility = View.GONE
        abstractDialogClick = parentFragment as OnTransactionSearchManagerListnier
//        viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (p0.toString().length > 4) {
//                    transactionPOSModel.searchArticleCode(
//                        ArticleCodeRequest(
//                            viewBinding.searchSiteText.text.toString().trim()
//                        )
//                    )
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//
//            }
//        })

        if (transactionPOSModel.listData.rows.isEmpty()) {
            viewBinding.noArticleFound.visibility = View.VISIBLE
            viewBinding.articleCodeRecyclerView.visibility = View.GONE
        } else {
            viewBinding.noArticleFound.visibility = View.GONE
            viewBinding.articleCodeRecyclerView.visibility = View.VISIBLE
            viewBinding.articleCodeRecyclerView.adapter =
                ManagerRecyclerView(transactionPOSModel.listData.rows, object : OnTransactionSearchManagerListnier {
                    override fun onSelectedManager(data: com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Row) {
                        abstractDialogClick.onSelectedManager(data)
                        dismiss()
                    }
                })
        }

        return viewBinding.root
    }
}

class ManagerRecyclerView(
    departmentListDto: List<com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Row>,
    var onSelectedListner: OnTransactionSearchManagerListnier,
) :
    SimpleRecyclerView<ViewItemRowBinding, com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Row>(
        departmentListDto,
        R.layout.view_item_row
    ) {
    override fun bindItems(
        binding: ViewItemRowBinding,
        items: com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Row,
        position: Int,
    ) {
        binding.itemName.text = items.first_name
        binding.root.setOnClickListener {
            onSelectedListner.onSelectedManager(items)
        }
    }
}

interface OnTransactionSearchManagerListnier {
    fun onSelectedManager(data: com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Row)
}