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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apollopharmacy.vishw.PendingFragment;
import com.apollopharmacy.vishwam.BuildConfig;
import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.data.Preferences;
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse;
import com.apollopharmacy.vishwam.data.model.LoginDetails;
import com.apollopharmacy.vishwam.databinding.DialogSubmenusBinding;
import com.apollopharmacy.vishwam.dialog.SignOutDialog;
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.adrenalin.attendance.AttendanceFragment;
import com.apollopharmacy.vishwam.ui.home.adrenalin.history.HistoryFragment;
import com.apollopharmacy.vishwam.ui.home.apna.survey.ApnaSurveyFragment;
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.PreRectroApprovalFragment;
import com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.PreRectroFragment;
import com.apollopharmacy.vishwam.ui.home.apolloassets.AssetsFragment;
import com.apollopharmacy.vishwam.ui.home.apollosensing.ApolloSensingFragment;
import com.apollopharmacy.vishwam.ui.home.cashcloser.CashCloserFragment;
import com.apollopharmacy.vishwam.ui.home.champs.reports.fragment.ChampsReportsFragment;
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.GetSurveyDetailsListActivity;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ComplainListFragment;
import com.apollopharmacy.vishwam.ui.home.cms.registration.RegistrationFragment;
import com.apollopharmacy.vishwam.ui.home.communityadvisor.CommunityAdvisorFragment;
import com.apollopharmacy.vishwam.ui.home.communityadvisor.CommunityAdvisorFragmentCallback;
import com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard.CeoDashboardFragment;
import com.apollopharmacy.vishwam.ui.home.dashboard.managerdashboard.ManagerDashboardFragment;
import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse;
import com.apollopharmacy.vishwam.ui.home.discount.approved.ApprovedFragment;
import com.apollopharmacy.vishwam.ui.home.discount.bill.BillCompletedFragment;
import com.apollopharmacy.vishwam.ui.home.discount.pending.PendingOrderFragment;
import com.apollopharmacy.vishwam.ui.home.discount.rejected.RejectedFragment;
import com.apollopharmacy.vishwam.ui.home.drugmodule.Drug;
import com.apollopharmacy.vishwam.ui.home.drugmodule.druglist.DrugListFragment;
import com.apollopharmacy.vishwam.ui.home.greeting.GreetingActivity;
import com.apollopharmacy.vishwam.ui.home.help.HelpActivity;
import com.apollopharmacy.vishwam.ui.home.home.HomeFragment;
import com.apollopharmacy.vishwam.ui.home.menu.notification.NotificationActivity;
import com.apollopharmacy.vishwam.ui.home.notification.NotificationsActivity;
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse;
import com.apollopharmacy.vishwam.ui.home.planogram.fragment.PlanogramFragment;
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.QcDashboard;
import com.apollopharmacy.vishwam.ui.home.retroqr.RetroQrFragment;
import com.apollopharmacy.vishwam.ui.home.swacchlist.SwacchFragment;
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.SwachListFragment;
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.SelectSiteActivityy;
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.SampleSwachUi;
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.swachuploadfragment.SwacchImagesUploadFragment;
import com.apollopharmacy.vishwam.ui.login.model.MobileAccessResponse;
import com.apollopharmacy.vishwam.util.FragmentUtils;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityCallback, MainActivityPlusIconCallback, MainActivityHelpIconCallback {
    public static MainActivity mInstance;
    public MainActivityCallback mainActivityCallback;

    public MainActivityHelpIconCallback mainActivityHelpIconCallback;
    public MainActivityPlusIconCallback mainActivityPlusIconCallback;
    public static boolean isSuperAdmin = false;
    public static boolean isAttendanceRequired = false;
    public static boolean isQcFailRequired = false;
    public static boolean isSwachhRequired = false;
    public static boolean isDrugRequired = false;
    public String isAdminRequired;
    public static boolean isSensingRequired = false;
    public String employeeRoleRetroQr = "";
    public static boolean isChampsRequired = false;
    public static boolean isApnaSurveyRequired = false;
    public static boolean isApnaRetroRequired = false;
    public static boolean isDashboardRequired = false;
    public static boolean isRetroQrAppRequired = false;
    public static boolean isPlanogramAppRequired = false;
    //    private String mCurrentFrag;
    private int selectedItemPos = -1;

    private static TextView cartCount, notificationText;
    public static boolean isCMSRequired = false;
    public static boolean isDiscountRequired = false;
    public static String userDesignation;
    public TextView headerText;
    public TextView headerTextLocation;

    public RelativeLayout settingsWhite;

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
    private RelativeLayout toolbar;
    private DrawerLayout drawer;
    public String employeeRole = "";
    public String employeeRoleRetro = "";

    public String employeeRoleNewDrugRequest;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private NavigationListView listView;
    public ImageView siteIdIcon;
    public ImageView plusIconAttendence;

    public RelativeLayout spinnerLayout;
    public ImageView plusIconApna;
    public ImageView filterIconApna;
    public ImageView helpIcon;

    public RelativeLayout onClickPlusIcon;
    public LinearLayout scannerIcon;
    public ImageView refreshIconQc;

    public RelativeLayout filterIcon;
    public RelativeLayout qcfilterIcon;

    private NotificationModelResponse notificationModelResponse;
    public LinearLayout logout;
    public static Boolean isAtdLogout = false;
    private Context context;
    public View filterIndicator;
    public View qcfilterIndicator;

    private ViewGroup header;
    private boolean isLanchedByPushNotification;
    private boolean IS_COMPLAINT_RESOLVED;

    Fragment fragment = null;
    private boolean isFromNotificaionIcon;
    private LocationManager locationManager;
    private final static int GPS_REQUEST_CODE = 2;
    private boolean isHomeScreen = true;
    private boolean isCeoDashboard = true;

//    private NavigationListAdapter adapter;

    private boolean isStoreSuperVisour;

    private TextView selectFilterType;

    private ImageView notifyImage;
    private static TextView notificationTextCustom;
    public String ceoDashboardAccessFromEmployee = "";

    //    private MenuItemAdapter attendanceManagementAdapter, cmsAdapter, discountAdapter, newDrugRequestAdapter, omsQcAdapter, swachhAdapter, monitoringAdapter, champsAdapter, planogramAdapter, apnaRetroAdapter, apnaAdapter;
    public ImageView backArrow;
    public ImageView openDrawer;

    private ImageView logoutBtn;
    private TextView logo;
    private LinearLayout customerDetails;
    public Dialog submenuDialog;
    public ArrayList<MenuModel> menuModels;
    public BottomNavigationView bottomNavigationView;
    public Switch switchBtn;

    private String module = "";
    private String uniqueId = "";

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;
        System.out.println("FCM KEY:::::::::::::::::::::::::::::::::::" + Preferences.INSTANCE.getFcmKey());
        mainActivityCallback = this;
        mainActivityHelpIconCallback = this;
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = findViewById(R.id.toolbar);

        if (getIntent() != null) {
            IS_COMPLAINT_RESOLVED = getIntent().getBooleanExtra("COMPLAINT_RESOLVED", false);
            isLanchedByPushNotification = (Boolean) getIntent().getBooleanExtra("isPushNotfication", false);
            isFromNotificaionIcon = (Boolean) getIntent().getBooleanExtra("is_from_notification", false);
            notificationModelResponse = (NotificationModelResponse) getIntent().getSerializableExtra("notificationResponse");

            if (getIntent().getStringExtra("MODULE") != null)
                module = (String) getIntent().getStringExtra("MODULE");
            if (getIntent().getStringExtra("UNIQUE_ID") != null)
                uniqueId = (String) getIntent().getStringExtra("UNIQUE_ID");
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
//        selectFilterType=findViewById(R.id.selectfiltertype);
//        logout = findViewById(R.id.logout_menu);
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
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

        if (empDetailsResponses != null && empDetailsResponses.getData() != null && empDetailsResponses.getData().getEnableMarketingDashboard() != null && empDetailsResponses.getData().getEnableMarketingDashboard().getUid() != null) {
            ceoDashboardAccessFromEmployee = empDetailsResponses.getData().getEnableMarketingDashboard().getUid();
        }

        if (empDetailsResponses != null && empDetailsResponses.getData() != null && empDetailsResponses.getData().getRole() != null && empDetailsResponses.getData().getRole().getCode().equals("store_supervisor"))
            isStoreSuperVisour = true;
        else isStoreSuperVisour = false;
        backArrow = findViewById(R.id.backArrow);
        logo = findViewById(R.id.apolloLogo);
        customerDetails = findViewById(R.id.customerDetailsLayout);
        qcfilterIcon = findViewById(R.id.qc_filter_icon);
        refreshIconQc = findViewById(R.id.refreshIconQc);

        helpIcon = findViewById(R.id.helpBtn);
        switchBtn = findViewById(R.id.switchBtn);

        filterIconApna = findViewById(R.id.filtericonapna);
        plusIconApna = findViewById(R.id.plusIconapna);
        filterIcon = findViewById(R.id.filterIcon);
        siteIdIcon = findViewById(R.id.siteId_icon);
        plusIconAttendence = findViewById(R.id.plusIconattendence);

        onClickPlusIcon = findViewById(R.id.plusIcon);
        onClickPlusIcon.setVisibility(View.GONE);
        settingsWhite = findViewById(R.id.settings_champs);
        settingsWhite.setVisibility(View.GONE);
        scannerIcon = findViewById(R.id.scanner);
        spinnerLayout = findViewById(R.id.spinnerlayoutRelative);
        helpIcon.setOnClickListener(v -> {
            if (mainActivityHelpIconCallback != null) {
                mainActivityHelpIconCallback.onclickHelpIconH();
            }
        });
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

        onClickPlusIcon.setOnClickListener(v -> {
            if (mainActivityPlusIconCallback != null) {
                mainActivityPlusIconCallback.onClickPlusIcon();
            }
        });

        settingsWhite.setOnClickListener(v -> {
            if (mainActivityPlusIconCallback != null) {
                mainActivityPlusIconCallback.onClickSettings();
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


        });

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
        employeeRoleRetroQr = Preferences.INSTANCE.getRetroQrEmployeeRoleUid();
        employeeRole = Preferences.INSTANCE.getEmployeeRoleUid();
        employeeRoleRetro = Preferences.INSTANCE.getRetroEmployeeRoleUid();
        isAdminRequired = Preferences.INSTANCE.getEmployeeRoleUidChampsAdmin();
        employeeRoleNewDrugRequest = Preferences.INSTANCE.getEmployeeRoleUidNewDrugRequest();
        if (loginData != null) {
//            userNameText.setText("JaiKumar Loknathan Mudaliar");
//            idText.setText("ID: " + loginData.getEMPID());
            isSuperAdmin = loginData.getIS_SUPERADMIN();
//            userNameText.setText(loginData.getEMPNAME());
            TextView username = findViewById(R.id.userName);
            username.setText(loginData.getEMPNAME());
            TextView userId = findViewById(R.id.userId);
            userId.setText("ID: " + loginData.getEMPID());


            MobileAccessResponse.AccessDetails accessDetails = Preferences.INSTANCE.getVishwamAccessResponse().getAccessDetails();


            isAttendanceRequired = accessDetails.getISATTENDENCEAPP(); //loginData.getIS_ATTANDENCEAPP();
            isCMSRequired = accessDetails.getISCMSAPP(); //loginData.getIS_CMSAPP();
            isDiscountRequired = accessDetails.getISDISCOUNTAPP(); //loginData.getIS_DISCOUNTAPP();
            isSwachhRequired = accessDetails.getISSWACHHAPP(); //loginData.getIS_SWACHHAPP();
            isQcFailRequired = accessDetails.getISQCFAILAPP(); //loginData.getIS_QCFAILAPP();
            isDrugRequired = accessDetails.getISNEWDRUGAPP(); //loginData.getIS_NEWDRUGAPP();
            isSensingRequired = accessDetails.getISSENSINGAPP(); //loginData.getIS_SENSINGAPP();
            isChampsRequired = accessDetails.getISCHAMPAPP();
            isApnaSurveyRequired = accessDetails.getISAPNAAPP();
            isApnaRetroRequired = accessDetails.getISAPNARETROAPP();
            if (accessDetails.getISDASHBOARDAPP() != null) {
                isDashboardRequired = accessDetails.getISDASHBOARDAPP();
            }
            if (accessDetails.getISRETROQRAPP() != null) {
                isRetroQrAppRequired = accessDetails.getISRETROQRAPP();
            }
            if (accessDetails.getISPLANAGRAMAPP() != null) {
                isPlanogramAppRequired = accessDetails.getISPLANAGRAMAPP();
            }
        }

        TextView version = findViewById(R.id.version);
        version.setText("Version: " + BuildConfig.VERSION_NAME);
//        updateDynamicNavMenu(isAttendanceRequired,isCMSRequired,isDiscountRequired,isSwachhRequired,isQcFailRequired,isDrugRequired,isSensingRequired,isChampsRequired,isApnaSurveyRequired,isApnaRetroRequired);

//        TextView versionInfo = findViewById(R.id.versionInfo);
//        versionInfo.setText("Version : " + BuildConfig.VERSION_NAME);
//        updateDynamicNavMenu(isAttendanceRequired, isCMSRequired, isDiscountRequired, isSwachhRequired, isQcFailRequired, isDrugRequired, isSensingRequired, isChampsRequired, isApnaSurveyRequired, isApnaRetroRequired);
//        updateDynamicNavMenu(false, false, false, false, false, false, false, false, false, false);

//        updateNavMenu(isAttendanceRequired, isCMSRequired, isDiscountRequired, isSwachhRequired, isQcFailRequired, isDrugRequired, isSensingRequired, isChampsRequired, isApnaSurveyRequired, isApnaRetroRequired);

//        listView.expandGroup(2);
//
//        ImageView openDrawer = findViewById(R.id.openDrawer);
        headerText = findViewById(R.id.headerTitle);
        headerTextLocation = findViewById(R.id.locationtext);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        if (module != null && !module.isEmpty() && module.equals("CMS")) {
            menuModels = new ArrayList<>();
            menuModels.add(new MenuModel("Register Complaint", R.drawable.cms_complaint_register, true, null, null));
            menuModels.add(new MenuModel("Complaints", R.drawable.cms_complaint_list, true, null, null));
            menuModels.add(new MenuModel("Approvals", R.drawable.cms_approval_list, true, null, null));

            displaySelectedScreen("Complaints");
        } else {
            displaySelectedScreen("HOME");
        }
//        listView.setSelected(0);


        FrameLayout notificationLayout = findViewById(R.id.notificationLayout);
        textCartItemCount = findViewById(R.id.cart_badge);
        setupBadge();
        notificationLayout.setOnClickListener(view -> {
            Intent homeIntent = new Intent(this, NotificationActivity.class);
            startActivity(homeIntent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        /*openDrawer.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.LEFT);
            }
        });*/

        gpsLoaderLayout = findViewById(R.id.gpsLoaderLayout);

        if (isAttendanceRequired) {
            initPermission();
            checkExternalPermission();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    displaySelectedScreen("HOME");
                    break;
                case R.id.help:
                    Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(intent);
                    break;
                case R.id.notification:
                    Intent notificationIntent = new Intent(MainActivity.this, NotificationsActivity.class);
                    startActivity(notificationIntent);
                    break;
                case R.id.menu:
                    if (menuModels != null && menuModels.size() > 1) {
                        updateSubmenu();
                    }
                    break;
            }
            return true;
        });
        backArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void updateSubmenu() {
        submenuDialog = new Dialog(MainActivity.this);
        submenuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogSubmenusBinding dialogSubmenusBinding = DataBindingUtil.inflate(LayoutInflater.from(MainActivity.this), R.layout.dialog_submenus, null, false);
        submenuDialog.setContentView(dialogSubmenusBinding.getRoot());
        submenuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SubmenuAdapter submenuAdapter = new SubmenuAdapter(MainActivity.this, menuModels, this);
        dialogSubmenusBinding.submenuRcv.setAdapter(submenuAdapter);
        dialogSubmenusBinding.submenuRcv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        submenuDialog.show();
        submenuDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        submenuDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /*private void updateNavMenu(boolean isAttendanceRequired, boolean isCMSRequired, boolean isDiscountRequired, boolean isSwachhRequired, boolean isQcFailRequired, boolean isDrugRequired, boolean isSensingRequired, boolean isChampsRequired, boolean isApnaSurveyRequired, boolean isApnaRetroRequired) {
        ArrayList<MenuModel> attendanceMenuModel = new ArrayList<MenuModel>();
        attendanceMenuModel.add(new MenuModel("Attendance", R.drawable.attendance));
        attendanceMenuModel.add(new MenuModel("History", R.drawable.history));

        ArrayList<MenuModel> cmsMenuModel = new ArrayList<MenuModel>();
        cmsMenuModel.add(new MenuModel("Complaint Register", R.drawable.cms_complaint_register));
        cmsMenuModel.add(new MenuModel("Complaint List", R.drawable.cms_complaint_list));
        cmsMenuModel.add(new MenuModel("Approval List", R.drawable.cms_approval_list));

        ArrayList<MenuModel> discountMenuModel = new ArrayList<MenuModel>();
        discountMenuModel.add(new MenuModel("Pending", R.drawable.discount_pending));
        discountMenuModel.add(new MenuModel("Approved", R.drawable.discount_approved));
        discountMenuModel.add(new MenuModel("Rejected", R.drawable.discount_rejected));
        discountMenuModel.add(new MenuModel("Bill", R.drawable.discount_bill));

        ArrayList<MenuModel> drugRequestMenuModel = new ArrayList<MenuModel>();
        drugRequestMenuModel.add(new MenuModel("New Drug Request", R.drawable.new_drug_request));
        drugRequestMenuModel.add(new MenuModel("New Drug List", R.drawable.new_drug_list));

        ArrayList<MenuModel> omsQcMenuModel = new ArrayList<MenuModel>();
        omsQcMenuModel.add(new MenuModel("Dashboard", R.drawable.qc_dashboard));
        omsQcMenuModel.add(new MenuModel("Outstanding", R.drawable.qc_outstanding));
        omsQcMenuModel.add(new MenuModel("Approved", R.drawable.qc_approved));
        omsQcMenuModel.add(new MenuModel("Rejected", R.drawable.qc_rejected));

        ArrayList<MenuModel> swachhMenuModel = new ArrayList<MenuModel>();
        if (isSwachhRequired) {
            if ((employeeRole.equalsIgnoreCase("Yes")) && userDesignation != null && (userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO"))) {
                swachhMenuModel.add(new MenuModel("Upload", R.drawable.swachh_upload));
                swachhMenuModel.add(new MenuModel("List", R.drawable.swachh_list));
            } else if (employeeRole.equalsIgnoreCase("Yes")) {
                swachhMenuModel.add(new MenuModel("Upload", R.drawable.swachh_upload));
            } else if (userDesignation != null && userDesignation.equalsIgnoreCase("MANAGER") || userDesignation.equalsIgnoreCase("GENERAL MANAGER") || userDesignation.equalsIgnoreCase("EXECUTIVE") || userDesignation.equalsIgnoreCase("CEO")) {
                swachhMenuModel.add(new MenuModel("List", R.drawable.swachh_list));
            }
        }

        ArrayList<MenuModel> monitoringMenuModel = new ArrayList<MenuModel>();
        monitoringMenuModel.add(new MenuModel("Dashboard", R.drawable.monitoring_dashboard));

        ArrayList<MenuModel> champsMenuModel = new ArrayList<MenuModel>();
        champsMenuModel.add(new MenuModel("Champs Survey", R.drawable.champs_survey));
        champsMenuModel.add(new MenuModel("Champs Reports", R.drawable.champs_reports));
        champsMenuModel.add(new MenuModel("Champs Admin", R.drawable.champs_admin));

        ArrayList<MenuModel> planogramMenuModel = new ArrayList<MenuModel>();
        planogramMenuModel.add(new MenuModel("", R.drawable.planogram));

        ArrayList<MenuModel> apnaRetroMenuModel = new ArrayList<MenuModel>();
        if (isApnaRetroRequired) {
            if ((employeeRoleRetro.equalsIgnoreCase("Yes")) && (Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("MANAGER") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("CEO") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("GENERAL MANAGER"))) {
                apnaRetroMenuModel.add(new MenuModel("Creation", R.drawable.retro_creation));
                apnaRetroMenuModel.add(new MenuModel("Approval", R.drawable.retro_approval));
            } else if (Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("NODATA")) {
                apnaRetroMenuModel.add(new MenuModel("Creation", R.drawable.retro_creation));
            } else if (Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("MANAGER") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("CEO") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("GENERAL MANAGER")) {
                apnaRetroMenuModel.add(new MenuModel("Approval", R.drawable.retro_approval));
            }
        }

        ArrayList<MenuModel> apnaMenuModel = new ArrayList<MenuModel>();
        apnaMenuModel.add(new MenuModel("Apna Survey", R.drawable.apna_survey));

        LinearLayout sensingMenu = findViewById(R.id.apolloSensingMenu);
        if (isSensingRequired) {
            sensingMenu.setVisibility(View.VISIBLE);
        } else {
            sensingMenu.setVisibility(View.GONE);
        }

        LinearLayout attendanceMenu = findViewById(R.id.attendanceManagementMenu);
        if (isAttendanceRequired) {
            attendanceMenu.setVisibility(View.VISIBLE);
        } else {
            attendanceMenu.setVisibility(View.GONE);
        }

        LinearLayout cmsMenu = findViewById(R.id.cmsMenu);
        if (isCMSRequired) {
            cmsMenu.setVisibility(View.VISIBLE);
        } else {
            cmsMenu.setVisibility(View.GONE);
        }

        LinearLayout discountMenu = findViewById(R.id.discountMenu);
        if (isDiscountRequired) {
            discountMenu.setVisibility(View.VISIBLE);
        } else {
            discountMenu.setVisibility(View.GONE);
        }

        LinearLayout drugRequestMenu = findViewById(R.id.drugRequestMenu);
        if (isDrugRequired) {
            if (employeeRoleNewDrugRequest.equalsIgnoreCase("Yes")) {
                drugRequestMenu.setVisibility(View.VISIBLE);
            } else {
                drugRequestMenu.setVisibility(View.GONE);
            }
        } else {
            drugRequestMenu.setVisibility(View.GONE);
        }

        LinearLayout qcFailMenu = findViewById(R.id.omsQcMenu);
        if (isQcFailRequired) {
            qcFailMenu.setVisibility(View.VISIBLE);
        } else {
            qcFailMenu.setVisibility(View.GONE);
        }

        LinearLayout monitoringMenu = findViewById(R.id.monitoringMenu);
        if (ceoDashboardAccessFromEmployee.equalsIgnoreCase("Yes")) {
            monitoringMenu.setVisibility(View.VISIBLE);
        } else {
            monitoringMenu.setVisibility(View.GONE);
        }

        LinearLayout champsMenu = findViewById(R.id.champsMenu);
        if (isChampsRequired) {
            champsMenu.setVisibility(View.VISIBLE);
        } else {
            champsMenu.setVisibility(View.GONE);
        }

        LinearLayout apnaSurveyMenu = findViewById(R.id.apnaMenu);
        if (isApnaSurveyRequired) {
            apnaSurveyMenu.setVisibility(View.VISIBLE);
        } else {
            apnaSurveyMenu.setVisibility(View.GONE);
        }

        RecyclerView attendanceRcv = findViewById(R.id.attendanceManagementRcv);
        attendanceManagementAdapter = new MenuItemAdapter(this, this, attendanceMenuModel);
        attendanceRcv.setAdapter(attendanceManagementAdapter);
        attendanceRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView cmsRcv = findViewById(R.id.cmsRcv);
        cmsAdapter = new MenuItemAdapter(this, this, cmsMenuModel);
        cmsRcv.setAdapter(cmsAdapter);
        cmsRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView discountRcv = findViewById(R.id.discountRcv);
        discountAdapter = new MenuItemAdapter(this, this, discountMenuModel);
        discountRcv.setAdapter(discountAdapter);
        discountRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView drugRequestRcv = findViewById(R.id.newDrugRequestRcv);
        newDrugRequestAdapter = new MenuItemAdapter(this, this, drugRequestMenuModel);
        drugRequestRcv.setAdapter(newDrugRequestAdapter);
        drugRequestRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView qcRcv = findViewById(R.id.omsQcRcv);
        omsQcAdapter = new MenuItemAdapter(this, this, omsQcMenuModel);
        qcRcv.setAdapter(omsQcAdapter);
        qcRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView swachhRcv = findViewById(R.id.swachhRcv);
        swachhAdapter = new MenuItemAdapter(this, this, swachhMenuModel);
        swachhRcv.setAdapter(swachhAdapter);
        swachhRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView monitoringRcv = findViewById(R.id.monitoringRcv);
        monitoringAdapter = new MenuItemAdapter(this, this, monitoringMenuModel);
        monitoringRcv.setAdapter(monitoringAdapter);
        monitoringRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView champsRcv = findViewById(R.id.champsRcv);
        champsAdapter = new MenuItemAdapter(this, this, champsMenuModel);
        champsRcv.setAdapter(champsAdapter);
        champsRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView planogramRcv = findViewById(R.id.planogramRcv);
        planogramAdapter = new MenuItemAdapter(this, this, planogramMenuModel);
        planogramRcv.setAdapter(planogramAdapter);
        planogramRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView apnaRetroRcv = findViewById(R.id.apnaRetroRcv);
        apnaRetroAdapter = new MenuItemAdapter(this, this, apnaRetroMenuModel);
        apnaRetroRcv.setAdapter(apnaRetroAdapter);
        apnaRetroRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView apnaRcv = findViewById(R.id.apnaRcv);
        apnaAdapter = new MenuItemAdapter(this, this, apnaMenuModel);
        apnaRcv.setAdapter(apnaAdapter);
        apnaRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        sensingMenu.setOnClickListener(v -> {
            displaySelectedScreen("Apollo Sensing");
        });
    }*/


    private void setUp() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
//        handleAssistiveTouchWindow();
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
//                drawer.closeDrawer(GravityCompat.START);
            }
        } else {
            if (isHomeScreen) {
                finish();
            } else if (frg instanceof ComplainListFragment) {
                if (isCeoDashboard) {
                    super.onBackPressed();
                } else {
                    displaySelectedScreen("HOME");
                }
            } else {
                displaySelectedScreen("HOME");
//                drawer.closeDrawer(GravityCompat.START);
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

    private TicketCountsByStatusRoleResponse.Data.ListData.Row row;
    private String status;
    private String fromDate;
    private String toDate;

    public void displaySelectedScreenFromCeoDashboard(String itemName, TicketCountsByStatusRoleResponse.Data.ListData.Row row, String status, String fromDate, String toDate) {
        this.row = row;
        this.status = status;
        this.fromDate = fromDate;
        this.toDate = toDate;
        displaySelectedScreen(itemName);
    }

    @SuppressLint("ResourceAsColor")
    public void displaySelectedScreen(String itemName) {
        isCeoDashboard = false;
//        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if(isAllowFragmentChange &&(frg instanceof RegistrationFragment || frg instanceof  Drug)){
//            showAlertDialog(itemName);
//            return;
//        }


//        if (!itemName.equalsIgnoreCase("Greetings to Chairman")) {
//            currentItem = itemName;
//            if (previousItem.equals(currentItem) && !currentItem.equals("Logout")) {
//                return;
//            }
//        }
//        if (itemName.equalsIgnoreCase("HOME")) {
//            if (menuModels != null && !menuModels.isEmpty()) {
//                menuModels.clear();
//
//            }
//        }
        if (submenuDialog != null && submenuDialog.isShowing()) {
            submenuDialog.dismiss();
        }


//        if (itemName.equalsIgnoreCase("HOME")) {
//            headerText.setText("HOME");
//            fragment = new HomeFragment();
//            filterIcon.setVisibility(View.GONE);
//            headerTextLocation.setVisibility(View.GONE);
//            plusIconAttendence.setVisibility(View.GONE);
//            plusIconApna.setVisibility(View.GONE);
//            refreshIconQc.setVisibility(View.GONE);
//            onClickPlusIcon.setVisibility(View.GONE);
//            settingsWhite.setVisibility(View.GONE);
//            filterIconApna.setVisibility(View.GONE);
//            qcfilterIcon.setVisibility(View.GONE);
//            siteIdIcon.setVisibility(View.GONE);
//            scannerIcon.setVisibility(View.GONE);
//            spinnerLayout.setVisibility(View.GONE);
//            isHomeScreen = true;
//            riderNotificationLayout.setVisibility(View.GONE);
//            logo.setVisibility(View.VISIBLE);
//            customerDetails.setVisibility(View.VISIBLE);
//            backArrow.setVisibility(View.GONE);
//            headerText.setVisibility(View.GONE);
//            logoutBtn.setVisibility(View.VISIBLE);
//            toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
//            bottomNavigationView.setVisibility(View.GONE);
//            if (fragment != null) {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_out_to_right,R.anim.slide_out_to_left);
//                ft.replace(R.id.fragment_container, fragment);
//                ft.addToBackStack(null);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.commit();
////            drawer.closeDrawer(GravityCompat.START);
//
//
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Update UI elements after the animation is complete
////                        drawer.closeDrawer(GravityCompat.START);
//                    }
//                }, 1000);
//
//            }
//
//        }

        //initializing the fragment object which is selected
        switch (itemName) {
            case "HOME":
                headerText.setText("HOME");
//                fragment = new HomeFragment();
                filterIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("notificationModelResponse", notificationModelResponse);
                fragment = new HomeFragment();
                fragment.setArguments(bundle2);

                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = true;
                logo.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.GONE);
                headerText.setVisibility(View.GONE);
                logoutBtn.setVisibility(View.VISIBLE);
                helpIcon.setVisibility(View.VISIBLE);

//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                bottomNavigationView.setVisibility(View.GONE);
                switchBtn.setVisibility(View.GONE);

                break;

            case "Register Complaint":
                headerText.setText("Register Complaint");
                fragment = new RegistrationFragment();
                filterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                isAllowFragmentChange = true;
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
                headerTextLocation.setVisibility(View.GONE);
                //                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bgs));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
//                toolbar.setBackground(getResources().getDrawable(R.color.splash_start_color));
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Complaints":
                headerText.setText("Complaints");

                Bundle bundleComplaints = new Bundle();
                bundleComplaints.putString("UNIQUE_ID", uniqueId);
                ComplainListFragment fragInfoComplaints = new ComplainListFragment();
                fragInfoComplaints.setArguments(bundleComplaints);
                fragment = fragInfoComplaints;
                uniqueId = "";

//                fragment = new ComplainListFragment();


                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Approvals":
                headerText.setText("Approvals");
//                fragment = new ComplainListFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isFromApprovalList", true);
                ComplainListFragment fragInfo1 = new ComplainListFragment();
                fragInfo1.setArguments(bundle1);
                fragment = fragInfo1;
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "DASHBOARD_TICKET_LIST":
                headerText.setText("Dashboard Tickets");
                isCeoDashboard = true;
//                fragment = new ComplainListFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                Bundle bundleDashboardTickets = new Bundle();
                bundleDashboardTickets.putBoolean("IS_DASHBOARD_TICKET_LIST", true);
                bundleDashboardTickets.putSerializable("ROW", row);
                bundleDashboardTickets.putString("STATUS", status);
                bundleDashboardTickets.putString("FROM_DATE", fromDate);
                bundleDashboardTickets.putString("TO_DATE", toDate);

                ComplainListFragment dashboardTicketsFragmnet = new ComplainListFragment();
                dashboardTicketsFragmnet.setArguments(bundleDashboardTickets);
                fragment = dashboardTicketsFragmnet;
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.GONE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Attendance":
                headerText.setText("Attendance");
                fragment = new AttendanceFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.VISIBLE);
                helpIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "History":
                headerText.setText("History");
                fragment = new HistoryFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Pending":
                headerText.setText("Pending");
                fragment = new PendingOrderFragment();
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Approved":
                headerText.setText("Approved");
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                fragment = new ApprovedFragment();
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Rejected":
                headerText.setText("Rejected");
                fragment = new RejectedFragment();
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Bill":
                headerText.setText("Bill");
                fragment = new BillCompletedFragment();
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);
                //                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Swachh Images Upload":
                headerText.setText("Swachh Images");
                fragment = new SwacchImagesUploadFragment();
                filterIcon.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "Swachh List":
                headerText.setText("SWACHH LIST");
                fragment = new SwacchFragment();
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "New Swachh":
                headerText.setText("SWACHH LIST");
                fragment = new SampleSwachUi();
                filterIcon.setVisibility(View.VISIBLE);
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
                isUploadScreen = true;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Swachh Details":
                headerText.setText("SWACHH LIST");
                fragment = new SwachListFragment();
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
                isListScreen = true;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Request New Drug":  //"Drug Request":
                headerText.setText("Request New Drug");
                fragment = new Drug();
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                helpIcon.setVisibility(View.GONE);

                logoutBtn.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                isAllowFragmentChange = true;
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Greetings":
                Intent i = new Intent(this, GreetingActivity.class);
                refreshIconQc.setVisibility(View.GONE);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity(i);
                break;
            case "Cash Deposit":
                headerText.setText("Cash Deposit");
                fragment = new CashCloserFragment();
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIconApna.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(false);
                switchBtn.setVisibility(View.GONE);
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
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                helpIcon.setVisibility(View.GONE);

                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.apna_project_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
//                bottomNavigationView.setBackgroundColor(Color.parseColor("#0c273a"));
                break;

            case "Sensing":
                headerText.setText("Sensing");
                fragment = new ApolloSensingFragment();
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                qcfilterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(false);
                switchBtn.setVisibility(View.GONE);
                break;

            case "Retro QR":
                headerText.setText("Retro QR");
                fragment = new RetroQrFragment();
                filterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                onClickPlusIcon.setVisibility(View.GONE);

                qcfilterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.retro_qr_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(false);
                switchBtn.setVisibility(View.GONE);
                break;

            case "Drug Requests":  //"Drug List":
                headerText.setText("Drug Requests");
                fragment = new DrugListFragment();
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromDrugList", true);
                ComplainListFragment fragInfo = new ComplainListFragment();
                fragInfo.setArguments(bundle);
                fragment = fragInfo;
                filterIcon.setVisibility(View.VISIBLE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Dashboard":
                headerText.setText("Dashboard");
                fragment = new QcDashboard();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.VISIBLE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bgs));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Outstanding":
                headerText.setText("Pending List");
                fragment = new PendingFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
//                selectFilterType.setText("Rows: " +String.valueOf(Preferences.INSTANCE.getQcPendingPageSiz()));
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bgs));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;


            case "Evaluation":
                headerText.setText("Evaluation");
                fragment = new PlanogramFragment();
                qcfilterIcon.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "QC Approved":
                headerText.setText("Approved List");
                fragment = new com.apollopharmacy.vishwam.ui.home.qcfail.approved.ApprovedFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
//                selectFilterType.setText("Rows: "+(String.valueOf(Preferences.INSTANCE.getQcApprovedPageSiz())));
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bgs));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "QC Rejected":
                headerText.setText("Rejected List");
                fragment = new com.apollopharmacy.vishwam.ui.home.qcfail.rejected.RejectedFragment();
                qcfilterIcon.setVisibility(View.VISIBLE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

//                selectFilterType.setText("Rows: "+ String.valueOf(Preferences.INSTANCE.getQcRejectedPageSiz()));
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bgs));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

