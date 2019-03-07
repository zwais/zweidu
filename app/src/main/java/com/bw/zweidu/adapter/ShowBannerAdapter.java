package com.bw.zweidu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bw.zweidu.bean.ShowBannerBean;
import java.util.ArrayList;
import java.util.List;

public class ShowBannerAdapter extends PagerAdapter {
    private List<ShowBannerBean.ResultBean> list=new ArrayList<>();
    private Context context;

    public ShowBannerAdapter(List<ShowBannerBean.ResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    public ShowBannerBean.ResultBean getItem(int position){
        return list.get(position);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        final ImageView image=new ImageView(context);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(image);
        Glide.with(context).load(getItem(position%list.size()).getImageUrl()).into(image);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    if (imageOnTouch!=null){
                        imageOnTouch.down(position);
                    }
                }else if(event.getAction()==MotionEvent.ACTION_UP) {
                        imageOnTouch.up(position);
                    }


                return true;

            }
        });
        return image;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    private ImageOnTouch imageOnTouch;

    public void setImageOnTouch(ImageOnTouch imageOnTouch){
        this.imageOnTouch=imageOnTouch;
    }

    public interface  ImageOnTouch{
        void down( int position );
        void up( int position );
    }
}
