package com.newer.player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private MediaPlayer player;
    private Handler handler;
    private Runnable runnable;
    private ArrayList<Music> list;
    private int position;//当前
    private boolean isPause = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //读取歌曲列表
        list = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.IS_MUSIC;//查询条件，查是音乐文件
        Cursor cursor = getContentResolver().query(uri, projection, selection, null, null);
        if (cursor.moveToFirst()) {
            do {
                Music music = new Music();
                music.setTitle(cursor.getString(0));
                music.setName(cursor.getString(1));
                music.setSpecial(cursor.getString(2));
                music.setMusicPath(cursor.getString(3));
                list.add(music);
            }while (cursor.moveToNext());
        }

        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Intent intent;
                if (msg.what == Config.BACKGROUND_STATUS) {
                    intent = new Intent(Config.BACKGROUND_MUSIC);
                    intent.putExtra("position", position);
                    intent.putExtra("duration", player.getDuration() / 1000);
                    sendBroadcast(intent);
                } else {
                    intent = new Intent(Config.RESUME_ACTION);
                    intent.putExtra("title", list.get(position).getTitle());
                    intent.putExtra("current", player.getCurrentPosition() / 1000);
                    intent.putExtra("max", player.getDuration() / 1000);
                    sendBroadcast(intent);
                }
                intent = new Intent(Config.NOTIFY_INIT_ACTION);
                intent.putExtra("title", list.get(position).getTitle());
                intent.putExtra("name", list.get(position).getName());
                sendBroadcast(intent);
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction(Config.MUSIC_CURRENT);
                intent.putExtra("current", player.getCurrentPosition() / 1000);
                sendBroadcast(intent);
                handler.postDelayed(runnable, 1000);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int status = intent.getIntExtra("status", Config.BACKGROUND_STATUS);
            switch (status) {
                case Config.INIT_STATUS:
                    initMusicList();
                    break;
                case Config.PLAY_STATUS:
                    position = intent.getIntExtra("position", 0);
                    playMusic(list.get(position).getMusicPath());
                    break;
                case Config.PAUSE_STATUS:
                    pauseMusic();
                    break;
                case Config.CONTINUE_STATUS://继续播放
                    int current = intent.getIntExtra("current", 0);
                    if (current != 0) {//判断seekbar是否被拖动
                        player.seekTo(current);
                    }
                    continuePlay();
                    break;
                case Config.BACKGROUND_STATUS:
                    System.out.println("进入到后台动作");
                    if (player != null && player.isPlaying()) {
                        System.out.println("后台播放中。、");
                        handler.sendEmptyMessageDelayed(Config.BACKGROUND_STATUS, 20);
                    }
                    if (isPause)
                        handler.sendEmptyMessage(Config.PAUSE_STATUS);
                    break;
                case Config.NEXT_STATUS:
                    nextMusic();
                    break;
            }
        } else {
            System.out.println("intent为空");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //继续播放
    private void continuePlay() {
        player.start();
        handler.post(runnable);
        sendBroadcast(new Intent(Config.CONTINUE_ACTION));
        sendBroadcast(new Intent(Config.NOTIFY_CONTINUE_ACTION));
    }

    //下一曲
    private void nextMusic() {
        if (position != list.size() - 1) {
            playMusic(list.get(++position).getMusicPath());
        } else {
            playMusic(list.get(0).getMusicPath());
        }
    }

    private void initMusicList() {
        Intent intent = new Intent();
        intent.setAction(Config.INIT_ACTION);
        intent.putExtra("list", list);
        sendBroadcast(intent);
        System.out.println("初始化完成");
    }

    //暂停
    private void pauseMusic() {
        if (player != null && player.isPlaying()) {
            handler.removeCallbacks(runnable);
            player.pause();
            Intent intent = new Intent();
            intent.setAction(Config.PAUSE_ACTION);
            sendBroadcast(intent);
            intent = new Intent(Config.NOTIFY_PAUSE_ACTION);
            sendBroadcast(intent);
            isPause = true;
        }
    }

    //播放
    private void playMusic(String path) {
        try {
            player.reset();
            handler.removeCallbacks(runnable);
            player.setDataSource(path);
            player.prepare();
            player.start();
            Intent intent = new Intent();
            intent.setAction(Config.MUSIC_DURATION);
            intent.putExtra("duration", player.getDuration() / 1000);
            intent.putExtra("position", position);
            sendBroadcast(intent);
            sendBroadcast(new Intent(Config.PLAY_ACTION));

            intent = new Intent(Config.NOTIFY_UPDATE_ACTION);
            intent.putExtra("title", list.get(position).getTitle());
            intent.putExtra("name", list.get(position).getName());
            sendBroadcast(intent);
            handler.post(runnable);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (position != list.size() - 1) {
            position++;
        } else {
            position = 0;
        }
        playMusic(list.get(position).getMusicPath());
        Intent intent = new Intent(Config.COMPLETION_ACTION);
        intent.putExtra("position", position);
        sendBroadcast(intent);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        player.release();
        return true;
    }
}
