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
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DialogLogoutBinding
import com.apollopharmacy.vishwam.ui.login.LoginActivity

class SignOutDialog : DialogFragment() {

    private lateinit var viewBinding: DialogLogoutBinding

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
        viewBinding = DialogLogoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.close.setOnClickListener { dismiss() }
        viewBinding.cancelButton.setOnClickListener { dismiss() }
        viewBinding.signOutButton.setOnClickListener {
            LoginRepo.signOutUser()
            val intent = Intent(requireActivity(),LoginActivity::class.java)
            startActivity(intent)
            requireActivity()!!.finish()

        }
    }
}