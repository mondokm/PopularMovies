package com.hhdev.milan.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private GridLayoutManager layoutManager;
    private LoadMoreListener listener;
    private ImageListener imageListener;
    private SQLiteDatabase sqLiteDatabase;
    private MovieAdapter movieAdapter;

    private static final int MODE_POPULAR = 0;
    private static final int MODE_TOP_RATED = 1;
    private static final int MODE_FAVORITE = 2;

    int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        loadingIndicator = (ProgressBar) findViewById(R.id.main_loading);

        layoutManager = new GridLayoutManager(this,2);
        listener = new LoadMoreListener();
        imageListener = new ImageListener();
        movieAdapter = new MovieAdapter(imageListener);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.addOnScrollListener(listener);

        fetchData(NetworkTools.POPULAR_URL,1+"");
        mode=MODE_POPULAR;

        setupDB();
    }

    public void onResume(){
        super.onResume();
        if(mode==MODE_FAVORITE){
            loadFavorites();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    private void fetchData(String url, String page){
        loadingIndicator.setVisibility(View.VISIBLE);
        new FetchDataTask(new TaskListener(),getString(R.string.themoviedb_key_v3)).execute(url,page);
    }

    public class ImageListener implements MovieAdapter.ItemListener{
        @Override
        public void onItemClick(JSONObject data) {
            Intent intent = new Intent(MainActivity.this,DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT,data.toString());
            startActivity(intent);
        }
    }

    public void setupDB(){
        FavoriteDBHelper dbHelper = new FavoriteDBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();
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

    private class PopupMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        public boolean onMenuItemClick(MenuItem item){
            switch (item.getItemId()){
                case R.id.sort_menu_popular:
                    if(mode==MODE_POPULAR) return true;
                    else {
                        mode=MODE_POPULAR;
                        ((TextView)findViewById(R.id.no_favorites_textview)).setVisibility(View.GONE);
                        movieAdapter.clearData();
                        listener.reset();
                        fetchData(NetworkTools.POPULAR_URL,1+"");
                        setTitle(getString(R.string.popular));
                        return true;
                    }
                case R.id.sort_menu_top_rated:
                    if(mode==MODE_TOP_RATED) return true;
                    else {
                        mode=MODE_TOP_RATED;
                        ((TextView)findViewById(R.id.no_favorites_textview)).setVisibility(View.GONE);
                        movieAdapter.clearData();
                        listener.reset();
                        fetchData(NetworkTools.TOP_RATED_URL,1+"");
                        setTitle(getString(R.string.top_rated));
                        return true;
                    }
                case R.id.sort_menu_favorite:
                    if(mode==MODE_FAVORITE) return true;
                    else {
                        mode=MODE_FAVORITE;
                        loadFavorites();
                    }
                 default:
                     return false;
            }
        }
    }

    public class TaskListener implements com.hhdev.milan.popularmovies.FetchDataTask.FinishedListener{
        public void taskFinished(String response){
            movieAdapter.addData(NetworkTools.responseToArray(response));
            listener.loadingDone();
            recyclerView.setVisibility(View.VISIBLE);
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

            if(dy>0 && mode!=MODE_FAVORITE){

                int totalCount = layoutManager.getItemCount();
                int currentlyVisible = layoutManager.getChildCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                if((lastVisible+currentlyVisible)>=totalCount) {
                    loading = true;
                    loadingIndicator.setVisibility(View.VISIBLE);
                    currentPage++;
                    switch (mode) {
                        case MODE_POPULAR:
                            fetchData(NetworkTools.POPULAR_URL, currentPage + "");
                        case MODE_TOP_RATED:
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

    public Cursor getFavorites(){
        String[] columns = {FavoriteContract.Favorites.COLUMN_DATA};
        return sqLiteDatabase.query(
                FavoriteContract.Favorites.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                FavoriteContract.Favorites._ID);

    }

    public void loadFavorites(){
        Cursor cursor = getFavorites();
        JSONObject[] movies = new JSONObject[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            try{
                cursor.moveToPosition(i);
                movies[i]= new JSONObject(cursor.getString(cursor.getColumnIndex(FavoriteContract.Favorites.COLUMN_DATA)));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if(movies.length==0) ((TextView)findViewById(R.id.no_favorites_textview)).setVisibility(View.VISIBLE);
        movieAdapter.clearData();
        movieAdapter.addData(movies);
    }
}
