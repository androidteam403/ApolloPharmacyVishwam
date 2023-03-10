package com.apollopharmacy.vishwam.ui.home.apolloassets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.base.BaseFragment;
import com.apollopharmacy.vishwam.databinding.FragmentAssetsBinding;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class AssetsFragment extends BaseFragment<AssetsViewModel, FragmentAssetsBinding> {
    public String idNo;
    private CodeScanner codeScanner;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (getSupportActionBar() != null) {
//            this.getSupportActionBar().hide();
//        }
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        assetsBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        int PERMISSION_ALL = 1;
//        String[] PERMISSIONS = {Manifest.permission.CAMERA,};
//        if (!hasPermission(this, PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        } else {
//            startScanning();
//
//        }
//
//
//    }

    public void startScanning() {
        codeScanner = new CodeScanner(getContext(), viewBinding.scannerView);
        codeScanner.startPreview();
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        if (result != null) {
                            ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                            toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//                            Uri notification= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                            Ringtone ringtone=RingtoneManager.getRingtone(MainActivity.this,notification);
//                            ringtone.play();
//                            Toast.makeText(getContext(), result.getText(), Toast.LENGTH_SHORT).show();
                            viewBinding.open.setBackgroundColor(Color.parseColor("#3CB371"));
                            idNo = result.getText();
                            viewBinding.open.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewBinding.scannerView.setVisibility(View.GONE);
                                    viewBinding.scanner.setVisibility(View.GONE);
                                    viewBinding.webview.setVisibility(View.VISIBLE);
                                    viewBinding.close.setVisibility(View.VISIBLE);
                                    viewBinding.open.setVisibility(View.GONE);
//                                    assetsBinding.title.setVisibility(View.GONE);
                                    viewBinding.scanmessage.setVisibility(View.GONE);
                                    viewBinding.webview.getSettings().setDomStorageEnabled(true);
                                    viewBinding.webview.getSettings().setJavaScriptEnabled(true);
                                    String url = Uri.parse("https://apollocms.v35.dev.zeroco.de/apollo_assets/assets/assets-details?id=" + idNo).toString();
                                    viewBinding.webview.loadUrl(url);
                                }
                            });

                            viewBinding.close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewBinding.scanner.setVisibility(View.VISIBLE);
                                    viewBinding.open.setBackgroundColor(Color.parseColor("#BBBBBB"));
//                                    assetsBinding.title.setVisibility(View.VISIBLE);
                                    viewBinding.open.setVisibility(View.VISIBLE);
                                    viewBinding.scanmessage.setVisibility(View.VISIBLE);
                                    viewBinding.scannerView.setVisibility(View.VISIBLE);
                                    viewBinding.close.setVisibility(View.GONE);
                                    viewBinding.webview.setVisibility(View.GONE);

                                }
                            });

                        }

//                    }
//                });
            }
        });
    }


    public static boolean hasPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_assets;
    }

    @NonNull
    @Override
    public AssetsViewModel retrieveViewModel() {
        return new AssetsViewModel();
    }

    @Override
    public void setup() {
//        assetsBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA,};
        if (!hasPermission(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, PERMISSION_ALL);
        } else {
            startScanning();

        }
    }
}