package com.solari.database.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PersonDbHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_PETS_TABLE =
            "CREATE TABLE " + PersonContract.PersonEntry.TABLE_NAME.toString() + "(" +
                    PersonContract.PersonEntry._ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PersonContract.PersonEntry.COLUMN_PERSON_ANGLEONE.toString() + " INTEGER NOT NULL, " +
                    PersonContract.PersonEntry.COLUMN_PERSON_ANGLETWO.toString() + " INTEGER NOT NULL, " +
                    PersonContract.PersonEntry.COLUMN_PERSON_ANGLETHREE.toString() + " INTEGER NOT NULL);"
        db.execSQL(SQL_CREATE_PETS_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
    }

    companion object {
        private const val DATABASE_NAME = "person.db"
        private const val DATABASE_VERSION = 1
    }
}
