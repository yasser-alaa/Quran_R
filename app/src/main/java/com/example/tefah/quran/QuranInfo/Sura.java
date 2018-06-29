package com.example.tefah.quran.QuranInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasseralaa on 26/06/18.
 */

public class Sura {
    int ayatCount ,suraNumber,startPage,endPage;
    List<Aya> ayaList = new ArrayList<>();

    public int getAyatCount() {
        return ayatCount;
    }

    public void setAyatCount(int ayatCount) {
        this.ayatCount = ayatCount;
    }

    public List<Aya> getListOfAyat() {
        return ayaList;
    }

    public void setListOfAyat(Aya a) {
        this.ayaList.add(a);
    }

    public Sura(int ayatCount, int suraNumber) {
        this.ayatCount = ayatCount;
        this.suraNumber = suraNumber;
    }

    public Sura(int ayatCount, int startAyah, int endAyay, int suraNumber, int startPage, int endPage) {
        this.ayatCount = ayatCount;
        this.suraNumber = suraNumber;
        this.startPage = startPage;
        this.endPage = endPage;
    }

    public int getayatCount() {
        return ayatCount;
    }

    public void setayatCount(int ayatCount) {
        this.ayatCount = ayatCount;
    }

    public int getSuraNumber() {
        return suraNumber;
    }

    public void setSuraNumber(int suraNumber) {
        this.suraNumber = suraNumber;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
}
