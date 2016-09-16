package com.ticcorp.ticsong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {

    Context context;
    Bitmap pagerImg;
    BitmapFactory.Options options;

    private final int[] pagerImgs = new int[] {
            R.drawable.tutorial_1,
            R.drawable.tutorial_2,
            R.drawable.tutorial_3,
            R.drawable.tutorial_4,
            R.drawable.tutorial_5
    };

    ImageAdapter (Context context) {
        this.context = context;
        options = new BitmapFactory.Options();
    }

    @Override
    public int getCount () {
        return pagerImgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        pagerImg = BitmapFactory.decodeResource(context.getResources(), pagerImgs[position], options);

        imageView.setImageBitmap(pagerImg);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
