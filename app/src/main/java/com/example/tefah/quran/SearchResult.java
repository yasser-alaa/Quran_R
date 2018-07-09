package com.example.tefah.quran;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tefah.quran.data.DataBaseHelper;
import com.example.tefah.quran.data.DataBaseHelper2;

public class SearchResult extends RecyclerView.Adapter<SearchResult.ViewHolder> {

    private String[] verses;
    private String[] Chapters;
    private int[] Ayah_Num;
    Context mcontext ;
    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Ayah;
        public TextView su;
        DataBaseHelper db = new DataBaseHelper(mcontext);

        public ViewHolder(View itemView) {
            super(itemView);
            su = (TextView) itemView.findViewById(R.id.details);
            Ayah = (TextView) itemView.findViewById(R.id.Ayah);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(mcontext,Main2Activity.class);
                    int ssss = db.getSuraNumber(Ayah.getText().toString());
                    intent.putExtra("su",ssss);
                    mcontext.startActivity(intent);
                                    }
            });
        }
    }
    public SearchResult(Context cont ,String txt){

        DataBaseHelper db = new DataBaseHelper(cont);
        //*********0*
        mcontext = cont;
        multiArrays res = db.SearchWith(txt);
        verses = res.Ayah;
        Chapters = res.Sura;
        if(res.Ayah.length != 0){
            return;
        }

        //------------------
        DataBaseHelper2 db2 = new DataBaseHelper2(cont);
        //*********0*
        mcontext = cont;
        res = db2.SearchWith(txt);
        verses = res.Ayah;
        Chapters = res.Sura;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Ayah.setText(verses[position]);
        holder.su.setText(Chapters[position]);
    }

    @Override
    public int getItemCount() {
        return verses.length;
    }


}

