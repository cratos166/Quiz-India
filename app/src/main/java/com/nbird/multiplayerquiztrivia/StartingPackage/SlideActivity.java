package com.nbird.multiplayerquiztrivia.StartingPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

public class SlideActivity extends AppCompatActivity {
    private ViewPager slideViewPager;
    private LinearLayout dotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private CardView nextbutton, backbutton;
    private int currentPage;
    ImageView next_done_button;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        AppData appData = new AppData();

        if (appData.getSharedPreferencesBollean(AppString.SP_MAIN, AppString.SP_IS_LOGIN, this)) {
            intentFunction();
        }else{
            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,SlideActivity.this,AppString.IMAGE_URL);
        }

        slideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        nextbutton = (CardView) findViewById(R.id.nextButton);
        backbutton = (CardView) findViewById(R.id.previousButton);

        next_done_button = (ImageView) findViewById(R.id.next_done_button);


        sliderAdapter = new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);


        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListner);

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPage == 2) {
                    intentFunction();
                }
                slideViewPager.setCurrentItem(currentPage + 1);

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideViewPager.setCurrentItem(currentPage - 1);
            }
        });


    }


    private void intentFunction() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        dotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(60);
            mDots[i].setTextColor(getResources().getColor(R.color.white));
            dotLayout.addView(mDots[i]);

        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.layer_1));
        }
    }


    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;
            if (currentPage == 0) {

                nextbutton.setEnabled(true);
                backbutton.setEnabled(false);
                backbutton.setVisibility(View.INVISIBLE);
                nextbutton.setScaleX(1);
                nextbutton.setScaleY(1);
                next_done_button.setBackgroundResource(R.drawable.arrow_forward);
            } else if (position == mDots.length - 1) {
                nextbutton.setEnabled(true);
                backbutton.setEnabled(true);
                backbutton.setVisibility(View.VISIBLE);
                next_done_button.setBackgroundResource(R.drawable.done_button);
            } else {
                nextbutton.setEnabled(true);
                backbutton.setEnabled(true);
                backbutton.setVisibility(View.VISIBLE);
                nextbutton.setScaleX(1);
                next_done_button.setBackgroundResource(R.drawable.arrow_forward);
                nextbutton.setScaleY(1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}