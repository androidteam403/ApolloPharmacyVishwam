package com.apollopharmacy.vishwam.ui.home.qcfail.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.DialogSiteListBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.util.Utils

class QcSiteDialog : DialogFragment() {


    val TAG = "SiteDialog"

    init {
        setCancelable(false)
    }

    lateinit var viewBinding: DialogSiteListBinding
     lateinit var abstractDialogClick: NewDialogSiteClickListner
    var siteDataArrayList = ArrayList<QcStoreList.Store>()
    lateinit var sitereCyclerView: SiteRecyclerView

    fun generateParsedData(data: ArrayList<QcStoreList.Store>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    companion object {
        const val KEY_DATA = "data"
    }
    object ViewModel{

    }


    interface NewDialogSiteClickListner {


        fun selectSite(departmentDto: QcStoreList.Store)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        viewBinding = DialogSiteListBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProviders.of(requireActivity())[QcSiteViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                sitereCyclerView = SiteRecyclerView(it, object : OnSelectedListnerSite {
                    override fun onSelected(data: QcStoreList.Store) {
                        abstractDialogClick.selectSite(data)
                        dismiss()
                    }
                })
                viewBinding.fieldRecyclerView.adapter = sitereCyclerView
            }
        })


        viewBinding.closeDialog.setOnClickListener {
            siteDataArrayList.clear()
            dismiss()
        }
        abstractDialogClick = activity as NewDialogSiteClickListner


        siteDataArrayList = (arguments?.getSerializable(KEY_DATA) as? ArrayList<QcStoreList.Store>)!!

        viewBinding.searchSite.visibility = View.VISIBLE
        viewModel.qcSiteArrayList(siteDataArrayList)







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
                    viewModel.filterDataBySiteId(textChanged)
                } else {
                    viewModel.qcSiteArrayList(siteDataArrayList)
                }
            }
        })
        return viewBinding.root
    }
}

class SiteRecyclerView(
    departmentListDto: ArrayList<QcStoreList.Store>,
    var onSelectedListner: OnSelectedListnerSite,
) :
    SimpleRecyclerView<ViewListItemBinding, QcStoreList.Store>(
        departmentListDto,
        R.layout.view_list_item
    ) {
    override fun bindItems(binding: ViewListItemBinding, items: QcStoreList.Store, position: Int) {
        binding.itemName.text = "${items.siteid}, ${items.sitename}"
        binding.root.setOnClickListener {

            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSelectedListnerSite {
    fun onSelected(data: QcStoreList.Store)
}

