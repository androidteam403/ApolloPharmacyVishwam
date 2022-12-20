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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.DialogSearchArticleBinding
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.dialog.model.SearchManagerDialogViewModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Data
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Row
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.OnSelectedListnerSite
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.SiteRecyclerView

class SearchManagerDialog(var transactionPOSModel: Data) : DialogFragment() {

    lateinit var abstractDialogClick: OnTransactionSearchManagerListnier
    lateinit var viewBinding: DialogSearchArticleBinding
    val TAG = "SearchArticleCodeDialog"
    lateinit var managerRecyclerView: ManagerRecyclerView



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
        //Made changes by naveen
        var viewModel =
            ViewModelProviders.of(requireActivity())[SearchManagerDialogViewModel::class.java]
        viewBinding = DialogSearchArticleBinding.inflate(inflater, container, false)
        viewBinding.closeDialog.setOnClickListener { dismiss() }
        viewBinding.textHead.text = "Select Manager"
        viewBinding.searchSiteText.setHint("Search Manager")
//        viewBinding.searchSite.visibility = View.GONE


        abstractDialogClick = parentFragment as OnTransactionSearchManagerListnier
        viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (p0.toString().length > 4) {
//                    transactionPOSModel.searchArticleCode(
//                        ArticleCodeRequest(
//                            viewBinding.searchSiteText.text.toString().trim()
//                        )
//                    )
//                }
            }

            override fun afterTextChanged(p0: Editable?) {
                var textChanged = p0.toString().trim()
                if (p0.toString().length > 1) {
                    viewModel.filterDataByManager(textChanged)
                } else {
                    viewModel.managerArrayList(transactionPOSModel.listData.rows as ArrayList<Row>)
                }
            }
        })
        viewModel.managerArrayList(transactionPOSModel.listData.rows as ArrayList<Row>)

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (transactionPOSModel.listData.rows.isEmpty()) {
                viewBinding.noArticleFound.visibility = View.VISIBLE
                viewBinding.articleCodeRecyclerView.visibility = View.GONE
            } else {
                viewBinding.noArticleFound.visibility = View.GONE
                viewBinding.articleCodeRecyclerView.visibility = View.VISIBLE
                managerRecyclerView = ManagerRecyclerView(it,
                    object : OnTransactionSearchManagerListnier {
                        override fun onSelectedManager(data: Row) {
                            abstractDialogClick.onSelectedManager(data)
                            dismiss()
                        }
                    })
                viewBinding.articleCodeRecyclerView.adapter =managerRecyclerView

            }
        })
//        if (transactionPOSModel.listData.rows.isEmpty()) {
//            viewBinding.noArticleFound.visibility = View.VISIBLE
//            viewBinding.articleCodeRecyclerView.visibility = View.GONE
//        } else {
//            viewBinding.noArticleFound.visibility = View.GONE
//            viewBinding.articleCodeRecyclerView.visibility = View.VISIBLE
//                managerRecyclerView = ManagerRecyclerView(transactionPOSModel.listData.rows,
//                    object : OnTransactionSearchManagerListnier {
//                        override fun onSelectedManager(data: Row) {
//                            abstractDialogClick.onSelectedManager(data)
//                            dismiss()
//                        }
//                    })
//            viewBinding.articleCodeRecyclerView.adapter =managerRecyclerView
//
//        }

        return viewBinding.root
    }
}

class ManagerRecyclerView(
    departmentListDto: List<Row>,
    var onSelectedListner: OnTransactionSearchManagerListnier,
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
        binding.itemName.text = items.first_name
        binding.root.setOnClickListener {
            onSelectedListner.onSelectedManager(items)
        }
    }
}

interface OnTransactionSearchManagerListnier {
    fun onSelectedManager(data: Row)
}