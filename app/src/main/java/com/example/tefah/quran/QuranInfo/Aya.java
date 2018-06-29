package com.example.tefah.quran.POJO;

import java.util.List;

/**
 * Created by yasseralaa on 20/06/18.
 */

public class Aya {
    private String AyasText ;
    int ayaNum;

    public Aya(String ayasText, int ayaNum) {
        AyasText = ayasText;
        this.ayaNum = ayaNum;
    }

    public int getAyaNum() {
        return ayaNum;
    }

    public void setAyaNum(int ayaNum) {
        this.ayaNum = ayaNum;
    }

    public Aya(String ayasText) {
        AyasText = ayasText;
    }

    public String getAyasText() {
        return AyasText;
    }

    public void setAyasText(String ayasText) {
        AyasText = ayasText;
    }


}
