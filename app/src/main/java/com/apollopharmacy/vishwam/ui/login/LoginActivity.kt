package com.apollopharmacy.vishwam.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.BuildConfig
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.LoginRequest
import com.apollopharmacy.vishwam.data.model.MPinRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.databinding.ActivityLoginBinding
import com.apollopharmacy.vishwam.dialog.AppUpdateDialog
import com.apollopharmacy.vishwam.ui.createpin.CreatePinActivity
import com.apollopharmacy.vishwam.ui.validatepin.ValidatePinActivity
import com.apollopharmacy.vishwam.util.*
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var downloadController: DownloadController
    private val PERMISSION_REQUEST_CODE = 1
    val TAG = "LoginActivity"
    private  var selectedcompany = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]



        loginBinding.nameEditText.setText(Preferences.getValidatedEmpId())
        loginBinding.nameEditText.isEnabled = false

        val isUpdatePwdStatus = intent.getBooleanExtra("isUpdatePwd", false)
        Utils.printMessage(TAG, "pwd status : " + isUpdatePwdStatus)

//        loginBinding.nameEditText.setText("APL00036")
//        loginBinding.passwordEditText.setText("Harit#12345")
//        signIn()

        loginBinding.loginButton.setOnClickListener {
            signIn()
        }

        loginViewModel.commands.observeForever({ command ->
            hideLoading()
            when (command) {
                is Command.NavigateTo -> {
                    handleGetPinService()
//                    val homeIntent = Intent(this, MainActivity::class.java)
//                    startActivity(homeIntent)
//                    finish()
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
                is Command.ShowToast -> {
                    context?.let { Toast.makeText(it, command.message, Toast.LENGTH_SHORT).show() }
                }
                is Command.MpinValidation -> {
                    if (command.value.Status) {
                        Preferences.setSiteIdListFetched(false)
                        val homeIntent = Intent(this, ValidatePinActivity::class.java)
                        startActivity(homeIntent)
                        finish()
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                    } else {
                        val homeIntent = Intent(this, CreatePinActivity::class.java)
                        homeIntent.putExtra("isUpdatePwd", isUpdatePwdStatus)
                        startActivity(homeIntent)
                        finish()
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                    }
                }
            }
        })


        loginBinding.entityEditText.setText(Preferences.getCompany())
        selectedcompany = Preferences.getCompany()
//        val companys = resources.getStringArray(R.array.company_list)
//        val mySpinner = findViewById<Spinner>(R.id.company_spinner)
//        val spinnerAdapter= object : ArrayAdapter<String>(this,R.layout.dropdown_item, companys) {
//
//            override fun isEnabled(position: Int): Boolean {
//                // Disable the first item from Spinner
//                // First item will be used for hint
//                return position != 0
//            }
//
//            override fun getDropDownView(
//                position: Int,
//                convertView: View?,
//                parent: ViewGroup
//            ): View {
//                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
//                //set the color of first item in the drop down list to gray
//                if(position == 0) {
//                    view.setTextColor(Color.GRAY)
//                } else {
//                    //here it is possible to define color for other items by
//                    //view.setTextColor(Color.RED)
//                }
//                return view
//            }
//
//        }
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        mySpinner.adapter = spinnerAdapter
//        val index = companys.indexOfFirst{
//            it == Preferences.getCompany()
//        }
//        mySpinner.setSelection(index)
//        mySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val value = parent!!.getItemAtPosition(position).toString()
//                if(value == companys[0]){
////                    (view as TextView).setTextColor(Color.GRAY)
//                }else{
//                    selectedcompany = companys[position]
//                }
//            }
//
//        }
        onCheckBuildDetails()
        loginViewModel.getRole(Preferences.getValidatedEmpId())
    }

    private fun signIn() {
        if (NetworkUtil.isNetworkConnected(this)) {
            showLoading(this)
            Utlis.hideKeyPad(this)
            loginViewModel.checkLogin(
                LoginRequest(
                    loginBinding.passwordEditText.text.toString().trim(),
                    loginBinding.nameEditText.text.toString().trim(),
                    selectedcompany
                )
            )
        }
        else {
            context?.let {
                Toast.makeText(
                    it,
                    ViswamApp.context.resources?.getString(R.string.label_network_error)
                        .toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
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
                            "Later",
                            buildDetailsEntity.DOWNLOADURL
                        )
                    }
                }
            } else {
                displayAppInfoDialog(
                    "Info",
                    buildDetailsEntity.AVABILITYMESSAGE,
                    "",
                    "",
                    ""
                )
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
        val dialogView = AppUpdateDialog(this@LoginActivity)
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
            dialogView.setNegativeListener(View.OnClickListener { dialogView.dismiss() })
        } else {
            dialogView.setDialogDismiss()
        }
        dialogView.show()
    }

    private fun startDownload() {
        if (checkPermission()) {
//            val apkUrl = "https://androidwave.com/source/apk/app-pagination-recyclerview.apk"
//            downloadController = DownloadController(this@LoginActivity, apkUrl)
            downloadController.enqueueDownload()
        } else {
            requestPermission() // Code for permission
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@LoginActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this@LoginActivity,
                "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@LoginActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                startDownload()
                Utils.printMessage("value", "Permission Granted, Now you can use local drive .")
            } else {
                Utils.printMessage("value", "Permission Denied, You cannot use local drive .")
            }
        }
    }

    private fun handleGetPinService() {
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            loginViewModel.checkMPinLogin(MPinRequest(Preferences.getToken(),
                "",
                "GETDETAILS"))
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