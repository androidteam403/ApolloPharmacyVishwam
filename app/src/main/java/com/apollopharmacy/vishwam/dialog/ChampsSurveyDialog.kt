package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.databinding.DialogChampsSurveyBinding

class ChampsSurveyDialog : DialogFragment() {
    val TAG = "CityDialog"

    init {
        setCancelable(false)
    }

    lateinit var abstractDialogClick: AbstractDialogItemClickListner
    lateinit var viewBinding: DialogChampsSurveyBinding

    interface AbstractDialogItemClickListner {
        fun selectCity(item: String)
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

        viewBinding = DialogChampsSurveyBinding.inflate(inflater, container, false)
        viewBinding.closeDialog.visibility = View.VISIBLE
        viewBinding.closeDialog.setOnClickListener {
            dismiss()
        }
        return viewBinding.root
    }
}