package com.newer.imgbrowse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.newer.classfare.R;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Mr_LUO on 2016/2/7.
 */
public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
//    private DisplayImageOptions options;

//    public GridAdapter(Context context, List<String> list, DisplayImageOptions options) {
//        this.context = context;
//        this.list = list;
//        this.options = options;
//    }

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
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.gridview_item, parent, false);
            holder.iView_item = (ImageView) convertView.findViewById(R.id.img_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
//        ImageLoader.getInstance().displayImage("file:///" + list.get(position), holder.iView_item, this.options);
        return convertView;
    }

    private class ViewHolder {
        public ImageView iView_item;
    }
}
