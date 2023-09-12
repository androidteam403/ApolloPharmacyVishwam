package com.apollopharmacy.vishwam.ui.home.home

import com.apollopharmacy.vishwam.ui.home.MenuModel

interface HomeFragmentCallback {

    fun onFialureMessage(message: String)
    fun onClickMenuItem(itemName: String?, menuModels: ArrayList<MenuModel>)
}