package com.example.androidtoolbox

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ble.*


class BleActivity: AppCompatActivity() {
    private lateinit var handler: Handler
    private var mScanning: Boolean = false

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE){
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val isBLEEnabled : Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble)

        when {
            isBLEEnabled -> initScan()
            bluetoothAdapter != null -> {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
            else -> {
                //BLE non compatible
                bleTextFailed.visibility = View.VISIBLE
            }
        }


    }

    override fun onStop() {
        super.onStop()
        if(isBLEEnabled){
            scanLeDevice(false)
        }
    }

    private fun initScan(){
      //  progressBar.visibility = View.VISIBLE
        //bleDivider.visibility = View.GONE
        handler = Handler()
        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.let {

            if (enable) {
                // Stops scanning after a pre-defined scan period.
                handler.postDelayed({
                    mScanning = false
                    it.stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                it.startScan(leScanCallback)
            }
            else {
                mScanning = false
                it.stopScan(leScanCallback)
            }
        }
    }


    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (result != null) {
                Log.w("MainActivity", "${result.device}")
            }
            runOnUiThread {
                //bleDivider.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 44
        private const val SCAN_PERIOD: Long = 10000

    }



}

