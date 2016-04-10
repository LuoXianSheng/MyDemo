package com.newer.robot;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mr_LUO on 2016/3/15.
 */
public class DataAsynTask extends AsyncTask<String, Void, String> {

    private String content;
    private DataAsynTaskListener listener;

    public DataAsynTask(DataAsynTaskListener listener, String content) {
        this.listener = listener;
        this.content = content;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection conn;
        OutputStreamWriter osw;
        try {
            conn = (HttpURLConnection) new URL(params[0]).openConnection();
            conn.setRequestMethod("POST");
            conn.connect();
            osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write("key=13989d3de128550986294b47d0d5f5bc&info=" + content);
            osw.flush();
            osw.close();

            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String data) {
        listener.getHttpData(data);
    }
}
