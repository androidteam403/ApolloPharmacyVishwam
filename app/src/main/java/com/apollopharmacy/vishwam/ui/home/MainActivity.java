package com.apollopharmacy.vishwam.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.apollopharmacy.vishw.PendingFragment;
import com.apollopharmacy.vishwam.BuildConfig;
import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.data.Preferences;
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse;
import com.apollopharmacy.vishwam.data.model.LoginDetails;
import com.apollopharmacy.vishwam.databinding.DialogAlertMessageBinding;
import com.apollopharmacy.vishwam.databinding.DialogAlertPermissionBinding;
import com.apollopharmacy.vishwam.dialog.SignOutDialog;
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.adrenalin.attendance.AttendanceFragment;
import com.apollopharmacy.vishwam.ui.home.adrenalin.history.HistoryFragment;
import com.apollopharmacy.vishwam.ui.home.apna.survey.ApnaSurveyFragment;
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.PreRectroApprovalFragment;
import com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.PreRectroFragment;
import com.apollopharmacy.vishwam.ui.home.apolloassets.AssetsFragment;
import com.apollopharmacy.vishwam.ui.home.apollosensing.ApolloSensingFragment;
import com.apollopharmacy.vishwam.ui.home.cashcloser.CashCloserFragment;
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
import com.apollopharmacy.vishwam.ui.rider.activity.SplashScreen;
import com.apollopharmacy.vishwam.ui.rider.changepassword.ChangePasswordFragment;
import com.apollopharmacy.vishwam.ui.rider.complaints.ComplaintsFragment;
import com.apollopharmacy.vishwam.ui.rider.complaints.ComplaintsFragmentCallback;
import com.apollopharmacy.vishwam.ui.rider.dashboard.DashboardFragment;
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;
import com.apollopharmacy.vishwam.ui.rider.help.HelpFragment;
import com.apollopharmacy.vishwam.ui.rider.login.LoginActivity;
import com.apollopharmacy.vishwam.ui.rider.myorders.MyOrdersFragment;
import com.apollopharmacy.vishwam.ui.rider.myorders.MyOrdersFragmentCallback;
import com.apollopharmacy.vishwam.ui.rider.profile.ProfileFragment;
import com.apollopharmacy.vishwam.ui.rider.reports.ReportsFragment;
import com.apollopharmacy.vishwam.ui.rider.service.BatteryLevelLocationService;
import com.apollopharmacy.vishwam.ui.rider.service.FloatingTouchService;
import com.apollopharmacy.vishwam.ui.rider.summary.SummaryFragment;
import com.apollopharmacy.vishwam.util.FragmentUtils;
import com.apollopharmacy.vishwam.util.Utils;
import com.apollopharmacy.vishwam.util.Utlis;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityCallback {
    public static MainActivity mInstance;
    public MainActivityCallback mainActivityCallback;
    public static boolean isSuperAdmin = false;
    public static boolean isAttendanceRequired = false;
    public static boolean isQcFailRequired = false;
    public static boolean isSwachhRequired = false;
    public static boolean isDrugRequired = false;
    //    private String mCurrentFrag;
    private int selectedItemPos = -1;

    private static TextView cartCount, notificationText;
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
    public String employeeRoleRetro = "";

    public String employeeRoleNewDrugRequest;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private NavigationListView listView;
    public ImageView siteIdIcon;
    public ImageView plusIconApna;
    public ImageView filterIconApna;
    public ImageView refreshIconQc;

    public RelativeLayout filterIcon;
    public RelativeLayout qcfilterIcon;
    public LinearLayout logout;
    public static Boolean isAtdLogout = false;
    private Context context;
    public View filterIndicator;
    public View qcfilterIndicator;
    private ViewGroup header;
    private boolean isLanchedByPushNotification;
    private boolean IS_COMPLAINT_RESOLVED;
    private MyOrdersFragmentCallback myOrdersFragmentCallback;

    Fragment fragment = null;
    private boolean isFromNotificaionIcon;
    private LocationManager locationManager;
    private final static int GPS_REQUEST_CODE = 2;
    private boolean isHomeScreen = true;

//    private NavigationListAdapter adapter;

    private boolean isStoreSuperVisour;

    private RelativeLayout riderNotificationLayout;
    private ImageView notifyImage;
    private static TextView notificationTextCustom;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) if (intent.getBooleanExtra("ORDER_ASSIGNED", false)) {
            Dialog alertDialog = new Dialog(this);
            DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
            alertDialog.setContentView(alertMessageBinding.getRoot());
            alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
            alertDialog.setCancelable(false);
            alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                alertDialog.dismiss();
                if (getSessionManager().getNotificationStatus()) displaySelectedScreen("Dashboard");
                else Toast.makeText(this, "No Notification.", Toast.LENGTH_SHORT).show();
            });
            alertDialog.show();
        } else if (intent.getBooleanExtra("order_cancelled", false)) {
            Dialog alertDialog = new Dialog(this);
            DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
            alertDialog.setContentView(alertMessageBinding.getRoot());
            alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
            alertDialog.setCancelable(false);
            alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            alertDialog.show();
        } else if (intent.getBooleanExtra("order_shifted", false)) {
            Dialog alertDialog = new Dialog(this);
            DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
            alertDialog.setContentView(alertMessageBinding.getRoot());
            alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
            alertDialog.setCancelable(false);
            alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            alertDialog.show();
        } else if (intent.getBooleanExtra("COMPLAINT_RESOLVED", false)) {
            Dialog alertDialog = new Dialog(this);
            DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
            alertDialog.setContentView(alertMessageBinding.getRoot());
            alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
            alertDialog.setCancelable(false);
            alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                alertDialog.dismiss();
                if (currentItem.equals(getString(R.string.menu_complaints)))
                    complaintsFragmentCallback.complaintResolvedCallback();
            });
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGpsEnambled()) {
                    checkLocationPermission();
                } else {
                    buildAlertMessageNoGps();
                }
            } else {
                Dialog dialog1 = new Dialog(this, R.style.fadeinandoutcustomDialog);
                DialogAlertPermissionBinding permissionDeniedBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_permission, null, false);
                dialog1.setContentView(permissionDeniedBinding.getRoot());
                dialog1.setCancelable(false);
                permissionDeniedBinding.locationPermissionDeniedText.setText("Location permission must be required to access application");
                permissionDeniedBinding.locationPermissionBtn.setText("Location Permission");
                permissionDeniedBinding.locationPermissionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isGpsEnambled()) {
                            checkLocationPermission();
                        } else {
                            buildAlertMessageNoGps();
                        }
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        }
    }

    public ComplaintsFragmentCallback complaintsFragmentCallback;

    public void setComplaintsFragmentCallback(ComplaintsFragmentCallback complaintsFragmentCallback) {
        this.complaintsFragmentCallback = complaintsFragmentCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        onClickRiderNotification();

        if (getIntent() != null) {
            IS_COMPLAINT_RESOLVED = getIntent().getBooleanExtra("COMPLAINT_RESOLVED", false);
            isLanchedByPushNotification = (Boolean) getIntent().getBooleanExtra("isPushNotfication", false);
            isFromNotificaionIcon = (Boolean) getIntent().getBooleanExtra("is_from_notification", false);
        }
        LinearLayout locationDeniedLayout = (LinearLayout) findViewById(R.id.location_denied);
        locationDeniedLayout.setVisibility(View.GONE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        if (isGpsEnambled()) {
//            checkLocationPermission();
//        } else {
//            buildAlertMessageNoGps();
//        }
        setUp();


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("tag")) {
            boolean flag = extras.getBoolean("tag");
            if (flag) {
                if (fragment != null) {

                    for (int i = 0; i < listView.getListHeader().size(); i++) {
                        if (listView.getListHeader().get(i).getChildModelList().contains("Dashboard")) {
                            fragment = new DashboardFragment();

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, fragment);
                            ft.commit();
                        }
                    }

                }

            }

        }

        filterIndicator = (View) findViewById(R.id.filter_indication);
        qcfilterIndicator = (View) findViewById(R.id.qc_filter_indication);
        logout = findViewById(R.id.logout_menu);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExit();
            }
        });


