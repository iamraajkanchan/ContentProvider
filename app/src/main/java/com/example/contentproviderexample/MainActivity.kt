package com.example.contentproviderexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mColumnProjection: Array<String> = arrayOf(
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        } else {
            ContactsContract.Contacts.DISPLAY_NAME
        },
        ContactsContract.Contacts.CONTACT_STATUS,
        ContactsContract.Contacts.HAS_PHONE_NUMBER
    )

    private val mSelectionClause = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + "Raaj"
    private val mSelectionArguments = arrayOf("Raaj")
    private val mOrderBy = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 100).also {
                loadContacts()
            }
        } else {
            loadContacts()
        }
    }

    private fun loadContacts() {
        val contentResolver = contentResolver
        val cursor =
            contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                mColumnProjection,
                null,
                null,
                mOrderBy
            )

        if (cursor != null && cursor.count > 0) {
            val stringBuilderQueryResult = StringBuilder()
            while (cursor.moveToNext()) {
                stringBuilderQueryResult.append(
                    cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(
                        2
                    ) + "\n"
                )
            }
            tvQueryResult.text = stringBuilderQueryResult.toString()
        } else {
            tvQueryResult.text = "No Contacts Found"
        }
    }
}