package com.example.androidtoolbox

import android.Manifest
import android.Manifest.permission
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_permissions.*
import java.security.AccessController.getContext


class PermissionsActivity : AppCompatActivity()  {

    val contacts = mutableListOf<String>()


    lateinit var currentPhotoPath: String
    private val TAG = "Permission"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        imageView.setOnClickListener {
            showPictureDialog();
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }
        getContacts()
        //contactRecycler.adapter = ContactAdapter(listOf("Juliette", "Melvin"))
        contactRecycler.adapter = ContactAdapter(contacts.sorted())
        //adapter permet de gerer l'affichage de chacune des cellules
        contactRecycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.data?.let{ //Nous return l'URI + Le .let va me dire si c'est pas nul
            permImg.setImageURI(it) // it = Conversion automatique par le let
        }
    }

    companion object { // Creer des statics
        private const  val REQUEST_CODE_GALLERY = 22
        private const  val RECORD_REQUEST_CODE = 101
    }

    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> pickImageFromGallery()
                    1 -> takePictureIntent()
                }
            })
        pictureDialog.show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 1001)
            }
        }
    }
    private fun getContacts() {
        setupPermissionsContact()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contacts.add("  Nom : $name")
            }
        } else {
            Toast.makeText(applicationContext, "No contacts available!", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }
    private fun setupPermissionsContact (){
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS)
        if (permission != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission to record denied")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), RECORD_REQUEST_CODE)
        }

    }



}