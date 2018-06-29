package com.example.tefah.quran;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tefah.quran.QuranInfo.*;
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
    List<Aya> ayas;
    List<Page> pageList;
    List<Sura> suraList;
    int startPagePosition;
    LayoutInflater inflater;    //Used to create individual pages
    ViewPager vp;               //Reference to class to swipe views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDBHelper = new DataBaseHelper(this);

        //get ayat into list of ayat class
        ayas = mDBHelper.getListOfAyas();
       pageList = mDBHelper.getListOfpages();
       suraList = mDBHelper.getListOfSewar();

        //get sura number that has been clicked
        Intent intent = getIntent();
        suraNumber = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        startPagePosition = suraList.get(suraNumber).getStartPage()-1;
        //get an inflater to be used to create single pages
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Reference ViewPager defined in activity
        vp = (ViewPager) findViewById(R.id.viewPager);
        //set the adapter that will create the individual pages
        vp.setAdapter(new MyPagesAdapter());
        vp.setCurrentItem(startPagePosition);


    }

    class MyPagesAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 604;
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View page = inflater.inflate(R.layout.page, null);
            /*TODO (1)
            number of lines*/
            String pageTextDisplay =getPageTextDisplay(position);
           ((TextView)(page.findViewById(R.id.ayatQuran))).setText(pageTextDisplay);
            //Add the page to the front of the queue
            ((ViewPager) container).addView(page, 0);
            return page;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }


    }

    private String getPageTextDisplay(int position) {
        String textDisplayed="";
        int ayahCounter =8; // initialy for fateha
        int currentSuraNumber = pageList.get(position).getSuraNumber()-1;
        int nextSuraNumber = pageList.get(position+1).getSuraNumber()-1;

        int pageStartAyahNumber = pageList.get(position).getPageStartAyahNumber();
        int nextPageStartAyahNumber = pageList.get(position+1).getPageStartAyahNumber();
        //first case :
            //law page ely ba3dha nafs lesa nafs el sura mekamela
        if( nextSuraNumber == currentSuraNumber){
            ayahCounter = pageList.get(position+1).getPageStartAyahNumber() ;
            for(int i = pageStartAyahNumber -1;i < ayahCounter-1 ;i++){
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
        }
        // matensash te3ml 7esab te3dy condition lel fateha
        //case 2:
            //el sura has end in the end of page
        else if(nextSuraNumber-currentSuraNumber==1&&nextPageStartAyahNumber==1){
            ayahCounter = suraList.get(currentSuraNumber).getAyatCount() +1; //last ayah in sura
            for(int i = pageStartAyahNumber -1;i < ayahCounter-1 ;i++){
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
        }
        //case 3:
            //sura ends in middle of page
        else if(nextSuraNumber - currentSuraNumber == 1 && nextPageStartAyahNumber >1){

            ayahCounter = suraList.get(currentSuraNumber).getAyatCount() +1; //last ayah in sura
            for(int i = pageStartAyahNumber -1;i < ayahCounter-1 ;i++){
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
            currentSuraNumber++;  //update current sura because it will begin in same page
            int lastAyahInPage = nextPageStartAyahNumber -1;
            for(int i=0 ;i<lastAyahInPage;i++){
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                                    .get(i).getAyahText();
            }

        }

        //case 4:
            //sura ends in middle of page but the other one start and ends in same page too
        else if(nextSuraNumber - currentSuraNumber == 2 && nextPageStartAyahNumber==1){

        }



        return textDisplayed;
    }

}