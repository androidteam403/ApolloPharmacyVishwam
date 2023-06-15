package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.InputFilter
import android.util.Log
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
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.cms.RequestComplainList
import com.apollopharmacy.vishwam.databinding.DialogComplaintListFilterBinding
import com.apollopharmacy.vishwam.databinding.DialogSurveyListFilterBinding
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyActivity
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter
import com.apollopharmacy.vishwam.ui.home.cms.complainList.submitButtonEnable
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),
    ApnaSurveyCallback, MainActivityCallback {
    var surveyResponseList = ArrayList<SurveyListResponse.Row>()
    lateinit var displayedItems: ArrayList<SurveyListResponse.Row>
    lateinit var layoutManager: LinearLayoutManager
    var isScrolling: Boolean = false
    private var isFirstTime: Boolean = true
    var surveyStatusList: String = "new,inprogress,rejected,approved,cancelled"

    var handler: Handler = Handler()
    var adapter: ApnaSurveyAdapter? = null
    val APNA_NEW_SURVEY_ACTIVITY_VALUE: Int? = 1000
    override val layoutRes: Int
        get() = R.layout.fragment_apna_survey

    override fun retrieveViewModel(): ApnaSurveylViewModel {
        return ViewModelProvider(this).get(ApnaSurveylViewModel::class.java)
    }

    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this
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
        viewBinding.search.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        viewBinding.search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callAPI(1, rowSize, true)
                    return true
                }
                return false
            }
        })

    }


    override fun onClick(position: Int, surveyListResponse: SurveyListResponse.Row) {
        val i = Intent(activity, ApnaPreviewActivity::class.java)
        i.putExtra("regionList", surveyListResponse)
//        startActivityForResult(i, 210)
        startActivity(i)
    }

    private var isLoading = false
    private var pageNo = 1

    var rowSize = 10
    override fun onSuccessgetSurveyDetails(surveyListResponse: SurveyListResponse) {
        hideLoading()
        var getsurveyList = surveyListResponse.data!!.listData!!.rows
        if (getsurveyList != null && getsurveyList!!.size > 0) {
            viewBinding.recyclerViewApproved.visibility = View.VISIBLE
            viewBinding.noListFound.visibility = View.GONE
            if (pageNo == 1 ) {
                surveyResponseList.clear()
                surveyResponseList.addAll(getsurveyList!!)
                layoutManager = LinearLayoutManager(context)
                viewBinding.recyclerViewApproved.layoutManager =
                    layoutManager
                initAdapter()
                pageNo++
                addScrollerListener()
            } else {
                if (isLoading) {
                    isLoading = false
                    surveyResponseList.removeAt(surveyResponseList.size - 1)
                    surveyResponseList.addAll(getsurveyList!!)
                    pageNo++

                    initAdapter()
                    addScrollerListener()
                }
            }


        } else {
            viewBinding.recyclerViewApproved.visibility = View.GONE
            viewBinding.noListFound.visibility = View.VISIBLE
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
                if (!isLoading && !isFirstTime) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter!!.getData()?.size!! - 1 ) {
                        loadMore()
                    }
                }
            }
        })
    }

    private fun loadMore() {
        handler.post(Runnable {
            isLoading = true
            val newdata = SurveyListResponse.Row()
            newdata.isLoading = "YES"
            adapter!!.getData().add(newdata)
            adapter!!.notifyItemInserted(adapter!!.getData().size - 1)
            callAPI(pageNo, rowSize, false)

        })

    }

    fun callAPI(startPageNo: Int, rowsize: Int, isSearch: Boolean) {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            isFirstTime = false
            if (!isLoading)
                showLoading()
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
            )
                .show()
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
                viewModel.getApnaSurveyList(
                    this,
                    "1",
                    rowSize.toString(),
                    surveyStatusList,
                    viewBinding.search.text.toString().trim(),
                    false
                )
            }
        }
    }

    override fun onClickFilterIcon() {
        val surveyListStatusFilterDialog = context?.let { Dialog(it) }
        val dialogSurveyListFilterBinding: DialogSurveyListFilterBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_survey_list_filter,
                null,
                false
            )

        viewBinding.search.setText("")
        surveyListStatusFilterDialog!!.setContentView(dialogSurveyListFilterBinding.root)
        surveyListStatusFilterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSurveyListFilterBinding.closeDialog.setOnClickListener {
            surveyListStatusFilterDialog.dismiss()
        }
        dialogSurveyListFilterBinding.isNewChecked = this.surveyStatusList.contains("new")
        dialogSurveyListFilterBinding.isInProgressChecked =
            this.surveyStatusList.contains("inprogress")

        dialogSurveyListFilterBinding.isRejectedChecked =
            this.surveyStatusList.contains("rejected")

        dialogSurveyListFilterBinding.isApproveChecked =
            this.surveyStatusList.contains("approved")

        dialogSurveyListFilterBinding.isClosedChecked =
            this.surveyStatusList.contains("cancelled")



        submitButtonEnabling(dialogSurveyListFilterBinding)


        dialogSurveyListFilterBinding.newStatus.setOnCheckedChangeListener { compoundButton, b ->
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
        }





        dialogSurveyListFilterBinding.submit.setOnClickListener {
//            this.complaintListStatus = complaintListStatusTemp
            this.surveyStatusList = ""
            if (dialogSurveyListFilterBinding.newStatus.isChecked) {
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
            }



            if (surveyListStatusFilterDialog != null && surveyListStatusFilterDialog.isShowing) {
                surveyListStatusFilterDialog.dismiss()
                callAPI(1, rowSize, false)


            }
            setSurveyFilterIndication()
        }
        surveyListStatusFilterDialog.show()
    }

    //    "new,inprogress,rejected,approved,cancelled"
    fun setSurveyFilterIndication() {
        if (!this.surveyStatusList.contains("new") || !this.surveyStatusList.contains("inprogress") || !this.surveyStatusList.contains(
                "rejected"
            ) || !this.surveyStatusList.contains("approved") || !this.surveyStatusList.contains(
                "cancelled"
            )
        ) {
            MainActivity.mInstance.filterIndicator.visibility = View.VISIBLE
        } else {
            MainActivity.mInstance.filterIndicator.visibility = View.GONE

        }
    }

    fun submitButtonEnabling(dialogComplaintListFilterBinding: DialogSurveyListFilterBinding) {
        if (!dialogComplaintListFilterBinding.newStatus.isChecked && !dialogComplaintListFilterBinding.inProgressStatus.isChecked && !dialogComplaintListFilterBinding.approvedStatus.isChecked && !dialogComplaintListFilterBinding.rejectedStatus.isChecked && !dialogComplaintListFilterBinding.closedStatus.isChecked) {
            dialogComplaintListFilterBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogComplaintListFilterBinding.isSubmitEnable = false
        } else {
            dialogComplaintListFilterBinding.submit.setBackgroundResource(R.drawable.apna_project_actionbar_bg)
            dialogComplaintListFilterBinding.isSubmitEnable = true
        }
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }
}