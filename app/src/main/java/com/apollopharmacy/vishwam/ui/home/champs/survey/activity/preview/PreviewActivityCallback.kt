package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview

import android.view.View

interface PreviewActivityCallback {
     fun onClickCategory(categoryName: String, position: Int)

     fun onClickBack()
     fun onClickImageView(it: View?, imageUrl: String?)
}