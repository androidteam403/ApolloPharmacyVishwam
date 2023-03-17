package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.ActivityAdminModuleBinding
import com.apollopharmacy.vishwam.databinding.DialogEditRangeChampsBinding

@Suppress("CAST_NEVER_SUCCEEDS")
class AdminModuleFragment : BaseFragment<AdminModuleViewModel, ActivityAdminModuleBinding>(),
    AdminModuleCallBack {
    var isRotated:Boolean=false
    private lateinit var dialogEditRangeChampsBinding: DialogEditRangeChampsBinding

    override val layoutRes: Int
        get() = R.layout.activity_admin_module

    override fun retrieveViewModel(): AdminModuleViewModel {
        return ViewModelProvider(this).get(AdminModuleViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        var prevValue = viewBinding.seekbar1.progress

            viewBinding.seekbar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                var updatedValue = viewBinding.seekbar1.progress
                var orangeValue = prevValue - updatedValue

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onClickExpand() {
        if(!isRotated){
            viewBinding.arrowImage.rotation= 450F
            viewBinding.cleanlinessExpandLayout.visibility= View.VISIBLE
            isRotated=true
        }else{
            viewBinding.arrowImage.rotation= 0F
            viewBinding.cleanlinessExpandLayout.visibility= View.GONE
            isRotated=false
        }

    }

    override fun onClickEdit() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if(dialogEditRangeChampsBinding.enterPoints.text.toString()> "5"){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }else if(dialogEditRangeChampsBinding.enterPoints.text.toString()<="0"){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val enterPoints = (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints*2
                viewBinding.seekbar1.progress =
                    (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditOverall() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.enterPoints.setText(viewBinding.cleanlinessOverallPoints.text)
        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) > 15) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }  else if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())<0){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }
            else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
             viewBinding.cleanlinessOverallPoints.text = dialogEditRangeChampsBinding.enterPoints.text.toString()
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }

        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditOverallHospitality() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
        dialogEditRangeChampsBinding.headingForDialog.text = "HOSPITALITY"
        dialogEditRangeChampsBinding.enterPoints.setText(viewBinding.overallHospitality.text.toString())
//        filtersList(dialogFilterChampsBinding)

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) > 15) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }  else if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())<0){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }
            else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                viewBinding.overallHospitality.text = dialogEditRangeChampsBinding.enterPoints.text.toString()
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }

        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub1() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Overall Appearance of the Store"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar1.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())> 5){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }else if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())<0){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val enterPoints = (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints*2
                viewBinding.seekbar1.progress =
                    (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub2() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Offer Display"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar2.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())> 2.5){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }else if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())<0){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val enterPoints = (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints*2
                viewBinding.seekbar2.progress =
                    (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub3() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Store Frontage"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar3.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())>2.5){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }else if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())<0){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val enterPoints = (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints*2
                viewBinding.seekbar3.progress =
                    (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub4() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Grooming of the staff"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar4.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())> 2.5){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }else if((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())<0){
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val enterPoints = (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints*2
                viewBinding.seekbar4.progress =
                    (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    private fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
}