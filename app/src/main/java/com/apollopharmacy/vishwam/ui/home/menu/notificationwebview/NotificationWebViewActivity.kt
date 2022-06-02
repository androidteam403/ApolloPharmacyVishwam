package com.apollopharmacy.vishwam.ui.home.menu.notificationwebview

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityNotificationWebViewBinding
import com.apollopharmacy.vishwam.util.Utils
import java.lang.Exception

class NotificationWebViewActivity : AppCompatActivity() {
    lateinit var viewModel: NotificationWebViewModel
    lateinit var webViewBinding: ActivityNotificationWebViewBinding
    val TAG = "NotificationWebViewActivity"
    var webURL: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_notification_web_view)
        viewModel = ViewModelProvider(this)[NotificationWebViewModel::class.java]

        if (intent.extras != null) {
            webURL = intent.getStringExtra("WebURL").toString()
        }

        webViewBinding.webViewBack.setOnClickListener {
            onBackPressed()
        }

        if (webURL.isNotEmpty()) {
            val progressBar = ProgressDialog(this@NotificationWebViewActivity)
            progressBar.setMessage("Please wait...")
            webViewBinding.webView.loadUrl(webURL)
            webViewBinding.webView.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (!progressBar.isShowing()) {
                        try {
                            progressBar.show()
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onPageFinished(view: WebView, url: String) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss()
                    }
                }

                override fun onReceivedError(
                    view: WebView,
                    errorCode: Int,
                    description: String,
                    failingUrl: String,
                ) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss()
                    }
                }
            })
        } else {
            Toast.makeText(this, "Unable to Load Website", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}