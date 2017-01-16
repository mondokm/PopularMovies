package com.hhdev.milan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        try {
            JSONObject data = new JSONObject(intent.getExtras().getString(Intent.EXTRA_TEXT));
            setTitle(data.getString("title"));
            ((TextView) findViewById(R.id.overview)).setText(data.getString("overview"));
            ((TextView) findViewById(R.id.rating)).setText(data.getString("vote_average"));
            ((TextView) findViewById(R.id.release_date)).setText(data.getString("release_date"));
            Picasso.with(this).load(NetworkTools.THE_MOVIE_DB_IMG_URL+data.getString("poster_path")).into((ImageView)findViewById(R.id.detail_poster));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
