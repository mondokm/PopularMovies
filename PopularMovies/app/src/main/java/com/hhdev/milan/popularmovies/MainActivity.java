package com.hhdev.milan.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar loadingIndicator;

    boolean popular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        loadingIndicator = (ProgressBar) findViewById(R.id.main_loading);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new MovieAdapter());
        fetchData(NetworkTools.POPULAR_URL);
        popular = true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public void fetchData(String url){
        new FetchDataTask().execute(url);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.main_menu_sort:
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,findViewById(R.id.main_menu_sort));
                popupMenu.setOnMenuItemClickListener(new PopupMenuItemClickListener());
                popupMenu.inflate(R.menu.sort_menu);
                popupMenu.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class PopupMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        public boolean onMenuItemClick(MenuItem item){
            switch (item.getItemId()){
                case R.id.sort_menu_popular:
                    if(popular) return true;
                    else {
                        popular = true;
                        fetchData(NetworkTools.POPULAR_URL);
                        setTitle(getString(R.string.popular));
                        return true;
                    }
                case R.id.sort_menu_top_rated:
                    if(!popular) return true;
                    else {
                        popular = false;
                        fetchData(NetworkTools.TOP_RATED_URL);
                        setTitle(getString(R.string.top_rated));
                        return true;
                    }
                 default:
                     return false;
            }
        }
    }

    public class FetchDataTask extends AsyncTask<String,Void,JSONObject[]>{

        protected void onPreExecute(){
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(JSONObject[] data){
            loadingIndicator.setVisibility(View.GONE);
            ((MovieAdapter)recyclerView.getAdapter()).swapData(data);
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