//
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

        if (empDetailsResponses != null && empDetailsResponses.getData() != null && empDetailsResponses.getData().getRole() != null && empDetailsResponses.getData().getRole().getCode().equals("store_supervisor"))
            isStoreSuperVisour = true;
        else isStoreSuperVisour = false;
        qcfilterIcon = findViewById(R.id.qc_filter_icon);
        refreshIconQc = findViewById(R.id.refreshIconQc);


        filterIconApna = findViewById(R.id.filtericonapna);
        plusIconApna = findViewById(R.id.plusIconapna);
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
        refreshIconQc.setOnClickListener(v -> {
            if (mainActivityCallback != null) {
                mainActivityCallback.onClickQcFilterIcon();
            }
//            displaySelectedScreen("QcDashboard");
//            headerText.setText("Dashboard");
//            fragment = new QcDashboard();
//            qcfilterIcon.setVisibility(View.GONE);
//            plusIconApna.setVisibility(View.GONE);
//            refreshIconQc.setVisibility(View.VISIBLE);
//
//            filterIconApna.setVisibility(View.GONE);
//            filterIcon.setVisibility(View.GONE);
//            siteIdIcon.setVisibility(View.GONE);
//            isHomeScreen = false;
//            riderNotificationLayout.setVisibility(View.GONE);
//            toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
            if (fragment != null) {
                fragment=new QcDashboard();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.detach(fragment);
                ft.attach(fragment);
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
            }




        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
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
        employeeRoleRetro = Preferences.INSTANCE.getRetroEmployeeRoleUid();

        employeeRoleNewDrugRequest = Preferences.INSTANCE.getEmployeeRoleUidNewDrugRequest();
        if (loginData != null) {
//            userNameText.setText("JaiKumar Loknathan Mudaliar");
            idText.setText("ID: " + loginData.getEMPID());
            isSuperAdmin = loginData.getIS_SUPERADMIN();
            userNameText.setText(loginData.getEMPNAME());

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

    public void setMyOrdersFragmentCallback(MyOrdersFragmentCallback myOrdersFragmentCallback) {
        this.myOrdersFragmentCallback = myOrdersFragmentCallback;
    }

    private void setUp() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
//        handleAssistiveTouchWindow();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            return;
        }
        startService();
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

    @SuppressLint("ResourceAsColor")
    public void displaySelectedScreen(String itemName) {
//        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if(isAllowFragmentChange &&(frg instanceof RegistrationFragment || frg instanceof  Drug)){
//            showAlertDialog(itemName);
//            return;
//        }


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
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = true;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Complaint Register":
                headerText.setText("Complaint Registration");
                fragment = new RegistrationFragment();
                filterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                isAllowFragmentChange = true;
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
//                toolbar.setBackground(getResources().getDrawable(R.color.splash_start_color));
                break;
            case "Complaint List":
                headerText.setText("Complaint List");
                fragment = new ComplainListFragment();
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Approval List":
                headerText.setText("Approval List");
//                fragment = new ComplainListFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isFromApprovalList", true);
                ComplainListFragment fragInfo1 = new ComplainListFragment();
                fragInfo1.setArguments(bundle1);
                fragment = fragInfo1;
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Attendance":
                headerText.setText("Attendance");
                fragment = new AttendanceFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "History":
                headerText.setText("History");
                fragment = new HistoryFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Pending":
                headerText.setText("Pending List");
                fragment = new PendingOrderFragment();
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Approved":
                headerText.setText("Approved List");
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                fragment = new ApprovedFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Rejected":
                headerText.setText("Rejected List");
                fragment = new RejectedFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Bill":
                headerText.setText("Bill List");
                fragment = new BillCompletedFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                refreshIconQc.setVisibility(View.GONE);

                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Swachh Images Upload":
                headerText.setText("Swachh Images");
                fragment = new SwacchImagesUploadFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "Swachh List":
                headerText.setText("SWACHH LIST");
                fragment = new SwacchFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "Upload":
                headerText.setText("SWACHH LIST");
                fragment = new SampleSwachUi();
                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                isUploadScreen = true;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "List":
                headerText.setText("SWACHH LIST");
                fragment = new SwachListFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                isListScreen = true;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "New Drug Request":  //"Drug Request":
                headerText.setText("New Drug Request");
                fragment = new Drug();
                filterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                isAllowFragmentChange = true;
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Greetings to Chairman":
                Intent i = new Intent(this, GreetingActivity.class);
                refreshIconQc.setVisibility(View.GONE);

                startActivity(i);
                break;


            case "Cash Deposit":
                headerText.setText("Cash Deposit");
                fragment = new CashCloserFragment();
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Apna Form":
//                headerText.setText("Apna Form");
//                fragment = new ApnaFormFragment();
//                filterIcon.setVisibility(View.GONE);
//                plusIconApna.setVisibility(View.GONE);
//                filterIconApna.setVisibility(View.GONE);
//                qcfilterIcon.setVisibility(View.GONE);
//                siteIdIcon.setVisibility(View.GONE);
//                isHomeScreen = false;
//                riderNotificationLayout.setVisibility(View.GONE);
//                break;
            case "Apna Survey":
                headerText.setText("Apna Survey");
                fragment = new ApnaSurveyFragment();
                filterIcon.setVisibility(View.VISIBLE);
                plusIconApna.setVisibility(View.VISIBLE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.apna_project_actionbar_bg));
                break;

            case "Apollo Sensing":
                headerText.setText("Apollo Pharmacy");
                fragment = new ApolloSensingFragment();
                filterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                qcfilterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                break;

            case "New Drug List":  //"Drug List":
                headerText.setText("New Drug List");
                fragment = new DrugListFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromDrugList", true);
                ComplainListFragment fragInfo = new ComplainListFragment();
                fragInfo.setArguments(bundle);
                fragment = fragInfo;
                filterIcon.setVisibility(View.VISIBLE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "QcDashboard":
                headerText.setText("Dashboard");
                fragment = new QcDashboard();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.VISIBLE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "OutStanding":
                headerText.setText("Pending List");
                fragment = new PendingFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Login":
                Intent j = new Intent(this, SplashScreen.class);
                startActivity(j);
                break;
            case "Dashboard":
                headerText.setText("Dashboard");
                fragment = new DashboardFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Profile":
                headerText.setText("Profile");
                fragment = new ProfileFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "My Orders":
                headerText.setText("My Orders");
                fragment = new MyOrdersFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Cash Deposits":
                headerText.setText("Cash Deposits");
                fragment = new ReportsFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Summary":
                headerText.setText("Summary");
                fragment = new SummaryFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Complaints":
                headerText.setText("Complaints");
                fragment = new ComplaintsFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Change Password":
                headerText.setText("Dashboard");
                fragment = new ChangePasswordFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Help":
                headerText.setText("Help");
                fragment = new HelpFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.VISIBLE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "LogOut":
                getSessionManager().clearAllSharedPreferences();
                Intent intent = new Intent(this, LoginActivity.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(intent);
                break;
            case "QcApproved":
                headerText.setText("Approved List");
                fragment = new com.apollopharmacy.vishwam.ui.home.qcfail.approved.ApprovedFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "QcRejected":
                headerText.setText("Rejected List");
                fragment = new com.apollopharmacy.vishwam.ui.home.qcfail.rejected.RejectedFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
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
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;
            case "Champs Reports":
                headerText.setText("CHAMPS Analysis Reports");
                fragment = new ChampsReportsFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "Champs Admin":
                headerText.setText("Admin Module");
                fragment = new AdminModuleFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "Creation":
                headerText.setText("Pre Retro");
                Bundle bundlePreRectro = new Bundle();
                bundlePreRectro.putBoolean("fromPreRectro", true);
                PreRectroFragment fragPreRectro = new PreRectroFragment();
                fragPreRectro.setArguments(bundlePreRectro);
                fragment = fragPreRectro;
                refreshIconQc.setVisibility(View.GONE);

                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "Post Rectro":
                headerText.setText("Post Retro");
                Bundle bundlePostRectro = new Bundle();
                bundlePostRectro.putBoolean("fromPostRectro", true);
                PreRectroFragment fragPostRectro = new PreRectroFragment();
                fragPostRectro.setArguments(bundlePostRectro);
                fragment = fragPostRectro;
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "After Completion":
                headerText.setText("After Completion");
                Bundle bundleAfterCompletion = new Bundle();
                bundleAfterCompletion.putBoolean("fromAfterCompletion", true);
                PreRectroFragment fragAfterCompletion = new PreRectroFragment();
                fragAfterCompletion.setArguments(bundleAfterCompletion);
                fragment = fragAfterCompletion;
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "Approval":
                headerText.setText("Pre Rectro Approval");
                Bundle preRetroApprovalbundle = new Bundle();
                preRetroApprovalbundle.putBoolean("fromPreRectroApproval", true);
                PreRectroApprovalFragment fragPreRectroApproval = new PreRectroApprovalFragment();
                fragPreRectroApproval.setArguments(preRetroApprovalbundle);
                fragment = fragPreRectroApproval;
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                isHomeScreen = false;
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                riderNotificationLayout.setVisibility(View.GONE);
                break;

            case "Post Rectro Approval":
                headerText.setText("Post Rectro Approval");
                Bundle postRetroApprovalbundle = new Bundle();
                postRetroApprovalbundle.putBoolean("fromPostRectroApproval", true);
                PreRectroApprovalFragment fragPostRectroApproval = new PreRectroApprovalFragment();
                fragPostRectroApproval.setArguments(postRetroApprovalbundle);
                fragment = fragPostRectroApproval;
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "After Completion Approval":
                headerText.setText("After Completion Approval");
                Bundle afterCompletionApprovalbundle = new Bundle();
                afterCompletionApprovalbundle.putBoolean("fromAfterCompletionApproval", true);
                PreRectroApprovalFragment fragAfterCompletionApproval = new PreRectroApprovalFragment();
                fragAfterCompletionApproval.setArguments(afterCompletionApprovalbundle);
                fragment = fragAfterCompletionApproval;
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;


            case "Assets":
                headerText.setText("Apollo Assets");
                fragment = new AssetsFragment();
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);

                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                riderNotificationLayout.setVisibility(View.GONE);
                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
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
        Log.d("onNavigationItem", String.valueOf(item));


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
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            boolean isFinishingActivity = Boolean.parseBoolean(data.getStringExtra("FinishingActivity"));
            if (isFinishingActivity) {
                displaySelectedScreen("Summary");
            }
        } else if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(MainActivity.this, FloatingTouchService.class);
                    if (!isMyServiceRunning(FloatingTouchService.class)) {
                        startService(intent);
                    }
                }
            }
        } else if (requestCode == GPS_REQUEST_CODE) {
            if (isGpsEnambled()) {
                LinearLayout locationDeniedLayout = (LinearLayout) findViewById(R.id.location_denied);
                locationDeniedLayout.setVisibility(View.GONE);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (isGpsEnambled()) {
                    checkLocationPermission();
                } else {
                    buildAlertMessageNoGps();
                }
            } else {
                LinearLayout locationDeniedLayout = (LinearLayout) findViewById(R.id.location_denied);
                TextView locationPermissionDeniedText = (TextView) findViewById(R.id.location_permission_denied_text);
                locationPermissionDeniedText.setText("GPS enable to access application");
                Button locationPermissionBtn = (Button) findViewById(R.id.location_permission_btn);
                locationPermissionBtn.setText("GPS Permission");
                locationDeniedLayout.setVisibility(View.VISIBLE);
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_notification, menu);

        final MenuItem menuNotificationItem = menu.findItem(R.id.action_setting_icon);
        View actionNotificationView = MenuItemCompat.getActionView(menuNotificationItem);

        notificationText = actionNotificationView.findViewById(R.id.notification_text);
        if (!getSessionManager().getNotificationStatus()) {
            notificationText.setVisibility(View.GONE);
            notificationText.clearAnimation();
            DashboardFragment.newOrderViewVisibility(false);
            getSessionManager().setNotificationStatus(false);
        }
        actionNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onOptionsItemSelected(menuNotificationItem);
                notificationText.setVisibility(View.GONE);
                notificationText.clearAnimation();
//                DashboardFragment.newOrderViewVisibility(false);
//                getSessionManager().setNotificationStatus(false);
            }
        });

        final MenuItem menuCartItem = menu.findItem(R.id.action_cart_icon);
        if (getString(R.string.menu_take_order).equals(currentItem)) {
            menuCartItem.setVisible(true);
        } else {
            menuCartItem.setVisible(false);
        }

        View actionCartView = MenuItemCompat.getActionView(menuCartItem);
        cartCount = actionCartView.findViewById(R.id.cart_count_text);
        actionCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuCartItem);
            }
        });
        return true;
    }

    static Animation anim;

    public static void notificationDotVisibility(boolean show) {
        if (show) {
            try {
                notificationTextCustom.setVisibility(View.VISIBLE);
                anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(350); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                notificationTextCustom.startAnimation(anim);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = Utlis.INSTANCE.getCurrentTimeDate();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                mInstance.getSessionManager().setNotificationArrivedTime(Utlis.INSTANCE.getTimeFormatter(orderDateMills));
            } catch (Exception e) {
                System.out.println("NavigationActivity:::::::::::::::::::::::::::::" + e.getMessage());
            }

        } else {
            notificationTextCustom.setVisibility(View.GONE);
            notificationTextCustom.clearAnimation();

        }
    }

    public SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    public void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(locationSettingsResponse -> {
            mRequestingLocationUpdates = true;
            Utils.printMessage(TAG, "All location settings are satisfied.");
            //Toast.makeText(applicationContext, "Started location updates!", Toast.LENGTH_SHORT).show()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
                        rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
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
        listView.init(this).addHeaderModel(new HeaderModel("Home", R.drawable.ic_menu_home));

//        listView = findViewById(R.id.expandable_navigation);
//        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)listView.getLayoutParams();
//        params.leftMargin=20;
//        params.topMargin=5;

//        listView.addHeaderModel(new HeaderModel("Greetings to Chairman", Color.WHITE, false, R.drawable.ic_network__1___2_));

//        listView.addHeaderModel(new HeaderModel("Cash Deposit", Color.WHITE, false, R.drawable.ic_apollo_pending));

        listView.addHeaderModel(new HeaderModel("Apollo Sensing", Color.WHITE, false, R.drawable.ic_menu_champ));

        if (isAttendanceRequired) {
            listView.addHeaderModel(new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_menu_cms).addChildModel(new ChildModel("Attendance", R.drawable.ic_menu_reports)).addChildModel(new ChildModel("History", R.drawable.ic_menu_survey)));
        }
        if (isCMSRequired) {
            listView.addHeaderModel(new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_menu_cms).addChildModel(new ChildModel("Complaint Register", R.drawable.ic_apollo_complaint_register)).addChildModel(new ChildModel("Complaint List", R.drawable.ic_apollo_complaint_list))
                    .addChildModel(new ChildModel("Approval List", R.drawable.ic_apollo_complaint_list))
            );
        }
        if (isDiscountRequired) {
            listView.addHeaderModel(new HeaderModel("Discount", Color.WHITE, true, R.drawable.ic_menu_discount).addChildModel(new ChildModel("Pending", R.drawable.ic_apollo_pending)).addChildModel(new ChildModel("Approved", R.drawable.ic_apollo_approve__1_)).addChildModel(new ChildModel("Rejected", R.drawable.ic_apollo_reject)).addChildModel(new ChildModel("Bill", R.drawable.ic_apollo_bill)));
        }
        if (isDrugRequired) {
            if (employeeRoleNewDrugRequest.equalsIgnoreCase("Yes")) {
                listView.addHeaderModel(new HeaderModel("Raise New Drug request", Color.WHITE, true, R.drawable.ic_menu_drug_request).addChildModel(new ChildModel("New Drug Request", R.drawable.ic_apollo_new_drug_request__1_)).addChildModel(new ChildModel("New Drug List", R.drawable.ic_apollo_new_drug_list)));

            }
        }


        if (isQcFailRequired) {
            listView.addHeaderModel(new HeaderModel("OMS QC", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard", R.drawable.ic_apollo_dashboard)).addChildModel(new ChildModel("OutStanding", R.drawable.ic_apollo_pending)).addChildModel(new ChildModel("Approved", R.drawable.ic_apollo_approve__1_)).addChildModel(new ChildModel("Rejected", R.drawable.ic_apollo_reject))


            );
        }
        if (isSwachhRequired) {
            if ((employeeRole.equalsIgnoreCase("Yes")) && userDesignation != null && (userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO"))) {
                listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.ic_menu_swachh).addChildModel(new ChildModel("Upload", R.drawable.ic_apollo_upload)).addChildModel(new ChildModel("List", R.drawable.ic_apollo_list2)));
            } else if (employeeRole.equalsIgnoreCase("Yes")) {
                listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.ic_menu_swachh).addChildModel(new ChildModel("Upload", R.drawable.ic_apollo_upload)));
            } else if (userDesignation != null && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
                listView.addHeaderModel(new HeaderModel("Swachh", Color.WHITE, true, R.drawable.ic_menu_swachh).addChildModel(new ChildModel("List", R.drawable.ic_apollo_list2)));
            }
        }
