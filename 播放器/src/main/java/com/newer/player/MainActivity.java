package com.newer.player;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnTouchListener,
        AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener {

    private ListView musicList;
    private ImageView musicImg, play, next;
    private TextView musicTitle;
    private SeekBar seekBar;
    private ArrayList<Music> list;
    protected static ListAdapter adapter;
    private static boolean isPlaying = false;//播放器播放状态
    private MusicReceiver receiver;
    protected static int position;//当前歌曲
    public static ArrayList<Integer> changeItemStatus;//标记当前播放歌曲的曲目
    private static Notification notification;
    private static NotificationManager manager;
    private static RemoteViews views;
    static NotificationCompat.Builder builder;
    private static boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isDestroy = false;
        initMusicReceiver();//注册广播
        initNotification();
        Intent intent = new Intent(this, PlayerService.class);
        intent.putExtra("status", Config.INIT_STATUS);
        startService(intent);
        startService(new Intent(this, PlayerService.class));
        initView();
    }

    private void initNotification() {
        builder = new NotificationCompat.Builder(this);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        views = new RemoteViews(getPackageName(), R.layout.notification);
        views.setTextViewText(R.id.tvTitle, "今天你要嫁给我");
        views.setTextViewText(R.id.tvName, "Mr.Dj");
        Intent downI = new Intent();
        downI.setAction(Config.NOTIFY_NEXT_ACTION);
        Intent openI = new Intent(this, MainActivity.class);//通知栏跳转
        PendingIntent openAty = PendingIntent.getActivity(this, Config.NOTIFY_ID,
                openI, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent downIntent = PendingIntent.getBroadcast(this, Config.NEXT_CODE,
                downI, PendingIntent.FLAG_CANCEL_CURRENT);//下一曲
//18874847856
        Intent playI = new Intent();
        playI.setAction(Config.NOTIFY_PLAY_ACTION);
        PendingIntent playIntent = PendingIntent.getBroadcast(this, Config.PLAY_CODE,
                playI, PendingIntent.FLAG_CANCEL_CURRENT);//播放
        views.setOnClickPendingIntent(R.id.imgPlay, playIntent);
        views.setOnClickPendingIntent(R.id.imgNext, downIntent);
        Intent deleteI = new Intent(Config.NOTIFY_DELETE_ACTION);
        PendingIntent deleteIntent = PendingIntent.getBroadcast(this, Config.DELETE_CODE,
                deleteI, PendingIntent.FLAG_CANCEL_CURRENT);
        notification = builder.setContentTitle("播放器")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.qq_aio_record_play_05))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(views)
                .setContentIntent(openAty)
                .setDeleteIntent(deleteIntent)
                .setOngoing(false)
                .build();
        manager.notify(Config.NOTIFY_ID, notification);

    }

    private void initMusicReceiver() {
        receiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.INIT_ACTION);
        filter.addAction(Config.MUSIC_CURRENT);//当前
        filter.addAction(Config.MUSIC_DURATION);//设置总时间
        filter.addAction(Config.BACKGROUND_MUSIC);//后台播放
        filter.addAction(Config.COMPLETION_ACTION);//播放完
        filter.addAction(Config.PAUSE_ACTION);
        filter.addAction(Config.PLAY_ACTION);
        filter.addAction(Config.CONTINUE_ACTION);
        filter.addAction(Config.RESUME_ACTION);
        registerReceiver(receiver, filter);
    }

    private void initView() {
        musicList = (ListView) findViewById(R.id.musicList);
        musicImg = (ImageView) findViewById(R.id.musicImg);
        play = (ImageView) findViewById(R.id.play);
        next = (ImageView) findViewById(R.id.next);
        musicTitle = (TextView) findViewById(R.id.musicTitle);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        musicList.setOnItemClickListener(this);
        play.setOnTouchListener(this);
        next.setOnTouchListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.position != position) {
            playMusic(position);
        }
    }

    //播放
    public void playMusic(int position) {
        Intent intent = new Intent(this, PlayerService.class);
        intent.putExtra("status", Config.PLAY_STATUS);
        intent.putExtra("position", position);
        startService(intent);//播放音乐
        play.setImageResource(R.drawable.playing);
        isPlaying = true;
        setItemGone(this.position);//隐藏上一首的标记
        setItemVisibility(position);//显示当前这一首的标记
        this.position = position;
    }

    //暂停
    public void pauseMusic(int status) {
        Intent intent = new Intent(this, PlayerService.class);
        intent.putExtra("status", status);
        startService(intent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            seekBar.setProgress(progress);
            Intent intent = new Intent(this, PlayerService.class);
            intent.putExtra("status", Config.CONTINUE_STATUS);
            intent.putExtra("current", progress * 1000);
            startService(intent);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        pauseMusic(Config.PAUSE_STATUS);//拖动的开始时暂停播放
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int res = 0;
        if (v.getId() == R.id.play) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isPlaying) {
                        res = R.drawable.playing_pressed;
                    } else {
                        res = R.drawable.play_pressed;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isPlaying) {
                        res = R.drawable.play;
                        isPlaying = false;
                        pauseMusic(Config.PAUSE_STATUS);
                        setItemGone(position);
                    } else {
                        if (!TextUtils.isEmpty(musicTitle.getText())) {//首次打开软件没有歌的情况
                            isPlaying = true;
                            pauseMusic(Config.CONTINUE_STATUS);
                        } else {
                            playMusic(0);
                        }
                        res = R.drawable.playing;
                        setItemVisibility(position);
                    }
                    break;
            }
            play.setImageResource(res);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    res = R.drawable.next_press;
                    break;
                case MotionEvent.ACTION_UP:
                    res = R.drawable.next;
                    if (TextUtils.isEmpty(musicTitle.getText())) {
                        isPlaying = true;
                        playMusic(0);
                    } else {
                        nextMusic();
                    }
                    break;
            }
            next.setImageResource(res);
        }
        return true;
    }

    private void nextMusic() {
        Intent intent = new Intent(this, PlayerService.class);
        intent.putExtra("status", Config.NEXT_STATUS);
        startService(intent);
        if (position != list.size() - 1) {
            setItemGone(position);
            position++;
            setItemVisibility(position);
        } else {
            position = 0;
        }
    }

    //把标记曲目的标记隐藏
    private static void setItemGone(int position) {
        changeItemStatus.remove(position);
        changeItemStatus.add(position, Config.GONE);
        adapter.notifyDataSetChanged();
    }

    private static void setItemVisibility(int position) {
        if (position == 0) {
            changeItemStatus.remove(position);
            changeItemStatus.add(position, Config.GONE);
            adapter.notifyDataSetChanged();
        }
        changeItemStatus.remove(position);
        changeItemStatus.add(position, Config.VISIBILITY);
        adapter.notifyDataSetChanged();
    }

    //设置通知栏在没有播放的情况下可以清除
    public static void clearNotify(boolean isClear) {
        notification = builder.setOngoing(!isClear).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;//Aty被销毁
        System.out.println("销毁");
        if (!isPlaying) {
            stopService(new Intent(this, PlayerService.class));
            manager.cancel(Config.NOTIFY_ID);
            System.out.println("关闭服务");
        } else {
            System.out.println("后台");
        }
        unregisterReceiver(receiver);
        System.out.println("注销广播");
    }

    private static boolean isFirst = true;

    /**
     * 通知栏广播处理类
     */
    public static class NotifyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Intent i = new Intent(context, PlayerService.class);
            switch (action) {
                case Config.NOTIFY_NEXT_ACTION:
                    isPlaying = true;
                    setItemGone(position);
                    i.putExtra("status", Config.NEXT_STATUS);
                    clearNotify(false);
                    context.startService(i);
                    break;
                case Config.NOTIFY_PLAY_ACTION:
                    if (isPlaying) {
                        clearNotify(true);
                        i.putExtra("status", Config.PAUSE_STATUS);
                    } else {
                        if (isFirst) {
                            isFirst = false;
                            i.putExtra("status", Config.PLAY_STATUS);
                            clearNotify(false);

                        } else {
                            i.putExtra("status", Config.CONTINUE_STATUS);
                            clearNotify(false);
                        }
                    }
                    context.startService(i);
                    break;
                case Config.NOTIFY_PAUSE_ACTION:
                    isPlaying = false;
                    notification.contentView = views;
                    views.setImageViewResource(R.id.imgPlay, R.drawable.play);
                    manager.notify(Config.NOTIFY_ID, notification);
                    break;
                case Config.NOTIFY_UPDATE_ACTION:
                    updateNotify(intent.getStringExtra("title"), intent.getStringExtra("name"));
                    break;
                case Config.NOTIFY_INIT_ACTION:
                    updateNotify(intent.getStringExtra("title"), intent.getStringExtra("name"));
                    break;
                case Config.NOTIFY_CONTINUE_ACTION:
                    isPlaying = true;
                    notification.contentView = views;
                    views.setImageViewResource(R.id.imgPlay, R.drawable.playing);
                    manager.notify(Config.NOTIFY_ID, notification);
                    break;
                case Config.NOTIFY_DELETE_ACTION://监听用户清除通知栏，停止服务
                    if (isDestroy)//Aty被销毁了才停止服务
                        context.stopService(new Intent(context, PlayerService.class));
                    break;
            }

        }

        private void updateNotify(String title, String name) {
            notification.contentView = views;
            views.setTextViewText(R.id.tvTitle, title);
            views.setTextViewText(R.id.tvName, name);
            if (isPlaying) {
                views.setImageViewResource(R.id.imgPlay, R.drawable.playing);
            }
            manager.notify(Config.NOTIFY_ID, notification);
        }
    }

    /**
     * Aty广播处理类
     */
    public class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Config.INIT_ACTION:
                    list = new ArrayList<>();
                    changeItemStatus = new ArrayList<>();
                    list = (ArrayList<Music>) intent.getSerializableExtra("list");
                    for (int i = 0; i < list.size(); i++) {
                        changeItemStatus.add(Config.GONE);
                    }
                    adapter = new ListAdapter(context, list);
                    musicList.setAdapter(adapter);
                    break;
                case Config.MUSIC_CURRENT:
                    seekBar.setProgress(intent.getIntExtra("current", 0));
                    break;
                case Config.MUSIC_DURATION:
                    position = intent.getIntExtra("position", 0);
                    musicTitle.setText(list.get(position).getTitle());
                    seekBar.setMax(intent.getIntExtra("duration", 0));
                    break;
                case Config.COMPLETION_ACTION:
                    position = intent.getIntExtra("position", 0);
                    if (position != 0) {
                        setItemGone(position - 1);
                    } else {
                        setItemGone(list.size() - 1);
                    }
                    musicTitle.setText(list.get(position).getTitle());
                    play.setImageResource(R.drawable.playing);
                    isPlaying = true;
                    break;
                case Config.BACKGROUND_MUSIC:
                    position = intent.getIntExtra("position", 0);
                    setItemVisibility(position);
                    seekBar.setMax(intent.getIntExtra("duration", 0));
                    isPlaying = true;
                    musicTitle.setText(list.get(position).getTitle());
                    play.setImageResource(R.drawable.playing);
                    clearNotify(false
                    );
                    break;
                case Config.PAUSE_ACTION:
                    clearNotify(true);
                    isPlaying = false;
                    play.setImageResource(R.drawable.play);
                    setItemGone(position);
                    break;
                case Config.PLAY_ACTION:
                    clearNotify(false);
                    isPlaying = true;
                    setItemVisibility(position);
                    play.setImageResource(R.drawable.playing);
                    break;
                case Config.CONTINUE_ACTION:
                    clearNotify(false);
                    isPlaying = true;
                    play.setImageResource(R.drawable.playing);
                    setItemVisibility(position);
                    break;
                case Config.RESUME_ACTION:
                    musicTitle.setText(intent.getStringExtra("title"));
                    seekBar.setMax(intent.getIntExtra("max", 0));
                    seekBar.setProgress(intent.getIntExtra("current", 0));
                    isPlaying = false;
                    play.setImageResource(R.drawable.play);
                    break;
            }
        }
    }
}
