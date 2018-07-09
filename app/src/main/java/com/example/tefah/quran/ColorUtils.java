package com.example.tefah.quran;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

/**
 * Created by yasseralaa on 15/05/18.
 */

public class ColorUtils {

    /**
     * This method returns the appropriate shade of green to form the gradient
     * seen in the list, based off of the order in which the
     * {@link com.example.android.recyclerview.GreenAdapter.NumberViewHolder}
     * instance was created.
     *
     * This method is used to show how ViewHolders are recycled in a RecyclerView.
     * At first, the colors will form a nice, consistent gradient. As the
     * RecyclerView is scrolled, the
     * {@link com.example.android.recyclerview.GreenAdapter.NumberViewHolder}'s will be
     * recycled and the list will no longer appear as a consistent gradient.
     *
     * @param context     Context for getting colors
     * @param instanceNum Order in which the calling ViewHolder was created
     *
     * @return A shade of green based off of when the calling ViewHolder
     * was created.
     */

    public static int getViewHolderBackgroundColorFromInstance(Context context, int instanceNum) {
        switch (instanceNum) {
            case 0:
                return ContextCompat.getColor(context, R.color.mushaf2);
            case 1:
                return ContextCompat.getColor(context, R.color.mushaf3);
            case 2:
                return ContextCompat.getColor(context, R.color.mushaf4);
            case 3:
                return ContextCompat.getColor(context, R.color.mushaf5);
            case 4:
                return ContextCompat.getColor(context, R.color.mushaf2);
            case 5:
                return ContextCompat.getColor(context, R.color.mushaf3);
            case 6:
                return ContextCompat.getColor(context, R.color.mushaf4);
            case 7:
                return ContextCompat.getColor(context, R.color.mushaf5);
            case 8:
                return ContextCompat.getColor(context, R.color.mushaf2);
            case 9:
                return ContextCompat.getColor(context, R.color.mushaf3);
            case 10:
                return ContextCompat.getColor(context, R.color.mushaf4);
            case 11:
                return ContextCompat.getColor(context, R.color.mushaf5);
            case 12:
                return ContextCompat.getColor(context, R.color.mushaf2);
            case 13:
                return ContextCompat.getColor(context, R.color.mushaf3);
            case 14:
                return ContextCompat.getColor(context, R.color.mushaf4);
            case 15:
                return ContextCompat.getColor(context, R.color.mushaf5);
            case 16:
                return ContextCompat.getColor(context, R.color.mushaf2);
            case 17:
                return ContextCompat.getColor(context, R.color.mushaf3);

            default:
                return Color.WHITE;
        }
    }
}
