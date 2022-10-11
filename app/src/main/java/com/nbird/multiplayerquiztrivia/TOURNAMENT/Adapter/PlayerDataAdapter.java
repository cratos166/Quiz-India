package com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.multiplayerquiztrivia.Dialog.DialogQuizMode;
import com.nbird.multiplayerquiztrivia.MAIN.Modes;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.JoinCreateTournamentDialoge;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.List;

public class PlayerDataAdapter extends RecyclerView.Adapter<PlayerDataAdapter.MyViewHolder> {
    private Context mContext;
    private List<Details> mData;

    public PlayerDataAdapter(Context mContext, List<Details> mData){
        this.mContext=mContext;
        this.mData=mData;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.lobby_detail_layout,parent,false);

        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.name.setText(mData.get(position).getName());

        Glide.with(mContext).load(mData.get(position).getImageURL()).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(holder.propic);


        holder.totalTimeTaken.setText(mData.get(position).getTotalTime());
        holder.accuracy.setText(mData.get(position).getAccuracy());
        holder.score.setText(mData.get(position).getHighestScore());


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,totalTimeTaken,accuracy,score;
        ImageView propic;



        public MyViewHolder(View itemView){
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.name);
            propic=(ImageView) itemView.findViewById(R.id.propic);
            totalTimeTaken=(TextView) itemView.findViewById(R.id.totalTimeTaken);
            accuracy=(TextView) itemView.findViewById(R.id.accuracy);
            score=(TextView) itemView.findViewById(R.id.score);
        }


    }

}
