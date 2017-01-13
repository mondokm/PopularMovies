package com.hhdev.milan.popularmovies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkTools {

    public static String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    public static String TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top-rated";
    static String THE_MOVIE_DB_IMG_URL = "http://image.tmdb.org/t/p/w185/";

    public static String getResponseFromHTTP(URL url) throws IOException{
        HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream inputStream = urlconnection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else{
                return null;
            }
        } finally{
            urlconnection.disconnect();
        }
    }

    public URL buildUrl(String baseUrl, String key){
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("api_key", key)
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
