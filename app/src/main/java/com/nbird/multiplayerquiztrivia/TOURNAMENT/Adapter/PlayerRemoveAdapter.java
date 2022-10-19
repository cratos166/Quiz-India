package com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PlayerRemoveAdapter extends RecyclerView.Adapter<PlayerRemoveAdapter.MyViewHolder> {
    private Context mContext;
    private List<Details> mData;
    private int[] arr;



    public PlayerRemoveAdapter(Context mContext, List<Details> mData,int[] arr){
        this.mContext=mContext;
        this.mData=mData;
        this.arr=arr;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.tournament_remove_player_item,parent,false);

        return new MyViewHolder(view);

    }






    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.name.setText(mData.get(position).getName());

        Glide.with(mContext).load(mData.get(position).getImageURL()).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(holder.propic);



        holder.cardview_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(arr[position]==0){
                     arr[position]=1;
                     holder.linear1.setBackgroundResource(R.drawable.double_color_2);

                 }else{
                     arr[position]=0;
                     holder.linear1.setBackgroundResource(R.drawable.border_theme);
                }
            }
        });


    }





    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView propic;
        CardView cardview_id;
        LinearLayout linear1;


        public MyViewHolder(View itemView){
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.text1);
            propic=(ImageView) itemView.findViewById(R.id.img1);
            cardview_id=(CardView) itemView.findViewById(R.id.cardview_id);
            linear1=(LinearLayout) itemView.findViewById(R.id.linear1);

        }


    }

}