//            case "Select Site":
//                headerText.setText("Select Site ID");
//                fragment = new SelectSiteActivityy();
//                imageView.setVisibility(View.GONE);
//                break;
            case "Champs Survey":
                headerText.setText("Champs Survey");
                fragment = new GetSurveyDetailsListActivity();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.VISIBLE);
                if (!isAdminRequired.isEmpty() && isAdminRequired.equals("Yes")) {
                    settingsWhite.setVisibility(View.VISIBLE);
                } else {
                    settingsWhite.setVisibility(View.GONE);
                }

                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.VISIBLE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Champs Reports":
                headerText.setText("CHAMPS Analysis Reports");
                fragment = new ChampsReportsFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "Champs Admin":
                headerText.setText("Admin Module");
//                fragment = new AdminModuleFragment();
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "New Retro":
                headerText.setText("New Retro");
                Bundle bundlePreRectro = new Bundle();
                bundlePreRectro.putBoolean("fromPreRectro", true);
                PreRectroFragment fragPreRectro = new PreRectroFragment();
                fragPreRectro.setArguments(bundlePreRectro);
                fragment = fragPreRectro;
                refreshIconQc.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
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
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
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
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
                switchBtn.setVisibility(View.GONE);
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                break;

            case "Approval":
                headerText.setText("Pre Retro Approval");
                Bundle preRetroApprovalbundle = new Bundle();
                preRetroApprovalbundle.putBoolean("fromPreRectroApproval", true);
                PreRectroApprovalFragment fragPreRectroApproval = new PreRectroApprovalFragment();
                fragPreRectroApproval.setArguments(preRetroApprovalbundle);
                fragment = fragPreRectroApproval;
                qcfilterIcon.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                onClickPlusIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.VISIBLE);
                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;

            case "Post Rectro Approval":
                headerText.setText("Post Retro Approval");
                Bundle postRetroApprovalbundle = new Bundle();
                postRetroApprovalbundle.putBoolean("fromPostRectroApproval", true);
                PreRectroApprovalFragment fragPostRectroApproval = new PreRectroApprovalFragment();
                fragPostRectroApproval.setArguments(postRetroApprovalbundle);
                fragment = fragPostRectroApproval;
                qcfilterIcon.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
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
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;


            case "Assets":
                headerText.setText("Apollo Assets");
                fragment = new AssetsFragment();
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                scannerIcon.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(false);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Monitoring Dashboard":
                if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("ceo")) {
//                    headerText.setText("Ceo Dashboard");
                    headerText.setText("Dashboard");
                } else if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("region_head")) {
//                    headerText.setText("Region Head Dashboard");
                    headerText.setText("Dashboard");
                } else if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("store_manager")) {
//                    headerText.setText("Manager Dashboard");
                    headerText.setText("Dashboard");
                } else if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("store_executive")) {
//                    headerText.setText("Executive Dashboard");
                    headerText.setText("Dashboard");
                } else {
//                    headerText.setText("Ceo Dashboard");
                    headerText.setText("Dashboard");
                }

                isCeoDashboard = true;
                fragment = new CeoDashboardFragment();
                qcfilterIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Dashboard Regional Head":
                headerText.setText("Regional head Dashboard");
                fragment = new CeoDashboardFragment();
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Dashboard Store Manager":
                headerText.setText("Store Manager Dashboard");
                fragment = new CeoDashboardFragment();
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                isHomeScreen = false;
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);
                //                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Dashboard Store Executive":
                headerText.setText("Store Executive Dashboard");
                fragment = new CeoDashboardFragment();
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Dashboard Store Supervisor":
                headerText.setText("Store Supervisor Dashboard");
                fragment = new CeoDashboardFragment();
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "DashboardManager":
                headerText.setText("Manager Dashboard");
                fragment = new ManagerDashboardFragment();
                qcfilterIcon.setVisibility(View.GONE);
                refreshIconQc.setVisibility(View.GONE);
                headerTextLocation.setVisibility(View.GONE);
                plusIconAttendence.setVisibility(View.GONE);
                helpIcon.setVisibility(View.GONE);

                onClickPlusIcon.setVisibility(View.GONE);
                settingsWhite.setVisibility(View.GONE);
                plusIconApna.setVisibility(View.GONE);
                filterIconApna.setVisibility(View.GONE);
                filterIcon.setVisibility(View.GONE);
                siteIdIcon.setVisibility(View.GONE);
                scannerIcon.setVisibility(View.GONE);
                isHomeScreen = false;
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
                logo.setVisibility(View.GONE);
                customerDetails.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(menuModels.size() > 1);
                switchBtn.setVisibility(View.GONE);
                break;
            case "Community Advisor":
            headerText.setText("Community Advisor");
            fragment = new CommunityAdvisorFragment();
            filterIcon.setVisibility(View.GONE);
            onClickPlusIcon.setVisibility(View.GONE);
            settingsWhite.setVisibility(View.GONE);
            plusIconApna.setVisibility(View.VISIBLE);
            filterIconApna.setVisibility(View.GONE);
            refreshIconQc.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            customerDetails.setVisibility(View.GONE);
            backArrow.setVisibility(View.VISIBLE);
            headerText.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
            headerTextLocation.setVisibility(View.GONE);
            plusIconAttendence.setVisibility(View.GONE);
            helpIcon.setVisibility(View.GONE);

            qcfilterIcon.setVisibility(View.GONE);
            siteIdIcon.setVisibility(View.GONE);
            scannerIcon.setVisibility(View.GONE);
            spinnerLayout.setVisibility(View.GONE);
            isHomeScreen = false;
