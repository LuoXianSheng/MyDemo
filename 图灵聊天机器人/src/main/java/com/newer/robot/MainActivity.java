package com.newer.robot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataAsynTaskListener, View.OnClickListener {

    public static final int TEXT_TYPE = 100000;
    public static final int URL_TYPE = 200000;
    public static final int NEWS_TYPE = 302000;
    public static final int MENU_TYPE = 308000;

    private ArrayList<ListData> list;
    private ListView listView;
    private EditText edt_content;
    private Button btn_send;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
        edt_content = (EditText) findViewById(R.id.edt_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        list = new ArrayList<>();
        btn_send.setOnClickListener(this);
        adapter = new ListAdapter(this, list);
        listView.setAdapter(adapter);
    }

    public void parseCode(String data) {
        ListData item = null;
        try {
            JSONObject object = new JSONObject(data);
            switch (object.getInt("code")) {
                case TEXT_TYPE:
                    item = new ListData(ListData.LEFT, TEXT_TYPE, object.getString("text"),
                            null, null, null);

                    break;
                case URL_TYPE:

                    break;
                case NEWS_TYPE:
                    JSONArray array = new JSONArray(object.getString("list"));
                    ArrayList<News> newses = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        News news = new News();
                        news.setArticle(o.getString("article"));
                        news.setSource(o.getString("source"));
                        news.setDetailurl(o.getString("detailurl"));
                        newses.add(news);
                    }
                    item = new ListData(ListData.LEFT, NEWS_TYPE, object.getString("text"), null, newses, null);
                    break;
                case MENU_TYPE:
                    JSONArray array2 = new JSONArray(object.getString("list"));
                    ArrayList<Menus> menuses = new ArrayList<>();
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject o = array2.getJSONObject(i);
                        Menus menus = new Menus();
                        menus.setName(o.getString("name"));
                        menus.setIcon(o.getString("icon"));
                        menus.setInfo(o.getString("info"));
                        menus.setDetailurl(o.getString("detailurl"));
                        menuses.add(menus);
                    }
                    item = new ListData(ListData.LEFT, NEWS_TYPE, object.getString("text"), null, null, menuses);
                    break;
            }
            list.add(item);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getHttpData(String data) {
        parseCode(data);
    }

    @Override
    public void onClick(View v) {
        ListData item = new ListData(ListData.RIGET, TEXT_TYPE, edt_content.getText().toString(), null, null, null);
        list.add(item);
        adapter.notifyDataSetChanged();
        DataAsynTask task = new DataAsynTask(this, edt_content.getText().toString());
        task.execute("http://www.tuling123.com/openapi/api");
        edt_content.setText("");
    }
}
