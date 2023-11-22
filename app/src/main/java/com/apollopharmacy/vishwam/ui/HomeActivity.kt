package com.apollopharmacy.vishwam.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apollopharmacy.vishwam.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_home)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}