package com.example.androidtoolbox


import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bluetooth.*
import java.util.*
import kotlin.collections.ArrayList

class BluetoothActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var mScanning: Boolean = false
    private lateinit var adapter: BluetoothActivityAdapter
    private val devices = ArrayList<ScanResult>()

    //var bluetoothGatt: BluetoothGatt? = null

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        bleTextFailed.visibility = View.GONE

        searchButton.setOnClickListener {
            when {
                isBLEEnabled -> {
                    //init scan
                    if (textView12.text == "Lancer le scan BLE") {
                        searchButton.setImageResource(android.R.drawable.ic_media_pause)
                        textView12.text = getString(R.string.scanning)
                        initBLEScan()
                        initScan()
                    } else if (textView12.text == "Scan en cours ...") {
                        searchButton.setImageResource(android.R.drawable.ic_media_play)
                        textView12.text = getString(R.string.start_scan)
                        progressBar.visibility = View.INVISIBLE
                        dividerBle.visibility = View.VISIBLE
                    }
                }
                bluetoothAdapter != null -> {
                    //ask for permission
                    val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
                }
                else -> {
                    //device is not compatible with your device
                    bleTextFailed.visibility = View.VISIBLE
                }
            }
        }
        deviceListRV.adapter = BluetoothActivityAdapter(devices, ::onDeviceClicked)
        deviceListRV.layoutManager = LinearLayoutManager(this)
    }

    private fun initScan() {
        progressBar.visibility = View.VISIBLE
        dividerBle.visibility = View.GONE

        handler = Handler()
        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                Log.w("BLE", "Scanning for devices")
                handler.postDelayed({
                    mScanning = false
                    stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                startScan(leScanCallback)
                adapter.clearResults()
                adapter.notifyDataSetChanged()
            } else {
                mScanning = false
                stopScan(leScanCallback)
            }
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLE", "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initBLEScan() {
        adapter = BluetoothActivityAdapter(
            arrayListOf(),
            ::onDeviceClicked
        )
        deviceListRV.adapter = adapter
        deviceListRV.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        handler = Handler()

        scanLeDevice(true)
        deviceListRV.setOnClickListener {
            scanLeDevice(!mScanning)
        }
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, BluetoothDetails::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (isBLEEnabled) {
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling has been canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        scanLeDevice(false)
    }

    companion object {
        private const val SCAN_PERIOD: Long = 60000
        private const val REQUEST_ENABLE_BT = 44
    }
}