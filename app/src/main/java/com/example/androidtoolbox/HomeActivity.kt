package com.example.androidtoolbox


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sauvegarde.*

class HomeActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private val USER = "user_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences(USER, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prefs = applicationContext.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        logoutButton.setOnClickListener {
            prefs.edit().clear().apply()
            finish()
        }

        //logoutButton.setOnClickListener{
        //   goToMain()


        saveImg.setOnClickListener {
            goToSauvegarde()
        }
        permImg.setOnClickListener {
            goToPermissions()
        }
        cycleImg.setOnClickListener {
            goToCycle()
        }
    }

    /* private fun goToMain(){
        val editor :SharedPreferences.Editor = sharedPreferences.edit()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        editor.clear()
        editor.apply()

    }*/

    private fun goToSauvegarde(){
        val intent = Intent(this, SauvegardeActivity::class.java)
        startActivity(intent)
    }
    private fun goToPermissions(){
        val intent = Intent(this, PermissionsActivity::class.java)
        startActivity(intent)
    }
    private fun goToCycle(){
        val intent = Intent(this, CycleActivity::class.java)
        startActivity(intent)
    }

}
