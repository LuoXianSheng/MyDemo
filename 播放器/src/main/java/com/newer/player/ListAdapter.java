package com.newer.player;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mr_LUO on 2016/3/23.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private List<Music> list;

    public ListAdapter(Context context, List<Music> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
            holder.itemSpecial = (TextView) convertView.findViewById(R.id.itemSpecial);
            holder.itemStatus = (ImageView) convertView.findViewById(R.id.itemStatus);
            holder.itemStatus.setImageResource(R.drawable.statue_gif);
            AnimationDrawable anima = (AnimationDrawable) holder.itemStatus.getDrawable();
            anima.start();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTitle.setText(list.get(position).getTitle());
        holder.itemName.setText(list.get(position).getName());
        holder.itemSpecial.setText(list.get(position).getSpecial());
        if (MainActivity.changeItemStatus.get(position) == Config.GONE) {
            holder.itemStatus.setVisibility(View.GONE);
        } else {
            holder.itemStatus.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView itemTitle, itemName, itemSpecial;
        public ImageView itemStatus;
    }
}
