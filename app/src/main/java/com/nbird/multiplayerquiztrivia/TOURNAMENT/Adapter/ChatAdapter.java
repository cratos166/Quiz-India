package com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.ChatHolder;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context mContext;
    ArrayList<ChatHolder> chatListArray;

    public ChatAdapter(Context mContext, ArrayList<ChatHolder> chatListArray){
        this.mContext=mContext;
        this.chatListArray=chatListArray;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.chat_item,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.name.setText(chatListArray.get(position).getName());
        holder.ans.setText(chatListArray.get(position).getAnswer());

    }


    @Override
    public int getItemCount() {
        return chatListArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,ans;



        public MyViewHolder(View itemView){
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.name);
            ans=(TextView) itemView.findViewById(R.id.ans);


        }
    }
}
