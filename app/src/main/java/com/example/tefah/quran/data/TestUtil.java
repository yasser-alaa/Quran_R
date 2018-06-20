package com.example.tefah.quran.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasseralaa on 15/05/18.
 */

public class TestUtil {


    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 1);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 1);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT,"بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ");
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفاتحة");

        list.add(cv);

        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 1);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 2);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT,"الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَِ ");
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفاتحة");

        list.add(cv);
//-------------------------------------------------------------------------------------------

        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 112);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 1);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT,"قُلْ هُوَ اللَّهُ أَحَدٌ");
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الاخلاص");


        list.add(cv);



        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 112);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 2);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT,"اللَّهُ الصَّمَدُ");
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الاخلاص");


        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 112);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 3);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT,"لمْ يَلِدْ وَلَمْ يُولَدٌْ");
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الاخلاص");


        list.add(cv);



        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 112);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 4);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT,"وَلَمْ يَكُنْ لَهُ كُفُوًا");
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الاخلاص");


        list.add(cv);

        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 113);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 1);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفلق");

        list.add(cv);



        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 113);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 2);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "مِنْ شَرِّ مَا خَلَقَِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفلق");

        list.add(cv);




        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 113);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 3);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "وَمِنْ شَرِّ غَاسِقٍ إِذَاِ وَقَبَ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفلق");

        list.add(cv);
        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 113);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 4);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "وَمِنْ شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفلق");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 113);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 5);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "وَمِنْ شَرِّ غَاسِقٍ إِذَا وَقَبَِِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفلق");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 113);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 6);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "وَمِنْ شَرِّ حاسد اذا حسد" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الفلق");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 114);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 1);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "قل اعوذ برب الناسِِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الناس");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 114);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 2);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "ملك الناسِِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الناس");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 114);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 3);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "اله الناسِِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الناس");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 113);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 4);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "وَمِنْ شَرِّ الوسواس الخناسِِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الناس");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 114);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 5);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "الذى يوسوس فى صدور الناس" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الناس");

        list.add(cv);


        cv = new ContentValues();
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_ID, 114);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_ID, 6);
        cv.put(QuranDbContract.QuranEntry.COLUMN_VERSE_TEXT, "من الجنة و الناسِِ" );
        cv.put(QuranDbContract.QuranEntry.COLUMN_SURA_NAME,"الناس");

        list.add(cv);
        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (QuranDbContract.QuranEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){

                db.insert(QuranDbContract.QuranEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }

}

