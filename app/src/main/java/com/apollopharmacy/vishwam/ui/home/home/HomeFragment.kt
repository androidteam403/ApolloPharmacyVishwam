package com.apollopharmacy.vishwam.ui.home.home

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences.getAppLevelDesignationApnaRetro
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentHomeBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MenuItemAdapter
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt


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
    var userDesignation: String = ""
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


    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun retrieveViewModel(): HomeViewModel {

        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun setup() {
        hideLoading()

        val userData = LoginRepo.getProfile()
        if (userData != null) {
            viewBinding.designation.setText(userData.DESIGNATION)
            viewBinding.userName.setText(userData.EMPNAME)
            viewBinding.userId.setText("ID: " + userData.EMPID)
        }
        Utlis.hideLoading()
        hideLoading()

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
        employeeRole = MainActivity.mInstance.employeeRole
        userDesignation = MainActivity.userDesignation

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
            isApnaRetroRequired
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
    ) {
        val attendanceMenuModel = ArrayList<MenuModel>()

        if (false) {
            attendanceMenuModel.add(MenuModel("Cash Deposit", R.drawable.cash_deposite_menu, false))

        }

        if (false) {
            attendanceMenuModel.add(
                MenuModel(
                    "Greetings to Chairman",
                    R.drawable.greetings_menu,
                    false
                )
            )

        }
        if (isSensingRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Apollo Sensing",
                    R.drawable.apollo_sensing_menu,
                    isSensingRequired
                )
            )
        }
        if (true) {
            attendanceMenuModel.add(MenuModel("Retro QR", R.drawable.retro_qr_menu, true))
        }
        //isAttendanceRequired
        if (true) {
            attendanceMenuModel.add(
                MenuModel(
                    "Attendance",
                    R.drawable.attendance_menu,
                    isAttendanceRequired
                )
            )

        }
        if (isCMSRequired) {
            attendanceMenuModel.add(MenuModel("Complaint List", R.drawable.cms_menu, isCMSRequired))

        }
        if (isDiscountRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Pending",
                    R.drawable.discount_menu,
                    isDiscountRequired
                )
            )

        }
        if (isDrugRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "New Drug List",
                    R.drawable.raise_menu,
                    isDrugRequired
                )
            )

        }

        if (isQcFailRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Out Standing",
                    R.drawable.omc_qc_menu,
                    isQcFailRequired
                )
            )

        }
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
//                attendanceMenuModel.add(MenuModel("Upload", R.drawable.swachh_upload,true))
                attendanceMenuModel.add(MenuModel("List", R.drawable.swachh_menu, true))
            } else if (employeeRole.equals("Yes", ignoreCase = true)) {
                attendanceMenuModel.add(MenuModel("Upload", R.drawable.swachh_menu, true))
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
                attendanceMenuModel.add(MenuModel("List", R.drawable.swachh_menu, true))
            }

