package com.apollopharmacy.vishwam.ui.home.retroqr.activity;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageComparator {
    static {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initDebug(true); // Enable extra image format support
        }
    }

    public static double compareImages(Mat imagePath1, String imagePath2) {
        // Load images

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
}

