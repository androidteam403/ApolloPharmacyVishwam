package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrScannerBinding
import com.apollopharmacy.vishwam.ui.home.apolloassets.AssetsFragment
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagepreview.RetroImagePreviewActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner.model.ScannerResponse
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.Result

class RetroQrScannerActivity : AppCompatActivity(), RetroQrScannerCallback {
    private lateinit var activityRetroQrScannerBinding: ActivityRetroQrScannerBinding
    private lateinit var viewModel: RetroQrScannerViewModel
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrScannerBinding = DataBindingUtil.setContentView(this@RetroQrScannerActivity,
            R.layout.activity_retro_qr_scanner)
        viewModel =
            ViewModelProvider(this@RetroQrScannerActivity)[RetroQrScannerViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        if (!AssetsFragment.hasPermission(this@RetroQrScannerActivity, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this@RetroQrScannerActivity,
                PERMISSIONS,
                PERMISSION_ALL)
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        codeScanner =
            CodeScanner(this@RetroQrScannerActivity, activityRetroQrScannerBinding.scanner)
        codeScanner.startPreview()
        codeScanner.isAutoFocusEnabled = true
        codeScanner.formats = CodeScanner.ALL_FORMATS
//        codeScanner.scanMode = ScanMode.CONTINUOUS
        codeScanner.setDecodeCallback(object : DecodeCallback {
            override fun onDecoded(result: Result) {
                if (result != null) {
                    val imageJsonBolb = result.text.toString()
                    viewModel.getImageUrl(imageJsonBolb, this@RetroQrScannerActivity)
                }
            }
        })
    }

    override fun onSuccessGetImageUrlApiCall(scannerResponse: ScannerResponse) {
        if (scannerResponse != null && scannerResponse.imageUrl != null && scannerResponse.imageUrl!!.toString().isNotEmpty()) {
            val intent = Intent(this@RetroQrScannerActivity, RetroImagePreviewActivity::class.java)
            intent.putExtra("SCANNER_RESPONSE", scannerResponse)
            startActivity(intent)
        }
    }

    override fun onFailureGetImageUrlApiCall(message: String) {
    }
}