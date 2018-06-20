package com.example.tefah.quran.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yasseralaa on 15/05/18.
 */

public class QuranDbHelper extends SQLiteOpenHelper {

    //database name
    private static final String DATABASE_NAME = "Quran.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 3;


    public QuranDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_QURAN_TABLE = "CREATE TABLE " + QuranDbContract.QuranEntry.TABLE_NAME + " (" +
                QuranDbContract.QuranEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuranDbContract.QuranEntry.COLUMN_SURA_ID + " INTEGER NOT NULL, " +
                QuranDbContract.QuranEntry.COLUMN_VERSE_ID + " INTEGER NOT NULL, " +
                QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT + " TEXT NOT NULL, "+
                QuranDbContract.QuranEntry.COLUMN_SURA_NAME + " TEXT NOT NULL "+

                "); ";

        //EXcute qurey
        sqLiteDatabase.execSQL(SQL_CREATE_QURAN_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //iNSTER HERE
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QuranDbContract.QuranEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}

