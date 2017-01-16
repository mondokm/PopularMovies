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
    private ItemListener itemListener;

    public MovieAdapter(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.movie_image);
        }

        public void onClick(View v){
            itemListener.onItemClick((JSONObject)poster.getTag());
        }

    }

    public interface ItemListener{
        public void onItemClick(JSONObject data);
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
            viewHolder.poster.setOnClickListener(viewHolder);
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

    public void addData(JSONObject[] newData){
        if(movieDetails==null){
            movieDetails = newData;
            notifyDataSetChanged();
            return;
        }
        JSONObject[] newArray = new JSONObject[movieDetails.length+newData.length];
        for(int i=0;i<movieDetails.length;i++){
            newArray[i]=movieDetails[i];
        }
        for(int i=movieDetails.length;i<(movieDetails.length+newData.length);i++){
            newArray[i]=newData[i-movieDetails.length];
        }
        movieDetails = newArray;
        notifyDataSetChanged();
    }

    public void clearData(){
        movieDetails=null;
        notifyDataSetChanged();
    }

}
