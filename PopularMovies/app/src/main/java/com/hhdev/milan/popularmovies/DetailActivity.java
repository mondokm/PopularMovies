package com.hhdev.milan.popularmovies;

import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity{

    JSONObject data;
    RecyclerView trailersView;
    TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        try {
            data = new JSONObject(intent.getExtras().getString(Intent.EXTRA_TEXT));
            setTitle(data.getString(NetworkTools.TITLE));
            ((TextView) findViewById(R.id.overview)).setText(data.getString(NetworkTools.OVERVIEW));
            ((TextView) findViewById(R.id.rating)).setText(data.getString(NetworkTools.VOTE_AVERAGE)+getString(R.string.vote_average));
            ((TextView) findViewById(R.id.release_date)).setText(data.getString(NetworkTools.RELEASE_DATE).substring(0,4));
            Picasso.with(this).load(NetworkTools.THE_MOVIE_DB_IMG_URL+data.getString(NetworkTools.POSTER_PATH)).into((ImageView)findViewById(R.id.detail_poster));
            new FetchDataTask(new TrailerListener(),getString(R.string.themoviedb_key_v3)).execute(NetworkTools.BASE_URL+"/"+data.getString(NetworkTools.ID)+"/"+NetworkTools.VIDEOS,1+"");
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

    public void onReviewButtonClick(View v){
        try {
            new FetchDataTask(new TaskListener(),getString(R.string.themoviedb_key_v3)).execute(NetworkTools.BASE_URL+"/"+data.getString(NetworkTools.ID)+"/"+NetworkTools.REVIEWS,1+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class TaskListener implements FetchDataTask.FinishedListener{
        public void taskFinished(String response){
            seeReviews(response);
        }
    }

    public void seeReviews(String data){
        Intent intent = new Intent(this,ReviewActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,data);
        System.out.println(data);
        startActivity(intent);
    }

    class TrailerListener implements FetchDataTask.FinishedListener{
        public void taskFinished(String response){
            seeTrailers(response);
        }
    }

    public void seeTrailers(String data){
        JSONObject[] trailers = NetworkTools.responseToArray(data);
        trailersView = (RecyclerView) findViewById(R.id.trailer_recyclerview);
        trailerAdapter = new TrailerAdapter(new TrailerItemListener());
        trailersView.setAdapter(trailerAdapter);
        trailerAdapter.setData(trailers);
        trailersView.setLayoutManager(new LinearLayoutManager(this));
        trailersView.setVisibility(View.VISIBLE);
    }

    class TrailerItemListener implements TrailerAdapter.TrailerListener{
        @Override
        public void onTrailerClick(JSONObject object) {
            try{
                Uri uri = Uri.parse(NetworkTools.YOUTUBE_BASE_URL).buildUpon().appendQueryParameter(NetworkTools.YOUTUBE_V,object.getString(NetworkTools.KEY)).build();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
