package com.hhdev.milan.popularmovies;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkTools {

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

}
