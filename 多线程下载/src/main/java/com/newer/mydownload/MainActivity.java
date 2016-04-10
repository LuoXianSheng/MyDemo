package com.newer.mydownload;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private ProgressBar progressBar;
    private Button btnStart, btnPause;
//    private String url = "http://192.168.191.1:8080/sun100/images/飘花电影piaohua.com帝国双璧BD1280高清中英双字.rmvb";
    private String url = "http://192.168.191.1:8080/sun100/images/1.mp3";
    private int fileSize;
    private String filePath;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tvResult.setText(msg.arg1 * 100 / fileSize + " %");
                    progressBar.setProgress(msg.arg1);
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "文件下载完成！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    tvResult.setText("0 %");
                    break;
                case 4:
                    downloadDao = new DownloadDao(filePath, url, fileSize, 1, MainActivity.this, totalSize,
                            new DownloadDao.DownloadListener() {

                        @Override
                        public void downloadProgress(int downloadedSize) {
                            Message message = new Message();
                            message.what = 1;
                            message.arg1 = downloadedSize;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void downloadReset() {
                            progressBar.setProgress(0);
                            handler.sendEmptyMessage(3);
                        }

                        @Override
                        public void downloadSuccess() {
                            handler.sendEmptyMessage(2);
                        }
                    });
                    progressBar.setMax(fileSize);
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = totalSize;
                    handler.sendMessage(message);
                    System.out.println("初始化完成");
                    break;
            }
        }
    };

    private DownloadDao downloadDao;
    private int totalSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();//开启一个线程去服务器获取需要下载文件的大小
        initView();
        filePath = Environment.getExternalStorageDirectory() + "/" + url.substring(url.lastIndexOf("/") + 1);
        DBDao dbDao = DBDao.getInstance(MainActivity.this);
        List<DownloadInfo> list = dbDao.getDownloadInfos(url);
        //获取所有线程的总下载大小
        for (DownloadInfo downloadInfo : list) {
            totalSize += downloadInfo.getDownloadSize();
        }
    }

    private void initView() {
        tvResult = (TextView) findViewById(R.id.tvResult);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnStart = (Button) findViewById(R.id.start);
        btnPause = (Button) findViewById(R.id.pause);
        btnPause.setClickable(false);
    }

    private void init() {
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    System.out.println("开始获取文件大小");
                    fileSize = conn.getContentLength();
                    System.out.println("获得文件大小为：" + fileSize);
                    File newFile = new File(filePath);
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    }
                    RandomAccessFile accessFile = new RandomAccessFile(newFile, "rwd");
                    accessFile.setLength(fileSize);
                    accessFile.close();
                    conn.disconnect();
                    handler.sendEmptyMessage(4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void start(View v) {
        downloadDao.start();
        btnStart.setClickable(false);
        btnPause.setClickable(true);
    }

    public void pause(View v) {
        downloadDao.pause();
        btnStart.setClickable(true);
        btnPause.setClickable(false);
    }

    public void reset(View v) {
        downloadDao.reset();
        btnStart.setClickable(false);
        btnPause.setClickable(true);
    }

}