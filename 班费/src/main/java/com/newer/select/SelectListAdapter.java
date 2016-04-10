package com.newer.select;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newer.classfare.Person;
import com.newer.classfare.R;
import com.newer.classfare.Tools;
import com.newer.dbdao.Dao;

import java.util.List;

/**
 * Created by Mr_LUO on 2016/2/1.
 */
public class SelectListAdapter extends BaseAdapter {

    private Context context;
    private List<Person> list;

    public SelectListAdapter(Context context, List<Person> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.select_list_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_remark.setText(list.get(position).getRemark());
        holder.tv_date.setText(SelectActivity.splitDate(Tools.dateFormat(list.get(position).getDate())));
        String str = "欠费：- ";
        if (list.get(position).getPay().equals(Dao.PAY_FALSE)) {
            holder.tv_name.setTextColor(Color.RED);
            holder.tv_money.setTextColor(Color.RED);
            holder.tv_money.setText(str + list.get(position).getMoney() + "元");
            holder.tv_remark.setTextColor(Color.RED);
            holder.tv_date.setBackgroundColor(Color.RED);
        } else {
            holder.tv_money.setText(list.get(position).getMoney() + "元");
            holder.tv_name.setTextColor(Color.parseColor("#444444"));
            holder.tv_money.setTextColor(Color.parseColor("#898989"));
            holder.tv_remark.setTextColor(Color.parseColor("#898989"));
            holder.tv_date.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_name;
        public TextView tv_money;
        public TextView tv_remark;
        public TextView tv_date;
    }
}
