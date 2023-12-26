package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.data.model.cms.ResponseticketRatingApi
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.ActivityComplaintsDetailsBinding
import com.apollopharmacy.vishwam.databinding.DialogUserListBinding
import com.apollopharmacy.vishwam.dialog.SearchManagerDialog
import com.apollopharmacy.vishwam.dialog.model.SubmitticketDialog
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ImageClickListener
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.callback.HistoryCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.model.SubworkFlowAssignedtoMeResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter.UserListAdapter
import com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter.ViewPagerAdapter
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Action
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CCAcceptRejectModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CmsTicketRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Data
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Department
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.FMTicket
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Feedback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.ForwardToManagerModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.InventoryAcceptrejectModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.ItemStatus
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Level
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Manager
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.NextLevel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Rating
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Site
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Status
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubworkflowConfigDetailsResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketData
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketOwner
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketResolveCloseModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketSubworkflowActionUpdateRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketSubworkflowActionUpdateResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.User
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.UserListForSubworkflowResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.WorkFlowUpdateModel
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.google.android.material.tabs.TabLayout
import com.hsalf.smilerating.SmileRating


class ComplaintsListDetailsActivity() : AppCompatActivity(), ComplaintsListDetailsCallback,
    HistoryCallback, ImageClickListener {
    private lateinit var activityComplaintsDetailsBinding: ActivityComplaintsDetailsBinding
    private lateinit var viewModel: ComplaintsListDetailsViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var orderDataWp: ResponseNewTicketlist.Row
    private lateinit var orderData: ArrayList<ResponseNewTicketlist.Row>
    private var position = 0
    lateinit var userData: LoginDetails
    private var isFromApprovalList = false
    var resList = java.util.ArrayList<ResponseNewTicketlist.Row>()
    var pos: Int = 0
    var frwdMngrPos: Int = 0
    var resListMnger = java.util.ArrayList<ResponseNewTicketlist.Row>()
    lateinit var userListDialog: Dialog
    var usersList: java.util.ArrayList<UserListForSubworkflowResponse.Rows>? = null
    lateinit var userListAdapter: UserListAdapter
    var userForsubworkflow = UserListForSubworkflowResponse.Rows()

    //    private lateinit var imageClickListener: ImageClickListener
//    private lateinit var viewPager2:ViewPager2
    var ticketratingapiresponse: ResponseticketRatingApi.Data? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComplaintsDetailsBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_complaints_details
        )
        viewModel = ViewModelProvider(this)[ComplaintsListDetailsViewModel::class.java]
        setUp()
    }

    private fun setUp() {
//        viewPager2 = findViewById(R.id.view_pager_cms)
        activityComplaintsDetailsBinding.callback = this
        userData = LoginRepo.getProfile()!!
        viewModel.getTicketRatingApi()


        orderDataWp = intent.getSerializableExtra("orderDataWp") as ResponseNewTicketlist.Row
        if (orderDataWp != null
            && orderDataWp.status != null
        ) {
            activityComplaintsDetailsBinding.ticketStatus.text = orderDataWp.status!!.name
            activityComplaintsDetailsBinding.ticketStatus.setTextColor(Color.parseColor(orderDataWp.status!!.background_color)) //= ColorStateList.valueOf(Color.parseColor(orderDataWp.status!!.background_color))
        }
        orderData =
            intent.getSerializableExtra("orderData") as ArrayList<ResponseNewTicketlist.Row>
        position = intent.getIntExtra("position", 0)
        isFromApprovalList = intent.getBooleanExtra("isFromApprovalList", false)
        activityComplaintsDetailsBinding.ticketNumber.text = orderDataWp.ticket_id

        activityComplaintsDetailsBinding.date.text = orderDataWp.created_time?.let {
            Utlis.cmsComplaintDateFormat(it)
        }


        viewModel.cmsTicketResponseList.observe(this) {
            hideLoading()
            if (it.success!!) {

                SubmitticketDialog().apply {
                    arguments = SubmitticketDialog().generateParsedData(it)
                }.show(getSupportFragmentManager(), "")
            }


        }
        viewModel.cmsticketRatingresponse.observe(this) {
//            Utlis.hideLoading()
//            ticketratingapiresponse = it.data;
//            callAPI(1)
//            if (!Preferences.getResponseNewTicketlist().isEmpty()) {
//                if (Config.COMPLAINTLISTFETCHEDTIME != null) {
//                    if (diffTime(Config.COMPLAINTLISTFETCHEDTIME!!,
//                            Calendar.getInstance().getTime()) < 6
//                    ) {
//                        isTicketListThereFirstTime = false
//                        viewModel.setTicketListFromSheredPeff()
//                    } else {
//                        Utlis.hideLoading()
//                        ticketratingapiresponse = it.data;
//                        callAPI(1)
//                    }
//                } else {
//                    Utlis.hideLoading()
//                    ticketratingapiresponse = it.data;
//                    callAPI(1)
//                }
//            } else {
            Utlis.hideLoading()
            ticketratingapiresponse = it.data;
//            callAPI(1)
//            }
        }
        viewModel.command.observe(this, Observer {
            hideLoading()
            when (it) {
//                is CmsCommand.VisibleLayout -> {
//                    viewBinding.emptyList.visibility = View.VISIBLE
//                }

                is ComplaintsListDetailsViewModel.CmsCommand.ShowToast -> {
                    hideLoading()
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }

                is ComplaintsListDetailsViewModel.CmsCommand.RefreshPageOnSuccess -> {
                    hideLoading()
//                    submitClick()
                }

                else -> {
                }
            }
        })



        viewPagerAdapter = ViewPagerAdapter(
            this,
            orderDataWp,
            orderData,
            position,
            this,
            this,
            isFromApprovalList,
            this
        )
        activityComplaintsDetailsBinding.viewPagerCms.adapter = viewPagerAdapter

        activityComplaintsDetailsBinding.tabLayout.getTabAt(0)
            ?.setIcon(R.drawable.info_detail_grey)!!.icon?.setTintList(resources.getColorStateList(R.color.tab_icon_color))
        activityComplaintsDetailsBinding.tabLayout.getTabAt(1)
            ?.setIcon(R.drawable.time_history_grey)!!.icon?.setTintList(resources.getColorStateList(R.color.tab_icon_color))
        activityComplaintsDetailsBinding.tabLayout.getTabAt(2)
            ?.setIcon(R.drawable.link_files_grey)!!.icon?.setTintList(resources.getColorStateList(R.color.tab_icon_color))
        if (orderDataWp != null && orderDataWp.ticketDetailsResponse != null
            && orderDataWp.ticketDetailsResponse!!.data != null && orderDataWp.ticketDetailsResponse!!.data.ticket_inventory != null
            && orderDataWp.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item.size > 0
        ) {
            activityComplaintsDetailsBinding.tabLayout.getTabAt(2)
                ?.setText("Files" + "(" + orderDataWp.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item.size + ")")?.setIcon(R.drawable.link_files_grey)!!.icon?.setTintList(resources.getColorStateList(R.color.tab_icon_color))
        }






        activityComplaintsDetailsBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                activityComplaintsDetailsBinding.viewPagerCms.setCurrentItem(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        activityComplaintsDetailsBinding.viewPagerCms.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                activityComplaintsDetailsBinding.tabLayout.getTabAt(position)!!.select();
            }
        })


    }

    override fun onClickBack() {
//       super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onSucessGetManagersLiveData(data: Data) {
//        SearchManagerDialog(data).apply { }.show(childFragmentManager, "")
    }

    override fun onSelectUserListItem(
        row: UserListForSubworkflowResponse.Rows,
        userSelect: EditText,
    ) {
        userForsubworkflow = row
        var userName = ""
        if (row.firstName != null) {
            userName = "${row.firstName}"
        }
        if (row.middleName != null) {
            userName = "$userName ${row.middleName}"
        }
        if (row.lastName != null) {
            userName = "$userName ${row.lastName}"
        }
        if (row.loginUnique != null) {
            userName = "$userName (${row.loginUnique})"
        }
        userSelect.setText(userName)
        userListDialog.dismiss()
    }


    override fun onClickForwardToFinance(
        data: CmsTicketRequest,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        showLoading(this)

        resList = responseList
        pos = position
        viewModel.cmsTicketStatusUpdate(data)
//        historyCallback.onClickForwardToFinance(data, responseList, position, historyCallback)
    }

    override fun onClickCCAccept(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Accept"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val ccAcceptRejectModel = CCAcceptRejectModel()
                ccAcceptRejectModel.setAction("resolve")
                ccAcceptRejectModel.setComment(remark.text.toString())
                ccAcceptRejectModel.setEmployee_id(userData.EMPID)
                ccAcceptRejectModel.setPos_status("approve")
                ccAcceptRejectModel.setStatus("inprogress")
                ccAcceptRejectModel.setTicket_id(data.ticket_id)
                val ticket_it = CCAcceptRejectModel.Ticket_it()
                ticket_it.setUid("approved")
                ccAcceptRejectModel.setTicket_it(ticket_it)
                ccAcceptRejectModel.setUid(data.uid)
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "solved"
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionCCAccept(ccAcceptRejectModel, 0, this)

//                adapter.notifyDataSetChanged()

//                \"background_color\":\"#047604\",\"code\":\"solved\",\"name\":\"Resolved\",\"text_color\":\"#FFFFFF\
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
//        historyCallback.onClickCCAccept(data, responseList, position, historyCallback)
    }

    override fun onClickCCReject(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reject"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val ccAcceptRejectModel = CCAcceptRejectModel()
                ccAcceptRejectModel.setAction(null)
                ccAcceptRejectModel.setComment(remark.text.toString())
                ccAcceptRejectModel.setEmployee_id(userData.EMPID)
                ccAcceptRejectModel.setPos_status(null)
                ccAcceptRejectModel.setStatus("reject")
                ccAcceptRejectModel.setTicket_id(data.ticket_id)
                val ticket_it = CCAcceptRejectModel.Ticket_it()
                ticket_it.setUid("rejected")
                ccAcceptRejectModel.setTicket_it(ticket_it)
                ccAcceptRejectModel.setUid(data.uid)
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "rejected"
                responseList.get(position).status!!.name = "Rejected"
                responseList.get(position).status!!.background_color = "#ed001c"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionCCAccept(ccAcceptRejectModel, 0, this)

//                adapter.notifyDataSetChanged()


//                "background_color\":\"#ed001c\",\"code\":\"rejected\",\"name\":\"Rejected\",\"text_color\":\"#FFFFFF\"}
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
//        historyCallback.onClickCCReject(data, responseList, position, historyCallback)
    }

    override fun onClickInventoryAccept(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Accept"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = InventoryAcceptrejectModel(
                    null,
                    remark.text.toString(),
                    ItemStatus("approved"),
                    data.site!!.site!!,
                    data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    "inprogress",
                    data.ticketDetailsResponse!!.data.ticket_id,
                    "resolve"
                )
                var workFlowUpdateModel = WorkFlowUpdateModel(
                    Action("97A318ACE84930236386DB1A70944825", "resolve"),
                    "Resolved",
                    Department(
                        data.ticketDetailsResponse?.data?.department?.uid,
                        data.ticketDetailsResponse?.data?.department?.code
                    ),
                    Level(
                        data.ticketDetailsResponse?.data?.level?.uid,
                        data.ticketDetailsResponse?.data?.level?.code
                    ),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(
                        data.ticketDetailsResponse?.data?.user?.first_name,
                        data.ticketDetailsResponse?.data?.user?.uid
                    ),
                    "Yes",
                    "manager"
                )
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "solved"
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionInventoryAcceptReject(
                    inventoryAcceptrejectModel, workFlowUpdateModel, 0, this
                )
