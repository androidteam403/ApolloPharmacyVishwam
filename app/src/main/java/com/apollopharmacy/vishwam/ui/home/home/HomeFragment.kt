package com.apollopharmacy.vishwam.ui.home.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.Preferences.getAppLevelDesignationApnaRetro
import com.apollopharmacy.vishwam.data.Preferences.getAppTheme
import com.apollopharmacy.vishwam.data.Preferences.getEmployeeRoleUid
import com.apollopharmacy.vishwam.data.Preferences.getRetroEmployeeRoleUid
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentHomeBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MenuItemAdapter
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.notification.NotificationsActivity
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import java.text.SimpleDateFormat
import java.time.LocalDateTime


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), HomeFragmentCallback {
    var isAttendanceRequired: Boolean = false
    var isCMSRequired: Boolean = false
    var isDiscountRequired: Boolean = false
    var isSwachhRequired: Boolean = false
    var isQcFailRequired: Boolean = false
    var isDrugRequired: Boolean = false
    var isSensingRequired: Boolean = false
    var isChampsRequired: Boolean = false
    var isApnaSurveyRequired: Boolean = false
    var isApnaRetroRequired: Boolean = false
    var employeeRole: String = ""
    var employeeRoleRetro: String = ""
    var userDesignation: String = ""
    var isDashboardRequired: Boolean = false
    var isRetroQrAppRequired: Boolean = false
    var isPlanogramAppRequired: Boolean = false
    var attendanceManagementAdapter: MenuItemAdapter? = null
    var cmsAdapter: MenuItemAdapter? = null
    var discountAdapter: MenuItemAdapter? = null
    var newDrugRequestAdapter: MenuItemAdapter? = null
    var omsQcAdapter: MenuItemAdapter? = null
    var swachhAdapter: MenuItemAdapter? = null
    var monitoringAdapter: MenuItemAdapter? = null
    var champsAdapter: MenuItemAdapter? = null
    var planogramAdapter: MenuItemAdapter? = null
    var apnaRetroAdapter: MenuItemAdapter? = null
    var apnaAdapter: MenuItemAdapter? = null
    var isEmployeeDetailsApiAvailable: Boolean = false
    private var notificationResponse: NotificationModelResponse? = null

    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun retrieveViewModel(): HomeViewModel {
        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {
        hideLoading()

        viewBinding.callback = this
        if (arguments?.getSerializable("notificationModelResponse") != null)
            notificationResponse =
                arguments?.getSerializable("notificationModelResponse") as NotificationModelResponse

        val userData = LoginRepo.getProfile()
        if (userData != null) {
            viewBinding.designation.setText(userData.DESIGNATION)
            viewBinding.userName.setText(userData.EMPNAME)
            viewBinding.userId.setText("ID: " + userData.EMPID)
        }
        Utlis.hideLoading()
        hideLoading()
        caluclateTimeDifference()

//        if (getDataManager().getRiderActiveStatus() == "Offline") {
//            viewModel.riderUpdateStauts(
//                getDataManager().getLoginToken(),
//                "Offline",
//                requireContext(),
//                this@HomeFragment
//            )
//        } else {
//            viewModel.riderUpdateStauts(
//                getDataManager().getLoginToken(),
//                "Online",
//                requireContext(),
//                this@HomeFragment
//            )
//        }

        //        selectFilterType.setOnClickListener(v -> {
//            if (mainActivityCallback != null) {
//                mainActivityCallback.onClickSpinnerLayout();
//            }
//        });
//        listView = findViewById(R.id.expandable_navigation);

//        drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();


//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

//        navigationView = findViewById(R.id.nav_view);
//        int colorInt = getResources().getColor(R.color.white);
//        ColorStateList csl = ColorStateList.valueOf(colorInt);
//        navigationView.setItemTextColor(csl);
//        TextView userNameText = navigationView.getHeaderView(0).findViewById(R.id.userName);
//        TextView idText = navigationView.getHeaderView(0).findViewById(R.id.id_for_menu);
//        navigationView.setNavigationItemSelectedListener(this);

        val appTheme = getAppTheme()
        if (appTheme.equals("DARK", true)) {
            enableDarkMode()
        } else if (appTheme.equals("LIGHT", true)) {
            enableLightMode()
        } else {
            enableDeviceMode()
        }

        viewBinding.darkModeLayout.setOnClickListener {
            Preferences.setAppTheme("DARK")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            enableDarkMode()
            MainActivity.mInstance.applyTheme()
        }
        viewBinding.lightModeLayout.setOnClickListener {
            Preferences.setAppTheme("LIGHT")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            enableLightMode()
            MainActivity.mInstance.applyTheme()
        }
        viewBinding.deviceModeLayout.setOnClickListener {
            Preferences.setAppTheme("DEVICE MODE")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            enableDeviceMode()
            MainActivity.mInstance.applyTheme()
        }

        val key = "blobfilesload"

        val encryptedUrl: String? =
            "cZm5NWe6ZyFdAND/ey5SBvzCn2o2jko3x1FMPE25e3NJhZyCBYvJwYx5A5YXspABZq1Qq8yrCQFolXVLXAlCNaMAN/9IvyXrb3d2oRBWy0gFQAUOwoAYvuk+tI4uqV5wQSidhz4l+IjDVyxhqwHwn9Fapzzp17jzkrs0ovVnj9UrcYrZ6I5oXqYKmVq94eRusj/vHGuY5YU1nA70HP10Zvwdgus0ckFwPdldomN9p3goBnFcnxXFcVnHgaLgDpOjDFjoG2ZTAMX92z0YYCLE3A==" //"OpyhspbP/YM14k2ir6LLBOtTwnSksuHUi6IflK5CI6oSa8uQC4GH+WWi/5WmEhng0Eksq2KL1rB08LOUFfmLCghazPsB1laA2nPFrYxlRK8lZhcAoupSCkWNA1D8qn20htUe2/xfrbSYaVI8W/ObMxNcOOGytjH23scXguFGJMMdRtcJgBH0UWGcMG3WqDxRANy9wDuBmYsx3FnzaNhb+FYJkN3LTcRc8JtOrKkBOw2dLyGKBoseVc/aonQAvf5pOW+YpskPHG5rd9NUGssJRw=="
        RijndaelCipherEncryptDecrypt().decrypt(encryptedUrl, key)

        val decryptUrl: String? =
            "https://pharmteststorage.blob.core.windows.net/test/vendor/SENSING/Apollo_20230713223659174.png?sv=2022-11-02&se=9999-12-31T23:59:59Z&sr=b&sp=r&sig=%2B%2FsZgYz2WC0lrLsNfSlZSztsNPFPcuh2hz%2FrvDKbQl8%3D"
        RijndaelCipherEncryptDecrypt().encrypt(decryptUrl, key)

        isAttendanceRequired = MainActivity.isAttendanceRequired
        isCMSRequired = MainActivity.isCMSRequired
        isDiscountRequired = MainActivity.isDiscountRequired
        isSwachhRequired = MainActivity.isSwachhRequired
        isQcFailRequired = MainActivity.isQcFailRequired
        isDrugRequired = MainActivity.isDrugRequired
        isSensingRequired = MainActivity.isSensingRequired
        isChampsRequired = MainActivity.isChampsRequired
        isApnaSurveyRequired = MainActivity.isApnaSurveyRequired
        isApnaRetroRequired = MainActivity.isApnaRetroRequired
        employeeRole = getEmployeeRoleUid()
        employeeRoleRetro = getRetroEmployeeRoleUid()
        userDesignation = MainActivity.userDesignation
        isDashboardRequired = MainActivity.isDashboardRequired
        isRetroQrAppRequired = MainActivity.isRetroQrAppRequired
        isPlanogramAppRequired = MainActivity.isPlanogramAppRequired
        isEmployeeDetailsApiAvailable = Preferences.getEmployeeApiAvailable()
        /*
        * isAttendanceRequired = accessDetails.getISATTENDENCEAPP(); //loginData.getIS_ATTANDENCEAPP();
                isCMSRequired = accessDetails.getISCMSAPP(); //loginData.getIS_CMSAPP();
                isDiscountRequired = accessDetails.getISDISCOUNTAPP(); //loginData.getIS_DISCOUNTAPP();
                isSwachhRequired = accessDetails.getISSWACHHAPP(); //loginData.getIS_SWACHHAPP();
                isQcFailRequired = accessDetails.getISQCFAILAPP(); //loginData.getIS_QCFAILAPP();
                isDrugRequired = accessDetails.getISNEWDRUGAPP(); //loginData.getIS_NEWDRUGAPP();
                isSensingRequired = accessDetails.getISSENSINGAPP(); //loginData.getIS_SENSINGAPP();
                isChampsRequired = accessDetails.getISCHAMPAPP();
                isApnaSurveyRequired = accessDetails.getISAPNAAPP();
                isApnaRetroRequired = accessDetails.getISAPNARETROAPP();
                isDashboardRequired = accessDetails.getISDASHBOARDAPP();
                isRetroQrAppRequired = accessDetails.getISRETROQRAPP();
                isPlanogramAppRequired = accessDetails.getISPLANAGRAMAPP();*/



        isAttendanceRequired = true
        updateNavMenu(
            isAttendanceRequired,
            isCMSRequired,
            isDiscountRequired,
            isSwachhRequired,
            isQcFailRequired,
            isDrugRequired,
            isSensingRequired,
            isChampsRequired,
            isApnaSurveyRequired,
            isApnaRetroRequired,
            isDashboardRequired,
            isRetroQrAppRequired,
            isPlanogramAppRequired
        );

        /*updateNavMenu(true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true);*/
        searchByModule()
    }

    private fun enableDeviceMode() {
        viewBinding.darkModeLayout.setBackgroundResource(R.drawable.unselected_theme_bg)
        viewBinding.darkModeIcon.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.theme_setting_icon
            )
        )
        viewBinding.darkModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )
        )

        viewBinding.lightModeLayout.setBackgroundResource(R.drawable.unselected_theme_bg)
