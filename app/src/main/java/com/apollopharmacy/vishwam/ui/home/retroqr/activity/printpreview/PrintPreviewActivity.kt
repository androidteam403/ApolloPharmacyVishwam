package com.apollopharmacy.vishwam.ui.home.retroqr.activity.printpreview

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPrintPreviewBinding
import com.apollopharmacy.vishwam.databinding.DialogConnectPrinterBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter.QRCodePreviewAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails
import com.apollopharmacy.vishwam.util.bluetooth.BluetoothActivity
import com.apollopharmacy.vishwam.util.bluetooth.manager.BluetoothManager
import com.apollopharmacy.vishwam.util.bluetooth.manager.PrintfTSPLManager

class PrintPreviewActivity : AppCompatActivity(), PrintPreviewCallback {
    var activityPrintPreviewBinding: ActivityPrintPreviewBinding? = null
    private lateinit var viewModel: PrintPreviewViewModel
    var reviewImagesList = ArrayList<StoreWiseRackDetails.StoreDetail>()
    private val ACTIVITY_BARCODESCANNER_DETAILS_CODE = 151
    private val BLUETOOTH_SCAN_REQUEST_CODE = 1001;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPrintPreviewBinding = DataBindingUtil.setContentView(
            this@PrintPreviewActivity, R.layout.activity_print_preview
        )

        viewModel = ViewModelProvider(this)[PrintPreviewViewModel::class.java]
//        activityPrintPreviewBinding.callback = this@PrintPreviewActivity
        setUp()
    }

    fun setUp() {
        activityPrintPreviewBinding!!.callback = this@PrintPreviewActivity
        if (intent != null) {
            val bundle = intent.extras
            reviewImagesList =
                bundle!!.getSerializable("STORE_WISE_RACK_DETAILS") as ArrayList<StoreWiseRackDetails.StoreDetail>
            if (!reviewImagesList.isNullOrEmpty()) {
                activityPrintPreviewBinding!!.totalRacks.text = "${reviewImagesList.size}"
                val qrCodePreviewAdapter =
                    QRCodePreviewAdapter(this@PrintPreviewActivity, reviewImagesList)
                val mManager = GridLayoutManager(this@PrintPreviewActivity, 2)
                activityPrintPreviewBinding!!.qrcodePreviewRecycler.layoutManager = mManager
                activityPrintPreviewBinding!!.qrcodePreviewRecycler.adapter = qrCodePreviewAdapter
            }
            if (!BluetoothManager.getInstance(this).isConnect) {

            }
        }
    }

    var handlerBluetoothConnection = Handler()
    var runnableBluetoothConnection = Runnable {
        if (BluetoothManager.getInstance(this@PrintPreviewActivity).isConnect) {
            activityPrintPreviewBinding!!.bluetoothConnectionStatusIcon.setBackgroundResource(R.drawable.green_background)
            activityPrintPreviewBinding!!.bluetoothConnectionStatus.text = "Connected to printer"
        } else {
            activityPrintPreviewBinding!!.bluetoothConnectionStatusIcon.setBackgroundResource(R.drawable.red_background)
            activityPrintPreviewBinding!!.bluetoothConnectionStatus.text = "Disconnected to printer"
        }
        startPostDelay()
    }

    fun startPostDelay() {
        handlerBluetoothConnection.removeCallbacks(runnableBluetoothConnection)
        handlerBluetoothConnection.postDelayed(runnableBluetoothConnection, 500)
    }

    override fun onResume() {
        handlerBluetoothConnection.removeCallbacks(runnableBluetoothConnection)
        handlerBluetoothConnection.postDelayed(runnableBluetoothConnection, 500)
        super.onResume()
    }

    override fun onPause() {
        handlerBluetoothConnection.removeCallbacks(runnableBluetoothConnection)
        super.onPause()
    }

    override fun onClickPrint() {
        requestBluetoothScanPermission()
    }

    fun onConnectBluetooth() {
        if (!reviewImagesList.isNullOrEmpty()) {
            if (!BluetoothManager.getInstance(this@PrintPreviewActivity).isConnect()) {
                val dialogView =
                    Dialog(this@PrintPreviewActivity) // R.style.Theme_AppCompat_DayNight_NoActionBar
                val connectPrinterBinding: DialogConnectPrinterBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(this@PrintPreviewActivity),
                        R.layout.dialog_connect_printer,
                        null,
                        false
                    )
                dialogView.setContentView(connectPrinterBinding.getRoot())
                dialogView.setCancelable(false)
                dialogView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                connectPrinterBinding.dialogButtonOK.setOnClickListener { view ->
                    dialogView.dismiss()
                    var intent =
                        Intent(this@PrintPreviewActivity, BluetoothActivity::class.java)
                    startActivityForResult(
                        intent,
                        ACTIVITY_BARCODESCANNER_DETAILS_CODE
                    )
                }
                connectPrinterBinding.dialogButtonNO.setOnClickListener { view -> dialogView.dismiss() }
                //            connectPrinterBinding.dialogButtonNot.setOnClickListener(view -> dialogView.dismiss());
                dialogView.show()

                //Toast.makeText(getContext(), "Please connect Bluetooth first", Toast.LENGTH_SHORT).show();
                // startActivityForResult(BluetoothActivity.getStartIntent(getContext()), ACTIVITY_BARCODESCANNER_DETAILS_CODE);
                // overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                // return;
            } else {
                printQRcode()
            }
        }

    }

    fun requestBluetoothScanPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onConnectBluetooth()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(
                        Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT
                    ),
                    BLUETOOTH_SCAN_REQUEST_CODE
                )
            } else {
                onConnectBluetooth()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BLUETOOTH_SCAN_REQUEST_CODE) {
            if (grantResults != null && grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    requestBluetoothScanPermission()
                }
            }
        }
    }

    override fun onClickClose() {
        finish()
    }

    fun printQRcode() {
        for (i in reviewImagesList) {
            val instance: PrintfTSPLManager =
                PrintfTSPLManager.getInstance(this@PrintPreviewActivity)
            instance.clearCanvas()
            instance.initCanvas(90, 23)
            instance.setDirection(0)
            if (i.isRackSelected) {
                val bmp = BitmapFactory.decodeByteArray(i.byteArray, 0, i.byteArray.size);
                /*instance.printBitmap(350, 10, bmp)
               instance.beginPrintf(1)*/
                instance.printQrCode(20,10, "",100,1,"Naveenmp")
                instance.beginPrintf(1)
            }
        }
        finish()
    }

    fun isBluetoothConned() {
        if (BluetoothManager.getInstance(this).isConnect) {

        } else {

        }
    }
}