package com.newer.classfare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newer.dbdao.Dao;
import com.newer.detail.DetailActivity;
import com.newer.rank.RankActivity;
import com.newer.record.RecordActivity;
import com.newer.select.SelectActivity;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btn_openSelect, btn_openDetail, btn_openRecord,
            btn_openRanking, btn_openImgBrowse, btn_openSet;
    private TextView tv_balance, tv_not_pay, tv_not_pay_name;
    private long firstClickTime = 0;// 退出第一次点击的时间
    private long lastClickTime = 0;// 退出第二次次点击时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initImgLoader();//初始化图片加载器
    }

    private void initImgLoader() {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
//        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    public void init() {
        btn_openSelect = (Button) findViewById(R.id.btn_openSelect);
        btn_openDetail = (Button) findViewById(R.id.btn_openDetail);
        btn_openRecord = (Button) findViewById(R.id.btn_openRecord);
        btn_openRanking = (Button) findViewById(R.id.btn_openRanking);
        btn_openImgBrowse = (Button) findViewById(R.id.btn_openImgBrowse);
        btn_openSet = (Button) findViewById(R.id.btn_openSet);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_not_pay = (TextView) findViewById(R.id.tv_not_pay);
        tv_not_pay_name = (TextView) findViewById(R.id.tv_not_pay_name);

        tv_not_pay.setOnClickListener(this);
        btn_openSelect.setOnClickListener(this);
        btn_openDetail.setOnClickListener(this);
        btn_openRecord.setOnClickListener(this);
        btn_openRanking.setOnClickListener(this);
        btn_openImgBrowse.setOnClickListener(this);
        btn_openSet.setOnClickListener(this);

        initBalance();

    }

    private void initBalance() {
        tv_balance.setText(Dao.getBalance(this, Dao.PAY_TRUE) + "元");
        int i = Dao.getBalance(this, Dao.PAY_FALSE);
        if (i != 0) {
            tv_not_pay_name.setVisibility(View.VISIBLE);
            tv_not_pay.setText("- " + i + "元");
        } else {
            tv_not_pay_name.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_not_pay:
                intent.setClass(this, NotPayActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_openSelect:
                intent.setClass(this, SelectActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_openDetail:
                intent.setClass(this, DetailActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_openRecord:
                intent.setClass(this, RecordActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_openRanking:
                intent.setClass(this, RankActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_openImgBrowse:
//                intent.setClass(this, ImgBrowseActivity.class);
//                startActivityForResult(intent, 1);
                Toast.makeText(this, "OOM", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_openSet:
                Toast.makeText(this, "暂未实现！", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        tv_balance.setText(Dao.getBalance(this, Dao.PAY_TRUE) + "元");
//        tv_not_pay.setText("- " + Dao.getBalance(this, Dao.PAY_FALSE) + "元");
        initBalance();
    }

    // 退出
    @Override
    public void onBackPressed() {
        if (firstClickTime <= 0) {
            Toast.makeText(MainActivity.this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            firstClickTime = System.currentTimeMillis();
        } else {
            lastClickTime = System.currentTimeMillis();
            if (lastClickTime - firstClickTime < 1500) {
                super.onBackPressed();// 退出
            } else {
                Toast.makeText(MainActivity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstClickTime = System.currentTimeMillis();
            }
        }
    }
}
