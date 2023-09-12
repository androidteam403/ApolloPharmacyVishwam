package com.apollopharmacy.vishwam.ui.home;

import java.util.ArrayList;

public interface MainActivityCallback {
    void onClickFilterIcon();
    void onClickSiteIdIcon();
    void onClickQcFilterIcon();

    void onSelectApprovedFragment(String listSize);

    void onSelectRejectedFragment();

    void onSelectPendingFragment();
    void onClickSpinnerLayout();
    void onClickSubmenuItem(String menuName, ArrayList<MenuModel> submenus, int position);
}
