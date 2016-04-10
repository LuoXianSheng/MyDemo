package com.newer.select;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.newer.classfare.Person;
import com.newer.classfare.R;
import com.newer.classfare.Tools;
import com.newer.dbdao.Dao;

import java.util.ArrayList;

public class PayDetailsActivity extends Activity {

    private TextView tv_name, tv_money, tv_remark, tv_pay, tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_details_activity);
        init();
        ArrayList<Person> list = Dao.getAllDetail(this, getIntent().getIntExtra("id", -1) + "", Dao.CODE_ID);
        tv_name.setText(list.get(0).getName());
        tv_money.setText(list.get(0).getMoney() + "");
        tv_remark.setText(list.get(0).getRemark());
        tv_pay.setText(list.get(0).getPay());
        if (tv_pay.getText().equals(Dao.PAY_FALSE)) {
            tv_pay.setTextColor(Color.RED);
        }
        tv_date.setText(Tools.dateFormat(list.get(0).getDate()));
    }

    private void init() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_remark = (TextView) findViewById(R.id.tv_remark);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_date = (TextView) findViewById(R.id.tv_date);
        ImageView img_back = (ImageView) findViewById(R.id.title_pay_details).findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.title_pay_details).findViewById(R.id.tv_top_title)).setText("记录明细");
    }
}