//        viewBinding.lightModeIcon.setColorFilter(ContextCompat.getColor(requireContext(),
//            R.color.grey))
        viewBinding.lightModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )
        )

        viewBinding.deviceModeLayout.setBackgroundResource(R.drawable.selected_theme_bg)
        viewBinding.deviceModeIcon.setColorFilter(Color.parseColor("#fbb831"))
        viewBinding.deviceModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white_for_both
            )
        )
    }

    private fun enableLightMode() {
        viewBinding.darkModeLayout.setBackgroundResource(R.drawable.unselected_theme_bg)
//        viewBinding.darkModeIcon.setColorFilter(ContextCompat.getColor(requireContext(),
//            R.color.grey))
        viewBinding.darkModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )
        )

        viewBinding.lightModeLayout.setBackgroundResource(R.drawable.selected_theme_bg)
//        viewBinding.lightModeIcon.setColorFilter(ContextCompat.getColor(requireContext(),
//            R.color.yellow))
        viewBinding.lightModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white_for_both
            )
        )

        viewBinding.deviceModeLayout.setBackgroundResource(R.drawable.unselected_theme_bg)
//        viewBinding.deviceModeIcon.setColorFilter(ContextCompat.getColor(requireContext(),
//            R.color.grey))
        viewBinding.deviceModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )
        )
    }

    private fun enableDarkMode() {
        viewBinding.darkModeLayout.setBackgroundResource(R.drawable.selected_theme_bg)
        viewBinding.darkModeIcon.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.theme_setting_icon
            )
        )
        viewBinding.darkModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white_for_both
            )
        )

        viewBinding.lightModeLayout.setBackgroundResource(R.drawable.unselected_theme_bg)
