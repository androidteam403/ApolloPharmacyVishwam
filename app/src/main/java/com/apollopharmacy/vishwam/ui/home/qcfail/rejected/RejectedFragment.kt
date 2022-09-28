package com.apollopharmacy.vishwam.ui.home.qcfail.rejected

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcList
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.PendingFragment
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity.TAG


class RejectedFragment : BaseFragment<QcRejectedViewModel, FragmentRejectedQcBinding>(),QcImagesListner,
    QcFilterFragment.QcFilterClicked {
    lateinit var adapter: RejectRecycleview

    val names = ArrayList<QcList>();

    override val layoutRes: Int
        get() = R.layout.fragment_rejected_qc

    override fun retrieveViewModel(): QcRejectedViewModel {
        return ViewModelProvider(this).get(QcRejectedViewModel::class.java)
    }

    override fun setup() {
        var name=QcList()
        name.setIschecked(false)
        names.add(name)

        adapter = RejectRecycleview(names,this)
        viewBinding.recyclerViewPending.adapter = adapter

        viewBinding.filter.setOnClickListener {
            viewModel.filterClicked()

        }
        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is Command.ShowButtonSheet -> {
                    var dialog = command.fragment.newInstance()
                    dialog.arguments = command.arguments
                    dialog.setTargetFragment(this, 0)
                    activity?.supportFragmentManager?.let { dialog.show(it, "") }
                }
                is Command.ShowToast -> {
                    hideLoading()
                    if (command.message.equals("no data found.please check empid")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.bulkAppRejLayout.visibility = View.GONE
                        viewBinding.recyclerViewPending.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    class RejectRecycleview(
        var orderData: ArrayList<QcList>,
        val imageClicklistner:QcImagesListner,


        ) :
        SimpleRecyclerView<QcrejectLayoutBinding, QcList>(
            orderData,
            R.layout.qcreject_layout
        ) {
        override fun bindItems(
            binding: QcrejectLayoutBinding,
            items: QcList,
            position: Int,
        ) {
            lateinit var orderadapter: RejectOrderDetailsRecycleView
            val names = ArrayList<String>();
            var ischecked: Boolean? = null
            names.add("a")

            orderadapter = RejectOrderDetailsRecycleView(names)
            binding.recyclerView.adapter = orderadapter

            if (!items.isIschecked){
                binding.checkBox.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
            }
            else{
                binding.checkBox.setImageResource(R.drawable.ic_circle_tick)
            }
            binding.checkBoxLayout.setOnClickListener {

                imageClicklistner.isChecked(orderData,position)

            }



            binding.arrow.setOnClickListener {
                binding.arrowClose.visibility = View.VISIBLE
                binding.arrow.visibility = View.GONE
                binding.extraData.visibility = View.VISIBLE


            }
            binding.arrowClose.setOnClickListener {
                binding.arrowClose.visibility = View.GONE
                binding.arrow.visibility = View.VISIBLE
                binding.extraData.visibility = View.GONE


            }
        }
    }

    class RejectOrderDetailsRecycleView(
        var orderData: ArrayList<String>,

        ) :
        SimpleRecyclerView<QcrejectorderdetailsBinding, String>(
            orderData,
            R.layout.qcrejectorderdetails
        ) {
        override fun bindItems(
            binding: QcrejectorderdetailsBinding,
            items: String,
            position: Int,
        ) {


        }
    }

    override fun clickedApply(
        selectedData: String,
        data: ArrayList<String>,
        tag: Int,
        toDate: String,
    ) {

    }

    override fun isChecked(array: ArrayList<QcList>, position: Int) {
        if (array[position].isIschecked){
            names[position].setIschecked(false)
        }
        else{
            names[position].setIschecked(true)
        }
        adapter.notifyDataSetChanged()
    }
    }




interface QcImagesListner {


    fun isChecked(array:ArrayList<QcList>, position: Int)
}

