package com.hhdev.milan.popularmovies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        try {
            JSONObject data = new JSONObject(intent.getExtras().getString(Intent.EXTRA_TEXT));
            setTitle(data.getString(NetworkTools.TITLE));
            ((TextView) findViewById(R.id.overview)).setText(data.getString(NetworkTools.OVERVIEW));
            ((TextView) findViewById(R.id.rating)).setText(data.getString(NetworkTools.VOTE_AVERAGE)+getString(R.string.vote_average));
            ((TextView) findViewById(R.id.release_date)).setText(data.getString(NetworkTools.RELEASE_DATE).substring(0,4));
            Picasso.with(this).load(NetworkTools.THE_MOVIE_DB_IMG_URL+data.getString(NetworkTools.POSTER_PATH)).into((ImageView)findViewById(R.id.detail_poster));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
