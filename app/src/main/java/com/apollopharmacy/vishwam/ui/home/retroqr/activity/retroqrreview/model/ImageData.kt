package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.model

import java.io.File

class ImageData {
    var images: List<Image>? = null
    var reviewImages: List<ReviewImage>? = null

    class Image {
        var file: File? = null
        var matchingPercentage: String? = null
    }

    class ReviewImage {
        var url: String? = null
    }
}