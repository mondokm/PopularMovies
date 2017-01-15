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
    GridLayoutManager layoutManager;
    LoadMoreListener listener;

    boolean popular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        loadingIndicator = (ProgressBar) findViewById(R.id.main_loading);

        layoutManager = new GridLayoutManager(this,2);
        listener = new LoadMoreListener();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MovieAdapter());
        recyclerView.addOnScrollListener(listener);

        fetchData(NetworkTools.POPULAR_URL,1+"");
        popular = true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public void fetchData(String url,String page){
        new FetchDataTask().execute(url,page);
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
                        ((MovieAdapter)recyclerView.getAdapter()).clearData();
                        listener.reset();
                        fetchData(NetworkTools.POPULAR_URL,1+"");
                        setTitle(getString(R.string.popular));
                        return true;
                    }
                case R.id.sort_menu_top_rated:
                    if(!popular) return true;
                    else {
                        popular = false;
                        ((MovieAdapter)recyclerView.getAdapter()).clearData();
                        listener.reset();
                        fetchData(NetworkTools.TOP_RATED_URL,1+"");
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
            ((MovieAdapter)recyclerView.getAdapter()).addData(data);
            listener.loadingDone();
            recyclerView.setVisibility(View.VISIBLE);
        }

        protected JSONObject[] doInBackground(String[] params){
            if(params.length == 0){
                return null;
            }
            try{
                String jsonResponse = NetworkTools.getResponseFromHTTP(NetworkTools.buildUrl(params[0], getString(R.string.themoviedb_key_v3),params[1])); //insert your own key here
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

    public class LoadMoreListener extends RecyclerView.OnScrollListener{

        int currentPage;
        boolean loading;

        public LoadMoreListener(){
            currentPage = 1;
            loading = false;
        }

        public void onScrolled(RecyclerView recyclerView,int dx,int dy){

            if(loading){
                return;
            }

            if(dy>0){

                int totalCount = layoutManager.getItemCount();
                int currentlyVisible = layoutManager.getChildCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                if((lastVisible+currentlyVisible)>=totalCount) {
                    loading = true;
                    loadingIndicator.setVisibility(View.VISIBLE);
                    currentPage++;
                    if (popular) {
                        fetchData(NetworkTools.POPULAR_URL, currentPage + "");
                    } else {
                        fetchData(NetworkTools.TOP_RATED_URL, currentPage + "");
                    }
                }
            }

        }

        public void loadingDone(){
            loading=false;
            loadingIndicator.setVisibility(View.GONE);
        }

        public void reset(){
            currentPage = 1;
            loading = false;
        }
    }
}
