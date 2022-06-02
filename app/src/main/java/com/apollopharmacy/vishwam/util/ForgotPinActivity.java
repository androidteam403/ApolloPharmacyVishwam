package com.apollopharmacy.vishwam.util;

import android.app.Dialog;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.ui.createpin.CreatePinActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;

public class ForgotPinActivity extends AppLockActivity {

    @Override
    public void showForgotDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_forgot_pin);

        ImageView closeImg = (ImageView) dialog.findViewById(R.id.close_dialog);
        Button dialogNoButton = (Button) dialog.findViewById(R.id.noBtn);
        Button dialogYesButton = (Button) dialog.findViewById(R.id.yesBtn);

        closeImg.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialogNoButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialogYesButton.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("showDialog", true);
            setResult(RESULT_OK, intent);
            finish();
        });
        dialog.show();
    }

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {

    }

    @Override
    public int getPinLength() {
        return super.getPinLength();
    }
}
