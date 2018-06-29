package com.example.tefah.quran.QuranInfo;

/**
 * Created by yasseralaa on 27/06/18.
 */

public class Page {
    int startAyah ;
    int pageNumber;
    int sura;

    public Page(int startAyah, int pageNumber, int sura) {
        this.startAyah = startAyah;
        this.pageNumber = pageNumber;
        this.sura = sura;
    }

    public int getSuraNumber() {
        return sura;
    }

    public void setSura(int sura) {
        this.sura = sura;
    }

    public Page(int startAyah, int pageNumber) {
        this.startAyah = startAyah;
        this.pageNumber = pageNumber;
    }

    public int getPageStartAyahNumber() {
        return startAyah;
    }

    public void setStartAyah(int startAyah) {
        this.startAyah = startAyah;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
