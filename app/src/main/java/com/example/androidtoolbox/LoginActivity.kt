package com.example.androidtoolbox


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private val GOOD_IDENTIFIANT = "admin"
    private val GOOD_PASSWORD = "123"
    private val USER = "user_prefs"
    private val KEY_ID = "id"
    private val KEY_PASSWORD = "password"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(USER, Context.MODE_PRIVATE)
        val savedIdentifiant :String? = sharedPreferences.getString(KEY_ID, "")
        val savedPassword :String? = sharedPreferences.getString(KEY_PASSWORD, "")

        if (savedIdentifiant == GOOD_IDENTIFIANT && savedPassword == GOOD_PASSWORD){
            goToHome()
        }

        loginButton.setOnClickListener {

            var username = usernameText.text.toString()
            var password = passwordText.text.toString()
            doLogin(username, password)
        }

    }

    private fun doLogin(username: String, password: String) {

        if (username != GOOD_IDENTIFIANT && password != GOOD_PASSWORD) {
            Toast.makeText(applicationContext, "Mauvaise Identification !", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(applicationContext, " Identification réussi !", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            saveCredentials(username,password)
            goToHome()

        }
    }
    private fun saveCredentials(id: String, pass:String) {
        val editor :SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_ID, id)
        editor.putString(KEY_PASSWORD, pass)
        editor.apply()
    }

    private fun goToHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }




}
