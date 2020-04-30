package com.solari.database

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.solari.database.data.PersonContract
import com.solari.database.data.PersonDbHelper


class MainActivity : AppCompatActivity() {
    /** Database helper that will provide us access to the database  */
    private var mDbHelper: PersonDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
// and pass the context, which is the current activity.
        mDbHelper = PersonDbHelper(this)
    }

    override fun onStart() {
        super.onStart()
        insertPerson()
        displayDatabaseInfo()
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private fun displayDatabaseInfo() { // Create and/or open a database to read from it
        val db = mDbHelper!!.readableDatabase

        // Perform a query on the pets table
        // Perform a query on the pets table
        val projection = arrayOf<String>(
            PersonContract.PersonEntry._ID,
            PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE,
            PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO,
            PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE
        )
        val cursor: Cursor =  db.query(
            PersonContract.PersonEntry.TABLE_NAME,   // The table to query
            projection,            // The columns to return
            null,                  // The columns for the WHERE clause
            null,                  // The values for the WHERE clause
            null,                  // Don't group the rows
            null,                  // Don't filter by row groups
            null);
        val displayView = findViewById<View>(R.id.text1) as TextView
        try { // Create a header in the Text View that looks like this:
//
// The pets table contains <number of rows in Cursor> pets.
// _id - name - breed - gender - weight
//
// In the while loop below, iterate through the rows of the cursor and display
// the information from each column in this order.
            displayView.text =
                "The persons table contains " + cursor.getCount().toString() + " persons.\n\n"
            displayView.append(
                PersonContract.PersonEntry._ID.toString() + " - " +
                        PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE + " - " +
                        PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO + " - " +
                        PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE+"\n"
            )
            // Figure out the index of each column
            val idColumnIndex: Int = cursor.getColumnIndex(PersonContract.PersonEntry._ID)
            val angleOneColumnIndex: Int = cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE)
            val angleTwoColumnIndex: Int = cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO)
            val angleThreeColumnIndex: Int = cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE)
            while (cursor.moveToNext()) { // Use that index to extract the String or Int value of the word
// at the current row the cursor is on.
                val currentID: Int = cursor.getInt(idColumnIndex)
                val currentAngleOne: Int = cursor.getInt(angleOneColumnIndex)
                val currentAngleTwo: Int = cursor.getInt(angleTwoColumnIndex)
                val currentAngleThree: Int = cursor.getInt(angleThreeColumnIndex)

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(
                    "\n" + currentID + " - " +
                            currentAngleOne + " - " +
                            currentAngleTwo + " - " +
                            currentAngleThree
                )
            }
        } finally { // Always close the cursor when you're done reading from it. This releases all its
// resources and makes it invalid.
            cursor.close()
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private fun insertPerson() { // Gets the database in write mode
        // Gets the database in write mode
        // Gets the database in write mode
        val db = mDbHelper!!.writableDatabase

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        // Create a ContentValues object where column names are the keys,
// and Toto's pet attributes are the values.
        val values = ContentValues()
        values.put(PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE, 7)
        values.put(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO, 10)
        values.put(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE, 57)

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        // Insert a new row for Toto in the database, returning the ID of that new row.
// The first argument for db.insert() is the pets table name.
// The second argument provides the name of a column in which the framework
// can insert NULL in the event that the ContentValues is empty (if
// this is set to "null", then the framework will not insert a row when
// there are no values).
// The third argument is the ContentValues object containing the info for Toto.
        val newRowId = db.insert(PersonContract.PersonEntry.TABLE_NAME, null, values)
    }
}
