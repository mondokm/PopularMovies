package com.hhdev.milan.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity{

    JSONObject data;
    RecyclerView trailersView,reviewsView;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    SQLiteDatabase sqLiteDatabase;
    Button favoriteButton;
    boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favoriteButton = (Button) findViewById(R.id.favorite_button);

        favorite = false;

        Intent intent = getIntent();
        try {
            data = new JSONObject(intent.getExtras().getString(Intent.EXTRA_TEXT));

            setTitle(data.getString(NetworkTools.TITLE));
            ((TextView) findViewById(R.id.overview)).setText(data.getString(NetworkTools.OVERVIEW));
            ((TextView) findViewById(R.id.rating)).setText(data.getString(NetworkTools.VOTE_AVERAGE)+getString(R.string.vote_average));
            ((TextView) findViewById(R.id.release_date)).setText(data.getString(NetworkTools.RELEASE_DATE).substring(0,4));
            Picasso.with(this).load(NetworkTools.THE_MOVIE_DB_IMG_URL+data.getString(NetworkTools.POSTER_PATH)).into((ImageView)findViewById(R.id.detail_poster));

            if(NetworkTools.isNetworkAvailable(this)){
                new FetchDataTask(new TrailerListener(),getString(R.string.themoviedb_key_v3)).execute(NetworkTools.BASE_URL+"/"+data.getString(NetworkTools.ID)+"/"+NetworkTools.VIDEOS,1+"");
                new FetchDataTask(new TaskListener(),getString(R.string.themoviedb_key_v3)).execute(NetworkTools.BASE_URL+"/"+data.getString(NetworkTools.ID)+"/"+NetworkTools.REVIEWS,1+"");
            } else {
                Toast.makeText(this,getString(R.string.no_internet),Toast.LENGTH_LONG).show();
            }

            FavoriteDBHelper helper = new FavoriteDBHelper(this);
            sqLiteDatabase = helper.getWritableDatabase();

            String query = "SELECT "+ FavoriteContract.Favorites.COLUMN_MOVIE_ID+" FROM "+ FavoriteContract.Favorites.TABLE_NAME+" WHERE "+ FavoriteContract.Favorites.COLUMN_MOVIE_ID+" = "+data.getString(NetworkTools.ID);
            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            if(cursor.getCount()>0) {
                favorite = true;
                favoriteButton.setText(getString(R.string.unfavorite));
            }
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

    public void onFavoriteButtonClick(View v){
        if(!favorite){
            try{
                ContentValues cv = new ContentValues();
                cv.put(FavoriteContract.Favorites.COLUMN_DATA, data.toString());
                cv.put(FavoriteContract.Favorites.COLUMN_MOVIE_ID,data.getString(NetworkTools.ID));
                //sqLiteDatabase.insert(FavoriteContract.Favorites.TABLE_NAME,null,cv);
                Uri uri = getContentResolver().insert(FavoriteContract.Favorites.CONTENT_URI, cv);

                favorite=true;
                favoriteButton.setText(getString(R.string.unfavorite));
            } catch (Exception e){
                e.printStackTrace();
            }

        } else{
            try{
                sqLiteDatabase.delete(FavoriteContract.Favorites.TABLE_NAME, FavoriteContract.Favorites.COLUMN_MOVIE_ID+"="+data.getString(NetworkTools.ID),null);
                favorite=false;
                favoriteButton.setText(getString(R.string.favorite_button_text));
            } catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public class TaskListener implements FetchDataTask.FinishedListener{
        public void taskFinished(String response){
            seeReviews(response);
        }
    }

    public void seeReviews(String data){
        JSONObject[] reviews = NetworkTools.responseToArray(data);
        reviewsView = (RecyclerView) findViewById(R.id.review_recyclerview);
        reviewAdapter = new ReviewAdapter();
        reviewsView.setAdapter(reviewAdapter);
        reviewAdapter.setData(reviews);
        reviewsView.setLayoutManager(new LinearLayoutManager(this));
        reviewsView.setVisibility(View.VISIBLE);
        reviewsView.setNestedScrollingEnabled(false);
        if(reviews.length!=0) findViewById(R.id.review_textview).setVisibility(View.VISIBLE);


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
        trailersView.setNestedScrollingEnabled(false);
        if(trailers.length!=0) findViewById(R.id.trailer_textview).setVisibility(View.VISIBLE);

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
