package com.apollopharmacy.vishwam.ui.home.cms.complainList

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.text.InputFilter
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.*
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.*
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Department
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.ItemStatus
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Manager
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Site
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Status
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utils.getDateDifference
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.hsalf.smilerating.SmileRating
import java.util.*

class ComplainListFragment : BaseFragment<ComplainListViewModel, FragmentComplaintsBinding>(),
    ImageClickListener, ComplaintListCalendarDialog.DateSelected, MainActivityCallback,
    OnTransactionSearchManagerListnier {

    var isFromDateSelected: Boolean = false

    lateinit var storeData: LoginDetails.StoreData

    var complaintListStatus: String = "new,inprogress,solved,rejected,reopened,closed,onHold"

    // var TicketHistorydata:ArrayList<NewTicketHistoryResponse.Row>()

    override fun onPause() {
        super.onPause()
        MainActivity.mInstance.filterIndicator.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        setFilterIndication()
    }

    override val layoutRes: Int
        get() = R.layout.fragment_complaints

    lateinit var adapter: ApproveRecyclerView
    lateinit var userData: LoginDetails
    lateinit var responseData: ResponseNewTicketlist
    private var isLoading: Boolean = false
    private var isFirstTime: Boolean = true
    lateinit var layoutManager: LinearLayoutManager
    var handler: Handler = Handler()
    var ticketratingapiresponse: ResponseticketRatingApi.Data? = null
    override fun retrieveViewModel(): ComplainListViewModel {
        return ViewModelProvider(this).get(ComplainListViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this
        viewBinding.viewmodel = viewModel
        setFilterIndication()
        val siteId = Preferences.getSiteId()
        userData = LoginRepo.getProfile()!!
        viewBinding.fromDateText.setText(Utils.getCurrentDate())
        viewBinding.toDateText.setText(Utils.getCurrentDate())

        viewBinding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClick()
        })

        viewBinding.searchView.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))

        layoutManager = LinearLayoutManager(context)
        //attaches LinearLayoutManager with RecyclerView
        viewBinding.recyclerViewApproved.layoutManager = layoutManager
        Utlis.showLoading(requireContext())
        viewModel.getTicketRatingApi()
        viewModel.cmsticketRatingresponse.observe(viewLifecycleOwner) {
            Utlis.hideLoading()
            ticketratingapiresponse = it.data;
            callAPI(1)
        }

//        viewBinding.searchView.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent): Boolean {
//                val DRAWABLE_LEFT = 0
//                val DRAWABLE_TOP = 1
//                val DRAWABLE_RIGHT = 2
//                val DRAWABLE_BOTTOM = 3
//                if (event.action === MotionEvent.ACTION_UP) {
//                    if (event.rawX >= viewBinding.searchView.getRight() - viewBinding.searchView.getCompoundDrawables()
//                            .get(DRAWABLE_RIGHT).getBounds().width()
//                    ) {
//                        // your action here
//                        viewBinding.searchView.setText("")
//                        return true
//                    }
//                }
//                return false
//            }
//        })


        viewBinding.searchView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    var fromDate =
                        Utils.getticketlistfiltersdate(viewBinding.fromDateText.text.toString())
                    var toDate =
                        Utils.getticketlistfiltersdate(viewBinding.toDateText.text.toString())
                    viewModel.getNewticketlist(
                        RequestComplainList(
                            Preferences.getSiteId(),
                            fromDate,
                            toDate,
                            userData.EMPID,
                            1
                        ), complaintListStatus, arguments?.getBoolean("isFromDrugList") ?: false,
                        true,
                        viewBinding.searchView.text.toString().trim()
                    )
                    return true
                }
                return false
            }
        })


        viewModel.resLiveData.observe(viewLifecycleOwner) {
            Utlis.hideLoading()
            if (viewBinding.pullToRefresh.isRefreshing) {
                viewBinding.pullToRefresh.isRefreshing = false
            }
            if (it.data.listData.rows.size == 0) {
                isLoadMoreAvailable = false
                if (isLoading) {
                    adapter.getData().removeAt(adapter.getData().size - 1)
                    var listSize = adapter.getData().size
                    adapter.notifyItemRemoved(listSize)
//                    adapter.getData().addAll(it.data.listData.rows)
                    adapter.notifyDataSetChanged()
                    isLoading = false
                } else {
                    viewBinding.recyclerViewApproved.visibility = View.GONE
                    viewBinding.emptyList.visibility = View.VISIBLE
                }
            } else {
                if (it.data.listData.rows.size < 10) {
                    isLoadMoreAvailable = false
                }
                responseData = it
                viewBinding.emptyList.visibility = View.GONE
                viewBinding.recyclerViewApproved.visibility = View.VISIBLE
                if (isLoading) {
                    adapter.getData().removeAt(adapter.getData().size - 1)
                    var listSize = adapter.getData().size
                    adapter.notifyItemRemoved(listSize)
                    adapter.getData().addAll(it.data.listData.rows)
                    adapter.notifyDataSetChanged()
                    isLoading = false
                } else {
                    adapter = ApproveRecyclerView(it.data.listData.rows, this)
                    viewBinding.recyclerViewApproved.adapter = adapter
                }
            }
        }
        //tickethistory api response...............................................................
        viewModel.newtickethistoryLiveData.observe(viewLifecycleOwner, Observer {

            // if (it.size == 0) {
            //  } else {

            var ticketHistory: ArrayList<ResponseNewTicketlist.NewTicketHistoryResponse.Row>
            ticketHistory = it.data.listData.rows
            var selectedPostion = 0
            for (item in ticketHistory) {
                var uid = item.ticket.uid
                var itemPos: Int = -1
                for (ticketrow in adapter.orderData) {
                    itemPos++
                    if (uid.equals(ticketrow.uid)) {
                        item.status = ticketrow.status?.name
                        adapter.orderData[itemPos].Tickethistory = it
                        selectedPostion = itemPos
                        break
                    }
                }
            }
            viewModel.getTicketFullDetails(adapter.orderData[selectedPostion].ticket_id,
                selectedPostion)
            //  adapter.notifyAdapter()

        })

//        viewModel.inventoryDetailsLiveData.observe(viewLifecycleOwner, Observer {
//            adapter.orderData[it.position].ticketDetailsResponse.data.ticket_inventory = it
//            Utlis.hideLoading()
//            adapter.notifyItemChanged(it.position)
//
//        })

        viewModel.ticketDetailsResponseLiveData.observe(viewLifecycleOwner, Observer {
            adapter.orderData[it.position].ticketDetailsResponse = it
            Utlis.hideLoading()
            adapter.notifyItemChanged(it.position)

        })

