package com.newer.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.edt)
    private EditText edt;

    @ViewInject(R.id.result)
    private TextView result;

    private OkHttpClient okHttpClient;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);//注入view和事件

//        save();
        find();

        EventBus.getDefault().register(this);//注册扫描器

        okHttpClient = new OkHttpClient();
    }

    private void find() {
        try {
            List<Student> list = x.getDb(App.getDaoConfig()).findAll(Student.class);
            StringBuilder sb = new StringBuilder();
            for (Student s : list) {
                sb.append(s.getId() + ". " + s.getName() + " : " + s.getAge() + "\n");
            }
            result.setText(sb.toString());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        Student student = new Student();
        student.setName("张三");
        student.setAge(21);
        try {
            x.getDb(App.getDaoConfig()).save(student);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Event(R.id.post)
    private void postEvent(View v) {
        EventBus.getDefault().post("普通事件");
    }

    @Event(value = R.id.btnHttpGet, type = View.OnClickListener.class)
    private void httpGetClick(View view) {//方法修饰符必须private
        String url = "http://www.tuling123.com/openapi/api" +
                "?key=13989d3de128550986294b47d0d5f5bc&info=" + edt.getText().toString();
        request = new Request.Builder()
                .url(url)
                .build();
//        okHttpClient.newCall(request).execute();//不是异步，要在子线程中请求做网络请求
        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(App.TAG, "请求失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                InputStream is = response.body().byteStream();
                //这个是在子线程运行
                String result = response.body().string();
                Log.e(App.TAG, "postEvent---" + Thread.currentThread().getName());
                Log.e(App.TAG, result);
                EventBus.getDefault().post(result);
            }
        });
    }

    @Event(R.id.btnHttpPost)
    private void httpPostClick(View v) {
//        okhttp3.FormBody instead of FormEncodingBuilder.（OkHttp3.x，FormEncodingBuilder已被FormBody取代）
        FormBody body = new FormBody.Builder()
                .add("key", "13989d3de128550986294b47d0d5f5bc")
                .add("info", edt.getText().toString())
                .build();
        request = new Request.Builder()
                .url("http://www.tuling123.com/openapi/api")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(App.TAG, "请求失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(App.TAG, "postEvent---" + Thread.currentThread().getName());
                Log.e(App.TAG, result);
                EventBus.getDefault().post(result);
            }
        });
    }

    @Event(R.id.btnHttpUpload)
    private void httpUploadClick(View v) {
        String url = "http://192.168.191.1:8080/sun100/fileupload";
        String filePath = Environment.getExternalStorageDirectory() + "/1.mp3";
        File file = new File(filePath);
        RequestBody fileBody = RequestBody.create(
                MediaType.parse("application/octet-stream"),
                file);
        RequestBody requestBody = new MultipartBody.Builder()
                .addPart(fileBody)
                .build();
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(App.TAG, "文件上传失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                EventBus.getDefault().post(result);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void a(String result) {
        this.result.setText(edt.getText().toString() + "\n" + result);
        edt.setText("");
        Log.e(App.TAG, "onEventMainThread---" + Thread.currentThread().getName());
    }
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void b(String result) {
        Log.e(App.TAG, "onEventPostThread---" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void c(String result) {
        Log.e(App.TAG, "onEventBackgroundThread---" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void d(String result) {
        Log.e(App.TAG, "onEventAsync---" + Thread.currentThread().getName());
    }

//    public void onEventPostThread(String result) {
//        Log.e(App.TAG, "onEventMainThread---" + Thread.currentThread().getName());
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