//            riderNotificationLayout.setVisibility(View.GONE);
//                toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.home_actionbar_bg));
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.getMenu().findItem(R.id.menu).setVisible(false);
            switchBtn.setVisibility(View.GONE);
            break;
            case "Logout":
                dialogExit();
                break;
        }
//        previousItem = itemName;
        //replacing the fragment
        if (fragment != null && itemName.equalsIgnoreCase("HOME")) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_to_right_new, R.anim.slide_to_left_new);

            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

        } else if (fragment != null && itemName.equalsIgnoreCase("DASHBOARD_TICKET_LIST")) {
            String backStateName = fragment.getClass().getName();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit);
            ft.add(R.id.fragment_container, fragment);
            ft.addToBackStack(backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } else if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
//            drawer.closeDrawer(GravityCompat.START);
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

        } else if (requestCode == GPS_REQUEST_CODE) {
            if (isGpsEnambled()) {
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


    static Animation anim;


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

    private void updateDynamicNavMenu(boolean isAttendanceRequired, boolean isCMSRequired, boolean isDiscountRequired, boolean isSwachhRequired, boolean isQcFailRequired, boolean isDrugRequired, boolean isSensingRequired, boolean isChampsRequired, boolean isApnaSurveyRequired, boolean isApnaRetroRequired) {
        listView.init(this).addHeaderModel(new HeaderModel("Home", R.drawable.ic_menu_home));

//        listView = findViewById(R.id.expandable_navigation);
//        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)listView.getLayoutParams();
//        params.leftMargin=20;
//        params.topMargin=5;

//        listView.addHeaderModel(new HeaderModel("Greetings to Chairman", Color.WHITE, false, R.drawable.ic_network__1___2_));

        /*listView.addHeaderModel(new HeaderModel("Cash Deposit", Color.WHITE, false, R.drawable.ic_apollo_pending));*/

        if (isSensingRequired) {
            listView.addHeaderModel(new HeaderModel("Apollo Sensing", Color.WHITE, false, R.drawable.ic_menu_champ));
        }
        listView.addHeaderModel(new HeaderModel("Retro QR", Color.WHITE, false, R.drawable.ic_menu_champ));

        if (isAttendanceRequired) {
            listView.addHeaderModel(new HeaderModel("Attendance Management", Color.WHITE, true, R.drawable.ic_menu_cms).addChildModel(new ChildModel("Attendance", R.drawable.ic_menu_reports)).addChildModel(new ChildModel("History", R.drawable.ic_menu_survey)));
        }
        if (isCMSRequired) {
            listView.addHeaderModel(new HeaderModel("CMS", Color.WHITE, true, R.drawable.ic_menu_cms).addChildModel(new ChildModel("Complaint Register", R.drawable.ic_apollo_complaint_register)).addChildModel(new ChildModel("Complaint List", R.drawable.ic_apollo_complaint_list)).addChildModel(new ChildModel("Approval List", R.drawable.ic_apollo_complaint_list)));
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

        if (ceoDashboardAccessFromEmployee.equalsIgnoreCase("Yes")) {
          /*  if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("ceo") || Preferences.INSTANCE.getRoleForCeoDashboard().equals("region_head") || Preferences.INSTANCE.getRoleForCeoDashboard().equals("store_manager") || Preferences.INSTANCE.getRoleForCeoDashboard().equals("store_executive")) {
                listView.addHeaderModel(new HeaderModel("Monitoring", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard", R.drawable.ic_apollo_dashboard)));
            }*/
            listView.addHeaderModel(new HeaderModel("Monitoring", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard", R.drawable.ic_apollo_dashboard)));
        }

        /*if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("ceo")) {
            listView.addHeaderModel(new HeaderModel("QC Dashboard CEO", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard Ceo", R.drawable.ic_apollo_dashboard)));
        } else if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("regional_head")) {
            listView.addHeaderModel(new HeaderModel("QC Dashboard CEO", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard Regional Head", R.drawable.ic_apollo_dashboard)));
        } else if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("store_manager")) {
            listView.addHeaderModel(new HeaderModel("QC Dashboard CEO", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard Store Manager", R.drawable.ic_apollo_dashboard)));
        } else if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("store_executive")) {
            listView.addHeaderModel(new HeaderModel("QC Dashboard CEO", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard Store Executive", R.drawable.ic_apollo_dashboard)));
        } else if (Preferences.INSTANCE.getRoleForCeoDashboard().equals("store_supervisor")) {
            listView.addHeaderModel(new HeaderModel("QC Dashboard CEO", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Dashboard Store Supervisor", R.drawable.ic_apollo_dashboard)));
        }*/

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
        if (isChampsRequired) {
            listView.addHeaderModel(new HeaderModel("Champs", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Champs Survey", R.drawable.ic_apollo_survey_68__1_)));
//            .addChildModel(new ChildModel("Champs Reports", R.drawable.ic_apollo_survey_report__1_)).addChildModel(new ChildModel("Champs Admin", R.drawable.ic_apollo_survey_admin))
        }
//        if (isChampsRequired) {
//            listView.addHeaderModel(new HeaderModel("Champs", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Champs Survey", R.drawable.ic_apollo_survey_68__1_)).addChildModel(new ChildModel("Champs Reports", R.drawable.ic_apollo_survey_report__1_)).addChildModel(new ChildModel("Champs Admin", R.drawable.ic_apollo_survey_admin)));
//        }
        listView.addHeaderModel(new HeaderModel("Planogram", Color.WHITE, true, R.drawable.ic_menu_qc_fall).addChildModel(new ChildModel("Planogram Evaluation", R.drawable.ic_apollo_survey_68__1_)));

//        listView.addHeaderModel(new HeaderModel("Apna Rectro", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Pre Rectro", R.drawable.ic_apollo_survey_68__1_)).addChildModel(new ChildModel("Post Rectro", R.drawable.ic_apollo_survey_report__1_)).addChildModel(new ChildModel("After Completion", R.drawable.ic_apollo_survey_admin)).addChildModel(new ChildModel("Pre Rectro Approval", R.drawable.ic_apollo_survey_68__1_)).addChildModel(new ChildModel("Post Rectro Approval", R.drawable.ic_apollo_survey_report__1_)).addChildModel(new ChildModel("After Completion Approval", R.drawable.ic_apollo_survey_admin)));

        if (isApnaRetroRequired) {
            if ((employeeRoleRetro.equalsIgnoreCase("Yes")) && (Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("MANAGER") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("CEO") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("GENERAL MANAGER"))) {
                listView.addHeaderModel(new HeaderModel("Apna Retro", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Creation", R.drawable.ic_apollo_survey_68__1_)).addChildModel(new ChildModel("Approval", R.drawable.ic_apollo_survey_68__1_)));
            } else if (Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("NODATA")) {
                listView.addHeaderModel(new HeaderModel("Apna Retro", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Creation", R.drawable.ic_apollo_survey_68__1_)));
            } else if (Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("MANAGER") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().contains("CEO") || Preferences.INSTANCE.getAppLevelDesignationApnaRetro().equals("GENERAL MANAGER")) {
                listView.addHeaderModel(new HeaderModel("Apna Retro", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Approval", R.drawable.ic_apollo_survey_68__1_)));
            }
        }

//        isApnaSurveyRequired = true;
        if (isApnaSurveyRequired) {
            listView.addHeaderModel(new HeaderModel("APNA", Color.WHITE, true, R.drawable.ic_menu_champ).addChildModel(new ChildModel("Apna Survey", R.drawable.ic_apollo_survey_68__1_)));
        }
        listView.build().addOnGroupClickListener((parent, v, groupPosition, id) -> {
            List<HeaderModel> listHeader = listView.getListHeader();
            if (listHeader.get(groupPosition).getTitle().equals("Home")) {
                listView.setSelected(groupPosition);
                displaySelectedScreen("HOME");
//                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Logout")) {
                displaySelectedScreen("Logout");
//                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Greetings to Chairman")) {
                displaySelectedScreen("Greetings to Chairman");
//                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Cash Deposit")) {
                displaySelectedScreen("Cash Deposit");
//                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Assets")) {
                displaySelectedScreen("Assets");
//                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Apollo Sensing")) {
                displaySelectedScreen("Apollo Sensing");
//                drawer.closeDrawer(GravityCompat.START);
            } else if (listHeader.get(groupPosition).getTitle().equals("Retro QR")) {
                displaySelectedScreen("Retro QR");
//                drawer.closeDrawer(GravityCompat.START);
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
                }
//                else if (childModelList.get(childPosition).getTitle().equals("Champs Reports")) {
//                    displaySelectedScreen("Champs Reports");
//                } else if (childModelList.get(childPosition).getTitle().equals("Champs Admin")) {
//                    displaySelectedScreen("Champs Admin");
//                }
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

            }//Monitoring
            else if (listHeader.get(groupPosition).getTitle().equals("Planogram")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Planogram Evaluation")) {
                    displaySelectedScreen("Planogram Evaluation");
                }
            }

            if (listHeader.get(groupPosition).getTitle().equals("Monitoring")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Dashboard")) {
                    displaySelectedScreen("DashboardCeo");
                }
            }
           /* if (listHeader.get(groupPosition).getTitle().equals("QC Dashboard CEO")) {
                List<ChildModel> childModelList = listHeader.get(groupPosition).getChildModelList();
                if (childModelList.get(childPosition).getTitle().equals("Dashboard Ceo")) {
                    displaySelectedScreen("DashboardCeo");
                } else if (childModelList.get(childPosition).getTitle().equals("Dashboard Regional Head")) {
                    displaySelectedScreen("Dashboard Regional Head");
                } else if (childModelList.get(childPosition).getTitle().equals("Dashboard Store Manager")) {
                    displaySelectedScreen("Dashboard Store Manager");
                } else if (childModelList.get(childPosition).getTitle().equals("Dashboard Store Executive")) {
                    displaySelectedScreen("Dashboard Store Executive");
                } else if (childModelList.get(childPosition).getTitle().equals("Dashboard Store Supervisor")) {
                    displaySelectedScreen("Dashboard Store Supervisor");
                }

            }*/


//            if (groupPosition == 1 && childPosition == 0) {
//                displaySelectedScreen("Pending");
//            } else if (groupPosition == 1 && childPosition == 1) {
//                displaySelectedScreen("Approved");
//            } else if (groupPosition == 1 && childPosition == 2) {
//                displaySelectedScreen("Rejected");
//            } else if (groupPosition == 1 && childPosition == 3) {
//                displaySelectedScreen("Bill");
//            }
//            drawer.closeDrawer(GravityCompat.START);
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

    public void updateQcListCount(String listSize) {
//        selectFilterType.setText("Rows: " + listSize);
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

    @Override
    public void onSelectApprovedFragment(String listSize) {
//       selectFilterType.setText("Rows: " + listSize);
    }

    @Override
    public void onSelectRejectedFragment() {

    }

    @Override
    public void onSelectPendingFragment() {

    }

    @Override
    public void onClickSpinnerLayout() {

    }

    @Override
    public void onClickSubmenuItem(String menuName, ArrayList<MenuModel> submenus, int position) {
        displaySelectedScreen(menuName);
    }

    @Override
    public void onclickHelpIcon() {
//        Intent intent = new Intent(this, HelpActivity.class);
//        startActivity(intent);
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
//            listView.setSelected(0);
        });
        noBtn.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    public void apolloSensingSiteVisiblity(boolean isSiteVisible) {
        if (isSiteVisible) {
            siteIdIcon.setVisibility(View.VISIBLE);
        } else {
            siteIdIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickPlusIcon() {

    }

    @Override
    public void onClickSettings() {

    }

    public void setSubmenu(ArrayList<MenuModel> menuModels) {
        this.menuModels = menuModels;
    }

    public void applyTheme() {
        recreate();
    }

    @Override
    public void onclickHelpIconH() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}


