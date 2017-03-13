package com.hhdev.milan.popularmovies;

import android.os.AsyncTask;

import java.io.IOException;

class FetchDataTask extends AsyncTask<String,Void,String> {

    FinishedListener finished;
    String key;

    public FetchDataTask(FinishedListener finished,String key){
        this.finished = finished;
        this.key = key;
    }

    public interface FinishedListener{
        void taskFinished(String response);
    }

    protected void onPreExecute(){
        super.onPreExecute();
    }

    protected void onPostExecute(String response){
        finished.taskFinished(response);
    }

    protected String doInBackground(String[] params){
        if(params.length == 0){
            return null;
        }
        try {
            return NetworkTools.getResponseFromHTTP(NetworkTools.buildUrl(params[0], key,params[1])); //insert your own key here
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}