//        viewModel.creditCardDetailsLiveData.observe(viewLifecycleOwner, Observer {
//            adapter.orderData[it.position].ticketDetailsResponse.data.ticket_it = it
//            Utlis.hideLoading()
//            adapter.notifyItemChanged(it.position)
//        })
        /* viewModel.complainLiveData.observe(viewLifecycleOwner, Observer {
             Utlis.hideLoading()
             if (it.size == 0) {
                 viewBinding.recyclerViewApproved.visibility = View.GONE
                 viewBinding.emptyList.visibility = View.VISIBLE
             } else {
                 viewBinding.emptyList.visibility = View.GONE
                 viewBinding.recyclerViewApproved.visibility = View.VISIBLE
                 adapter = ApproveRecyclerView(it, this)
                 viewBinding.recyclerViewApproved.adapter = adapter
             }
         })*/
        viewModel.command.observe(viewLifecycleOwner, Observer {
            Utlis.hideLoading()
            when (it) {
                is CmsCommand.VisibleLayout -> {
                    viewBinding.emptyList.visibility = View.VISIBLE
                }
                is CmsCommand.ShowToast -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is CmsCommand.RefreshPageOnSuccess -> {
                    hideLoading()
                    submitClick()
                }
                else -> {}
            }
        })

        viewBinding.fromDateText.setOnClickListener {
            isFromDateSelected = true
            openDateDialog()
        }

        viewBinding.toDateText.setOnClickListener {
            isFromDateSelected = false
            openDateDialog()
        }

        viewBinding.submit.setOnClickListener {
            submitClick()
        }

        addScrollerListener()
    }

    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        viewBinding.recyclerViewApproved.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isFirstTime) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getData().size - 1) {
                        loadMore()
                    }
                }
            }
        })
    }

    fun setFilterIndication() {
        if (!this.complaintListStatus.contains("new")
            || !this.complaintListStatus.contains("inprogress")
            || !this.complaintListStatus.contains("solved")
            || !this.complaintListStatus.contains("rejected")
            || !this.complaintListStatus.contains("reopened")
            || !this.complaintListStatus.contains("closed")
            || !this.complaintListStatus.contains("onHold")
        ) {
            MainActivity.mInstance.filterIndicator.visibility = View.VISIBLE
        } else {
            MainActivity.mInstance.filterIndicator.visibility = View.GONE

        }
    }

    var isLoadMoreAvailable = true
    private fun loadMore() {
        //notify adapter using Handler.post() or RecyclerView.post()
        handler.post(Runnable
        {
            if (isLoadMoreAvailable) {
                isLoading = true
                val newdata = ResponseNewTicketlist.Row(
                    "",
                    "",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    false,
                    null,
                    null

                )
                adapter.getData().add(newdata)
                adapter.notifyItemInserted(adapter.getData().size - 1)
                callAPI(responseData.data.listData.page!! + 1)
            }
        })
    }

    fun callAPI(page: Int) {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            isFirstTime = false
            var fromDate = Utils.getticketlistfiltersdate(viewBinding.fromDateText.text.toString())
            var toDate = Utils.getticketlistfiltersdate(viewBinding.toDateText.text.toString())
            if (!isLoading)
                Utlis.showLoading(requireContext())

            viewModel.getNewticketlist(
                RequestComplainList(
                    //"11002",
                    Preferences.getSiteId(),
                    // viewBinding.fromDateText.text.toString(),
                    // viewBinding.toDateText.text.toString(),
                    fromDate,
                    toDate,
                    userData.EMPID,
                    page
                ), complaintListStatus, this.arguments?.getBoolean("isFromDrugList") ?: false,
                false, viewBinding.searchView.text.toString().trim()
            )

        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun submitClick() {
        var fromDate = viewBinding.fromDateText.text.toString()
        var toDate = viewBinding.toDateText.text.toString()
        if (getDateDifference(fromDate, toDate) > 0) {
            if (!viewBinding.pullToRefresh.isRefreshing)
                Utlis.showLoading(requireContext())
            isLoadMoreAvailable = true
            callAPI(1)
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_check_dates),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(viewBinding.fromDateText.text.toString(),
                    false,
                    viewBinding.fromDateText.text.toString())
            }.show(childFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(viewBinding.toDateText.text.toString(),
                    true,
                    viewBinding.fromDateText.text.toString())
            }.show(childFragmentManager, "")
        }
    }

    //new ticket list data recycler views..............
    class ApproveRecyclerView(
        var orderData: ArrayList<ResponseNewTicketlist.Row>,
        val imageClickListener: ImageClickListener,

        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var orderItemsId = ArrayList<String>()
        var tickethistory = ArrayList<NewTicketHistoryResponse.Row>()

        //var historyitem : NewTicketHistoryResponse.Row()
        var tickethistoryresponsenew = ArrayList<NewTicketHistoryResponse.Row>()

        companion object {
            private const val VIEW_TYPE_DATA = 0
            private const val VIEW_TYPE_PROGRESS = 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
            return when (viewtype) {
                VIEW_TYPE_DATA -> {//inflates row layout
                    val binding = DataBindingUtil.inflate<ViewComplaintItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.view_complaint_item,
                        parent,
                        false
                    )
                    DataViewHolder(binding)
                }
                VIEW_TYPE_PROGRESS -> {//inflates progressbar layout
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.progressbar, parent, false)
                    ProgressViewHolder(view)
                }
                else -> throw IllegalArgumentException("Different View type")
            }
        }

        fun getData(): ArrayList<ResponseNewTicketlist.Row> {
            return orderData
        }

        fun getItem(position: Int): ResponseNewTicketlist.Row {
            return orderData[position]
        }

        override fun getItemCount(): Int {
            return orderData.size
        }

        override fun getItemViewType(position: Int): Int {
            var viewtype = orderData.get(position)
            //if data is load, returns PROGRESSBAR viewtype.
            return if (viewtype.uid.isNullOrEmpty()) {
                VIEW_TYPE_PROGRESS
            } else VIEW_TYPE_DATA

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
            if (holder is DataViewHolder) {
                holder.bind(getItem(p1))
            }
        }

        inner class DataViewHolder(private val binding: ViewComplaintItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(items: ResponseNewTicketlist.Row) {
                bindItems(binding, items, adapterPosition)
            }
        }

        inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        fun bindItems(
            binding: ViewComplaintItemBinding,
            items: ResponseNewTicketlist.Row,
            position: Int,

            ) {
            binding.ticketNumber.text = items.ticket_id
            binding.regDate.text = items.created_time?.let {
                Utlis.cmsComplaintDateFormat(
                    it
                )
            }

            lateinit var userData: LoginDetails
            userData = LoginRepo.getProfile()!!

//            lateinit var StoreData: StoreData
//            StoreData = LoginRepo.getStoreData()!!
//            if(items.status.code != "new")
//                binding.staffNameText.text = items.user.first_name + if(items.user.middle_name != null)  " "+ items.user.middle_name else "" +if(items.user.last_name != null)   " "+ items.user.last_name else ""
//            else
            binding.staffNameText.text =
                items.ticketDetailsResponse?.data?.created_id?.first_name + (if (items.ticketDetailsResponse?.data?.created_id?.middle_name != null) " " + items.ticketDetailsResponse?.data?.created_id?.middle_name else "") + (if (items.ticketDetailsResponse?.data?.created_id?.last_name != null) " " + items.ticketDetailsResponse?.data?.created_id?.last_name else "") + " (" + items.ticketDetailsResponse?.data?.created_id?.login_unique + ")"
            binding.departmentName.text = items.ticketDetailsResponse?.data?.department?.name
            binding.problemSinceText.text = items.created_time?.let {
                Utlis.convertCmsDate(it)
            }

            var empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()
            var managerUid: String? = null
            var employeeDetailsResponse: EmployeeDetailsResponse? = null
            try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(empDetailsResponse,
                    EmployeeDetailsResponse::class.java)

            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
            var isDonthaveInventory = false
            if (items.ticketDetailsResponse?.data?.ticket_inventory?.uid != null && items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item.isNotEmpty()) {
                binding.inventoryDetailsLayout.visibility = View.VISIBLE
//                binding.articleCode.text =
//                    "${items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item[0].item_code}"
//              if (items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item)
                binding.articleName.text =
                    "${items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_name}"
                binding.batchNumber.text =
                    " ${items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].batch_no}"
                binding.barcode.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].barcode
                        ?: "--"
                binding.expairyDate.text =
                    Utlis.convertCmsExparyDate(items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].expiry_date)

                binding.purchaseRate.text =
                    "₹ ${items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].purchase_rate}"
                if (items.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req")) {
                    binding.oldMrpLabel.text = "MRP : "
                    binding.oldMrp.text =
                        "₹ ${items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].mrp}"
                    binding.newMrp.visibility = View.GONE
                    binding.newMrpLabel.visibility = View.GONE
                } else {
                    binding.oldMrp.text =
                        "₹ ${items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].old_mrp}"
                    binding.newMrp.text =
                        "₹ ${items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].new_mrp}"
                }
