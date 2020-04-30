package com.solari.database.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object PersonContract {
    val CONTENT_AUTHORITY = "com.example.android.persons"
    val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")
    val PATH_PERSONS = "persons"

    // To prevent someone from accidentally instantiating the contract class,
// give it an empty constructor.
    private fun PersonContract() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    object PersonEntry : BaseColumns {
        /**
         * Name of database table for pets
         */
        val TABLE_NAME = "persons"
        /**
         * Unique ID number for the PERSON (only for use in the database table).
         *
         *
         * Type: INTEGER
         */
        val _ID = BaseColumns._ID

        val COLUMN_PERSON_ANGLEONE = "angleOne"

        val COLUMN_PERSON_ANGLETWO = "angleTwo"

        val COLUMN_PERSON_ANGLETHREE = "angleThree"

        /**
         * The MIME type of the [.CONTENT_URI] for a list of persons.
         */
        val CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSONS
        /**
         * The MIME type of the [.CONTENT_URI] for a single person.
         */
        val CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSONS
        val CONTENT_URI =
            Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PERSONS)

    }

}

