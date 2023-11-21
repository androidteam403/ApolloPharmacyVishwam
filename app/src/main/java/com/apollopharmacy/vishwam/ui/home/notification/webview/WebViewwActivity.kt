package com.apollopharmacy.vishwam.ui.home.notification.webview

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityWebViewwBinding
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.apollopharmacy.vishwam.util.Utlis


class WebViewwActivity : AppCompatActivity(), WebViewCallback {
    private lateinit var activityWebViewwBinding: ActivityWebViewwBinding
    private var response: NotificationModelResponse.Data.ListData.Row?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_web_vieww)
        activityWebViewwBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_web_vieww)
        setUp()

    }

    private fun setUp() {
        activityWebViewwBinding.callback=this
        response = intent.getSerializableExtra("response") as NotificationModelResponse.Data.ListData.Row
        Utlis.showLoading(this)
        val webView: WebView = findViewById(R.id.webView_n)
        val webSettings: WebSettings = webView.settings
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webSettings.setJavaScriptEnabled(true)
        webSettings.setAllowContentAccess(true)
        webSettings.setDomStorageEnabled(true)
        webView.webViewClient = WebViewClient()
        webView.setWebViewClient(object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String,
            ) {
                Toast.makeText(applicationContext, description, Toast.LENGTH_SHORT).show()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
//                Utlis.hideLoading()
            }

            override fun onPageFinished(view: WebView, url: String) {
                Utlis.hideLoading()
                val webUrl: String = webView.getUrl()!!
            }
        })
//        webView.loadUrl(url)
        if(response!=null && !response!!.link.isNullOrEmpty()){
//            activityWebViewwBinding.webViewN.setWebViewClient(WebViewClient())
//            activityWebViewwBinding.webViewN.getSettings().setJavaScriptEnabled(true);

            webView.loadUrl(response!!.link!!);

        }
    }

    override fun onclickBackArrow() {
        super.onBackPressed()
    }
}