//        if (true) {
//
//            if (getSessionManager().getLoginToken().isEmpty()) {
//                listView.addHeaderModel(new HeaderModel("Vivekagam", Color.WHITE, true, R.drawable.ic_untitled_1)
//                        .addChildModel(new ChildModel("Login", R.drawable.ic_apollo_pending)));
//            } else if (getSessionManager().getLoginToken().length() > 1) {
//                listView.addHeaderModel(new HeaderModel("Vivekagam", Color.WHITE, true, R.drawable.ic_untitled_1)
//                                .addChildModel(new ChildModel("Dashboard", R.drawable.ic_apollo_dashboard))
//                                .addChildModel(new ChildModel("My Orders", R.drawable.ic_apollo_list2))
//                                .addChildModel(new ChildModel("Cash Deposits", R.drawable.ic_apollo_bill))
//                                .addChildModel(new ChildModel("Summary", R.drawable.ic_apollo_survey_68__1_))
//                                .addChildModel(new ChildModel("Complaints", R.drawable.ic_apollo_complaint_list))
////                                .addChildModel(new ChildModel("Profile", R.drawable.ic_apollo_survey_admin))
////                                .addChildModel(new ChildModel("Change Password", R.drawable.ic_apollo_complaint_register))
//                                .addChildModel(new ChildModel("Help", R.drawable.ic_apollo_new_drug_request__1_))
//                        //.addChildModel(new ChildModel("LogOut", R.drawable.ic_apollo_pending))
//
//
//                );
//            }
//
//        }


        listView.addHeaderModel(new HeaderModel("Champs", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Champs Survey", R.drawable.ic_apollo_survey_68__1_)).addChildModel(new ChildModel("Champs Reports", R.drawable.ic_apollo_survey_report__1_)).addChildModel(new ChildModel("Champs Admin", R.drawable.ic_apollo_survey_admin)));
//
//        listView.addHeaderModel(new HeaderModel("Apna Rectro", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Pre Rectro", R.drawable.ic_apollo_survey_68__1_)).addChildModel(new ChildModel("Post Rectro", R.drawable.ic_apollo_survey_report__1_)).addChildModel(new ChildModel("After Completion", R.drawable.ic_apollo_survey_admin)).addChildModel(new ChildModel("Pre Rectro Approval", R.drawable.ic_apollo_survey_68__1_)).addChildModel(new ChildModel("Post Rectro Approval", R.drawable.ic_apollo_survey_report__1_)).addChildModel(new ChildModel("After Completion Approval", R.drawable.ic_apollo_survey_admin)));

        if ((employeeRoleRetro.equalsIgnoreCase("Yes")) && Preferences.INSTANCE.getAppLevelDesignationApnaRetro().toLowerCase().contains("exec") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("MANAGER") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().toLowerCase().contains("ceo") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("GENERAL MANAGER")) {
            listView.addHeaderModel(new HeaderModel("Apna Retro", Color.WHITE, true, R.drawable.ic_menu_champ)
                    .addChildModel(new ChildModel("Creation", R.drawable.ic_apollo_survey_68__1_))
                    .addChildModel(new ChildModel("Approval", R.drawable.ic_apollo_survey_68__1_)));

        } else if ((employeeRoleRetro.equalsIgnoreCase("Yes"))) {
            listView.addHeaderModel(new HeaderModel("Apna Retro", Color.WHITE, true, R.drawable.ic_menu_champ)
                    .addChildModel(new ChildModel("Creation", R.drawable.ic_apollo_survey_68__1_)));
        } else if (Preferences.INSTANCE.getAppLevelDesignationApnaRetro().toLowerCase().contains("exec") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("MANAGER") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().toLowerCase().contains("ceo") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("GENERAL MANAGER")) {
            listView.addHeaderModel(new HeaderModel("Apna Retro", Color.WHITE, true, R.drawable.ic_menu_champ)
                    .addChildModel(new ChildModel("Approval", R.drawable.ic_apollo_survey_68__1_)));
        }

        listView.addHeaderModel(new HeaderModel("APNA", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Apna Survey", R.drawable.ic_apollo_survey_68__1_)));

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
            } else if (listHeader.get(groupPosition).getTitle().equals("Cash Deposit")) {
                displaySelectedScreen("Cash Deposit");
                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Assets")) {
                displaySelectedScreen("Assets");
                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Apollo Sensing")) {
                displaySelectedScreen("Apollo Sensing");
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
                } else if (childModelList.get(childPosition).getTitle().equals("Approval List")) {
                    displaySelectedScreen("Approval List");
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
            } else if (listHeader.get(groupPosition).getTitle().equals("Vivekagam")) {
//                adapter = new NavigationListAdapter(context, listView.getListHeader());

                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Login")) {
                    displaySelectedScreen("Login");
                } else if (childModelList.get(childPosition).getTitle().equals("Dashboard")) {
                    displaySelectedScreen("Dashboard");
                } else if (childModelList.get(childPosition).getTitle().equals("Profile")) {
                    displaySelectedScreen("Profile");
                } else if (childModelList.get(childPosition).getTitle().equals("My Orders")) {
                    displaySelectedScreen("My Orders");
                } else if (childModelList.get(childPosition).getTitle().equals("Cash Deposits")) {
                    displaySelectedScreen("Cash Deposits");
                } else if (childModelList.get(childPosition).getTitle().equals("Summary")) {
                    displaySelectedScreen("Summary");
                } else if (childModelList.get(childPosition).getTitle().equals("Complaints")) {
                    displaySelectedScreen("Complaints");
                } else if (childModelList.get(childPosition).getTitle().equals("Change Password")) {
                    displaySelectedScreen("Change Password");
                } else if (childModelList.get(childPosition).getTitle().equals("Help")) {
                    displaySelectedScreen("Help");
                } else if (childModelList.get(childPosition).getTitle().equals("LogOut")) {
                    displaySelectedScreen("LogOut");
                }

//                adapter.notifyDataSetChanged();
            } else if (listHeader.get(groupPosition).getTitle().equals("Champs")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Champs Survey")) {
                    displaySelectedScreen("Champs Survey");
                } else if (childModelList.get(childPosition).getTitle().equals("Champs Reports")) {
                    displaySelectedScreen("Champs Reports");
                } else if (childModelList.get(childPosition).getTitle().equals("Champs Admin")) {
                    displaySelectedScreen("Champs Admin");
                }
            } else if (listHeader.get(groupPosition).getTitle().equals("APNA")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Apna Survey")) {
                    displaySelectedScreen("Apna Survey");
                } else if (childModelList.get(childPosition).getTitle().equals("Video View")) {
                    displaySelectedScreen("Video View");
                }
            } else if (listHeader.get(groupPosition).getTitle().equals("Apna Retro")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Creation")) {
                    displaySelectedScreen("Creation");
                }
