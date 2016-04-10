package com.newer.rank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newer.classfare.Person;
import com.newer.classfare.R;
import com.newer.classfare.Rank;
import com.newer.classfare.Tools;
import com.newer.select.SelectActivity;

import java.util.List;

/**
 * Created by Mr_LUO on 2016/2/1.
 */
public class RankListAdapter extends BaseAdapter {

    private Context context;
    private List<Rank> list;

    public RankListAdapter(Context context, List<Rank> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.rank_list_item, null);
            holder = new ViewHolder();
            holder.tv_rank = (TextView) convertView.findViewById(R.id.tv_rank);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_rank.setText((position + 1) + "");
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_total.setText(list.get(position).getTotal() + "元");
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_rank;
        public TextView tv_name;
        public TextView tv_total;
    }
}