//                if(items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.first_name == null) {
//                    binding.manager.text =
//                        " ${items.inventoryDetailsModel?.data?.site?.manager?.first_name}"
//                }else {
//                    binding.manager.text =
//                        " ${items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.first_name}"
//                }
//                binding.category.text =
//                    " ${items.inventoryDetailsModel?.data?.category!!.name}"
//                binding.subCategory.text =
//                    " ${items.inventoryDetailsModel?.data?.subcategory!!.name}"
//                binding.reason.text =
//                    " ${items.inventoryDetailsModel?.data?.reason!!.name}"

                if (items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.uid == null) {
                    managerUid = items.ticketDetailsResponse?.data?.site?.manager?.uid!!
                } else {
                    managerUid =
                        items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.uid
                }

                if (items.status!!.code.equals("inprogress") || items.status!!.code.equals("reopen")) {
                    if (items.ticketDetailsResponse?.data?.category?.code.equals("mrp_cr") && items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid != null &&
                        items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid.equals(
                            "forward") &&
                        managerUid.equals(employeeDetailsResponse?.data!!.uid)
                    ) {
                        binding.inventoryActionLayout.visibility = View.VISIBLE
                        binding.inventoryRejectBtn.visibility = View.VISIBLE
                        binding.inventoryForwardManagerBtn.visibility = View.GONE
                        binding.inventoryChangeForwardBtn.visibility = View.VISIBLE
                        binding.inventoryAcceptBtn.text = "Approve"
                    } else if (items.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req") &&
                        items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid == null &&
                        employeeDetailsResponse?.data!!.uid.equals(items.ticketDetailsResponse?.data?.user!!.uid)
                    ) {
                        binding.inventoryActionLayout.visibility = View.VISIBLE
                        binding.inventoryRejectBtn.visibility = View.VISIBLE
                        binding.inventoryForwardManagerBtn.visibility = View.GONE
                        binding.inventoryChangeForwardBtn.visibility = View.GONE
                        binding.inventoryAcceptBtn.text = "Approve"
                    }
                    binding.inventoryAcceptBtn.setOnClickListener {
                        imageClickListener.onClickInventoryAccept(items)
                    }
                    binding.inventoryRejectBtn.setOnClickListener {
                        imageClickListener.onClickInventoryReject(items)
                    }
                    binding.inventoryForwardManagerBtn.setOnClickListener {
                        imageClickListener.onClickForwardToManager(items)
                    }
                    binding.inventoryChangeForwardBtn.setOnClickListener {
                        imageClickListener.onClickForwardChangeManager(items)
                    }
                } else {
                    binding.inventoryActionLayout.visibility = View.GONE
                }
                binding.inventoryImagesLayout.visibility = View.VISIBLE
                if (!items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(binding.frontImgView)
                    binding.frontImgView.setOnClickListener {
                        items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.let { it1 ->
                            imageClickListener.onItemClick(position,
                                it1)
                        }
                    }
                } else {
                    binding.frontImgLabel.visibility = View.GONE
                    binding.frontImgView.visibility = View.GONE
                }
                if (!items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(binding.backImgView)
                    binding.backImgView.setOnClickListener {
                        items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.let { it1 ->
                            imageClickListener.onItemClick(position,
                                it1)
                        }
                    }
                } else {
                    binding.backImgLabel.visibility = View.GONE
                    binding.backImgView.visibility = View.GONE
                }
                if (!items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(binding.otherImgView)
                    binding.otherImgView.setOnClickListener {
                        items.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.let { it1 ->
                            imageClickListener.onItemClick(position,
                                it1)
                        }
                    }
                    binding.otherImgLabel.visibility = View.VISIBLE
                    binding.otherImgView.visibility = View.VISIBLE
                } else {
                    binding.otherImgLabel.visibility = View.GONE
                    binding.otherImgView.visibility = View.GONE
                }
            } else {
                isDonthaveInventory = true
                binding.inventoryDetailsLayout.visibility = View.GONE
                binding.inventoryActionLayout.visibility = View.GONE
                binding.inventoryImagesLayout.visibility = View.GONE
            }
            items.created_id?.first_name + (if (items.created_id?.middle_name != null) " " + items.created_id?.middle_name else "") + (if (items.created_id?.last_name != null) " " + items.created_id?.last_name else "") + " (" + items.created_id?.login_unique + ")"
