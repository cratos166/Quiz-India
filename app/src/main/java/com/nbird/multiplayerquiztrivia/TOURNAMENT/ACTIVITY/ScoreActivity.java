package com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDataAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.ResultAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.BasicDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.JoinCreateTournamentDialoge;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerInfo;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.SERVER.DataSetter;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.Comparator;

public class ScoreActivity extends AppCompatActivity {

    String roomCode;
    int maxQuestions;
    ValueEventListener resultEventListener, numberOfActivePlayerEventListener, hostActiveEventListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ArrayList<DataExchangeHolder> playerDataArrayList;
    int numberOfActivePlayer, myPlayerNum;

    ResultAdapter resultAdapter;
    RecyclerView recyclerView;

    LottieAnimationView party_popper;

    Button reMatch, joinOrCreateOtherRoom, quitButton;

    Boolean winnerDeclared = false, isHostActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        roomCode = getIntent().getStringExtra("roomCode");
        maxQuestions = getIntent().getIntExtra("maxQuestions", 10);
        myPlayerNum = getIntent().getIntExtra("playerNum", 1);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        party_popper = (LottieAnimationView) findViewById(R.id.party_popper);
        reMatch = (Button) findViewById(R.id.reMatch);
        joinOrCreateOtherRoom = (Button) findViewById(R.id.joinOrCreateOtherRoom);
        quitButton = (Button) findViewById(R.id.quitButton);

        playerDataArrayList = new ArrayList<>();

        resultAdapter = new ResultAdapter(ScoreActivity.this, playerDataArrayList, maxQuestions);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(resultAdapter);


        numberOfActivPlayers();
        resultGetter();


        reMatch.setEnabled(false);
        joinOrCreateOtherRoom.setVisibility(View.GONE);