//                adapter.notifyDataSetChanged()


//                \"background_color\":\"#047604\",\"code\":\"solved\",\"name\":\"Resolved\",\"text_color\":\"#FFFFFF\
//                viewModel.actionInventoryAcceptReject(inventoryAcceptrejectModel,
//                    workFlowUpdateModel,
//                    0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

//        historyCallback.onClickInventoryAccept(data, position, responseList, historyCallback)
    }

    override fun onClickInventoryReject(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reject"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = InventoryAcceptrejectModel(
                    remark.text.toString(),
                    null,
                    ItemStatus("rejected"),
                    data.site!!.site!!,
                    data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    "reject",
                    data.ticketDetailsResponse!!.data.ticket_id,
                    null

                )
                var workFlowUpdateModel = WorkFlowUpdateModel(
                    Action("52E2C8F5C204B5BD03DF3A73EB096484", "reject"),
                    "Resolved",
                    Department(
                        data.ticketDetailsResponse?.data?.department?.uid,
                        data.ticketDetailsResponse?.data?.department?.code
                    ),
                    Level(
                        data.ticketDetailsResponse?.data?.level?.uid,
                        data.ticketDetailsResponse?.data?.level?.code
                    ),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(
                        data.ticketDetailsResponse?.data?.user?.first_name,
                        data.ticketDetailsResponse?.data?.user?.uid
                    ),
                    "Yes",
                    "manager"
                )
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "rejected"
                responseList.get(position).status!!.name = "Rejected"
                responseList.get(position).status!!.background_color = "#ed001c"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionInventoryAcceptReject(
                    inventoryAcceptrejectModel, workFlowUpdateModel, 0, this
                )
