package com.nbird.multiplayerquiztrivia.MAIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
import com.nbird.multiplayerquiztrivia.FACTS.mainMenuFactsHolder;
import com.nbird.multiplayerquiztrivia.FACTS.slideAdapterMainMenuHorizontalSlide;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.Model.FirstTime;
import com.nbird.multiplayerquiztrivia.NAVIGATION.ACTIVITY.AboutUsActivity;
import com.nbird.multiplayerquiztrivia.NAVIGATION.ACTIVITY.MyProfileActivity;
import com.nbird.multiplayerquiztrivia.NAVIGATION.MODEL.UpdateInfo;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.BasicDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("NEW_APP");
    DatabaseReference myRef = database.getReference();
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;
    AppData appData;
    Dialog loadingDialog;
    AlertDialog alertDialog;
    int num=0;

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


    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10;
    CardView cardView11, cardView12, cardView13, cardView14, cardView15, cardView16, cardView17, cardView18, cardView19, cardView20;
    CardView cardView21, cardView22, cardView23, cardView24, cardView25, cardView26;

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7, linearLayout8, linearLayout9, linearLayout10;
    LinearLayout linearLayout11, linearLayout12, linearLayout13, linearLayout14, linearLayout15, linearLayout16, linearLayout17, linearLayout18, linearLayout19, linearLayout20;
    LinearLayout linearLayout21, linearLayout22, linearLayout23, linearLayout24, linearLayout25, linearLayout26;

    ImageView nav_image;


    TextInputEditText usernameEditText;

    Uri imageUri;

    Context context;
    View v;
    int id;


    FirebaseStorage storage;
    StorageReference storageReference;


    AppString appString;
    String mailid123;

    List<Modes> lstExam;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle mToggle;
    androidx.appcompat.widget.Toolbar toolbar;

    public ViewPager slideViewPager;
    private LinearLayout dotLayout;
  //  private slideAdapterMainMenuHorizontalSlide sliderAdapter;
    private TextView[] mDots;
    private List<mainMenuFactsHolder> list;

    private ShimmerFrameLayout mShimmerViewContainer;
    slideAdapterMainMenuHorizontalSlide sliderAdapter;

    private int currentPage;

    ImageView nav_image123;
    TextView nav_mail;



    public static final int APP_UPDATE_VERSION_CODE = 1;


    AdView mAdView;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        isShowAds();




        mAuth = FirebaseAuth.getInstance();

        createRequest();
        appData = new AppData();

        list=new ArrayList<>();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signupDialog(this);
        }else{
            Dialog dialog=null;
            SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MainActivity.this);
            supportAlertDialog.showLoadingDialog();


                    table_user.child("UPDATE_CODE").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            try{
                                supportAlertDialog.dismissLoadingDialog();

                                UpdateInfo updateInfo=snapshot.getValue(UpdateInfo.class);

                                if(updateInfo.getCODE()!=APP_UPDATE_VERSION_CODE){

                                    updateDialog(updateInfo.getDIS(),updateInfo.getTITLE(),updateInfo.getLINKDATA());

                                }else{

                                    table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            try{
                                                LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);
                                                if(leaderBoardHolder.getSumationScore()>2000&&leaderBoardHolder.getSumationScore()<5000){
                                                    table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("isReview").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            try{
                                                                if(!snapshot.getValue(Boolean.class)){

                                                                }
                                                            }catch (Exception e){

                                                                table_user.child("UPDATE_CODE").child("linkdata").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        try{
                                                                            String urlStr=snapshot.getValue(String.class);
                                                                            reViewDialog(urlStr);
                                                                        }catch (Exception e1){

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });


                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



        }

        RecyclerCardView();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        slideViewPager=(ViewPager) findViewById(R.id.slideViewPager);
        dotLayout=(LinearLayout) findViewById(R.id.dotLayout);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        mToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open,R.string.close);

        navigationView.bringToFront();


        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


        for(int i=1;i<=3;i++){
            dataForHorizontalSlide();
        }


        int notificationOfHost=getIntent().getIntExtra("notificationOfHost",0);

        if(notificationOfHost==1){
            BasicDialog basicDialog=new BasicDialog(MainActivity.this,toolbar,"Notification.","Your previous room was dissolved because your host left the game.","Okay",R.raw.notification_anim);
            basicDialog.start();
        }else if(notificationOfHost==2){
            BasicDialog basicDialog=new BasicDialog(MainActivity.this,toolbar,"Notification.","Your were kicked out by the host from the room.","Okay",R.raw.notification_anim);
            basicDialog.start();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        nav_mail = (TextView)hView.findViewById(R.id.mailidtext);
        nav_image123 = (ImageView) hView.findViewById(R.id.proimage);

        nav_mail.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MainActivity.this));
        Glide.with(getBaseContext()).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MainActivity.this)).apply(RequestOptions
                .bitmapTransform(new RoundedCorners(18)))
                .into(nav_image123);

        try{

            table_user.child("VS_REQUEST").child(mAuth.getCurrentUser().getUid()).removeValue();
            table_user.child("VS_RESPONSE").child(mAuth.getCurrentUser().getUid()).removeValue();

            table_user.child("VS_PLAY").child("DataExchange").child(mAuth.getCurrentUser().getUid()).removeValue();
            table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).removeValue();
            table_user.child("VS_PLAY").child("PlayerCurrentAns").child(mAuth.getCurrentUser().getUid()).removeValue();
            //   table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").removeValue();
            table_user.child("VS_PLAY").child("IsDone").child(mAuth.getCurrentUser().getUid()).removeValue();




        }catch (Exception e){
            e.printStackTrace();
        }

        noInternet();










    }

    public void reViewDialog(String urlStr){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);
        textTitle.setTextSize(13);
        textTitle.setText("Please do tell us how you liked the game by giving a review to our app.It will help our game grow.  Will just take 5 seconds, please!!!");



        yesButton.setTextSize(8);
        noButton.setTextSize(8);
        yesButton.setText("Yes, Ofcourse");
        noButton.setText("No, Not Interested");

        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.imageIcon);
        anim.setAnimation(R.raw.rateus_anim);
        anim.playAnimation();
        anim.loop(true);







        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("isReview").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
                startActivity(browserIntent);
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);

                alertDialog.cancel();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("isReview").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                alertDialog.dismiss();
            }
        });

    }







    private void isShowAds(){
        table_user.child("ADS").child("showAds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    Boolean isShow=snapshot.getValue(Boolean.class);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS,MainActivity.this,isShow);
                    if(isShow){
                        mAdView = findViewById(R.id.adView);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                        mAdView.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void noInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


        }
        else {


            noInternetDialog();


        }
    }

    private void noInternetDialog(){



        AlertDialog.Builder builderRemove=new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_model_1,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);
        Button button=(Button) viewRemove1.findViewById(R.id.button);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);
        textTitle.setText("No Internet");


        TextView textDis=(TextView) viewRemove1.findViewById(R.id.textDis);
        textDis.setText("This game requires internet connection for all the modes. Please connect with internet and retry again.");

        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.anim);
        anim.setAnimation(R.raw.no_internet);
        anim.playAnimation();
        anim.loop(true);



        button.setText("OKAY");









        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }

            }
        });

    }



    private void updateDialog(String dis,String title,String linkdata){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_model_1,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);
        Button button=(Button) viewRemove1.findViewById(R.id.button);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);
        textTitle.setText(title);


        TextView textDis=(TextView) viewRemove1.findViewById(R.id.textDis);
        textDis.setText(dis);

        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.anim);
        anim.setAnimation(R.raw.updateanim);
        anim.playAnimation();
        anim.loop(true);



        button.setText("UPDATE");


        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)){

            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);

                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = viewRemove1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }




        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(linkdata));
                    startActivity(browserIntent);
                }catch (Exception e){

                }
            }
        });
    }


    public void dataForHorizontalSlide(){

        // create instance of Random class
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int setRandomNumber;
        final int categoryRandomNumber = rand.nextInt(7)+1;
        if(categoryRandomNumber<=5||categoryRandomNumber==7){
            setRandomNumber = rand.nextInt(49)+1;
        }else{
            setRandomNumber = rand.nextInt(199)+1;
        }
        myRef.child("Facts").child(String.valueOf(categoryRandomNumber)).child(String.valueOf(setRandomNumber)).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange( DataSnapshot snapshot) {

                list.add(snapshot.getValue(mainMenuFactsHolder.class));
                num++;


                if(num==3){
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    AdapterManupulation();
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Facts Data Can't be Loaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void AdapterManupulation(){
        sliderAdapter=new slideAdapterMainMenuHorizontalSlide(MainActivity.this,list);
        slideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListner);
        sliderAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void addDotsIndicator(int position){
        mDots=new TextView[3];
        dotLayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.white));
            dotLayout.addView(mDots[i]);

        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.button_color));
        }
    }
    ViewPager.OnPageChangeListener viewListner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage=position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void signupDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_sign_up, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        SignInButton googsignin = (SignInButton) view1.findViewById(R.id.googlesignin);


        alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        googsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog = new Dialog(MainActivity.this);
                loadingDialog.setContentView(R.layout.loading_screen);
                loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                loadingDialog.setCancelable(true);
                loadingDialog.show();
                signIn();
            }
        });
    }


    public void RecyclerCardView(){

        lstExam=new ArrayList<>();
        parto();
        //   timerStarter();
        RecyclerView myrv=(RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter myAdapter=new RecyclerViewAdapter(this,lstExam);
        myrv.setLayoutManager(new GridLayoutManager(this,2));

        myrv.setAdapter(myAdapter);
    }

    public void parto(){
        lstExam.add(new Modes("1 Vs 1",R.drawable.versusicon,"Time for the One-On-One. Compete with a rival online. Time your knowledge and be the champion."));
        lstExam.add(new Modes("Tournament Mode",R.drawable.tournament,"Quizzers from all over the world come together in the arena to show who's the ultimate leaderboard breaker."));
        lstExam.add(new Modes("Single Mode",R.drawable.singleicon,"Test your knowledge and compete against time. Score points for accuracy and achieve ranks."));
        lstExam.add(new Modes("Buzzer Mode",R.drawable.buzzer_icon_main,"The test of agility! Smart buzzers! Experience it in the Buzzer mode and be the first one to buzz your way ahead!"));
    }


    public void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        try {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    // ...
                }
            }
        } catch (Exception e) {

        }


        try {
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
                                try {
                                    nav_image.setImageBitmap(selectedImage);
                                } catch (Exception e) {
                                }


                                uploadImage();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context, "You haven't picked Image", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }

        } catch (Exception e) {

        }


    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            String personEmail = account.getEmail();
                            appData.setSharedPreferencesString(AppString.SP_MAIN, AppString.SP_MY_MAIL, MainActivity.this, personEmail);

                            table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("personal").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {

                                        FirstTime firstTime=snapshot.getValue(FirstTime.class);

                                        if (firstTime.getFirstTime() == 1) {
                                            Toast.makeText(getBaseContext(), "Logged In Successful!", Toast.LENGTH_LONG).show();

                                            try {
                                                loadingDialog.dismiss();
                                            } catch (Exception e) {
                                            }
                                            try {
                                                alertDialog.dismiss();
                                            } catch (Exception e) {
                                            }


                                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, MainActivity.this,firstTime.getUserName());
                                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, MainActivity.this,firstTime.getImageURL());
                                            try{
                                                nav_mail.setText(firstTime.getUserName());
                                                Glide.with(MainActivity.this).load(firstTime.getImageURL()).apply(RequestOptions
                                                                .bitmapTransform(new RoundedCorners(18)))
                                                        .into(nav_image123);
                                            }catch (Exception e){

                                            }

                                            // NOT FOR FIRST TIME
                                        }

                                    } catch (Exception e) {
                                        //  FOR FIRST TIME
                                        Random rand = new Random();

                                        final int setNumber = rand.nextInt(95000) + 1;
                                        String playerName = "Player" + setNumber;

                                        FirstTime firstTime = new FirstTime(AppString.IMAGE_URL, playerName, 1, 0);

                                        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("personal").setValue(firstTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getBaseContext(), "Logged In Successful!", Toast.LENGTH_LONG).show();
                                                    try {
                                                        loadingDialog.dismiss();
                                                    } catch (Exception e) {
                                                    }
                                                    try {
                                                        alertDialog.dismiss();
                                                    } catch (Exception e) {
                                                    }


                                                    start();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Record Not Saved!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {

                            Toast.makeText(getBaseContext(), "Error!Try Again" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            try {
                                loadingDialog.dismiss();
                            } catch (Exception e) {
                            }


                        }
                    }
                });
    }





    public void start() {
        mailid123 = appData.getSharedPreferencesString(appString.SP_MAIN, appString.SP_MY_MAIL, MainActivity.this);
        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_profile_selection, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
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

        nav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


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
                                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MainActivity.this);
                                supportAlertDialog.showLoadingDialog();


                                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("userName").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        supportAlertDialog.dismissLoadingDialog();
                                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                        appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MainActivity.this,usernameEntered);
                                        nav_mail.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MainActivity.this));
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
                    SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MainActivity.this);
                    supportAlertDialog.showLoadingDialog();

                    myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("userName").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            supportAlertDialog.dismissLoadingDialog();
                            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MainActivity.this,usernameEntered);

                            nav_mail.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,MainActivity.this));

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

                settingBlack();
                linearLayout.setBackgroundResource(R.drawable.double_color_2);
                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("imageURL").setValue(urlAva).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MainActivity.this,urlAva);
                            nav_image123.setImageResource(0);
                            Glide.with(getBaseContext()).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MainActivity.this)).apply(RequestOptions
                                    .bitmapTransform(new RoundedCorners(18)))
                                    .into(nav_image123);
                        } else {

                        }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }


    private void uploadImage() {


        if (imageUri != null) {

            try {
                settingBlack();
            } catch (Exception e) {

            }


            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();


            storageReference = storageReference.child("NEW_APP/images/" + mailid123);


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
                                    Toast.makeText(MainActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    try {

                                        StorageReference urlref = storageReference;
                                        urlref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri downloadUrl) {
                                                String imageurl = downloadUrl.toString();

                                                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("imageURL").setValue(imageurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MainActivity.this,imageurl);
                                                            nav_image123.setImageResource(0);
                                                            Glide.with(getBaseContext()).load(imageurl).apply(RequestOptions
                                                                    .bitmapTransform(new RoundedCorners(18)))
                                                                    .into(nav_image123);

                                                        } else {

                                                        }
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
                                    .makeText(MainActivity.this,
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




    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        //  music.start();

    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
        // music.pause();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_logout:


                AlertDialog.Builder builderRemove=new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
                View viewRemove1= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
                builderRemove.setView(viewRemove1);
                builderRemove.setCancelable(false);


                Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
                Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);

                TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);
                textTitle.setText("You really want to Logout?");

                LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.imageIcon);
                anim.setAnimation(R.raw.logout);
                anim.playAnimation();
                anim.loop(true);

                AppData appData=new AppData();
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, MainActivity.this)){

                    MobileAds.initialize(MainActivity.this);
                    AdLoader adLoader = new AdLoader.Builder(MainActivity.this, AppString.NATIVE_ID)
                            .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                @Override
                                public void onNativeAdLoaded(NativeAd nativeAd) {
                                    ColorDrawable cd = new ColorDrawable(0x393F4E);
                                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                                    TemplateView template = viewRemove1.findViewById(R.id.my_template);
                                    template.setStyles(styles);
                                    template.setNativeAd(nativeAd);
                                    template.setVisibility(View.VISIBLE);
                                }
                            })
                            .build();

                    adLoader.loadAd(new AdRequest.Builder().build());

                }





                final AlertDialog alertDialog=builderRemove.create();
                if(alertDialog.getWindow()!=null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                try{
                    alertDialog.show();
                }catch (Exception e){

                }


                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    alertDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                break;



            case R.id.nav_profile:
                Intent intent=new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_about:
                Intent intent12=new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent12);
                break;
            case R.id.nav_rate:
                Dialog dialog1=null;
                SupportAlertDialog supportAlertDialog1=new SupportAlertDialog(dialog1,MainActivity.this);
                supportAlertDialog1.showLoadingDialog();
                table_user.child("UPDATE_CODE").child("linkdata").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            String linkdata=snapshot.getValue(String.class);
                            Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(linkdata));
                            startActivity(browserIntent);
                            overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
                            supportAlertDialog1.dismissLoadingDialog();
                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.share_us:
                Toast.makeText(this, "Share Me!", Toast.LENGTH_SHORT).show();
                Dialog dialog=null;
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,MainActivity.this);
                supportAlertDialog.showLoadingDialog();
                table_user.child("UPDATE_CODE").child("linkdata").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            supportAlertDialog.dismissLoadingDialog();
                            String linkdata=snapshot.getValue(String.class);
                            Intent shareIntent=new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plane");
                            String shareBody="Multiplayer Quiz Trivia: The ultimate Quiz Station!\n\n" +
                                    "Experience the fun of quizzing with your friends and family in the most innovative way. \n\n" +
                                    "Download Now! \n" + linkdata;
                            String sharesub="Multiplayer Quiz Trivia";
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                            shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                            startActivity(Intent.createChooser(shareIntent,"Share Using"));
                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
            case R.id.nav_tos:
                Intent browserIntenttos = new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/LegalFiles%2FMULTIPLAYER%20TRIVIA%2FTERMS_OF_SERVICE_Multiplayer_Quiz_Trivia.pdf?alt=media&token=9529f008-a6f9-47cd-8793-15ab326b25bf"));
                startActivity(browserIntenttos);
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
                break;
            case R.id.nav_ref:
                Intent browserIntenttos1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/LegalFiles%2FMULTIPLAYER%20TRIVIA%2FRefund_Policy_Multiplayer_Quiz_Trivia.pdf?alt=media&token=5802edbd-d3d9-41db-b890-3b1904a71da1"));
                startActivity(browserIntenttos1);
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
                break;
            case R.id.nav_ps:
                Intent browserIntenttos2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/LegalFiles%2FMULTIPLAYER%20TRIVIA%2FPrivacy_policy_MULTIPLAYER%20TRIVIA%20QUIZ.pdf?alt=media&token=cef533fc-94b8-4155-b1bd-b52bde245ce8"));
                startActivity(browserIntenttos2);
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
                break;
//            case R.id.nav_help:
//                Intent helpguide = new Intent(mainMenuActivity.this,HelpGuide1.class);
//                startActivity(helpguide);
//                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
//                finish();
//                break;

            default :
                return true;


        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {mAdView.destroy();}catch (Exception e){}
        Runtime.getRuntime().gc();
    }
}



