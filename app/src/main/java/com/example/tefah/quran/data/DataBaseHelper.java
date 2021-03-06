package com.example.tefah.quran.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.tefah.quran.QuranInfo.*;
import com.example.tefah.quran.multiArrays;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by yasseralaa on 17/06/18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "quran.sqlite";
    private static final int DATABASE_VERSION = 2;
    private static final String SP_KEY_DB_VER = "db_ver";
    public static final String DBLOCATION = "/data/data/com.example.tefah.quran/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DataBaseHelper(Context context) {
        super(context, DBNAME, null, 3);
        this.mContext = context;
        initialize();
    }

    /**
     * Initializes database. Creates database if doesn't exist.
     */
    private void initialize() {
        if (databaseExists()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DATABASE_VERSION != dbVersion) {
                File dbFile = mContext.getDatabasePath(DBNAME);
                if (!dbFile.delete()) {
                    Log.w(TAG, "Unable to update database");
                }
            }
        }
        if (!databaseExists()) {
            createDatabase();
        }
    }

    /**
     * Returns true if database file exists, false otherwise.
     *
     * @return
     */
    private boolean databaseExists() {
        File dbFile = mContext.getDatabasePath(DBNAME);
        return dbFile.exists();
    }

    /**
     * Creates database by copying it from assets directory.
     */
    private void createDatabase() {
        String parentPath = mContext.getDatabasePath(DBNAME).getParent();
        String path = mContext.getDatabasePath(DBNAME).getPath();

        File file = new File(parentPath);
        if (!file.exists()) {
            if (!file.mkdir()) {
                Log.w(TAG, "Unable to create database directory");
                return;
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = mContext.getAssets().open(DBNAME);
            os = new FileOutputStream(path);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SP_KEY_DB_VER, DATABASE_VERSION);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public String getaya(int ayaNumber, int suraNumber) {
        String Sura = Integer.toString(suraNumber);
        String aya = Integer.toString(ayaNumber);
        String ayaText = " ";
        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT text FROM quran_text WHERE sura = " + Sura + " and aya = " + ayaNumber, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
             ayaText = cursor.getString(cursor.getColumnIndex("text"));
        }
        cursor.close();
        closeDatabase();
        return ayaText;
    }
    public multiArrays SearchWith(String Txt) {
        String[] Ayah, surah,Ayah_num,surah_num;
        List<String>Chapters = suraNames();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT text,sura,aya FROM quran_text WHERE text LIKE " + "'%" + Txt + "%' ", null);
        cursor.moveToFirst();
        Ayah = new String[cursor.getCount()];
        surah = new String[cursor.getCount()];
        Ayah_num=new String[cursor.getCount()];
        surah_num=new String[cursor.getCount()];
        int counter = 0;
        while (!cursor.isAfterLast()) {
            Ayah[counter] = cursor.getString(cursor.getColumnIndex("text"));
            Ayah_num[counter]=cursor.getString(cursor.getColumnIndex("aya"));
            String sura_num = cursor.getString(cursor.getColumnIndex("sura"));
            surah_num[counter]=sura_num;
            surah[counter] = Chapters.get(Integer.parseInt(sura_num)-1);
            counter++;
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        multiArrays m = new multiArrays();
        m.Ayah = Ayah;
        m.Sura = surah;
        m.Ayah_num = Ayah_num;
        m.Sura_num = surah_num;
        return m;
    }

    public int getSuraNumber(String idOfSura){
        int suraNum=0;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("select sura from quran_text where text = '"+ idOfSura+"'",null);
        cursor.moveToFirst();
        suraNum =  cursor.getInt(0);
        cursor.close();
        closeDatabase();
        return suraNum;
    }
    public List<Aya> getListOfAyas() {
        List<Aya> ayaList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT text FROM quran_text ", null);
        cursor.moveToFirst();
        int counter = 0;
        while (!cursor.isAfterLast()) {

            ayaList.add(new Aya(cursor.getString(cursor.getColumnIndex("text")), counter));
            counter++;
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return ayaList;
    }


    public static List<String> suraNames() {
        List<String> suraName = new ArrayList<String>();
        suraName.add("سورة الفاتحة");
        suraName.add("سورة البقرة");
        suraName.add("سورة ال عمران");
        suraName.add("سورة النساء");
        suraName.add("سورة المائدة");
        suraName.add("سورة الأنعام");
        suraName.add("سورة الأعراف");
        suraName.add("سورة الأنفال");
        suraName.add("سورة التوبة");
        suraName.add("سورة يونس");
        suraName.add("سورة هود");
        suraName.add("سورة يوسف");
        suraName.add("سورة الرعد");
        suraName.add("سورة ابراهيم");
        suraName.add("سورة الحجر");
        suraName.add("سورة النحل");
        suraName.add("سورة الاسراء");
        suraName.add("سورة الكهف");
        suraName.add("سورة مريم");
        suraName.add("سورة طه");
        suraName.add("سورة الأنبياء");
        suraName.add("سورة الحج");
        suraName.add("سورة المؤمنون");
        suraName.add("سورة النور");
        suraName.add("سورة الفرقان");
        suraName.add("سورة الشعراء");
        suraName.add("سورة النمل");
        suraName.add("سورة القصص");
        suraName.add("سورة العنكبوت");
        suraName.add("سورة الروم");
        suraName.add("سورة لقمان");
        suraName.add("سورة السجدة");
        suraName.add("سورة الأحزاب");
        suraName.add("سورة سبأ");
        suraName.add("سورة فاطر");
        suraName.add("سورة يس");
        suraName.add("سورة الصافات");
        suraName.add("سورة ص");
        suraName.add("سورة الزمر");
        suraName.add("سورة غافر");
        suraName.add("سورة فصلت");
        suraName.add("سورة الشورى");
        suraName.add("سورة الزخرف");
        suraName.add("سورة الدخان");
        suraName.add("سورة الجاثية");
        suraName.add("سورة الأحقاف");
        suraName.add("سورة محمد");
        suraName.add("سورة الفتح");
        suraName.add("سورة الحجرات");
        suraName.add("سورة ق");
        suraName.add("سورة الذاريات");
        suraName.add("سورة الطور");
        suraName.add("سورة النجم");
        suraName.add("سورة القمر");
        suraName.add("سورة الرحمن");
        suraName.add("سورة الواقعة");
        suraName.add("سورة الحديد");
        suraName.add("سورة المجادلة");
        suraName.add("سورة الحشر");
        suraName.add("سورة الممتحنة");
        suraName.add("سورة الصف");
        suraName.add("سورة الجمعة");
        suraName.add("سورة المنافقون");
        suraName.add("سورة التغابن");
        suraName.add("سورة الطلاق");
        suraName.add("سورة التحريم");
        suraName.add("سورة الملك");
        suraName.add("سورة القلم");
        suraName.add("سورة الحاقة");
        suraName.add("سورة المعارج");
        suraName.add("سورة نوح");
        suraName.add("سورة الجن");
        suraName.add("سورة المزمل");
        suraName.add("سورة المدثر");
        suraName.add("سورة القيامة");
        suraName.add("سورة الانسان");
        suraName.add("سورة المرسلات");
        suraName.add("سورة عم");
        suraName.add("سورة النازعات");
        suraName.add("سورة عبس");
        suraName.add("سورة التكوير");
        suraName.add("سورة الانفطار");
        suraName.add("سورة المطففين");
        suraName.add("سورة الانشقاق");
        suraName.add("سورة البروج");
        suraName.add("سورة الطارق");
        suraName.add("سورة الأعلى");
        suraName.add("سورة الغاشية");
        suraName.add("سورة الفجر");
        suraName.add("سورة البلد");
        suraName.add("سورة الشمس");
        suraName.add("سورة الليل");
        suraName.add("سورة الضحى");
        suraName.add("سورة الشرح");
        suraName.add("سورة التين");
        suraName.add("سورة العلق");
        suraName.add("سورة القدر");
        suraName.add("سورة البينة");
        suraName.add("سورة الزلزلة");
        suraName.add("سورة العاديات");
        suraName.add("سورة القارعة");
        suraName.add("سورة التكاثر");
        suraName.add("سورة العصر");
        suraName.add("سورة الهمزة");
        suraName.add("سورة الفيل");
        ;
        suraName.add("سورة قريش");
        suraName.add("سورة الماعون");
        suraName.add("سورة الكوثر");
        suraName.add("سورة الكافرون");
        suraName.add("سورة النصر");
        suraName.add("سورة المسد");
        suraName.add("سورة الاخلاص");
        suraName.add("سورة العلق");
        suraName.add("سورة الناس");

        return suraName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

    public List<Page> getListOfpages() {

        List<Page> pageList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Book2 ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            pageList.add(new Page(cursor.getInt(cursor.getColumnIndex("aya")), cursor.getInt(cursor.getColumnIndex("page")), cursor.getInt(cursor.getColumnIndex("sura"))));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return pageList;
    }

    public List<Sura> getListOfSewar() {
        List<Sura> suraList = new ArrayList<>();
        openDatabase();
        Cursor cursorAyatCount = mDatabase.rawQuery("select max(aya) from quran_text" +
                " group by sura ", null);
        cursorAyatCount.moveToFirst();
        while (!cursorAyatCount.isAfterLast()) {
            //calc. number of ayat fe kol sura
            suraList.add(new Sura(cursorAyatCount.getInt(0), cursorAyatCount.getPosition()));
            cursorAyatCount.moveToNext();
        }
        cursorAyatCount.close();

        /*
        //set end page of every sura
        */
        int counter = 0;       //counter that accessing the sewar list
        int endPageNumber=604, suraNum;
        //specific case of fateha
        suraList.get(counter).setStartPage(1);
        suraList.get(counter).setEndPage(1);

       // counter++;
       // store end pages of sewar
        Cursor cursorEndPage = mDatabase.rawQuery("select page,sura from Book2 group by sura ", null);
        cursorEndPage.moveToFirst();
        while (!cursorEndPage.isAfterLast()) {
            if (counter != 114) {
                suraNum = cursorEndPage.getInt(1);  //el rakam el sa7e7 ely fel quran
                endPageNumber = cursorEndPage.getInt(0);

                suraList.get(counter).setEndPage(endPageNumber);
                cursorEndPage.moveToNext();
                //check if there are sewar fe el nos has same end page
                if(!cursorEndPage.isAfterLast()){
                    if((cursorEndPage.getInt(1) - suraNum) == 2) {
                        // kam sura fatet fe el nos (have same end page)>>assign to them the same end page num
                            counter++;
                            suraList.get(counter).setEndPage(endPageNumber);
                    }
                    else if((cursorEndPage.getInt(1) - suraNum) == 3){
                        counter++;
                        suraList.get(counter).setEndPage(endPageNumber);
                        counter++;
                        suraList.get(counter).setEndPage(endPageNumber);
                    }
                        counter++;

                }
            }
        }
        counter++;
        suraList.get(counter).setEndPage(endPageNumber);
        counter++;
        suraList.get(counter).setEndPage(endPageNumber);
        cursorEndPage.close();
        closeDatabase();
        /*
        *calc start page
        * then store them in suraList */
        List<Page> pageList = getListOfpages();
        int startAyahOfNextPage =0 ,currentSuraEndPageNumber,nextSuraEndPageNumber ,nextSuraStartPageNumber=0;
        for(int i =0;i<113;i++){
            //law el soraten m3ndhmsh nafs el endpage yb2a aked homa msh m3 b3d fe nafs el page
            // zay el baqra w 2al 3mran
            currentSuraEndPageNumber = suraList.get(i).getEndPage();
            nextSuraEndPageNumber = suraList.get(i+1).getEndPage();
            if(i==85){//sura el a3la
                Log.d("CRASH","msh by3rd 7aga fe el do7a , crach");
            }
            if((currentSuraEndPageNumber+1) < 604){  //3ashan ama yewsl le a5r page
                startAyahOfNextPage = pageList.get(currentSuraEndPageNumber).getPageStartAyahNumber();
            }

            if(currentSuraEndPageNumber != nextSuraEndPageNumber){
                 /*case 1 :
                *   sura ends and the next start at the new page*/
                if(startAyahOfNextPage == 1){
                    nextSuraStartPageNumber = currentSuraEndPageNumber+1;
                    suraList.get(i+1).setStartPage(nextSuraStartPageNumber);
                }
            /*case 2 :
            * sura ends then the next starts and ends in another page
            * zay el ma2da maslan*/
                else{
                    nextSuraStartPageNumber = currentSuraEndPageNumber;
                    suraList.get(i+1).setStartPage(nextSuraStartPageNumber);
                }
            }
             /*case 3 :
            * sura ends then the next starts and ends in the same page
            * zay el do7a w el shar7*/
            else if(currentSuraEndPageNumber == nextSuraEndPageNumber){
                nextSuraStartPageNumber = currentSuraEndPageNumber;
                    suraList.get(i+1).setStartPage(nextSuraStartPageNumber);
            }
        }

       /* first we get all ayat of quran
         we want to store each list of ayat in each sura
        one big forloop for number of sewar
         one small loop for ayat in every sura*/
        List<Aya> allOfAyat = getListOfAyas();
        int ayahCounter = 0;     // da el pointer of ely hymshy 3la ayat el quran kolha 3ashan
        // y7othm fe list el ayat ely fe kol sora
        int suraNumOfAyat;
        for (int i = 0; i < 114; i++) {
            suraNumOfAyat = suraList.get(i).getayatCount();  //number of ayat in sura
            for (int j = 0; j < suraNumOfAyat; j++) {
                suraList.get(i). //el sura
                        setListOfAyat(allOfAyat.get(ayahCounter));
                ayahCounter++;
            }
        }
        return suraList;
    }
}
