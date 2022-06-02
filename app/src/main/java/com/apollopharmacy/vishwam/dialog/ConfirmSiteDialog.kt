package com.apollopharmacy.eposmobileapp.ui.dashboard

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.DialogConfirmSiteBinding

class ConfirmSiteDialog : DialogFragment() {

    private lateinit var viewBinding: DialogConfirmSiteBinding
    lateinit var onSiteClickListener: OnSiteClickListener
    lateinit var siteDataItem: StoreListItem

    init {
        setCancelable(false)
    }

    companion object {
        const val KEY_STORE_ITEM = "store_item"
    }

    fun generateParsedData(data: StoreListItem): Bundle {
        return Bundle().apply {
            putSerializable(KEY_STORE_ITEM, data)
        }
    }

    interface OnSiteClickListener {
        fun confirmSite(departmentDto: StoreListItem)

        fun cancelledSite()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        viewBinding = DialogConfirmSiteBinding.inflate(inflater, container, false)

        onSiteClickListener = parentFragment as OnSiteClickListener
        siteDataItem = arguments?.getSerializable(KEY_STORE_ITEM) as StoreListItem
        return viewBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.cancelButton.setOnClickListener {
            onSiteClickListener.cancelledSite()
            dismiss()
        }
        viewBinding.signOutButton.setOnClickListener {
            onSiteClickListener.confirmSite(siteDataItem)
            dismiss()
        }
    }
}