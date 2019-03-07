package com.bw.zweidu;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ShowViewPagerTransformer implements ViewPager.PageTransformer {
    public static final float minScale = 0.9f;
    private static final float DEFAULT_MAX_ROTATE = 15f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;
    public static final float DEFAULT_CENTER = 0.4f;

    @Override
    public void transformPage(View page, float position) {
        page.setPivotY(page.getHeight()/2);
        if (position < -1) {
            page.setScaleY(minScale);
            page.setScaleX(minScale);
            page.setRotationY(-1 * mMaxRotate);
            page.setPivotX(page.getWidth());
        } else if (position <= 1) {
            page.setRotationY(position * mMaxRotate);
            if (position < 0)
            {
                float factor = minScale + (1 - minScale) * (1 + position);
                page.setScaleY(factor);
                page.setScaleX(factor);
                page.setPivotX(page.getWidth());
            } else
            {
                float factor = minScale + (1 - minScale) * (1 - position);
                page.setScaleY(factor);
                page.setScaleX(factor);
                page.setPivotX(0);
            }
        } else {
            page.setScaleY(minScale);
            page.setScaleX(minScale);
            page.setRotationY(1 * mMaxRotate);
            page.setPivotX(0);
        }
    }

}
