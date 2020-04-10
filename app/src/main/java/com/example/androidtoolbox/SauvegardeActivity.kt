@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.androidtoolbox


import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sauvegarde.*
import org.json.JSONObject
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*


class  SauvegardeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sauvegarde)

        envoyer.setOnClickListener {
            saveDataToFile(
                lastName.text.toString(),
                firstName.text.toString(),
                dateOfBirth.text.toString()
            )
        }


        show.setOnClickListener {
            readDataFromFile()
        }

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                cal.set(YEAR, year)
                cal.set(MONTH, monthOfYear)
                cal.set(DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat( "dd/MM/yyyy", Locale.FRANCE)
                dateOfBirth.text = sdf.format(cal.time)

            }

        dateOfBirth.setOnClickListener {
            this.showDatePicker(dateSetListener)
        }

    }

    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener){
        val cal = Calendar.getInstance()
        return DatePickerDialog(
            this@SauvegardeActivity, dateSetListener,
            cal.get(YEAR),
            cal.get(MONTH),
            cal.get(DAY_OF_MONTH)
            ).show()
    }

    private fun readDataFromFile() {
        val data: String = File(cacheDir.absolutePath, "user_data.json").readText()
        val jsonObject = JSONObject(data)
        val lastName = jsonObject.optString(KEY_LAST_NAME)
        val firstName = jsonObject.optString(KEY_FIRST_NAME)
        val date = jsonObject.optString(KEY_DATE)
        val age = jsonObject.optString(KEY_AGE)

        AlertDialog.Builder( this@SauvegardeActivity)
            .setTitle("Lecture du fichier")
            .setMessage("Nom : $lastName\n Prenom : $firstName \n Naissance: $date Age: $age \n")
            .create()
            .show()

    }


    private fun saveDataToFile(lastName: String, firstName: String, date: String) {
        val jsonObject = JSONObject()
        jsonObject.put(KEY_LAST_NAME, lastName)
        jsonObject.put(KEY_FIRST_NAME, firstName)
        jsonObject.put(KEY_DATE, date)
        val age = calculdeAge(dateOfBirth.text.toString())
        jsonObject.put(KEY_AGE, age)
        val data = jsonObject.toString()
        File(cacheDir.absolutePath, "user_data.json").writeText(data)
    }


    private fun calculdeAge(date: String): Int {

        var age = 0

        try {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            val dates = simpleDateFormat.parse(date)


            val today = getInstance();
            val birth = getInstance();

            birth.time = dates

            val thisYear = today.get(YEAR)
            val yearBirth = birth.get(YEAR)

            age = thisYear - yearBirth

            val thisMonth = today.get(MONTH)
            val birthMonth = birth.get(MONTH)

            if(birthMonth > thisMonth){
                age--
            }else if (birthMonth == thisMonth){
                val thisDay = today.get(DAY_OF_MONTH)
                val birthDay = birth.get(DAY_OF_MONTH)

                if(birthDay > thisDay){
                    age--
                }
            }
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return age
    }

    companion object {
        private const val KEY_LAST_NAME = "KEY_LAST_NAME"
        private const val KEY_FIRST_NAME = "KEY_FIRST_NAME"
        private const val KEY_DATE = "KEY_DATE"
        private const val KEY_AGE = "KEY_AGE"

    }


}







