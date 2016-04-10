package com.newer.httpget;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText edt_name;
    private EditText edt_pwd;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_pwd = (EditText) findViewById(R.id.edt_pwd);
        tv_result = (TextView) findViewById(R.id.tv_result);
        findViewById(R.id.btn_get).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String name = edt_name.getText().toString();
        final String pwd = edt_pwd.getText().toString();
        if (v.getId() == R.id.btn_get) {
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... params) {
                    try {
//                        URL url = new URL(params[0] + "?hi=罗");
                        URL url = new URL(params[0]);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setDoOutput(true);//如果此处设置为true，则这个请求变成了post请求，
                        // 这个方法的意思是是否向connection输出，因为post请求参数要放在http正文内，不向get请求样放在URL后面，
                        // 这里设置true了，则下面设置为POST请求与之对应
                        connection.setRequestMethod("POST");
                        connection.connect();
                        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                        BufferedWriter bw = new BufferedWriter(osw);
                        bw.write("name=" + name + "&pass=" + pwd);
                        bw.flush();

                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String result = "";
                        String line;
                        while ((line = br.readLine()) != null) {
                            result += line;
                        }
                        System.out.println(result);
                        return result;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    tv_result.setText(s);
                    super.onPostExecute(s);
                }
            }.execute("http://192.168.191.1:8080/TestAndroidHttp/login.jsp");
        }
    }
}
