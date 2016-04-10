package com.newer.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.newer.dbdao.Dao;

import java.util.ArrayList;

/**
 * Created by Mr_LUO on 2016/2/5.
 */
public class TabIndicatorAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> list;
    private String[] title = { "全部记录", Dao.TYPE_IN, Dao.TYPE_OUT};

    public TabIndicatorAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