//        viewBinding.lightModeIcon.setColorFilter(ContextCompat.getColor(requireContext(),
//            R.color.grey))
        viewBinding.lightModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )
        )

        viewBinding.deviceModeLayout.setBackgroundResource(R.drawable.unselected_theme_bg)
        viewBinding.deviceModeIcon.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.theme_setting_icon
            )
        )
        viewBinding.deviceModeText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )
        )
    }

    private fun updateNavMenu(
        isAttendanceRequired: Boolean,
        isCMSRequired: Boolean,
        isDiscountRequired: Boolean,
        isSwachhRequired: Boolean,
        isQcFailRequired: Boolean,
        isDrugRequired: Boolean,
        isSensingRequired: Boolean,
        isChampsRequired: Boolean,
        isApnaSurveyRequired: Boolean,
        isApnaRetroRequired: Boolean,
        isDashboardRequired: Boolean,
        isRetroQrAppRequired: Boolean,
        isPlanogramAppRequired: Boolean,
    ) {
        val attendanceMenuModel = ArrayList<MenuModel>()
        if (true) {
            attendanceMenuModel.add(
                MenuModel(
                    "Cash Deposit", R.drawable.cash_deposite_menu, false, null, "Cash Deposit"
                )
            )
        }
        if (false) {
            attendanceMenuModel.add(
                MenuModel(
                    "Greetings", R.drawable.greetings_menu, false, null, "Greetings"
                )
            )
        }
        if (isSensingRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Sensing", R.drawable.apollo_sensing_menu, isSensingRequired, null, "Sensing"
                )
            )
        }
        if (isRetroQrAppRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Retro QR", R.drawable.retro_qr_menu, true, null, "Retro QR"
                )
            )
        }


        if (false) {
            attendanceMenuModel.add(
                MenuModel(
                    "Vahan", R.drawable.deliveryvahan, true, null, "Vahan"
                )
            )
        }


        val attendanceSubMenuModel = ArrayList<MenuModel>()
        attendanceSubMenuModel.add(MenuModel("Attendance", R.drawable.attendance, true, null, null))
        attendanceSubMenuModel.add(MenuModel("History", R.drawable.history, true, null, null))
        if (isAttendanceRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Attendance",
                    R.drawable.attendance_menu,
                    isAttendanceRequired,
                    attendanceSubMenuModel,
                    "Attendance"
                )
            )
        }

        val cmsMenuModel = ArrayList<MenuModel>()
        cmsMenuModel.add(
            MenuModel(
                "Register Complaint", R.drawable.cms_complaint_register, true, null, null
            )
        )
        cmsMenuModel.add(MenuModel("Complaints", R.drawable.cms_complaint_list, true, null, null))
        cmsMenuModel.add(MenuModel("Approvals", R.drawable.cms_approval_list, true, null, null))
        if (isCMSRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Complaints", R.drawable.cms_menu, isCMSRequired, cmsMenuModel, "Complaints"
                )
            )

        }

        val discountMenuModel = ArrayList<MenuModel>()
        discountMenuModel.add(MenuModel("Pending", R.drawable.discount_pending, true, null, null))
        discountMenuModel.add(MenuModel("Approved", R.drawable.discount_approved, true, null, null))
        discountMenuModel.add(MenuModel("Rejected", R.drawable.discount_rejected, true, null, null))
        discountMenuModel.add(MenuModel("Bill", R.drawable.discount_bill, true, null, null))
        if (isDiscountRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Discount",
                    R.drawable.discount_menu,
                    isDiscountRequired,
                    discountMenuModel,
                    "Pending"
                )
            )

        }
        val drugRequestMenuModel = ArrayList<MenuModel>()
        drugRequestMenuModel.add(
            MenuModel(
                "Request New Drug", R.drawable.new_drug_request, true, null, null
            )
        )
        drugRequestMenuModel.add(
            MenuModel(
                "Drug Requests", R.drawable.new_drug_list, true, null, null
            )
        )
        if (isDrugRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "New Drug",
                    R.drawable.raise_menu,
                    isDrugRequired,
                    drugRequestMenuModel,
                    "Drug Requests"
                )
            )

        }

        val omsQcMenuModel = ArrayList<MenuModel>()
        omsQcMenuModel.add(MenuModel("Dashboard", R.drawable.qc_dashboard, true, null, null))
        omsQcMenuModel.add(MenuModel("Outstanding", R.drawable.qc_outstanding, true, null, null))
        omsQcMenuModel.add(MenuModel("QC Approved", R.drawable.qc_approved, true, null, null))
        omsQcMenuModel.add(MenuModel("QC Rejected", R.drawable.qc_rejected, true, null, null))
        if (isQcFailRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    " OMS QC", R.drawable.omc_qc_menu, isQcFailRequired, omsQcMenuModel, "Dashboard"
                )
            )

        }


        val swachhMenuModel = ArrayList<MenuModel>()
        if (isSwachhRequired) {
            if (isEmployeeDetailsApiAvailable && employeeRole.equals(
                    "Yes", ignoreCase = true
                ) && MainActivity.userDesignation != null && (MainActivity.userDesignation.equals(
                    "MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "GENERAL MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "EXECUTIVE", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "CEO", ignoreCase = true
                ))
            ) {
                swachhMenuModel.add(
                    MenuModel("New Swachh", R.drawable.swachh_upload, true, null, null),
                )
                swachhMenuModel.add(
                    MenuModel(
                        "Swachh Details", R.drawable.swachh_list, true, null, null
                    )
                )
            } else if (isEmployeeDetailsApiAvailable && employeeRole.equals(
                    "Yes",
                    ignoreCase = true
                )
            ) {
                swachhMenuModel.add(
                    MenuModel(
                        "New Swachh", R.drawable.swachh_upload, true, null, null
                    )
                )
            } else if (MainActivity.userDesignation != null && MainActivity.userDesignation.equals(
                    "MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "GENERAL MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "EXECUTIVE", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "CEO", ignoreCase = true
                )
            ) {
                swachhMenuModel.add(
                    MenuModel(
                        "Swachh Details", R.drawable.swachh_list, true, null, null
                    )
                )
            }
        }

        if (isSwachhRequired) {
            if (employeeRole.equals(
                    "Yes", ignoreCase = true
                ) && isEmployeeDetailsApiAvailable && MainActivity.userDesignation != null && (MainActivity.userDesignation.equals(
                    "MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "GENERAL MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "EXECUTIVE", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "CEO", ignoreCase = true
                ))
            ) {
//                attendanceMenuModel.add(MenuModel("Upload", R.drawable.swachh_upload,true))
                attendanceMenuModel.add(
                    MenuModel(
                        "Swachh", R.drawable.swachh_menu, true, swachhMenuModel, "Swachh Details"
                    )
                )
            } else if (isEmployeeDetailsApiAvailable && employeeRole.equals(
                    "Yes",
                    ignoreCase = true
                )
            ) {
                attendanceMenuModel.add(
                    MenuModel(
                        "Swachh", R.drawable.swachh_menu, true, swachhMenuModel, "New Swachh"
                    )
                )
            } else if (MainActivity.userDesignation != null && MainActivity.userDesignation.equals(
                    "MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "GENERAL MANAGER", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "EXECUTIVE", ignoreCase = true
                ) || MainActivity.userDesignation.equals(
                    "CEO", ignoreCase = true
                )
            ) {
                attendanceMenuModel.add(
                    MenuModel(
                        "Swachh", R.drawable.swachh_menu, true, swachhMenuModel, "Swachh Details"
                    )
                )
            }

//            attendanceMenuModel.add(MenuModel("SWACHH", R.drawable.attendance,isSwachhRequired))

        }
        if (isDashboardRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Monitoring Dashboard",
                    R.drawable.monitoring_menu,
                    false,
                    null,
                    "Monitoring Dashboard"
                )
            )

        }
        if (isChampsRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Champs", R.drawable.champs_menu, isChampsRequired, null, "Champs Survey"
                )
            )

        }
        if (isPlanogramAppRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Planogram", R.drawable.planogram_menu, false, null, "Evaluation"
                )
            )

        }

        /*if(isQcFailRequired){
            attendanceMenuModel.add(MenuModel("Out Standing", R.drawable.omc_qc_menu,isQcFailRequired))

        }*/

        val apnaRetroMenuModel = ArrayList<MenuModel>()
        if (isApnaRetroRequired) {
            if (employeeRoleRetro.equals(
                    "Yes", ignoreCase = true
                ) && isEmployeeDetailsApiAvailable && (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER")
            ) {
                apnaRetroMenuModel.add(
                    MenuModel(
                        "New Retro", R.drawable.apna_store_menu, true, null, null
                    )
                )
                apnaRetroMenuModel.add(
                    MenuModel(
                        "Approval", R.drawable.apna_store_menu, true, null, null
                    )
                )
            } else if (getAppLevelDesignationApnaRetro().contains("NODATA")) {
                apnaRetroMenuModel.add(
                    MenuModel(
                        "New Retro", R.drawable.apna_store_menu, true, null, null
                    )
                )
            } else if (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER"
            ) {
                apnaRetroMenuModel.add(
                    MenuModel(
                        "Approval", R.drawable.apna_store_menu, true, null, null
                    )
                )
            }
        }
        /*apnaRetroMenuModel.add(
            MenuModel(
                "New Retro", R.drawable.apna_store_menu, true, null, null
            )
        )*/

        if (isApnaRetroRequired) {
            if (employeeRoleRetro.equals(
                    "Yes", ignoreCase = true
                ) && isEmployeeDetailsApiAvailable && (getAppLevelDesignationApnaRetro().contains(
                    "EXECUTIVE"
                ) || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER")
            ) {
//                attendanceMenuModel.add(MenuModel("Creation", R.drawable.retro_creation,true))
                attendanceMenuModel.add(
                    MenuModel(
                        "Apna Retro",
                        R.drawable.apna_store_menu,
                        true,
                        apnaRetroMenuModel,
                        "Approval"
                    )
                )
            } else if (getAppLevelDesignationApnaRetro().contains("NODATA")) {
                attendanceMenuModel.add(
                    MenuModel(
                        "Apna Retro",
                        R.drawable.apna_store_menu,
                        true,
                        apnaRetroMenuModel,
                        "New Retro"
                    )
                )
            } else if (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER"
            ) {
                attendanceMenuModel.add(
                    MenuModel(
                        "Apna Retro",
                        R.drawable.apna_store_menu,
                        true,
                        apnaRetroMenuModel,
                        "Approval"
                    )
                )
            }
//            attendanceMenuModel.add(MenuModel("APNA Store", R.drawable.attendance,isApnaRetroRequired))

        }

        val apnaMenuModel = ArrayList<MenuModel>()
        apnaMenuModel.add(MenuModel("Apna Survey", R.drawable.apna_survey, true, null, null))
        if (isApnaSurveyRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Apna Survey",
                    R.drawable.apna_menu,
                    isApnaSurveyRequired,
                    apnaMenuModel,
                    "Apna Survey"
                )
            )

        }

        attendanceMenuModel.add(
            MenuModel(
                "Community Advisor",
                R.drawable.community_advisor,
                true,
                apnaRetroMenuModel,
                "Community Advisor"
            )
        )
        /* val attendanceSubMenuModel = ArrayList<MenuModel>()
         attendanceSubMenuModel.add(MenuModel("Attendance", R.drawable.attendance, true))
         attendanceSubMenuModel.add(MenuModel("History", R.drawable.history, true))*/


        /*val cmsMenuModel = ArrayList<MenuModel>()
        cmsMenuModel.add(MenuModel("Complaint Register", R.drawable.cms_complaint_register, true))
        cmsMenuModel.add(MenuModel("Complaint List", R.drawable.cms_complaint_list, true))
        cmsMenuModel.add(MenuModel("Approval List", R.drawable.cms_approval_list, true))*/

        /*val discountMenuModel = ArrayList<MenuModel>()
        discountMenuModel.add(MenuModel("Pending", R.drawable.discount_pending, true))
        discountMenuModel.add(MenuModel("Approved", R.drawable.discount_approved, true))
        discountMenuModel.add(MenuModel("Rejected", R.drawable.discount_rejected, true))
        discountMenuModel.add(MenuModel("Bill", R.drawable.discount_bill, true))*/

        /*val drugRequestMenuModel = ArrayList<MenuModel>()
        drugRequestMenuModel.add(MenuModel("New Drug Request", R.drawable.new_drug_request, true))
        drugRequestMenuModel.add(MenuModel("New Drug List", R.drawable.new_drug_list, true))*/

        /*val omsQcMenuModel = ArrayList<MenuModel>()
        omsQcMenuModel.add(MenuModel("Qc Dashboard", R.drawable.qc_dashboard, true))
        omsQcMenuModel.add(MenuModel("Out Standing", R.drawable.qc_outstanding, true))
        omsQcMenuModel.add(MenuModel("Qc Approved", R.drawable.qc_approved, true))
        omsQcMenuModel.add(MenuModel("Qc Rejected", R.drawable.qc_rejected, true))*/

        /* val swachhMenuModel = ArrayList<MenuModel>()
         if (isSwachhRequired) {
             if (employeeRole.equals(
                     "Yes",
                     ignoreCase = true
                 ) && MainActivity.userDesignation != null && (MainActivity.userDesignation.equals(
                     "MANAGER",
                     ignoreCase = true
                 ) || MainActivity.userDesignation.equals(
                     "GENERAL MANAGER",
                     ignoreCase = true
                 ) || MainActivity.userDesignation.equals(
                     "EXECUTIVE",
                     ignoreCase = true
                 ) || MainActivity.userDesignation.equals(
                     "CEO",
                     ignoreCase = true
                 ))
             ) {
                 swachhMenuModel.add(MenuModel("Upload", R.drawable.swachh_upload, true))
                 swachhMenuModel.add(MenuModel("List", R.drawable.swachh_list, true))
             } else if (employeeRole.equals("Yes", ignoreCase = true)) {
                 swachhMenuModel.add(MenuModel("Upload", R.drawable.swachh_upload, true))
             } else if (MainActivity.userDesignation != null && MainActivity.userDesignation.equals(
                     "MANAGER",
                     ignoreCase = true
                 ) || MainActivity.userDesignation.equals(
                     "GENERAL MANAGER",
                     ignoreCase = true
                 ) || MainActivity.userDesignation.equals(
                     "EXECUTIVE",
                     ignoreCase = true
                 ) || MainActivity.userDesignation.equals(
                     "CEO",
                     ignoreCase = true
                 )
             ) {
                 swachhMenuModel.add(MenuModel("List", R.drawable.swachh_list, true))
             }
         }*/

        val monitoringMenuModel = ArrayList<MenuModel>()
        monitoringMenuModel.add(
            MenuModel(
                "Monitoring Dashboard", R.drawable.monitoring_dashboard, true, null, null
            )
        )

        val champsMenuModel = ArrayList<MenuModel>()
        champsMenuModel.add(MenuModel("Champs Survey", R.drawable.champs_survey, true, null, null))
