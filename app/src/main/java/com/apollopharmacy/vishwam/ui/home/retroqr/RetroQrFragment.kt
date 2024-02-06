package com.apollopharmacy.vishwam.ui.home.retroqr

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentRetroQrBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.RetroQrUploadActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.SitesDialogQr
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner.RetroQrScannerActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.selectqrretrositeid.SelectRetroQrSiteIDActivity
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson

class RetroQrFragment : BaseFragment<RetroQrViewModel, FragmentRetroQrBinding>(),
    SitesDialogQr.NewDialogSiteClickListner, RetroQrFragmentCallback {
    override val layoutRes: Int
        get() = R.layout.fragment_retro_qr

    override fun retrieveViewModel(): RetroQrViewModel {
        return ViewModelProvider(this)[RetroQrViewModel::class.java]
    }

    override fun setup() {


        viewBinding.incharge.setText(Preferences.getToken())
        viewModel.getProxySiteListResponse(this)

        if (Preferences.getQrSiteId().isEmpty()) {
//            showLoading()
            val i = Intent(context, SelectRetroQrSiteIDActivity::class.java)
            i.putExtra("modulename", "CHAMPS")
            startActivityForResult(i, 781)
        } else {
            viewBinding.searchbystore.setText(Preferences.getQrSiteId() + " , " + Preferences.getQrSiteName())

            viewBinding.storename.setText(Preferences.getQrSiteId() + " , " + Preferences.getQrSiteName())
        }

        if (MainActivity.mInstance.employeeRoleRetroQr.isNullOrEmpty() || MainActivity.mInstance.employeeRoleRetroQr.equals(
                "NO",
                true
            )
        ) {
            viewBinding.reviewButton.visibility = View.VISIBLE
            viewBinding.uploadButton.visibility = View.GONE

        } else {
            viewBinding.reviewButton.visibility = View.GONE
            viewBinding.uploadButton.visibility = View.VISIBLE
        }

        MainActivity.mInstance.scannerIcon.setOnClickListener {
            val intent = Intent(requireContext(), RetroQrScannerActivity::class.java)
            startActivity(intent)
        }

        viewBinding.searchBar.setOnClickListener {
            SitesDialogQr().apply {
                arguments =
                    SitesDialogQr().generateParsedData(viewModel.getSiteData())
            }.show(childFragmentManager, "")
//            val i = Intent(context, SelectRetroQrSiteIDActivity::class.java)
//            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivityForResult(i, 781)
        }
        viewBinding.uploadButton.setOnClickListener {
            val intent = Intent(requireContext(), RetroQrUploadActivity::class.java)
            intent.putExtra("activity", "upload")
            startActivity(intent)
        }
        viewBinding.reviewButton.setOnClickListener {
            val intent = Intent(requireContext(), RetroQrUploadActivity::class.java)
            intent.putExtra("activity", "review")

            startActivity(intent)
        }

        viewModel.commands.observeForever {
            when (it) {
                is RetroQrViewModel.Command.ShowToast -> {
                    Utlis.hideLoading()
                    Preferences.setQrSiteIdList(Gson().toJson(viewModel.getSiteData()))
                    Preferences.setSiteIdListFetchedQrRetro(true)
                }


                else -> {}
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var isSiteIdEmpty: Boolean = false

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 781) {
                if (data != null) {
                    isSiteIdEmpty = data!!.getBooleanExtra("isSiteIdEmpty", isSiteIdEmpty)
                    if (isSiteIdEmpty) {
                        MainActivity.mInstance.onBackPressed()
                    }

                    if (data.getStringExtra("siteId").toString()
                            .isNotEmpty() && data.getStringExtra("siteId").toString() != "null"
                    ) {
                        viewBinding.storename.setText(
                            data.getStringExtra("siteId")
                                .toString() + " , " + data.getStringExtra("sitename").toString()
                        )

                        viewBinding.searchbystore.setText(
                            data.getStringExtra("siteId")
                                .toString() + " , " + data.getStringExtra("sitename").toString()
                        )
                    }


                }

            }
        }
    }

    override fun selectSite(departmentDto: StoreDetailsModelResponse.Row) {
        Preferences.setQrSiteId(departmentDto.site.toString())
        Preferences.setQrSiteName(departmentDto.storeName.toString())

        viewBinding.searchbystore.text = departmentDto.site + ", " + departmentDto.storeName
        viewBinding.storename.text = departmentDto.site + ", " + departmentDto.storeName

    }

    override fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>) {
        Utlis.hideLoading()
    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
        Utlis.hideLoading()
    }
}