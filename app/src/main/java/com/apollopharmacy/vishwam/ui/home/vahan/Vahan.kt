package com.apollopharmacy.vishwam.ui.home.vahan

import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentVahanBinding

class Vahan : BaseFragment<VahanViewModel, FragmentVahanBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_vahan


    override fun retrieveViewModel(): VahanViewModel {
        return ViewModelProvider(this).get(VahanViewModel::class.java)
    }

    override fun setup() {
    }




}