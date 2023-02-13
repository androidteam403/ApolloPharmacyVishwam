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
import com.apollopharmacy.vishwam.data.model.cms.ArticleCodeRequest
import com.apollopharmacy.vishwam.data.model.cms.ArticleCodeResponse
import com.apollopharmacy.vishwam.databinding.DialogSearchArticleBinding
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.dialog.model.ArticleCodeViewModel
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FetchItemModel
import com.apollopharmacy.vishwam.util.Utils

class SearchArticleCodeDialog : DialogFragment() {

    lateinit var abstractDialogClick: SearchArticleDialogClickListner
    lateinit var viewBinding: DialogSearchArticleBinding
    lateinit var viewModel: ArticleCodeViewModel
    val TAG = "SearchArticleCodeDialog"

    init {
        setCancelable(false)
    }

    interface SearchArticleDialogClickListner {
        fun selectSubCategory(articleData: FetchItemModel.Rows)
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
        viewModel = ViewModelProviders.of(requireActivity())[ArticleCodeViewModel::class.java]
        viewBinding.closeDialog.setOnClickListener { dismiss() }
        viewBinding.searchSiteText.setHint(getString(R.string.label_search_article))
        viewBinding.registration = viewModel
        abstractDialogClick = parentFragment as SearchArticleDialogClickListner
        viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 4) {
                    viewModel.searchArticleCode(
                        ArticleCodeRequest(
                            viewBinding.searchSiteText.text.toString().trim()
                        )
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        viewModel.mutableLiveData.observe(requireActivity(), Observer {
            if (it.size === 0) {
                viewBinding.noArticleFound.visibility = View.VISIBLE
                viewBinding.articleCodeRecyclerView.visibility = View.GONE
            } else {
                viewBinding.noArticleFound.visibility = View.GONE
                viewBinding.articleCodeRecyclerView.visibility = View.VISIBLE
                viewBinding.articleCodeRecyclerView.adapter =
                    ArticleRecyclerView(it, object : OnArticleSelectedListner {
                        override fun onSelected(data: FetchItemModel.Rows) {
                            Utils.printMessage(TAG, "Selected Data :: " + data.toString())
                            abstractDialogClick.selectSubCategory(data)
                            dismiss()
                        }
                    })
            }
        })
        return viewBinding.root
    }
}

class ArticleRecyclerView(
    departmentListDto: ArrayList<FetchItemModel.Rows>,
    var onSelectedListner: OnArticleSelectedListner,
) :
    SimpleRecyclerView<ViewItemRowBinding, FetchItemModel.Rows>(
        departmentListDto,
        R.layout.view_item_row
    ) {
    override fun bindItems(
        binding: ViewItemRowBinding,
        items: FetchItemModel.Rows,
        position: Int,
    ) {
        binding.itemName.text = items.artCodeName
        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnArticleSelectedListner {
    fun onSelected(data: FetchItemModel.Rows)
}