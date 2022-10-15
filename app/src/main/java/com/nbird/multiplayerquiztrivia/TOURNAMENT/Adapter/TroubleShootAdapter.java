package com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.nbird.multiplayerquiztrivia.R;

public class TroubleShootAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public TroubleShootAdapter(Context context){
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
      "KBC - Custom Quiz - Picture Quiz",
      "Play more To Achieve Ranks! "
    };

    public String[] slide_descs={
      "Compete with players across the world. \n" +
              "1 vs 1 - Tournaments - Single player",
      "Play the legendary KBC... \n" +
              "Test your visual skills with picture mode and \n"+
              "Do your friends really know you? \n" +
              "Check out the personal custom quiz. \n" +
              "Create Share Enjoy! ",
      "* Top the Leadership boards\n" +
              "* Track your progress and achievements\n" +
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
