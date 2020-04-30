package com.solari.database.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log

class PersonProvider : ContentProvider() {
    companion object {
        /** URI matcher code for the content URI for the persons table  */
        private const val PERSONS = 100
        /** URI matcher code for the content URI for a single person in the persons table  */
        private const val PERSONS_ID = 101
        /**
         * UriMatcher object to match a content URI to a corresponding code.
         * The input passed into the constructor represents the code to return for the root URI.
         * It's common to use NO_MATCH as the input for this case.
         */
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        val LOG_TAG: String = PersonProvider::class.java.getSimpleName()

        // Static initializer. This is run the first time anything is called from this class.
        init { // The calls to addURI() go here, for all of the content URI patterns that the provider
// should recognize. All paths added to the UriMatcher have a corresponding code to return
// when a match is found.
            sUriMatcher.addURI(
                PersonContract.CONTENT_AUTHORITY,
                PersonContract.PATH_PERSONS,
                PERSONS
            )
            sUriMatcher.addURI(
                PersonContract.CONTENT_AUTHORITY,
                PersonContract.PATH_PERSONS.toString() + "/#",
                PERSONS_ID
            )
        }
    }

    private var mDbHelper: PersonDbHelper? = null
    override fun onCreate(): Boolean {
        mDbHelper = PersonDbHelper(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        var selection = selection
        var selectionArgs = selectionArgs
        val db: SQLiteDatabase = mDbHelper!!.getReadableDatabase()
        val cursor: Cursor
        val match = sUriMatcher.match(uri)
        when (match) {
            PERSONS -> cursor = db.query(
                PersonContract.PersonEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            PERSONS_ID -> {
                selection = PersonContract.PersonEntry._ID.toString() + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = db.query(
                    PersonContract.PersonEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
            else -> throw IllegalArgumentException("Can not query unknown URI $uri")
        }
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        val match = sUriMatcher.match(uri)
        return when (match) {
            PERSONS -> PersonContract.PersonEntry.CONTENT_LIST_TYPE
            PERSONS_ID -> PersonContract.PersonEntry.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        return when (match) {
            PERSONS -> insertPerson(uri, values)
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }
    }

    private fun insertPerson(
        uri: Uri,
        values: ContentValues?
    ): Uri? { // Check that the angleONE is not null
        val angleOne = values!!.getAsInteger(PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE)
        require(angleOne == null) { "Person requires an angleOne" }

        val angleTwo = values!!.getAsInteger(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO)
        require(angleTwo == null ) { "Person requires an angleTwo" }

        val angleThree = values!!.getAsInteger(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE)
        require(angleThree == null) { "Person requires an angleThree" }

// Get writeable database
        val database: SQLiteDatabase = mDbHelper!!.getWritableDatabase()
        // Insert the new pet with the given values
        val id = database.insert(PersonContract.PersonEntry.TABLE_NAME, null, values)
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1L) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }
        //notify all listeners that data has changed for the pet content URI
        context!!.contentResolver.notifyChange(uri, null)
        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id)
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int { // Get writeable database
        var selection = selection
        var selectionArgs = selectionArgs
        val database: SQLiteDatabase = mDbHelper!!.getWritableDatabase()
        val rowsDeleted: Int
        val match = sUriMatcher.match(uri)
        when (match) {
            PERSONS ->  // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(PersonContract.PersonEntry.TABLE_NAME, selection, selectionArgs)
            PERSONS_ID -> {
                // Delete a single row given by the ID in the URI
                selection = PersonContract.PersonEntry._ID.toString() + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                rowsDeleted = database.delete(PersonContract.PersonEntry.TABLE_NAME, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Deletion is not supported for $uri")
        }
        if (rowsDeleted != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rowsDeleted
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val match = sUriMatcher.match(uri)
        return when (match) {
            PERSONS -> updatePerson(
                uri,
                contentValues,
                selection,
                selectionArgs
            )
            PERSONS_ID -> {
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PersonContract.PersonEntry._ID.toString() + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                updatePerson(uri, contentValues, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Update is not supported for $uri")
        }
    }

    private fun updatePerson(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int { // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
// check that the name value is not null.

        if (values!!.containsKey(PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE)) { // Check that the weight is greater than or equal to 0 kg
            val angleOne = values!!.getAsInteger(PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE)
            require(!(angleOne != null && angleOne < 0)) { "Person requires valid angle" }
        }
        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
// check that the gender value is valid.
        if (values!!.containsKey(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO)) { // Check that the weight is greater than or equal to 0 kg
            val angleTwo = values!!.getAsInteger(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO)
            require(!(angleTwo != null && angleTwo < 0)) { "Person requires valid angle" }
        }
        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
// check that the weight value is valid.
        if (values!!.containsKey(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE)) { // Check that the weight is greater than or equal to 0 kg
            val angleThree = values!!.getAsInteger(PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE)
            require(!(angleThree != null && angleThree < 0)) { "Person requires valid angle" }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0
        }
        // TODO: Update the selected pets in the pets database table with the given ContentValues
// TODO: Return the number of rows that were affected
        val database: SQLiteDatabase = mDbHelper!!.getWritableDatabase()
        context!!.contentResolver.notifyChange(uri, null)
        // Insert the new pet with the given values
        val rowsUpdated =
            database.update(PersonContract.PersonEntry.TABLE_NAME, values, selection, selectionArgs)
        // If 1 or more rows were updated, then notify all listeners that the data at the
// given URI has changed
        if (rowsUpdated != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rowsUpdated
    }
}
