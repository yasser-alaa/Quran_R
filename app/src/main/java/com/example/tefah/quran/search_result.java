package com.example.tefah.quran;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


public class search_result extends AppCompatActivity {

    private RecyclerView sResultList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager recyclerManager;
    private  String sTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sResultList= findViewById(R.id.search_result_recycleView);
        sResultList.setHasFixedSize(true);
        recyclerManager = new LinearLayoutManager(this);
        sResultList.setLayoutManager(recyclerManager);
        Intent intent = getIntent();

        sTxt = intent.getStringExtra("sTxt");
        Toast.makeText(search_result.this, sTxt, Toast.LENGTH_SHORT).show();

        SearchResult sAdapter  = new SearchResult(search_result.this,sTxt);
        sResultList.setAdapter(sAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i1 = new Intent(this,MainActivity.class);
                startActivity(i1);
        }
        return true;
    }

}