//            attendanceMenuModel.add(MenuModel("SWACHH", R.drawable.attendance,isSwachhRequired))

        }

        if (false) {
            attendanceMenuModel.add(MenuModel("Dashboard", R.drawable.monitoring_menu, false))

        }
        if (isChampsRequired) {
            attendanceMenuModel.add(
                MenuModel(
                    "Champs Survey",
                    R.drawable.champs_menu,
                    isChampsRequired
                )
            )

        }
        if (true) {
            attendanceMenuModel.add(
                MenuModel(
                    "Planogram Evaluation",
                    R.drawable.planogram_menu,
                    false
                )
            )

        }

        /*if(isQcFailRequired){
            attendanceMenuModel.add(MenuModel("Out Standing", R.drawable.omc_qc_menu,isQcFailRequired))

        }*/
        if (isApnaRetroRequired) {
            if (MainActivity.mInstance.employeeRoleRetro.equals(
                    "Yes",
                    ignoreCase = true
                ) && (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER")
            ) {
//                attendanceMenuModel.add(MenuModel("Creation", R.drawable.retro_creation,true))
                attendanceMenuModel.add(MenuModel("Approval", R.drawable.apna_store_menu, true))
            } else if (getAppLevelDesignationApnaRetro().contains("NODATA")) {
                attendanceMenuModel.add(MenuModel("Creation", R.drawable.apna_store_menu, true))
            } else if (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER"
            ) {
                attendanceMenuModel.add(MenuModel("Approval", R.drawable.apna_store_menu, true))
            }
//            attendanceMenuModel.add(MenuModel("APNA Store", R.drawable.attendance,isApnaRetroRequired))

        }

        if (isApnaSurveyRequired) {
            attendanceMenuModel.add(MenuModel("APNA", R.drawable.apna_menu, isApnaSurveyRequired))

        }


        val attendanceSubMenuModel = ArrayList<MenuModel>()
        attendanceSubMenuModel.add(MenuModel("Attendance", R.drawable.attendance, true))
        attendanceSubMenuModel.add(MenuModel("History", R.drawable.history, true))


        val cmsMenuModel = ArrayList<MenuModel>()
        cmsMenuModel.add(MenuModel("Complaint Register", R.drawable.cms_complaint_register, true))
        cmsMenuModel.add(MenuModel("Complaint List", R.drawable.cms_complaint_list, true))
        cmsMenuModel.add(MenuModel("Approval List", R.drawable.cms_approval_list, true))

        val discountMenuModel = ArrayList<MenuModel>()
        discountMenuModel.add(MenuModel("Pending", R.drawable.discount_pending, true))
        discountMenuModel.add(MenuModel("Approved", R.drawable.discount_approved, true))
        discountMenuModel.add(MenuModel("Rejected", R.drawable.discount_rejected, true))
        discountMenuModel.add(MenuModel("Bill", R.drawable.discount_bill, true))

        val drugRequestMenuModel = ArrayList<MenuModel>()
        drugRequestMenuModel.add(MenuModel("New Drug Request", R.drawable.new_drug_request, true))
        drugRequestMenuModel.add(MenuModel("New Drug List", R.drawable.new_drug_list, true))

        val omsQcMenuModel = ArrayList<MenuModel>()
        omsQcMenuModel.add(MenuModel("Qc Dashboard", R.drawable.qc_dashboard, true))
        omsQcMenuModel.add(MenuModel("Out Standing", R.drawable.qc_outstanding, true))
        omsQcMenuModel.add(MenuModel("Qc Approved", R.drawable.qc_approved, true))
        omsQcMenuModel.add(MenuModel("Qc Rejected", R.drawable.qc_rejected, true))

        val swachhMenuModel = ArrayList<MenuModel>()
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
        }

        val monitoringMenuModel = ArrayList<MenuModel>()
        monitoringMenuModel.add(MenuModel("Dashboard", R.drawable.monitoring_dashboard, true))

        val champsMenuModel = ArrayList<MenuModel>()
        champsMenuModel.add(MenuModel("Champs Survey", R.drawable.champs_survey, true))
//        champsMenuModel.add(MenuModel("Champs Reports", R.drawable.champs_reports))
//        champsMenuModel.add(MenuModel("Champs Admin", R.drawable.champs_admin))

        val planogramMenuModel = ArrayList<MenuModel>()
        planogramMenuModel.add(MenuModel("Planogram Evaluation", R.drawable.planogram, true))

        val apnaRetroMenuModel = ArrayList<MenuModel>()
        if (isApnaRetroRequired) {
            if (MainActivity.mInstance.employeeRoleRetro.equals(
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

        val apnaMenuModel = ArrayList<MenuModel>()
        apnaMenuModel.add(MenuModel("Apna Survey", R.drawable.apna_survey, true))






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


    }

    fun getDataManager(): SessionManager {
        return SessionManager(context);
    }

    override fun onFialureMessage(message: String) {

    }

    override fun onClickMenuItem(itemName: String?, menuModels: ArrayList<MenuModel>) {
        MainActivity.mInstance.setSubmenu(menuModels)
        if (itemName.equals("Dashboard", true)) {
            MainActivity.mInstance.displaySelectedScreen("DashboardCeo")
        } else {
            MainActivity.mInstance.displaySelectedScreen(itemName)
        }
    }
}