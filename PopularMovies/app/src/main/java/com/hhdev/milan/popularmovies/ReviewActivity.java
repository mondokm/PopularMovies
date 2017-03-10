package com.hhdev.milan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        JSONObject[] data = NetworkTools.responseToArray(intent.getExtras().getString(Intent.EXTRA_TEXT));
        try{
            if(data.length==0) System.out.println("There are no reviews for this movie."); else System.out.println(data[0].getString("content"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
