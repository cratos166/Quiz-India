package com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BUZZER.BOT.ACTIVITY.BOTLobbyBuzzerActivity;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.ConnectionStatus;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.BOTLobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.BOTRoom;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerInfo;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Room;

import java.util.List;

public class BOTRoomListAdapter extends RecyclerView.Adapter<BOTRoomListAdapter.MyViewHolder>  {

    private Context mContext;
    private List<BOTRoom> mData;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    int gate;
    public BOTRoomListAdapter(Context mContext, List<BOTRoom> mData,int gate){
        this.mContext=mContext;
        this.mData=mData;
        this.gate=gate;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.tournament_joining_listview,parent,false);

        return new MyViewHolder(view);

    }



    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getHostName());
        Glide.with(mContext).load(mData.get(position).getHostImageURL()).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(holder.proImage);

        if(gate==2){
            holder.numberOfPlayer.setText(mData.get(position).getNumberOfPlayers()+"/"+ AppString.BUZZER_MAX_PLAYERS);
        }else{
            holder.numberOfPlayer.setText(mData.get(position).getNumberOfPlayers()+"/"+ AppString.TOURNAMENT_MAX_PLAYERS);
        }



        if (mData.get(position).getNumberOfQuestions()==1){
            holder.questionnumber.setText(" Q : 10");
        }else if(mData.get(position).getNumberOfQuestions()==2){
            holder.questionnumber.setText(" Q : 15");
        }else{
            holder.questionnumber.setText(" Q : 20");
        }


        if (mData.get(position).getTime()==1){
            holder.time.setText(" |  T : 3 Mins ");
        }else if(mData.get(position).getTime()==2){
            holder.time.setText(" |  T : 4.5 Mins ");
        }else{
            holder.time.setText(" |  T : 6 Mins ");
        }


        int manu;
        manu=mData.get(position).getGameMode();
        if(manu==1){
            holder.modequestion.setText(" |  M : Normal");
        }else if(manu==2){
            holder.modequestion.setText(" |  M : Picture");
        }else if(manu==3){
            holder.modequestion.setText(" |  M : Audio");
        }else{
            holder.modequestion.setText(" |  M : Video");
        }


        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(gate==2){
                    intent=new Intent(mContext, BOTLobbyBuzzerActivity.class);
                }else{
                    intent=new Intent(mContext, BOTLobbyActivity.class);
                }


                intent.putExtra("playerNum",2);
                intent.putExtra("hostName",mData.get(position).getHostName());
                intent.putExtra("numberOfPlayers",mData.get(position).getNumberOfPlayers());
                intent.putExtra("hostImage",mData.get(position).getHostImageURL());
                intent.putExtra("numberOfQuestions",mData.get(position).getNumberOfQuestions());
                intent.putExtra("time",mData.get(position).getTime());
                intent.putExtra("gameMode",mData.get(position).getGameMode());
                mContext.startActivity(intent);
                ((Activity) mContext).finish();




            }
        });

    }





    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,numberOfPlayer,time,questionnumber,modequestion;
        ImageView proImage;
        Button joinButton;



        public MyViewHolder(View itemView){
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.username);
            numberOfPlayer=(TextView) itemView.findViewById(R.id.num);
            time=(TextView) itemView.findViewById(R.id.time);

            questionnumber=(TextView) itemView.findViewById(R.id.questionnumber);
            modequestion=(TextView) itemView.findViewById(R.id.modequestion);
            proImage=(ImageView) itemView.findViewById(R.id.proImage);

            joinButton=(Button) itemView.findViewById(R.id.joinButton);






        }


    }

}
