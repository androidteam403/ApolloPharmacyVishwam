package com.apollopharmacy.vishwam.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.BuildConfig
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.ValidateRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.databinding.ActivitySplashBinding
import com.apollopharmacy.vishwam.dialog.AppUpdateDialog
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.login.LoginActivity
import com.apollopharmacy.vishwam.ui.validatepin.ValidatePinActivity
import com.apollopharmacy.vishwam.ui.verifyuser.VerifyUserActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson
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
        val appTheme = Preferences.getAppTheme()
        if (appTheme.equals("DARK", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (appTheme.equals("LIGHT", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
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

        splashViewModel.command.observeForever { command ->
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
        }


    }

    private fun handleNextIntent() {
//        val validateResponse = Preferences.getGlobalResponse()
//        val data = Gson().fromJson(validateResponse, ValidateResponse::class.java)
//
//        if (data.BUILDDETAILS!!.APPAVALIBALITY) {
//            if (data.BUILDDETAILS!!.BUILDVERSION!!.toInt() != BuildConfig.VERSION_CODE) {
//                if (data.BUILDDETAILS!!.FORCEDOWNLOAD) {
//                    updateinApp()
//                } else {
//
//                }
//            } else {
//                handleNextIntentnew()
//            }
//        } else {
//            handleNextIntentnew()
//        }

//        updateinApp()
        onCheckBuildDetails()

//        if (!Preferences.getIsFCMKeyUpdated()) {
//            Preferences.setIsUserValidated(false)
//            val homeIntent = Intent(this, VerifyUserActivity::class.java)
//            startActivity(homeIntent)
//            finish()
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//        } else {
//            if (Preferences.getIsPinCreated()) {
//                if (Preferences.getLoginDate().isEmpty()) {
//                    Preferences.setLoginDate(Utils.getCurrentDate())
//                    val homeIntent = Intent(this, ValidatePinActivity::class.java)
//                    startActivity(homeIntent)
//                    finish()
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//                } else {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                        val datePattern =
//                            android.icu.text.SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
//                        val dateDifference = Utils.getDateDiff(datePattern,
//                            Preferences.getLoginDate(),
//                            Utils.getCurrentDate()).toInt()
//                        if (dateDifference < 30) {
//                            val homeIntent = Intent(this, ValidatePinActivity::class.java)
//                            startActivity(homeIntent)
//                            finish()
//                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//                        } else {
//                            println("Login" + "129")
//                            val homeIntent = Intent(this, LoginActivity::class.java)
//                            startActivity(homeIntent)
//                            finish()
//                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//                        }
//                    }
//                }
////                val homeIntent = Intent(this, ValidatePinActivity::class.java)
////                startActivity(homeIntent)
////                finish()
////                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//            } else {
//                println("Login" + "142")
//                val homeIntent = Intent(this, LoginActivity::class.java)
//                startActivity(homeIntent)
//                finish()
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//            }
//        }
    }

    fun updateinApp() {
        val appUpdateManager = AppUpdateManagerFactory.create(this@SplashActivity)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // The current activity making the update request.
                    this,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                        .setAllowAssetPackDeletion(true)
                        .build(),
                    // Include a request code to later monitor this update request.
                    1)
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // The current activity making the update request.
                    this,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                        .setAllowAssetPackDeletion(true)
                        .build(),
                    // Include a request code to later monitor this update request.
                    1)
            } else {
                handleNextIntentnew()
            }
        }
    }

    fun handleNextIntentnew() {
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
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                handleNextIntent()
            }
        }
    }

    private fun handleMainActivityIntent() {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
        finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun onCheckBuildDetails() {
        val globalRes = Preferences.getGlobalResponse()
        val data = Gson().fromJson(globalRes, ValidateResponse::class.java)
        val buildDetailsEntity = data.BUILDDETAILS
        if (buildDetailsEntity != null) {
            if (buildDetailsEntity.APPAVALIBALITY) {
                if (buildDetailsEntity.BUILDVERSION.toInt() > BuildConfig.VERSION_CODE) {
                    if (buildDetailsEntity.FORCEDOWNLOAD) {
                        displayAppInfoDialog(
                            "Update Available",
                            buildDetailsEntity.BUILDMESSAGE,
                            "Update",
                            "",
                            buildDetailsEntity.DOWNLOADURL
                        )
                    } else {
                        displayAppInfoDialog(
                            "Update Available",
                            buildDetailsEntity.BUILDMESSAGE,
                            "Update Now",
                            "No thanks",
                            buildDetailsEntity.DOWNLOADURL
                        )
                    }
                }else{
                    handleNextIntentnew()
                }
            } else {
                handleNextIntentnew()
            }
        }
    }

    fun displayAppInfoDialog(
        title: String?,
        subTitle: String?,
        positiveBtn: String?,
        negativeBtn: String?,
        downloadUrl: String?,
    ) {
        val dialogView = AppUpdateDialog(this@SplashActivity)
        dialogView.setTitle(title)
        dialogView.setSubtitle(subTitle)
        if (!TextUtils.isEmpty(positiveBtn)) {
            dialogView.setPositiveLabel(positiveBtn)
            dialogView.setPositiveListener(View.OnClickListener {
//                dialogView.dismiss()
                val viewIntent = Intent("android.intent.action.VIEW",
                    Uri.parse(downloadUrl))
                startActivity(viewIntent)
            })
        }
        if (!TextUtils.isEmpty(negativeBtn)) {
            dialogView.setNegativeLabel(negativeBtn)
            dialogView.setNegativeListener(View.OnClickListener {
                dialogView.dismiss()
                handleNextIntentnew()
            })
        } else {
            dialogView.setDialogDismiss()
        }
        dialogView.show()
    }

    private fun handleCreatePinActivityIntent() {
//        Utils.printMessage(TAG, "Calling activity")
//        val homeIntent = Intent(this, CreatePinActivity::class.java)
//        finishAffinity()
//        startActivity(homeIntent)
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}
//https://www.section.io/engineering-education/android-application-in-app-update-using-android-studio/
//https://developer.android.com/guide/playcore/in-app-updates#check-priority
//https://stackoverflow.com/questions/61115215/how-to-set-update-priority-for-a-new-android-release