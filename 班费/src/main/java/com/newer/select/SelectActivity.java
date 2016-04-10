package com.newer.select;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.newer.classfare.Person;
import com.newer.classfare.R;
import com.newer.dbdao.Dao;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mr_LUO on 2016/1/31.
 */
public class SelectActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText edt_content;
    private Button btn_select;
    private TextView tv_null, tv_content;
    private ArrayList<Person> list;
    private SelectListAdapter adapter;
    private ListView listView;
    private boolean isSelectName = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        init();
    }

    private void init() {
        ImageView img_back = (ImageView) findViewById(R.id.title_select).findViewById(R.id.img_back);
        ((TextView) findViewById(R.id.title_select).findViewById(R.id.tv_top_title)).setText("班费查询");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        edt_content = (EditText) findViewById(R.id.edt_content);
        btn_select = (Button) findViewById(R.id.btn_select);
        tv_null = (TextView) findViewById(R.id.tv_null);
        tv_content = (TextView) findViewById(R.id.tv_content);
        listView = (ListView) findViewById(R.id.listview);

        img_back.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        btn_select.setOnClickListener(this);
        tv_content.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (list != null) {
            list.clear();
        }
        tv_null.setVisibility(View.GONE);
        if (position == 0) {
            isSelectName = true;
            edt_content.setVisibility(View.VISIBLE);
            edt_content.requestFocus();
            btn_select.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.GONE);
        } else {
            isSelectName = false;
            showDatePickerDialog();
            tv_content.setText("选择日期");
            tv_content.setTextColor(Color.RED);
            edt_content.setVisibility(View.GONE);
            btn_select.setVisibility(View.GONE);
            tv_content.setVisibility(View.VISIBLE);
        }
    }

    //实例化日期选择器
    private void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_select:
                if (edt_content.length() > 0) {
                    getData(edt_content.getText().toString());
                } else {
                    Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_content:
                showDatePickerDialog();
                break;
        }

    }

    //获取数据
    private void getData(String name) {
        if (isSelectName) {
            list = Dao.getAllDetail(this, name, Dao.CODE_NAME);
        } else {
            list = Dao.getAllDetail(this, tv_content.getText().toString() + "%", Dao.CODE_DATE);
        }
        if (list.isEmpty()) {
            tv_null.setVisibility(View.VISIBLE);
        } else {
            tv_null.setVisibility(View.GONE);
        }
        adapter = new SelectListAdapter(this, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PayDetailsActivity.class);
        intent.putExtra("id", list.get(position).getId());
        startActivity(intent);
    }

    //日历
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            tv_content.setText(year + "-" + checkDate(month, day));
            tv_content.setTextColor(Color.BLACK);
            isSelectName = false;
            getData(tv_content.getText().toString());
        }

    }

    //判断日期
    public static String checkDate(int month, int day) {
        StringBuilder date = new StringBuilder();
        month++;//月份从0开始的，所以加一
        if (month < 10) {
            date.append("0").append(month);
        } else {
            date.append(month);
        }
        if (day < 10) {
            date.append("-0").append(day);
        } else {
            date.append("-").append(day);
        }
        return date.toString();
    }

    //获取星期几
    public static String getWeeKOfMonth(int i) {
        switch (i) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 0:
                return "六";
        }
        return null;
    }

    /**
     * 拆分日期，获取年月日
     * @param str 需要拆分的日期
     * @return 月-日 星期几
     */
    public static String splitDate(String str) {
        String date = str.substring(0, str.indexOf(" "));
        String[] dates = date.split("-");
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        //月份从0开始的，这里重新设置了下，所以设置后的月份数会大于之前的，这里减一
        return checkDate(month - 1, day) + " 星期" + getWeeKOfMonth(c.get(Calendar.DAY_OF_WEEK) - 1);
    }
}