        if (myPlayerNum == 1) {
            table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("active").setValue(3).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        } else {
            hostActiveEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        isHostActive = snapshot.getValue(Boolean.class);


                        if (winnerDeclared) {
                            if (isHostActive) {
                                reMatch.setEnabled(true);
                            } else {
                                reMatch.setEnabled(true);
                                joinOrCreateOtherRoom.setVisibility(View.VISIBLE);
                            }
                        }


                    } catch (Exception e) {

                        isHostActive = false;


                        try {
                            table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").addValueEventListener(hostActiveEventListener);
                        } catch (Exception e1) {
                        }

                        reMatch.setEnabled(true);
                        joinOrCreateOtherRoom.setVisibility(View.VISIBLE);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").addValueEventListener(hostActiveEventListener);
        }


        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                quitScoreActivityActivity();

            }
        });


        reMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (myPlayerNum == 1) {
                    table_user.child("TOURNAMENT").child("RESULT").child(roomCode).removeValue();
                    table_user.child("TOURNAMENT").child("QUESTIONS").child(roomCode).removeValue();
                    table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).removeValue();
                    table_user.child("TOURNAMENT").child("CHAT").child(roomCode).removeValue();

                    roomEnter();

                } else {
                    if (isHostActive) {
                        roomEnter();
                    } else {

                        BasicDialog basicDialog = new BasicDialog(ScoreActivity.this, reMatch, "Room Dissolved", "Your host left the room because of which the room is dissolved. Please join or create some other room.", "OKAY", R.raw.host_removed);
                        basicDialog.start();

                    }
                }


            }
        });


        joinOrCreateOtherRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).removeEventListener(numberOfActivePlayerEventListener);
                } catch (Exception e) {
                }
                try {
                    table_user.child("TOURNAMENT").child("RESULT").child(roomCode).removeEventListener(resultEventListener);
                } catch (Exception e) {
                }
                try {
                    table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostActiveEventListener);
                } catch (Exception e) {
                }


                JoinCreateTournamentDialoge joinCreateTournamentDialoge = new JoinCreateTournamentDialoge();
                joinCreateTournamentDialoge.start(ScoreActivity.this, joinOrCreateOtherRoom);
            }
        });


    }

    private void roomEnter() {
        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("active").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    int data = snapshot.getValue(Integer.class);

                    if (data == 3) {
                        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("active").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                intentFunction();

                            }
                        });
                    } else if (data == 1) {
                        intentFunction();
                    } else {

                        BasicDialog basicDialog = new BasicDialog(ScoreActivity.this, reMatch, "Game Started", "Host started the game. Cannot enter in the middle of the game. Please join or create some other room.", "OKAY", R.raw.host_started_game);
                        basicDialog.start();

                    }

                } catch (Exception e) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void intentFunction() {
        Dialog dialog = null;
        SupportAlertDialog supportAlertDialog = new SupportAlertDialog(dialog, ScoreActivity.this);
        supportAlertDialog.showLoadingDialog();

        try {
            table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).removeEventListener(numberOfActivePlayerEventListener);
        } catch (Exception e) {
        }
        try {
            table_user.child("TOURNAMENT").child("RESULT").child(roomCode).removeEventListener(resultEventListener);
        } catch (Exception e) {
        }


        try {
            table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostActiveEventListener);
        } catch (Exception e) {

        }

        supportAlertDialog.dismissLoadingDialog();

        Intent intent = new Intent(ScoreActivity.this, LobbyActivity.class);
        intent.putExtra("playerNum", myPlayerNum);
        intent.putExtra("roomCode", roomCode);
        startActivity(intent);
        finish();
    }

    private void numberOfActivPlayers() {
        numberOfActivePlayerEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                numberOfActivePlayer = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    PlayerInfo playerInfo = dataSnapshot.getValue(PlayerInfo.class);

                    if (playerInfo.isActive()) {
                        numberOfActivePlayer++;
                    } else {

                        try {
                            table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(String.valueOf(dataSnapshot.getKey())).removeValue();
                        } catch (Exception e) {

                        }

                    }


                }


                try {

                    if (numberOfActivePlayer == playerDataArrayList.size()) {

                        resultComparator();
                        Collections.reverse(playerDataArrayList);
                        winnerDialog(playerDataArrayList.get(0).getMyPicURL(), playerDataArrayList.get(0).getMyNameString());

                        for (int i = 0; i < playerDataArrayList.size(); i++) {
                            playerDataArrayList.get(i).setTotalScoreInt(i + 1);
                        }


                        resultAdapter.notifyDataSetChanged();
                    } else {


                        resultComparator();
                        Collections.reverse(playerDataArrayList);
                        for (int i = 0; i < playerDataArrayList.size(); i++) {
                            playerDataArrayList.get(i).setTotalScoreInt(i + 1);
                        }

                        resultAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).addValueEventListener(numberOfActivePlayerEventListener);

    }


    private void resultGetter() {

        resultEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playerDataArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DataExchangeHolder dataExchangeHolder = dataSnapshot.getValue(DataExchangeHolder.class);

                    playerDataArrayList.add(dataExchangeHolder);


                }

                if (numberOfActivePlayer == playerDataArrayList.size()) {

                    resultComparator();

                    Collections.reverse(playerDataArrayList);
                    winnerDialog(playerDataArrayList.get(0).getMyPicURL(), playerDataArrayList.get(0).getMyNameString());

                    for (int i = 0; i < playerDataArrayList.size(); i++) {
                        playerDataArrayList.get(i).setTotalScoreInt(i + 1);
                    }


                    resultAdapter.notifyDataSetChanged();
                } else {


                    resultComparator();
                    Collections.reverse(playerDataArrayList);
                    for (int i = 0; i < playerDataArrayList.size(); i++) {
                        playerDataArrayList.get(i).setTotalScoreInt(i + 1);
                    }

                    resultAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        table_user.child("TOURNAMENT").child("RESULT").child(roomCode).addValueEventListener(resultEventListener);
    }


    private void resultComparator() {

        Collections.sort(playerDataArrayList, new Comparator<DataExchangeHolder>() {
            @Override
            public int compare(DataExchangeHolder a1, DataExchangeHolder a2) {
                return a1.getScoreInt() - a2.getScoreInt();
            }
        });

    }


    private void winnerDialog(String imageURL, String nameStr) {

        winnerDeclared = true;

        reMatch.setEnabled(true);


        try {
            table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).removeEventListener(numberOfActivePlayerEventListener);
        } catch (Exception e) {
        }
        try {
            table_user.child("TOURNAMENT").child("RESULT").child(roomCode).removeEventListener(resultEventListener);
        } catch (Exception e) {
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(ScoreActivity.this).inflate(R.layout.dialog_tournament_winner, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        ImageView imageIcon = (ImageView) view1.findViewById(R.id.imageIcon);
        TextView name = (TextView) view1.findViewById(R.id.name);
        TextView textTitle = (TextView) view1.findViewById(R.id.textTitle);

        Button okButton = (Button) view1.findViewById(R.id.okButton);


        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        party_popper.setAnimation(R.raw.party_popper);
        party_popper.playAnimation();
        party_popper.loop(false);

        Glide.with(ScoreActivity.this).load(imageURL).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(imageIcon);

        name.setText(nameStr);
        textTitle.setText(nameStr + " won the round.");


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {

                }
            }
        });
    }


    public void quitScoreActivityActivity() {
        AlertDialog.Builder builderRemove = new AlertDialog.Builder(ScoreActivity.this, R.style.AlertDialogTheme);
        View viewRemove1 = LayoutInflater.from(ScoreActivity.this).inflate(R.layout.dialog_model_2, (ConstraintLayout) findViewById(R.id.layoutDialogContainer), false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        Button yesButton = (Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton = (Button) viewRemove1.findViewById(R.id.buttonNo);

        TextView textTitle = (TextView) viewRemove1.findViewById(R.id.textTitle);


        if (myPlayerNum == 1) {
            textTitle.setText("You are the host. If you left the room, the whole room will be dissolved.\n\n You really want to quit ?");
        } else {
            textTitle.setText("You really want to quit ?");
        }


        LottieAnimationView anim = (LottieAnimationView) viewRemove1.findViewById(R.id.imageIcon);
        anim.setAnimation(R.raw.exit_lobby);
        anim.playAnimation();
        anim.loop(true);


        final AlertDialog alertDialog = builderRemove.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = null;
                SupportAlertDialog supportAlertDialog = new SupportAlertDialog(dialog, ScoreActivity.this);
                supportAlertDialog.showLoadingDialog();


                if (myPlayerNum == 1) {
                    table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            supportAlertDialog.dismissLoadingDialog();

                            try {
                                alertDialog.dismiss();
                            } catch (Exception e) {

                            }

                            table_user.child("TOURNAMENT").child("RESULT").child(roomCode).removeValue();
                            table_user.child("TOURNAMENT").child("QUESTIONS").child(roomCode).removeValue();
                            table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).removeValue();
                            table_user.child("TOURNAMENT").child("CHAT").child(roomCode).removeValue();

                            intentMain();

                        }
                    });
                } else {
                    table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            supportAlertDialog.dismissLoadingDialog();

                            try {
                                alertDialog.dismiss();
                            } catch (Exception e) {

                            }

                            intentMain();

                        }
                    });
                }


            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void intentMain() {
        try {
            table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).removeEventListener(numberOfActivePlayerEventListener);
        } catch (Exception e) {
        }
        try {
            table_user.child("TOURNAMENT").child("RESULT").child(roomCode).removeEventListener(resultEventListener);
        } catch (Exception e) {
        }
        try {
            table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostActiveEventListener);
        } catch (Exception e) {
        }


        Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void onBackPressed() {
        quitScoreActivityActivity();
    }


}