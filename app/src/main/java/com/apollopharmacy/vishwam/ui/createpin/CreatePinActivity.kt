package com.apollopharmacy.vishwam.ui.createpin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.MPinRequest
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.util.ForgotPinActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.github.omadahealth.lollipin.lib.managers.AppLock

@Suppress("DEPRECATION")
class CreatePinActivity : AppCompatActivity() {
    lateinit var viewModel: CreatePinViewModel
    private val REQUEST_CODE_ENABLE = 11
    private val REQUEST_CODE_CHANGE = 13
    lateinit var userData: LoginDetails
    var isUpdatePwdStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)

        viewModel = ViewModelProvider(this)[CreatePinViewModel::class.java]
        userData = LoginRepo.getProfile()!!

        isUpdatePwdStatus = intent.getBooleanExtra("isUpdatePwd", false)

        val intent = Intent(this@CreatePinActivity, ForgotPinActivity::class.java)
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK)
        startActivityForResult(intent, REQUEST_CODE_ENABLE)

        viewModel.commands.observeForever({ command ->
            Utlis.hideLoading()
            when (command) {
                is Command.NavigateTo -> {
                    Preferences.setIsPinCreated(true)
                    val homeIntent = Intent(this, MainActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
                is Command.ShowToast -> {
                    ViswamApp.context.let {
                        Toast.makeText(it, command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ENABLE) {
            if (resultCode == RESULT_OK) {
                val userCustPin = data!!.getStringExtra("userPin")
                if (userCustPin != null) {
                    handleMPinService(userCustPin)
                }
            }
        } else if (requestCode == REQUEST_CODE_CHANGE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "PinCode Updated : ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleMPinService(userPin: String) {
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            if (isUpdatePwdStatus) {
                viewModel.checkMPinLogin(MPinRequest(userData.EMPID, userPin, "UPDATE"))
            } else {
                viewModel.checkMPinLogin(MPinRequest(userData.EMPID, userPin, "CREATE"))
            }
        } else {
            Toast.makeText(
                this,
                resources?.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}