//                else if (childModelList.get(childPosition).getTitle().equals("Post Rectro")) {
//                    displaySelectedScreen("Post Rectro");
//                } else if (childModelList.get(childPosition).getTitle().equals("After Completion")) {
//                    displaySelectedScreen("After Completion");
//                }
                else if (childModelList.get(childPosition).getTitle().equals("Approval")) {
                    displaySelectedScreen("Approval");
                }
//                else if (childModelList.get(childPosition).getTitle().equals("Post Rectro Approval")) {
//                    displaySelectedScreen("Post Rectro Approval");
//                }
//                else if (childModelList.get(childPosition).getTitle().equals("After Completion Approval")) {
//                    displaySelectedScreen("After Completion Approval");
//                }

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

    public void updateSelection(int pos) {
        selectedItemPos = pos;
        if (pos == 0) {
            displaySelectedScreen("Dashboard");
        } else if (pos == 1) {
            displaySelectedScreen("My Orders");
        } else if (pos == 2) {
            displaySelectedScreen("Cash Deposits");
        } else if (pos == 3) {
            displaySelectedScreen("Summary");
        } else if (pos == 4) {
            displaySelectedScreen("Complaints");
        } else if (pos == 5) {
            displaySelectedScreen("Profile");
        } else if (pos == 6) {
            displaySelectedScreen("Change Password");
        } else if (pos == 7) {
            displaySelectedScreen("Help");
        }
//        adapter.onSelection(pos);
    }

    @SuppressLint("WrongConstant")
    public void showFragment(Fragment fragment, @StringRes int titleResId) {
        FragmentUtils.replaceFragment(this, fragment, R.id.content_frame, true, 5);
//        if (titleResId == 0)
//            return;
//        setTitle(titleResId);
    }

    @Override
    public void onClickSiteIdIcon() {

    }

    @Override
    public void onClickQcFilterIcon() {

    }

    public void startService() {
        startService(new Intent(this, BatteryLevelLocationService.class));
    }

    // Method to stop the BatteryLevelLocationService
    public void stopBatteryLevelLocationService() {
        stopService(new Intent(this, BatteryLevelLocationService.class));
    }

    private boolean isGpsEnambled() {

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public final static int REQUEST_CODE = 1234;

    private void handleAssistiveTouchWindow() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                Intent intent = new Intent(MainActivity.this, FloatingTouchService.class);
                if (!isMyServiceRunning(FloatingTouchService.class)) {
                    startService(intent);
                }
            }
        } else {
            Intent intent = new Intent(MainActivity.this, FloatingTouchService.class);
            if (!isMyServiceRunning(FloatingTouchService.class)) {
                startService(intent);
            }
        }
    }

    public void setProfileData() {
//        if (getSessionManager().getRiderProfileResponse() != null) {
//            ImageView userImg = header.findViewById(R.id.user_image);
//            if (getSessionManager().getRiderProfileResponse() != null && getSessionManager().getRiderProfileResponse().getData() != null && getSessionManager().getRiderProfileResponse().getData().getPic() != null && getSessionManager().getRiderProfileResponse().getData().getPic().size() > 0)
//                Glide.with(this).load(getSessionManager().getrRiderIconUrl()).circleCrop().error(R.drawable.apollo_app_logo).into(userImg);
////            TextView riderName = header.findViewById(R.id.nav_header_rider_name);
////            riderName.setText(getSessionManager().getRiderProfileResponse().getData().getFirstName() + " " + getSessionManager().getRiderProfileResponse().getData().getLastName());
////            TextView riderPhoneNumber = header.findViewById(R.id.nav_header_rider_phone_number);
////            riderPhoneNumber.setText(getSessionManager().getRiderProfileResponse().getData().getPhone());
//        }
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

    private void buildAlertMessageNoGps() {

        new AlertDialog.Builder(this).setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", (dialog, id) -> startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_REQUEST_CODE)).setNegativeButton("No", (dialog, id) -> {
            dialog.dismiss();
            dialog.cancel();
            Dialog dialog1 = new Dialog(this, R.style.fadeinandoutcustomDialog);
            DialogAlertPermissionBinding permissionDeniedBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_permission, null, false);
            dialog1.setContentView(permissionDeniedBinding.getRoot());
            dialog1.setCancelable(false);
            permissionDeniedBinding.locationPermissionDeniedText.setText("GPS enable to access application");
            permissionDeniedBinding.locationPermissionBtn.setText("GPS permission");
            permissionDeniedBinding.locationPermissionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isGpsEnambled()) {
                        checkLocationPermission();
                    } else {
                        buildAlertMessageNoGps();
                    }
                    dialog1.dismiss();
                }
            });
            dialog1.show();

        }).create().show();
    }

    private void onClickRiderNotification() {
        riderNotificationLayout = (RelativeLayout) findViewById(R.id.rider_notification_layout);
        notifyImage = (ImageView) findViewById(R.id.notify_image);
        notificationTextCustom = (TextView) findViewById(R.id.notification_text);

        notifyImage.setOnClickListener(v -> {
            notificationTextCustom.setVisibility(View.GONE);
            notificationTextCustom.clearAnimation();
            if (getSessionManager().getNotificationStatus()) updateSelection(1);
            else Toast.makeText(this, "No Notification.", Toast.LENGTH_SHORT).show();
        });
        notificationTextCustom.setOnClickListener(v -> {
            notificationTextCustom.setVisibility(View.GONE);
            notificationTextCustom.clearAnimation();
            if (getSessionManager().getNotificationStatus()) updateSelection(1);
            else Toast.makeText(this, "No Notification.", Toast.LENGTH_SHORT).show();
        });
    }
}


