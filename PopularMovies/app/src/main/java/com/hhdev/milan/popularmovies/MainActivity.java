package com.hhdev.milan.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        loadingIndicator = (ProgressBar) findViewById(R.id.main_loading);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.main_menu_settings:
                //open settings
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchDataTask extends AsyncTask<String,Void,JSONObject[]>{

        protected void onPreExecute(){
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(JSONObject[] data){
            loadingIndicator.setVisibility(View.GONE);
            recyclerView.setAdapter(new MovieAdapter(data));
            recyclerView.setVisibility(View.VISIBLE);

        }

        protected JSONObject[] doInBackground(String[] params){
            if(params.length == 0){
                return null;
            }
            try{
                String jsonResponse = NetworkTools.getResponseFromHTTP(NetworkTools.buildUrl(params[0], getString(R.string.themoviedb_key_v3))); //insert your own key here
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray results = jsonObject.getJSONArray("results");
                JSONObject[] movieDetails = new JSONObject[results.length()];
                for(int i = 0;i<results.length();i++){
                    movieDetails[i] = results.getJSONObject(i);
                }
                return movieDetails;
                
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

    }
}
