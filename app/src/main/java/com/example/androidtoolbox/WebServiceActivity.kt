package com.example.androidtoolbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webservice.*

class WebServiceActivity : AppCompatActivity()  {
    val webservices = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webservice)
        webserviceRecycler.adapter = ContactAdapter(webservices.sorted())
    }


}