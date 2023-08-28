package com.apollopharmacy.vishwam.util.signaturepad;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apollopharmacy.vishwam.R;

public class DialogManager {

    public static Dialog mDialog;
    private static Button mDialogBtn, mDialogCancelBtn;
    private static TextView mDialogAlertTxt;
    private static TextView mDialogTitleTxt;

    public static void showToast(Context context, String message) {
        Toast mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        TextView mToastTxt = mToast.getView().findViewById(
                android.R.id.message);
        mToast.show();
    }




    public static Dialog getDialog(Context mContext, int mLayout) {
        Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mLayout);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);

        mDialog.setCancelable(true);

        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }

    private static String getMessage(String mMessage) {
        if(!isValidStr(mMessage))
            return "";
        return (mMessage.trim().charAt(mMessage.trim().length() - 1) == '.' || mMessage.charAt(mMessage.length() - 1) == '?') ? mMessage : mMessage + ".";
    }

    public static boolean isValidStr(String string) {
        return string != null && !string.trim().isEmpty();
    }

}
