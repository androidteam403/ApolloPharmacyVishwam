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
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewComplaintRegistration
import com.apollopharmacy.vishwam.databinding.DialogSubmitcomplaintBinding

class SubmitcomplaintDialog : DialogFragment() {
    private lateinit var viewBinding: DialogSubmitcomplaintBinding

    lateinit var abstractDialogClick: AbstractDialogSubmitClickListner

    init {
        setCancelable(false)
    }


    companion object {
        const val KEY_DATA = "submitcomplaint"
    }

    fun generateParsedData(data: ResponseNewComplaintRegistration): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        viewBinding = DialogSubmitcomplaintBinding.inflate(inflater, container, false)

        abstractDialogClick = parentFragment as AbstractDialogSubmitClickListner
        return viewBinding.root


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewBinding.close.setOnClickListener { dismiss() }
        viewBinding.okButton.setOnClickListener {
            abstractDialogClick.confirmsavetheticket()
            dismiss()

        }
        /* viewBinding.signOutButton.setOnClickListener {
             LoginRepo.signOutUser()
             requireActivity()!!.finishAndRemoveTask()
         }*/
        //datanew.message
        val datanew = arguments?.getSerializable(KEY_DATA) as ResponseNewComplaintRegistration
        viewBinding.messagetext.text = "Ticket created successfully"
        //"Successfully Ticket Created"+"\n"+"Your Ticket Id is"+"\n"+datanew.data.ticket_id
        viewBinding.ticketId.text = "${datanew.data.ticket_id}"
    }

    interface AbstractDialogSubmitClickListner {
        fun confirmsavetheticket()
    }
}