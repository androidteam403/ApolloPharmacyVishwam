package com.apollopharmacy.vishwam.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.apollopharmacy.vishw.PendingFragment;
import com.apollopharmacy.vishwam.BuildConfig;
import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.data.Preferences;
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse;
import com.apollopharmacy.vishwam.data.model.LoginDetails;
import com.apollopharmacy.vishwam.dialog.SignOutDialog;
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.AttendanceFragment;
import com.apollopharmacy.vishwam.ui.home.adrenalin.history.HistoryFragment;
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.AdminModuleFragment;
import com.apollopharmacy.vishwam.ui.home.champs.reports.fragment.ChampsReportsFragment;
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyFragment;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ComplainListFragment;
import com.apollopharmacy.vishwam.ui.home.cms.registration.RegistrationFragment;
import com.apollopharmacy.vishwam.ui.home.discount.approved.ApprovedFragment;
import com.apollopharmacy.vishwam.ui.home.discount.bill.BillCompletedFragment;
import com.apollopharmacy.vishwam.ui.home.discount.pending.PendingOrderFragment;
import com.apollopharmacy.vishwam.ui.home.discount.rejected.RejectedFragment;
import com.apollopharmacy.vishwam.ui.home.drugmodule.Drug;
import com.apollopharmacy.vishwam.ui.home.drugmodule.druglist.DrugListFragment;
import com.apollopharmacy.vishwam.ui.home.greeting.GreetingActivity;
import com.apollopharmacy.vishwam.ui.home.home.HomeFragment;
import com.apollopharmacy.vishwam.ui.home.menu.notification.NotificationActivity;
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.QcDashboard;
import com.apollopharmacy.vishwam.ui.home.swacchlist.SwacchFragment;
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.SwachListFragment;
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.SelectSiteActivityy;
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.SampleSwachUi;
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.swachuploadfragment.SwacchImagesUploadFragment;
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
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityCallback {
    public static MainActivity mInstance;
    public MainActivityCallback mainActivityCallback;
    public static boolean isSuperAdmin = false;
    public static boolean isAttendanceRequired = false;
    public static boolean isQcFailRequired = false;
    public static boolean isSwachhRequired = false;
    public static boolean isDrugRequired = false;

    public static boolean isCMSRequired = false;
    public static boolean isDiscountRequired = false;
    public static String userDesignation;
    private TextView headerText;
    public Boolean isListScreen = false;
    public Boolean isUploadScreen = false;
    private LinearLayout gpsLoaderLayout;
    private String previousItem = "";
    private String currentItem = "";
    private TextView textCartItemCount;
    private int mCartItemCount = 16;
    public static boolean siteIdScreen = false;
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
    private Toolbar toolbar;
    private DrawerLayout drawer;
    public String employeeRole = "";
    public String employeeRoleNewDrugRequest;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private NavigationListView listView;
    public ImageView siteIdIcon;
    public RelativeLayout filterIcon;
    public RelativeLayout qcfilterIcon;
    public LinearLayout logout;
    public static Boolean isAtdLogout = false;
    private Context context;
    public View filterIndicator;
    public View qcfilterIndicator;

    Fragment fragment = null;

    private boolean isHomeScreen = true;

    private boolean isStoreSuperVisour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;


        filterIndicator = (View) findViewById(R.id.filter_indication);
        qcfilterIndicator = (View) findViewById(R.id.qc_filter_indication);
        logout = findViewById(R.id.logout_menu);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExit();
            }
        });