//            if(items.department?.code.equals("IT")){
            if (!TextUtils.isEmpty(items.ticketDetailsResponse?.data?.executive?.first_name)) {
                binding.itTicketExecutive.text =
                    items.ticketDetailsResponse?.data?.executive?.first_name + (if (items.ticketDetailsResponse?.data?.executive?.middle_name != null) " " + items.ticketDetailsResponse?.data?.executive?.middle_name else "") + (if (items.ticketDetailsResponse?.data?.executive?.last_name != null) " " + items.ticketDetailsResponse?.data?.executive?.last_name else "") + " (" + items.ticketDetailsResponse?.data?.executive?.login_unique + ")"
                binding.itTicketExecutiveLayout.visibility = View.VISIBLE
            } else {
                binding.itTicketExecutiveLayout.visibility = View.GONE
            }
            if (!TextUtils.isEmpty(items.ticketDetailsResponse?.data?.manager?.first_name)) {
                binding.itTicketManager.text =
                    items.ticketDetailsResponse?.data?.manager?.first_name + (if (items.ticketDetailsResponse?.data?.manager?.middle_name != null) " " + items.ticketDetailsResponse?.data?.manager?.middle_name else "") + (if (items.ticketDetailsResponse?.data?.manager?.last_name != null) " " + items.ticketDetailsResponse?.data?.manager?.last_name else "") + " (" + items.ticketDetailsResponse?.data?.manager?.login_unique + ")"
                binding.itTicketManagerLayout.visibility = View.VISIBLE
            } else {
                binding.itTicketManagerLayout.visibility = View.GONE
            }
