package com.apollopharmacy.vishwam.dialog

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DialogCommentBinding
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.ui.home.cms.registration.RegistrationViewModel
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis.convertCmsDate

class CommentTicketDialog : DialogFragment() {

    lateinit var viewBinding: DialogCommentBinding
    lateinit var viewModel: RegistrationViewModel
    val TAG = "AcknowledgementDialog"
    var token: String? = null

    lateinit var datanew: ResponseTicktResolvedapi.Data
    protected var mProgressDialog: ProgressDialog? = null

    companion object {
        const val KEY_DATA_ACK = "acknowledgement"
    }

    init {
    }

    fun generateParsedData(data: ArrayList<DataItem>, tag: String): Bundle {
        return Bundle().apply {
            putSerializable(SiteDialog.KEY_DATA, data)
        }
    }



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.setLayout(
            0.95.toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(false)
        viewBinding = DialogCommentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(requireActivity())[RegistrationViewModel::class.java]
        viewBinding.viewModel = viewModel
        datanew = arguments?.getSerializable(SiteDialog.KEY_DATA) as ResponseTicktResolvedapi.Data

        viewBinding.dilogaClose.setOnClickListener { dismiss() }

        viewBinding.ticketNo.text = "${datanew.ticket_id}"
        if (datanew.ticket_created_time != null) {
            viewBinding.regDate.text = "${convertCmsDate(datanew.ticket_created_time.toString())}"
        }


        viewBinding.problemDesc.text = datanew.ticket_reason_name
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_acknowledgement)
        viewBinding.submit.setOnClickListener {

        }

        viewBinding.reject.setOnClickListener {
            dismiss()
        }
        return viewBinding.root
    }



    fun showLoading() {
        hideLoading()
        mProgressDialog = Utils.showLoadingDialog(this.context)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.cancel()
        }
    }
}