//       Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        String empDetailsResponseJson = Preferences.INSTANCE.getEmployeeDetailsResponseJson();
        EmployeeDetailsResponse empDetailsResponses = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            empDetailsResponses = gson.fromJson(empDetailsResponseJson, EmployeeDetailsResponse.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        if (empDetailsResponses != null
                && empDetailsResponses.getData() != null
                && empDetailsResponses.getData().getRole() != null
                && empDetailsResponses.getData().getRole().getCode().equals("store_supervisor")
        ) isStoreSuperVisour = true;
        else isStoreSuperVisour = false;
        qcfilterIcon = findViewById(R.id.qc_filter_icon);


        filterIcon = findViewById(R.id.filterIcon);
        siteIdIcon = findViewById(R.id.siteId_icon);
        siteIdIcon.setOnClickListener(v -> {
            if (mainActivityCallback != null) {
                mainActivityCallback.onClickSiteIdIcon();
            }
        });
        filterIcon.setOnClickListener(v -> {
            if (mainActivityCallback != null) {
                mainActivityCallback.onClickFilterIcon();
            }
        });

        qcfilterIcon.setOnClickListener(v -> {
            if (mainActivityCallback != null) {
                mainActivityCallback.onClickQcFilterIcon();
            }
        });
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


//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        int colorInt = getResources().getColor(R.color.white);
        ColorStateList csl = ColorStateList.valueOf(colorInt);
        navigationView.setItemTextColor(csl);
        TextView userNameText = navigationView.getHeaderView(0).findViewById(R.id.userName);
        TextView idText = navigationView.getHeaderView(0).findViewById(R.id.id_for_menu);
        navigationView.setNavigationItemSelectedListener(this);

        String loginJson = Preferences.INSTANCE.getLoginJson();
        LoginDetails loginData = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            loginData = gson.fromJson(loginJson, LoginDetails.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        if (Preferences.INSTANCE.getAppLevelDesignationSwach() != null && !Preferences.INSTANCE.getAppLevelDesignationSwach().isEmpty()) {
            userDesignation = Preferences.INSTANCE.getAppLevelDesignationSwach();
        }

        employeeRole = Preferences.INSTANCE.getEmployeeRoleUid();
        employeeRoleNewDrugRequest = Preferences.INSTANCE.getEmployeeRoleUidNewDrugRequest();
        if (loginData != null) {
            userNameText.setText(loginData.getEMPNAME());
            idText.setText("ID: " + loginData.getEMPID());
            isSuperAdmin = loginData.getIS_SUPERADMIN();

//            Toast.makeText(getApplicationContext(), "" + userDesignation, Toast.LENGTH_SHORT).show();

//            employeeRoleNewDrugRequest="Yes";
//            employeeRole="Yes";
//           userDesignation="EXECUTIVE";
//           Toast.makeText(this, userDesignation, Toast.LENGTH_SHORT).show();
            isAttendanceRequired = loginData.getIS_ATTANDENCEAPP();
            isCMSRequired = loginData.getIS_CMSAPP();
            isDiscountRequired = loginData.getIS_DISCOUNTAPP();
            isSwachhRequired = loginData.getIS_SWACHHAPP();
            isQcFailRequired = loginData.getIS_QCFAILAPP();
            isDrugRequired = loginData.getIS_NEWDRUGAPP();
        }

        TextView versionInfo = findViewById(R.id.versionInfo);
        versionInfo.setText("Version : " + BuildConfig.VERSION_NAME);
        updateDynamicNavMenu(isAttendanceRequired, isCMSRequired, isDiscountRequired, isSwachhRequired, isQcFailRequired, isDrugRequired);
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
        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (frg instanceof IOnBackPressed) {
            if (((IOnBackPressed) frg).onBackPressed()) {
                if (frg instanceof RegistrationFragment) {
                    showAlertDialog("HOME", "Do you want to exit Complaint Registration?");
                } else if (frg instanceof Drug) {
                    showAlertDialog("HOME", "Do you want to exit New Drug Request?");
                }
            } else {
                displaySelectedScreen("HOME");
                drawer.closeDrawer(GravityCompat.START);
            }
        } else {
            if (isHomeScreen) {
                finish();
            } else {
                displaySelectedScreen("HOME");
                drawer.closeDrawer(GravityCompat.START);
            }
        }


//        if (isListScreen || isUploadScreen) {
//            displaySelectedScreen("HOME");
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (drawer.isDrawerOpen(GravityCompat.START)) {
//                drawer.closeDrawer(GravityCompat.START);
//            } else {
//                if (isAtdLogout) {
//                    super.onBackPressed();
//                } else {
//                    dialogExit();
//                }
//
//            }
//        }


    }

    private boolean isAllowFragmentChange = false;

    public void displaySelectedScreen(String itemName) {
//        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if(isAllowFragmentChange &&(frg instanceof RegistrationFragment || frg instanceof  Drug)){
//            showAlertDialog(itemName);
//            return;
//        }

        //creating fragment object
        if (!itemName.equalsIgnoreCase("Greetings to Chairman")) {
            currentItem = itemName;
            if (previousItem.equals(currentItem) && !currentItem.equals("Logout")) {
                return;
            }
        }
        //initializing the fragment object which is selected
        switch (itemName) {
            case "HOME":
                headerText.setText("HOME");
                fragment = new HomeFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = true;
                break;
            case "Complaint Register":
                headerText.setText("Complaint Registration");
                fragment = new RegistrationFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                isAllowFragmentChange = true;
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Complaint List":
                headerText.setText("Complaint List");
                fragment = new ComplainListFragment();
                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Attendance":
                headerText.setText("Attendance");
                fragment = new AttendanceFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "History":
                headerText.setText("History");
                fragment = new HistoryFragment();
                qcfilterIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Pending":
                headerText.setText("Pending List");
                fragment = new PendingOrderFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Approved":
                headerText.setText("Approved List");

                fragment = new ApprovedFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Rejected":
                headerText.setText("Rejected List");
                fragment = new RejectedFragment();
                qcfilterIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Bill":
                headerText.setText("Bill List");
                fragment = new BillCompletedFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Swachh Images Upload":
                headerText.setText("Swachh Images");
                fragment = new SwacchImagesUploadFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;

            case "Swachh List":
                headerText.setText("SWACHH LIST");
                fragment = new SwacchFragment();
                qcfilterIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;

            case "Upload":
                headerText.setText("SWACHH LIST");
                fragment = new SampleSwachUi();
                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                isUploadScreen = true;
                break;
            case "List":
                headerText.setText("SWACHH LIST");
                fragment = new SwachListFragment();
                qcfilterIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                isListScreen = true;
                break;
            case "New Drug Request":  //"Drug Request":
                headerText.setText("New Drug Request");
                fragment = new Drug();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                isAllowFragmentChange = true;
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Greetings to Chairman":
                Intent i = new Intent(this, GreetingActivity.class);
                startActivity(i);
                break;

            case "New Drug List":  //"Drug List":
                headerText.setText("New Drug List");
                fragment = new DrugListFragment();
                qcfilterIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromDrugList", true);
                ComplainListFragment fragInfo = new ComplainListFragment();
                fragInfo.setArguments(bundle);
                fragment = fragInfo;
                filterIcon.setVisibility(View.VISIBLE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "QcDashboard":
                headerText.setText("Dashboard");
                fragment = new QcDashboard();
                qcfilterIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "OutStanding":
                headerText.setText("Pending List");
                fragment = new PendingFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "QcApproved":
                headerText.setText("Approved List");
                fragment = new com.apollopharmacy.vishwam.ui.home.qcfail.approved.ApprovedFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;

            case "QcRejected":
                headerText.setText("Rejected List");
                fragment = new com.apollopharmacy.vishwam.ui.home.qcfail.rejected.RejectedFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;

//            case "Select Site":
//                headerText.setText("Select Site ID");
//                fragment = new SelectSiteActivityy();
//                imageView.setVisibility(View.GONE);
//                break;
            case "Champs Survey":
                headerText.setText("New Survey");
                fragment = new NewSurveyFragment();
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;
            case "Champs Reports":
                headerText.setText("CHAMPS Analysis Reports");
                fragment = new ChampsReportsFragment();
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                break;

            case "Champs Admin":
                headerText.setText("Admin Module");
                fragment = new AdminModuleFragment();
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
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

    public void close() {
        listView.setSelected(3);

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
        resetAllMenuItemsTextColor(navigationView);
        setTextColorForMenuItem(item, R.color.white);
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(String.valueOf(item.getItemId()));
        setTextColorForMenuItem(item, R.color.white);
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

    private void updateDynamicNavMenu(boolean isAttendanceRequired, boolean isCMSRequired, boolean isDiscountRequired, boolean isSwachhRequired, boolean isQcFailRequired, boolean isDrugRequired) {
        listView.init(this)
                .addHeaderModel(new HeaderModel("Home", R.drawable.ic_menu_home));


        listView.addHeaderModel(new HeaderModel("Greetings to Chairman", Color.WHITE, false, R.drawable.ic_baseline_celebration_24));

        if (isAttendanceRequired) {
            listView.addHeaderModel(
                    new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
                            .addChildModel(new ChildModel("Attendance", R.drawable.ic_menu_reports))
                            .addChildModel(new ChildModel("History", R.drawable.ic_menu_survey))
            );
        }
        if (isCMSRequired) {
            listView.addHeaderModel(
                    new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_menu_cms)
                            .addChildModel(new ChildModel("Complaint Register", R.drawable.ic_apollo_complaint_register))
                            .addChildModel(new ChildModel("Complaint List", R.drawable.ic_apollo_complaint_list))
            );
        }
        if (isDiscountRequired) {
            listView.addHeaderModel(
                    new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_menu_discount)
                            .addChildModel(new ChildModel("Pending", R.drawable.ic_apollo_pending))
                            .addChildModel(new ChildModel("Approved", R.drawable.ic_apollo_approve__1_))
                            .addChildModel(new ChildModel("Rejected", R.drawable.ic_apollo_reject))
                            .addChildModel(new ChildModel("Bill", R.drawable.ic_apollo_bill))
            );
        }
        if (isDrugRequired) {
            if (employeeRoleNewDrugRequest.equalsIgnoreCase("Yes")) {
                listView.addHeaderModel(
                        new HeaderModel("Raise New Drug request", Color.WHITE, true, R.drawable.ic_menu_drug_request)
                                .addChildModel(new ChildModel("New Drug Request", R.drawable.ic_apollo_new_drug_request__1_))
                                .addChildModel(new ChildModel("New Drug List", R.drawable.ic_apollo_new_drug_list)));

            }
        }


        if (isQcFailRequired) {
            listView.addHeaderModel(new HeaderModel("OMS QC", Color.WHITE, true, R.drawable.ic_menu_qc_fall)
                    .addChildModel(new ChildModel("Dashboard", R.drawable.ic_apollo_dashboard))
                    .addChildModel(new ChildModel("OutStanding", R.drawable.ic_apollo_pending))
                    .addChildModel(new ChildModel("Approved", R.drawable.ic_apollo_approve__1_))
                    .addChildModel(new ChildModel("Rejected", R.drawable.ic_apollo_reject))


            );
        }
        if (isSwachhRequired) {
            if ((employeeRole.equalsIgnoreCase("Yes")) && userDesignation != null && (userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO"))) {
                listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.ic_menu_swachh)
                        .addChildModel(new ChildModel("Upload", R.drawable.ic_apollo_upload))
                        .addChildModel(new ChildModel("List", R.drawable.ic_apollo_list2)));
            } else if (employeeRole.equalsIgnoreCase("Yes")) {
                listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.ic_menu_swachh)
                        .addChildModel(new ChildModel("Upload", R.drawable.ic_apollo_upload)));
            } else if (userDesignation != null && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
                listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.ic_menu_swachh)
                        .addChildModel(new ChildModel("List", R.drawable.ic_apollo_list2)));
            }
        }
        listView.addHeaderModel(new HeaderModel("Champs", Color.WHITE, true, R.drawable.ic_menu_champ)
                .addChildModel(new ChildModel("Champs Survey", R.drawable.ic_apollo_survey_68__1_))
                .addChildModel(new ChildModel("Champs Reports", R.drawable.ic_apollo_survey_report__1_))
                .addChildModel(new ChildModel("Champs Admin", R.drawable.ic_apollo_survey_admin))
        );
