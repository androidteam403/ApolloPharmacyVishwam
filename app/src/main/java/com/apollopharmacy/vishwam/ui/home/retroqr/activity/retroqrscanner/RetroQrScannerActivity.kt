package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrScannerBinding
import com.apollopharmacy.vishwam.ui.home.apolloassets.AssetsFragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.zxing.Result

class RetroQrScannerActivity : AppCompatActivity() {
    private lateinit var activityRetroQrScannerBinding: ActivityRetroQrScannerBinding
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrScannerBinding = DataBindingUtil.setContentView(this@RetroQrScannerActivity,
            R.layout.activity_retro_qr_scanner)
        setUp()
    }

    private fun setUp() {
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        if (!AssetsFragment.hasPermission(this@RetroQrScannerActivity, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this@RetroQrScannerActivity, PERMISSIONS, PERMISSION_ALL)
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        codeScanner = CodeScanner(this@RetroQrScannerActivity, activityRetroQrScannerBinding.scanner)
        codeScanner.startPreview()
        codeScanner.isAutoFocusEnabled = true
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.setDecodeCallback(object : DecodeCallback {
            override fun onDecoded(result: Result) {
                if (result != null) {
                    Log.i("TAG", "onDecoded: SUCCESS")
                }
            }
        })
    }
}