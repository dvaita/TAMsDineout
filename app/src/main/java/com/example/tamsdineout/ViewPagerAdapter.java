package com.example.tamsdineout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] images={R.drawable.menu1,R.drawable.menu2};

    ViewPagerAdapter(Context context) {
        this.context =context;
    }

    @Override
    public int getCount() {
       return  images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View itemView= inflater.inflate(R.layout.viewpager_item,container,false);
        ImageView image;
        image=itemView.findViewById(R.id.img1);
        image.setImageResource(images[position]);

        ViewPager vp=(ViewPager)container;
        vp.addView(itemView,0);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager) container;
        View view=(View)object;
        vp.removeView(view);
    }
}
