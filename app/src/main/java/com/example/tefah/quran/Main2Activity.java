package com.example.tefah.quran;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tefah.quran.POJO.Aya;
import com.example.tefah.quran.data.DataBaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private int suraNumber;
    String aya = "";
    private DataBaseHelper mDBHelper;
    List<Aya> Ayas;
    TextView QuranView ;
    List<String> separators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        suraNumber = intent.getIntExtra(Intent.EXTRA_TEXT,2);
        mDBHelper = new DataBaseHelper(this);
        // check exists Database
        File database = getApplicationContext().getDatabasePath(DataBaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        QuranView = (TextView) findViewById(R.id.QuranView);
        aya =  mDBHelper.getaya(1,1);
        Ayas = mDBHelper.getListProduct(2);
        String sora = "";
        for (Aya ayaTemp : Ayas){
            sora += ayaTemp.getAyasText();
            sora += " ﴿" + ayaTemp.getAyaNum() + "﴾";
        }
        QuranView.setText(sora);



    }
    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DataBaseHelper.DBNAME);
            String outFileName = DataBaseHelper.DBLOCATION + DataBaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
