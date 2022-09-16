package com.apollopharmacy.vishwam.ui.home.home

import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {



    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun retrieveViewModel(): HomeViewModel {

        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun setup() {

        val userData = LoginRepo.getProfile()
        if (userData != null) {
            viewBinding.customerName.setText("Welcome, " + userData.EMPNAME)
            viewBinding.customerID.setText("Emp ID: " + userData.EMPID)
        }





    }
}