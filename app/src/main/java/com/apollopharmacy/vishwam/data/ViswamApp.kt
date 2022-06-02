package com.apollopharmacy.vishwam.data

import android.app.Application
import android.content.Context

class ViswamApp : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}