package com.hhdev.milan.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    static String THE_MOVIE_DB_BASE_URL = "http://image.tmdb.org/t/p/";

    class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.movie_image);
        }
    }

}
