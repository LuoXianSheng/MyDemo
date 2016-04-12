package com.newer.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private Request request;
    private String url = "http://www.tuling123.com/openapi/api?key=13989d3de128550986294b47d0d5f5bc&info=长沙天气";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        okHttpClient = new OkHttpClient();
    }

    @Event(value = R.id.btnHttpGet, type = View.OnClickListener.class)
    private void httpGetClick(View view) {
        request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(App.TAG, "请求失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                InputStream is = response.body().byteStream();
                String result = response.body().string();
                Log.e(App.TAG, result);
            }
        });
    }
}
