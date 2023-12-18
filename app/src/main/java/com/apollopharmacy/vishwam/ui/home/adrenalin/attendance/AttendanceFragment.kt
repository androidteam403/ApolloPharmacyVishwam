package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.adrenalin.attendance

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.androidisland.ezpermission.EzPermission
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.attendance.*
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DialogDeleteBinding
import com.apollopharmacy.vishwam.databinding.FragmentAttendanceBinding
import com.apollopharmacy.vishwam.databinding.ViewTaskItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivity.isAtdLogout
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.AttendanceFragmentCallback
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.adapter.AttendenceImageRecycleView
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.fileupload.AttendenceFileUpload
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.fileupload.AttendenceFileUploadCallback
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.fileupload.AttendenceFileUploadModel
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.DoctorListRequest
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.DoctorListResponse
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.SiteListRequest
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.SiteListResponse
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.AttendenceSubTaskDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DoctorListDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.SiteDialogAttendence
import com.apollopharmacy.vishwam.util.LocationUtil
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utils.*
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import com.google.android.material.textfield.TextInputLayout
import com.waheed.location.updates.livedata.LocationViewModel
import me.echodev.resizer.Resizer
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AttendanceFragment() : BaseFragment<AttendanceViewModel, FragmentAttendanceBinding>(),
    SiteDialogAttendence.NewDialogSiteClickListner, DoctorListDialog.NewDialogSiteClickListner,
    MainActivityCallback, AttendenceSubTaskDialog.GstDialogClickListner,
    AttendenceFileUploadCallback,

    ImageClickListener, AttendanceFragmentCallback {

    val TAG = "AttendanceFragment"
    lateinit var adapter: TaskRecyclerView
    lateinit var userData: LoginDetails
    var employeeID: String = ""
    var branchName: String = ""
    var imageList = ArrayList<Image>()

    lateinit var imageAdapter: AttendenceImageRecycleView
    private lateinit var cameraIcon: ImageView
    val MARKETING_TASK_LIST: ArrayList<String> = ArrayList()
    var fileUploadModelList = ArrayList<AttendenceFileUploadModel>()
    var imageUrlsList = ArrayList<String>()
    val MARKETING_SUBTASK_LIST: ArrayList<String> = ArrayList()
    var lastLogDateTime: String = ""
    var taskAlreadyAvailable: Boolean = false
    var taskAvailableId: String = ""
    var REQUEST_CODE_CAMERA = 2234243
    var REQUEST_CODE_MARKETING = 2234241

    var imageFromCameraFile: File? = null
    private var fileArrayList = ArrayList<ImageDataDto>()
    var imagePathToServer: String = ""
    var isPermissionAsked: Boolean = false
    lateinit var departmentList: ArrayList<DepartmentListRes.DepartmentItem>
    val DEPT_LIST: ArrayList<String> = ArrayList()

    var siteIdList = ArrayList<SiteListResponse.Site>()
    var doctorList = ArrayList<DoctorListResponse.Doctor>()
    var marketingSubTaskList: ArrayList<DepartmentSubTaskResponse.Subtask> = ArrayList()

    lateinit var departmentTaskList: ArrayList<DepartmentTaskListRes.DepartmentTaskItem>
    val DEPT_TASK_LIST: ArrayList<String> = ArrayList()
    val dept_List_Map = HashMap<Int, String>()
    private lateinit var deptTaskSpinner: Spinner
    private lateinit var siteId: TextInputLayout
    private lateinit var marketingTaskSpinner: Spinner
    private lateinit var imageRecycleView: RecyclerView
    private lateinit var marketingSubBranchSpinner: Spinner
    private lateinit var description: TextInputLayout
    private lateinit var captureLayout: LinearLayout
    var subTaskName: String = ""
    var imageUrls: String = ""
    private lateinit var closeIcon: ImageView

    private lateinit var doctorSpecialist: EditText
    var comment: String = ""
    var siteIds: String = ""
    var doctorNAme: String = ""

    private lateinit var siteIdText: EditText
    private lateinit var doctorId: TextInputLayout
    private lateinit var doctorText: EditText
    var isFirstTime: Boolean = false

    var isDepartmentSelected: Boolean = false
    var isDepartmentTaskSelected: Boolean = false
    var enteredTaskName: String = ""
    var taskName: Boolean = false
    val params = MainActivity.mInstance.headerText!!.layoutParams as RelativeLayout.LayoutParams
    var isMarketingSelected: Boolean = false
    private lateinit var descriptionText: EditText

    val LOCATION_PERMISSION_REQUEST = 101
    private lateinit var locationViewModel: LocationViewModel
    private var isGPSEnabled = false
    var locationLatitude: String = ""
    var locationLongitude: String = ""

    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    lateinit var locationManager: LocationManager

    override val layoutRes: Int
        get() = R.layout.fragment_attendance

    override fun retrieveViewModel(): AttendanceViewModel {
        return AttendanceViewModel()
    }

    override fun setup() {
        viewBinding.attendanceViewModel = viewModel
        MainActivity.mInstance.mainActivityCallback = this
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        MainActivity.mInstance.headerText.textSize = 18f

        userData = LoginRepo.getProfile()!!
        employeeID = userData.EMPID

        MainActivity.mInstance.plusIconAttendence.setOnClickListener {
            if (taskAlreadyAvailable) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_close_task),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.dialog_task_signin)
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
                isDepartmentSelected = false
                isDepartmentTaskSelected = false
                enteredTaskName = ""
//                val createTaskEditText = dialog.findViewById<TextView>(R.id.createTaskText)
                val signInLayout = dialog.findViewById<LinearLayout>(R.id.singInLayout)
                val deptSpinner = dialog.findViewById<Spinner>(R.id.dept_spinner)
                deptTaskSpinner = dialog.findViewById<Spinner>(R.id.task_spinner)
                siteId = dialog.findViewById<TextInputLayout>(R.id.siteIdAttendence)
                doctorSpecialist = dialog.findViewById<EditText>(R.id.doctorspecialityinattendence)
                siteIdText = dialog.findViewById<EditText>(R.id.siteIdSelectAttendence)
                doctorText = dialog.findViewById<EditText>(R.id.doctorNameSelectAttendence)
                deptTaskSpinner = dialog.findViewById<Spinner>(R.id.task_spinner)
                marketingTaskSpinner = dialog.findViewById<Spinner>(R.id.marketing_spinner)
                doctorId = dialog.findViewById<TextInputLayout>(R.id.doctornameAttendence)
                cameraIcon = dialog.findViewById<ImageView>(R.id.addImage)
                imageRecycleView = dialog.findViewById<RecyclerView>(R.id.imageRecyclerView)
                marketingSubBranchSpinner = dialog.findViewById<Spinner>(R.id.sub_marketing_spinner)
                description = dialog.findViewById<TextInputLayout>(R.id.description)
                descriptionText = dialog.findViewById<EditText>(R.id.descriptionText)

                captureLayout = dialog.findViewById<LinearLayout>(R.id.capture_upload_layout)
                closeIcon = dialog.findViewById<ImageView>(R.id.close_icon)


                marketingSubBranchSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parentView: AdapterView<*>?,
                            selectedItemView: View?,
                            position: Int,
                            id: Long,
                        ) {
                            // Get the selected item from the Spinner and update the TextView
                            subTaskName = MARKETING_SUBTASK_LIST[position]!!
                            if (subTaskName.equals("Select SubTask")) {

                            } else {
                                captureLayout.visibility = View.VISIBLE
                                description.visibility = View.VISIBLE
                            }

                        }

                        override fun onNothingSelected(parentView: AdapterView<*>?) {
                            // Do nothing if nothing is selected
                        }
                    }



                closeIcon.setOnClickListener {
                    dialog.dismiss()
                    imageList.clear()
                }
                cameraIcon.setOnClickListener {
                    if (imageList.size>= 4) {

                        Toast.makeText(
                            requireContext(),
                            "You can only Capture up to 4 images",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        handleTaskCameraFunctionality()

                    }
                }

                siteIdText.setOnClickListener {
                    SiteDialogAttendence().apply {
                        arguments =
                            SiteDialogAttendence().generateParsedData(siteIdList)
                    }.show(childFragmentManager, "")
                }

                doctorText.setOnClickListener {
                    DoctorListDialog().apply {
                        arguments = DoctorListDialog().generateParsedData(doctorList)
                    }.show(childFragmentManager, "")
                }

                DEPT_LIST.clear()




                DEPT_LIST.add("Select Department")
                for (item in departmentList) {
                    dept_List_Map.put(Integer.parseInt(item.ID), item.DEPARTMENT)
                    DEPT_LIST.add(item.DEPARTMENT)


                }
                if (deptSpinner != null) {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.view_spinner_item, R.id.spinner_text, DEPT_LIST
                    )
                    deptSpinner.adapter = adapter

                    deptSpinner.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View, position: Int, id: Long,
                        ) {


                            if (getKeyFromValue(dept_List_Map, DEPT_LIST[position]) != 0) {
                                isDepartmentSelected = true
                                isDepartmentTaskSelected = false
                                enteredTaskName = DEPT_LIST[position]

                                if (DEPT_LIST[position].equals("Marketing")) {
                                    taskName = true

                                    val adapter = ArrayAdapter(
                                        requireContext(),
                                        R.layout.view_spinner_item,
                                        R.id.spinner_text,
                                        DEPT_TASK_LIST
                                    )

                                    marketingSubBranchSpinner.visibility = View.GONE
                                    description.visibility = View.GONE
                                    captureLayout.visibility = View.GONE
                                    marketingTaskSpinner.adapter = adapter
                                    marketingTaskSpinner.visibility = View.VISIBLE
                                    deptTaskSpinner.visibility = View.GONE
                                    siteId.visibility = View.GONE
                                    doctorId.visibility = View.GONE

                                } else if (DEPT_LIST[position].equals("DR CONNECT")) {
                                    imageList.clear()

                                    siteIdText.setText("")
                                    marketingTaskSpinner.visibility = View.GONE
                                    description.visibility = View.GONE
                                    captureLayout.visibility = View.GONE

                                    marketingSubBranchSpinner.visibility = View.GONE
                                    taskName = true
                                } else {
                                    branchName = ""
                                    siteIdText.setText("")
                                    doctorText.setText("")
                                    siteId.visibility = View.GONE
                                    doctorId.visibility = View.GONE
                                    description.visibility = View.GONE
                                    captureLayout.visibility = View.GONE
                                    imageList.clear()

                                    marketingSubBranchSpinner.visibility = View.GONE
                                    marketingTaskSpinner.visibility = View.GONE

                                    doctorSpecialist.visibility = View.GONE
                                    taskName = false
                                    isMarketingSelected = false
                                }

                                viewModel.getDepartmentTaskList(
                                    getKeyFromValue(
                                        dept_List_Map,
                                        DEPT_LIST[position]
                                    ) as Int
                                )
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            isDepartmentSelected = false
                            // write code to perform some action

                        }
                    }
                }


                marketingTaskSpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long,
                    ) {


                        if (position != 0) {


                        }


                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        isDepartmentTaskSelected = false
                        // write code to perform some action
                    }
                }




                deptTaskSpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long,
                    ) {


                        for (i in departmentTaskList.indices) {

                            if (DEPT_TASK_LIST.get(position)
                                    .equals(departmentTaskList.get(i).TASK)
                            ) {


                                viewModel.getSubTaskList(departmentTaskList.get(i).ID.toInt())
                            }


                            description.visibility = View.GONE
                            captureLayout.visibility = View.GONE

                        }
                        if (position != 0) {


                            if (taskName && DEPT_TASK_LIST.get(position).equals("BRANCH VISIT")) {
                                branchName = "BRANCH VISIT"
                                showLoading()
                                siteIdText.setText("")
                                doctorText.setText("")
                                doctorSpecialist.setText("")
                                imageList.clear()

                                doctorSpecialist.visibility = View.GONE

                                doctorId.visibility = View.GONE

                                siteId.visibility = View.VISIBLE
                                val siteListRequest = SiteListRequest("SITELIST", "")
                                viewModel.siteListResponse(siteListRequest)
                            } else if (taskName && DEPT_TASK_LIST.get(position)
                                    .equals("DOCTOR VISIT")
                            ) {
                                branchName = "DOCTOR VISIT"
                                showLoading()
                                siteIdText.setText("")

                                siteId.visibility = View.VISIBLE
                                val siteListRequest = SiteListRequest("SITELIST", "")
                                viewModel.siteListResponse(siteListRequest)
                            } else {
                                branchName = ""
                                siteIdText.setText("")
                                doctorText.setText("")
                                siteId.visibility = View.GONE
                                doctorId.visibility = View.GONE

                                doctorSpecialist.visibility = View.GONE
                            }


                            if (enteredTaskName.contains("-")) {
                                enteredTaskName = enteredTaskName.substring(
                                    0,
                                    enteredTaskName.indexOf("-") - 1
                                ) + " - " + DEPT_TASK_LIST[position]

                            } else {
                                enteredTaskName = enteredTaskName + " - " + DEPT_TASK_LIST[position]

                            }
                            isDepartmentTaskSelected = true
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        isDepartmentTaskSelected = false
                        // write code to perform some action
                    }
                }



                signInLayout.setOnClickListener { v1: View? ->


                    if (!isDepartmentSelected) {
                        Toast.makeText(
                            requireContext(),
                            "Please Select Department",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    } else if (DEPT_TASK_LIST.size > 0 && !isDepartmentTaskSelected) {
                        Toast.makeText(
                            requireContext(),
                            "Please Select Task",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    } else if (enteredTaskName.toLowerCase().contains("marketing")) {
                        if (subTaskName.isEmpty() || subTaskName.equals("Select SubTask")) {
                            Toast.makeText(
                                requireContext(),
                                "Please Select Subtask",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (imageList.size < 1) {
                            Toast.makeText(
                                requireContext(),
                                "Please Capture Image",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            for (i in imageList.distinct().indices) {
                                if (imageList[i].file != null) {
                                    var fileUploadModel = AttendenceFileUploadModel()
                                    fileUploadModel.file =
                                        compresImageSize(imageList.get(i).file)
//                            fileUploadModel.categoryId = i.categoryId
                                    fileUploadModelList.add(fileUploadModel)
                                }
                            }
                            AttendenceFileUpload().uploadFiles(
                                requireContext(),
                                this,
                                fileUploadModelList, dialog
                            )
                        }
                    } else if (validationCheck()) {

                        Utils.printMessage("TAG", "Entered Task :: " + enteredTaskName)
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (locationLatitude.isNotEmpty() && locationLongitude.isNotEmpty()
                            ) {
                                if (NetworkUtil.isNetworkConnected(requireContext())) {
                                    showLoading()
                                    viewModel.taskInsertUpdateService(
                                        TaskInfoReq(
                                            enteredTaskName,
                                            employeeID,
                                            "",
                                            locationLatitude,
                                            locationLongitude,
                                            "SIGNIN",
                                            getAttendanceCity(
                                                requireContext(),
                                                locationLatitude.toDouble(),
                                                locationLongitude.toDouble()
                                            ),
                                            descriptionText.text.toString(),
                                            siteIds,
                                            doctorNAme,
                                            imageUrls,
                                            subTaskName
                                        )
                                    )
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        context?.resources?.getString(R.string.label_network_error),
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } else {
                                (activity as MainActivity).initPermission()
                                (activity as MainActivity).startLocationUpdates()
                            }
                        } else {
                            (activity as MainActivity).initPermission()
                            (activity as MainActivity).startLocationUpdates()
                        }
                    }
                }
            }

        }
        viewBinding.capturedImg.setOnClickListener {
            handleCameraFunctionality()
        }




        locationManager =
            (requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?)!!

        // Instance of LocationViewModel
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        //Check weather Location/GPS is ON or OFF
        LocationUtil(requireContext()).turnGPSOn(object :
            LocationUtil.OnLocationOnListener {

            override fun locationStatus(isLocationOn: Boolean) {
                isGPSEnabled = isLocationOn
            }
        })

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
            viewModel.getLastLogin(employeeID)
        } else {
            Toast.makeText(
                requireContext(),
                context?.resources?.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }



        viewModel.lastLoginData.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                if (isFirstTime){
                    if (it.lastLoginDate!!.isNotEmpty()&& it.lastLogoutDate!!.isNotEmpty()){
                        Toast.makeText(requireContext(), "Sucessfully Logout", Toast.LENGTH_SHORT).show()

                    }else if (it.lastLogoutDate.isNullOrEmpty()){
                        Toast.makeText(requireContext(), "Sucessfully Login", Toast.LENGTH_SHORT).show()

                    }
                }
                else{
                    isFirstTime=true
                }

                if (it.lastLogoutDate.isNullOrEmpty()) {
                    lastLogDateTime = it.lastLoginDate.toString()
                    handleLogInOutStatus(true)
                } else {
                    handleLogInOutStatus(false)
                }
            } else {
                handleLogInOutStatus(false)
            }
        })

        viewModel.departmentData.observe(viewLifecycleOwner) {
            if (it.status) {
                departmentList = it.DEPARTMENTLIST
            } else {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }



        viewModel.siteLiveData.observe(viewLifecycleOwner) {
            hideLoading()

            if (it.sitelist?.isEmpty()!!) {

            } else {
                siteIdList = it.sitelist as ArrayList<SiteListResponse.Site>


            }

        }

        viewModel.doctorLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            if (it.doctorlist?.isEmpty()!!) {

            } else {
                doctorId.visibility = View.VISIBLE
                doctorList = it.doctorlist as ArrayList<DoctorListResponse.Doctor>


            }


        }

        viewModel.departmentSubTaskData.observe(viewLifecycleOwner) {
            MARKETING_SUBTASK_LIST.clear()
            if (it.status!!) {
                marketingSubTaskList =
                    it.subtasklist as java.util.ArrayList<DepartmentSubTaskResponse.Subtask>
                MARKETING_SUBTASK_LIST.add("Select SubTask")
                if (marketingSubTaskList.size > 0) {
                    for (item in marketingSubTaskList) {
                        MARKETING_SUBTASK_LIST.add(item.subtask!!)
                    }

                    marketingSubBranchSpinner.visibility = View.VISIBLE
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.view_spinner_item, R.id.spinner_text, MARKETING_SUBTASK_LIST
                    )
                    marketingSubBranchSpinner.adapter = adapter
                } else {


                    marketingSubBranchSpinner.visibility = View.GONE
                }
            } else {

                marketingSubBranchSpinner.visibility = View.GONE
                Toast.makeText(requireContext(), "Task list not found", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.departmentTaskData.observe(viewLifecycleOwner) {
            DEPT_TASK_LIST.clear()
            if (it.status) {
                departmentTaskList = it.TASKLIST
                DEPT_TASK_LIST.add("Select Task")
                if (departmentTaskList.size > 0) {
                    for (item in departmentTaskList) {
                        DEPT_TASK_LIST.add(item.TASK)
                    }

                    deptTaskSpinner.visibility = View.VISIBLE
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.view_spinner_item, R.id.spinner_text, DEPT_TASK_LIST
                    )
                    deptTaskSpinner.adapter = adapter
                } else {


                    deptTaskSpinner.visibility = View.GONE
                }
            } else {

                deptTaskSpinner.visibility = View.GONE
                Toast.makeText(requireContext(), "Task list not found", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.singInLayout.setOnClickListener {
//            viewBinding.singInLayout.isClickable = false
//            viewBinding.singInLayout.setBackgroundDrawable(requireContext().resources.getDrawable(R.drawable.bg_button_disable))
            viewBinding.onSignInClick = true
            viewBinding.submitBtnLayout.visibility = View.GONE
            (activity as MainActivity).initPermission()
            (activity as MainActivity).startLocationUpdates()
            handleCameraFunctionality()
        }

//        updateLocationInfo()

        viewBinding.captureBtnLayout.setOnClickListener {
            handleCameraFunctionality()
        }

        viewBinding.deleteImage.setOnClickListener {
            fileArrayList.clear()
            imageFromCameraFile?.let {
                it.delete()
            }
            viewBinding.capturedImageLayout.visibility = View.VISIBLE
            viewBinding.capturedImg.setImageURI(Uri.fromFile(imageFromCameraFile!!))
            viewBinding.capturedImg.setImageDrawable(null)
            viewBinding.submitBtnLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )


//            viewBinding.captureBtnLayout.visibility = View.VISIBLE
            viewBinding.deleteImage.visibility = View.GONE
            viewBinding.cameraIcon.visibility = View.VISIBLE
            viewBinding.submitBtnLayout.visibility = View.VISIBLE
        }

        viewBinding.submitBtnLayout.setOnClickListener {
            if (fileArrayList.size > 0) {
                if (NetworkUtil.isNetworkConnected(requireContext())) {
                    viewBinding.singInLayout.isClickable = true
                    if (taskAlreadyAvailable) {
                        onItemClick(taskAvailableId, comment)
                    } else {
                        showLoading()
                        viewModel.connectToAzure(fileArrayList)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        context?.resources?.getString(R.string.label_network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, "Please capture the Photo", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.complainLiveData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (it.size == 0) {
                taskAlreadyAvailable = false
                viewBinding.emptyList.visibility = View.VISIBLE
            } else {

                viewBinding.emptyList.visibility = View.GONE
                adapter = TaskRecyclerView(it, this, requireContext())
                viewBinding.taskRecyclerView.adapter = adapter
                taskAlreadyAvailable = false
                for (item in it) {
                    if (item.signOutDate.isNullOrEmpty()) {
                        taskAlreadyAvailable = true
                        taskAvailableId = item.taskId
                        break
                    }
                }
            }
        })

        viewModel.command.observe(viewLifecycleOwner, Observer {
            when (it) {

                is AttendanceCommand.ShowToast -> {
                    hideLoading()
                }

                is AttendanceCommand.UpdateTaskList -> {
                    hideLoading()

                    if (NetworkUtil.isNetworkConnected(requireContext())) {
                        if (viewBinding.onLogoutClick == true) {
                            viewModel.connectToAzure(fileArrayList)
                        } else {
                            viewModel.getTaskList(employeeID)
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            context?.resources?.getString(R.string.label_network_error),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                is AttendanceCommand.ImageIsUploadedInAzur -> {

                    imagePathToServer = it.filePath[0].base64Images
                    hideLoading()
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (locationLatitude.isNotEmpty() && locationLongitude.isNotEmpty()
                        ) {
                            if (NetworkUtil.isNetworkConnected(requireContext())) {
                                showLoading()
                                viewModel.attendanceSignInOutService(
                                    AtdLogInOutReq(
                                        employeeID,
                                        locationLatitude,
                                        locationLongitude,
                                        imagePathToServer,
                                        getAttendanceLocation(
                                            requireContext(),
                                            locationLatitude.toDouble(),
                                            locationLongitude.toDouble()
                                        ),
                                        getAttendanceCity(
                                            requireContext(),
                                            locationLatitude.toDouble(),
                                            locationLongitude.toDouble()
                                        ),
                                        getAttendanceState(
                                            requireContext(),
                                            locationLatitude.toDouble(),
                                            locationLongitude.toDouble()
                                        )
                                    ), this@AttendanceFragment
                                )
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    context?.resources?.getString(R.string.label_network_error),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            (activity as MainActivity).initPermission()
                            (activity as MainActivity).startLocationUpdates()
                        }
                    } else {
                        (activity as MainActivity).initPermission()
                        (activity as MainActivity).startLocationUpdates()
                    }
                }

                is AttendanceCommand.RefreshPageOnSuccess -> {
                    if (it.status) {
                        if (NetworkUtil.isNetworkConnected(requireContext())) {
                            refreshView()
                            viewModel.getLastLogin(employeeID)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                context?.resources?.getString(R.string.label_network_error),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        hideLoading()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }

                else -> {
                }
            }
        })



        viewBinding.signOutParentLayout.setOnClickListener {
            handleLogoutClick()
        }
    }

    private fun refreshView() {
        hideLoading()

        if (viewBinding.onLogoutClick == true) {
            viewBinding.onLogoutClick = false
            isAtdLogout = true
//            (activity as MainActivity).onBackPressed()
            viewBinding.signInOutParentLayout.visibility = View.VISIBLE
            viewBinding.taskInfoLayout.visibility = View.GONE
            viewBinding.capturedImg.setImageDrawable(null)
            viewBinding.deleteImage.visibility = View.GONE
            viewBinding.cameraIcon.visibility = View.VISIBLE
            viewBinding.submitBtnLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            viewBinding.submitBtnLayout.visibility = View.VISIBLE
        } else {
            viewBinding.onLogoutClick = false
            viewBinding.signInOutParentLayout.visibility = View.GONE
            viewBinding.taskInfoLayout.visibility = View.VISIBLE
            viewModel.getTaskList(employeeID)
        }
    }

    class TaskRecyclerView(
        var orderData: List<GetTaskListResponse>,
        val imageClickListener: ImageClickListener, val context: Context,
    ) :

        SimpleRecyclerView<ViewTaskItemBinding, GetTaskListResponse>(
            orderData,
            R.layout.view_task_item
        ) {
        private lateinit var remarsText: EditText

        @SuppressLint("SetTextI18n")
        override fun bindItems(
            binding: ViewTaskItemBinding,
            items: GetTaskListResponse,
            position: Int,
        ) {
            if (items.signOutDate.isNullOrEmpty()) {

//                binding.overAllLayout.setBackgroundColor(context.resources.getColor(R.color.active_task_color_new))
                binding.taskCompletedLayout.visibility = View.GONE
//                binding.taskPendingLayout.visibility = View.VISIBLE
            } else {

//                binding.overAllLayout.setBackgroundColor(context.resources.getColor(R.color.newtask_bgs))
                binding.taskCompletedLayout.visibility = View.VISIBLE
//                binding.taskPendingLayout.visibility = View.GONE
                binding.signOutTime.text =
                    getAttendanceCustomDate(
                        items.signOutDate
                    )

                binding.durationTexthrs.text = items.duration.split(":").get(0)
                binding.durationTextminutes.text = items.duration.split(":").get(1)
                binding.durationTextsecs.text = items.duration.split(":").get(2)

            }
            binding.viewTaskLayout.setOnClickListener {
                imageClickListener.onComplaintItemClick(position, orderData)
            }

            if (items.isExpanded) {
                binding.viewExpand.visibility = View.VISIBLE
                binding.viewCollapse.visibility = View.GONE
//               binding.overAllLayout.setBackgroundColor(context.resources.getColor(R.color.active_task_color_new))

                if (items.signOutDate.isNullOrEmpty()) {
//                    binding.taskCompletedLayout.visibility = View.GONE
//                    binding.taskPendingLayout.visibility = View.VISIBLE
                    binding.durationLayout.visibility = View.GONE
                    binding.taskPendingLayout.visibility = View.VISIBLE
//                   binding.overAllLayout.setBackgroundColor(context.resources.getColor(R.color.active_task_color_new))

                } else {
//                   binding.overAllLayout.setBackgroundColor(context.resources.getColor(R.color.active_task_color_new))
//
//                    binding.taskCompletedLayout.visibility = View.VISIBLE
                    binding.taskPendingLayout.visibility = View.GONE
                    binding.durationLayout.visibility = View.VISIBLE
                }
            } else {
                binding.viewExpand.visibility = View.GONE
                binding.viewCollapse.visibility = View.VISIBLE
//               binding.overAllLayout.setBackgroundColor(context.resources.getColor(R.color.newtask_bgs))

//                binding.taskCompletedLayout.visibility = View.GONE
                binding.taskPendingLayout.visibility = View.GONE
                binding.durationLayout.visibility = View.GONE
            }
            binding.taskName.text = items.taskName
            binding.signInTime.text = getAttendanceCustomDate(
                items.signInDate
            )
            binding.pendingSignInTime.text = getAttendanceCustomDate(
                items.signInDate
            )
            fun validationCheckTaskOut(): Boolean {

                val remarks = remarsText.text.toString().trim()
                if (remarks.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please Enter Remarks",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return false
                }

                return true

            }

            binding.signOutLayout.setOnClickListener {
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.dialog_task_signout)
                if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                val taskName = dialog.findViewById<TextView>(R.id.taskName)
                val signInTimeText = dialog.findViewById<TextView>(R.id.signInTimeText)
                val close = dialog.findViewById<ImageView>(R.id.closedialog)

                val currentTime = dialog.findViewById<TextView>(R.id.currentTime)
                val durationValuehrs = dialog.findViewById<TextView>(R.id.durationhrs)
                val durationValuemins = dialog.findViewById<TextView>(R.id.durationminutes)
                val durationValuesecs = dialog.findViewById<TextView>(R.id.durationsecs)

                remarsText = dialog.findViewById<EditText>(R.id.descriptionTextattendence)
                close.setOnClickListener {
                    dialog.dismiss()
                }


                taskName.text = items.taskName
                signInTimeText.text =
                    getAttendanceCustomDateFormat(items.signInDate) + "\n" + getAttendanceCustomTimeFormat(
                        items.signInDate
                    )

                currentTime.text =
                    getAttendanceCurrentDateNewFormat() + "\n" + getAttendanceCurrentDateNewFormatTime()
                durationValuehrs.text = getDurationTime(
                    getAttendanceCurrentDate(),
                    getAttendanceCustomDate(items.signInDate)
                )

                durationValuemins.text = getDurationTimMin(
                    getAttendanceCurrentDate(),
                    getAttendanceCustomDate(items.signInDate)
                )

                durationValuesecs.text = getDurationTimeSec(
                    getAttendanceCurrentDate(),
                    getAttendanceCustomDate(items.signInDate)
                )
                val singOutLayout = dialog.findViewById<LinearLayout>(R.id.singOutLayout)
                singOutLayout.setOnClickListener { v1: View? ->
                    if (validationCheckTaskOut()) {
                        imageClickListener.onItemClick(items.taskId, remarsText.text.toString())
                        dialog.dismiss()
                    }

                }
            }
        }

        fun notifyAdapter(taskList: List<GetTaskListResponse>) {
            this.orderData = taskList
            notifyDataSetChanged()
        }
    }


    private fun validationCheck(): Boolean {


        if (branchName.equals("DOCTOR VISIT")) {

            val siteId = siteIdText.text.toString().trim()
            val doctorName = doctorText.text.toString().trim()
            if (siteId.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please Select Site Id",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            } else if (doctorName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please Select Doctor's Name",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            }
        } else if (branchName.equals("BRANCH VISIT")) {
            val siteId = siteIdText.text.toString().trim()
            if (siteId.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please Select Site Id",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            }
        } else {
            return true

        }
        return true

    }

    override fun onItemClick(ticketNumber: String, remarks: String) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (locationLatitude.isNotEmpty() && locationLongitude.isNotEmpty()
            ) {
                comment = remarks
                if (NetworkUtil.isNetworkConnected(requireContext())) {
                    showLoading()
                    viewModel.taskInsertUpdateService(
                        TaskInfoReq(
                            "",
                            employeeID,
                            ticketNumber,
                            locationLatitude,
                            locationLongitude,
                            "SIGNOUT",
                            getAttendanceCity(
                                requireContext(),
                                locationLatitude.toDouble(),
                                locationLongitude.toDouble()
                            ), remarks, siteIds, doctorNAme, imageUrls, subTaskName
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        context?.resources?.getString(R.string.label_network_error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                (activity as MainActivity).initPermission()
                (activity as MainActivity).startLocationUpdates()
            }
        } else {
            (activity as MainActivity).initPermission()
            (activity as MainActivity).startLocationUpdates()
        }
    }

    override fun onComplaintItemClick(position: Int, orderData: List<GetTaskListResponse>) {
        val curStatus: Boolean = orderData.get(position).isExpanded
        orderData.forEach {
            it.isExpanded = false
        }
        orderData.get(position).isExpanded = !curStatus
        adapter.notifyAdapter(orderData)
    }

    @SuppressLint("SetTextI18n")
    private fun handleLogInOutStatus(status: Boolean) {
        if (status) {
            viewBinding.signInOutParentLayout.visibility = View.GONE
            viewBinding.taskInfoLayout.visibility = View.VISIBLE
            MainActivity.mInstance.plusIconAttendence.visibility = View.VISIBLE
            viewBinding.signInViewLayout.visibility = View.GONE
            viewBinding.onLogoutClick = false
            MainActivity.mInstance.headerTextLocation.visibility = View.GONE
            params.addRule(RelativeLayout.CENTER_VERTICAL)
            MainActivity.mInstance.headerText.textSize = 18f
            viewBinding.gpsInfoLayout.visibility = View.GONE

            viewBinding.lastLogTime.setText(
                getLastLoginDate(lastLogDateTime)
            )
            if (NetworkUtil.isNetworkConnected(requireContext())) {
                viewModel.getTaskList(employeeID)
                viewModel.getDepartmentList()
            } else {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            hideLoading()
            viewBinding.signInViewLayout.visibility = View.VISIBLE
            viewBinding.onLogoutClick = true
            viewBinding.gpsInfoLayout.visibility = View.VISIBLE
            params.removeRule(RelativeLayout.CENTER_VERTICAL)
            MainActivity.mInstance.headerText.textSize = 16f
            MainActivity.mInstance.headerTextLocation.visibility = View.VISIBLE

            params.topMargin = dpToPx(8)
            MainActivity.mInstance.headerText!!.layoutParams = params

            MainActivity.mInstance.plusIconAttendence.visibility = View.GONE
            viewBinding.signInOutParentLayout.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleLogoutClick() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog_alert)
        if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val dialogLastLogTime = dialog.findViewById<TextView>(R.id.lastLoginTime)
        val dialogCurrentTime = dialog.findViewById<TextView>(R.id.currentTime)
        val dialogDurationHrs = dialog.findViewById<TextView>(R.id.durationhrs)
        val dialogDurationMins = dialog.findViewById<TextView>(R.id.durationminutes)
        val dialogDurationSecs = dialog.findViewById<TextView>(R.id.durationsecs)

        val dialogTitleText = dialog.findViewById<TextView>(R.id.dialog_info)
        val okButton = dialog.findViewById<Button>(R.id.dialog_ok)
        val declineButton = dialog.findViewById<ImageView>(R.id.closedialog)
        dialogLastLogTime.text =
            getLastLoginDateNewFormat(lastLogDateTime) + "\n" + getLastLoginDateNewFormatTime(
                lastLogDateTime
            )
        dialogCurrentTime.text =
            getAttendanceCurrentDateNewFormat() + "\n" + getAttendanceCurrentDateNewFormatTime()

        var timeDuration =
            printDifferenceNav(
                getAttendanceCurrentDateNew(), getLastLoginDateNewinSec(
                    lastLogDateTime
                )
            )
        val split = timeDuration.split(":")
        dialogDurationHrs.text = split[0] //lastLogDateTime.split(" ").get(1).split(":").get(0)
        dialogDurationMins.text = split[1] // lastLogDateTime.split(" ").get(1).split(":").get(1)
        dialogDurationSecs.text = split[2] //lastLogDateTime.split(" ").get(1).split(":").get(2)
//        dialogDurationHrs.text = lastLogDateTime.split(" ").get(1).split(":").get(0)
//        dialogDurationMins.text = lastLogDateTime.split(" ").get(1).split(":").get(1)
//        dialogDurationSecs.text = lastLogDateTime.split(" ").get(1).split(":").get(2)

//        dialogDurationTime.text =
//            " " + lastLogDateTime.trimSubstring(9, 19).trimSubstring(
//                0,
//                2) + "   " + "   " + lastLogDateTime.trimSubstring(9,
//                19).trimSubstring(3,
//                5) + "    " + "   " + lastLogDateTime.trimSubstring(9,
//                19).trimSubstring(6,
//                8)
        dialogTitleText.text = resources.getString(R.string.label_logout_alert)
        okButton.text = resources.getString(R.string.label_ok)
//        declineButton.text = resources.getString(R.string.label_cancel_text)
        okButton.setOnClickListener { v1: View? ->
            dialog.dismiss()
            //Handle Camera Functionality
            viewBinding.signInOutParentLayout.visibility = View.VISIBLE
            MainActivity.mInstance.plusIconAttendence.visibility = View.GONE
            MainActivity.mInstance.headerTextLocation.visibility = View.VISIBLE
            viewBinding.capturedImg.setImageDrawable(null)
            // Assuming you have a reference to the view
            params.removeRule(RelativeLayout.CENTER_VERTICAL)
            MainActivity.mInstance.headerText.textSize = 16f


            params.topMargin = dpToPx(8)
            MainActivity.mInstance.headerText!!.layoutParams = params
            viewBinding.taskInfoLayout.visibility = View.GONE
            viewBinding.onLogoutClick = true
//            handleCameraFunctionality()
            //Handle SignOut Service Check Task SignOut as well
        }
        declineButton.setOnClickListener { v12: View? -> dialog.dismiss() }
    }

    fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun printDifferenceNav(endDate: String, startDate: String): String {
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            val format = SimpleDateFormat("dd MMM yyyy, hh:mm:ss aa")
            val date1 = format.parse(startDate)
            val date2 = format.parse(endDate)

            var startDate: Date = date1
            var endDate: Date = date2
            //milliseconds
            var different = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            //long elapsedDays = different / daysInMilli;
            //different = different % daysInMilli;
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli
//        System.out.printf(
//            "%d hours, %d minutes, %d seconds%n",
//            elapsedHours, elapsedMinutes, elapsedSeconds)
//        return String.format(
//            "%d hours, %d minutes, %d seconds%n",
//            elapsedHours, elapsedMinutes, elapsedSeconds)
            return "$elapsedHours:$elapsedMinutes:$elapsedSeconds"
        } else {
            return "00:00:00"
        }
    }






    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askPermissions(PermissonCode: Int) {
        if (Build.VERSION.SDK_INT >= TIRAMISU) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        }
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun compresImageSize(imageFromCameraFile: File): File {
        val resizedImage = Resizer(requireContext())
            .setTargetLength(1080)
            .setQuality(100)
            .setOutputFormat("JPG")
            .setOutputDirPath(
                ViswamApp.context.cacheDir.toString()
            )
            .setSourceImage(imageFromCameraFile)
            .resizedFile
        return resizedImage


    }


override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
        REQUEST_CODE_MARKETING -> handleMarketingResult(resultCode)
        REQUEST_CODE_CAMERA -> handleCameraResult(resultCode)
         LOCATION_PERMISSION_REQUEST->
            if (resultCode == Activity.RESULT_OK) {
                isGPSEnabled = true
                startLocationUpdates()
            }
               // Add more cases if needed
        else -> {
            // Handle other cases or remove this block if not needed
        }
    }
}

    private fun handleMarketingResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK && imageFromCameraFile != null) {
            // Assuming that the compresImageSize function returns the correct data
            val capturedImage = Image(compresImageSize(imageFromCameraFile!!), "", "")

            // Add the captured image to the beginning of the list
            imageList.add(0, capturedImage)

            // Create or update the adapter with the reversed list
            imageAdapter = AttendenceImageRecycleView(requireContext(), imageList, this)

            // Set the adapter to the RecyclerView
            imageRecycleView.adapter = imageAdapter

            // Move the UI update code inside this block
            updateUIForCapturedImage()
        }
    }

    private fun handleCameraResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK && imageFromCameraFile != null) {
            isPermissionAsked = false
            fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
            updateUIForCapturedImage()
        }
    }

    private fun updateUIForCapturedImage() {
        viewBinding.capturedImageLayout.visibility = View.VISIBLE
        viewBinding.capturedImg.setImageURI(Uri.fromFile(imageFromCameraFile!!))
        viewBinding.captureBtnLayout.visibility = View.GONE
        viewBinding.submitBtnLayout.visibility = View.VISIBLE
        viewBinding.cameraIcon.visibility = View.GONE
        viewBinding.deleteImage.visibility = View.VISIBLE
        viewBinding.submitBtnLayout.setBackgroundColor(Color.parseColor("#045a71"))
        viewBinding.singInLayout.visibility = View.GONE
    }

    private fun handleCameraFunctionality() {
        viewBinding.onCaptureClick = true
        if (!checkPermission()) {
            askPermissions(REQUEST_CODE_CAMERA)
            return
        } else
            openCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
        else if (requestCode == REQUEST_CODE_MARKETING) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openTaskCamera()
            }
        }
    }

    fun getKeyFromValue(hm: Map<*, *>, value: Any): Int? {
        for (o in hm.keys) {
            if (hm[o] == value) {
                return o as Int?
            }
        }
        return 0
    }

    fun updateLocationInfo() {
        try {
            if (locationLatitude.isNotEmpty() && locationLongitude.isNotEmpty()) {
                viewBinding.gpsInfoLayout.visibility = View.VISIBLE

                if (viewBinding.currentLocationInfo.text.toString().isNullOrEmpty()) {
                    viewBinding.currentLocationInfo.setText(
                        getCurrentAddress(
                            requireContext(),
                            locationLatitude.toDouble(),
                            locationLongitude.toDouble()
                        )
                    )
                }
            } else {
                viewBinding.gpsInfoLayout.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
            //Nothing To Do
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        when {
            !isGPSEnabled -> {
//                Toast.makeText(requireContext(), "Enable Device\\'s GPS", Toast.LENGTH_SHORT).show()
            }

            isLocationPermissionsGranted() -> {
                observeLocationUpdates()
            }

            else -> {
                askLocationPermission()
            }
        }
    }

    private fun observeLocationUpdates() {
        locationViewModel.getLocationData.observe(this, Observer {
            locationLatitude = it.latitude.toString()
            locationLongitude = it.longitude.toString()
            updateLocationInfo()
//            Utils.printMessage("AtdFrag",
//                "Lat : " + it.longitude.toString() + ", Long : " + it.latitude.toString())
//            lat.text = it.longitude.toString()
//            latitude.text = it.latitude.toString()
//            info.text = getString(R.string.location_successfully_received)
        })
    }

    private fun isLocationPermissionsGranted(): Boolean {
        return (EzPermission.isGranted(requireContext(), locationPermissions[0])
                && EzPermission.isGranted(requireContext(), locationPermissions[1]))
    }

    private fun askLocationPermission() {
        EzPermission
            .with(requireContext())
            .permissions(locationPermissions[0], locationPermissions[1])
            .request { granted, denied, permanentlyDenied ->
                if (granted.contains(locationPermissions[0]) &&
                    granted.contains(locationPermissions[1])
                ) { // Granted
                    startLocationUpdates()

                } else if (denied.contains(locationPermissions[0]) ||
                    denied.contains(locationPermissions[1])
                ) { // Denied

                    showDeniedDialog()

                } else if (permanentlyDenied.contains(locationPermissions[0]) ||
                    permanentlyDenied.contains(locationPermissions[1])
                ) { // Permanently denied
                    showPermanentlyDeniedDialog()
                }

            }
    }

    private fun showPermanentlyDeniedDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(getString(R.string.title_permission_permanently_denied))
        dialog.setMessage(getString(R.string.message_permission_permanently_denied))
        dialog.setNegativeButton(getString(R.string.not_now)) { _, _ -> }
        dialog.setPositiveButton(getString(R.string.settings)) { _, _ ->
            startActivity(
                EzPermission.appDetailSettingsIntent(
                    requireContext()
                )
            )
        }
        dialog.setOnCancelListener { } //important
        dialog.show()
    }

    private fun showDeniedDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(getString(R.string.title_permission_denied))
        dialog.setMessage(getString(R.string.message_permission_denied))
        dialog.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        dialog.setPositiveButton(getString(R.string.allow)) { _, _ ->
            askLocationPermission()
        }
        dialog.setOnCancelListener { } //important
        dialog.show()
    }

    override fun selectSite(departmentDto: SiteListResponse.Site) {
//        siteId.visibility = View.VISIBLE
        if (branchName.equals("DOCTOR VISIT")) {
            showLoading()
            val doctorListRequest = DoctorListRequest("DOCTORLIST", departmentDto.siteid!!)

            doctorText.setText("")
            doctorSpecialist.visibility = View.GONE

            viewModel.doctorListResponse(doctorListRequest)
        }
        siteIds = departmentDto.siteid!!
        siteIdText.setText(departmentDto.siteid + " - " + departmentDto.sitename)
    }

    override fun selectSite(departmentDto: DoctorListResponse.Doctor) {
        doctorNAme = departmentDto.doctorname!!
        doctorText.setText(departmentDto.doctorname)
        doctorSpecialist.visibility = View.VISIBLE
        doctorSpecialist.setText(departmentDto.speciality)
    }

    override fun onSuccessAttendanceSignedOut(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onSuccessAttendanceSignedIn(message: String) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(position: Int, imagePath: String, name: String) {
        PopUpWIndow(
            context,
            R.layout.layout_image_fullview,
            cameraIcon,
            imagePath,
            null,
            name,
            position
        )
    }

    private fun openTaskCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile =
            File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_MARKETING)
    }

    override fun deleteImage(position: Int) {
        val dialogBinding: DialogDeleteBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_delete,
                null,
                false
            )
        val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {


            imageList.removeAt(position)

            imageAdapter.notifyDataSetChanged()
            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun onClickFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onSelectApprovedFragment(listSize: String?) {
        TODO("Not yet implemented")
    }

    override fun onSelectRejectedFragment() {
        TODO("Not yet implemented")
    }

    override fun onSelectPendingFragment() {
        TODO("Not yet implemented")
    }

    override fun onClickSpinnerLayout() {
        TODO("Not yet implemented")
    }

    private fun handleTaskCameraFunctionality() {
        viewBinding.onCaptureClick = true
        if (!checkPermission()) {
            askPermissions(REQUEST_CODE_MARKETING)
            return
        } else
            openTaskCamera()
    }

    override fun onClickSubmenuItem(
        menuName: String?,
        submenus: ArrayList<MenuModel>?,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onclickHelpIcon() {
        TODO("Not yet implemented")
    }

    override fun selectGST(gst: String) {
//        subTaskName = gst
//        description.visibility = View.VISIBLE
//        captureLayout.visibility = View.VISIBLE
    }

    override fun onFailureUpload(message: String) {

    }

    override fun allFilesDownloaded(
        fileUploadModelList: ArrayList<AttendenceFileUploadModel>?,
        dialog: Dialog,
    ) {


    }


    override fun allFilesUploaded(
        fileUploadModelList: ArrayList<AttendenceFileUploadModel>?,
        dialog: Dialog,
    ) {


        imageUrls = fileUploadModelList!!.mapNotNull {
            it.sensingFileUploadResponse?.referenceurl
        }.joinToString(", ")
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
            viewModel.taskInsertUpdateService(
                TaskInfoReq(
                    enteredTaskName,
                    employeeID,
                    "",
                    locationLatitude,
                    locationLongitude,
                    "SIGNIN",
                    getAttendanceCity(
                        requireContext(),
                        locationLatitude.toDouble(),
                        locationLongitude.toDouble()
                    ), descriptionText.text.toString(), siteIds, doctorNAme, imageUrls, subTaskName
                )
            )
            imageList.clear()
            fileUploadModelList.clear()
            descriptionText.setText("")
            dialog.dismiss()
        } else {
            Toast.makeText(
                requireContext(),
                context?.resources?.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}

interface ImageClickListener {
    fun onItemClick(ticketNumber: String, remarks: String)

    fun onComplaintItemClick(position: Int, orderData: List<GetTaskListResponse>)
}