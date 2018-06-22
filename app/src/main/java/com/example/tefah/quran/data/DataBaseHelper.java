package com.example.tefah.quran.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tefah.quran.POJO.Aya;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasseralaa on 17/06/18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "quran.sqlite";
    public static final String DBLOCATION = "/data/data/com.example.tefah.quran/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase ;

    public DataBaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
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
    public  String getaya(int ayaNumber , int suraNumber){
        String Sura = Integer.toString(suraNumber);
        String aya = Integer.toString(ayaNumber);
        openDatabase();

        Cursor cursor =  mDatabase.rawQuery("SELECT text FROM quran_text WHERE sura = "+Sura+" and aya = "+ayaNumber,null);

       cursor.moveToFirst();
        String ayaText = cursor.getString(cursor.getColumnIndex("text"));
        cursor.close();
        closeDatabase();
        return ayaText;
    }
    public List<Aya> getListProduct(int suraNumber) {
        String Sura = Integer.toString(suraNumber);

        List<Aya> ayaList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT text FROM quran_text where sura = "+Sura, null);
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
    public static List<String> suraNames(){
        List<String> suraName = new ArrayList<String>();
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
        suraName.add("سورة النمل") ;
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
        suraName.add("سورة الفيل");;
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
}
