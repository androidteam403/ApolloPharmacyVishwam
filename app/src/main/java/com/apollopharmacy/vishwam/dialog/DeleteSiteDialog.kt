package com.apollopharmacy.vishwam.dialog

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
import com.apollopharmacy.vishwam.databinding.ConfirmationDialogBinding

class DeleteSiteDialog : DialogFragment() {
    private lateinit var viewBinding: ConfirmationDialogBinding
    lateinit var onSiteClickListener: OnSiteClickListener
    lateinit var siteDataItem: String

    init {
        setCancelable(false)
    }

    interface OnSiteClickListener {
        fun deleteSite(siteDataItem: String)

        fun doNotDeleteSite()
    }

    companion object {
        const val KEY_STORE_DELETE_ITEM = "store_item"
    }

    fun generateParsedData(data: String): Bundle {
        return Bundle().apply {
            putSerializable(DeleteSiteDialog.KEY_STORE_DELETE_ITEM, data)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        viewBinding = ConfirmationDialogBinding.inflate(inflater, container, false)

        onSiteClickListener = parentFragment as OnSiteClickListener
        siteDataItem =
            arguments?.getSerializable(KEY_STORE_DELETE_ITEM) as String
        return viewBinding.root
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.noBtnExit.setOnClickListener {
//            onSiteClickListener.doNotDeleteSite()
            dismiss()
        }
        viewBinding.yesBtnExit.setOnClickListener {
            onSiteClickListener.deleteSite(siteDataItem)
            dismiss()
        }
    }
}