//                adapter.notifyDataSetChanged()

//                background_color\":\"#ed001c\",\"code\":\"rejected\",\"name\":\"Rejected\",\"text_color\":\"#FFFFFF

            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
//        historyCallback.onClickInventoryReject(data, position, responseList, historyCallback)
    }

    override fun onClickForwardToManager(
        data: ResponseNewTicketlist.Row,
        historyCallback: HistoryCallback,
    ) {
//        historyCallback.onClickForwardToManager(data, historyCallback)
        val dialog = Dialog(applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Forward to Manager"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
                showLoading(this)
                var forwardToManagerModel = ForwardToManagerModel(
                    remark.text.toString(),
                    Manager(""),//data.inventoryDetailsModel!!.data.ticket_inventory.ticket_inventory_item[0].manager.uid
                    FMTicket(data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    userData.EMPID
                )
                viewModel.actionForwardToManager(forwardToManagerModel, 0, this)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private lateinit var selectedInventeryTicket: ResponseNewTicketlist.Row
    override fun onClickForwardChangeManager(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        historyCallback: HistoryCallback,
    ) {
        resListMnger = responseList
        frwdMngrPos = pos
        selectedInventeryTicket = data
        viewModel.getManagersLiveData.observe(this, Observer {
            SearchManagerDialog(it).apply { }.show(getSupportFragmentManager(), "")
        })
        viewModel.getManagers(data.site?.uid!!, this)
//        historyCallback.onClickForwardChangeManager(data, position, responseList, historyCallback)
    }

    override fun onClickTicketClose(
        data: ResponseNewTicketlist.Row,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        var ratingduid: String = ""
        val rating = dialog.findViewById(R.id.smile_rating) as SmileRating
        rating.visibility = View.VISIBLE
        rating.setOnRatingSelectedListener { level, reselected ->
            for (rows in ticketratingapiresponse?.listData?.rows!!) {
                if (rows.value.equals(level.toString())) {
                    ratingduid = rows.uid!!
                    break
                }
            }
        }
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Close"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else if (ratingduid.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    applicationContext?.resources?.getString(R.string.label_rate_complaint),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "close",
                    remark.text.toString(),
                    userData.EMPID,
                    "closed",
                    data.ticket_id!!,
                    Feedback(Rating(ratingduid))
                )

                responseList.get(position).status!!.name = "Closed"
                responseList.get(position).status!!.background_color = "#00ba00"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                responseList.get(position).status!!.code = "closed"

                data.isExpanded = false
                viewModel.actionTicketResolveClose(
                    inventoryAcceptrejectModel,
                    historyCallback,
                    this
                )
//                adapter.notifyDataSetChanged()

//                "background_color\":\"#00ba00\",\"code\":\"closed\",\"name\":\"Closed\",\"text_color\":\"#FFFFFF\
//                viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
//        historyCallback.onClickTicketClose(data, responseList, position, historyCallback)
    }

    override fun onClickTicketReopen(
        data: ResponseNewTicketlist.Row,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reopen"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "reopen",
                    remark.text.toString(),
                    userData.EMPID,
                    "reopened",
                    data.ticket_id!!,
                    null
                )
                responseList.get(position).status!!.name = "Reopen"
                responseList.get(position).status!!.background_color = "#f57200"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                responseList.get(position).status!!.code = "reopened"

                data.isExpanded = false
                viewModel.actionTicketResolveClose(
                    inventoryAcceptrejectModel,
                    historyCallback,
                    this
                )

//                adapter.notifyDataSetChanged()
//                \"background_color\":\"#f57200\",\"code\":\"reopened\",\"name\":\"Reopen\",\"text_color\":\"#FFFFFF\"},
//                data.status.
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
//        historyCallback.onClickTicketReopen(data, responseList, position, historyCallback)
    }

    //    override fun onClickSubWorkflowAccept(
//        data: TicketData,
//        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
//        position: Int,
//    ) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onClickSubWorkflowReject(
//        data: TicketData,
//        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
//        position: Int,
//    ) {
//        TODO("Not yet implemented")
//    }
//
    override fun onClickAction(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
        historyCallback: HistoryCallback,
    ) {
        if ((row.action!!.code.equals("forward") || row.action!!.code.equals("change_forward_manager")) && row.assignToUser!!.uid!!.equals(
                "Yes"
            )
        ) {
            if (NetworkUtil.isNetworkConnected(applicationContext)) {
                showLoading(this)
                viewModel.userlistForSubworkflowApiCall(
                    this,
                    data, responseList, position, row
                )
            }
        } else {
            showActionPopup(data, responseList, position, row, null)
        }
//        historyCallback.onClickAction(data, responseList, position, row, historyCallback)
    }

    fun showActionPopup(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
        userListForSubworkflowResponse: UserListForSubworkflowResponse?,
    ) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        val userSelect = dialog.findViewById(R.id.user_list_selected_item) as EditText

        yesBtn.text = row.action!!.name!!
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        var userDropdownLayout =
            dialog.findViewById(R.id.user_dropdown_layout) as LinearLayout
//        var userForsubworkflow = UserListForSubworkflowResponse.Rows()
        if ((row.action!!.code.equals("forward") || row.action!!.code.equals("change_forward_manager")) && row.assignToUser!!.uid!!.equals(
                "Yes"
            )
        ) {
            if (userListForSubworkflowResponse != null
                && userListForSubworkflowResponse.success!!
                && userListForSubworkflowResponse.data != null
                && userListForSubworkflowResponse.data!!.listData != null
                && userListForSubworkflowResponse.data!!.listData!!.rows != null
                && userListForSubworkflowResponse.data!!.listData!!.rows!!.size > 0
            ) {
                userDropdownLayout.visibility = View.VISIBLE
                usersList = userListForSubworkflowResponse.data!!.listData!!.rows!!

                var userListSelectedItem =
                    dialog.findViewById(R.id.user_list_selected_item) as EditText
                userListSelectedItem.setOnClickListener {
                    userListDialog = Dialog(this)
                    val dialogUserListBinding =
                        DataBindingUtil.inflate<DialogUserListBinding>(
                            LayoutInflater.from(this),
                            R.layout.dialog_user_list,
                            null,
                            false
                        )
                    userListDialog.setContentView(dialogUserListBinding.root)
                    userListDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    userListDialog.setCancelable(false)
                    dialogUserListBinding.closeDialog.setOnClickListener {
                        userListDialog.dismiss()
                    }
                    userListAdapter = UserListAdapter(
                        this,
                        applicationContext,
                        usersList!!, userSelect
                    )
                    dialogUserListBinding.locationRcv.adapter = userListAdapter
                    dialogUserListBinding.locationRcv.layoutManager =
                        LinearLayoutManager(this)

                    dialogUserListBinding.searchLocationListText.addTextChangedListener(object :
                        TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int,
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int,
                        ) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            filterUserList(s.toString(), dialogUserListBinding)
                        }
                    })
                    userListDialog.show()
                }
                /*  val spinner = dialog.findViewById(R.id.user_list_for_subworkflow_spinner) as Spinner
                  var usersListforSubworkflowSpinnerAdapter =
                      UsersListforSubworkflowSpinnerAdapter(
                          requireContext(),
                          userListForSubworkflowResponse.data!!.listData!!.rows!!
                      )
                  spinner.adapter = usersListforSubworkflowSpinnerAdapter
                  spinner.setSelection(0)
                  spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                      override fun onItemSelected(
                          parent: AdapterView<*>?,
                          view: View,
                          positionDropDown: Int,
                          id: Long,
                      ) {
                          userForsubworkflow =
                              userListForSubworkflowResponse.data!!.listData!!.rows!!.get(
                                  positionDropDown
                              )
                      }

                      override fun onNothingSelected(parent: AdapterView<*>?) {}
                  })*/
            } else {
                userDropdownLayout.visibility = View.GONE
            }
        } else {
            userDropdownLayout.visibility = View.GONE
        }

        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else if ((row.action!!.code.equals("forward") || row.action!!.code.equals("change_forward_manager")) && row.assignToUser!!.uid!!.equals(
                    "Yes"
                ) && userSelect.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please select user", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                if (NetworkUtil.isNetworkConnected(this)) {
                    showLoading(this)
                    var ticketSubworkflowActionUpdateRequest =
                        TicketSubworkflowActionUpdateRequest()
                    ticketSubworkflowActionUpdateRequest.uid = responseList.get(position).uid
                    ticketSubworkflowActionUpdateRequest.comment = "${remark.text.toString()}"
                    ticketSubworkflowActionUpdateRequest.employee_id =
                        "${Preferences.getValidatedEmpId()}"//"RH75774748" //"SE35674"
                    // "SE35674"//${Preferences.getValidatedEmpId()}
                    ticketSubworkflowActionUpdateRequest.items_uid =
                        data.ticket_inventory!!.ticket_inventory_item!!.get(0).uid
                    if (row.action!!.code != null && row.action!!.code.equals("forward_to_manager")) {
                        ticketSubworkflowActionUpdateRequest.items_uid =
                            data.ticket_inventory!!.ticket_inventory_item!!.get(0).uid
                        var manager = TicketSubworkflowActionUpdateRequest.Manager()
                        manager.uid = data.manager!!.uid
                        ticketSubworkflowActionUpdateRequest.manager = manager
                        var ticket = TicketSubworkflowActionUpdateRequest.Ticket()
                        ticket.uid = responseList.get(position).uid
                        ticketSubworkflowActionUpdateRequest.ticket = ticket
                    }

                    if (row.action!!.code != null && row.action!!.code.equals("change_forward_manager") && row.assignToUser!!.uid!!.equals(
                            "Yes"
                        )
                    ) {
                        ticketSubworkflowActionUpdateRequest.items_uid =
                            data.ticket_inventory!!.ticket_inventory_item!!.get(0).uid
                        var manager = TicketSubworkflowActionUpdateRequest.Manager()
                        manager.uid = userForsubworkflow!!.uid
                        ticketSubworkflowActionUpdateRequest.manager = manager
                        var ticket = TicketSubworkflowActionUpdateRequest.Ticket()
                        ticket.uid = responseList.get(position).uid
                        ticketSubworkflowActionUpdateRequest.ticket = ticket

                        var oldManager = TicketSubworkflowActionUpdateRequest.OldManager()
                        oldManager.uid = data.manager!!.uid
                        ticketSubworkflowActionUpdateRequest.old_manager = oldManager

                        var toUser = TicketSubworkflowActionUpdateRequest.ToUser()
                        toUser.uid = userForsubworkflow.uid
                        toUser.firstName = userForsubworkflow.firstName
                        toUser.middleName = userForsubworkflow.middleName
                        toUser.lastName = userForsubworkflow.lastName
                        toUser.loginUnique = userForsubworkflow.loginUnique
                        var role = TicketSubworkflowActionUpdateRequest.Role()
                        role.uid = userForsubworkflow.role!!.uid
                        role.code = userForsubworkflow.role!!.code
                        role.name = userForsubworkflow.role!!.name
                        toUser.role = role
                        var level = TicketSubworkflowActionUpdateRequest.Level()
                        level.uid = userForsubworkflow.level!!.uid
                        level.name = userForsubworkflow.level!!.name
                        toUser.level = level
                        ticketSubworkflowActionUpdateRequest.toUser = toUser


                        var site = TicketSubworkflowActionUpdateRequest.Site()
                        site.uid = data.site.uid
                        site.site = data.site.site
                        ticketSubworkflowActionUpdateRequest.site = site
                    }

                    var subworkflow = TicketSubworkflowActionUpdateRequest.Subworkflow()
                    subworkflow.uid = row.uid!!
                    ticketSubworkflowActionUpdateRequest.subworkflow = subworkflow

                    if (row.action!!.code.equals("forward") && row.assignToUser!!.uid!!.equals("Yes")) {
                        var toUser = TicketSubworkflowActionUpdateRequest.ToUser()
                        toUser.uid = userForsubworkflow.uid
                        toUser.firstName = userForsubworkflow.firstName
                        toUser.middleName = userForsubworkflow.middleName
                        toUser.lastName = userForsubworkflow.lastName
                        toUser.loginUnique = userForsubworkflow.loginUnique
                        var role = TicketSubworkflowActionUpdateRequest.Role()
                        role.uid = userForsubworkflow.role!!.uid
                        role.code = userForsubworkflow.role!!.code
                        role.name = userForsubworkflow.role!!.name
                        toUser.role = role
                        var level = TicketSubworkflowActionUpdateRequest.Level()
                        level.uid = userForsubworkflow.level!!.uid
                        level.name = userForsubworkflow.level!!.name
                        toUser.level = level
                        ticketSubworkflowActionUpdateRequest.toUser = toUser
                    }

                    viewModel.actionUpdateApiCall(
                        this,
                        ticketSubworkflowActionUpdateRequest,
                        row, remark.text.toString(),
                        data,
                        responseList,
                        position,
                    )
                }
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun filterUserList(
        searchText: String,
        dialogUserListBinding: DialogUserListBinding?,
    ) {
        val filteredList = java.util.ArrayList<UserListForSubworkflowResponse.Rows>()
        for (i in usersList!!.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(usersList!!)
            } else {
                var userName = ""
                if (usersList!![i].firstName != null) {
                    userName = "${usersList!![i].firstName}"
                }
                if (usersList!![i].middleName != null) {
                    userName = "$userName ${usersList!![i].middleName}"
                }
                if (usersList!![i].lastName != null) {
                    userName = "$userName ${usersList!![i].lastName}"
                }
                if (usersList!![i].loginUnique != null) {
                    userName = "$userName (${usersList!![i].loginUnique})"
                }

                if (userName!!.contains(searchText, true)) {
                    filteredList.add(usersList!![i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogUserListBinding!!.locationRcv.visibility = View.GONE
            dialogUserListBinding.locationAvailable.visibility = View.VISIBLE
        } else {
            dialogUserListBinding!!.locationRcv.visibility = View.VISIBLE
            dialogUserListBinding.locationAvailable.visibility = View.GONE
        }
        userListAdapter.filter(filteredList)
    }

//    fun showActionPopup(
//        data: TicketData,
//        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
//        position: Int,
//        row: SubworkflowConfigDetailsResponse.Rows,
//        userListForSubworkflowResponse: UserListForSubworkflowResponse?,
//    ) {
//
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_comment)
//        val body = dialog.findViewById(R.id.textHead) as TextView
//        body.text = "Ticket Details"
//        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
//        ticketNo.text = data.ticket_id
//        val regDate = dialog.findViewById(R.id.regDate) as TextView
//        regDate.text = Utlis.convertCmsDate(data.created_time)
//        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
//        problemDesc.text = data.reason.name
//        val remark = dialog.findViewById(R.id.remark) as EditText
//        val yesBtn = dialog.findViewById(R.id.submit) as Button
//        yesBtn.text = row.action!!.name!!
//        val noBtn = dialog.findViewById(R.id.reject) as Button
//        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
//        var userDropdownLayout =
//            dialog.findViewById(R.id.user_dropdown_layout) as LinearLayout
//        var userForsubworkflow = UserListForSubworkflowResponse.Rows()
//        if (row.action!!.code.equals("forward") && row.assignToUser!!.uid!!.equals("Yes")) {
//            if (userListForSubworkflowResponse != null
//                && userListForSubworkflowResponse.success!!
//                && userListForSubworkflowResponse.data != null
//                && userListForSubworkflowResponse.data!!.listData != null
//                && userListForSubworkflowResponse.data!!.listData!!.rows != null
//                && userListForSubworkflowResponse.data!!.listData!!.rows!!.size > 0
//            ) {
//                userDropdownLayout.visibility = View.VISIBLE
//                val spinner = dialog.findViewById(R.id.user_list_for_subworkflow_spinner) as Spinner
//                var usersListforSubworkflowSpinnerAdapter =
//                    UsersListforSubworkflowSpinnerAdapter(
//                        applicationContext,
//                        userListForSubworkflowResponse.data!!.listData!!.rows!!
//                    )
//                spinner.adapter = usersListforSubworkflowSpinnerAdapter
//                spinner.setSelection(0)
//                spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(
//                        parent: AdapterView<*>?,
//                        view: View,
//                        positionDropDown: Int,
//                        id: Long,
//                    ) {
//                        userForsubworkflow =
//                            userListForSubworkflowResponse.data!!.listData!!.rows!!.get(
//                                positionDropDown
//                            )
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>?) {}
//                })
//            } else {
//                userDropdownLayout.visibility = View.GONE
//            }
//        } else {
//            userDropdownLayout.visibility = View.GONE
//        }
//
//        dialogClose.setOnClickListener { dialog.dismiss() }
//        yesBtn.setOnClickListener {
//            if (remark.text.toString().isEmpty()) {
//                remark.error = "Please enter comment"
//                remark.requestFocus()
//            } else {
//                dialog.dismiss()
//                if (NetworkUtil.isNetworkConnected(applicationContext)) {
//                    showLoading(this)
//                    var ticketSubworkflowActionUpdateRequest =
//                        TicketSubworkflowActionUpdateRequest()
//                    ticketSubworkflowActionUpdateRequest.uid = responseList.get(position).uid
//                    ticketSubworkflowActionUpdateRequest.comment = "${remark.text.toString()}"
//                    ticketSubworkflowActionUpdateRequest.employee_id =
//                        "${Preferences.getValidatedEmpId()}"//"RH75774748" //"SE35674"
//                    // "SE35674"//${Preferences.getValidatedEmpId()}
//                    var subworkflow = TicketSubworkflowActionUpdateRequest.Subworkflow()
//                    subworkflow.uid = row.uid!!
//                    ticketSubworkflowActionUpdateRequest.subworkflow = subworkflow
//
//                    if (row.action!!.code.equals("forward") && row.assignToUser!!.uid!!.equals("Yes")) {
//                        var toUser = TicketSubworkflowActionUpdateRequest.ToUser()
//                        toUser.uid = userForsubworkflow.uid
//                        toUser.firstName = userForsubworkflow.firstName
//                        toUser.middleName = userForsubworkflow.middleName
//                        toUser.lastName = userForsubworkflow.lastName
//                        toUser.loginUnique = userForsubworkflow.loginUnique
//                        var role = TicketSubworkflowActionUpdateRequest.Role()
//                        role.uid = userForsubworkflow.role!!.uid
//                        role.code = userForsubworkflow.role!!.code
//                        role.name = userForsubworkflow.role!!.name
//                        toUser.role = role
//                        var level = TicketSubworkflowActionUpdateRequest.Level()
//                        level.uid = userForsubworkflow.level!!.uid
//                        level.name = userForsubworkflow.level!!.name
//                        toUser.level = level
//                        ticketSubworkflowActionUpdateRequest.toUser = toUser
//                    }
//
//                    viewModel.actionUpdateApiCall(
//                        this,
//                        ticketSubworkflowActionUpdateRequest,
//                        row, remark.text.toString(),
//                        data,
//                        responseList,
//                        position,
//                    )
//                }
//            }
//        }
//        noBtn.setOnClickListener { dialog.dismiss() }
//        dialog.show()
//    }

    override fun onSuccessBack(complaintsListDetailsCallback: ComplaintsListDetailsCallback) {
        onClickBack()
    }

    override fun onSuccessSubworkflowAssignedtoMeApiCall(subworkFlowAssignedtoMeResponse: SubworkFlowAssignedtoMeResponse) {

    }

    override fun onSuccessSubWorkflowAcceptApiCall() {
        TODO("Not yet implemented")
    }

    override fun onSuccessSubWorkflowRejectApiCall() {
        TODO("Not yet implemented")
    }

    override fun onSuccessUsersListforSubworkflow(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
        userListForSubworkflowResponse: UserListForSubworkflowResponse?,
        historyCallback: HistoryCallback,
    ) {
        hideLoading()
        showActionPopup(data, responseList, position, row, userListForSubworkflowResponse)
//        historyCallback.onSuccessUsersListforSubworkflow(ticketData, responseList, position, row, request, historyCallback)
    }

    override fun onSuccessActionUpdate(
        ticketSubworkflowActionUpdateResponse: TicketSubworkflowActionUpdateResponse?,
        row: SubworkflowConfigDetailsResponse.Rows,
        remark: String,
        data1: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {

        hideLoading()
        responseList.get(position).isExpanded = false

        if (ticketSubworkflowActionUpdateResponse != null && ticketSubworkflowActionUpdateResponse.success!! && ticketSubworkflowActionUpdateResponse.data != null && ticketSubworkflowActionUpdateResponse.data!!.status != null && ticketSubworkflowActionUpdateResponse.data!!.status != null) {
            responseList.get(position).status!!.code =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.code!! //"solved"
            responseList.get(position).status!!.name =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.name!! //"Resolved"
            responseList.get(position).status!!.background_color =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.background_color //"#047604"
            responseList.get(position).status!!.text_color =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.text_color!! //"#FFFFFF"
        }
        if (ticketSubworkflowActionUpdateResponse != null && ticketSubworkflowActionUpdateResponse.success!!
            && ticketSubworkflowActionUpdateResponse.data!! != null && ticketSubworkflowActionUpdateResponse.data!!.ticketSubworkflowInfo != null
        ) {
            responseList.get(position).ticketSubworkflowInfo =
                ticketSubworkflowActionUpdateResponse.data!!.ticketSubworkflowInfo!!
        }
        if (ticketSubworkflowActionUpdateResponse != null && ticketSubworkflowActionUpdateResponse.success!!
            && ticketSubworkflowActionUpdateResponse.data!! != null && ticketSubworkflowActionUpdateResponse.data!!.ticket_subworkflow_history != null
        ) {
            responseList.get(position).ticket_subworkflow_history =
                ticketSubworkflowActionUpdateResponse.data!!.ticket_subworkflow_history!!
        }
        orderData = responseList
        onClickBack()
//        historyCallback.onSuccessActionUpdate(request, row, remark, data1, responseList, position, historyCallback)
    }

    override fun onSuccessSubworkflowConfigDetailsApi(
        responseSubworkflowConfigDetailsResponse: SubworkflowConfigDetailsResponse,
        position: Int,
    ) {
        Utlis.hideLoading()
        orderDataWp.subworkflowConfigDetailsResponse =
            responseSubworkflowConfigDetailsResponse
    }

    override fun onFailureSubworkflowConfigDetailsApi(message: String) {
        Utlis.hideLoading()
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickTicketResolveHis(
        data: ResponseNewTicketlist.Row,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Resolve"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "resolve",
                    remark.text.toString(),
                    userData.EMPID,
                    "solved",
                    data.ticket_id!!,
                    null
                )
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                responseList.get(position).status!!.code = "solved"

                data.isExpanded = false

//                adapter.notifyDataSetChanged()
                viewModel.actionTicketResolveClose(
                    inventoryAcceptrejectModel,
                    historyCallback,
                    this
                )
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
//        historyCallback.onClickTicketResolveHis(orderDataWp, orderData, position, historyCallback)
    }

    override fun onItemClick(position: Int, imagePath: String) {

    }

    override fun onComplaintItemClick(
        position: Int,
        orderData: java.util.ArrayList<ResponseNewTicketlist.Row>,
        get: ResponseNewTicketlist.Row,
    ) {

    }

    override fun onClickForwardToFinance(
        data: CmsTicketRequest,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        showLoading(this)

        resList = responseList
        pos = position
        viewModel.cmsTicketStatusUpdate(data)
    }

    override fun onClickCCAccept(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        val dialog = Dialog(applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Accept"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val ccAcceptRejectModel = CCAcceptRejectModel()
                ccAcceptRejectModel.setAction("resolve")
                ccAcceptRejectModel.setComment(remark.text.toString())
                ccAcceptRejectModel.setEmployee_id(userData.EMPID)
                ccAcceptRejectModel.setPos_status("approve")
                ccAcceptRejectModel.setStatus("inprogress")
                ccAcceptRejectModel.setTicket_id(data.ticket_id)
                val ticket_it = CCAcceptRejectModel.Ticket_it()
                ticket_it.setUid("approved")
                ccAcceptRejectModel.setTicket_it(ticket_it)
                ccAcceptRejectModel.setUid(data.uid)
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "solved"
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionCCAccept(ccAcceptRejectModel, 0, this)

//                adapter.notifyDataSetChanged()

//                \"background_color\":\"#047604\",\"code\":\"solved\",\"name\":\"Resolved\",\"text_color\":\"#FFFFFF\
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickCCReject(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        val dialog = Dialog(applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reject"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val ccAcceptRejectModel = CCAcceptRejectModel()
                ccAcceptRejectModel.setAction(null)
                ccAcceptRejectModel.setComment(remark.text.toString())
                ccAcceptRejectModel.setEmployee_id(userData.EMPID)
                ccAcceptRejectModel.setPos_status(null)
                ccAcceptRejectModel.setStatus("reject")
                ccAcceptRejectModel.setTicket_id(data.ticket_id)
                val ticket_it = CCAcceptRejectModel.Ticket_it()
                ticket_it.setUid("rejected")
                ccAcceptRejectModel.setTicket_it(ticket_it)
                ccAcceptRejectModel.setUid(data.uid)
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "rejected"
                responseList.get(position).status!!.name = "Rejected"
                responseList.get(position).status!!.background_color = "#ed001c"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionCCAccept(ccAcceptRejectModel, 0, this)

//                adapter.notifyDataSetChanged()


//                "background_color\":\"#ed001c\",\"code\":\"rejected\",\"name\":\"Rejected\",\"text_color\":\"#FFFFFF\"}
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickInventoryAccept(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
    ) {
        val dialog = Dialog(applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Accept"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = InventoryAcceptrejectModel(
                    null,
                    remark.text.toString(),
                    ItemStatus("approved"),
                    data.site!!.site!!,
                    data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    "inprogress",
                    data.ticketDetailsResponse!!.data.ticket_id,
                    "resolve"
                )
                var workFlowUpdateModel = WorkFlowUpdateModel(
                    Action("97A318ACE84930236386DB1A70944825", "resolve"),
                    "Resolved",
                    Department(
                        data.ticketDetailsResponse?.data?.department?.uid,
                        data.ticketDetailsResponse?.data?.department?.code
                    ),
                    Level(
                        data.ticketDetailsResponse?.data?.level?.uid,
                        data.ticketDetailsResponse?.data?.level?.code
                    ),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(
                        data.ticketDetailsResponse?.data?.user?.first_name,
                        data.ticketDetailsResponse?.data?.user?.uid
                    ),
                    "Yes",
                    "manager"
                )
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "solved"
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionInventoryAcceptReject(
                    inventoryAcceptrejectModel, workFlowUpdateModel, 0, this
                )
//                adapter.notifyDataSetChanged()


//                \"background_color\":\"#047604\",\"code\":\"solved\",\"name\":\"Resolved\",\"text_color\":\"#FFFFFF\
//                viewModel.actionInventoryAcceptReject(inventoryAcceptrejectModel,
//                    workFlowUpdateModel,
//                    0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickInventoryReject(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
    ) {
        val dialog = Dialog(applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reject"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = InventoryAcceptrejectModel(
                    remark.text.toString(),
                    null,
                    ItemStatus("rejected"),
                    data.site!!.site!!,
                    data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    "reject",
                    data.ticketDetailsResponse!!.data.ticket_id,
                    null

                )
                var workFlowUpdateModel = WorkFlowUpdateModel(
                    Action("52E2C8F5C204B5BD03DF3A73EB096484", "reject"),
                    "Resolved",
                    Department(
                        data.ticketDetailsResponse?.data?.department?.uid,
                        data.ticketDetailsResponse?.data?.department?.code
                    ),
                    Level(
                        data.ticketDetailsResponse?.data?.level?.uid,
                        data.ticketDetailsResponse?.data?.level?.code
                    ),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(
                        data.ticketDetailsResponse?.data?.user?.first_name,
                        data.ticketDetailsResponse?.data?.user?.uid
                    ),
                    "Yes",
                    "manager"
                )
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "rejected"
                responseList.get(position).status!!.name = "Rejected"
                responseList.get(position).status!!.background_color = "#ed001c"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionInventoryAcceptReject(
                    inventoryAcceptrejectModel, workFlowUpdateModel, 0, this
                )
//                adapter.notifyDataSetChanged()

//                background_color\":\"#ed001c\",\"code\":\"rejected\",\"name\":\"Rejected\",\"text_color\":\"#FFFFFF

            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickForwardToManager(data: ResponseNewTicketlist.Row) {
        val dialog = Dialog(applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Forward to Manager"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
                showLoading(this)
                var forwardToManagerModel = ForwardToManagerModel(
                    remark.text.toString(),
                    Manager(""),//data.inventoryDetailsModel!!.data.ticket_inventory.ticket_inventory_item[0].manager.uid
                    FMTicket(data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    userData.EMPID
                )
                viewModel.actionForwardToManager(forwardToManagerModel, 0, this)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickForwardChangeManager(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
    ) {
        resListMnger = responseList
        frwdMngrPos = pos
        selectedInventeryTicket = data
//        viewModel.getManagersLiveData.observe(viewLifecycleOwner, Observer {
//            SearchManagerDialog(it).apply { }.show(childFragmentManager, "")
//        })
        viewModel.getManagers(data.site?.uid!!, this)
    }

    override fun onClickTicketResolve(
        data: ResponseNewTicketlist.Row,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickTicketClose(
        data: ResponseNewTicketlist.Row,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickTicketReopen(
        data: ResponseNewTicketlist.Row,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickSubWorkflowAccept(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickSubWorkflowReject(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickAction(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
    ) {

    }

    override fun onClickPreviewIconBackOther(url: String?, view: View) {
        if (!url.isNullOrEmpty()) {
            PhotoPopupWindow(applicationContext, R.layout.layout_image_fullview, view, url, null)
        }
    }


}