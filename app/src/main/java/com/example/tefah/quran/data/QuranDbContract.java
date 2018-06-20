package com.example.tefah.quran.data;

import android.provider.BaseColumns;

/**
 * Created by yasseralaa on 15/05/18.
 */

public class QuranDbContract {
    public static final class QuranEntry implements BaseColumns {
        public static final String TABLE_NAME = "Quran";
        public static final String COLUMN_SURA_ID = "SuraID";
        public static final String COLUMN_VERSE_ID = "VerseID";
        public static final String COLUMN_VERSE_TEXT = "AyahText";
        public static final String COLUMN_SURA_NAME = "SuraName";

    }
}
