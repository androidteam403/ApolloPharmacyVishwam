package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.DialogComplaintListFilterBinding
import com.apollopharmacy.vishwam.databinding.DialogSurveyListFilterBinding
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyActivity
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter
import com.apollopharmacy.vishwam.ui.home.cms.complainList.submitButtonEnable
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),
    ApnaSurveyCallback, MainActivityCallback {
    var surveyResponseList = ArrayList<SurveyListResponse.Row>()
    lateinit var displayedItems: ArrayList<SurveyListResponse.Row>
    lateinit var layoutManager: LinearLayoutManager
    var isScrolling: Boolean = false
    private var isFirstTime: Boolean = true
    var surveyStatusList: String = "new,inprogress,rejected,approved"//cancelled
    lateinit var dialogSurveyListFilterBinding: DialogSurveyListFilterBinding
    var handler: Handler = Handler()
    var adapter: ApnaSurveyAdapter? = null
    val APNA_NEW_SURVEY_ACTIVITY_VALUE: Int? = 1000
    var isNewStatusClicked = true
    var inProgressStatus = true
    var rejectedStatus = true
    var approvedStatus = true

    override val layoutRes: Int
        get() = R.layout.fragment_apna_survey

    override fun retrieveViewModel(): ApnaSurveylViewModel {
        return ViewModelProvider(this).get(ApnaSurveylViewModel::class.java)
    }

    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this
        viewBinding.callback = this

        MainActivity.mInstance.filterIndicator.visibility = View.GONE
//        showLoading()
        callAPI(pageNo, rowSize, false)
        MainActivity.mInstance.plusIconApna.setOnClickListener {
            requireActivity().startActivityForResult(
                ApnaNewSurveyActivity().getStartIntent(
                    requireContext()
                ), APNA_NEW_SURVEY_ACTIVITY_VALUE!!
            )
        }

        viewBinding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClick()
        })

        viewBinding.search.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        viewBinding.search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                textView: TextView?,
                actionId: Int,
                event: KeyEvent?,
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView!!.text.trim().isEmpty()) {
                        pageNo = 1
                        isLoading = false
                        isFirstTime = true
                        showLoading()
                        Utlis.hideKeyPad(context as Activity)
                        callAPI(pageNo, rowSize, false)
                        return true
                    } else {
                        pageNo = 1
                        isLoading = false
                        isFirstTime = true
                        showLoading()
                        Utlis.hideKeyPad(context as Activity)
                        callAPI(pageNo, rowSize, true)
                        return true
                    }
                }
                return false
            }
        })
        viewBinding.searchIcon.setOnClickListener {
            val searchText = viewBinding.search.text.toString().trim()
            if (searchText.isEmpty()) {
                pageNo = 1
                isLoading = false
                isFirstTime = true
                showLoading()
                Utlis.hideKeyPad(context as Activity)
                callAPI(pageNo, rowSize, false)
            } else {
                pageNo = 1
                isLoading = false
                isFirstTime = true
                showLoading()
                Utlis.hideKeyPad(context as Activity)
                callAPI(pageNo, rowSize, true)
            }
        }
        viewBinding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                if (searchText.isEmpty()) {
                    pageNo = 1
                    isLoading = false
                    isFirstTime = true
                    showLoading()
                    Utlis.hideKeyPad(context as Activity)
                    viewBinding.search.clearFocus()
                    callAPI(pageNo, rowSize, false)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    fun submitClick() {
        if (!viewBinding.pullToRefresh.isRefreshing) Utlis.showLoading(requireContext())
        pageNo = 1
        viewBinding.search.setText("")
        surveyStatusList = "new,inprogress,rejected,approved"//cancelled
//        dialogSurveyListFilterBinding.isNewChecked = this.surveyStatusList.contains("new")
//        dialogSurveyListFilterBinding.isInProgressChecked =
//            this.surveyStatusList.contains("inprogress")
//
//        dialogSurveyListFilterBinding.isRejectedChecked =
//            this.surveyStatusList.contains("rejected")
//
//        dialogSurveyListFilterBinding.isApproveChecked =
//            this.surveyStatusList.contains("approved")
//
//        dialogSurveyListFilterBinding.isClosedChecked =
//            this.surveyStatusList.contains("cancelled")

        MainActivity.mInstance.filterIndicator.visibility = View.GONE
        callAPI(pageNo, rowSize, false)
    }

    override fun onClick(position: Int, surveyListResponse: SurveyListResponse.Row) {
        val i = Intent(activity, ApnaPreviewActivity::class.java)
        i.putExtra("regionList", surveyListResponse)
//        startActivityForResult(i, 210)
        startActivity(i)
    }

    private var isLoading = false
    private var pageNo = 1
    private var isLastPage = false
    var rowSize = 18
    override fun onSuccessgetSurveyDetails(surveyListResponse: SurveyListResponse) {
        hideLoading()
        if (viewBinding.pullToRefresh.isRefreshing) {
            viewBinding.pullToRefresh.isRefreshing = false
            pageNo = 1
            isLastPage = false
        }
        var getsurveyList = surveyListResponse.data!!.listData!!.rows
        if (getsurveyList != null && getsurveyList!!.size > 0) {
            viewBinding.pullToRefresh.visibility = View.VISIBLE
            viewBinding.recyclerViewApproved.visibility = View.VISIBLE
            viewBinding.noListFound.visibility = View.GONE
            if (pageNo == 1) {
                isLoading = false
                isLastPage = false
                surveyResponseList.clear()
                surveyResponseList.addAll(getsurveyList!!)
                layoutManager = LinearLayoutManager(context)
                viewBinding.recyclerViewApproved!!.removeAllViews()
                viewBinding.recyclerViewApproved.layoutManager = layoutManager
                initAdapter()
                pageNo++
                addScrollerListener()
            } else {
                if (isLoading) {
                    isLastPage = false
                    isLoading = false
                    val pos = surveyResponseList.size - 1
                    surveyResponseList.removeAt(surveyResponseList.size - 1)
                    surveyResponseList.addAll(getsurveyList!!)
                    pageNo++
//                    adapter!!.notifyDataSetChanged()
//                    viewBinding.recyclerViewApproved.smoothScrollToPosition(pos)
                    initAdapter()
                    viewBinding.recyclerViewApproved.smoothScrollToPosition(pos)
//                    addScrollerListener()
                }
            }


        } else {
            isLastPage = true
            if (isLoading) {
                isLoading = false
            }
            if (pageNo == 1) {
                viewBinding.pullToRefresh.visibility = View.GONE
                viewBinding.recyclerViewApproved.visibility = View.GONE
                viewBinding.noListFound.visibility = View.VISIBLE
            } else {
                val pos = surveyResponseList.size - 1
                surveyResponseList.removeAt(surveyResponseList.size - 1)
                initAdapter()
                viewBinding.recyclerViewApproved.smoothScrollToPosition(pos)

            }

        }

    }

    private fun initAdapter() {
        adapter = ApnaSurveyAdapter(
            requireContext(),
            surveyResponseList,
            this,
        )
        viewBinding.recyclerViewApproved.adapter = adapter
    }

    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        viewBinding.recyclerViewApproved.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isFirstTime && !isLastPage) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.

                    if (layoutManager.findLastCompletelyVisibleItemPosition() == surveyResponseList.size!! - 1) {//adapter!!.getData()?
                        if (surveyResponseList.size >= rowSize)
                            loadMore()
                    }

                }
            }
        })
    }

    private fun loadMore() {
        if (!isLoading) {
            isLoading = true
            val newdata = SurveyListResponse.Row()
            newdata.isLoading = "YES"
            surveyResponseList.add(newdata)
//            adapter!!.getData().add(newdata)
            adapter!!.notifyItemInserted(surveyResponseList.size - 1)
            callAPI(pageNo, rowSize, false)
        }
        /*handler.post(Runnable {
            if (!isLoading) {
                isLoading = true
                val newdata = SurveyListResponse.Row()
                newdata.isLoading = "YES"
                surveyResponseList.add(newdata)
//            adapter!!.getData().add(newdata)
                adapter!!.notifyItemInserted(surveyResponseList.size - 1)
                callAPI(pageNo, rowSize, false)
            }
        })*/

    }

    fun callAPI(startPageNo: Int, rowsize: Int, isSearch: Boolean) {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            isFirstTime = false
            if (!isLoading) showLoading()
            viewModel.getApnaSurveyList(
                this,
                startPageNo.toString(),
                rowsize.toString(),
                viewBinding.search.text.toString().trim(),
                surveyStatusList,
                isSearch
            )

        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onFailureGetSurveyDetails(message: String) {
        hideLoading()
        viewBinding.recyclerViewApproved.visibility = View.GONE
        viewBinding.noListFound.visibility = View.VISIBLE
    }

    override fun onFailuregetSurveyDetails(surveyListResponse: Any) {
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == APNA_NEW_SURVEY_ACTIVITY_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                showLoading()
                pageNo = 1
                isLoading = false
                isLastPage = false
                viewModel.getApnaSurveyList(
                    this@ApnaSurveyFragment,
                    pageNo.toString(),
                    rowSize.toString(),
                    viewBinding.search.text.toString(),
                    surveyStatusList,
                    false
                )
//                viewModel.getApnaSurveyList(
//                    this,
//                    pageNo.toString(),
//                    rowSize.toString(),
//                    surveyStatusList,
//                    viewBinding.search.text.toString().trim(),
//                    false
//                )
            }
        }
    }

    override fun onClickFilterIcon() {

        val surveyListStatusFilterDialog = context?.let { Dialog(it) }
        dialogSurveyListFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_survey_list_filter, null, false
        )
        viewBinding.search.setText("")
        surveyListStatusFilterDialog!!.setContentView(dialogSurveyListFilterBinding.root)
        surveyListStatusFilterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSurveyListFilterBinding.closeDialog.setOnClickListener {
            surveyListStatusFilterDialog.dismiss()
        }
        /*dialogSurveyListFilterBinding.isNewChecked = this.surveyStatusList.contains("new")
        dialogSurveyListFilterBinding.isInProgressChecked =
            this.surveyStatusList.contains("inprogress")

        dialogSurveyListFilterBinding.isRejectedChecked = this.surveyStatusList.contains("rejected")

        dialogSurveyListFilterBinding.isApproveChecked = this.surveyStatusList.contains("approved")

        dialogSurveyListFilterBinding.isClosedChecked = this.surveyStatusList.contains("cancelled")*/


        dialogSurveyListFilterBinding!!.clearAllFilters.setOnClickListener {
            dialogSurveyListFilterBinding!!.selectAll.isChecked = true
            if (dialogSurveyListFilterBinding!!.selectAll.isChecked) {
                isNewStatusClicked = true
                inProgressStatus = true
                rejectedStatus = true
                approvedStatus = true
                dialogSurveyListFilterBinding!!.newStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.newStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.inProgressStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.rejectedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.rejectedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.approvedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.approvedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.selectAll.isChecked = true
            }
            this.surveyStatusList = ""
            if (isNewStatusClicked) {
                this.surveyStatusList = "new"
            }
            if (inProgressStatus) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "inprogress"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},inprogress"
                }
            }
            if (rejectedStatus) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "solved"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},solved"
                }
            }
            if (approvedStatus) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "approved"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},approved"
                }
            }

            if (dialogSurveyListFilterBinding!!.selectAll.isChecked) {
                this.surveyStatusList = "new,inprogress,rejected,approved"
            }

            if (surveyListStatusFilterDialog != null && surveyListStatusFilterDialog.isShowing) {
                surveyListStatusFilterDialog.dismiss()
                callAPI(pageNo, rowSize, false)
            }
            setSurveyFilterIndication()
        }
        if (isNewStatusClicked &&
            inProgressStatus &&
            rejectedStatus &&
            approvedStatus
        ) {
            dialogSurveyListFilterBinding!!.selectAll.isChecked = true
        } else {
            dialogSurveyListFilterBinding!!.selectAll.isChecked = false
        }
        if (isNewStatusClicked) {
            dialogSurveyListFilterBinding!!.newStatus.background =
                requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
            dialogSurveyListFilterBinding!!.newStatus.setTextColor(requireContext().resources.getColor(
                R.color.white_for_both))
        } else {
            dialogSurveyListFilterBinding!!.newStatus.background =
                requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
            dialogSurveyListFilterBinding!!.newStatus.setTextColor(requireContext().resources.getColor(
                R.color.greyyy))
        }
        if (inProgressStatus) {
            dialogSurveyListFilterBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(
                R.color.white_for_both))
            dialogSurveyListFilterBinding!!.inProgressStatus.background =
                requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
        } else {
            dialogSurveyListFilterBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(
                R.color.greyyy))
            dialogSurveyListFilterBinding!!.inProgressStatus.background =
                requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
        }
        if (rejectedStatus) {
            dialogSurveyListFilterBinding!!.rejectedStatus.setTextColor(requireContext().resources.getColor(
                R.color.white_for_both))
            dialogSurveyListFilterBinding!!.rejectedStatus.background =
                requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
        } else {
            dialogSurveyListFilterBinding!!.rejectedStatus.setTextColor(requireContext().resources.getColor(
                R.color.greyyy))
            dialogSurveyListFilterBinding!!.rejectedStatus.background =
                requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
        }
        if (approvedStatus) {
            dialogSurveyListFilterBinding!!.approvedStatus.setTextColor(requireContext().resources.getColor(
                R.color.white_for_both))
            dialogSurveyListFilterBinding!!.approvedStatus.background =
                requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
        } else {
            dialogSurveyListFilterBinding!!.approvedStatus.setTextColor(requireContext().resources.getColor(
                R.color.greyyy))
            dialogSurveyListFilterBinding!!.approvedStatus.background =
                requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
        }
        dialogSurveyListFilterBinding!!.isSelectAllChecked =
            this.surveyStatusList.contains("new,inprogress,rejected,approved")


