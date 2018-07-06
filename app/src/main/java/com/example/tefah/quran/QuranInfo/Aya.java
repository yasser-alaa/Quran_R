package com.example.tefah.quran.QuranInfo;

import java.util.List;

/**
 * Created by yasseralaa on 20/06/18.
 */

public class Aya {
    private String ayahText ;
    int ayaNum;
    boolean startPage=false,endPage=false;

    public Aya(String ayahText, int ayaNum) {
        this.ayahText = ayahText;
        this.ayaNum = ayaNum;
    }
    public Aya(String ayahText) {
        this.ayahText = ayahText;
    }
    public int getAyaNum() {
        return ayaNum;
    }

    public void setAyaNum(int ayaNum) {
        this.ayaNum = ayaNum;
    }

    public String getAyahText() {
        return ayahText;
    }

    public void setAyahText(String ayasText) {
        ayahText = ayasText;
    }
    public boolean isStartPage() {
        return startPage;
    }

    public void setStartPage(boolean startPage) {
        this.startPage = startPage;
    }

    public boolean isEndPage() {
        return endPage;
    }

    public void setEndPage(boolean endPage) {
        this.endPage = endPage;
    }
}
