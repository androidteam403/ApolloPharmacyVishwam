package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview

import android.view.View
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse

interface PreviewActivityCallback {
     fun onClickCategory(categoryName: String, position: Int)

     fun onClickBack()
     fun onClickImageView(it: View?, imageUrl: GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas)
}