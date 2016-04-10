package com.newer.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.newer.classfare.R;
import com.newer.dbdao.Dao;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class DetailActivity extends FragmentActivity {

    private TabPageIndicator indicator;
    private ViewPager pager;
    private ArrayList<Fragment> list;
    private UnderlinePageIndicatorEx mUnderlinePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        init();
    }

    private void init() {
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.viewpager);
        list = new ArrayList<>();
        list.add(new AllDetailFragment(Dao.CODE_ALL_DETAIL));
        list.add(new AllDetailFragment(Dao.CODE_TYPE_IN));
        list.add(new AllDetailFragment(Dao.CODE_TYPE_OUT));
        pager.setAdapter(new TabIndicatorAdapter(getSupportFragmentManager(), list));
        pager.setOffscreenPageLimit(3);// 默认的预加载是1，设置的只能是大于1才会有效果的
        indicator.setViewPager(pager, 0);

        //重写TabIndicator样式
        mUnderlinePageIndicator = (UnderlinePageIndicatorEx)findViewById(R.id.underline_indicator);
        mUnderlinePageIndicator.setViewPager(pager);
        mUnderlinePageIndicator.setFades(false);

        indicator.setOnPageChangeListener(mUnderlinePageIndicator);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
