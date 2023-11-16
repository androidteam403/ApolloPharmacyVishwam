package com.apollopharmacy.vishwam.ui.home.help

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity(), HelpActivityCallback {
    private lateinit var activityHelpBinding: ActivityHelpBinding
    private lateinit var helpActivityViewModel: HelpActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHelpBinding =
            DataBindingUtil.setContentView(this@HelpActivity, R.layout.activity_help)
        helpActivityViewModel = ViewModelProvider(this)[HelpActivityViewModel::class.java]
        activityHelpBinding.callback = this@HelpActivity
        setup()
    }

    private fun setup() {
    }

    override fun onclickBackArrow() {
        super.onBackPressed()
    }
}