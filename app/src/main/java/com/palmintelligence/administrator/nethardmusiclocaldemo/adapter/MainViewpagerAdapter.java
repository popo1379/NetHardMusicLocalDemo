package com.palmintelligence.administrator.nethardmusiclocaldemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class MainViewpagerAdapter extends PagerAdapter {
    private ArrayList<View> mViewpagees;
    public MainViewpagerAdapter(ArrayList<View> mViewpages) {

        this.mViewpagees = mViewpages;
    }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewpagees.get(position %  mViewpagees.size());
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


