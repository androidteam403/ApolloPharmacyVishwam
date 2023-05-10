package com.apollopharmacy.vishwam.ui.home.home

import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentHomeBinding
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager
import com.apollopharmacy.vishwam.util.Utlis

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), HomeFragmentCallback {


    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun retrieveViewModel(): HomeViewModel {

        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun setup() {
        hideLoading()

        val userData = LoginRepo.getProfile()
        if (userData != null) {
            viewBinding.customerName.setText("Welcome, " + userData.EMPNAME)
            viewBinding.customerID.setText("Emp ID: " + userData.EMPID)
        }
//        Utlis.hideLoading()
//        hideLoading()

//        if (getDataManager().getRiderActiveStatus() == "Offline") {
//            viewModel.riderUpdateStauts(getDataManager().getLoginToken(),
//                "Offline",
//                requireContext(),
//                this@HomeFragment)
//        } else {
//            viewModel.riderUpdateStauts(getDataManager().getLoginToken(),
//                "Online",
//                requireContext(),
//                this@HomeFragment)
//        }
    }

    fun getDataManager(): SessionManager {
        return SessionManager(context);
    }

    override fun onFialureMessage(message: String) {

    }
}