package com.newer.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_LUO on 2016/3/15.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private List<ListData> list;
    private LayoutInflater inflater;
    private LinearLayout layout;

    public ListAdapter(Context context, List<ListData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        System.out.println(list.get(position).getCode() + "----------");
        if (list.get(position).getCode() == MainActivity.TEXT_TYPE) {
            layout = (LinearLayout) inflater.inflate(R.layout.text_item, null);
            ((TextView) layout.findViewById(R.id.tv_text_text)).setText(list.get(position).getContent());
        }
        if (list.get(position).getCode() == MainActivity.URL_TYPE) {
            layout = (LinearLayout) inflater.inflate(R.layout.url_item, null);
            ((TextView) layout.findViewById(R.id.tv_url_text)).setText(list.get(position).getContent());
            ((TextView) layout.findViewById(R.id.tv_url_url)).setText(list.get(position).getUrl());
        }
        if (list.get(position).getCode() == MainActivity.NEWS_TYPE) {
            layout = (LinearLayout) inflater.inflate(R.layout.news_item, null);
            ((TextView) layout.findViewById(R.id.tv_news_text)).setText(list.get(position).getContent());
            List<News> newses = list.get(position).getNewses();
            if (newses != null) {
                for (News news : newses) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(news.getSource() + "：" + news.getArticle() + "\n" + news.getDetailurl() + "\n\n");
                    TextView tv1 = new TextView(context);
                    tv1.setText(sb.toString());
                    layout.addView(tv1);
                }
            }
        }
        if (list.get(position).getCode() == MainActivity.MENU_TYPE) {
            layout = (LinearLayout) inflater.inflate(R.layout.menus_item, null);
            ((TextView) layout.findViewById(R.id.tv_menus_text)).setText(list.get(position).getContent());
            List<Menus> menuses = list.get(position).getMenuses();
            System.out.println(menuses);
            if (menuses != null) {
                for (Menus menus : menuses) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(menus.getName() + "：" + menus.getIcon() + "\n" + menus.getInfo() + "\n" + menus.getDetailurl() + "\n\n");
                    TextView tv1 = new TextView(context);
                    tv1.setText(sb.toString());
                    layout.addView(tv1);
                }
            }
        }
        return layout;
    }
}
