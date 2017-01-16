package com.hhdev.milan.popularmovies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class NetworkTools {

    public static final String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    public static final String TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";
    public static final String THE_MOVIE_DB_IMG_URL = "http://image.tmdb.org/t/p/w342/";
    public static final String RESULTS = "results";
    public static final String OVERVIEW = "overview";
    public static final String TITLE = "title";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_PATH = "poster_path";
    public static final String API_KEY = "api_key";
    public static final String PAGE = "page";



    public static String getResponseFromHTTP(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else{
                return null;
            }
        } finally{
            urlConnection.disconnect();
        }
    }

    public static URL buildUrl(String baseUrl, String key, String page){
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(NetworkTools.API_KEY, key)
                .appendQueryParameter(NetworkTools.PAGE, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

}
