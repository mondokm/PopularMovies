package com.hhdev.milan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        JSONObject[] data = NetworkTools.responseToArray(intent.getExtras().getString(Intent.EXTRA_TEXT));
        try{
            if(data.length==0) {
                TextView textView = (TextView) findViewById(R.id.review_textview);
                textView.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.no_reviews));
            }
            else{
                recyclerView = (RecyclerView) findViewById(R.id.review_recyclerview);
                reviewAdapter = new ReviewAdapter();

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                reviewAdapter.setData(data);
                recyclerView.setAdapter(reviewAdapter);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
