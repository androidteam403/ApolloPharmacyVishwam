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
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FetchItemModel
import com.apollopharmacy.vishwam.util.Utils

class SearchTransactionPOSDialog(var transactionPOSModel: TransactionPOSModel) : DialogFragment() {

    lateinit var abstractDialogClick: OnTransactionPOSSelectedListnier
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
        viewBinding.textHead.text = "Select TID"
        viewBinding.searchSiteText.setHint("Search tid")
        viewBinding.searchSite.visibility = View.GONE
        abstractDialogClick = parentFragment as OnTransactionPOSSelectedListnier
        viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 4) {
//                    transactionPOSModel.searchArticleCode(
//                        ArticleCodeRequest(
//                            viewBinding.searchSiteText.text.toString().trim()
//                        )
//                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        if (transactionPOSModel.data.listData.rows.isEmpty()) {
            viewBinding.noArticleFound.text = "No TID Found"
            viewBinding.noArticleFound.visibility = View.VISIBLE
            viewBinding.articleCodeRecyclerView.visibility = View.GONE
        } else {
            viewBinding.noArticleFound.visibility = View.GONE
            viewBinding.articleCodeRecyclerView.visibility = View.VISIBLE
            viewBinding.articleCodeRecyclerView.adapter =
                POSRecyclerView(transactionPOSModel.data.listData.rows, object : OnTransactionPOSSelectedListnier {
                    override fun onSelectedPOSTid(data: Row) {
                        Utils.printMessage(TAG, "Selected Data :: " + data.toString())
                        abstractDialogClick.onSelectedPOSTid(data)
                        dismiss()
                    }
                })
        }

        return viewBinding.root
    }
}

class POSRecyclerView(
    departmentListDto: List<Row>,
    var onSelectedListner: OnTransactionPOSSelectedListnier,
) :
    SimpleRecyclerView<ViewItemRowBinding, Row>(
        departmentListDto,
        R.layout.view_item_row
    ) {
    override fun bindItems(
        binding: ViewItemRowBinding,
        items: Row,
        position: Int,
    ) {
        binding.itemName.text = items.tid
        binding.root.setOnClickListener {
            onSelectedListner.onSelectedPOSTid(items)
        }
    }
}

interface OnTransactionPOSSelectedListnier {
    fun onSelectedPOSTid(data: Row)
}