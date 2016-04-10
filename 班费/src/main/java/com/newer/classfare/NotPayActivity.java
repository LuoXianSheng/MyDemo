package com.newer.classfare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.newer.dbdao.Dao;
import com.newer.select.PayDetailsActivity;

import java.util.ArrayList;

/**
 * Created by Mr_LUO on 2016/2/7.
 */
public class NotPayActivity extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<Person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_pay_activity);
        ((TextView) findViewById(R.id.title_not_pay).findViewById(R.id.tv_top_title)).setText("欠费记录");
        findViewById(R.id.title_not_pay).findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        ListView lv_pay_record = (ListView) findViewById(R.id.lv_not_pay);
        list = Dao.getAllDetail(this, null, Dao.CODE_PAY_FALSE);
        lv_pay_record.setAdapter(new NotPayListAdapter(this, list));
        lv_pay_record.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PayDetailsActivity.class);
        intent.putExtra("id", list.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
