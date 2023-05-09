package com.github.omadahealth.lollipin.lib.managers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.omadahealth.lollipin.lib.PinActivity;
import com.github.omadahealth.lollipin.lib.R;
import com.github.omadahealth.lollipin.lib.enums.KeyboardButtonEnum;
import com.github.omadahealth.lollipin.lib.interfaces.KeyboardButtonClickedListener;
import com.github.omadahealth.lollipin.lib.views.KeyboardView;
import com.github.omadahealth.lollipin.lib.views.PinCodeRoundView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by stoyan and olivier on 1/13/15.
 * The activity that appears when the password needs to be set or has to be asked.
 * Call this activity in normal or singleTop mode (not singleTask or singleInstance, it does not work
 * with {@link android.app.Activity#startActivityForResult(android.content.Intent, int)}).
 */
public abstract class AppLockActivity extends PinActivity implements KeyboardButtonClickedListener, View.OnClickListener, FingerprintUiHelper.Callback {

    public static final String TAG = AppLockActivity.class.getSimpleName();
    public static final String ACTION_CANCEL = TAG + ".actionCancelled";
    private static final int DEFAULT_PIN_LENGTH = 4;

    protected TextView mStepTextView;
    protected TextView mForgotTextView;
    protected PinCodeRoundView mPinCodeRoundView;
    protected KeyboardView mKeyboardView;
    protected ImageView mFingerprintImageView;
    protected TextView mFingerprintTextView;

    protected LockManager mLockManager;


    protected FingerprintManager mFingerprintManager;
    protected FingerprintUiHelper mFingerprintUiHelper;

    protected int mType = AppLock.UNLOCK_PIN;
    protected String mValidPin = "";
    protected String mInputPin = "";
    protected int mAttempts = 1;
    protected String mPinCode;
    private Boolean isAppAvailability = false;
    private String availabilityMsg = "";
    private Boolean isForceDownload = false;
    private String buildMessage = "";
    private String downloadUrl = "";
    private int serviceAppVer = 0;
    private int currentAppVer = 0;

    protected String mOldPinCode;

    private boolean isCodeSuccessful = false;

    /**
     * First creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());
        initLayout(getIntent());
    }

    /**
     * If called in singleTop mode
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initLayout(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Init layout for Fingerprint
        //initLayoutForFingerprint();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFingerprintUiHelper != null) {
            mFingerprintUiHelper.stopListening();
        }
    }

    /**
     * Init completely the layout, depending of the extra {@link com.github.omadahealth.lollipin.lib.managers.AppLock#EXTRA_TYPE}
     */
    private void initLayout(Intent intent) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            //Animate if greater than 2.3.3
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        }

        Bundle extras = intent.getExtras();
        if (extras != null) {
            mValidPin = extras.getString("validatePin", "");
            isAppAvailability = extras.getBoolean("isAppAvailability", false);
            availabilityMsg = extras.getString("availabilityMsg", "");
            isForceDownload = extras.getBoolean("isForceDownload", false);
            buildMessage = extras.getString("buildMessage", "");
            downloadUrl = extras.getString("downloadUrl", "");
            serviceAppVer = extras.getInt("serviceAppVer", 0);
            currentAppVer = extras.getInt("currentAppVer", 0);
            mType = extras.getInt(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        }
        Log.e("AppLock", "Type : " + mType);
        if (isAppAvailability) {
//            onCheckBuildDetails();
        }
//        else {
//            mType = AppLock.ENABLE_PINLOCK;
//        }

        mLockManager = LockManager.getInstance();
        mPinCode = "";
        mOldPinCode = "";

        enableAppLockerIfDoesNotExist();
        mLockManager.getAppLock().setPinChallengeCancelled(false);

        mStepTextView = (TextView) this.findViewById(R.id.pin_code_step_textview);
        mPinCodeRoundView = (PinCodeRoundView) this.findViewById(R.id.pin_code_round_view);
        mPinCodeRoundView.setPinLength(this.getPinLength());
        mForgotTextView = (TextView) this.findViewById(R.id.pin_code_forgot_textview);
        mForgotTextView.setOnClickListener(this);
        mKeyboardView = (KeyboardView) this.findViewById(R.id.pin_code_keyboard_view);
        mKeyboardView.setKeyboardButtonClickedListener(this);

//        ImageView logoImage = ((ImageView) findViewById(R.id.pin_code_logo_imageview));
//        logoImage.setVisibility(View.VISIBLE);
//        logoImage.setImageResource(R.drawable.logo_vishwam);

//        int logoId = mLockManager.getAppLock().getLogoId();
//        ImageView logoImage = ((ImageView) findViewById(R.id.pin_code_logo_imageview));
//        if (logoId != AppLock.LOGO_ID_NONE) {
//            logoImage.setVisibility(View.VISIBLE);
//            logoImage.setImageResource(logoId);
//        }
        mForgotTextView.setText(getForgotText());
        setForgotTextVisibility();
//        mForgotTextView.setVisibility(View.GONE);

        setStepText();
    }

    /**
     * Init {@link FingerprintManager} of the {@link android.os.Build.VERSION#SDK_INT} is > to Marshmallow
     * and {@link FingerprintManager#isHardwareDetected()}.
     */
    private void initLayoutForFingerprint() {
        mFingerprintImageView = (ImageView) this.findViewById(R.id.pin_code_fingerprint_imageview);
        mFingerprintTextView = (TextView) this.findViewById(R.id.pin_code_fingerprint_textview);
        if (mType == AppLock.UNLOCK_PIN && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            mFingerprintUiHelper = new FingerprintUiHelper.FingerprintUiHelperBuilder(mFingerprintManager).build(mFingerprintImageView, mFingerprintTextView, this);
            try {
                if (mFingerprintManager.isHardwareDetected() && mFingerprintUiHelper.isFingerprintAuthAvailable()
                        && mLockManager.getAppLock().isFingerprintAuthEnabled()) {
                    mFingerprintImageView.setVisibility(View.VISIBLE);
                    mFingerprintTextView.setVisibility(View.VISIBLE);
                    mFingerprintUiHelper.startListening();
                } else {
                    mFingerprintImageView.setVisibility(View.GONE);
                    mFingerprintTextView.setVisibility(View.GONE);
                }
            } catch (SecurityException e) {
                Log.e(TAG, e.toString());
                mFingerprintImageView.setVisibility(View.GONE);
                mFingerprintTextView.setVisibility(View.GONE);
            }
        } else {
            mFingerprintImageView.setVisibility(View.GONE);
            mFingerprintTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Re enable {@link AppLock} if it has been collected to avoid
     * {@link NullPointerException}.
     */
    @SuppressWarnings("unchecked")
    private void enableAppLockerIfDoesNotExist() {
        try {
            if (mLockManager.getAppLock() == null) {
                mLockManager.enableAppLock(this, getCustomAppLockActivityClass());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Init the {@link #mStepTextView} based on {@link #mType}
     */
    private void setStepText() {
        mStepTextView.setText(getStepText(mType));
        if (getStepText(mType).startsWith("Confirm your")) {
            mStepTextView.setTextColor(getResources().getColor(R.color.dark_yellow));
        }
    }

    /**
     * Gets the {@link String} to be used in the {@link #mStepTextView} based on {@link #mType}
     *
     * @param reason The {@link #mType} to return a {@link String} for
     * @return The {@link String} for the {@link AppLockActivity}
     */
    public String getStepText(int reason) {
        String msg = null;
        switch (reason) {
            case AppLock.DISABLE_PINLOCK:
                msg = getString(R.string.pin_code_step_disable, this.getPinLength());
                break;
            case AppLock.ENABLE_PINLOCK:
                msg = getString(R.string.pin_code_step_create, this.getPinLength());
                break;
            case AppLock.CHANGE_PIN:
                msg = getString(R.string.pin_code_step_change, this.getPinLength());
                break;
            case AppLock.UNLOCK_PIN:
                msg = getString(R.string.pin_code_step_unlock, this.getPinLength());
                break;
            case AppLock.CONFIRM_PIN:
                msg = getString(R.string.pin_code_step_enable_confirm, this.getPinLength());
                break;
        }
        return msg;
    }

    public String getForgotText() {
        return getString(R.string.pin_code_forgot_text);
    }

    private void setForgotTextVisibility() {
        mForgotTextView.setVisibility(mLockManager.getAppLock().shouldShowForgot(mType) ? View.VISIBLE : View.GONE);
    }

    /**
     * Overrides to allow a slide_down animation when finishing
     */
    @Override
    public void finish() {
        super.finish();

        //If code successful, reset the timer
        if (isCodeSuccessful) {
            if (mLockManager != null) {
                AppLock appLock = mLockManager.getAppLock();
                if (appLock != null) {
                    appLock.setLastActiveMillis();
                }
            }
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            //Animate if greater than 2.3.3
            overridePendingTransition(R.anim.nothing, R.anim.slide_down);
        }
    }

    /**
     * Add the button clicked to {@link #mPinCode} each time.
     * Refreshes also the {@link com.github.omadahealth.lollipin.lib.views.PinCodeRoundView}
     */
    @Override
    public void onKeyboardClick(KeyboardButtonEnum keyboardButtonEnum) {
        if (mPinCode.length() < this.getPinLength()) {
            int value = keyboardButtonEnum.getButtonValue();

            if (value == KeyboardButtonEnum.BUTTON_CLEAR.getButtonValue()) {
                if (!mPinCode.isEmpty()) {
                    setPinCode(mPinCode.substring(0, mPinCode.length() - 1));
                } else {
                    setPinCode("");
                }
            } else {
                setPinCode(mPinCode + value);
            }
        }
    }

    /**
     * Called at the end of the animation of the {@link com.andexert.library.RippleView}
     * Calls {@link #onPinCodeInputed} when {@link #mPinCode}
     */
    @Override
    public void onRippleAnimationEnd() {
        if (mPinCode.length() == this.getPinLength()) {
            onPinCodeInputed();
        }
    }

    /**
     * Switch over the {@link #mType} to determine if the password is ok, if we should pass to the next step etc...
     */
    protected void onPinCodeInputed() {
        switch (mType) {
            case AppLock.DISABLE_PINLOCK:
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    setResult(RESULT_OK);
                    mLockManager.getAppLock().setPasscode(null);
                    onPinCodeSuccess();
                    finish();
                } else {
                    onPinCodeError();
                }
                break;
            case AppLock.ENABLE_PINLOCK:
                mOldPinCode = mPinCode;
                setPinCode("");
                mType = AppLock.CONFIRM_PIN;
                setStepText();
                setForgotTextVisibility();
                break;
            case AppLock.CONFIRM_PIN:
                if (mPinCode.equals(mOldPinCode)) {
                    Intent intent = new Intent();
                    intent.putExtra("userPin", mPinCode);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    mOldPinCode = "";
                    setPinCode("");
                    mType = AppLock.ENABLE_PINLOCK;
                    setStepText();
                    setForgotTextVisibility();
                    onPinCodeError();
                }
                break;
            case AppLock.CHANGE_PIN:
                mOldPinCode = mPinCode;
                setPinCode("");
                mType = AppLock.CONFIRM_PIN;
                setStepText();
                setForgotTextVisibility();
                break;
            case AppLock.UNLOCK_PIN:
                if (!mValidPin.isEmpty()) {
                    mLockManager.getAppLock().setPasscode(mValidPin);
                }
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    Intent intent = new Intent();
                    intent.putExtra("isVerified", true);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    mLockManager.getAppLock().setPasscode(null);
                    onPinCodeError();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Override {@link #onBackPressed()} to prevent user for finishing the activity
     */
    @Override
    public void onBackPressed() {
        if (getBackableTypes().contains(mType)) {
            if (AppLock.UNLOCK_PIN == getType()) {
                mLockManager.getAppLock().setPinChallengeCancelled(true);
                LocalBroadcastManager
                        .getInstance(this)
                        .sendBroadcast(new Intent().setAction(ACTION_CANCEL));
            }
            super.onBackPressed();
        }
    }

    @Override
    public void onAuthenticated() {
        Log.e(TAG, "Fingerprint READ!!!");
        setResult(RESULT_OK);
        onPinCodeSuccess();
        finish();
    }

    @Override
    public void onError() {
        Log.e(TAG, "Fingerprint READ ERROR!!!");
    }

    /**
     * Gets the list of {@link AppLock} types that are acceptable to be backed out of using
     * the device's back button
     *
     * @return an {@link List<Integer>} of {@link AppLock} types which are backable
     */
    public List<Integer> getBackableTypes() {
        return Arrays.asList(AppLock.CHANGE_PIN, AppLock.ENABLE_PINLOCK);
    }

    /**
     * Displays the information dialog when the user clicks the
     * {@link #mForgotTextView}
     */
    public abstract void showForgotDialog();

    /**
     * Run a shake animation when the password is not valid.
     */
    protected void onPinCodeError() {
        onPinFailure(mAttempts++);
        Thread thread = new Thread() {
            public void run() {
                mPinCode = "";
                mPinCodeRoundView.refresh(mPinCode.length());
                Animation animation = AnimationUtils.loadAnimation(
                        AppLockActivity.this, R.anim.shake);
                mKeyboardView.startAnimation(animation);
            }
        };
        runOnUiThread(thread);
    }

    protected void onPinCodeSuccess() {
        isCodeSuccessful = true;
        onPinSuccess(mAttempts);
        mAttempts = 1;
    }

    /**
     * Set the pincode and refreshes the {@link com.github.omadahealth.lollipin.lib.views.PinCodeRoundView}
     */
    public void setPinCode(String pinCode) {
        mPinCode = pinCode;
        mPinCodeRoundView.refresh(mPinCode.length());
    }


    /**
     * Returns the type of this {@link com.github.omadahealth.lollipin.lib.managers.AppLockActivity}
     */
    public int getType() {
        return mType;
    }

    /**
     * When we click on the {@link #mForgotTextView} handle the pop-up
     * dialog
     *
     * @param view {@link #mForgotTextView}
     */
    @Override
    public void onClick(View view) {
        showForgotDialog();

//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.custom_forgot_pin);
//
//        ImageView closeImg = (ImageView) dialog.findViewById(R.id.close_dialog);
//        Button dialogNoButton = (Button) dialog.findViewById(R.id.noBtn);
//        Button dialogYesButton = (Button) dialog.findViewById(R.id.yesBtn);
//
//        closeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialogNoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialogYesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                Intent intent = new Intent();
//                intent.putExtra("showDialog", true);
//                setResult(RESULT_OK, intent);
//                finish();
////            Intent homeIntent = new Intent(this, CreatePinActivity.class);
////            startActivity(homeIntent);
////            finishAffinity();
////            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//            }
//        });
//        dialog.show();
    }

    /**
     * When the user has failed a pin challenge
     *
     * @param attempts the number of attempts the user has used
     */
    public abstract void onPinFailure(int attempts);

    /**
     * When the user has succeeded at a pin challenge
     *
     * @param attempts the number of attempts the user had used
     */
    public abstract void onPinSuccess(int attempts);

    /**
     * Gets the resource id to the {@link View} to be set with {@link #setContentView(int)}.
     * The custom layout must include the following:
     * - {@link TextView} with an id of pin_code_step_textview
     * - {@link TextView} with an id of pin_code_forgot_textview
     * - {@link PinCodeRoundView} with an id of pin_code_round_view
     * - {@link KeyboardView} with an id of pin_code_keyboard_view
     *
     * @return the resource id to the {@link View}
     */
    public int getContentView() {
        return R.layout.activity_pin_code;
    }

    /**
     * Gets the number of digits in the pin code.  Subclasses can override this to change the
     * length of the pin.
     *
     * @return the number of digits in the PIN
     */
    public int getPinLength() {
        return AppLockActivity.DEFAULT_PIN_LENGTH;
    }

    /**
     * Get the current class extending {@link AppLockActivity} to re-enable {@link AppLock}
     * in case it has been collected
     *
     * @return the current class extending {@link AppLockActivity}
     */
    public Class<? extends AppLockActivity> getCustomAppLockActivityClass() {
        return this.getClass();
    }

    private void onCheckBuildDetails() {
        if (isAppAvailability) {
            if (serviceAppVer > currentAppVer) {
                if (isForceDownload) {
                    displayAppInfoDialog(
                            "Update Available",
                            buildMessage,
                            "Update",
                            "",
                            downloadUrl
                    );
                } else {
                    displayAppInfoDialog(
                            "Update Available",
                            buildMessage,
                            "Update Now",
                            "Later",
                            downloadUrl
                    );
                }
            }
        } else {
            displayAppInfoDialog(
                    "Info",
                    availabilityMsg,
                    "",
                    "",
                    ""
            );
        }
    }

    private void displayAppInfoDialog(String title, String subTitle, String positiveBtn, String negativeBtn, final String downloadUrl) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.app_update_dialog);
        TextView mDialogTitle = dialog.findViewById(R.id.title);
        TextView mDialogSubTitle = dialog.findViewById(R.id.subtitle);
        TextView mDialogBtnOk = dialog.findViewById(R.id.dialogButtonOK);
        TextView mDialogBtnNo = dialog.findViewById(R.id.dialogButtonNO);
        View mDialogSeparator = dialog.findViewById(R.id.separator);

        mDialogTitle.setText(title);
        mDialogSubTitle.setText(subTitle);
        if (!TextUtils.isEmpty(positiveBtn)) {
            mDialogBtnOk.setText(positiveBtn);
            mDialogBtnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl)));
                }
            });
        }
        if (!TextUtils.isEmpty(negativeBtn)) {
            mDialogBtnNo.setVisibility(View.VISIBLE);
            mDialogSeparator.setVisibility(View.VISIBLE);
            mDialogBtnNo.setText(negativeBtn);
            mDialogBtnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            mDialogBtnNo.setVisibility(View.GONE);
            mDialogSeparator.setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                    }
                    return true;
                }
            });
        }
        dialog.show();
    }
}
