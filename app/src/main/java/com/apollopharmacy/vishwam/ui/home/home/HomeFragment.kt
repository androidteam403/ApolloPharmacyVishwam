package com.apollopharmacy.vishwam.ui.home.home

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
//            viewBinding.customerName.setText("Welcome, " + userData.EMPNAME)
//            viewBinding.customerID.setText("Emp ID: " + userData.EMPID)
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
        attendanceMenuModel.add(MenuModel("Attendance", R.drawable.attendance))
        attendanceMenuModel.add(MenuModel("History", R.drawable.history))

        val cmsMenuModel = ArrayList<MenuModel>()
        cmsMenuModel.add(MenuModel("Complaint Register", R.drawable.cms_complaint_register))
        cmsMenuModel.add(MenuModel("Complaint List", R.drawable.cms_complaint_list))
        cmsMenuModel.add(MenuModel("Approval List", R.drawable.cms_approval_list))

        val discountMenuModel = ArrayList<MenuModel>()
        discountMenuModel.add(MenuModel("Pending", R.drawable.discount_pending))
        discountMenuModel.add(MenuModel("Approved", R.drawable.discount_approved))
        discountMenuModel.add(MenuModel("Rejected", R.drawable.discount_rejected))
        discountMenuModel.add(MenuModel("Bill", R.drawable.discount_bill))

        val drugRequestMenuModel = ArrayList<MenuModel>()
        drugRequestMenuModel.add(MenuModel("New Drug Request", R.drawable.new_drug_request))
        drugRequestMenuModel.add(MenuModel("New Drug List", R.drawable.new_drug_list))

        val omsQcMenuModel = ArrayList<MenuModel>()
        omsQcMenuModel.add(MenuModel("QcDashboard", R.drawable.qc_dashboard))
        omsQcMenuModel.add(MenuModel("OutStanding", R.drawable.qc_outstanding))
        omsQcMenuModel.add(MenuModel("Qc Approved", R.drawable.qc_approved))
        omsQcMenuModel.add(MenuModel("Qc Rejected", R.drawable.qc_rejected))

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
                swachhMenuModel.add(MenuModel("Upload", R.drawable.swachh_upload))
                swachhMenuModel.add(MenuModel("List", R.drawable.swachh_list))
            } else if (employeeRole.equals("Yes", ignoreCase = true)) {
                swachhMenuModel.add(MenuModel("Upload", R.drawable.swachh_upload))
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
                swachhMenuModel.add(MenuModel("List", R.drawable.swachh_list))
            }
        }

        val monitoringMenuModel = ArrayList<MenuModel>()
        monitoringMenuModel.add(MenuModel("Dashboard", R.drawable.monitoring_dashboard))

        val champsMenuModel = ArrayList<MenuModel>()
        champsMenuModel.add(MenuModel("Champs Survey", R.drawable.champs_survey))