//        listView.addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout));

        listView.build().addOnGroupClickListener((parent, v, groupPosition, id) -> {
            List<HeaderModel> listHeader = listView.getListHeader();
            if (listHeader.get(groupPosition).getTitle().equals("Home")) {
                listView.setSelected(groupPosition);
                displaySelectedScreen("HOME");
                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Logout")) {
                displaySelectedScreen("Logout");
                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Greetings to Chairman")) {
                displaySelectedScreen("Greetings to Chairman");
                drawer.closeDrawer(GravityCompat.START);
            }
            return false;

        }).addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            listView.setSelected(groupPosition, childPosition);
            List<HeaderModel> listHeader = listView.getListHeader();

            if (listHeader.get(groupPosition).getTitle().equals("Attendance Management")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Attendance")) {
                    displaySelectedScreen("Attendance");
                } else if (childModelList.get(childPosition).getTitle().equals("History")) {
                    displaySelectedScreen("History");
                }

            }
//

            else if (listHeader.get(groupPosition).getTitle().equals("CMS")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Complaint Register")) {
                    displaySelectedScreen("Complaint Register");
                } else if (childModelList.get(childPosition).getTitle().equals("Complaint List")) {
                    displaySelectedScreen("Complaint List");
                }
            } else if (listHeader.get(groupPosition).getTitle().equals("Discount")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();

                if (childModelList.get(childPosition).getTitle().equals("Pending")) {
                    displaySelectedScreen("Pending");
                } else if (childModelList.get(childPosition).getTitle().equals("Approved")) {
                    displaySelectedScreen("Approved");
                } else if (childModelList.get(childPosition).getTitle().equals("Rejected")) {
                    displaySelectedScreen("Rejected");
                } else if (childModelList.get(childPosition).getTitle().equals("Bill")) {
                    displaySelectedScreen("Bill");
                }
            } else if (listHeader.get(groupPosition).getTitle().equals("Raise New Drug request")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("New Drug Request")) {
                    displaySelectedScreen("New Drug Request");
                } else if (childModelList.get(childPosition).getTitle().equals("New Drug List")) {
                    displaySelectedScreen("New Drug List");
                }
            } else if (listHeader.get(groupPosition).getTitle().equals("OMS QC")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Dashboard")) {
                    displaySelectedScreen("QcDashboard");
                }
                if (childModelList.get(childPosition).getTitle().equals("OutStanding")) {
                    displaySelectedScreen("OutStanding");
                } else if (childModelList.get(childPosition).getTitle().equals("Approved")) {
                    displaySelectedScreen("QcApproved");
                } else if (childModelList.get(childPosition).getTitle().equals("Rejected")) {
                    displaySelectedScreen("QcRejected");
                }

            } else if (listHeader.get(groupPosition).getTitle().equals("Swachh")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Upload")) {
                    displaySelectedScreen("Upload");
                } else if (childModelList.get(childPosition).getTitle().equals("List")) {
                    displaySelectedScreen("List");
                }
            } else if (listHeader.get(groupPosition).getTitle().equals("Champs")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Champs Survey")) {
                    displaySelectedScreen("Champs Survey");
                } else if (childModelList.get(childPosition).getTitle().equals("Champs Reports")) {
                    displaySelectedScreen("Champs Reports");
                } else if (childModelList.get(childPosition).getTitle().equals("Champs Admin")) {
                    displaySelectedScreen("Champs Admin");
                }
            }


