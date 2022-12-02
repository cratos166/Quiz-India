package com.nbird.multiplayerquiztrivia.StartingPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.nbird.multiplayerquiztrivia.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    //Array
    public int[] slide_images={
            R.raw.slide1,
            R.raw.slide2,
            R.raw.slide3,
    };

    public String[] slide_Headings={
      "Experience Quizing Like Never Before! ",
      "Audio - Video - Picture Quiz",
      "Play more To Achieve Ranks! "
    };

    public String[] slide_descs={
      "Compete with players across the world. \n" +
              "1 vs 1 - Tournaments(10vs10) - Single player",
      "Challenge your senses with creative quizzing modes...  \n" +
              "Ace with your skills. Learn and win! \n"+
              "With Audio, Video and Picture Quiz \n" +
              "Play with your freinds and family...  \n" +
              "Quiz, Learn and Enjoy! ",
      "Track your progress and achievements\n" +
              "Score more, rank high and rule all\n" +
              "\n" +
              "Welcome to the best way to\n" +
              "Learn, Compete and Have Fun!!!",
    };
    @Override
    public int getCount() {
        return slide_Headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== (LinearLayout) object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.slidelayout,container,false);

        LottieAnimationView animation=(LottieAnimationView) view.findViewById(R.id.anim);
        TextView slideHeading=(TextView) view.findViewById(R.id.textView);
        TextView slideDiscription=(TextView) view.findViewById(R.id.textView2);

        animation.setAnimation(slide_images[position]);
        slideHeading.setText(slide_Headings[position]);
        slideDiscription.setText(slide_descs[position]);

        container.addView(view);


        switch (position){
            case 1:
                animation.setScaleY(0.85f);animation.setScaleX(0.85f);break;
            case 2:
                animation.setScaleY(0.75f);animation.setScaleX(0.75f);break;
        }



        return view;
    }


    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }
}
