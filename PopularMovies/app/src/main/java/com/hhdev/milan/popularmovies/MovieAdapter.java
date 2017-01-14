package com.hhdev.milan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    public JSONObject[] movieDetails;

    public MovieAdapter(JSONObject[] movieDetails) {
        this.movieDetails = movieDetails;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.movie_image);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view =  layoutInflater.inflate(R.layout.movie_item,parent,false);
        return new MovieViewHolder(view);
    }

    public void onBindViewHolder(MovieViewHolder viewHolder, int pos){
        try{
            String url = NetworkTools.THE_MOVIE_DB_IMG_URL + movieDetails[pos].getString("poster_path");
            Context context = viewHolder.poster.getContext();
            viewHolder.poster.setTag(movieDetails[pos]);
            Log.d("DEBUG",url);
            Picasso.with(context).load(url).into(viewHolder.poster);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public int getItemCount(){
        if(movieDetails == null) return 0;
        return movieDetails.length;
    }

    public void swapData(JSONObject[] details){
        movieDetails = details;
        notifyDataSetChanged();
    }

}
