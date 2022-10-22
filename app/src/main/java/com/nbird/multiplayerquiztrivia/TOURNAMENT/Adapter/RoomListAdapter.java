package com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.ConnectionStatus;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerInfo;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Room;

import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Room> mData;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public RoomListAdapter(Context mContext, List<Room> mData){
        this.mContext=mContext;
        this.mData=mData;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.tournament_joining_listview,parent,false);

        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.name.setText(mData.get(position).getHostName());

        Glide.with(mContext).load(mData.get(position).getHostImageURL()).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(holder.proImage);

        holder.numberOfPlayer.setText(mData.get(position).getNumberOfPlayers()+"/4");




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
            holder.modequestion.setText(" |  M : Buzzer Normal");
        }else{
            holder.modequestion.setText(" |  M : Buzzer Picture");
        }


        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String roomCode1=mData.get(position).getRoomCode();


                Dialog dialog=null;
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,mContext);
                supportAlertDialog.showLoadingDialog();


                table_user.child("TOURNAMENT").child("ROOM").child(roomCode1).child("active").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if(snapshot.getValue(Integer.class)==1){


                                table_user.child("TOURNAMENT").child("ROOM").child(roomCode1).child("numberOfPlayers").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try{
                                            int player=snapshot.getValue(Integer.class);
                                            if(player<AppString.TOURNAMENT_MAX_PLAYERS){


                                                joiner(position,supportAlertDialog);



                                            }
                                        }catch (Exception e){
                                            Toast.makeText(mContext, "They Have Started Playing!!!Please Refresh And Try Some Other Room", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });





                            }else{
                                Toast.makeText(mContext, "They Have Started Playing!!!Please Refresh And Try Some Other Room", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(mContext, "They Have Started Playing!!!Please Refresh And Try Some Other Room", Toast.LENGTH_SHORT).show();
                            // joiner(holder,position);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });

    }


    private void joiner(int position, SupportAlertDialog supportAlertDialog){


        ConnectionStatus connectionStatus=new ConnectionStatus();
      //  connectionStatus.myStatusSetter();

        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{
                    LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                    PlayerInfo playerInfo = new PlayerInfo(leaderBoardHolder.getUsername(),leaderBoardHolder.getScore(),leaderBoardHolder.getTotalTime(),leaderBoardHolder.getCorrect(),leaderBoardHolder.getWrong(),leaderBoardHolder.getImageUrl(),leaderBoardHolder.getSumationScore(),true);


                    table_user.child("TOURNAMENT").child("PLAYERS").child(mData.get(position).getRoomCode()).child(mAuth.getCurrentUser().getUid()).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            try{
                                supportAlertDialog.dismissLoadingDialog();
                            }catch (Exception e){

                            }


                            connectionStatus.tournamentStatusSetter(mData.get(position).getRoomCode());


                            Intent intent=new Intent(mContext, LobbyActivity.class);
                            intent.putExtra("playerNum",2);
                            intent.putExtra("roomCode",mData.get(position).getRoomCode());
                            intent.putExtra("hostName",mData.get(position).getHostName());
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        }
                    });

                }catch (Exception e){

                    AppData appData=new AppData();

                    String name=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, mContext);
                    String imageURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,mContext);
                    LeaderBoardHolder leaderBoardHolder=new LeaderBoardHolder(name,0,0,0,0,imageURL,0);


                    PlayerInfo playerInfo = new PlayerInfo(leaderBoardHolder.getUsername(),leaderBoardHolder.getScore(),leaderBoardHolder.getTotalTime(),leaderBoardHolder.getCorrect(),leaderBoardHolder.getWrong(),leaderBoardHolder.getImageUrl(),leaderBoardHolder.getSumationScore(),true);


                    table_user.child("TOURNAMENT").child("PLAYERS").child(mData.get(position).getRoomCode()).child(mAuth.getCurrentUser().getUid()).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            try{
                                supportAlertDialog.dismissLoadingDialog();
                            }catch (Exception e){

                            }


                            connectionStatus.tournamentStatusSetter(mData.get(position).getRoomCode());

                            Intent intent=new Intent(mContext, LobbyActivity.class);
                            intent.putExtra("playerNum",2);
                            intent.putExtra("roomCode",mData.get(position).getRoomCode());
                            intent.putExtra("hostName",mData.get(position).getHostName());
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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