package com.apollopharmacy.vishwam.ui.home;

public interface MainActivityCallback {
    void onClickFilterIcon();
    void onClickSiteIdIcon();
    void onClickQcFilterIcon();

    void onSelectApprovedFragment(String listSize);

    void onSelectRejectedFragment();

    void onSelectPendingFragment();
    void onClickSpinnerLayout();
}
