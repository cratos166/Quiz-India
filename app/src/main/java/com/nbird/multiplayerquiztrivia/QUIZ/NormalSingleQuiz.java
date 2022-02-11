package com.nbird.multiplayerquiztrivia.QUIZ;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.LL.LifeLine;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NormalSingleQuiz extends AppCompatActivity {

    TextView questionTextView,scoreBoard;
    Button option1,option2,option3,option4,nextButton;
    LinearLayout linearLayout;

    CardView audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL;
    LinearLayout linearLayoutexpert,linearLayoutAudience,linearLayoutFiftyFifty,linearLayoutSwap;

    TextView timerText;

    LottieAnimationView anim11,anim12,anim13,anim14,anim15,anim16,anim17,anim18,anim19,anim20;

    ImageView myPic;
    Dialog loadingDialog;
    SupportAlertDialog supportAlertDialog;

    int category;
    AppData appData;
    CardView clockCardView;


    private List<questionHolder> list;

    int fiftyfiftynum=0,audiencenum=0,swapnum=0,expertnum=0;

    int lifelineSum=0;

    LifeLine lifeLine;
    String myName;

    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_single_quiz);

        category=getIntent().getIntExtra("category",1);

        list=new ArrayList<>();



        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, NormalSingleQuiz.this);

        questionTextView=findViewById(R.id.question);
        scoreBoard=findViewById(R.id.questionNumber);
        option1=(Button) findViewById(R.id.button1);
        option2=(Button) findViewById(R.id.button2);
        option3=(Button) findViewById(R.id.button3);
        option4=(Button) findViewById(R.id.button4);
        nextButton=(Button) findViewById(R.id.nextbutton);
        linearLayout=(LinearLayout) findViewById(R.id.linearButtonlayout);
        timerText=(TextView) findViewById(R.id.timer);
        audienceLL=(CardView) findViewById(R.id.audience);
        expertAdviceLL=(CardView) findViewById(R.id.expert);
        fiftyfiftyLL=(CardView) findViewById(R.id.fiftyfifty);
        swapTheQuestionLL=(CardView) findViewById(R.id.swap);
        linearLayoutexpert=(LinearLayout) findViewById(R.id.linearLayoutexpert) ;
        linearLayoutAudience=(LinearLayout) findViewById(R.id.linearLayoutAudience) ;
        linearLayoutFiftyFifty=(LinearLayout) findViewById(R.id.linearLayoutfiftyfifty) ;
        linearLayoutSwap=(LinearLayout) findViewById(R.id.linearLayoutSwap) ;
        anim11=(LottieAnimationView) findViewById(R.id.anim11);
        anim12=(LottieAnimationView) findViewById(R.id.anim12);
        anim13=(LottieAnimationView) findViewById(R.id.anim13);
        anim14=(LottieAnimationView) findViewById(R.id.anim14);
        anim15=(LottieAnimationView) findViewById(R.id.anim15);
        anim16=(LottieAnimationView) findViewById(R.id.anim16);
        anim17=(LottieAnimationView) findViewById(R.id.anim17);
        anim18=(LottieAnimationView) findViewById(R.id.anim18);
        anim19=(LottieAnimationView) findViewById(R.id.anim19);
        anim20=(LottieAnimationView) findViewById(R.id.anim20);
        myPic=(ImageView) findViewById(R.id.myPic);
        clockCardView = (CardView) findViewById(R.id.cardView3);

        appData=new AppData();



        Glide.with(getBaseContext()).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, NormalSingleQuiz.this)).apply(RequestOptions
                .bitmapTransform(new RoundedCorners(18)))
                .into(myPic);


        supportAlertDialog=new SupportAlertDialog(loadingDialog,NormalSingleQuiz.this);
        supportAlertDialog.showLoadingDialog();




        lifeLine();



    }


    public void lifeLine(){

        lifeLine=new LifeLine(linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert,position,list,option1,option2,
                option3,option4,myName,NormalSingleQuiz.this);


        fiftyfiftyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fiftyfiftynum==0) {
                    lifelineSum++;
                    fiftyfiftynum = 1;

                    lifeLine.fiftyfiftyLL();

                }else{
                    lifeLine.LLUsed();

                }


            }
        });


        audienceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(audiencenum==0) {

                    lifelineSum++;
                    audiencenum = 1;

                     lifeLine.audienceLL();

                }else{
                    lifeLine.LLUsed();
                }

            }
        });


        swapTheQuestionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swapnum==0) {

                    lifelineSum++;
                    swapnum = 1;
//                    linearLayoutSwap.setBackgroundResource(R.drawable.usedicon);
//                    nextButton.setEnabled(false);
//                    nextButton.setAlpha(0.7f);
//                    enableOption(true);
//                    position++;
//                    LLTrueManupulator();
//
//
//                    count = 0;
//                    playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                }else{
                    lifeLine.LLUsed();
                }
            }
        });


        expertAdviceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expertnum==0){


                    lifelineSum++;
                    expertnum=1;


                   lifeLine.expertAdviceLL();
                }else{
                    lifeLine.LLUsed();
                }
            }
        });



    }


    public void questionSelector(){
//        for(int i=0;i<11;i++){
//            // create instance of Random class
//            Random rand = new Random();
//
//            // Generate random integers in range 0 to 29
//            int setNumber;
//            switch (category){
//                case 1:
//                case 3: case 4: case 5: case 6: case 9: case 10: case 11: case 12: case 17:
//                    setNumber = rand.nextInt(299)+1;
//                    fireBaseData(setNumber);break;
//                case 2: case 14:
//                    setNumber = rand.nextInt(499)+1;
//                    fireBaseData(setNumber);break;
//                case 7:
//                    setNumber = rand.nextInt(401)+1;
//                    fireBaseData(setNumber);break;
//                case 8: case 18:
//                    setNumber = rand.nextInt(339)+1;
//                    fireBaseData(setNumber);break;
//                case 13: case 15: case 16:
//                    setNumber = rand.nextInt(249)+1;
//                    fireBaseData(setNumber);break;
//                case 19:
//                    setNumber = rand.nextInt(399)+1;
//                    fireBaseData(setNumber);break;
//                default:
//                    setNumber = rand.nextInt(6326)+1;
//                    fireBaseData2(setNumber);break;
//            }
//            //NEED TO CHANGE HERE
//            //NEED TO CHANGE HERE
        }







    }






