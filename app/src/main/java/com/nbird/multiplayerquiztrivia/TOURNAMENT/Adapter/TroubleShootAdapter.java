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
            R.raw.ts1,
            R.raw.ts2,
            R.raw.ts3,
    };

    public String[] slide_Headings={
      "Anti-Cheat",
      "Duplicate player details",
      "Facing other issues? "
    };

    public String[] slide_descs={
      "- Using App In Background for long will result in automatic kick-outs",
      "- Relax, it can be caused due to poor network. Will be solved later on in quiz itself or you can rejoin the lobby. \n\n" + "- Restarting app can solve Sluggishness (if any)\n ",
      "- Check your Network Connection\n\n" + "- Re-creating Or Re-joining lobby might be a possible solution.",
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
                animation.setScaleY(1.5f);animation.setScaleX(1.5f);break;
            case 2:
                animation.setScaleY(1.25f);animation.setScaleX(1.25f);break;
        }



        return view;
    }


    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }
}
