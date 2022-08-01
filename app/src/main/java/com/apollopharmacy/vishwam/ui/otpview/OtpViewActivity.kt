package com.apollopharmacy.vishwam.ui.otpview

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.ValidateOtpRequest
import com.apollopharmacy.vishwam.databinding.ActivityOtpViewBinding
import com.apollopharmacy.vishwam.ui.splash.SplashActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit


class OtpViewActivity : AppCompatActivity() {
    lateinit var otpViewModel: OtpViewModel
    lateinit var otpViewBinding: ActivityOtpViewBinding
    var enteredOtpNumber: String = ""
    val TAG = "OtpViewActivity"
    private var enteredOtp: String? = null
    private var receivedOtp: String? = null
    private var empVal = ""
    private var FCM_KEY: String = "Unknown"
    private var isTimerStarted: Boolean = false
    private var serviceEmpID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_view)
        otpViewModel = ViewModelProvider(this)[OtpViewModel::class.java]
        if (intent.extras != null) {
            empVal = intent.getStringExtra("EmpID").toString()
            otpViewBinding.employeeId.append(
                " (" + Utils.getMaskedEmpID(empVal) + ")" + resources.getString(
                    R.string.please_check_employee_id
                )
            )
        }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(object : OnCompleteListener<String> {
                override fun onComplete(task: Task<String>) {
                    if (!task.isSuccessful) {
                        FCM_KEY = "Unknown"
                        Utils.printMessage(
                            TAG,
                            "Fetching FCM registration token failed" + task.exception
                        )
                        return
                    }

                    // Get new FCM registration token
                    val token = task.result
                    // Log and toast
                    Utils.printMessage(TAG, "FCM Key : " + token)
                    FCM_KEY = token
                }
            })

        otpViewModel.validateOtpModel.observeForever {
            Utlis.hideLoading()
            if (it.STATUS) {
                otpViewBinding.customerMobileNum.visibility = View.VISIBLE
                otpViewBinding.customerMobileNum.setText("")
                otpViewBinding.customerMobileNum.setText(resources.getText(R.string.please_type_the_verification_code_sent_to_))
                otpViewBinding.customerMobileNum.append(" " + Utils.getMaskedNumber(it.MOBILENO))
                otpViewBinding.linearEdtOtp.visibility = View.VISIBLE
                otpViewBinding.otpView.visibility = View.VISIBLE
                otpViewBinding.fabNext.visibility = View.VISIBLE
                countDownTimer.start()
                receivedOtp = it.OTP
            } else {
                otpViewBinding.updateButton.isClickable = true
                otpViewBinding.confirmButton.isClickable = true
                otpViewBinding.employeeIdET.isEnabled = true
                otpViewBinding.proceedButton.isClickable = true
                Toast.makeText(this, it.MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }

        otpViewModel.deviceDeRegModel.observeForever {
            Utlis.hideLoading()
            handleOtpService(serviceEmpID)
        }

        otpViewModel.commands.observeForever { command ->
            Utlis.hideLoading()
            when (command) {
                is Command.ShowToast -> {
                    otpViewBinding.updateButton.isClickable = true
                    otpViewBinding.confirmButton.isClickable = true
                    otpViewBinding.employeeIdET.isEnabled = true
                    otpViewBinding.proceedButton.isClickable = true
                    ViswamApp.context?.let {
                        if (command.message.startsWith("you are already login in other device")) {
                            showBottomSheetDialog(command.message)
                        } else {
                            Toast.makeText(it, command.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        otpViewBinding.updateButton.setOnClickListener {
            otpViewBinding.updateButton.setBackgroundDrawable(resources.getDrawable(R.drawable.yellow_drawable))
            otpViewBinding.confirmButton.setBackgroundDrawable(resources.getDrawable(R.drawable.grey_rectangle1))
            otpViewBinding.updateButton.isClickable = false
            otpViewBinding.confirmButton.isClickable = false
            otpViewBinding.updateIdLayout.visibility = View.VISIBLE
            otpViewBinding.employeeIdET.setText(empVal)
            otpViewBinding.employeeIdET.setSelection(otpViewBinding.employeeIdET.text!!.length)
            otpViewBinding.employeeIdET.requestFocus()
        }

        otpViewBinding.confirmButton.setOnClickListener {
            isTimerStarted = true;
            otpViewBinding.confirmButton.setBackgroundDrawable(resources.getDrawable(R.drawable.yellow_drawable))
            otpViewBinding.updateButton.isClickable = false
            otpViewBinding.confirmButton.isClickable = false
            handleOtpConfirmView(empVal)
        }

        otpViewBinding.proceedButton.setOnClickListener {
            isTimerStarted = true;
            otpViewBinding.employeeIdET.postDelayed(
                { otpViewBinding.employeeIdET.hideKeyboard() },
                50
            )
            otpViewBinding.employeeIdET.isEnabled = false
            otpViewBinding.proceedButton.setBackgroundDrawable(resources.getDrawable(R.drawable.yellow_drawable))
            otpViewBinding.proceedButton.isClickable = false
            handleOtpConfirmView(
                otpViewBinding.employeeIdET.text.toString().trim()
                    .replace(" ", "")
            )
        }

        otpViewBinding.fabPrevious.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        }

        otpViewBinding.fabNext.setOnClickListener {
//            handleOtpService()
            handleNextIntent()
        }

        otpViewBinding.resendOtpLayout.setOnClickListener {
            otpViewBinding.otpView.setOTP("")
            countDownTimer.onFinish()
            countDownTimer.cancel()
            countDownTimer.start()
            otpViewBinding.resendOtpLayout.visibility = View.GONE
            otpViewBinding.resendOtpLayout.setBackgroundDrawable(resources.getDrawable(R.drawable.grey_rectangle1))
            val userID: String
            if (otpViewBinding.employeeIdET.text.toString().isEmpty()) {
                userID = empVal
            } else {
                userID = otpViewBinding.employeeIdET.text.toString().trim().replace(" ", "")
            }
            serviceEmpID = userID
            handleOtpService(userID)
        }
    }

    private var countDownTimer: CountDownTimer = object : CountDownTimer(180000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            otpViewBinding.timer.setText(millisecondsToTime(millisUntilFinished))
        }

        override fun onFinish() {
            otpViewBinding.timer.setText("")
            otpViewBinding.resendOtpLayout.isClickable = true
            otpViewBinding.resendOtpLayout.setBackgroundDrawable(resources.getDrawable(R.drawable.yellow_rectangle))
            otpViewBinding.resendOtpLayout.visibility = View.VISIBLE
        }
    }
    var handler = Handler()
    var runnable = Runnable {
        if (enteredOtpNumber == receivedOtp) {
            otpViewBinding.otpView.showSuccess()
            countDownTimer.cancel()
        } else {
            otpViewBinding.otpView.showError()
        }
    }

    private fun millisecondsToTime(milliseconds: Long): String? {
        return "Time remaining " + String.format(
            "%02d : %02d",
            TimeUnit.MILLISECONDS.toMinutes(milliseconds),
            TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        )
    }

    override fun onStop() {
        super.onStop()
//        if (countDownTimer != null) {
//            countDownTimer.onFinish()
//            countDownTimer.cancel()
//        }
    }

    override fun onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.onFinish()
            countDownTimer.cancel()
        }
        super.onDestroy()
    }

    override fun onResume() {
        Utils.printMessage(TAG, "isTimerStarted : " + isTimerStarted)
//        if (isTimerStarted) {
//            otpViewBinding.resendOtpLayout.visibility = View.VISIBLE
//        } else {
//            otpViewBinding.resendOtpLayout.visibility = View.GONE
//        }
        super.onResume()
    }

    private fun handleOtpConfirmView(empID: String) {
        serviceEmpID = empID
        handleOtpService(empID)
    }

    private fun handleNextIntent() {
        enteredOtp = otpViewBinding.otpView.otp
        if(enteredOtp == receivedOtp && otpViewBinding.resendOtpLayout.visibility == View.VISIBLE){
            Toast.makeText(this, "Entered OTP has Expired", Toast.LENGTH_SHORT).show()
            return
        }
         //   otpViewBinding.txtOtp1.text.toString() + "" + otpViewBinding.txtOtp2.text.toString() + "" + otpViewBinding.txtOtp3.text.toString() + "" + otpViewBinding.txtOtp4.text.toString()
        if (enteredOtp == receivedOtp) {
            countDownTimer.cancel()
            otpViewBinding.timer.setText("")
            Preferences.setIsUserValidated(true)
            Preferences.setFCMKeyUpdated(true)
            Preferences.setValidatedEmpId(serviceEmpID)
            Handler().postDelayed(Runnable {
                intent = Intent(this@OtpViewActivity, SplashActivity::class.java)
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }, 500)
        } else {
            Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    fun EditText.hideKeyboard(
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

    private fun handleOtpService(empID: String) {
        Utils.printMessage(TAG, "Entered EMP : " + empID.toUpperCase())
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            val versionNum = Build.VERSION.RELEASE
            val fields: Array<Field> = Build.VERSION_CODES::class.java.fields
            var codeName = "UNKNOWN"
            for (field in fields) {
                try {
                    if (field.getInt(Build.VERSION_CODES::class.java) == Build.VERSION.SDK_INT) {
                        codeName = field.name
                        Utils.printMessage("Android OsName:", codeName)
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            otpViewModel.checkEmpAvailability(
                ValidateOtpRequest(
                    empID.toUpperCase(),
                    Utils.getDeviceId(this),
                    FCM_KEY,
                    codeName,
                    versionNum.toString(),
                    Build.MANUFACTURER,
                    Build.MODEL, Preferences.getCompany()
                )
            )
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onBackPressed() {
        //Disable Back
    }

    private fun showBottomSheetDialog(msg: String) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

        val deviceHeader = view.findViewById<TextView>(R.id.deviceAlertHeader)
        deviceHeader.setText(msg)
        val btnNo = view.findViewById<Button>(R.id.noBtn)
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        val btnYes = view.findViewById<Button>(R.id.yesBtn)
        btnYes.setOnClickListener {
            dialog.dismiss()
            handleDeviceDeRegService()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun handleDeviceDeRegService() {
        Utils.printMessage(TAG, "DeReg EMP : " + serviceEmpID)
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            otpViewModel.deRegisterDevice(serviceEmpID)
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}