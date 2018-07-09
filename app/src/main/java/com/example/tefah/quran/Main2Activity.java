package com.example.tefah.quran;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tefah.quran.QuranInfo.*;
import com.example.tefah.quran.data.DataBaseHelper;
import com.example.tefah.quran.data.DataBaseHelper2;

import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private int suraNumber;
    private int ayaNumServer;
    String mAyaSearched = "";
    private DataBaseHelper mDBHelper;
    private DataBaseHelper2 mDBHelper2;
    List<Aya> ayas;
    List<Page> pageList;
    List<Sura> suraList;
    List<String> resultAyas;
    int startPagePosition;      //number of page that it will show (send it to viewpager)
    LayoutInflater inflater;    //Used to create individual pages
    ViewPager vp;               //Reference to class to swipe views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDBHelper = new DataBaseHelper(this);
        mDBHelper2 =new DataBaseHelper2(this);
        //get all ayat into list of ayat class
        ayas = mDBHelper.getListOfAyas();
        ayas = mDBHelper2.getListOfAyas();
        pageList = mDBHelper.getListOfpages();
        suraList = mDBHelper.getListOfSewar();

        //get sura number that has been clicked
        Intent intent = getIntent();
        //get aya writen in searchBOX
        /*
        * algorithm
        * 1- get the text from editBOX
        * 2- search for the sequence
        *       if detected in ayah then add this aya to the result list
        *       continue search till ayas end*/

        if(intent.hasExtra(getString(R.string.aya_returned)) &&
                intent.hasExtra(getString(R.string.sura_returned))){
            suraNumber = intent.getIntExtra(getString(R.string.sura_returned),2)-1;
            ayaNumServer = intent.getIntExtra(getString(R.string.aya_returned),3);
            String resultAya = mDBHelper.getaya(ayaNumServer,suraNumber+1);
            Toast.makeText(this,resultAya + suraNumber+"  "+ayaNumServer,Toast.LENGTH_LONG).show();
        }
        if(intent.hasExtra("su")){
            suraNumber = intent.getIntExtra("su",1)-1;

            /*for(Aya ayaTemp : ayas){
                if(ayaTemp.getAyahText().contains(mAyaSearched)){
                    resultAyas.add(ayaTemp.getAyahText());
                    suraNumber = mDBHelper.getSuraNumber(ayaTemp.getAyaNum());
                }
            }*/
        }
        else if(intent.hasExtra(Intent.EXTRA_INDEX)){
            suraNumber = intent.getIntExtra(Intent.EXTRA_INDEX, 1);
        }

        startPagePosition = suraList.get(suraNumber).getStartPage() - 1;
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
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "ZekrQuran.ttf");

            /*TODO (1)
            number of lines*/
            String pageTextDisplay = getPageTextDisplay(position);
            pageTextDisplay = Utils.translateNumbers(pageTextDisplay);
            TextView ayatTV =  (TextView) (page.findViewById(R.id.ayatQuran));
            ayatTV.setTypeface(custom_font);
            ayatTV.setText(pageTextDisplay);

         //   ((TextView) (page.findViewById(R.id.ayatQuran))).setText(pageTextDisplay);
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
        String textDisplayed = "";
        int ayahCounter = 8; // initialy for fateha
        int currentSuraNumber = pageList.get(position).getSuraNumber() - 1;
        int startPageOfCurrentSura = suraList.get(currentSuraNumber).getStartPage();
        int endPageOfCurrentSura = suraList.get(currentSuraNumber).getEndPage();
        if(position == 603){
            position--;
            //handle last page
            position--;
            ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
            for (int i = 0; i < ayahCounter - 1; i++) {
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
            currentSuraNumber++;
            ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
            for (int i = 0; i < ayahCounter - 1; i++) {
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
            currentSuraNumber++;
            ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
            for (int i = 0; i < ayahCounter - 1; i++) {
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
        }
        int nextSuraNumber = pageList.get(position + 1).getSuraNumber() - 1;
        int pageStartAyahNumber = pageList.get(position).getPageStartAyahNumber();
        int nextPageStartAyahNumber = pageList.get(position + 1).getPageStartAyahNumber();

        //first case :
        //law page ely ba3dha nafs lesa nafs el sura mekamela

        if (nextSuraNumber == currentSuraNumber) {
            ayahCounter = pageList.get(position + 1).getPageStartAyahNumber();
            for (int i = pageStartAyahNumber - 1; i < ayahCounter - 1; i++) {
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
        }

        if (endPageOfCurrentSura == position + 1) {
            //e3rd el sora le a5rha
            ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
            for (int i = pageStartAyahNumber - 1; i < ayahCounter - 1; i++) {
                textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                        .get(i).getAyahText();
                textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
            }
            currentSuraNumber++; // sura ends so we must update sura number to display the next
            endPageOfCurrentSura = suraList.get(currentSuraNumber).getEndPage();
            startPageOfCurrentSura = suraList.get(currentSuraNumber).getStartPage();
            //check if sura ends in same page or hatstmr
            //if hatstmr
            if (endPageOfCurrentSura != startPageOfCurrentSura) {
                /*
                * this block will display it till end of page*/
                int lastAyahInPage = nextPageStartAyahNumber - 1;
                for (int i = 0; i < lastAyahInPage; i++) {
                    textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                            .get(i).getAyahText();
                    textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                }
            }
            // tayeb low hat5ls fe nafs el page fe e7tmal men etnen ya 2ma hat5ls
            // fe nehayt el saf7a ya2ma hat5ls w wa7daa tanya tebd2 w el tanya de momkn t5ls fe nehayt el saf7a
            // aw b3dha kman
            else if (endPageOfCurrentSura == startPageOfCurrentSura) {
                if (nextSuraNumber - currentSuraNumber == 1)// ma3na kda enha a5r sura fe el saf7a >>display only
                {
                    ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
                    for (int i = 0; i < ayahCounter - 1; i++) {
                        textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                .get(i).getAyahText();
                        textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                    }
                }
                else if (nextSuraNumber - currentSuraNumber == 2)// ma3na kda en fe sura kman fe el saf7a
                {
                    //display it to end
                    ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
                    for (int i = 0; i < ayahCounter - 1; i++) {
                        textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                .get(i).getAyahText();
                        textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                    }
                    currentSuraNumber++; // sura ends so we must update sura number to display the next
                    endPageOfCurrentSura = suraList.get(currentSuraNumber).getEndPage();
                    startPageOfCurrentSura = suraList.get(currentSuraNumber).getStartPage();
                    //law ely b3dha de hatkml
                    if (endPageOfCurrentSura != startPageOfCurrentSura) {
                        int lastAyahInPage = nextPageStartAyahNumber - 1;
                        for (int i = 0; i < lastAyahInPage; i++) {
                            textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                    .get(i).getAyahText();
                            textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                        }
                    }
                    //law hatnthy
                    else if(endPageOfCurrentSura == startPageOfCurrentSura)
                    {
                        //display it to end
                        ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
                        for (int i = 0; i < ayahCounter - 1; i++) {
                            textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                    .get(i).getAyahText();
                            textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                        }
                    }

                }
                else if(nextSuraNumber - currentSuraNumber == 3)//fe 3 sewar ma3rofen fe el a5r fe saf7a wa7da
                {
                    //display it to end
                    ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
                    for (int i = 0; i < ayahCounter - 1; i++) {
                        textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                .get(i).getAyahText();
                        textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                    }
                    currentSuraNumber++;
                    ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
                    for (int i = 0; i < ayahCounter - 1; i++) {
                        textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                .get(i).getAyahText();
                        textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                    }
                    currentSuraNumber++;
                    ayahCounter = suraList.get(currentSuraNumber).getAyatCount() + 1; //last ayah in sura
                    for (int i = 0; i < ayahCounter - 1; i++) {
                        textDisplayed += suraList.get(currentSuraNumber).getListOfAyat()
                                .get(i).getAyahText();
                        textDisplayed += " ﴿" + ayas.get(i).getAyaNum() + "﴾";
                    }
                }
            }
        }
        return textDisplayed;
    }

}