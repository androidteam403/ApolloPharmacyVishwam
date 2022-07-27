package com.apollopharmacy.vishwam.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apollopharmacy.vishwam.BuildConfig;
import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.data.Preferences;
import com.apollopharmacy.vishwam.data.model.LoginDetails;
import com.apollopharmacy.vishwam.dialog.SignOutDialog;

import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.AttendanceFragment;
import com.apollopharmacy.vishwam.ui.home.adrenalin.history.HistoryFragment;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ComplainListFragment;
import com.apollopharmacy.vishwam.ui.home.cms.registration.RegistrationFragment;
import com.apollopharmacy.vishwam.ui.home.discount.approved.ApprovedFragment;
import com.apollopharmacy.vishwam.ui.home.discount.bill.BillCompletedFragment;
import com.apollopharmacy.vishwam.ui.home.discount.pending.PendingOrderFragment;
import com.apollopharmacy.vishwam.ui.home.discount.rejected.RejectedFragment;
import com.apollopharmacy.vishwam.ui.home.home.HomeFragment;
import com.apollopharmacy.vishwam.ui.home.menu.notification.NotificationActivity;
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.SwacchImagesUpload;
import com.apollopharmacy.vishwam.ui.home.swacchlist.SwacchFragment;
import com.apollopharmacy.vishwam.util.Utils;
import com.dvinfosys.model.ChildModel;
import com.dvinfosys.model.HeaderModel;
import com.dvinfosys.ui.NavigationListView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.util.Date;

