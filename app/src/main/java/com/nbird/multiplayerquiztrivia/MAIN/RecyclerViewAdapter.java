package com.nbird.multiplayerquiztrivia.MAIN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nbird.multiplayerquiztrivia.Dialog.DialogOnlineLocal;
import com.nbird.multiplayerquiztrivia.Dialog.DialogQuizMode;
import com.nbird.multiplayerquiztrivia.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Modes> mData;
    int a=1;

    public RecyclerViewAdapter(Context mContext, List<Modes> mData){
        this.mContext=mContext;
        this.mData=mData;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.cardviewmainmenu,parent,false);

        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.tv_exam_title.setText(mData.get(position).getTitle());
        holder.img_exam_thumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.exam_dis.setText(mData.get(position).getDis());



       holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                a=position+1;
                switch (a){
                    case 1:
                        DialogQuizMode dialogQuizMode =new DialogQuizMode();
                        dialogQuizMode.start(mContext,holder.cardView,2);
                        break;
                    case 3:
                        DialogQuizMode dialogQuizMode1 =new DialogQuizMode();
                        dialogQuizMode1.start(mContext,holder.cardView,1);
                       break;



                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_exam_title;
        ImageView img_exam_thumbnail;
        TextView exam_dis;
        CardView cardView;


        public MyViewHolder(View itemView){
            super(itemView);

            tv_exam_title=(TextView) itemView.findViewById(R.id.exam_title);
            img_exam_thumbnail=(ImageView) itemView.findViewById(R.id.exam_img_id);
            exam_dis=(TextView) itemView.findViewById(R.id.exam_dis);
            cardView=(CardView) itemView.findViewById(R.id.cardview_id);

        }


    }

}
