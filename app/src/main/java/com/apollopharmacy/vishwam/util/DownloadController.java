package com.apollopharmacy.vishwam.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.apollopharmacy.vishwam.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DownloadController {
    private final Context context;
    private final String url;
    private static final String FILE_NAME = "app-pagination-recyclerview.apk";
    private static final String FILE_BASE_PATH = "file://";
    private static final String MIME_TYPE = "application/vnd.android.package-archive";
    private static final String PROVIDER_PATH = ".provider";
    private static final String APP_INSTALL_PATH = "\"application/vnd.android.package-archive\"";

    public DownloadController(@NotNull Context context, @NotNull String url) {
        this.context = context;
        this.url = url;
    }

    public final void enqueueDownload() {
        String destination = this.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/";
        destination = destination + FILE_NAME;
        Uri uri = Uri.parse(FILE_BASE_PATH + destination);
        File file = new File(destination);
        if (file.exists()) {
            file.delete();
        }

        DownloadManager downloadManager = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(this.url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setMimeType(MIME_TYPE);
        request.setTitle("APK is downloading");
        request.setDescription("Downloading...");
        request.setDestinationUri(uri);

        this.showInstallOption(destination, uri);
        // Enqueue a new download and same the referenceId
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
        Toast.makeText(this.context, "Downloading...", 1).show();

    }

    private final void showInstallOption(final String destination, final Uri uri) {
        // set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(@NotNull Context context, @NotNull Intent intent) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + PROVIDER_PATH, new File(destination));
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    install.setData(contentUri);
                    context.startActivity(install);
                    context.unregisterReceiver(this);
                } else {
                    Intent installx = new Intent(Intent.ACTION_VIEW);
                    installx.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    installx.setDataAndType(uri, APP_INSTALL_PATH);
                    context.startActivity(installx);
                    context.unregisterReceiver(this);
                }

            }
        };
        this.context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


}
