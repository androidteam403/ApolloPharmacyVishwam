package com.apollopharmacy.vishwam.ui.splash

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.ValidateRequest
import com.apollopharmacy.vishwam.databinding.ActivitySplashBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.login.LoginActivity
import com.apollopharmacy.vishwam.ui.validatepin.ValidatePinActivity
import com.apollopharmacy.vishwam.ui.verifyuser.VerifyUserActivity
import com.apollopharmacy.vishwam.util.EncryptionManager
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.karumi.dexter.BuildConfig
import java.util.*

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    // Disable Logs at Utils
    // Enable Firebase Crashlytics
    // Check Login Emp ID, Password static or dynamic
    // Check Verify User page is checking otp or Splash activity
    // Check User Designation and Modules Dynamic at Main Activity

    private val SPLASH_TIME_OUT = 3000
    lateinit var splashViewModel: SplashViewModel
    lateinit var splashBinding: ActivitySplashBinding
    var isSplashTimeCompleted: Boolean = false
    val TAG = "SplashActivity"
    private val REQUEST_CODE_VERIFY = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        val animZoomOut = AnimationUtils.loadAnimation(
            applicationContext, R.anim.zoom_in
        )
        splashBinding.imageAppLogo.startAnimation(animZoomOut)


        Utils.printMessage(TAG, "Clear Cache is not required")
        if (NetworkUtil.isNetworkConnected(this)) {
            splashViewModel.getSplashScreenData(ValidateRequest(Config.DEVICE_ID, Config.KEY))
            Handler().postDelayed({
                isSplashTimeCompleted = true
            }, SPLASH_TIME_OUT.toLong())
        } else {
            Toast.makeText(
                this,
                resources?.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        splashViewModel.command.observeForever({ command ->
            when (command) {
                is Command.NavigateTo -> {
                    if (isSplashTimeCompleted) {
                        handleNextIntent()
                    } else {
                        Handler().postDelayed({
                            handleNextIntent()
                        }, 2000)
                    }
                }
                is Command.ShowToast -> {
                    splashBinding.serviceLoading.visibility = View.GONE
                    ViswamApp.context?.let {
                        Toast.makeText(it, command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })



    }

    private fun handleNextIntent() {
        if (!Preferences.getIsFCMKeyUpdated()) {
            Preferences.setIsUserValidated(false)
            val homeIntent = Intent(this, VerifyUserActivity::class.java)
            startActivity(homeIntent)
            finish()
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        } else {
            if (Preferences.getIsPinCreated()) {
                if (Preferences.getLoginDate().isEmpty()) {
                    Preferences.setLoginDate(Utils.getCurrentDate())
                    val homeIntent = Intent(this, ValidatePinActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        val datePattern =
                            android.icu.text.SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                        val dateDifference = Utils.getDateDiff(datePattern,
                            Preferences.getLoginDate(),
                            Utils.getCurrentDate()).toInt()
                        if (dateDifference < 30) {
                            val homeIntent = Intent(this, ValidatePinActivity::class.java)
                            startActivity(homeIntent)
                            finish()
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                        } else {
                            println("Login" + "129")
                            val homeIntent = Intent(this, LoginActivity::class.java)
                            startActivity(homeIntent)
                            finish()
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                        }
                    }
                }
//                val homeIntent = Intent(this, ValidatePinActivity::class.java)
//                startActivity(homeIntent)
//                finish()
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            } else {
                println("Login" + "142")
                val homeIntent = Intent(this, LoginActivity::class.java)
                startActivity(homeIntent)
                finish()
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VERIFY) {
            if (resultCode == RESULT_OK) {
                val isUserVerified = data!!.getBooleanExtra("isVerified", false)
                val dialogStatus = data.getBooleanExtra("showDialog", false)
                if (isUserVerified && !dialogStatus) {
                    handleMainActivityIntent()
                } else if (!isUserVerified && dialogStatus) {
                    handleCreatePinActivityIntent()
                }
            } else {
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleMainActivityIntent() {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
        finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun handleCreatePinActivityIntent() {
//        Utils.printMessage(TAG, "Calling activity")
//        val homeIntent = Intent(this, CreatePinActivity::class.java)
//        finishAffinity()
//        startActivity(homeIntent)
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}