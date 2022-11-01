package com.nbird.multiplayerquiztrivia.BUZZER.ADAPTER;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.PlayerDisplayBuzzerHolder;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;

public class PlayerDisplayInBuzzerAdapter extends RecyclerView.Adapter<PlayerDisplayInBuzzerAdapter.MyViewHolder> {
    private Context mContext;
    ArrayList<PlayerDisplayBuzzerHolder> arrayList;
    int currentQuestionStatus;







    public PlayerDisplayInBuzzerAdapter(Context mContext, ArrayList<PlayerDisplayBuzzerHolder> arrayList, int currentQuestionStatus){
        this.mContext=mContext;
        this.arrayList=arrayList;
        this.currentQuestionStatus=currentQuestionStatus;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.tournament_buzzer_player_item,parent,false);

        return new MyViewHolder(view);

    }

    private void manupulator(MyViewHolder holder ,int position){
        ArrayList<ImageView> list=new ArrayList<>();




        list.add(holder.anim1);list.add(holder.anim2);list.add(holder.anim3);list.add(holder.anim4);list.add(holder.anim5);
        list.add(holder.anim6);list.add(holder.anim7);list.add(holder.anim8);list.add(holder.anim9);list.add(holder.anim10);
        list.add(holder.anim11);list.add(holder.anim12);list.add(holder.anim13);list.add(holder.anim14);list.add(holder.anim15);
        list.add(holder.anim16);list.add(holder.anim17);list.add(holder.anim18);list.add(holder.anim19);list.add(holder.anim20);


        try{

            ArrayList<Integer> arr=arrayList.get(position).getArrayList();
            for(int i=0;i<arr.size();i++){

                ImageView animationView=list.get(i);
                animationView.setBackgroundResource(0);



                if(arr.get(i)==1){
                    Log.i("position", String.valueOf(position)+arrayList.get(position).getArrayList().get(i));
                    animationView.setBackgroundResource(R.drawable.green_tick);
                    Log.i("imagevIEW", String.valueOf(animationView.getId()));

                }else if(arr.get(i)==2){
                    Log.i("position", String.valueOf(position)+arrayList.get(position).getArrayList().get(i));
                    animationView.setBackgroundResource(R.drawable.red_cross);
                    Log.i("imagevIEW", String.valueOf(animationView.getId()));

                }else if(arr.get(i)==3){
                    Log.i("position", String.valueOf(position)+arrayList.get(position).getArrayList().get(i));
                    animationView.setBackgroundResource(R.drawable.not_ans);
                    Log.i("imagevIEW", String.valueOf(animationView.getId()));
                 //   bellVisible(holder);
                }

            }

            holder.score.setText("Score : "+arrayList.get(position).getScore());




            list.clear();

        }catch (Exception e){
            list.clear();
            e.printStackTrace();
        }



    }





    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        holder.name.setText(arrayList.get(position).getName());

        Glide.with(mContext).load(arrayList.get(position).getImageURL()).apply(RequestOptions.bitmapTransform(new RoundedCorners(18))).into(holder.image);

        manupulator(holder,position);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,score;
        ImageView image;
        ImageView anim1,anim2,anim3,anim4,anim5,anim6,anim7,anim8,anim9,anim10;
        ImageView anim11,anim12,anim13,anim14,anim15,anim16,anim17,anim18,anim19,anim20;
        
        public MyViewHolder(View itemView){
            super(itemView);

            image=(ImageView) itemView.findViewById(R.id.image);
            name=(TextView) itemView.findViewById(R.id.name);
            score=(TextView) itemView.findViewById(R.id.score);

            anim1=(ImageView) itemView.findViewById(R.id.anim1);
            anim2=(ImageView) itemView.findViewById(R.id.anim2);
            anim3=(ImageView) itemView.findViewById(R.id.anim3);
            anim4=(ImageView) itemView.findViewById(R.id.anim4);
            anim5=(ImageView) itemView.findViewById(R.id.anim5);
            anim6=(ImageView) itemView.findViewById(R.id.anim6);
            anim7=(ImageView) itemView.findViewById(R.id.anim7);
            anim8=(ImageView) itemView.findViewById(R.id.anim8);
            anim9=(ImageView) itemView.findViewById(R.id.anim9);
            anim10=(ImageView) itemView.findViewById(R.id.anim10);

            anim11=(ImageView) itemView.findViewById(R.id.anim11);
            anim12=(ImageView) itemView.findViewById(R.id.anim12);
            anim13=(ImageView) itemView.findViewById(R.id.anim13);
            anim14=(ImageView) itemView.findViewById(R.id.anim14);
            anim15=(ImageView) itemView.findViewById(R.id.anim15);
            anim16=(ImageView) itemView.findViewById(R.id.anim16);
            anim17=(ImageView) itemView.findViewById(R.id.anim17);
            anim18=(ImageView) itemView.findViewById(R.id.anim18);
            anim19=(ImageView) itemView.findViewById(R.id.anim19);
            anim20=(ImageView) itemView.findViewById(R.id.anim20);
        }
    }
}
