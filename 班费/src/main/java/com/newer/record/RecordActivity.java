package com.newer.record;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.newer.classfare.R;
import com.newer.dbdao.Dao;

public class RecordActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private ImageView img_record_back, img_record_reset;
    private EditText edt_money, edt_name, edt_remark;
    private RadioGroup radioGroup1, radioGroup2;
    private RadioButton rb_1, rb_3, rb_4;
    private Button btn_add;
    private LinearLayout layout1, layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        init();
    }

    private void init() {
        img_record_back = (ImageView) findViewById(R.id.img_record_back);
        img_record_reset = (ImageView) findViewById(R.id.img_record_reset);
        edt_money = (EditText) findViewById(R.id.edt_money);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_remark = (EditText) findViewById(R.id.edt_remark);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rb_1 = (RadioButton) findViewById(R.id.rb_1);
        rb_3 = (RadioButton) findViewById(R.id.rb_3);
        rb_4 = (RadioButton) findViewById(R.id.rb_4);
        btn_add = (Button) findViewById(R.id.btn_add);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);

        img_record_back.setOnClickListener(this);
        img_record_reset.setOnClickListener(this);
        radioGroup1.setOnCheckedChangeListener(this);
        radioGroup2.setOnCheckedChangeListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_1) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
        } else if (checkedId == R.id.rb_2) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_record_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.img_record_reset:
                edt_money.setText("");
                rb_1.setChecked(true);
                edt_name.setText("");
                edt_name.setVisibility(View.VISIBLE);
                edt_remark.setText("");
                rb_3.setChecked(true);
                layout2.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_add:
                add();
                break;
        }

    }

    private void add() {
        if (check()) {
            String pay = Dao.PAY_TRUE;
            if (rb_4.isChecked()) {//判断是否缴费
                pay = Dao.PAY_FALSE;
            }
            if (rb_1.isChecked()) {
                Dao.insert(this, edt_name.getText().toString(),
                        Dao.TYPE_IN, edt_money.getText().toString(), pay, edt_remark.getText().toString());
            } else {
                Dao.insert(this, "班级消费", Dao.TYPE_OUT, edt_money.getText().toString(),
                        "-", edt_remark.getText().toString());
            }
            Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
            edt_name.setText("");
            edt_name.requestFocus();
        }
    }

    //检测数据有效性
    private boolean check() {
        if (edt_money.getText().length() <= 0) {
            Toast.makeText(this, "金额不能为空！", Toast.LENGTH_SHORT).show();
            return  false;
        } else {
            try {
                Integer.parseInt(edt_money.getText().toString());
            }catch (Exception e) {
                Toast.makeText(this, "不能包含数字以外的字符！", Toast.LENGTH_SHORT).show();
            }
        }
        if (layout1.getVisibility() == View.VISIBLE && edt_name.getText().length() <= 0) {
            Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
