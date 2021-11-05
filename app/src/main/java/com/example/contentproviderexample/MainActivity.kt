package com.example.contentproviderexample

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    private var isFirstTimeLoaded = false

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
        btContacts.setOnClickListener {
            if (isFirstTimeLoaded) {
                LoaderManager.getInstance(this).restartLoader(1, null, this)
            } else {
                LoaderManager.getInstance(this).initLoader(1, null, this)
                isFirstTimeLoaded = true
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        if (id == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSIONS_REQUEST_READ_CONTACTS
                )
            } else {
                return CursorLoader(
                    this,
                    ContactsContract.Contacts.CONTENT_URI,
                    mColumnProjection,
                    null,
                    null,
                    mOrderBy
                )
            }
        }
        return Loader(this)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null && data.count > 0) {
            val stringBuilderQueryResult = StringBuilder()
            while (data.moveToNext()) {
                stringBuilderQueryResult.append(
                    data.getString(0) + " " + data.getString(1) + " " + data.getString(
                        2
                    ) + "\n"
                )
            }
            tvQueryResult.text = stringBuilderQueryResult.toString()
        } else {
            tvQueryResult.text = "No Contacts Found"
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}