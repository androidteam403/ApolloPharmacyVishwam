package com.apollopharmacy.vishwam.ui.home.retroqr

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentRetroQrBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.RetroQrUploadActivity

class RetroQrFragment : BaseFragment<RetroQrViewModel, FragmentRetroQrBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_retro_qr

    override fun retrieveViewModel(): RetroQrViewModel {
        return ViewModelProvider(this)[RetroQrViewModel::class.java]
    }

    override fun setup() {
        viewBinding.uploadButton.setOnClickListener {
            val intent = Intent(requireContext(), RetroQrUploadActivity::class.java)
            startActivity(intent)
        }
    }
}