//            }else{
//                binding.itTicketExecutiveLayout.visibility = View.GONE
//                binding.itTicketManagerLayout.visibility = View.GONE
//            }
            if (items.ticketDetailsResponse?.data?.ticket_it?.tid?.uid != null) {
                binding.creditCardDetailsLayout.visibility = View.VISIBLE
//                binding.ccReason.text = " ${items.creditCardTSDetails?.data?.reason!!.name }"
//                binding.ccExecutive.text = " ${items.creditCardTSDetails?.data?.executive!!.first_name }"
//                binding.ccManager.text = items.creditCardTSDetails?.data?.manager!!.first_name
                binding.ccTid.text = " ${items.ticketDetailsResponse?.data?.ticket_it!!.tid.tid}"
                binding.billNumber.text =
                    " ${items.ticketDetailsResponse?.data?.ticket_it!!.bill_number}"
                binding.transactionNumber.text =
                    " ${items.ticketDetailsResponse?.data?.ticket_it!!.transaction_id}"
                binding.approvalCode.text =
                    " ${items.ticketDetailsResponse?.data?.ticket_it!!.approval_code}"
                binding.billAmount.text =
                    "₹ ${items.ticketDetailsResponse?.data?.ticket_it!!.bill_amount}"
                if (((items.status!!.code.equals("new") || items.status!!.code.equals("reopen"))
                            && items.ticketDetailsResponse!!.data.executive.uid.equals(
                        employeeDetailsResponse?.data!!.uid)
                            && items.ticketDetailsResponse!!.data.reason.sub_workflow.uid.equals("Yes")
                            && checkResonDepot(items.ticketDetailsResponse!!.data.reason.reason_dept,
                        employeeDetailsResponse))
                ) {
                    if (items.ticketDetailsResponse!!.data.ticket_it.status.uid != null && (items.ticketDetailsResponse!!.data.ticket_it.status.uid.equals(
                            "approved") || items.ticketDetailsResponse!!.data.ticket_it.status.uid.equals(
                            "rejected"))
                    ) {
                        binding.ccActionLayout.visibility = View.GONE
                    } else {
                        binding.ccActionLayout.visibility = View.VISIBLE
                        binding.acceptBtn.setOnClickListener {
                            imageClickListener.onClickCCAccept(items.ticketDetailsResponse!!.data)
                        }
                        binding.rejectBtn.setOnClickListener {
                            imageClickListener.onClickCCReject(items.ticketDetailsResponse!!.data)
                        }
                    }
                } else {
                    binding.ccActionLayout.visibility = View.GONE
                }
            } else {
                binding.creditCardDetailsLayout.visibility = View.GONE
                binding.ccActionLayout.visibility = View.GONE
            }

            if (items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request?.uid != null) {
                binding.drugLayout.drugDetailsLayout.visibility = View.VISIBLE
//                binding.drugLayout.drugBarcode.text = items.ticket_inventory.drug_request.barcode ?: "--"
                binding.drugLayout.drugItemNumber.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.item_name ?: "--"
//                binding.drugLayout.drugItemDetailsNumber.text = items.subcategory?.name ?: "--"
                binding.drugLayout.drugPackSize.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.pack_size.toString() ?: "--"
                binding.drugLayout.drugMrp.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.mrp.toString() ?: "--"
//                binding.drugLayout.drugPurchasePrice.text =
//                    items.ticket_inventory.drug_request.purchase_price.toString() ?: "--"
//                binding.drugLayout.drugRemarks.text = items.ticket_inventory.drug_request.remarks ?: "--"
                binding.drugLayout.drugBatchNo.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.batch_no ?: "--"


                //From made changes by naveen//
                binding.drugLayout.itemType.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.item_type.name
                binding.drugLayout.requiredQty.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.required_quantity.toString()
                binding.drugLayout.doctorName.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.doctors_name
                binding.drugLayout.doctorSpeciality.text =
                    items.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.doctor_specialty.name

                //To made changes by naveen//


//                binding.drugLayout.drugManufactuing.text = if(items.ticket_inventory.drug_request?.manufacturing_date == null ){ "--"} else { Utlis.convertCmsExparyDate(items.ticket_inventory.drug_request.manufacturing_date)}
//                binding.drugLayout.drugExpairy.text =  if(items.ticket_inventory.drug_request?.expiry_date == null ){ "--"} else { Utlis.convertCmsExparyDate(items.ticket_inventory.drug_request.expiry_date)}
//                binding.drugLayout.drugHsn.text = items.ticket_inventory.drug_request.hsn_code ?: "--"
//                binding.drugLayout.drugGst.text = items.ticket_inventory.drug_request.gst ?: "--"
//                binding.drugLayout.drugReference.text = items.ticket_inventory.drug_request.reference_no ?: "--"

                if (items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.front_mb != null) {
                    Glide.with(context)
                        .load(items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.front_mb)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(binding.frontImgView)
                    binding.frontImgView.setOnClickListener {
                        items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.front_mb.let { it1 ->
                            imageClickListener.onItemClick(position,
                                it1)
                        }
                    }
                } else {
                    binding.frontImgLabel.visibility = View.GONE
                    binding.frontImgView.visibility = View.GONE
                }
                if (items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request?.back_mb != null) {
                    Glide.with(context)
                        .load(items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.back_mb)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(binding.backImgView)
                    binding.backImgView.setOnClickListener {
                        items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.back_mb.let { it1 ->
                            imageClickListener.onItemClick(position,
                                it1)
                        }
                    }
                } else {
                    binding.backImgLabel.visibility = View.GONE
                    binding.backImgView.visibility = View.GONE
                }
                binding.inventoryImagesLayout.visibility = View.VISIBLE
            } else {
                binding.drugLayout.drugDetailsLayout.visibility = View.GONE

            }
            binding.complainDetails.text =
                items.ticketDetailsResponse?.data?.description?.trim()?.replace("\\s+".toRegex(), " ") ?: "--"
            if (items.status?.code.isNullOrEmpty()) {
            } else {

                if (items.status!!.code.equals("solved") && employeeDetailsResponse?.data!!.uid.equals(
                        items.created_id!!.uid)
                ) {
                    binding.ticketResolveBtn.visibility = View.VISIBLE
                    if (items.ticketDetailsResponse?.data?.category?.code.equals("mrp_cr") || items.ticketDetailsResponse?.data?.category?.code.equals(
                            "new_batch_req")
                    ) {
                        binding.ticketResolveBtn.visibility = View.GONE
                    }
                    binding.ticketCloseBtn.visibility = View.VISIBLE
                    binding.ticketCloseBtn.setOnClickListener {
                        imageClickListener.onClickTicketClose(items)
                    }
                    binding.ticketActionLayout.visibility = View.VISIBLE
                    binding.ticketResolveBtn.text = "Reopen"
                    binding.ticketResolveBtn.setOnClickListener {
                        imageClickListener.onClickTicketReopen(items)
                    }
                } else if ((items.status!!.code.equals("inprogress") || items.status!!.code.equals("reopened")) && employeeDetailsResponse?.data!!.uid.equals(
                        items.ticketDetailsResponse?.data?.user?.uid)
                ) {
                    if (items.ticketDetailsResponse != null && (items.ticketDetailsResponse?.data?.category?.code.equals(
                            "mrp_cr") || items.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req"))
                    ) {
                        binding.ticketResolveBtn.visibility = View.GONE
                        binding.ticketActionLayout.visibility = View.GONE
                    } else if (items.ticketDetailsResponse?.data?.ticket_inventory?.drug_request?.uid != null && items.ticketDetailsResponse?.data?.category?.code.equals(
                            "new_drug_req")
                    ) {
                        binding.ticketResolveBtn.visibility = View.GONE
                        binding.ticketActionLayout.visibility = View.GONE
                    } else {
                        binding.ticketResolveBtn.visibility = View.VISIBLE
                        binding.ticketCloseBtn.visibility = View.GONE
                        binding.ticketResolveBtn.text = "Resolve"
                        binding.ticketResolveBtn.setOnClickListener {
                            imageClickListener.onClickTicketResolve(items)
                        }
                        binding.ticketActionLayout.visibility = View.VISIBLE
                    }
                } else {
                    binding.ticketActionLayout.visibility = View.GONE
                }
            }
            if (items.site?.uid == null) {
                binding.siteidLable.text = "Ticket type: "
                binding.siteid.text = "Self"
                binding.regionLayout.visibility = View.VISIBLE
                binding.locationLayout.visibility = View.VISIBLE
                binding.region.text = items.ticketDetailsResponse?.data?.region?.name
                binding.location.text = items.ticketDetailsResponse?.data?.location?.name
            } else {
                binding.siteid.text = items.site?.site + "-" + items.site?.store_name
            }
            binding.ticketCategory.text = items.ticketDetailsResponse?.data?.category?.name
//            if(items.ticket_inventory?.drug_request?.uid != null){
//                binding.subCategoryLayout.visibility = View.GONE
//                binding.regionLayout.visibility = View.GONE
//            }else {
            binding.subCategory.text = items.ticketDetailsResponse?.data?.subcategory?.name
            binding.region.text = items.ticketDetailsResponse?.data?.reason?.name
            binding.subCategoryLayout.visibility = View.VISIBLE
            binding.regionLayout.visibility = View.VISIBLE
//            }
//            binding.siteName.text = items.site.store_name


            binding.pendingLayout.setOnClickListener {
                imageClickListener.onComplaintItemClick(position, orderData)

            }
            if (items.isExpanded) {

                binding.extraData.visibility = View.VISIBLE
                binding.arrow.visibility = View.GONE
                binding.arrowClose.visibility = View.VISIBLE

                /*   tickethistoryresponsenew=
                       orderData[position].uid?.let { imageClickListener.gettickethistory(it) }!!;*/

            } else {
                binding.extraData.visibility = View.GONE
                binding.arrow.visibility = View.VISIBLE
                binding.arrowClose.visibility = View.GONE
            }

            if (items.Tickethistory == null) {
                binding.remarksLayout.visibility = View.GONE
            } else {
                if (items.Tickethistory!!.data.listData.rows.isNullOrEmpty()) binding.remarksLayout.visibility =
                    View.GONE
                else binding.remarksLayout.visibility = View.VISIBLE
                binding.remarksRecycleView.adapter =
                    RemarkAdapter(items.Tickethistory!!.data.listData.rows)
            }


            var problemimages = ArrayList<ResponseNewTicketlist.Image>()
            if (orderData[position].ticketDetailsResponse?.data?.problem_images != null && orderData[position].ticketDetailsResponse?.data?.problem_images?.images != null) {
                problemimages = orderData[position].ticketDetailsResponse?.data?.problem_images!!.images
                binding.addedImagesRecyclerView.adapter =
                    ImageRecyclerView(problemimages, imageClickListener)
                binding.attacheImages.visibility = View.VISIBLE
            } else {
                binding.attacheImages.visibility = View.GONE
            }


            binding.cmpStatus.text =
                " " + items.status!!.name
            binding.cmpStatus.setTextColor(Color.parseColor(items.status!!.background_color))


            val complaintStatus = orderData[position].status!!.name
            if (complaintStatus?.toUpperCase().equals("Closed") || complaintStatus?.toUpperCase()
                    .equals("In Progress")
            ) {
                binding.closedDateLayout.visibility = View.VISIBLE
                binding.closedDate.text = orderData[position].ticketDetailsResponse?.data?.closed_date

            } else {
                binding.closedDateLayout.visibility = View.GONE
            }
        }

        fun notifyAdapter(userList: ArrayList<ResponseNewTicketlist.Row>) {
            this.orderData = userList
            notifyDataSetChanged()
        }


        class RemarkAdapter(val remarkList: ArrayList<ResponseNewTicketlist.NewTicketHistoryResponse.Row>) :
            RecyclerView.Adapter<RemarkAdapter.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int,
            ): ViewHolder {
                val view = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.view_order_status,
                    parent,
                    false
                ) as ViewOrderStatusBinding
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(remarkList[position])
                if (position == (remarkList.size - 1)) {
                    holder.remarkBinding.viewCreated.visibility = View.GONE
                    holder.remarkBinding.viewHoldProcess.visibility = View.GONE
                    holder.remarkBinding.viewProcess.visibility = View.GONE
                    holder.remarkBinding.viewClosed.visibility = View.GONE
                    holder.remarkBinding.viewLOne.visibility = View.GONE
                    holder.remarkBinding.viewLTwo.visibility = View.GONE
                    holder.remarkBinding.viewLThree.visibility = View.GONE
                    holder.remarkBinding.viewReopenRequest.visibility = View.GONE
                    holder.remarkBinding.viewAcknowledged.visibility = View.GONE
                    holder.remarkBinding.viewReopenedForProcess.visibility = View.GONE
                    holder.remarkBinding.viewReOpen.visibility = View.GONE
                    holder.remarkBinding.viewAccept.visibility = View.GONE
                }
            }

            override fun getItemCount(): Int {
                return remarkList.size
            }

            inner class ViewHolder(val remarkBinding: ViewOrderStatusBinding) :
                RecyclerView.ViewHolder(remarkBinding.root) {
                fun bind(remarks: ResponseNewTicketlist.NewTicketHistoryResponse.Row) {
                    // remarks.status?.name?: "In PROGRESS"
                    if (remarks.status?.toUpperCase().equals("NEW")) {
                        remarkBinding.createdLayout.visibility = View.VISIBLE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.GONE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.createdPerson.text = remarks.description
                        if (!remarks.workflow_comment.isNullOrEmpty()) {
                            remarkBinding.createdCommentTxt.text = remarks.workflow_comment
                        } else remarkBinding.createdCommentTxt.visibility = View.GONE
                        remarkBinding.createdDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }
                    } else if (remarks.status?.toUpperCase().equals("IN PROGRESS")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.VISIBLE
                        remarkBinding.closedLayout.visibility = View.GONE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.processPerson.text = remarks.description
                        if (!remarks.workflow_comment.isNullOrEmpty()) {
                            remarkBinding.processCommentTxt.text = remarks.workflow_comment
                        } else remarkBinding.processCommentTxt.visibility = View.GONE
                        remarkBinding.processDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }

                    } else if (remarks.status?.toUpperCase().equals("CLOSED")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        if (!remarks.workflow_comment.isNullOrEmpty()) {
                            remarkBinding.closedCommentTxt.text = remarks.workflow_comment
                        } else remarkBinding.closedCommentTxt.visibility = View.GONE
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }

                    } else if (remarks.status?.toUpperCase().equals("RESOLVED")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        if (!remarks.workflow_comment.isNullOrEmpty()) {
                            remarkBinding.closedCommentTxt.text = remarks.workflow_comment
                        } else remarkBinding.closedCommentTxt.visibility = View.GONE
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }

                    } else if (remarks.status?.toUpperCase().equals("REOPEN")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE
                        if (!remarks.workflow_comment.isNullOrEmpty()) {
                            remarkBinding.closedCommentTxt.text = remarks.workflow_comment
                        } else remarkBinding.closedCommentTxt.visibility = View.GONE
                        remarkBinding.closedPerson.text =
                            remarks.description //+" ("+remarks.created_id.role?.name +" "+ remarks.created_id.role?.name+")"
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }

                    } else if (remarks.status?.toUpperCase().equals("REJECTED")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        if (!remarks.workflow_comment.isNullOrEmpty()) {
                            remarkBinding.closedCommentTxt.text = remarks.workflow_comment
                        } else remarkBinding.closedCommentTxt.visibility = View.GONE
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }

                    } else if (remarks.status?.toUpperCase().equals("ON HOLD")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        if (!remarks.workflow_comment.isNullOrEmpty()) {
                            remarkBinding.closedCommentTxt.text = remarks.workflow_comment
                        } else remarkBinding.closedCommentTxt.visibility = View.GONE
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }
                    }

                }
            }
        }
    }

    //Image Recyclerview
    class ImageRecyclerView(
        var orderData: ArrayList<ResponseNewTicketlist.Image>,
        val imageClickListener: ImageClickListener,
    ) :
        SimpleRecyclerView<ViewImageShowBinding, ResponseNewTicketlist.Image>(
            orderData,
            R.layout.view_image_show
        ) {
        override fun bindItems(
            binding: ViewImageShowBinding,
            items: ResponseNewTicketlist.Image,
            position: Int,
        ) {
            Glide.with(context).load(items.url).placeholder(R.drawable.thumbnail_image)
                .into(binding.image)
            binding.image.setOnClickListener {
                items.url?.let { it1 -> imageClickListener.onItemClick(position, it1) }
            }
        }
    }

    override fun onItemClick(position: Int, imagePath: String) {
        PhotoPopupWindow(context, R.layout.layout_image_fullview, view, imagePath, null)
    }
    /* override  fun gettickethistory(uid:String):ArrayList<NewTicketHistoryResponse.Row>
    {
        var newtickethistory=ArrayList<NewTicketHistoryResponse.Row>()
        newtickethistory= viewModel.gettickethistory(1,100,uid)
         return newtickethistory;

    }*/

    fun calltickethistory(uid: String) {
        Utlis.showLoading(requireContext())
        val tickethistory = ArrayList<NewTicketHistoryResponse.Row>()
        viewModel.getNewticketHistory(
            RequestTicketHistory(
                1,
                50,
                uid
            )
        )
    }

    override fun onComplaintItemClick(
        position: Int,
        orderData: ArrayList<ResponseNewTicketlist.Row>,
    ) {

        // orderData[position].uid?.let { calltickethistory(it) }
        var curStatus: Boolean = orderData.get(position).isExpanded
        orderData.forEach {
            it.isExpanded = false
        }

        orderData.get(position).isExpanded = !curStatus
        // orderData[position].uid?.let { gettickethistory(it)}
        orderData[position].uid?.let { calltickethistory(it) }


        // adapter.notifyAdapter(orderData)


    }

    override fun onClickCCAccept(data: TicketData) {
        val dialog = Dialog(requireContext())
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
                showLoading()
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
                viewModel.actionCCAccept(ccAcceptRejectModel, 0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()


    }

    override fun onClickCCReject(data: TicketData) {
        val dialog = Dialog(requireContext())
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
                showLoading()
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
                viewModel.actionCCAccept(ccAcceptRejectModel, 0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onClickInventoryAccept(data: ResponseNewTicketlist.Row) {
        val dialog = Dialog(requireContext())
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
                showLoading()
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
                    Department(data.ticketDetailsResponse?.data?.department?.uid, data.ticketDetailsResponse?.data?.department?.code),
                    Level(data.ticketDetailsResponse?.data?.level?.uid, data.ticketDetailsResponse?.data?.level?.code),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(data.ticketDetailsResponse?.data?.user?.first_name, data.ticketDetailsResponse?.data?.user?.uid),
                    "Yes",
                    "manager"
                )
                viewModel.actionInventoryAcceptReject(inventoryAcceptrejectModel,
                    workFlowUpdateModel,
                    0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onClickInventoryReject(data: ResponseNewTicketlist.Row) {
        val dialog = Dialog(requireContext())
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
                showLoading()
                val inventoryAcceptrejectModel = InventoryAcceptrejectModel(
                    remark.text.toString(),
                    null,
                    ItemStatus("rejected"),
                    data.site!!.site!!,
                    data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID, "reject", data.ticketDetailsResponse!!.data.ticket_id,
                    null

                )
                var workFlowUpdateModel = WorkFlowUpdateModel(
                    Action("52E2C8F5C204B5BD03DF3A73EB096484", "reject"),
                    "Resolved",
                    Department(data.ticketDetailsResponse?.data?.department?.uid, data.ticketDetailsResponse?.data?.department?.code),
                    Level(data.ticketDetailsResponse?.data?.level?.uid, data.ticketDetailsResponse?.data?.level?.code),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(data.ticketDetailsResponse?.data?.user?.first_name, data.ticketDetailsResponse?.data?.user?.uid),
                    "Yes",
                    "manager"
                )
                viewModel.actionInventoryAcceptReject(inventoryAcceptrejectModel,
                    workFlowUpdateModel,
                    0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onClickForwardToManager(data: ResponseNewTicketlist.Row) {
        val dialog = Dialog(requireContext())
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
                showLoading()
                var forwardToManagerModel = ForwardToManagerModel(
                    remark.text.toString(),
                    Manager(""),//data.inventoryDetailsModel!!.data.ticket_inventory.ticket_inventory_item[0].manager.uid
                    FMTicket(data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    userData.EMPID
                )
                viewModel.actionForwardToManager(forwardToManagerModel, 0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private lateinit var selectedInventeryTicket: ResponseNewTicketlist.Row
    override fun onClickForwardChangeManager(data: ResponseNewTicketlist.Row) {
        selectedInventeryTicket = data
        viewModel.getManagersLiveData.observe(viewLifecycleOwner, Observer {
            SearchManagerDialog(it).apply { }.show(childFragmentManager, "")
        })
        viewModel.getManagers(data.site?.uid!!)
    }

    override fun onClickTicketResolve(data: ResponseNewTicketlist.Row) {
        val dialog = Dialog(requireContext())
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
                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "resolve",
                    remark.text.toString(),
                    userData.EMPID,
                    "solved",
                    data.ticket_id!!,
                    null
                )
                viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickTicketClose(data: ResponseNewTicketlist.Row) {
        val dialog = Dialog(requireContext())
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
                Toast.makeText(requireContext(),
                    context?.resources?.getString(R.string.label_rate_complaint),
                    Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "close",
                    remark.text.toString(),
                    userData.EMPID,
                    "closed",
                    data.ticket_id!!,
                    Feedback(Rating(ratingduid))
                )
                viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickTicketReopen(data: ResponseNewTicketlist.Row) {
        val dialog = Dialog(requireContext())
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
                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "reopen",
                    remark.text.toString(),
                    userData.EMPID,
                    "reopened",
                    data.ticket_id!!,
                    null
                )
                viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            viewBinding.fromDateText.setText(showingDate)
            val fromDate = viewBinding.fromDateText.text.toString()
            val toDate = viewBinding.toDateText.text.toString()
            if (getDateDifference(fromDate, toDate) == 0) {
                viewBinding.toDateText.setText(Utils.getCurrentDate())
            }
        } else {
            viewBinding.toDateText.setText(showingDate)
        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
    }

    override fun onClickFilterIcon() {
        val complaintListStatusFilterDialog = context?.let { Dialog(it) }
        val dialogComplaintListFilterBinding: DialogComplaintListFilterBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dialog_complaint_list_filter, null, false)
        complaintListStatusFilterDialog!!.setContentView(dialogComplaintListFilterBinding.root)
        complaintListStatusFilterDialog.window
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogComplaintListFilterBinding.closeDialog.setOnClickListener {
            complaintListStatusFilterDialog.dismiss()
        }
        dialogComplaintListFilterBinding.isNewChecked = this.complaintListStatus.contains("new")
        dialogComplaintListFilterBinding.isInProgressChecked =
            this.complaintListStatus.contains("inprogress")
        dialogComplaintListFilterBinding.isResolvedChecked =
            this.complaintListStatus.contains("solved")
        dialogComplaintListFilterBinding.isRejectedChecked =
            this.complaintListStatus.contains("rejected")
        dialogComplaintListFilterBinding.isReopenChecked =
            this.complaintListStatus.contains("reopened")
        dialogComplaintListFilterBinding.isClosedChecked =
            this.complaintListStatus.contains("closed")
        dialogComplaintListFilterBinding.isOnHoldChecked =
            this.complaintListStatus.contains("onHold")


        submitButtonEnable(dialogComplaintListFilterBinding)


        dialogComplaintListFilterBinding.newStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogComplaintListFilterBinding)
        }
        dialogComplaintListFilterBinding.inProgressStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogComplaintListFilterBinding)
        }
        dialogComplaintListFilterBinding.resolvedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogComplaintListFilterBinding)
        }
        dialogComplaintListFilterBinding.rejectedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogComplaintListFilterBinding)
        }
        dialogComplaintListFilterBinding.reopenStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogComplaintListFilterBinding)
        }
        dialogComplaintListFilterBinding.closedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogComplaintListFilterBinding)
        }
        dialogComplaintListFilterBinding.onholdStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogComplaintListFilterBinding)
        }