import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean isSuperAdmin = false;
    public static boolean isAttendanceRequired = false;
    public static boolean isCMSRequired = false;
    public static boolean isDiscountRequired = false;
    public static String userDesignation = "";
    private TextView headerText;
    private LinearLayout gpsLoaderLayout;
    private String previousItem = "";
    private String currentItem = "";
    private TextView textCartItemCount;
    private int mCartItemCount = 16;

    private final String TAG = "MainActivity";
    public String locationLatitude = "";
    public String locationLongitude = "";
    private String mLastUpdateTime = "";
    private final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000L;
    private final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000L;
    private final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient = null;
    private LocationRequest mLocationRequest = null;
    private LocationSettingsRequest mLocationSettingsRequest = null;
    private LocationCallback mLocationCallback = null;
    private Location mCurrentLocation = null;
    private Boolean mRequestingLocationUpdates = false;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private NavigationListView listView;
    public static Boolean isAtdLogout = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Utils.printMessage(TAG, "Fetching FCM registration token failed" + task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast
                        Utils.printMessage(TAG, "Firebase Token" + token);
//                        Toast.makeText(MainActivity.this, "Firebase Token : " + token, Toast.LENGTH_SHORT).show();
                    }
                });

        listView = findViewById(R.id.expandable_navigation);

        drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        TextView userNameText = navigationView.getHeaderView(0).findViewById(R.id.userName);
        navigationView.setNavigationItemSelectedListener(this);

        String loginJson = Preferences.INSTANCE.getLoginJson();
        LoginDetails loginData = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            loginData = gson.fromJson(loginJson, LoginDetails.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        if (loginData != null) {
            userNameText.setText("Hi, " + loginData.getEMPNAME());
            isSuperAdmin = loginData.getIS_SUPERADMIN();
            userDesignation = loginData.getAPPLEVELDESIGNATION();
            isAttendanceRequired = loginData.getIS_ATTANDENCEAPP();
            isCMSRequired = loginData.getIS_CMSAPP();
            isDiscountRequired = loginData.getIS_DISCOUNTAPP();
        }

        TextView versionInfo = findViewById(R.id.versionInfo);
        versionInfo.setText("Version : " + BuildConfig.VERSION_NAME);
        updateDynamicNavMenu(isAttendanceRequired, isCMSRequired, isDiscountRequired);
//        listView.expandGroup(2);

        ImageView openDrawer = findViewById(R.id.openDrawer);
        headerText = findViewById(R.id.headerTitle);
        displaySelectedScreen("HOME");
        listView.setSelected(0);


        FrameLayout notificationLayout = findViewById(R.id.notificationLayout);
        textCartItemCount = findViewById(R.id.cart_badge);
        setupBadge();
        notificationLayout.setOnClickListener(view -> {
            Intent homeIntent = new Intent(this, NotificationActivity.class);
            startActivity(homeIntent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        openDrawer.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        gpsLoaderLayout = findViewById(R.id.gpsLoaderLayout);

        if (isAttendanceRequired) {
            initPermission();
            checkExternalPermission();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isAtdLogout) {
                super.onBackPressed();
            } else {
                dialogExit();
            }

        }
    }

    private void displaySelectedScreen(String itemName) {
        //creating fragment object
        Fragment fragment = null;

        currentItem = itemName;
        if (previousItem.equals(currentItem) && !currentItem.equals("Logout")) {
            return;
        }
        //initializing the fragment object which is selected
        switch (itemName) {
            case "HOME":
                headerText.setText("HOME");
                fragment = new HomeFragment();
                break;
            case "Complaint Register":
                headerText.setText("Complaint Registration");
                fragment = new RegistrationFragment();
                break;
            case "Complaint List":
                headerText.setText("Complaint List");
                fragment = new ComplainListFragment();
                break;
            case "Attendance":
                headerText.setText("Attendance");
                fragment = new AttendanceFragment();
                break;
            case "History":
                headerText.setText("History");
                fragment = new HistoryFragment();
                break;
            case "Pending":
                headerText.setText("Pending List");
                fragment = new PendingOrderFragment();
                break;
            case "Approved":
                headerText.setText("Approved List");
                fragment = new ApprovedFragment();
                break;
            case "Rejected":
                headerText.setText("Rejected List");
                fragment = new RejectedFragment();
                break;
            case "Bill":
                headerText.setText("Bill List");
                fragment = new BillCompletedFragment();
                break;
            case "Swacch Images Upload":
                headerText.setText("Swacch Images");
                fragment = new SwacchImagesUpload();
                break;

            case "Swacch List":
                headerText.setText("Swacch List");

                fragment = new SwacchFragment();
                break;
            case "Logout":
                dialogExit();
                break;
        }
        previousItem = itemName;
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void dialogExit() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new SignOutDialog().show(getSupportFragmentManager(), "");
            }
        }, 200);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(String.valueOf(item.getItemId()));
        //make this method blank
        return true;
    }

    public void initPermission() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            public void onLocationResult(@NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void checkExternalPermission() {
        Dexter.withActivity((Activity) this).withPermission("android.permission.ACCESS_FINE_LOCATION").withListener((PermissionListener) (new PermissionListener() {
            public void onPermissionGranted(@NotNull PermissionGrantedResponse response) {
                mRequestingLocationUpdates = true;
            }

            public void onPermissionDenied(@NotNull PermissionDeniedResponse response) {
                if (response.isPermanentlyDenied()) {
                    openSettings();
                }
            }

            public void onPermissionRationaleShouldBeShown(@NotNull PermissionRequest permission, @NotNull PermissionToken token) {
                token.continuePermissionRequest();
            }
        })).check();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        Uri uri = Uri.fromParts("package", "com.apollo.viswam", null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Utils.printMessage(this.TAG, "Actvity result");

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case RESULT_OK:
                    locationLatitude = "";
                    locationLongitude = "";
                    showGPSLoader();
                    Utils.printMessage(this.TAG, "User agreed to make required location settings changes.");
                    startLocationUpdates();
                    break;
                case RESULT_CANCELED:
                    hideGPSLoader();
                    locationLatitude = "";
                    locationLongitude = "";
                    Utils.printMessage(this.TAG, "User chose not to make required location settings changes.");
                    mRequestingLocationUpdates = false;
            }
        }
    }

    public void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(locationSettingsResponse -> {
            mRequestingLocationUpdates = true;
            Utils.printMessage(TAG, "All location settings are satisfied.");
            //Toast.makeText(applicationContext, "Started location updates!", Toast.LENGTH_SHORT).show()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
            );
            updateLocationUI();
        });
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnFailureListener(e -> {
            Intrinsics.checkNotNullParameter(e, "e");
            int statusCode = ((ApiException) e).getStatusCode();
            switch (statusCode) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Utils.printMessage(TAG, "Location settings are not satisfied. Attempting to upgrade location settings ");
                    try {
                        locationLatitude = "";
                        locationLongitude = "";
                        ResolvableApiException rae = (ResolvableApiException) e;
                        rae.startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                        );
                    } catch (IntentSender.SendIntentException var4) {
                        Utils.printMessage(TAG, "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                    Utils.printMessage(TAG, errorMessage);
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
            updateLocationUI();
        });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public void updateLocationUI() {
        if (mCurrentLocation != null) {
            hideGPSLoader();
        }
    }

    public void showGPSLoader() {
        gpsLoaderLayout.setVisibility(View.VISIBLE);
    }

    public void hideGPSLoader() {
        gpsLoaderLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAttendanceRequired) {
            if (mRequestingLocationUpdates) {
                // pausing location updates
                stopLocationUpdates();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAttendanceRequired) {
            if (mRequestingLocationUpdates && checkPermissions()) {
                startLocationUpdates();
            }
            updateLocationUI();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void updateDynamicNavMenu(boolean isAttendanceRequired, boolean isCMSRequired, boolean isDiscountRequired) {
        switch (isAttendanceRequired + "-" + isCMSRequired + "-" + isDiscountRequired) {
            case "false-false-true":
                listView.init(this)

                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
                        .addHeaderModel(
                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
                                        .addChildModel(new ChildModel("Pending"))
                                        .addChildModel(new ChildModel("Approved"))
                                        .addChildModel(new ChildModel("Rejected"))
                                        .addChildModel(new ChildModel("Bill"))
                        )

                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            if (id == 0) {
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 2) {
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        })
                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                            listView.setSelected(groupPosition, childPosition);
                            if (groupPosition == 1 && childPosition == 0) {
                                displaySelectedScreen("Pending");
                            } else if (groupPosition == 1 && childPosition == 1) {
                                displaySelectedScreen("Approved");
                            } else if (groupPosition == 1 && childPosition == 2) {
                                displaySelectedScreen("Rejected");
                            } else if (groupPosition == 1 && childPosition == 3) {
                                displaySelectedScreen("Bill");
                            }
                            drawer.closeDrawer(GravityCompat.START);
                            return false;
                        });
                break;
            case "false-true-false":
                listView.init(this)
                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
                        .addHeaderModel(
                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
                                        .addChildModel(new ChildModel("Complaint Register"))
                                        .addChildModel(new ChildModel("Complaint List"))
                        )
                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            if (id == 0) {
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 2) {
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        })
                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                            listView.setSelected(groupPosition, childPosition);
                            if (groupPosition == 1 && childPosition == 0) {
                                displaySelectedScreen("Complaint Register");
                            } else if (groupPosition == 1 && childPosition == 1) {
                                displaySelectedScreen("Complaint List");
                            }
                            drawer.closeDrawer(GravityCompat.START);
                            return false;
                        });
                break;
            case "false-true-true":
                listView.init(this)
                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
                        .addHeaderModel(
                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
                                        .addChildModel(new ChildModel("Complaint Register"))
                                        .addChildModel(new ChildModel("Complaint List"))
                        )
                        .addHeaderModel(
                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
                                        .addChildModel(new ChildModel("Pending"))
                                        .addChildModel(new ChildModel("Approved"))
                                        .addChildModel(new ChildModel("Rejected"))
                                        .addChildModel(new ChildModel("Bill"))

                        ).addHeaderModel(

                        new HeaderModel("Swacch Apollo", Color.WHITE, true, R.drawable.ic_baseline_discount)
                                .addChildModel(new ChildModel("Swacch Images Upload"))
                                .addChildModel(new ChildModel("Swacch List"))
                )


                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            if (id == 0) {
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            }  else if (id == 5) {
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        })
                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                            listView.setSelected(groupPosition, childPosition);
                            if (groupPosition == 1 && childPosition == 0) {
                                displaySelectedScreen("Complaint Register");
                            } else if (groupPosition == 1 && childPosition == 1) {
                                displaySelectedScreen("Complaint List");
                            } else if (groupPosition == 2 && childPosition == 0) {
                                displaySelectedScreen("Pending");
                            } else if (groupPosition == 2 && childPosition == 1) {
                                displaySelectedScreen("Approved");
                            } else if (groupPosition == 2 && childPosition == 2) {
                                displaySelectedScreen("Rejected");
                            } else if (groupPosition == 2 && childPosition == 3) {
                                displaySelectedScreen("Bill");
                            } else if (groupPosition == 3 && childPosition == 0) {
                                displaySelectedScreen("Swacch Images Upload");
                            } else if (groupPosition == 3 && childPosition ==1) {
                                displaySelectedScreen("Swacch List");
                            }
                            drawer.closeDrawer(GravityCompat.START);
                            return false;
                        });
                break;
            case "true-false-false":
                listView.init(this)
                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
                        .addHeaderModel(
                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
                                        .addChildModel(new ChildModel("Attendance"))
                                        .addChildModel(new ChildModel("History"))
                        )
                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            if (id == 0) {
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 2) {
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        })
                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                            listView.setSelected(groupPosition, childPosition);
                            if (groupPosition == 1 && childPosition == 0) {
                                displaySelectedScreen("Attendance");
                            } else if (groupPosition == 1 && childPosition == 1) {
                                displaySelectedScreen("History");
                            }
                            drawer.closeDrawer(GravityCompat.START);
                            return false;
                        });
                break;
            case "true-false-true":
                listView.init(this)
                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
                        .addHeaderModel(
                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
                                        .addChildModel(new ChildModel("Attendance"))
                                        .addChildModel(new ChildModel("History"))
                        )
                        .addHeaderModel(
                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
                                        .addChildModel(new ChildModel("Pending"))
                                        .addChildModel(new ChildModel("Approved"))
                                        .addChildModel(new ChildModel("Rejected"))
                                        .addChildModel(new ChildModel("Bill"))
                        )

                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            if (id == 0) {
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 3) {
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        })
                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                            listView.setSelected(groupPosition, childPosition);
                            if (groupPosition == 1 && childPosition == 0) {
                                displaySelectedScreen("Attendance");
                            } else if (groupPosition == 1 && childPosition == 1) {
                                displaySelectedScreen("History");
                            } else if (groupPosition == 2 && childPosition == 0) {
                                displaySelectedScreen("Pending");
                            } else if (groupPosition == 2 && childPosition == 1) {
                                displaySelectedScreen("Approved");
                            } else if (groupPosition == 2 && childPosition == 2) {
                                displaySelectedScreen("Rejected");
                            } else if (groupPosition == 2 && childPosition == 3) {
                                displaySelectedScreen("Bill");
                            }
                            drawer.closeDrawer(GravityCompat.START);
                            return false;
                        });
                break;
            case "true-true-false":
                listView.init(this)
                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
                        .addHeaderModel(
                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
                                        .addChildModel(new ChildModel("Attendance"))
                                        .addChildModel(new ChildModel("History"))
                        )
                        .addHeaderModel(
                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
                                        .addChildModel(new ChildModel("Complaint Register"))
                                        .addChildModel(new ChildModel("Complaint List"))
                        )
                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            if (id == 0) {
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 3) {
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        })
                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                            listView.setSelected(groupPosition, childPosition);
                            if (groupPosition == 1 && childPosition == 0) {
                                displaySelectedScreen("Attendance");
                            } else if (groupPosition == 1 && childPosition == 1) {
                                displaySelectedScreen("History");
                            } else if (groupPosition == 2 && childPosition == 0) {
                                displaySelectedScreen("Complaint Register");
                            } else if (groupPosition == 2 && childPosition == 1) {
                                displaySelectedScreen("Complaint List");
                            }
                            drawer.closeDrawer(GravityCompat.START);
                            return false;
                        });
                break;
            case "true-true-true":
                listView.init(this)
                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
                        .addHeaderModel(
                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
                                        .addChildModel(new ChildModel("Attendance"))
                                        .addChildModel(new ChildModel("History"))
                        )
                        .addHeaderModel(
                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
                                        .addChildModel(new ChildModel("Complaint Register"))
                                        .addChildModel(new ChildModel("Complaint List"))
                        )
                        .addHeaderModel(
                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
                                        .addChildModel(new ChildModel("Pending"))
                                        .addChildModel(new ChildModel("Approved"))
                                        .addChildModel(new ChildModel("Rejected"))
                                        .addChildModel(new ChildModel("Bill"))
                        )
                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            //drawer.closeDrawer(GravityCompat.START);
                            if (id == 0) {
                                //Home Menu
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            } /*else if (id == 1) {
                        //Cart Menu
                        Common.showToast(context, "CMS");
                        drawer.closeDrawer(GravityCompat.START);
                    } /*else if (id == 2) {
                        //Categories Menu
                        Common.showToast(context, "Attendance");
                    } else if (id == 3) {
                        //Orders Menu
                        Common.showToast(context, "Discount");
                        drawer.closeDrawer(GravityCompat.START);
                    } */ else if (id == 5) {
                                //Logout
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        })
                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                            listView.setSelected(groupPosition, childPosition);
                            if (groupPosition == 1 && childPosition == 0) {
                                displaySelectedScreen("Attendance");
                            } else if (groupPosition == 1 && childPosition == 1) {
                                displaySelectedScreen("History");
                            } else if (groupPosition == 2 && childPosition == 0) {
                                displaySelectedScreen("Complaint Register");
                            } else if (groupPosition == 2 && childPosition == 1) {
                                displaySelectedScreen("Complaint List");
                            } else if (groupPosition == 3 && childPosition == 0) {
                                displaySelectedScreen("Pending");
                            } else if (groupPosition == 3 && childPosition == 1) {
                                displaySelectedScreen("Approved");
                            } else if (groupPosition == 3 && childPosition == 2) {
                                displaySelectedScreen("Rejected");
                            } else if (groupPosition == 3 && childPosition == 3) {
                                displaySelectedScreen("Bill");
                            }
                            drawer.closeDrawer(GravityCompat.START);
                            return false;
                        });
                break;
            default:
                listView.init(this)
                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))

                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
                        .build()
                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
                            if (id == 0) {
                                listView.setSelected(groupPosition);
                                displaySelectedScreen("HOME");
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 1) {
                                displaySelectedScreen("Logout");
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        });
                break;
        }
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
