package com.example.androidtoolbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cycle.*
import android.widget.Toast


class CycleActivity : AppCompatActivity() {

    private var texte :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycle)
        texte += "onCreate()\n"
        CycleTextView.text = texte
    }

    override fun onPause() {
        super.onPause()
        texte += "onPause()\n"
        CycleTextView.text = texte
    }

    override fun onResume() {
        super.onResume()
        texte += "onResume()\n"
        CycleTextView.text = texte
    }

    override fun onStop() {
        super.onStop()
        texte += "onStop()\n"
        CycleTextView.text = texte
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext,"onDestroy()", Toast.LENGTH_SHORT).show()
    }
}