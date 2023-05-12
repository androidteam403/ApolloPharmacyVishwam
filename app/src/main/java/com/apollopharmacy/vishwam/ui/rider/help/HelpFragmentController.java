package com.apollopharmacy.vishwam.ui.rider.help;

import android.content.Context;

public class HelpFragmentController {
    private Context context;
    private HelpFragmentCallback mListener;

    public HelpFragmentController(Context context, HelpFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

}
