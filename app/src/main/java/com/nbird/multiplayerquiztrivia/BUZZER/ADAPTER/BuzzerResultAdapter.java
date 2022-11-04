package com.nbird.multiplayerquiztrivia.BUZZER.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.BuzzerDataExchangeHolder;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;

public class BuzzerResultAdapter extends RecyclerView.Adapter<BuzzerResultAdapter.MyViewHolder> {
    private Context mContext;
    ArrayList<BuzzerDataExchangeHolder> dataExchangeHolderArrayList;
    int maxQuestion;

    public BuzzerResultAdapter(Context mContext, ArrayList<BuzzerDataExchangeHolder> chatListArray, int maxQuestion){
        this.mContext=mContext;
        this.dataExchangeHolderArrayList =chatListArray;
        this.maxQuestion=maxQuestion;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.buzzer_result_item,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);


        if(dataExchangeHolderArrayList.get(position).getPosition()==1){
            holder.rank.setText(String.valueOf(dataExchangeHolderArrayList.get(position).getPosition())+"st");
        }else if(dataExchangeHolderArrayList.get(position).getPosition()==2){
            holder.rank.setText(String.valueOf(dataExchangeHolderArrayList.get(position).getPosition())+"nd");
        }else if(dataExchangeHolderArrayList.get(position).getPosition()==3){
            holder.rank.setText(String.valueOf(dataExchangeHolderArrayList.get(position).getPosition())+"rd");
        }else{
            holder.rank.setText(String.valueOf(dataExchangeHolderArrayList.get(position).getPosition())+"th");
        }


        holder.myName.setText(dataExchangeHolderArrayList.get(position).getMyNameString());
        holder.correctAnswer.setText("Correct Answer : "+ dataExchangeHolderArrayList.get(position).getCorrectAns());
        holder.totalScore.setText("Total Score : "+ dataExchangeHolderArrayList.get(position).getTotalScore());
        float acc=(dataExchangeHolderArrayList.get(position).getCorrectAns()*100)/maxQuestion;
        holder.accuracy.setText("Accuracy : "+acc+"%");

        Glide.with(mContext).load(dataExchangeHolderArrayList.get(position).getMyPicURL()).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(holder.myPic);



        ArrayList<LottieAnimationView> list=new ArrayList<>();



        list.add(holder.anim11);list.add(holder.anim12);list.add(holder.anim13);list.add(holder.anim14);list.add(holder.anim15);
        list.add(holder.anim16);list.add(holder.anim17);list.add(holder.anim18);list.add(holder.anim19);list.add(holder.anim20);
        list.add(holder.anim1);list.add(holder.anim2);list.add(holder.anim3);list.add(holder.anim4);list.add(holder.anim5);
        list.add(holder.anim6);list.add(holder.anim7);list.add(holder.anim8);list.add(holder.anim9);list.add(holder.anim10);


        for(int i = 0; i< dataExchangeHolderArrayList.get(position).getAnimList().size(); i++){
            try{
                if(dataExchangeHolderArrayList.get(position).getAnimList().get(i)==1){
                    list.get(i).setAnimation(R.raw.tickanim);
                    list.get(i).playAnimation();
                    list.get(i).loop(false);
                }else if(dataExchangeHolderArrayList.get(position).getAnimList().get(i)==2){
                    list.get(i).setAnimation(R.raw.wronganim);
                    list.get(i).playAnimation();
                    list.get(i).loop(false);
                }else{
                    list.get(i).setAnimation(R.raw.not_ans_anim);
                    list.get(i).playAnimation();
                    list.get(i).setScaleX(0.75f);
                    list.get(i).setScaleY(0.75f);
                    list.get(i).loop(false);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }






    }


    @Override
    public int getItemCount() {
        return dataExchangeHolderArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myName,correctAnswer,totalScore,rank,accuracy;
        ImageView myPic;

        LottieAnimationView anim1,anim2,anim3,anim4,anim5,anim6,anim7,anim8,anim9,anim10,anim11,anim12,anim13,anim14,anim15,anim16,anim17,anim18,anim19,anim20;

        public MyViewHolder(View itemView){
            super(itemView);

            myPic=(ImageView) itemView.findViewById(R.id.myPic);


            myName=(TextView) itemView.findViewById(R.id.myName);
            correctAnswer=(TextView) itemView.findViewById(R.id.correctAnswer);
            rank=(TextView) itemView.findViewById(R.id.rank);
            accuracy=(TextView) itemView.findViewById(R.id.accuracy);



            totalScore=(TextView) itemView.findViewById(R.id.totalScore);


            anim11=(LottieAnimationView) itemView.findViewById(R.id.anim1);
            anim12=(LottieAnimationView) itemView.findViewById(R.id.anim2);
            anim13=(LottieAnimationView) itemView.findViewById(R.id.anim3);
            anim14=(LottieAnimationView) itemView.findViewById(R.id.anim4);
            anim15=(LottieAnimationView) itemView.findViewById(R.id.anim5);
            anim16=(LottieAnimationView) itemView.findViewById(R.id.anim6);
            anim17=(LottieAnimationView) itemView.findViewById(R.id.anim7);
            anim18=(LottieAnimationView) itemView.findViewById(R.id.anim8);
            anim19=(LottieAnimationView) itemView.findViewById(R.id.anim9);
            anim20=(LottieAnimationView) itemView.findViewById(R.id.anim10);

             anim1=(LottieAnimationView) itemView.findViewById(R.id.anim11);
             anim2=(LottieAnimationView) itemView.findViewById(R.id.anim12);
             anim3=(LottieAnimationView) itemView.findViewById(R.id.anim13);
             anim4=(LottieAnimationView) itemView.findViewById(R.id.anim14);
             anim5=(LottieAnimationView) itemView.findViewById(R.id.anim15);
             anim6=(LottieAnimationView) itemView.findViewById(R.id.anim16);
             anim7=(LottieAnimationView) itemView.findViewById(R.id.anim17);
             anim8=(LottieAnimationView) itemView.findViewById(R.id.anim18);
             anim9=(LottieAnimationView) itemView.findViewById(R.id.anim19);
             anim10=(LottieAnimationView) itemView.findViewById(R.id.anim20);






        }
    }
}
