package com.newer.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.newer.classfare.Person;
import com.newer.classfare.R;
import com.newer.dbdao.Dao;
import com.newer.select.PayDetailsActivity;

import java.util.ArrayList;

/**
 * Created by Mr_LUO on 2016/2/5.
 */
public class AllDetailFragment extends Fragment implements AdapterView.OnItemClickListener {

    private int code_type;

    public AllDetailFragment(int code) {
        code_type = code;
    }

    private ArrayList<Person> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_all_detail, container, false);
        ListView lv_all_detail = (ListView) view.findViewById(R.id.lv_all_detail);
        if (code_type == Dao.CODE_ALL_DETAIL) {
            list = Dao.getAllDetail(getContext(), null, Dao.CODE_ALL_DETAIL);
        } else if (code_type == Dao.CODE_TYPE_IN) {
            list = Dao.getAllDetail(getContext(), Dao.TYPE_IN, Dao.CODE_TYPE);
        } else {
            list = Dao.getAllDetail(getContext(), Dao.TYPE_OUT, Dao.CODE_TYPE);
        }
        lv_all_detail.setAdapter(new AllDetailListAdapter(getContext(), list));
        lv_all_detail.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), PayDetailsActivity.class);
        intent.putExtra("id", list.get(position).getId());
        startActivity(intent);
    }
}