//        champsMenuModel.add(MenuModel("Champs Reports", R.drawable.champs_reports))
//        champsMenuModel.add(MenuModel("Champs Admin", R.drawable.champs_admin))

        val planogramMenuModel = ArrayList<MenuModel>()
        planogramMenuModel.add(MenuModel("Evaluation", R.drawable.planogram, true, null, null))

        /*val apnaRetroMenuModel = ArrayList<MenuModel>()
        if (isApnaRetroRequired) {
            if (employeeRoleRetro.equals(
                    "Yes",
                    ignoreCase = true
                ) && (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER")
            ) {
                apnaRetroMenuModel.add(MenuModel("Creation", R.drawable.apna_store_menu, true))
                apnaRetroMenuModel.add(MenuModel("Approval", R.drawable.apna_store_menu, true))
            } else if (getAppLevelDesignationApnaRetro().contains("NODATA")) {
                apnaRetroMenuModel.add(MenuModel("Creation", R.drawable.apna_store_menu, true))
            } else if (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER"
            ) {
                apnaRetroMenuModel.add(MenuModel("Approval", R.drawable.apna_store_menu, true))
            }
        }
*//*val apnaMenuModel = ArrayList<MenuModel>()
        apnaMenuModel.add(MenuModel("Apna Survey", R.drawable.apna_survey, true))*/





        if (attendanceMenuModel.size > 0) {
            viewBinding.newMenuRecycleview.layoutManager = GridLayoutManager(context, 4)
            attendanceManagementAdapter = MenuItemAdapter(
                context,
                this,
                attendanceMenuModel,
                attendanceSubMenuModel,
                cmsMenuModel,
                discountMenuModel,
                drugRequestMenuModel,
                omsQcMenuModel,
                swachhMenuModel,
                monitoringMenuModel,
                champsMenuModel,
                planogramMenuModel,
                apnaRetroMenuModel,
                apnaMenuModel
            )
            viewBinding.newMenuRecycleview.adapter = attendanceManagementAdapter
            noModuleFound(attendanceMenuModel.size)
        } else {
            noModuleFound(0)
        }
    }


    override fun onFialureMessage(message: String) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshData() {
        setup()
    }

    override fun onClickMenuItem(itemName: String?, menuModels: ArrayList<MenuModel>) {
        MainActivity.mInstance.setSubmenu(menuModels)
        MainActivity.mInstance.displaySelectedScreen(itemName)

        /*if (itemName.equals("Monitoring", true)) {
            MainActivity.mInstance.displaySelectedScreen("DashboardCeo")
        } else {
            MainActivity.mInstance.displaySelectedScreen(itemName)
        }*/
    }

    override fun noModuleFound(count: Int) {
        if (count > 0) {
            viewBinding.noModuleFoundLayout.visibility = View.GONE
            viewBinding.newMenuRecycleview.visibility = View.VISIBLE
        } else {
            viewBinding.noModuleFoundLayout.visibility = View.VISIBLE
            viewBinding.newMenuRecycleview.visibility = View.GONE
        }
    }

    override fun onclickNotificationIcon() {
        val intent = Intent(context, NotificationsActivity::class.java)
        intent.putExtra("notificationResponse", notificationResponse)
        startActivityForResult(intent, 990)
    }

    fun searchByModule() {
        viewBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changing.
                val newText = s.toString()
                // Do something with the new text.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has changed.
                val text = s.toString()
//                if (!text.isNullOrEmpty() && text.length > 0) {
                if (attendanceManagementAdapter != null) {
                    attendanceManagementAdapter!!.filter.filter(text)
                }
//                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun caluclateTimeDifference() {
        if (!Preferences.getNotificationTime().isNullOrEmpty()) {
            if (notificationResponse != null && notificationResponse!!.data != null && notificationResponse!!.data!!.listData != null
                && notificationResponse!!.data!!.listData!!.rows != null && notificationResponse!!.data!!.listData!!.rows!!.size > 0
            ) {
                notificationResponse!!.data!!.listData!!.rows!!.sortedByDescending { it.createdTime }
//            val strDate = Preferences.getNotificationTime()
//            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
//            val date = dateFormat.parse(strDate)
//            val dateNewFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
//            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            val date1: LocalDate = LocalDate.parse(dateNewFormat, formatter)
//            val date2: LocalDate = LocalDate.parse(notificationResponse!!.data!!.listData!!.rows!!.get(0).createdTime, formatter)
//            if (date2.isBefore(date1)) {
//                System.out.println("date1 is before date2");
//                viewBinding.notificationText.visibility=View.GONE
//            } else if (date2.isAfter(date1)) {
//                viewBinding.notificationText.visibility=View.VISIBLE
//            } else {
//                System.out.println("date1 and date2 are equal");
//                viewBinding.notificationText.visibility=View.GONE
//            }

//            val currentDateTime: LocalDateTime = LocalDateTime.now()
//            var pastDateTimeStr = notificationResponse!!.data!!.listData!!.rows!!.get(0).createdTime
                val strDate = Preferences.getNotificationTime()
                val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS").format(date)
                val lastNotificationTime = LocalDateTime.parse(dateNewFormat)

                val strDates = notificationResponse!!.data!!.listData!!.rows!!.get(0).createdTime
                val dateFormats = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                val dates = dateFormats.parse(strDates)
                val dateNewFormats = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS").format(dates)
                val firstCreatedTime =
                    LocalDateTime.parse(dateNewFormats)

                if (firstCreatedTime.isBefore(lastNotificationTime)) {
                    viewBinding.notificationText.visibility = View.GONE
                    System.out.println("Current date and time is before the past date and time.");
                } else if (firstCreatedTime.isAfter(lastNotificationTime)) {
                    System.out.println("Current date and time is after the past date and time.");
                    viewBinding.notificationText.visibility = View.VISIBLE
                } else {
                    System.out.println("Current date and time is equal to the past date and time.");
                    viewBinding.notificationText.visibility = View.GONE
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 990 && resultCode == Activity.RESULT_OK) {
            caluclateTimeDifference()
        }
    }
}