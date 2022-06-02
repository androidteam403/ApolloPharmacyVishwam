package com.apollopharmacy.vishwam.util

import android.content.Context
import android.widget.Toast

class ShowError {

    companion object {
        fun showToastMessage(message: String, context: Context) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}