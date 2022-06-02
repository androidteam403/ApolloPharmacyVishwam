package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.DialogStatusTrackBinding
import com.apollopharmacy.vishwam.dialog.model.SiteViewModel

class DiscountTrackDialog : DialogFragment() {

    init {
        setCancelable(false)
    }

    lateinit var viewBinding: DialogStatusTrackBinding
    lateinit var statusArrayList: ArrayList<PendingOrder.STATUSItem>

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: ArrayList<PendingOrder.STATUSItem>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
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
        viewBinding = DialogStatusTrackBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProviders.of(requireActivity())[SiteViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE
        viewBinding.closeDialog.setOnClickListener { dismiss() }

        statusArrayList = arguments?.getSerializable(KEY_DATA) as ArrayList<PendingOrder.STATUSItem>
//        viewModel.siteArrayList(siteDataArrayList)

        return viewBinding.root
    }
}