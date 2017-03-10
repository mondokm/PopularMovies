package com.hhdev.milan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {


    private JSONObject[] reviews;

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView author;
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
        }

    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view =  layoutInflater.inflate(R.layout.review_item,parent,false);
        return new ReviewViewHolder(view);
    }

    public void onBindViewHolder(ReviewViewHolder viewHolder, int pos){
        try{
            viewHolder.author.setText(reviews[pos].getString(NetworkTools.AUTHOR));
            viewHolder.content.setText(reviews[pos].getString(NetworkTools.CONTENT));
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public int getItemCount(){
        if(reviews == null) return 0;
        return reviews.length;
    }

    public void setData(JSONObject[] reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }

}
