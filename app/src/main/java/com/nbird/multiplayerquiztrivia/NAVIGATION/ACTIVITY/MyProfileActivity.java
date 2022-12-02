package com.nbird.multiplayerquiztrivia.NAVIGATION.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.BarGroupHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.NAVIGATION.DIALOG.ChangeNameAndImageDialog;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    CardView changeNameCardView;
    ImageView profileImage;
    TextView name;

    String usernameStr;
    String imageStr;
    AppData appData;

    TextView correctVsWrong,totalTimeTaken,totalQuizPlayed,accuracy;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("NEW_APP");
    DatabaseReference myRef = database.getReference();

    SupportAlertDialog supportAlertDialog;

    int lu=0,mu=0;
    CountDownTimer countDownTimer,countDownTimer1;
    ArrayList correctBar,wrongBar;
    BarDataSet barDataSet1,barDataSet2;
    BarChart barChart;
    String[] days = new String[]{"Sunday", "Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};
    int v=0,x=0;
    ArrayList<PieEntry> visitors1;
    PieChart pieChart;
    LineData lineData;
    List<Entry> entryList = new ArrayList<>();
    LineChart lineChart;
    AdView mAdView;




    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10;
    CardView cardView11, cardView12, cardView13, cardView14, cardView15, cardView16, cardView17, cardView18, cardView19, cardView20;
    CardView cardView21, cardView22, cardView23, cardView24, cardView25, cardView26;

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7, linearLayout8, linearLayout9, linearLayout10;
    LinearLayout linearLayout11, linearLayout12, linearLayout13, linearLayout14, linearLayout15, linearLayout16, linearLayout17, linearLayout18, linearLayout19, linearLayout20;
    LinearLayout linearLayout21, linearLayout22, linearLayout23, linearLayout24, linearLayout25, linearLayout26;

    ImageView nav_image;

    TextInputEditText usernameEditText;
    Uri imageUri;

    String urlAva1 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava1.png?alt=media&token=39fc4486-0021-443f-974d-daa3fc17bec2";
    String urlAva2 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava2.png?alt=media&token=e19cd95b-6012-4fe7-94bb-003c1b9f92c0";
    String urlAva3 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava3.png?alt=media&token=d0a90643-c6f3-446e-a386-8de3dc052765";
    String urlAva4 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava4.png?alt=media&token=ff5f2d52-eb31-4d55-af59-2de2ecda6a51";
    String urlAva5 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava5.png?alt=media&token=6a354ce9-d45a-4bfa-8c26-6d1db00ceb84";
    String urlAva6 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava6.png?alt=media&token=445f480e-c646-45b1-b55c-8ff86469e97f";
    String urlAva7 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava7.png?alt=media&token=41f409b7-6c19-4827-9723-fbd8856215f2";
    String urlAva8 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava8.png?alt=media&token=4a3682cc-3242-4582-b3c3-fff6591b7af5";
    String urlAva9 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava9.png?alt=media&token=f9048bb1-fd25-48fe-95ed-65f70f0607b3";
    String urlAva10 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava10.png?alt=media&token=881f92b8-973a-462a-8a3c-8c6f70cd888b";
    String urlAva11 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava11.png?alt=media&token=26a5711e-2a8c-449e-bc3a-4a2d98811cc1";
    String urlAva12 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava12.png?alt=media&token=e09e3572-c79d-4bb3-9756-29f18967f0db";
    String urlAva13 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava13.png?alt=media&token=82a5dece-0e9a-4e2c-824a-1c4b441073bc";
    String urlAva14 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava14.png?alt=media&token=bb2e7b65-2709-432b-add4-cac32bf3cd6d";
    String urlAva15 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava15.png?alt=media&token=fea2cbe9-2359-4577-a113-562e54341031";
    String urlAva16 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava16.png?alt=media&token=f31d2368-92f4-4e82-a384-aaabf11c6dc3";
    String urlAva17 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava17.png?alt=media&token=4d7eacfd-d75e-432d-bf6d-bc95acdb3ebf";
    String urlAva18 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava18.png?alt=media&token=2e1493eb-905a-47cc-b86c-0540d5a7561c";
    String urlAva19 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava19.png?alt=media&token=faa84af3-2195-46b7-a44f-d6b272698443";
    String urlAva20 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava20.png?alt=media&token=b7aee3f8-a774-4ceb-9be9-4bd4dba39b04";
    String urlAva21 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava21.png?alt=media&token=d32fccfd-57c3-4f07-99c6-0da9cc921d5e";
    String urlAva22 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava22.png?alt=media&token=37793d6e-55bf-4be6-9c3e-b49f69cb25f8";
    String urlAva23 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava23.png?alt=media&token=c4fe300d-b6cb-4102-b956-082c89ed6bcb";
    String urlAva24 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava24.png?alt=media&token=35520f68-1000-4c84-b8ed-56b2bf64b14f";
    String urlAva25 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava25.png?alt=media&token=c2eec723-3f40-4dc6-a294-caa4db20cf66";
    String urlAva26 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava26.png?alt=media&token=2cd731f2-e525-49b3-ac81-dc939cbd82fe";

    FirebaseStorage storage;
    StorageReference storageReference;

    String mailid123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);




        Dialog dialog=null;
        supportAlertDialog=new SupportAlertDialog(dialog,MyProfileActivity.this);
        supportAlertDialog.showLoadingDialog();

        changeNameCardView=(CardView) findViewById(R.id.changeNameCardView);
        profileImage=(ImageView) findViewById(R.id.profileImage);
        name=(TextView) findViewById(R.id.name);

        correctVsWrong=(TextView) findViewById(R.id.correctVsWrong);
        totalTimeTaken=(TextView) findViewById(R.id.totalTimeTaken);
        totalQuizPlayed=(TextView) findViewById(R.id.totalQuizPlayed);
        accuracy=(TextView) findViewById(R.id.accuracy);
        barChart = findViewById(R.id.idBarChart);

        pieChart=(PieChart) findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);
        mAuth = FirebaseAuth.getInstance();

        correctBar=new ArrayList();
        wrongBar=new ArrayList();
        visitors1=new ArrayList<>();

        appData = new AppData();

        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, MyProfileActivity.this)){
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        usernameStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, MyProfileActivity.this);
        imageStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MyProfileActivity.this);

        name.setText(usernameStr);
        Glide.with(MyProfileActivity.this).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MyProfileActivity.this)).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(profileImage);


        changeNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ChangeNameAndImageDialog changeNameAndImageDialog=new ChangeNameAndImageDialog(imageStr,usernameStr,MyProfileActivity.this,changeNameCardView,name,profileImage);
