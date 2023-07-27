package com.apollopharmacy.vishwam.ui.home.home

import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentHomeBinding
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt


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
        Utlis.hideLoading()
        hideLoading()

//        if (getDataManager().getRiderActiveStatus() == "Offline") {
//            viewModel.riderUpdateStauts(
//                getDataManager().getLoginToken(),
//                "Offline",
//                requireContext(),
//                this@HomeFragment
//            )
//        } else {
//            viewModel.riderUpdateStauts(
//                getDataManager().getLoginToken(),
//                "Online",
//                requireContext(),
//                this@HomeFragment
//            )
//        }



        val key = "blobfilesload"

        val encryptedUrl : String? = "cZm5NWe6ZyFdAND/ey5SBvzCn2o2jko3x1FMPE25e3NJhZyCBYvJwYx5A5YXspABZq1Qq8yrCQFolXVLXAlCNaMAN/9IvyXrb3d2oRBWy0gFQAUOwoAYvuk+tI4uqV5wQSidhz4l+IjDVyxhqwHwn9Fapzzp17jzkrs0ovVnj9UrcYrZ6I5oXqYKmVq94eRusj/vHGuY5YU1nA70HP10Zvwdgus0ckFwPdldomN9p3goBnFcnxXFcVnHgaLgDpOjDFjoG2ZTAMX92z0YYCLE3A==" //"OpyhspbP/YM14k2ir6LLBOtTwnSksuHUi6IflK5CI6oSa8uQC4GH+WWi/5WmEhng0Eksq2KL1rB08LOUFfmLCghazPsB1laA2nPFrYxlRK8lZhcAoupSCkWNA1D8qn20htUe2/xfrbSYaVI8W/ObMxNcOOGytjH23scXguFGJMMdRtcJgBH0UWGcMG3WqDxRANy9wDuBmYsx3FnzaNhb+FYJkN3LTcRc8JtOrKkBOw2dLyGKBoseVc/aonQAvf5pOW+YpskPHG5rd9NUGssJRw=="
       RijndaelCipherEncryptDecrypt().decrypt(encryptedUrl, key)

        val decryptUrl : String? = "https://pharmteststorage.blob.core.windows.net/test/vendor/SENSING/Apollo_20230713223659174.png?sv=2022-11-02&se=9999-12-31T23:59:59Z&sr=b&sp=r&sig=%2B%2FsZgYz2WC0lrLsNfSlZSztsNPFPcuh2hz%2FrvDKbQl8%3D"
        RijndaelCipherEncryptDecrypt().encrypt(decryptUrl,key)
    }

    fun getDataManager(): SessionManager {
        return SessionManager(context);
    }

    override fun onFialureMessage(message: String) {

    }
}