//        champsMenuModel.add(MenuModel("Champs Reports", R.drawable.champs_reports))
//        champsMenuModel.add(MenuModel("Champs Admin", R.drawable.champs_admin))

        val planogramMenuModel = ArrayList<MenuModel>()
        planogramMenuModel.add(MenuModel("Planogram Evaluation", R.drawable.planogram))

        val apnaRetroMenuModel = ArrayList<MenuModel>()
        if (isApnaRetroRequired) {
            if (MainActivity.mInstance.employeeRoleRetro.equals(
                    "Yes",
                    ignoreCase = true
                ) && (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER")
            ) {
                apnaRetroMenuModel.add(MenuModel("Creation", R.drawable.retro_creation))
                apnaRetroMenuModel.add(MenuModel("Approval", R.drawable.retro_approval))
            } else if (getAppLevelDesignationApnaRetro().contains("NODATA")) {
                apnaRetroMenuModel.add(MenuModel("Creation", R.drawable.retro_creation))
            } else if (getAppLevelDesignationApnaRetro().contains("EXECUTIVE") || getAppLevelDesignationApnaRetro() == "MANAGER" || getAppLevelDesignationApnaRetro().contains(
                    "CEO"
                ) || getAppLevelDesignationApnaRetro() == "GENERAL MANAGER"
            ) {
                apnaRetroMenuModel.add(MenuModel("Approval", R.drawable.retro_approval))
            }
        }

        val apnaMenuModel = ArrayList<MenuModel>()
        apnaMenuModel.add(MenuModel("Apna Survey", R.drawable.apna_survey))

        viewBinding.greetingsToChairmanMenu.visibility = View.GONE
        viewBinding.cashDepositMenu.visibility = View.GONE



        if (isSensingRequired) {
            viewBinding.apolloSensingMenu.visibility = View.VISIBLE
        } else {
            viewBinding.apolloSensingMenu.visibility = View.GONE
        }
        //isAttendanceRequired
        if (true) {
            viewBinding.attendanceManagementMenu.visibility = View.VISIBLE
        } else {
            viewBinding.attendanceManagementMenu.visibility = View.GONE
        }

        if (isCMSRequired) {
            viewBinding.cmsMenu.visibility = View.VISIBLE
        } else {
            viewBinding.cmsMenu.visibility = View.GONE
        }

        if (isDiscountRequired) {
            viewBinding.discountMenu.visibility = View.VISIBLE
        } else {
            viewBinding.discountMenu.visibility = View.GONE
        }

        if (isDrugRequired) {
            if (MainActivity.mInstance.employeeRoleNewDrugRequest.equals(
                    "Yes",
                    ignoreCase = true
                )
            ) {
                viewBinding.drugRequestMenu.visibility = View.VISIBLE
            } else {
                viewBinding.drugRequestMenu.visibility = View.GONE
            }
        } else {
            viewBinding.drugRequestMenu.visibility = View.GONE
        }

        if (isQcFailRequired) {
            viewBinding.omsQcMenu.visibility = View.VISIBLE
        } else {
            viewBinding.omsQcMenu.visibility = View.GONE
        }

        if (MainActivity.mInstance.ceoDashboardAccessFromEmployee.equals(
                "Yes",
                ignoreCase = true
            )
        ) {
            viewBinding.monitoringMenu.visibility = View.VISIBLE
        } else {
            viewBinding.monitoringMenu.visibility = View.GONE
        }

        if (isChampsRequired) {
            viewBinding.champsMenu.visibility = View.VISIBLE
        } else {
            viewBinding.champsMenu.visibility = View.GONE
        }


        if (isApnaRetroRequired) {
            viewBinding.apnaRetroMenu.visibility = View.VISIBLE
        } else {
            viewBinding.apnaRetroMenu.visibility = View.GONE
        }

        if (isApnaSurveyRequired) {
            viewBinding.apnaMenu.visibility = View.VISIBLE
        } else {
            viewBinding.apnaMenu.visibility = View.GONE
        }

        attendanceManagementAdapter =
            MenuItemAdapter(context, this, attendanceMenuModel)
        viewBinding.attendanceManagementRcv.adapter = attendanceManagementAdapter
        viewBinding.attendanceManagementRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        cmsAdapter = MenuItemAdapter(context, this, cmsMenuModel)
        viewBinding.cmsRcv.adapter = cmsAdapter
        viewBinding.cmsRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        discountAdapter = MenuItemAdapter(context, this, discountMenuModel)
        viewBinding.discountRcv.adapter = discountAdapter
        viewBinding.discountRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        newDrugRequestAdapter = MenuItemAdapter(context, this, drugRequestMenuModel)
        viewBinding.newDrugRequestRcv.adapter = newDrugRequestAdapter
        viewBinding.newDrugRequestRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        omsQcAdapter = MenuItemAdapter(context, this, omsQcMenuModel)
        viewBinding.omsQcRcv.adapter = omsQcAdapter
        viewBinding.omsQcRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        swachhAdapter = MenuItemAdapter(context, this, swachhMenuModel)
        viewBinding.swachhRcv.adapter = swachhAdapter
        viewBinding.swachhRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        monitoringAdapter = MenuItemAdapter(context, this, monitoringMenuModel)
        viewBinding.monitoringRcv.adapter = monitoringAdapter
        viewBinding.monitoringRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        champsAdapter = MenuItemAdapter(context, this, champsMenuModel)
        viewBinding.champsRcv.adapter = champsAdapter
        viewBinding.champsRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        planogramAdapter = MenuItemAdapter(context, this, planogramMenuModel)
        viewBinding.planogramRcv.adapter = planogramAdapter
        viewBinding.planogramRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        apnaRetroAdapter = MenuItemAdapter(context, this, apnaRetroMenuModel)
        viewBinding.apnaRetroRcv.adapter = apnaRetroAdapter
        viewBinding.apnaRetroRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        apnaAdapter = MenuItemAdapter(context, this, apnaMenuModel)
        viewBinding.apnaRcv.adapter = apnaAdapter
        viewBinding.apnaRcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewBinding.greetingsToChairmanMenu.setOnClickListener {
            MainActivity.mInstance.displaySelectedScreen("Greetings to Chairman")
        }
        viewBinding.cashDepositMenu.setOnClickListener {
            MainActivity.mInstance.displaySelectedScreen("Cash Deposit")
        }
        viewBinding.apolloSensingMenu.setOnClickListener {
            MainActivity.mInstance.displaySelectedScreen("Apollo Sensing")
        }
        viewBinding.retroQrMenu.setOnClickListener {
            MainActivity.mInstance.displaySelectedScreen("Retro QR")
        }
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