//                changeNameAndImageDialog.start();

                setChangeNameDialog();

            }
        });
        textDataSetter();
        groupBarData();
//        barChart();
        pieCaller();
        linearChart();
    }

    public void setChangeNameDialog(){
        mAuth = FirebaseAuth.getInstance();


        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(MyProfileActivity.this).inflate(R.layout.dialog_profile_selection, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);
        ((TextView) view1.findViewById(R.id.textTitle)).setText(" Enter Username ");
        ((Button) view1.findViewById(R.id.buttonYes)).setText("OK");
        ((TextView) view1.findViewById(R.id.textView6)).setText("Select An Avatar Or Upload Your Pic ");
        nav_image = ((ImageView) view1.findViewById(R.id.propic));
        usernameEditText = ((TextInputEditText) view1.findViewById(R.id.username));

        cardView1 = (CardView) view1.findViewById(R.id.cardview1);
        cardView2 = (CardView) view1.findViewById(R.id.cardview2);
        cardView3 = (CardView) view1.findViewById(R.id.cardview3);
        cardView4 = (CardView) view1.findViewById(R.id.cardview4);
        cardView5 = (CardView) view1.findViewById(R.id.cardview5);
        cardView6 = (CardView) view1.findViewById(R.id.cardview6);
        cardView7 = (CardView) view1.findViewById(R.id.cardview7);
        cardView8 = (CardView) view1.findViewById(R.id.cardview8);
        cardView9 = (CardView) view1.findViewById(R.id.cardview9);
        cardView10 = (CardView) view1.findViewById(R.id.cardview10);
        cardView11 = (CardView) view1.findViewById(R.id.cardview11);
        cardView12 = (CardView) view1.findViewById(R.id.cardview12);
        cardView13 = (CardView) view1.findViewById(R.id.cardview13);
        cardView14 = (CardView) view1.findViewById(R.id.cardview14);
        cardView15 = (CardView) view1.findViewById(R.id.cardview15);
        cardView16 = (CardView) view1.findViewById(R.id.cardview16);
        cardView17 = (CardView) view1.findViewById(R.id.cardview17);
        cardView18 = (CardView) view1.findViewById(R.id.cardview18);
        cardView19 = (CardView) view1.findViewById(R.id.cardview19);
        cardView20 = (CardView) view1.findViewById(R.id.cardview20);
        cardView21 = (CardView) view1.findViewById(R.id.cardview21);
        cardView22 = (CardView) view1.findViewById(R.id.cardview22);
        cardView23 = (CardView) view1.findViewById(R.id.cardview23);
        cardView24 = (CardView) view1.findViewById(R.id.cardview24);
        cardView25 = (CardView) view1.findViewById(R.id.cardview25);
        cardView26 = (CardView) view1.findViewById(R.id.cardview26);


        linearLayout1 = (LinearLayout) view1.findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) view1.findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) view1.findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) view1.findViewById(R.id.linearLayout4);
        linearLayout5 = (LinearLayout) view1.findViewById(R.id.linearLayout5);
        linearLayout6 = (LinearLayout) view1.findViewById(R.id.linearLayout6);
        linearLayout7 = (LinearLayout) view1.findViewById(R.id.linearLayout7);
        linearLayout8 = (LinearLayout) view1.findViewById(R.id.linearLayout8);
        linearLayout9 = (LinearLayout) view1.findViewById(R.id.linearLayout9);
        linearLayout10 = (LinearLayout) view1.findViewById(R.id.linearLayout10);
        linearLayout11 = (LinearLayout) view1.findViewById(R.id.linearLayout11);
        linearLayout12 = (LinearLayout) view1.findViewById(R.id.linearLayout12);
        linearLayout13 = (LinearLayout) view1.findViewById(R.id.linearLayout13);
        linearLayout14 = (LinearLayout) view1.findViewById(R.id.linearLayout14);
        linearLayout15 = (LinearLayout) view1.findViewById(R.id.linearLayout15);
        linearLayout16 = (LinearLayout) view1.findViewById(R.id.linearLayout16);
        linearLayout17 = (LinearLayout) view1.findViewById(R.id.linearLayout17);
        linearLayout18 = (LinearLayout) view1.findViewById(R.id.linearLayout18);
        linearLayout19 = (LinearLayout) view1.findViewById(R.id.linearLayout19);
        linearLayout20 = (LinearLayout) view1.findViewById(R.id.linearLayout20);
        linearLayout21 = (LinearLayout) view1.findViewById(R.id.linearLayout21);
        linearLayout22 = (LinearLayout) view1.findViewById(R.id.linearLayout22);
        linearLayout23 = (LinearLayout) view1.findViewById(R.id.linearLayout23);
        linearLayout24 = (LinearLayout) view1.findViewById(R.id.linearLayout24);
        linearLayout25 = (LinearLayout) view1.findViewById(R.id.linearLayout25);
        linearLayout26 = (LinearLayout) view1.findViewById(R.id.linearLayout26);


        avaMunupulator(cardView1, urlAva1, linearLayout1);
        avaMunupulator(cardView2, urlAva2, linearLayout2);
        avaMunupulator(cardView3, urlAva3, linearLayout3);
        avaMunupulator(cardView4, urlAva4, linearLayout4);
        avaMunupulator(cardView5, urlAva5, linearLayout5);
        avaMunupulator(cardView6, urlAva6, linearLayout6);
        avaMunupulator(cardView7, urlAva7, linearLayout7);
        avaMunupulator(cardView10, urlAva10, linearLayout10);
        avaMunupulator(cardView8, urlAva8, linearLayout8);
        avaMunupulator(cardView9, urlAva9, linearLayout9);
        avaMunupulator(cardView11, urlAva11, linearLayout11);
        avaMunupulator(cardView12, urlAva12, linearLayout12);
        avaMunupulator(cardView13, urlAva13, linearLayout13);
        avaMunupulator(cardView14, urlAva14, linearLayout14);
        avaMunupulator(cardView15, urlAva15, linearLayout15);
        avaMunupulator(cardView16, urlAva16, linearLayout16);
        avaMunupulator(cardView17, urlAva17, linearLayout17);
        avaMunupulator(cardView18, urlAva18, linearLayout18);
        avaMunupulator(cardView19, urlAva19, linearLayout19);
        avaMunupulator(cardView20, urlAva20, linearLayout20);
        avaMunupulator(cardView21, urlAva21, linearLayout21);
        avaMunupulator(cardView22, urlAva22, linearLayout22);
        avaMunupulator(cardView23, urlAva23, linearLayout23);
        avaMunupulator(cardView24, urlAva24, linearLayout24);
        avaMunupulator(cardView25, urlAva25, linearLayout25);
        avaMunupulator(cardView26, urlAva26, linearLayout26);


        appData = new AppData();
        mailid123 = appData.getSharedPreferencesString(AppString.SP_MAIN, AppString.SP_MY_MAIL, MyProfileActivity.this);
        nav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        usernameEditText.setText(usernameStr);


        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }


        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:


                            if (username()) {
                                String usernameEntered = usernameEditText.getText().toString();

                                Dialog dialog=null;
                                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MyProfileActivity.this);
                                supportAlertDialog.showLoadingDialog();


                                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("userName").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        myRef.child("NEW_APP").child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).child("username").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                try{supportAlertDialog.dismissLoadingDialog();}catch (Exception e){}
                                                Toast.makeText(MyProfileActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MyProfileActivity.this,usernameEntered);
                                                name.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MyProfileActivity.this));
                                            }
                                        });

                                    }
                                });
                                try {
                                    alertDialog.dismiss();
                                } catch (Exception e) {
                                }
                            }
                    }
                }
                return false;
            }
        });

        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (username()) {
                    String usernameEntered = usernameEditText.getText().toString();

                    Dialog dialog=null;
                    SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MyProfileActivity.this);
                    supportAlertDialog.showLoadingDialog();

                    myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("userName").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            myRef.child("NEW_APP").child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).child("username").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    try{supportAlertDialog.dismissLoadingDialog();}catch (Exception e){}
                                    Toast.makeText(MyProfileActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                    appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MyProfileActivity.this,usernameEntered);
                                    name.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MyProfileActivity.this));
                                }
                            });



                        }
                    });
                    try {
                        alertDialog.dismiss();
                    } catch (Exception e) {
                    }
                }

            }
        });
    }


    public void avaMunupulator(CardView cardView, String urlAva, LinearLayout linearLayout) {

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=null;
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MyProfileActivity.this);
                supportAlertDialog.showLoadingDialog();
                settingBlack();
                linearLayout.setBackgroundResource(R.drawable.double_color_2);
                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("imageURL").setValue(urlAva).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myRef.child("NEW_APP").child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).child("imageUrl").setValue(urlAva).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    try{supportAlertDialog.dismissLoadingDialog();}catch (Exception e){}
                                    appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MyProfileActivity.this,urlAva);
                                    profileImage.setImageResource(0);
                                    Glide.with(MyProfileActivity.this).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MyProfileActivity.this)).apply(RequestOptions
                                                    .bitmapTransform(new RoundedCorners(18)))
                                            .into(profileImage);
                                }
                            }
                        });

                    }
                });
            }
        });


    }

    private boolean username() {
        String name1 = usernameEditText.getText().toString();
        //String noWhihteSpaces=("\\A\\w{4,20}\\z");
        if (name1.isEmpty()) {
            usernameEditText.setError("Field cannot be empty");
            return false;
        } else if (name1.length() > 10) {
            usernameEditText.setError("Username should be less than 10 characters");
            return false;
        } else
            usernameEditText.setError(null);
        return true;
    }


    public void settingBlack() {
        linearLayout1.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout2.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout3.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout4.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout5.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout6.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout7.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout8.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout9.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout10.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout11.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout12.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout13.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout14.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout15.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout16.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout17.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout18.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout19.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout20.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout21.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout22.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout23.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout24.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout25.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout26.setBackgroundResource(R.drawable.border_theme_2);
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

               /* if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } */
                if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((Activity) MyProfileActivity.this).startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
              /*  case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        nav_image.setImageBitmap(selectedImage);
                        uploadImage();
                    }
                    break;*/
                case 1:
                    if (resultCode == RESULT_OK) {
                        try {
                            imageUri = data.getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            try{
                                nav_image.setImageBitmap(selectedImage);
                            }catch (Exception e){

                            }


                            uploadImage();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(MyProfileActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private void uploadImage() {


        if (imageUri != null) {

            try {
                settingBlack();
            } catch (Exception e) {

            }



            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(MyProfileActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();


            storageReference = storageReference.child("NEW_APP/images/" + mailid123);
            Dialog dialog=null;
            SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MyProfileActivity.this);
            supportAlertDialog.showLoadingDialog();

            // adding listeners on upload
            // or failure of image
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(MyProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    try {

                                        StorageReference urlref = storageReference;
                                        urlref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri downloadUrl) {
                                                String imageurl = downloadUrl.toString();

                                                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("imageURL").setValue(imageurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        myRef.child("NEW_APP").child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).child("imageUrl").setValue(imageurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    try{supportAlertDialog.dismissLoadingDialog();}catch (Exception e){}
                                                                    appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MyProfileActivity.this,imageurl);
                                                                    profileImage.setImageResource(0);
                                                                    Glide.with(MyProfileActivity.this).load(imageurl).apply(RequestOptions
                                                                                    .bitmapTransform(new RoundedCorners(18)))
                                                                            .into(profileImage);

                                                                }
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        });

                                    } catch (Exception e) {

                                    }
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(MyProfileActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }


    public void linearChart(){

        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("LineGraph").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    int historyData = dataSnapshot1.getValue(Integer.class);

                    entryList.add(new Entry(x, historyData));
                    x++;

                }




                LineDataSet lineDataSet = new LineDataSet(entryList,"Quiz Score");
                lineDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                lineDataSet.setFillAlpha(110);

                lineDataSet.setValueTextColor(Color.parseColor("#98A8D0"));
                lineData = new LineData(lineDataSet);
                lineChart.getDescription().setEnabled(false);
                lineChart.setData(lineData);

                lineChart.setVisibleXRangeMaximum(10);
                lineChart.moveViewToX(lineChart.getXChartMax());
                lineChart.invalidate();

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setTextColor(Color.parseColor("#98A8D0"));

                YAxis leftAxis = lineChart.getAxisLeft();
                YAxis rightAxis = lineChart.getAxisRight();

                leftAxis.setTextColor(Color.parseColor("#98A8D0"));
                rightAxis.setTextColor(Color.parseColor("#98A8D0"));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pieCaller(){
        pieDataManu(1,"Historical Monuments");
        pieDataManu(2,"Flora And Fauna");
        pieDataManu(3,"Geography");
        pieDataManu(4,"Astronomy And Space");
        pieDataManu(5,"Sports");
        pieDataManu(6,"Some Superlatives");
        pieDataManu(7,"Country,Capitals And Currencies");
        pieDataManu(8,"Famous Personalities");
        pieDataManu(9,"Science");
        pieDataManu(10,"Important Dates And Events");
        pieDataManu(11,"Religion And Mythology");
        pieDataManu(12,"History");
        pieDataManu(13,"Film And Entertainment");
        pieDataManu(14,"Inventions And Discoveries");
        pieDataManu(15,"First In Different Fields");
        pieDataManu(16,"Festival,Art And Culture");
        pieDataManu(17,"Polity And Constitution");
        pieDataManu(18,"Literature");
        pieDataManu(19,"Health And Disease");
        pieDataManu(20,"Miscellaneous");


        countDownTimer1=new CountDownTimer(1000*60*60,1000) {
            @Override
            public void onTick(long l) {
                if(mu>=20){
                    pieChart();
                    countDownTimer1.cancel();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    public void pieDataManu(int i, final String category){
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("PieChart").child(String.valueOf(i)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    int i=snapshot.getValue(Integer.class);

                    visitors1.add(new PieEntry(i,category));
                }catch (Exception e){

                }
                mu++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void pieChart(){


        PieDataSet pieDataSet=new PieDataSet(visitors1,"Category Wise Total Number Of Times Played");
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setValueLineColor(R.color.black);
        pieDataSet.setFormSize(4);


        PieData pieData=new PieData(pieDataSet);

        pieChart.setData(pieData);

        pieChart.invalidate();         //Imporatant line showing pie chart
        pieChart.setEntryLabelTextSize(6);
        pieChart.setEntryLabelColor(R.color.button_color);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Times Played");
        pieChart.setCenterTextColor(Color.parseColor("#2E323C"));
        pieChart.animate();
    }




    private void textDataSetter(){

        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                    correctVsWrong.setText(leaderBoardHolder.getCorrect()+"/"+leaderBoardHolder.getWrong());
                    String timeStr=leaderBoardHolder.getTotalTime()/60+" Min "+leaderBoardHolder.getTotalTime()%60+" Sec ";
                    totalTimeTaken.setText(timeStr);
                    String quizPlayed=String.valueOf((leaderBoardHolder.getCorrect()+leaderBoardHolder.getWrong())/10);
                    totalQuizPlayed.setText(quizPlayed);
                    try{
                        int accuStr=(leaderBoardHolder.getCorrect()*100)/(leaderBoardHolder.getCorrect()+leaderBoardHolder.getWrong());
                        accuracy.setText(String.valueOf(accuStr)+"%");
                    }catch (Exception e){
                        accuracy.setText("0%");
                    }

                }catch (Exception e){
                    correctVsWrong.setText("0/0");
                    totalTimeTaken.setText("0 Min 0 Sec");
                    totalQuizPlayed.setText("0");
                    accuracy.setText("0%");
                }
                supportAlertDialog.dismissLoadingDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void groupBarData(){

        BarGroupDataGetter("Sunday",1);
        BarGroupDataGetter("Monday",2);
        BarGroupDataGetter("Tuesday",3);
        BarGroupDataGetter("Wednesday",4);
        BarGroupDataGetter("Thursday",5);
        BarGroupDataGetter("Friday",6);
        BarGroupDataGetter("Saturday",7);

            countDownTimer=new CountDownTimer(1000*60*60,1000) {
                @Override
                public void onTick(long l) {
                    if(lu==7){
                        barGroupChart();
                        countDownTimer.cancel();
                    }
                }

                @Override
                public void onFinish() {

                }
            }.start();


    }

    public void barGroupChart(){

        barDataSet1 = new BarDataSet(correctBar, "Correct");
        barDataSet1.setValueTextColor(Color.parseColor("#98A8D0"));
        barDataSet1.setColor(Color.parseColor("#98A8D0"));
        barDataSet2 = new BarDataSet(wrongBar, "Wrong");
        barDataSet2.setValueTextColor(Color.parseColor("#98A8D0"));
        barDataSet2.setColor(Color.parseColor("#98A8D0"));



        BarData data = new BarData(barDataSet1, barDataSet2);
        barDataSet1.setColors(ColorTemplate.LIBERTY_COLORS);
        barDataSet2.setColors(ColorTemplate.LIBERTY_COLORS);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(5f);
        xAxis.setTextColor(Color.parseColor("#98A8D0"));


        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        leftAxis.setTextColor(Color.parseColor("#98A8D0"));
        rightAxis.setTextColor(Color.parseColor("#98A8D0"));

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(7);
        float barSpace = 0.1f;
        float groupSpace = 0.5f;
        data.setBarWidth(0.15f);
        data.setValueTextSize(5f);
        data.setValueTextColor(Color.parseColor("#98A8D0"));
        barChart.getXAxis().setAxisMinimum(0);
        barChart.animate();
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
    }


  private void BarGroupDataGetter(String day,int i){
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("BarGroup").child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    BarGroupHolder barGroupHolder =new BarGroupHolder();
                    barGroupHolder=snapshot.getValue(BarGroupHolder.class);
                    int c=barGroupHolder.getCorrect();
                    int w=barGroupHolder.getWrong();
                    correctBar.add(new BarEntry(i, c));
                    wrongBar.add(new BarEntry(i, w));
                    lu++;


                }catch (Exception e){
                    correctBar.add(new BarEntry(i, 0));
                    wrongBar.add(new BarEntry(i, 0));
                    lu++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
  }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{mAdView.destroy();}catch (Exception e){}
        Runtime.getRuntime().gc();

    }

    public void onBackPressed() {
        Intent intent=new Intent(MyProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}