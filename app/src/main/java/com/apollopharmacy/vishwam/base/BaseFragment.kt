package com.apollopharmacy.vishwam.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.util.Utils

abstract class BaseFragment<VM : ViewModel, DB : ViewDataBinding> : Fragment() {


    lateinit var viewModel: VM
    lateinit var viewBinding: DB
    abstract val layoutRes: Int
    abstract fun retrieveViewModel(): VM
    abstract fun setup()
    protected var mProgressDialog: ProgressDialog? = null
    protected var mLocationProgressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        viewModel = retrieveViewModel()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = null
        mProgressDialog = Utils.showLoadingDialog(this.context)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
    }
}

