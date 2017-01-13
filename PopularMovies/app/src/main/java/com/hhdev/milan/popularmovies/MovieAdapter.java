package com.hhdev.milan.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    public String[] posterPaths;

    public MovieAdapter(String[] posterPaths) {
        this.posterPaths = posterPaths;
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
        String url = NetworkTools.THE_MOVIE_DB_IMG_URL + posterPaths[pos];
        Context context = viewHolder.poster.getContext();
        Picasso.with(context).load(url).into(viewHolder.poster);
    }

    public int getItemCount(){
        if(posterPaths == null) return 0;
        return posterPaths.length;
    }

    public void swapData(String[] paths){
        posterPaths = paths;
        notifyDataSetChanged();
    }

}
