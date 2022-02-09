package com.nbird.multiplayerquiztrivia.FACTS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nbird.multiplayerquiztrivia.R;

import java.util.List;


public class slideAdapterMainMenuHorizontalSlide extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private List<mainMenuFactsHolder> listItem;


    public slideAdapterMainMenuHorizontalSlide(Context context, List<mainMenuFactsHolder> list){
        this.context=context;
        this.listItem=list;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== (ConstraintLayout) object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.asset_mainmenuhorizontalslide,container,false);

        TextView slideHeading=(TextView) view.findViewById(R.id.textView);
        TextView slideDiscription=(TextView) view.findViewById(R.id.textView2);
        ImageView image=(ImageView) view.findViewById(R.id.image);


        slideHeading.setText(listItem.get(position).getTitle());
        slideDiscription.setText(listItem.get(position).getDis());
        Glide.with(context).load(listItem.get(position).getImageUrl()).transition(DrawableTransitionOptions.withCrossFade()).into(image);

        container.addView(view);



        return view;
    }




    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((ConstraintLayout)object);
    }
}