//        var complaintListStatusTemp = this.complaintListStatus
//        dialogComplaintListFilterBinding.status = complaintListStatusTemp

//        dialogComplaintListFilterBinding.statusRadioGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
//            if (i == R.id.new_status) {
//                complaintListStatusTemp = "new"
//            } else if (i == R.id.in_progress_status) {
//                complaintListStatusTemp = "inprogress"
//            } else if (i == R.id.resolved_status) {
//                complaintListStatusTemp = "solved"
//            } else if (i == R.id.reopen_status) {
//                complaintListStatusTemp = "reopened"
//            } else if (i == R.id.closed_status) {
//                complaintListStatusTemp = "closed"
//            }
//        }


        dialogComplaintListFilterBinding.submit.setOnClickListener {
//            this.complaintListStatus = complaintListStatusTemp
            this.complaintListStatus = ""
            if (dialogComplaintListFilterBinding.newStatus.isChecked) {
                this.complaintListStatus = "new"
            }
            if (dialogComplaintListFilterBinding.inProgressStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "inprogress"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},inprogress"
                }
            }
            if (dialogComplaintListFilterBinding.resolvedStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "solved"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},solved"
                }
            }
            if (dialogComplaintListFilterBinding.rejectedStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "rejected"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},rejected"
                }
            }
            if (dialogComplaintListFilterBinding.reopenStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "reopened"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},reopened"
                }
            }
            if (dialogComplaintListFilterBinding.closedStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "closed"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},closed"
                }
            }
            if (dialogComplaintListFilterBinding.onholdStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "onHold"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},onHold"
                }
            }


            if (complaintListStatusFilterDialog != null && complaintListStatusFilterDialog.isShowing) {
                complaintListStatusFilterDialog.dismiss()
                callAPI(1)


            }
            setFilterIndication()
        }
        complaintListStatusFilterDialog.show()
    }

    override fun onClickSiteIdIcon() {

    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onSelectedManager(data: Row) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = selectedInventeryTicket.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(selectedInventeryTicket.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = selectedInventeryTicket.ticketDetailsResponse?.data?.description
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
                showLoading()
                var request = ChangeManagerRequest(
                    remark.text.toString(),
                    ChangeManager(data.uid),
                    OldManager(selectedInventeryTicket.ticketDetailsResponse!!.data.site.uid),
                    ChangeSite(selectedInventeryTicket.ticketDetailsResponse!!.data.site.site,
                        selectedInventeryTicket.ticketDetailsResponse!!.data.site.uid),
                    selectedInventeryTicket.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    CCTicket(selectedInventeryTicket.ticketDetailsResponse!!.data.uid)
                )
                var actionRequest = ChangeManagerRequest(
                    remark.text.toString(),
                    ChangeManager(data.uid),
                    OldManager(selectedInventeryTicket.ticketDetailsResponse!!.data.site.manager.uid),
                    ChangeSite(selectedInventeryTicket.ticketDetailsResponse!!.data.site.site,
                        selectedInventeryTicket.ticketDetailsResponse!!.data.site.uid),
                    selectedInventeryTicket.ticketDetailsResponse!!.data.uid,
                    userData.EMPID,
                    CCTicket(selectedInventeryTicket.ticketDetailsResponse!!.data.uid)
                )
                viewModel.actionChangeForwardToManager(actionRequest, request, 0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }
}

fun checkResonDepot(
    reasonDept: List<ReasonDept>,
    employeeDetailsResponse: EmployeeDetailsResponse,
): Boolean {
    for (data in reasonDept) {
        if (data.department.uid.equals(employeeDetailsResponse.data?.department?.uid)) {
            return true
        }
    }
    return false
}


fun submitButtonEnable(dialogComplaintListFilterBinding: DialogComplaintListFilterBinding) {
    if (!dialogComplaintListFilterBinding.newStatus.isChecked
        && !dialogComplaintListFilterBinding.inProgressStatus.isChecked
        && !dialogComplaintListFilterBinding.resolvedStatus.isChecked
        && !dialogComplaintListFilterBinding.rejectedStatus.isChecked
        && !dialogComplaintListFilterBinding.reopenStatus.isChecked
        && !dialogComplaintListFilterBinding.closedStatus.isChecked
        && !dialogComplaintListFilterBinding.onholdStatus.isChecked
    ) {
        dialogComplaintListFilterBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
        dialogComplaintListFilterBinding.isSubmitEnable = false
    } else {
        dialogComplaintListFilterBinding.submit.setBackgroundResource(R.drawable.yellow_drawable)
        dialogComplaintListFilterBinding.isSubmitEnable = true
    }
}


interface ImageClickListener {
    fun onItemClick(position: Int, imagePath: String)

    fun onComplaintItemClick(position: Int, orderData: ArrayList<ResponseNewTicketlist.Row>)

    fun onClickCCAccept(data: TicketData)

    fun onClickCCReject(data: TicketData)

    fun onClickInventoryAccept(data: ResponseNewTicketlist.Row)

    fun onClickInventoryReject(data: ResponseNewTicketlist.Row)

    fun onClickForwardToManager(data: ResponseNewTicketlist.Row)

    fun onClickForwardChangeManager(data: ResponseNewTicketlist.Row)

    fun onClickTicketResolve(data: ResponseNewTicketlist.Row)

    fun onClickTicketClose(data: ResponseNewTicketlist.Row)

    fun onClickTicketReopen(data: ResponseNewTicketlist.Row)
    //  fun gettickethistory(uid:String):ArrayList<NewTicketHistoryResponse.Row>

    // fun calltickethistory(uid:String?);
    // abstract fun callticketHistory(uid: String?)
}