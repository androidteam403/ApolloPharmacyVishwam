package com.apollopharmacy.vishwam.ui.home.qcfail.model

import java.io.Serializable

class MainMenuList : Serializable {
    var name: String? = null
    var isClicked = false

    constructor(name: String?) {
        this.name = name
    }

    fun setisClicked(click:Boolean){
        isClicked=click
    }
}