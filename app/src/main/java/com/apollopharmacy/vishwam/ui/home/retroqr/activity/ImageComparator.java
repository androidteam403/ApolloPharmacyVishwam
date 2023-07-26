package com.apollopharmacy.vishwam.ui.home.retroqr.activity;


import static android.os.Build.VERSION_CODES.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ebr163.bifacialview.view.utils.BitmapUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;


public class ImageComparator implements View.OnTouchListener {

    ImageView imageview;

    static {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initDebug(true); // Enable extra image format support
        }
    }

    public static double compareImages(Mat imagePath1, String imagePath2) {

//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap1);
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap2);

//        double matchingPercentage = BitmapUtils.calculateMatchingPercentage(bitmap1, bitmap2);
//        Log.d("MatchingPercentage", "Percentage: " + matchingPercentage + "%");


        Mat img1 = imagePath1;
        Mat img2 = Imgcodecs.imread(imagePath2, Imgcodecs.IMREAD_GRAYSCALE);

        if (img1.empty() || img2.empty()) {
            // Handle error: unable to load images
            return -1;
        }

        // Detect keypoints and compute descriptors using ORB
        ORB orb = ORB.create();
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

        orb.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);
        orb.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);

        // Match descriptors
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);

        // Calculate the percentage of similarity
        double totalMatches = matches.rows();
        double goodMatches = 0;
        double thresholdDistance = 0.7; // Adjust this threshold to control the matching sensitivity

        for (int i = 0; i < totalMatches; i++) {
            if (matches.toArray()[i].distance < thresholdDistance) {
                goodMatches++;
            }
        }

        double percentageSimilarity = (goodMatches / totalMatches) * 100;
        return percentageSimilarity;
    }


    public static double calculateMatchingPercentage(Bitmap bitmap1, Bitmap bitmap2) {
        int width1 = bitmap1.getWidth();
        int height1 = bitmap1.getHeight();
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();

        if (width1 != width2 || height1 != height2) {
            throw new IllegalArgumentException("Bitmaps must have the same dimensions.");
        }

        int[] pixels1 = new int[width1 * height1];
        int[] pixels2 = new int[width2 * height2];

        bitmap1.getPixels(pixels1, 0, width1, 0, 0, width1, height1);
        bitmap2.getPixels(pixels2, 0, width2, 0, 0, width2, height2);

        int matchingPixels = 0;

        for (int i = 0; i < pixels1.length; i++) {
            if (pixels1[i] == pixels2[i]) {
                matchingPixels++;
            }
        }

        double matchingPercentage = (double) matchingPixels / pixels1.length * 100;
        return matchingPercentage;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case 1:
                if (v == imageview) {

                    return true;
                }

        }
        return false;
    }
}

