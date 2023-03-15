package com.nbird.multiplayerquiztrivia.BUZZER.BOT.DIALOG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BUZZER.BOT.ACTIVITY.BOTLobbyBuzzerActivity;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.BOTLobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.BOTRoomListAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.BOT.RoomAdder;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.BOTRoom;

import java.util.ArrayList;

public class BOTBuzzerJoinCreateTournamentDialog {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    ShimmerFrameLayout shimmer;
    AppData appData;
    RecyclerView recyclerView;
    ArrayList<BOTRoom> list;
    BOTRoomListAdapter categoryAdapter;
    TextView recyclerText;

    int roomCodeInt;

    public void start(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_tournament_join_create, (ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);


        Button createButton = (Button) view1.findViewById(R.id.createButton);
        Button joinButton = (Button) view1.findViewById(R.id.joinPrivateRoom);
        Button cancelButton=(Button) view1.findViewById(R.id.cancelButton);
        CardView refresh=(CardView) view1.findViewById(R.id.refresh);
        recyclerText=(TextView) view1.findViewById(R.id.recyclerText);

        shimmer=view1.findViewById(R.id.shimmer);
        recyclerView=view1.findViewById(R.id.recyclerView);

        shimmer.startShimmerAnimation();

        appData=new AppData();


        list=new ArrayList<>();

        NativeAd NATIVE_ADS=null;
        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)) {
            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        NativeAd N_D=NATIVE_ADS;
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);
                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = view1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                            N_D=nativeAd;
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter = new BOTRoomListAdapter(context,list,2);
        recyclerView.setAdapter(categoryAdapter);


        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        displayDataOnRecyclerView(context);



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, BOTLobbyBuzzerActivity.class);
                intent.putExtra("playerNum",1);
                intent.putExtra("hostName",appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context));
                intent.putExtra("numberOfPlayers",1);
                intent.putExtra("hostImage",appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context));
                intent.putExtra("numberOfQuestions",1);
                intent.putExtra("time",1);
                intent.putExtra("gameMode",1);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDataOnRecyclerView(context);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}

                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }
            }
        });


       joinButton.setVisibility(View.GONE);

//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createRoom(context,alertDialog,NATIVE_ADS);
//            }
//        });

    }


    private void displayDataOnRecyclerView(Context context){
        Dialog dialog=null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,context);
        supportAlertDialog.showLoadingDialog();




                list.clear();

               RoomAdder roomAdder=new RoomAdder();
               roomAdder.start(list,2);

                recyclerText.setVisibility(View.GONE);

                supportAlertDialog.dismissLoadingDialog();


                categoryAdapter.notifyDataSetChanged();

                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.GONE);

                if(list.size()==0){
                    recyclerText.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "No Rooms Available! Please Create one.", Toast.LENGTH_SHORT).show();
                }

    }

}
