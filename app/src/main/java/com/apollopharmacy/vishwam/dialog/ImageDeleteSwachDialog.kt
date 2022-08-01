package com.apollopharmacy.vishwam.dialog

import android.content.Intent
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
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DialogCameraDeleteBinding
import com.apollopharmacy.vishwam.databinding.DialogLogoutBinding
import com.apollopharmacy.vishwam.ui.login.LoginActivity

class ImageDeleteSwachDialog : DialogFragment() {

    private lateinit var viewBinding: DialogCameraDeleteBinding

    init {
        setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        viewBinding = DialogCameraDeleteBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewBinding.close.setOnClickListener { dismiss() }
        viewBinding.noBtnN.setOnClickListener { dismiss() }
        viewBinding.yesBtnN.setOnClickListener {
          dismiss()

        }
    }
}