package com.newer.rank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.newer.classfare.Person;
import com.newer.classfare.R;
import com.newer.dbdao.Dao;
import com.newer.select.PayDetailsActivity;
import com.newer.select.SelectListAdapter;

import java.util.ArrayList;

public class PayRecordActivity extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<Person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_record_activity);
        ((TextView) findViewById(R.id.title_pay_record).findViewById(R.id.tv_top_title)).setText("缴费记录");
        findViewById(R.id.title_pay_record).findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ListView lv_pay_record = (ListView) findViewById(R.id.lv_pay_record);
        String name = getIntent().getStringExtra("name");
        list = Dao.getAllDetail(this, name, Dao.CODE_NAME);
        lv_pay_record.setAdapter(new SelectListAdapter(this, list));
        lv_pay_record.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PayDetailsActivity.class);
        intent.putExtra("id", list.get(position).getId());
        startActivity(intent);
    }
}