//        submitButtonEnabling(dialogSurveyListFilterBinding)
        submitButtonEnabling(isNewStatusClicked,
            inProgressStatus,
            rejectedStatus,
            approvedStatus,
            dialogSurveyListFilterBinding!!)

        dialogSurveyListFilterBinding!!.newStatus.setOnClickListener {
            if (isNewStatusClicked) {
                isNewStatusClicked = false
                dialogSurveyListFilterBinding!!.newStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.newStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))
            } else {
                isNewStatusClicked = true
                dialogSurveyListFilterBinding!!.newStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.newStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))

            }
            submitButtonEnabling(
                isNewStatusClicked,
                inProgressStatus,
                rejectedStatus,
                approvedStatus,
                dialogSurveyListFilterBinding
            )
        }
        dialogSurveyListFilterBinding!!.inProgressStatus.setOnClickListener {
            if (inProgressStatus) {
                inProgressStatus = false
                dialogSurveyListFilterBinding!!.inProgressStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))

            } else {
                inProgressStatus = true
                dialogSurveyListFilterBinding!!.inProgressStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))

            }
            submitButtonEnabling(
                isNewStatusClicked,
                inProgressStatus,
                rejectedStatus,
                approvedStatus,
                dialogSurveyListFilterBinding!!
            )
        }
        dialogSurveyListFilterBinding!!.rejectedStatus.setOnClickListener {
            if (rejectedStatus) {
                dialogSurveyListFilterBinding!!.rejectedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.rejectedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))
                rejectedStatus = false
            } else {
                rejectedStatus = true
                dialogSurveyListFilterBinding!!.rejectedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.rejectedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))

            }
            submitButtonEnabling(
                isNewStatusClicked,
                inProgressStatus,
                rejectedStatus,
                approvedStatus,
                dialogSurveyListFilterBinding!!
            )
        }
        dialogSurveyListFilterBinding!!.approvedStatus.setOnClickListener {
            if (approvedStatus) {
                dialogSurveyListFilterBinding!!.approvedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.approvedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))
                approvedStatus = false
            } else {
                dialogSurveyListFilterBinding!!.approvedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.approvedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                approvedStatus = true
            }
            submitButtonEnabling(
                isNewStatusClicked,
                inProgressStatus,
                rejectedStatus,
                approvedStatus,
                dialogSurveyListFilterBinding!!
            )
        }
        dialogSurveyListFilterBinding!!.selectAllCheckboxLayout.setOnClickListener {
            dialogSurveyListFilterBinding!!.selectAll.isChecked =
                !dialogSurveyListFilterBinding!!.selectAll.isChecked

            if (dialogSurveyListFilterBinding!!.selectAll.isChecked) {
                isNewStatusClicked = true
                inProgressStatus = true
                rejectedStatus = true
                approvedStatus = true

                dialogSurveyListFilterBinding!!.newStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.newStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.inProgressStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.rejectedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.rejectedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.approvedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogSurveyListFilterBinding!!.approvedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.white_for_both))
                dialogSurveyListFilterBinding!!.selectAll.isChecked = true
            } else {
                isNewStatusClicked = false
                inProgressStatus = false
                rejectedStatus = false
                approvedStatus = false
                dialogSurveyListFilterBinding!!.newStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.newStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))
                dialogSurveyListFilterBinding!!.inProgressStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))
                dialogSurveyListFilterBinding!!.rejectedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.rejectedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))
                dialogSurveyListFilterBinding!!.approvedStatus.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogSurveyListFilterBinding!!.approvedStatus.setTextColor(requireContext().resources.getColor(
                    R.color.greyyy))
                dialogSurveyListFilterBinding!!.selectAll.isChecked = true
            }
            submitButtonEnabling(
                isNewStatusClicked,
                inProgressStatus,
                rejectedStatus,
                approvedStatus,
                dialogSurveyListFilterBinding!!
            )
        }

        /*dialogSurveyListFilterBinding.newStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnabling(dialogSurveyListFilterBinding)
        }
        dialogSurveyListFilterBinding.inProgressStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnabling(dialogSurveyListFilterBinding)
        }

        dialogSurveyListFilterBinding.rejectedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnabling(dialogSurveyListFilterBinding)
        }
        dialogSurveyListFilterBinding.approvedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnabling(dialogSurveyListFilterBinding)
        }
        dialogSurveyListFilterBinding.closedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnabling(dialogSurveyListFilterBinding)
        }*/





        dialogSurveyListFilterBinding.submit.setOnClickListener {
            pageNo = 1

//            this.complaintListStatus = complaintListStatusTemp
            this.surveyStatusList = ""
            /*if (dialogSurveyListFilterBinding.newStatus.isChecked) {
                this.surveyStatusList = "new"
            }
            if (dialogSurveyListFilterBinding.inProgressStatus.isChecked) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "inprogress"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},inprogress"
                }
            }
            if (dialogSurveyListFilterBinding.rejectedStatus.isChecked) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "rejected"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},rejected"
                }
            }
            if (dialogSurveyListFilterBinding.approvedStatus.isChecked) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "approved"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},approved"
                }
            }
            if (dialogSurveyListFilterBinding.closedStatus.isChecked) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "cancelled"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},cancelled"
                }
            }*/

            if (isNewStatusClicked) {
                this.surveyStatusList = "new"
            }
            if (inProgressStatus) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "inprogress"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},inprogress"
                }
            }
            if (rejectedStatus) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "rejected"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},rejected"
                }
            }
            if (approvedStatus) {
                if (this.surveyStatusList.isEmpty()) {
                    this.surveyStatusList = "approved"
                } else {
                    this.surveyStatusList = "${this.surveyStatusList},approved"
                }
            }
            if (dialogSurveyListFilterBinding!!.selectAll.isChecked) {
                this.surveyStatusList = "new,inprogress,rejected,approved"
            }


            if (surveyListStatusFilterDialog != null && surveyListStatusFilterDialog.isShowing) {
                surveyListStatusFilterDialog.dismiss()

                showLoading()
                callAPI(pageNo, rowSize, false)


            }
            setSurveyFilterIndication()
        }
        surveyListStatusFilterDialog.show()
    }

    //    "new,inprogress,rejected,approved,cancelled"
    fun setSurveyFilterIndication() {
        if (!this.surveyStatusList.contains("new") || !this.surveyStatusList.contains("inprogress") || !this.surveyStatusList.contains(
                "rejected"
            ) || !this.surveyStatusList.contains("approved")
        ) {
            MainActivity.mInstance.filterIndicator.visibility = View.VISIBLE
        } else {
            MainActivity.mInstance.filterIndicator.visibility = View.GONE

        }
    }

    fun submitButtonEnabling(
        isNewStatusClicked: Boolean,
        inProgressStatus: Boolean,
        rejectedStatus: Boolean,
        approvedStatus: Boolean,
        dialogSurveyListFilterBinding: DialogSurveyListFilterBinding,
    ) {
        /*if (!dialogComplaintListFilterBinding.newStatus.isChecked && !dialogComplaintListFilterBinding.inProgressStatus.isChecked && !dialogComplaintListFilterBinding.approvedStatus.isChecked && !dialogComplaintListFilterBinding.rejectedStatus.isChecked && !dialogComplaintListFilterBinding.closedStatus.isChecked) {
            dialogComplaintListFilterBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogComplaintListFilterBinding.isSubmitEnable = false
        } else {
            dialogComplaintListFilterBinding.submit.setBackgroundResource(R.drawable.search_button_bg)
            dialogComplaintListFilterBinding.isSubmitEnable = true
        }*/

        if (!isNewStatusClicked && !inProgressStatus && !rejectedStatus && !approvedStatus) {
            dialogSurveyListFilterBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogSurveyListFilterBinding.isSubmitEnable = false
            dialogSurveyListFilterBinding.isSelectAllChecked = false
        } else if (isNewStatusClicked && inProgressStatus && rejectedStatus && approvedStatus) {
            dialogSurveyListFilterBinding.submit.setBackgroundResource(R.drawable.dark_blue_bg_for_btn)
            dialogSurveyListFilterBinding.isSubmitEnable = true
            dialogSurveyListFilterBinding.isSelectAllChecked = true
        } else {
            dialogSurveyListFilterBinding.submit.setBackgroundResource(R.drawable.dark_blue_bg_for_btn)
            dialogSurveyListFilterBinding.isSubmitEnable = true
            dialogSurveyListFilterBinding.isSelectAllChecked = false
        }
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

    }

    override fun onClickSubmenuItem(
        menuName: String?,
        submenus: java.util.ArrayList<MenuModel>?,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

}