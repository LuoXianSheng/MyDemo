package com.newer.rank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.newer.classfare.R;
import com.newer.classfare.Rank;
import com.newer.dbdao.Dao;

import java.util.ArrayList;

public class RankActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView lv_rank;
    private ArrayList<Rank> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_activity);
        findViewById(R.id.title_rank).findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        ((TextView) findViewById(R.id.title_rank).findViewById(R.id.tv_top_title)).setText("排行榜");
        lv_rank = (ListView) findViewById(R.id.lv_rank);
        list = Dao.getRankAll(this);
        lv_rank.setAdapter(new RankListAdapter(this, list));
        lv_rank.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PayRecordActivity.class);
        intent.putExtra("name", list.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
