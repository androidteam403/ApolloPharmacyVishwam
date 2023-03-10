package com.apollopharmacy.vishwam.ui.rider.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.ActivityLoginRiderBinding;
import com.apollopharmacy.vishwam.ui.home.MainActivity;
import com.apollopharmacy.vishwam.ui.rider.activity.BaseActivity;
import com.apollopharmacy.vishwam.ui.rider.dialog.ForgotPwdDialog;
import com.apollopharmacy.vishwam.ui.rider.login.model.LoginResponse;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollopharmacy.vishwam.ui.rider.profile.model.GetRiderProfileResponse;
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.novoda.merlin.Merlin;

import java.util.Objects;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements LoginActivityCallback {

    private ActivityLoginRiderBinding loginBinding;
    private String firebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_rider);
        ButterKnife.bind(this);
        setUp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        loginBinding.setCallback(this);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                this.firebaseToken = token;
                Log.d("newToken", "retrieve token successful : " + token);
            } else {
                Log.w("newToken", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task -> Log.v("newToken", "This is the token : " + task.getResult()));

        loginBinding.loginParentLayout.setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    private Boolean validate() {
        if (Objects.requireNonNull(loginBinding.etUserEmail.getText()).toString().trim().isEmpty()) {
            loginBinding.etUserEmail.setError("User name should not be empty");
            loginBinding.etUserEmail.requestFocus();
            return false;
        } else if (Objects.requireNonNull(loginBinding.etUserPassword.getText()).toString().trim().isEmpty()) {
            loginBinding.etUserPassword.setError("Password should not be empty");
            loginBinding.etUserPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onFialureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessLoginApi(LoginResponse loginResponse) {
        if (loginResponse != null && loginResponse.getData() != null && loginResponse.getSuccess() && loginResponse.getData().getToken() != null) {
            try {

                getSessionManager().setLoginToken(loginResponse.getData().getToken());
                getSessionManager().setRiderIconUrl(loginResponse.getData().getPic().get(0).getDimenesions().get200200FullPath());
                new LoginActivityController(this, this).getRiderProfileDetailsApi(getSessionManager().getLoginToken());
            } catch (Exception e) {
                System.out.println("onSuccessLoginApi ::::::::::::::::::::::::" + e.getMessage());
                ActivityUtils.hideDialog();
                Toast.makeText(this, "Please try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailureLoginApi(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse) {
        if (getRiderProfileResponse != null) {
            getSessionManager().setRiderProfileDetails(getRiderProfileResponse);
            new LoginActivityController(this, this).deliveryFailureReasonApiCall();
            new LoginActivityController(this, this).getComplaintReasonsListApiCall();
        }
    }

    @Override
    public void onSuccessDeliveryReasonApiCall(DeliveryFailreReasonsResponse deliveryFailreReasonsResponse) {
        if (deliveryFailreReasonsResponse != null) {
            getSessionManager().setDeliveryFailureReasonsList(deliveryFailreReasonsResponse);
//            MainActivity.mInstance.displaySelectedScreen("Dashboard");

            Intent i = new Intent(com.apollopharmacy.vishwam.ui.rider.login.LoginActivity.this, MainActivity.class);
            boolean True = true;
            i.putExtra("tag", true);
            startActivity(i);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        }
    }

    @Override
    public void onFailureGetProfileDetailsApi(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickSignin() {
        if (validate()) {
            new LoginActivityController(this, this).loginApiCall(
                    Objects.requireNonNull(loginBinding.etUserEmail.getText()).toString().trim(),
                    Objects.requireNonNull(loginBinding.etUserPassword.getText()).toString().trim(), firebaseToken);
        }
    }

    @Override
    public void onClickForgotPasswordText() {
        ForgotPwdDialog forgotPwdDialog = new ForgotPwdDialog(LoginActivity.this);
        forgotPwdDialog.setCancelable(false);
        forgotPwdDialog.show();
        Window window = forgotPwdDialog.getWindow();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (window != null) {
            window.setLayout(width, height);
        }
    }
}
