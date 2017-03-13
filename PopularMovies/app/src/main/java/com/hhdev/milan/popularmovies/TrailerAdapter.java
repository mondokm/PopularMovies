package com.hhdev.milan.popularmovies;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {


    private JSONObject[] trailers;
    TrailerListener listener;

    public TrailerAdapter(TrailerListener listener){
        this.listener=listener;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;
        CardView card;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.trailer_title);
            card = (CardView) itemView.findViewById(R.id.trailer_card);
            icon = (ImageView) itemView.findViewById(R.id.play_icon);

        }

        public void onClick(View v){
            listener.onTrailerClick((JSONObject)v.getTag());
        }

    }

    public interface TrailerListener{
        void onTrailerClick(JSONObject object);
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view =  layoutInflater.inflate(R.layout.trailer_item,parent,false);
        return new TrailerViewHolder(view);
    }

    public void onBindViewHolder(TrailerViewHolder viewHolder, int pos){
        try{
            viewHolder.title.setText(trailers[pos].getString(NetworkTools.NAME));
            viewHolder.title.setOnClickListener(viewHolder);
            viewHolder.title.setTag(trailers[pos]);
            viewHolder.icon.setOnClickListener(viewHolder);
            viewHolder.icon.setTag(trailers[pos]);
            viewHolder.card.setOnClickListener(viewHolder);
            viewHolder.card.setTag(trailers[pos]);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public int getItemCount(){
        if(trailers == null) return 0;
        return trailers.length;
    }

    public void setData(JSONObject[] trailers){
        this.trailers = trailers;
        notifyDataSetChanged();
    }

}
