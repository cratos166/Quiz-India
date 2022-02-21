package com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;
import com.nbird.multiplayerquiztrivia.R;

import java.util.List;

public class RecyclerViewLeaderBoardAdapter extends RecyclerView.Adapter<RecyclerViewLeaderBoardAdapter.MyViewHolder> {
    private List<LeaderBoardHolder> mData;

    public RecyclerViewLeaderBoardAdapter(List<LeaderBoardHolder> mData){
        this.mData=mData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_listview,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.setData(mData.get(position).getUsername()
                ,mData.get(position).getScore()
                ,mData.get(position).getTotalTime()
                ,mData.get(position).getCorrect()
                ,mData.get(position).getWrong()
                ,mData.get(position).getImageUrl()
        ,mData.get(position).getSumationScore());


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView score;
        TextView totalTime;
        TextView correctByWrong;
        ImageView imageUrl1;
        ImageView levelImageView;
        TextView levelTextView;

        public MyViewHolder(View itemView){
            super(itemView);

            username=(TextView) itemView.findViewById(R.id.username);
            score=(TextView) itemView.findViewById(R.id.score);
            totalTime=(TextView) itemView.findViewById(R.id.totalTime);
            correctByWrong=(TextView) itemView.findViewById(R.id.totalCorrectAnswer);
            imageUrl1=(ImageView) itemView.findViewById(R.id.pic);
            levelImageView=(ImageView) itemView.findViewById(R.id.batch);
            levelTextView=(TextView) itemView.findViewById(R.id.levelText123);
        }

        public void setData(String username, int score, int totalTime , int correct, int wrong, String imageUrl, int sumationScore) {

            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(imageUrl1);

            this.username.setText(username+" ");
            this.score.setText(String.valueOf("HS : "+score+" "));



            if(totalTime<60){
                this.totalTime.setText(String.valueOf("Total Time : "+totalTime+" sec "));
            }else{
                int minutes=totalTime/60;
                int sec=totalTime%60;
                this.totalTime.setText(String.valueOf("Total Time : "+minutes+" min "+sec+" sec "));
            }


            BatchGenerator batchGenerator=new BatchGenerator(sumationScore,levelImageView);
            batchGenerator.start();

            LevelGenerators levelGenerators=new LevelGenerators(sumationScore,levelTextView);
            levelGenerators.start();

            this.correctByWrong.setText("Correct/Wrong : "+correct+"/"+wrong+" ");
        }



    }


}
