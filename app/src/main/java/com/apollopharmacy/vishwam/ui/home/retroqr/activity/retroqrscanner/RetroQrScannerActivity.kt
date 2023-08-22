package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrScannerBinding
import com.apollopharmacy.vishwam.ui.home.apolloassets.AssetsFragment
import com.apollopharmacy.vishwam.ui.home.model.GetImageByRackResponse
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagepreview.RetroImagePreviewActivity
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.zxing.Result

class RetroQrScannerActivity : AppCompatActivity(), RetroQrScannerCallback {
    private lateinit var activityRetroQrScannerBinding: ActivityRetroQrScannerBinding
    private lateinit var viewModel: RetroQrScannerViewModel
    private lateinit var codeScanner: CodeScanner
    var imageBlob: String = ""
    var activity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrScannerBinding = DataBindingUtil.setContentView(
            this@RetroQrScannerActivity,
            R.layout.activity_retro_qr_scanner
        )
        viewModel =
            ViewModelProvider(this@RetroQrScannerActivity)[RetroQrScannerViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        if (intent != null && !intent.getStringExtra("activity").isNullOrEmpty()) {
            activity = intent.getStringExtra("activity")!!


        }
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        if (!AssetsFragment.hasPermission(this@RetroQrScannerActivity, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(
                this@RetroQrScannerActivity,
                PERMISSIONS,
                PERMISSION_ALL
            )
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
                    imageBlob = result.text.toString()
                    runOnUiThread {
                        // Show a loading dialog on the main UI thread
                        Utils.showLoadingDialog(this@RetroQrScannerActivity) // Assuming RetroQrScannerActivity is the current activity
                    }
                    viewModel.getImageUrlsFromRackId(imageBlob, this@RetroQrScannerActivity)
                }
            }
        })
    }

    override fun onSuccessGetImageUrlApiCall(imageByRackResponse: GetImageByRackResponse) {
        if (imageByRackResponse != null && imageByRackResponse.imageurl != null) {
            val intent = Intent(this@RetroQrScannerActivity, RetroImagePreviewActivity::class.java)
            intent.putExtra("activity", "scanner")
            intent.putExtra("rackNo", imageBlob)
            intent.putExtra("SCANNER_RESPONSE", imageByRackResponse)
            hideLoading()
            startActivityForResult(intent, 210)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 210) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

    override fun onFailureGetImageUrlApiCall(message: String) {
    }
}