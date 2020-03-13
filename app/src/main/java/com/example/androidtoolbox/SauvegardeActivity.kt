package com.example.androidtoolbox

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sauvegarde.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class  SauvegardeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sauvegarde)

        envoyer.setOnClickListener {
            saveDataToFile(
                lastName.text.toString(),
                firstName.text.toString(),
                date.text.toString()
            )
        }
        show.setOnClickListener {
            readDataFromFile()
        }
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat( "dd/MM:yyyy", Locale.FRANCE)
                date.text = sdf.format(cal.time)

            }

       /* dateTitle.setOnClickListener {
            showDatePicker(dateSetListener)
        }*/
        date.setOnClickListener {
            showDatePicker(dateSetListener)
        }
    }

    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener){
        val cal = Calendar.getInstance()
        return DatePickerDialog(
            this@SauvegardeActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
            ).show()
    }

    private fun readDataFromFile() {
        val data: String = File(cacheDir.absolutePath, "user_data.json").readText()
        val jsonObject = JSONObject(data)
        val lastName = jsonObject.optString(KEY_LAST_NAME)
        val firstName = jsonObject.optString(KEY_FIRST_NAME)
        val date = jsonObject.optString(KEY_DATE)

        AlertDialog.Builder( this@SauvegardeActivity)
            .setTitle("Lecture du fichier")
            .setMessage("Nom : $lastName\n Prenom : $firstName \n Date: $date")
            .create()
            .show()

    }


    private fun saveDataToFile(lastName: String, firstName: String, date: String) {
        val jsonObject = JSONObject()
        jsonObject.put(KEY_LAST_NAME, lastName)
        jsonObject.put(KEY_FIRST_NAME, firstName)
        jsonObject.put(KEY_DATE, date)
        val data = jsonObject.toString()
        File(cacheDir.absolutePath, "user_data.json").writeText(data)
    }

    companion object {
        private const val KEY_LAST_NAME = "KEY_LAST_NAME"
        private const val KEY_FIRST_NAME = "KEY_FIRST_NAME"
        private const val KEY_DATE = "KEY_DATE_NAME"
    }


}
