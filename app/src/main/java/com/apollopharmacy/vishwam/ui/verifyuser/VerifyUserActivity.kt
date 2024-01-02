package com.apollopharmacy.vishwam.ui.verifyuser

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.Preferences.getIsUserValidated
import com.apollopharmacy.vishwam.data.model.ValidateRequest
import com.apollopharmacy.vishwam.databinding.ActivityVerifyUserBinding
import com.apollopharmacy.vishwam.ui.otpview.OtpViewActivity
import com.apollopharmacy.vishwam.ui.splash.SplashActivity
import com.apollopharmacy.vishwam.util.EncryptionManager
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils


class VerifyUserActivity : AppCompatActivity() {
    lateinit var verifyUserViewModel: VerifyUserViewModel
    lateinit var verifyUserBinding: ActivityVerifyUserBinding
    val TAG = "VerifyUserActivity"
    private val permission = 101
    private var selectedcompany = ""


    var module = ""
    var uniqueId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_user)
        verifyUserViewModel = ViewModelProvider(this)[VerifyUserViewModel::class.java]
        if (intent != null) {
            if (intent.getStringExtra("MODULE") != null)
                module = intent.getStringExtra("MODULE") as String
            if (intent.getStringExtra("UNIQUE_ID") != null)
                uniqueId = intent.getStringExtra("UNIQUE_ID") as String
        }
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

        val companys = resources.getStringArray(R.array.company_list)
        val mySpinner = findViewById<Spinner>(R.id.company_spinner)
        val spinnerAdapter = object : ArrayAdapter<String>(this, R.layout.dropdown_item, companys) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup,
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    //here it is possible to define color for other items by
                    //view.setTextColor(Color.RED)
                }
                return view
            }

        }
        verifyUserBinding.employeeId.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = spinnerAdapter

        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == companys[0]) {
//                    (view as TextView).setTextColor(Color.GRAY)
                } else {
                    selectedcompany = companys[position]
                }
            }

        }
        if (NetworkUtil.isNetworkConnected(this)) {
            verifyUserViewModel.getSplashScreenData(
                ValidateRequest(Config.DEVICE_ID, Config.KEY),
                this@VerifyUserActivity
            )
        } else {
            Toast.makeText(
                this,
                resources?.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun initVerification() {
        verifyUserBinding.employeeId.onEditorAction(EditorInfo.IME_ACTION_DONE)
        if (verifyUserBinding.employeeId.text?.trim().toString().isEmpty()) {
            Toast.makeText(this, "Employee ID should not be empty", Toast.LENGTH_SHORT).show()
            return
        } else if (selectedcompany.isEmpty()) {
            Toast.makeText(this, "Select company", Toast.LENGTH_SHORT).show()
            return
        } else {
            handleNextIntent()
        }
    }

    private fun handleNextIntent() {
        Preferences.setCompany(selectedcompany)
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
        intent.putExtra("MODULE", module)
        intent.putExtra("UNIQUE_ID", uniqueId)
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