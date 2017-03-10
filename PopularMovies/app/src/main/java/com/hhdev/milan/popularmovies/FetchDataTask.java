package com.hhdev.milan.popularmovies;

import android.os.AsyncTask;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONObject;

class FetchDataTask extends AsyncTask<String,Void,JSONObject[]> {

    FinishedListener finished;
    String key;

    public FetchDataTask(FinishedListener finished,String key){
        this.finished = finished;
        this.key = key;
    }

    public interface FinishedListener{
        void taskFinished(JSONObject[] data);
    }

    protected void onPreExecute(){
        super.onPreExecute();
    }

    protected void onPostExecute(JSONObject[] data){
        finished.taskFinished(data);
    }

    protected JSONObject[] doInBackground(String[] params){
        this.finished = finished;
        if(params.length == 0){
            return null;
        }
        try{
            String jsonResponse = NetworkTools.getResponseFromHTTP(NetworkTools.buildUrl(params[0], key,params[1])); //insert your own key here
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray(NetworkTools.RESULTS);
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