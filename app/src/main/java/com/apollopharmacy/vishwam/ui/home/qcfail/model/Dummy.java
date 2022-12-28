package com.apollopharmacy.vishwam.ui.home.qcfail.model;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

public class Dummy {
    private String siteId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void runw () {
        Set<String> stringSet = new LinkedHashSet<>();
//        stringSet.addAll()
    }
//public void run() throws MalformedURLException {
//     URL url=null;
//     String file = null;
//     url=new URL(file);
//     file=url.getPath();
//     file=file.substring(file.lastIndexOf('/')+1);
//    DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url+""));
//    request.setTitle(file);
//    request.setMimeType("application/pdf");
//    request.allowScanningByMediaScanner();
//    request.setAllowedOverMetered(true);
//    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,file);
//    DownloadManager dm=(DownloadManager) getSystemService(DoWNLAOD_SERVICE);
//    dm.enqueue(request);
//
//}



    public void run(){
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ filename);
//        Intent target = new Intent(Intent.ACTION_VIEW);
//        target.setDataAndType(Uri.fromFile(file),"application/pdf");
//        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//        Intent intent = Intent.createChooser(target, "Open File");
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            // Instruct the user to install a PDF reader here, or something
//        }

    }
}