//            if (groupPosition == 1 && childPosition == 0) {
//                displaySelectedScreen("Pending");
//            } else if (groupPosition == 1 && childPosition == 1) {
//                displaySelectedScreen("Approved");
//            } else if (groupPosition == 1 && childPosition == 2) {
//                displaySelectedScreen("Rejected");
//            } else if (groupPosition == 1 && childPosition == 3) {
//                displaySelectedScreen("Bill");
//            }
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });


//        switch (isAttendanceRequired + "-" + isCMSRequired + "-" + isDiscountRequired) {
//
//
//            case "false-false-true":
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//                        .addHeaderModel(
//                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
//                                        .addChildModel(new ChildModel("Pending"))
//                                        .addChildModel(new ChildModel("Approved"))
//                                        .addChildModel(new ChildModel("Rejected"))
//                                        .addChildModel(new ChildModel("Bill"))
//                        )
//
//
//                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            if (id == 0) {
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } else if (id == 2) {
//                                displaySelectedScreen("Logout");
//                                drawer.closeDrawer(GravityCompat.START);
//                            }
//                            return false;
//                        })
//                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//                            listView.setSelected(groupPosition, childPosition);
//                            if (groupPosition == 1 && childPosition == 0) {
//                                displaySelectedScreen("Pending");
//                            } else if (groupPosition == 1 && childPosition == 1) {
//                                displaySelectedScreen("Approved");
//                            } else if (groupPosition == 1 && childPosition == 2) {
//                                displaySelectedScreen("Rejected");
//                            } else if (groupPosition == 1 && childPosition == 3) {
//                                displaySelectedScreen("Bill");
//                            }
//                            drawer.closeDrawer(GravityCompat.START);
//                            return false;
//                        });
//                break;
//            case "false-true-false":
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//                        .addHeaderModel(
//                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
//                                        .addChildModel(new ChildModel("Complaint Register"))
//                                        .addChildModel(new ChildModel("Complaint List"))
//                        )
//                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            if (id == 0) {
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } else if (id == 2) {
//                                displaySelectedScreen("Logout");
//                                drawer.closeDrawer(GravityCompat.START);
//                            }
//                            return false;
//                        })
//                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//                            listView.setSelected(groupPosition, childPosition);
//                            if (groupPosition == 1 && childPosition == 0) {
//                                displaySelectedScreen("Complaint Register");
//                            } else if (groupPosition == 1 && childPosition == 1) {
//                                displaySelectedScreen("Complaint List");
//                            }
//                            drawer.closeDrawer(GravityCompat.START);
//                            return false;
//                        });
//                break;
//            case "false-true-true":
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//                        .addHeaderModel(
//                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
//                                        .addChildModel(new ChildModel("Complaint Register"))
//                                        .addChildModel(new ChildModel("Complaint List"))
//                        )
//                        .addHeaderModel(
//                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
//                                        .addChildModel(new ChildModel("Pending"))
//                                        .addChildModel(new ChildModel("Approved"))
//                                        .addChildModel(new ChildModel("Rejected"))
//                                        .addChildModel(new ChildModel("Bill"))
//                        ).addHeaderModel(new HeaderModel("QC Fail", Color.WHITE, true, R.drawable.returns)
//                                .addChildModel(new ChildModel("Pending"))
//                                .addChildModel(new ChildModel("Approved"))
//                                .addChildModel(new ChildModel("Rejected")));
//
//
////                        ).addHeaderModel(
////                        new HeaderModel("Swacch Apollo", Color.WHITE, true, R.drawable.ic_baseline_discount)
////                                .addChildModel(new ChildModel("Swacch Images Upload"))
////                                .addChildModel(new ChildModel("Swacch List")));
////                if ( Designation.equalsIgnoreCase("manager")||userDesignation.equalsIgnoreCase("GeneralManager") || userDesignation.equalsIgnoreCase("executive")||userDesignation.equalsIgnoreCase("ceo") ) {
////                    listView.addHeaderModel(new HeaderModel("Sample Swacch UI", Color.WHITE, true, R.drawable.ic_baseline_discount)
////                            .addChildModel(new ChildModel("List Module")));
////                }
//
//                if ((employeeRole.equalsIgnoreCase("Yes")) && userDesignation != null && (userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO"))) {
//                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
//                            .addChildModel(new ChildModel("Upload"))
//                            .addChildModel(new ChildModel("List")));
//                } else if (employeeRole.equalsIgnoreCase("Yes")) {
//                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
//                            .addChildModel(new ChildModel("Upload")));
//                } else if (userDesignation != null && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
//                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
//                            .addChildModel(new ChildModel("List")));
//                }
//
//
////                if (employeeRole.equalsIgnoreCase("Yes") && !isStoreSuperVisour && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
////                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
////                            .addChildModel(new ChildModel("Upload"))
////                            .addChildModel(new ChildModel("List")));
////
////                } else if (!isStoreSuperVisour && !employeeRole.equalsIgnoreCase("Yes") && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
////                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
//////                            .addChildModel(new ChildModel("Upload"))
////                            .addChildModel(new ChildModel("List")));
//////                    listView.addHeaderModel(new HeaderModel("Sample Swacch UI", Color.WHITE, true, R.drawable.ic_baseline_discount)
//////                            .addChildModel(new ChildModel("List Module")));
////                } else if (employeeRole.equalsIgnoreCase("Yes") && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
////                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
////                            .addChildModel(new ChildModel("Upload"))
////                            .addChildModel(new ChildModel("List")));
////                } else if (employeeRole.equalsIgnoreCase("Yes") ) {
////                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
////                            .addChildModel(new ChildModel("Upload")));
////                }
////                else if (!employeeRole.equalsIgnoreCase("Yes") && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
////                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon)
////                            .addChildModel(new ChildModel("List")));
////                }else {
////                    listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.apollo_icon));
////                }
//
//
//                if (employeeRoleNewDrugRequest.equalsIgnoreCase("Yes")) {
//                    listView.addHeaderModel(
//                            new HeaderModel("Raise New Drug request", Color.WHITE, true, R.drawable.ic_baseline_article)
//                                    .addChildModel(new ChildModel("New Drug Request"))
//                                    .addChildModel(new ChildModel("New Drug List")));
//
//                } else {
//
//                }
//
////                ).addHeaderModel(
////                        new HeaderModel("Sample Swacch UI", Color.WHITE, true, R.drawable.ic_baseline_discount)
////                                .addChildModel(new ChildModel("Upload Module"))
////                                .addChildModel(new ChildModel("List Module"))
//
//
//                listView.addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            if (id == 0) {
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } else if (id == 6 || id == 5) {
//                                if (employeeRoleNewDrugRequest != null && employeeRoleNewDrugRequest.equalsIgnoreCase("Yes")) {
//                                    if (id == 6) {
//                                        displaySelectedScreen("Logout");
//                                        drawer.closeDrawer(GravityCompat.START);
//                                    }
//                                } else {
//                                    if (id == 5) {
//                                        displaySelectedScreen("Logout");
//                                        drawer.closeDrawer(GravityCompat.START);
//                                    }
//                                }
//
//                            }
//                            return false;
//                        })
//                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//                            listView.setSelected(groupPosition, childPosition);
//                            if (groupPosition == 1 && childPosition == 0) {
//                                displaySelectedScreen("Complaint Register");
//                            } else if (groupPosition == 1 && childPosition == 1) {
//                                displaySelectedScreen("Complaint List");
//                            } else if (groupPosition == 2 && childPosition == 0) {
//                                displaySelectedScreen("Pending");
//                            } else if (groupPosition == 2 && childPosition == 1) {
//                                displaySelectedScreen("Approved");
//                            } else if (groupPosition == 2 && childPosition == 2) {
//                                displaySelectedScreen("Rejected");
//                            } else if (groupPosition == 2 && childPosition == 3) {
//                                displaySelectedScreen("Bill");
//                            } else if (groupPosition == 3 && childPosition == 0) {
//                                displaySelectedScreen("QcPending");
//                            } else if (groupPosition == 3 && childPosition == 1) {
//                                displaySelectedScreen("QcApproved");
//                            } else if (groupPosition == 3 && childPosition == 2) {
//                                displaySelectedScreen("QcRejected");
//                            } else if (groupPosition == 5 && childPosition == 0) {
//                                displaySelectedScreen("Drug Request");
//                            } else if (groupPosition == 5 && childPosition == 1) {
//                                displaySelectedScreen("Drug List");
//                            }
////                            else if (groupPosition == 4 && childPosition == 1) {
////                                displaySelectedScreen("Swachh List");
////                            }
//                            else if (groupPosition == 4) {
//                                if (childPosition == 0) {
//                                    if ((employeeRole.equalsIgnoreCase("Yes")) && (userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO"))) {
//                                        displaySelectedScreen("Upload");
//                                    } else if (employeeRole.equalsIgnoreCase("Yes")) {
//                                        displaySelectedScreen("Upload");
//                                    } else if (userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
//                                        displaySelectedScreen("List");
//                                    }
//
//
////                                    if ((employeeRole.equalsIgnoreCase("Yes") && !isStoreSuperVisour) || employeeRole.equalsIgnoreCase("Yes")) {
////
////                                        displaySelectedScreen("Upload");
//////                                    if (userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO"))
////
////                                    } else {
////                                        displaySelectedScreen("List");
////
////                                    }
//                                } else if (childPosition == 1) {
//                                    displaySelectedScreen("List");
//                                }
//                            }
////                            else if (groupPosition == 3 && childPosition == 1) {
////                                if (userDesignation.equalsIgnoreCase("NODATA")) {
////                                    displaySelectedScreen("List");
////                                }
////                            }
////                            else if (groupPosition == 3 && childPosition == 1) {
////                                displaySelectedScreen("List");
////                            }
//                            drawer.closeDrawer(GravityCompat.START);
//                            return false;
//                        });
//                break;
//            case "true-false-false":
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//                        .addHeaderModel(
//                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
//                                        .addChildModel(new ChildModel("Attendance"))
//                                        .addChildModel(new ChildModel("History"))
//                        )
//                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            if (id == 0) {
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } else if (id == 2) {
//                                displaySelectedScreen("Logout");
//                                drawer.closeDrawer(GravityCompat.START);
//                            }
//                            return false;
//                        })
//                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//                            listView.setSelected(groupPosition, childPosition);
//                            if (groupPosition == 1 && childPosition == 0) {
//                                displaySelectedScreen("Attendance");
//                            } else if (groupPosition == 1 && childPosition == 1) {
//                                displaySelectedScreen("History");
//                            }
//                            drawer.closeDrawer(GravityCompat.START);
//                            return false;
//                        });
//                break;
//            case "true-false-true":
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//                        .addHeaderModel(
//                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
//                                        .addChildModel(new ChildModel("Attendance"))
//                                        .addChildModel(new ChildModel("History"))
//                        )
//                        .addHeaderModel(
//                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
//                                        .addChildModel(new ChildModel("Pending"))
//                                        .addChildModel(new ChildModel("Approved"))
//                                        .addChildModel(new ChildModel("Rejected"))
//                                        .addChildModel(new ChildModel("Bill"))
//                        )
//
//                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            if (id == 0) {
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } else if (id == 3) {
//                                displaySelectedScreen("Logout");
//                                drawer.closeDrawer(GravityCompat.START);
//                            }
//                            return false;
//                        })
//                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//                            listView.setSelected(groupPosition, childPosition);
//                            if (groupPosition == 1 && childPosition == 0) {
//                                displaySelectedScreen("Attendance");
//                            } else if (groupPosition == 1 && childPosition == 1) {
//                                displaySelectedScreen("History");
//                            } else if (groupPosition == 2 && childPosition == 0) {
//                                displaySelectedScreen("Pending");
//                            } else if (groupPosition == 2 && childPosition == 1) {
//                                displaySelectedScreen("Approved");
//                            } else if (groupPosition == 2 && childPosition == 2) {
//                                displaySelectedScreen("Rejected");
//                            } else if (groupPosition == 2 && childPosition == 3) {
//                                displaySelectedScreen("Bill");
//                            }
//                            drawer.closeDrawer(GravityCompat.START);
//                            return false;
//                        });
//                break;
//            case "true-true-false":
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//                        .addHeaderModel(
//                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
//                                        .addChildModel(new ChildModel("Attendance"))
//                                        .addChildModel(new ChildModel("History"))
//                        )
//                        .addHeaderModel(
//                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
//                                        .addChildModel(new ChildModel("Complaint Register"))
//                                        .addChildModel(new ChildModel("Complaint List"))
//                        )
//                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            if (id == 0) {
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } else if (id == 3) {
//                                displaySelectedScreen("Logout");
//                                drawer.closeDrawer(GravityCompat.START);
//                            }
//                            return false;
//                        })
//                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//                            listView.setSelected(groupPosition, childPosition);
//                            if (groupPosition == 1 && childPosition == 0) {
//                                displaySelectedScreen("Attendance");
//                            } else if (groupPosition == 1 && childPosition == 1) {
//                                displaySelectedScreen("History");
//                            } else if (groupPosition == 2 && childPosition == 0) {
//                                displaySelectedScreen("Complaint Register");
//                            } else if (groupPosition == 2 && childPosition == 1) {
//                                displaySelectedScreen("Complaint List");
//                            }
//                            drawer.closeDrawer(GravityCompat.START);
//                            return false;
//                        });
//                break;
//            case "true-true-true":
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//                        .addHeaderModel(
//                                new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_baseline_attendance)
//                                        .addChildModel(new ChildModel("Attendance"))
//                                        .addChildModel(new ChildModel("History"))
//                        )
//                        .addHeaderModel(
//                                new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_baseline_article)
//                                        .addChildModel(new ChildModel("Complaint Register"))
//                                        .addChildModel(new ChildModel("Complaint List"))
//                        )
//                        .addHeaderModel(
//                                new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_baseline_discount)
//                                        .addChildModel(new ChildModel("Pending"))
//                                        .addChildModel(new ChildModel("Approved"))
//                                        .addChildModel(new ChildModel("Rejected"))
//                                        .addChildModel(new ChildModel("Bill"))
//                        )
//                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            //drawer.closeDrawer(GravityCompat.START);
//                            if (id == 0) {
//                                //Home Menu
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } /*else if (id == 1) {
//                        //Cart Menu
//                        Common.showToast(context, "CMS");
//                        drawer.closeDrawer(GravityCompat.START);
//                    } /*else if (id == 2) {
//                        //Categories Menu
//                        Common.showToast(context, "Attendance");
//                    } else if (id == 3) {
//                        //Orders Menu
//                        Common.showToast(context, "Discount");
//                        drawer.closeDrawer(GravityCompat.START);
//                    } */ else if (id == 5) {
//                                //Logout
//                                displaySelectedScreen("Logout");
//                                drawer.closeDrawer(GravityCompat.START);
//                            }
//                            return false;
//                        })
//                        .addOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//                            listView.setSelected(groupPosition, childPosition);
//                            if (groupPosition == 1 && childPosition == 0) {
//                                displaySelectedScreen("Attendance");
//                            } else if (groupPosition == 1 && childPosition == 1) {
//                                displaySelectedScreen("History");
//                            } else if (groupPosition == 2 && childPosition == 0) {
//                                displaySelectedScreen("Complaint Register");
//                            } else if (groupPosition == 2 && childPosition == 1) {
//                                displaySelectedScreen("Complaint List");
//                            } else if (groupPosition == 3 && childPosition == 0) {
//                                displaySelectedScreen("Pending");
//                            } else if (groupPosition == 3 && childPosition == 1) {
//                                displaySelectedScreen("Approved");
//                            } else if (groupPosition == 3 && childPosition == 2) {
//                                displaySelectedScreen("Rejected");
//                            } else if (groupPosition == 3 && childPosition == 3) {
//                                displaySelectedScreen("Bill");
//                            }
//                            drawer.closeDrawer(GravityCompat.START);
//                            return false;
//                        });
//                break;
//            default:
//                listView.init(this)
//                        .addHeaderModel(new HeaderModel("Home", R.drawable.ic_baseline_home))
//
//                        .addHeaderModel(new HeaderModel("Logout", R.drawable.ic_baseline_logout))
//                        .build()
//                        .addOnGroupClickListener((parent, v, groupPosition, id) -> {
//                            if (id == 0) {
//                                listView.setSelected(groupPosition);
//                                displaySelectedScreen("HOME");
//                                drawer.closeDrawer(GravityCompat.START);
//                            } else if (id == 1) {
//                                displaySelectedScreen("Logout");
//                                drawer.closeDrawer(GravityCompat.START);
//                            }
//                            return false;
//                        });
//                break;
//        }
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

    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
    }

    private void resetAllMenuItemsTextColor(NavigationView navigationView) {
        for (int i = 0; i < navigationView.getMenu().size(); i++)
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.white);
    }


    public void onClickFilterIcon() {
        Intent i = new Intent(MainActivity.this, SelectSiteActivityy.class);
        startActivity(i);
    }

    @Override
    public void onClickSiteIdIcon() {

    }

    @Override
    public void onClickQcFilterIcon() {

    }

    public void showAlertDialog(String itemName, String description) {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_aleart);
        TextView desc = dialog.findViewById(R.id.description_text);
        desc.setText(description);
        Button noBtn = dialog.findViewById(R.id.cancel_button);
        Button yesBtn = dialog.findViewById(R.id.yes_button);
        yesBtn.setOnClickListener(view -> {
            isAllowFragmentChange = false;
            dialog.dismiss();
            displaySelectedScreen(itemName);
            listView.setSelected(0);
        });
        noBtn.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

}


