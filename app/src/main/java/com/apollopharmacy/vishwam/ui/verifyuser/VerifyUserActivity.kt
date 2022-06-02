package com.apollopharmacy.vishwam.ui.verifyuser

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences.getIsUserValidated
import com.apollopharmacy.vishwam.databinding.ActivityVerifyUserBinding
import com.apollopharmacy.vishwam.ui.otpview.OtpViewActivity
import com.apollopharmacy.vishwam.ui.splash.SplashActivity
import com.apollopharmacy.vishwam.util.EncryptionManager
import com.apollopharmacy.vishwam.util.Utils

class VerifyUserActivity : AppCompatActivity() {
    lateinit var verifyUserViewModel: VerifyUserViewModel
    lateinit var verifyUserBinding: ActivityVerifyUserBinding
    val TAG = "VerifyUserActivity"
    private val permission = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_user)
        verifyUserViewModel = ViewModelProvider(this)[VerifyUserViewModel::class.java]

//        handleSplashIntent()


        val decriptData = EncryptionManager.decryptData(
            "ljuciU6itzRiYDcCqzhaBW07VH+V46jUvDAhizr64gZD/B603QHzLKRcuzQaIg53",
            Config.ENCRIPTION_KEY
        )
        // val Responsee = Gson().fromJson(decriptData, LoginDetails::class.java)
        Utils.printMessage("LoginResponse", decriptData.toString())


        if (getIsUserValidated()) {
            handleSplashIntent()
        } else {
            verifyUserBinding.verifyButton.setOnClickListener {
                initVerification()
            }
        }
    }

    private fun initVerification() {
        verifyUserBinding.employeeId.onEditorAction(EditorInfo.IME_ACTION_DONE)
        if (verifyUserBinding.employeeId.text?.trim().toString().isEmpty()) {
            Toast.makeText(this, "Employee ID should not be empty", Toast.LENGTH_SHORT).show()
            return
        } else {
            handleNextIntent()
        }
    }

    private fun handleNextIntent() {
        val homeIntent = Intent(this, OtpViewActivity::class.java)
        homeIntent.putExtra(
            "EmpID",
            verifyUserBinding.employeeId.text?.trim().toString().replace(" ", "")
        )
        startActivity(homeIntent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun handleSplashIntent() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permission -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}