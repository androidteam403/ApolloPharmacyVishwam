package com.apollopharmacy.vishwam.ui.rider.base;
/*
 * Created on : oct 29, 2021.
 * Author : NAVEEN.M
 */

import androidx.fragment.app.Fragment;

import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;


public abstract class BaseFragment extends Fragment {

    public SessionManager getSessionManager() {
        return new SessionManager(getContext());
    }

}