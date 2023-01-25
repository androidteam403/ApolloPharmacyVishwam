@file:Suppress("DEPRECATION")

package com.apollopharmacy.vishwam.ui.home.swacchlist


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.cms.CCData
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.databinding.RejectlistSwacchBinding
import com.apollopharmacy.vishwam.databinding.SwacchListBinding
import com.apollopharmacy.vishwam.databinding.SwacchimagelistRecycleviewBinding
import com.apollopharmacy.vishwam.databinding.SwacchlistRecycleviewBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.SwachhApollo.SwachhListViewModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ImageClickListener
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketData
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.*
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide

class SwacchFragment() : BaseFragment<SwachhListViewModel, SwacchListBinding>(),
    ImageClickListener, ComplaintListCalendarDialog.DateSelected, clickListener {

    private var swachhList = ArrayList<ApproveRejectListResponse.GetApproved>()
    private var swachhListResp = ArrayList<ApproveRejectListResponse>()
    var getImageList = ArrayList<LineImagesResponse.ImageUrl>()
    private val imageUrls: List<ApproveRequest.ImageUrl>? = null
    var pendingList = ArrayList<ApproveRejectListResponse.GetPending>()
    var approveList = ArrayList<ApproveRejectListResponse.GetApproved>()

    public var swachList = ArrayList<ApproveRejectListResponse.GetApproved>()
    var isFromDateSelected: Boolean = false
    var categoryId: Int? = null
    var categoryAutoId: String? = null
    lateinit var adapter: ListRecycleView
    lateinit var rejectAdapter: RejectListRecycleView
    lateinit var Imageadapter: ImageRecyclerView

    override val layoutRes: Int
        get() = R.layout.swacch_list

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)

    }

    val iti = ArrayList<String>()
    val url: String? = null


    override fun retrieveViewModel(): SwachhListViewModel {
        return ViewModelProvider(this).get(SwachhListViewModel::class.java)
    }

    override fun setup() {


        val siteId = Preferences.getSiteId()

        viewBinding.fromDateText.setText(Utils.getCurrentDate())
        viewBinding.toDateText.setText(Utils.getCurrentDate())

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            var fromDate = Utils.getticketlistfiltersdate(viewBinding.fromDateText.text.toString())
            var toDate = Utils.getticketlistfiltersdate(viewBinding.toDateText.text.toString())
            viewModel.getSwachhList(ApproveRejectListRequest("APL49396", fromDate, toDate, 0, 100))
        }

        viewModel.swacchList.observe(viewLifecycleOwner) {
            if (it.getApprovedList?.isEmpty() == true) {
                viewBinding.recyclerViewApproved.visibility = View.GONE
                viewBinding.emptyList.visibility = View.VISIBLE
            } else {
                viewBinding.recyclerViewApproved.visibility = View.VISIBLE
                viewBinding.emptyList.visibility = View.GONE
            }
            pendingList = it.getPendingList as ArrayList<ApproveRejectListResponse.GetPending>
            approveList = it.getApprovedList as ArrayList<ApproveRejectListResponse.GetApproved>
            adapter = ListRecycleView(approveList, this, this, getImageList)
            viewBinding.recyclerViewApproved.adapter = adapter
            rejectAdapter = RejectListRecycleView(pendingList, this, getImageList, this)
            viewBinding.recyclerViewReject.adapter = rejectAdapter

        }

        viewBinding.submit.setOnClickListener() {
            var fromDate = Utils.getticketlistfiltersdate(viewBinding.fromDateText.text.toString())
            var toDate = Utils.getticketlistfiltersdate(viewBinding.toDateText.text.toString())
            viewModel.getSwachhList(
                ApproveRejectListRequest(
                    "APL49396",
                    fromDate,
                    toDate,
                    0,
                    100
                )
            )

        }
        viewBinding.accept.setBackgroundColor(Color.parseColor("#dceaf4"))

        viewBinding.reject.setOnClickListener() {
            if (pendingList.isEmpty()) {
                viewBinding.recyclerViewReject.visibility = View.GONE
                viewBinding.emptyList.visibility = View.VISIBLE
            } else {
                viewBinding.recyclerViewReject.visibility = View.VISIBLE
                viewBinding.emptyList.visibility = View.GONE
            }


            viewBinding.reject.setBackgroundColor(Color.parseColor("#dceaf4"))
            viewBinding.accept.setBackgroundColor(Color.WHITE)
            viewBinding.recyclerViewApproved.visibility = View.GONE


        }
        viewBinding.accept.setOnClickListener() {

            if (approveList.isEmpty()) {
                viewBinding.recyclerViewApproved.visibility = View.GONE
                viewBinding.emptyList.visibility = View.VISIBLE
            } else {
                viewBinding.recyclerViewApproved.visibility = View.VISIBLE
                viewBinding.emptyList.visibility = View.GONE
            }


            viewBinding.recyclerViewApproved.visibility = View.VISIBLE
            viewBinding.reject.setBackgroundColor(Color.WHITE)
            viewBinding.accept.setBackgroundColor(Color.parseColor("#dceaf4"))

            viewBinding.recyclerViewReject.visibility = View.GONE

        }



        viewBinding.fromDateText.setOnClickListener {
            isFromDateSelected = true
            openDateDialog()
        }

        viewBinding.toDateText.setOnClickListener {
            isFromDateSelected = false
            openDateDialog()
        }

    }


    override fun onItemClick(position: Int, imagePath: String) {
        PhotoPopupWindow(context, R.layout.layout_image_fullview, view, imagePath, null)
    }

    override fun onComplaintItemClick(
        position: Int,
        orderData: ArrayList<ResponseNewTicketlist.Row>
    ) {

    }

    override fun onClickCCAccept(data: TicketData) {
        TODO("Not yet implemented")
    }

    override fun onClickCCReject(data: TicketData) {
        TODO("Not yet implemented")
    }

    override fun onClickInventoryAccept(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    override fun onClickInventoryReject(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    override fun onClickForwardToManager(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    override fun onClickForwardChangeManager(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    override fun onClickTicketResolve(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    override fun onClickTicketClose(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    override fun onClickTicketReopen(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    fun setSupportActionBar(toolbarswachh: Toolbar) {
        TODO("Not yet implemented")
    }


    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.fromDateText.text.toString(),
                    false,
                    viewBinding.fromDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.toDateText.text.toString(),
                    true,
                    viewBinding.fromDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }


    class ListRecycleView(
        var orderData: List<ApproveRejectListResponse.GetApproved>,
        val imageClickListener: ImageClickListener, val clickListener: clickListener,
        var imageList: List<LineImagesResponse.ImageUrl>,
    ) :

        SimpleRecyclerView<SwacchlistRecycleviewBinding, ApproveRejectListResponse.GetApproved>(
            orderData,
            R.layout.swacchlist_recycleview
        ) {
        override fun bindItems(
            binding: SwacchlistRecycleviewBinding,
            items: ApproveRejectListResponse.GetApproved,
            position: Int
        ) {

            binding.storeId.setText(items.storeId)
            binding.categoryautoid.setText(items.status)
            binding.categoryaut.setText(items.categoryAuotId)
            binding.categoryname.setText(items.categoryName)


            binding.arrowClose.setOnClickListener() {
                clickListener.onClickApproveImage(
                    position,
                    orderData as ArrayList<ApproveRejectListResponse.GetApproved>
                )

                notifyDataSetChanged()
            }
            if (items.isExpanded == true) {
                binding.approveimagesRecycleview.visibility = View.VISIBLE
                binding.arrow.visibility = View.VISIBLE
                binding.arrowClose.visibility = View.GONE
            } else if (items.isExpanded==false) {
                binding.approveimagesRecycleview.visibility = View.GONE
                binding.arrow.visibility = View.GONE
                binding.arrowClose.visibility = View.VISIBLE
            }
            binding.arrow.setOnClickListener() {
                orderData[position].isExpanded = false
                notifyDataSetChanged()
            }


            lateinit var Imageadapter: ImageRecyclerView
            if (orderData[position].imageUrls == null) {

            } else {
                Imageadapter =
                    orderData[position].imageUrls?.let {
                        ImageRecyclerView(
                            it,
                            imageClickListener
                        )
                    }!!

                binding.approveimagesRecycleview.adapter = Imageadapter
            }
        }

    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            viewBinding.fromDateText.setText(showingDate)
            val fromDate = viewBinding.fromDateText.text.toString()
            val toDate = viewBinding.toDateText.text.toString()
            if (Utils.getDateDifference(fromDate, toDate) == 0) {
                viewBinding.toDateText.setText(Utils.getCurrentDate())
            }
        } else {
            viewBinding.toDateText.setText(showingDate)
        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
    }

    override fun onClickApproveImage(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetApproved>
    ) {
        var curStatus: Boolean = orderData.get(position).isExpanded
        orderData.forEach {
            it.isExpanded = false
        }

        orderData.get(position).isExpanded = !curStatus

        Utlis.showLoading(requireContext())
        viewModel.getImagesList(
            LineImagesRequest(
                orderData[position].categoryId,
                "16001",
                orderData[position].categoryAuotId
            )
        )

        viewModel.swacchImagesList.observe(viewLifecycleOwner) {
            approveList[position].imageUrls = it.imageUrls
            adapter.notifyDataSetChanged()
            Utlis.hideLoading()



        }

    }


    override fun onClickPendingImage(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetPending>
    ) {
        Utlis.showLoading(requireContext())


        var curStatus: Boolean = orderData.get(position).isExpanded
        orderData.forEach {
            it.isExpanded = false
        }

        orderData.get(position).isExpanded = !curStatus


        viewModel.getImagesList(
            LineImagesRequest(
                orderData[position].categoryId,
                "16001",
                orderData[position].categoryAuotId
            )
        )
        viewModel.swacchImagesList.observe(viewLifecycleOwner) {
            pendingList[position].imageUrls = it.imageUrls
            rejectAdapter.notifyDataSetChanged()
            Utlis.hideLoading()


        }
    }


    override fun onClickPendingrejectButton(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetPending>
    ) {
        var submitList = ArrayList<ApproveRequest>()
        var reject = ApproveRequest()
        reject.actionEvent = "REJECT"
        reject.storeId = Preferences.getSiteId();
        reject.useridId = Preferences.getToken()
        reject.categoryId = orderData[position].categoryId.toString()
        reject.imageUrls = getImageList
        submitList.add(reject)
        viewModel.getApproveList(submitList)
        var rejects: String? = null

        viewModel.swachhAproveList.observe(viewLifecycleOwner) {
            rejects = it.message
        }
        Toast.makeText(
            requireContext(),
            ("CategoryId " + orderData[position].categoryId.toString() + " Rejected " + rejects),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onClickPendingAcceptButton(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetPending>
    ) {
        var submitList = ArrayList<ApproveRequest>()
        var reject = ApproveRequest()
        reject.actionEvent = "APPROVE"
        reject.storeId = Preferences.getSiteId();
        reject.useridId = Preferences.getToken()
        reject.categoryId = orderData[position].categoryId.toString()
        reject.imageUrls = getImageList
        submitList.add(reject)
        viewModel.getApproveList(submitList)
        var rejects: String? = null

        viewModel.swachhAproveList.observe(viewLifecycleOwner) {
            rejects = it.message
        }
        Toast.makeText(
            requireContext(),
            ("CategoryId " + orderData[position].categoryId.toString() + " Approved " + rejects),
            Toast.LENGTH_SHORT
        ).show()

    }


}


class RejectListRecycleView(
    var orderData: List<ApproveRejectListResponse.GetPending>,
    val imageClickListener: ImageClickListener,
    var imageList: List<LineImagesResponse.ImageUrl>,
    val clickListener: clickListener,

    ) :

    SimpleRecyclerView<RejectlistSwacchBinding, ApproveRejectListResponse.GetPending>(
        orderData, R.layout.rejectlist_swacch
    ) {
    override fun bindItems(
        binding: RejectlistSwacchBinding,
        items: ApproveRejectListResponse.GetPending,
        position: Int
    ) {


//            binding.storeId.setText(items.storeId?.get(position))

        binding.accept.setOnClickListener {
            clickListener.onClickPendingAcceptButton(
                position,
                orderData as ArrayList<ApproveRejectListResponse.GetPending>
            )
        }
        binding.reject.setOnClickListener {
            clickListener.onClickPendingrejectButton(
                position,
                orderData as ArrayList<ApproveRejectListResponse.GetPending>
            )
        }


        binding.arrowClose.setOnClickListener() {

            clickListener.onClickPendingImage(
                position,
                orderData as ArrayList<ApproveRejectListResponse.GetPending>
            )

            notifyDataSetChanged()
        }

        binding.arrow.setOnClickListener() {
            orderData[position].isExpanded = false
            notifyDataSetChanged()
        }

        if (items.isExpanded == true) {
            binding.addedImagesRecyclerView.visibility = View.VISIBLE
            binding.arrow.visibility = View.VISIBLE
            binding.arrowClose.visibility = View.GONE
        } else {
            binding.addedImagesRecyclerView.visibility = View.GONE
            binding.arrow.visibility = View.GONE
            binding.arrowClose.visibility = View.VISIBLE
        }


        binding.storeId.setText(items.storeId)
        binding.categoryautoid.setText(items.categoryAuotId)
        binding.categoryname.setText(items.categoryName)

        if (orderData[position].imageUrls == null) {


        } else {
            lateinit var Imageadapter: ImageRecyclerView
            Imageadapter =
                orderData[position].imageUrls?.let { ImageRecyclerView(it, imageClickListener) }!!
            binding.addedImagesRecyclerView.adapter = Imageadapter
        }

//            Imageadapter = ImageRecyclerView(it, imageClickListener)
    }
//        lateinit var Imageadapter: ImageRecycleView


}


class ImageRecyclerView(
    var orderData: List<LineImagesResponse.ImageUrl>,
    val imageClickListener: ImageClickListener,

    ) :
    SimpleRecyclerView<SwacchimagelistRecycleviewBinding, LineImagesResponse.ImageUrl>(
        orderData,
        R.layout.swacchimagelist_recycleview
    ) {


    override fun bindItems(
        binding: SwacchimagelistRecycleviewBinding,
        items: LineImagesResponse.ImageUrl,
        position: Int,
    ) {

//        if (orderData.isEmpty()) {
//            binding.emptyList.visibility = View.VISIBLE
//            binding.image.visibility = View.GONE
//        } else {
//            binding.emptyList.visibility = View.GONE
//            binding.image.visibility = View.VISIBLE
//        }

        Glide.with(ViswamApp.context).load(items.url).placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.image.setOnClickListener {
            items.url?.let { it1 -> imageClickListener.onItemClick(position, it1) }
        }

    }
}


interface clickListener {


    fun onClickApproveImage(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetApproved>
    )

    fun onClickPendingImage(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetPending>
    )


    fun onClickPendingrejectButton(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetPending>
    )

    fun onClickPendingAcceptButton(
        position: Int,
        orderData: ArrayList<ApproveRejectListResponse.GetPending>
    )


}
