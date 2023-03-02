package com.apollopharmacy.vishwam.ui.rider.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;


import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.ActivitySplashBinding;
import com.apollopharmacy.vishwam.databinding.ActivitySplashRiderBinding;
import com.apollopharmacy.vishwam.ui.rider.login.LoginActivity;
import com.apollopharmacy.vishwam.ui.verifyuser.VerifyUserActivity;
import com.novoda.merlin.Merlin;

import butterknife.ButterKnife;

public class SplashScreen extends BaseActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ActivitySplashRiderBinding splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_rider);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation slideLefttoRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        slideLefttoRight.setDuration(1700);
        splashBinding.riderIcon.setVisibility(View.VISIBLE);
        splashBinding.riderIcon.startAnimation(slideLefttoRight);

        Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        splashBinding.imageAppLogo.startAnimation(animZoomOut);

        new Handler().postDelayed(() -> {
            if (getSessionManager().getLoginToken() != null && !getSessionManager().getLoginToken().isEmpty()) {
                if (getIntent() != null) {
                    Intent mainIntent = new Intent(SplashScreen.this, VerifyUserActivity.class);
                    startActivity(mainIntent);

                    String notificationType = getIntent().getStringExtra("notification_type");
                    if (notificationType != null && notificationType.equals("ORDER_ASSIGNED")) {
//                        startActivity(OrderDeliveryActivity.getStartIntent(SplashScreen.this, getIntent().getStringExtra("uid"), true));
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else if (notificationType != null && notificationType.equals("COMPLAINT_RESOLVED")) {
//                        Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
//                        mainIntent.putExtra("COMPLAINT_RESOLVED", true);
//                        startActivity(mainIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else {
//                        Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
//                        startActivity(mainIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                } else {
//                    Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
//                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            } else {
                Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }
}
