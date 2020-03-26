package com.example.androidtoolbox

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import kotlinx.android.synthetic.main.activity_ble_cell.view.*
import kotlinx.android.synthetic.main.activity_permissions_cell.view.*
import org.w3c.dom.Text

class BleScanAdapter (
    private val list : List<ScanResult>,
    val deviceClickListener: (BluetoothDevice) -> Unit
) :

    RecyclerView.Adapter<BleScanAdapter.BLEScanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLEScanViewHolder =
        BLEScanViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_ble_cell, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BLEScanViewHolder, position: Int) {
        holder.deviceName.text = list[position].device.name
        holder.deviceAdress.text = list[position].device.address
        holder.deviceRssi.text = list[position].rssi.toString()
       /* holder.layout.setOnClickListener{
            deviceClickListener.invoke(list[position].device)
        }*/
    }

    class BLEScanViewHolder(bluetoothView: View) : RecyclerView.ViewHolder(bluetoothView){
        val deviceName: TextView = bluetoothView.nameBT
        val deviceAdress: TextView = bluetoothView.addressBT
        val deviceRssi: TextView = bluetoothView.rssiBT

    }


}