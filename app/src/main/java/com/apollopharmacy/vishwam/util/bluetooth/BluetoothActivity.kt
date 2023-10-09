package com.apollopharmacy.vishwam.util.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.util.bluetooth.adaptor.BluetoothRecyclerViewAdapter
import com.apollopharmacy.vishwam.util.bluetooth.manager.BluetoothManager
import com.apollopharmacy.vishwam.util.bluetooth.model.BluetoothModel
import com.apollopharmacy.vishwam.util.utils.PermissionUtil

class BluetoothActivity : Activity() {

    var rvBluetoothShowList: RecyclerView? = null

    var context: Context? = null

    private val bluetoothModels: MutableList<BluetoothModel> = ArrayList<BluetoothModel>()

    private var recyclerShowAdapter: BluetoothRecyclerViewAdapter? = null

    fun getStartIntent(context: Context?): Intent? {
        return Intent(context, BluetoothActivity::class.java)
    }

    /**
     * 扫描到蓝牙的回调（添加蓝牙设备到列表）
     */
    /**
     * 扫描到蓝牙的回调（添加蓝牙设备到列表）
     */
    /**
     * Scan to Bluetooth callback (add Bluetooth device to the list)
     */
    var scanBlueCallBack: BluetoothManager.ScanBlueCallBack = object : BluetoothManager.ScanBlueCallBack {
        override fun scanDevice(bluetoothModel: BluetoothModel) {
            bluetoothModels.add(bluetoothModel)
            recyclerShowAdapter!!.notifyDataSetChanged()
        }
    }

    /**
     * 连接蓝牙结果的回调
     * Callback of Bluetooth connection result
     */
    var connectResultCallBack: BluetoothManager.ConnectResultCallBack = object : BluetoothManager.ConnectResultCallBack {
        override fun success(device: BluetoothDevice?) {
            Toast.makeText(context, "Bluetooth connection is successful", Toast.LENGTH_SHORT).show()
            finish()
        }

        override fun close(device: BluetoothDevice?) {
            Toast.makeText(context, "Bluetooth off", Toast.LENGTH_SHORT).show()
        }

        override fun fail(device: BluetoothDevice?) {
            Toast.makeText(context, "Bluetooth connection failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        context = this
        rvBluetoothShowList = findViewById<RecyclerView>(R.id.rv_bluetooth_show_list)
        recyclerShowAdapter = BluetoothRecyclerViewAdapter(this, bluetoothModels)
        rvBluetoothShowList!!.setAdapter(recyclerShowAdapter)
        rvBluetoothShowList!!.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        //检查是否开启了定位权限（安卓6.0以上，搜索蓝牙需要定位权限）
        //Check whether the location permission is enabled (Android 6.0 or higher, searching for Bluetooth requires location permission)
        if (PermissionUtil.checkLocationPermission(this)) {
            //添加扫描到蓝牙回调
            //Add scan to Bluetooth callback
            BluetoothManager.getInstance(this).addScanBlueCallBack(scanBlueCallBack)
            //添加连接蓝牙结果回调
            //Add callback for Bluetooth connection result
            BluetoothManager.getInstance(this).addConnectResultCallBack(connectResultCallBack)
            //开始搜索蓝牙
            //Start searching for Bluetooth
            val i: Int = BluetoothManager.getInstance(this).beginSearch()
            //蓝牙适配器未打开
            //Bluetooth adapter is not turned on
            if (i == 2) {
                BluetoothManager.getInstance(this)
                    .openBluetoothAdapter(this@BluetoothActivity, 101)
            }
        }
        //点击蓝牙列表中的item
        //Click on the item in the Bluetooth list
        recyclerShowAdapter!!.setOnClickItemLister(object : BluetoothRecyclerViewAdapter.OnClickItemLister {
            override fun onClick(view: View?, position: Int) {
                val bluetoothMac: String = bluetoothModels[position].getBluetoothMac()
                //配对连接蓝牙（打印机设备默认配对码：0000）
                //Pair and connect to Bluetooth (default pairing code for printer devices: 0000)
                BluetoothManager.getInstance(this@BluetoothActivity).pairBluetooth(bluetoothMac)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //定位权限申请结果回调
        //Callback of location permission application result
        if (requestCode == PermissionUtil.MY_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //添加扫描到蓝牙回调
                //Add scan to Bluetooth callback
                BluetoothManager.getInstance(this).addScanBlueCallBack(scanBlueCallBack)
                //添加连接蓝牙结果回调
                //Add a callback to the Bluetooth connection result
                BluetoothManager.getInstance(this).addConnectResultCallBack(connectResultCallBack)
                val i: Int = BluetoothManager.getInstance(this).beginSearch()
                if (i == 2) {
                    BluetoothManager.getInstance(this)
                        .openBluetoothAdapter(this@BluetoothActivity, 101)
                }
                //连接上一次的蓝牙
                //Connect to the last Bluetooth
                BluetoothManager.getInstance(this).connectLastBluetooth()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //打开蓝牙适配器结果回调
        //Open the Bluetooth adapter result callback
        if (requestCode == 101 && resultCode == RESULT_OK) {
            BluetoothManager.getInstance(this).beginSearch()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothManager.getInstance(this@BluetoothActivity)
            .removeScanBlueCallBack(scanBlueCallBack)
        BluetoothManager.getInstance(this@BluetoothActivity)
            .removeConnectResultCallBack(connectResultCallBack)
        BluetoothManager.getInstance(this@BluetoothActivity)
            .stopSearch()
    }
}