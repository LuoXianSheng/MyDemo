package com.newer.mydownload;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mr_LUO on 2016/4/5.
 */
public class DownloadDao {

    private String filePath;//文件的保存位置
    private int fileSize;//下载文件的大小
    private String url;

    private int threadCount;//线程数量
    private int totalDownloadSize;//每个线程所需要下载的大小
    private int totalSize;//所有线程下载的总大小
    private downloadStatue statue;
    List<DownloadInfo> list;

    private DBDao dbDao;

    private DownloadListener downloadListener;
    private ExecutorService executorService;

    private enum downloadStatue {
        Start, Pause, Reset
    }

    public DownloadDao(String filePath, String url, int fileSize, int threadCount, Context context,
                       int totalSize, DownloadListener downloadListener) {
        this.filePath = filePath;
        this.url = url;
        this.threadCount = threadCount;
        this.downloadListener = downloadListener;
        dbDao = DBDao.getInstance(context);
        this.fileSize = fileSize;
        this.totalSize = totalSize;
        totalDownloadSize = this.fileSize % threadCount == 0 ? this.fileSize / threadCount : this.fileSize / threadCount + 1;
        System.out.println("每个线程下载的总大小：" + totalDownloadSize);
//        //固定长度线程池
//        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void start() {
        statue = downloadStatue.Start;
        list = new ArrayList<>();
        if (dbDao.getDownloadRecord(url)) {//第一次下载
            System.out.println("第一次下载");
            for (int i = 0; i < threadCount - 1; i++) {
                DownloadInfo downloadInfo = new DownloadInfo(i, i * totalDownloadSize,
                        (i + 1) * totalDownloadSize - 1, 0, url);
                list.add(downloadInfo);
            }
            DownloadInfo downloadInfo = new DownloadInfo(threadCount - 1, (threadCount - 1) * totalDownloadSize,
                    fileSize - 1, 0, url);
            list.add(downloadInfo);
            dbDao.saveDownloadinfos(list);//保存下载信息
        } else {
            System.out.println("继续下载");
            list = dbDao.getDownloadInfos(url);
        }
        for (int i = 0; i < threadCount; i++) {
            new Thread(new DownloadThread(i, list.get(i).getStartPosition(), list.get(i).getEndPosition(),
                    list.get(i).getDownloadSize())).start();
//            executorService.submit(new DownloadThread(i, list.get(i).getStartPosition(), list.get(i).getEndPosition(),
//                    list.get(i).getDownloadSize()));
        }

    }

    public void pause() {
        System.out.println("暂停");
        statue = downloadStatue.Pause;
    }

    public void reset() {
        downloadListener.downloadReset();
        dbDao.deleteDownloadInfos(url);
        statue = downloadStatue.Reset;
        totalSize = 0;
        start();
    }

    class DownloadThread implements Runnable {

        private int threadId;
        private int startPos;
        private int endPos;
        private int downloadSize;//已经下载的大小

        public DownloadThread(int threadId, int startPos, int endPos, int downloadSize) {
            this.threadId = threadId;
            this.startPos = startPos;
            this.endPos = endPos;
            this.downloadSize = downloadSize;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Range", "bytes="
                        + (startPos + downloadSize) + "-" + endPos);
                conn.connect();
                InputStream is = conn.getInputStream();
                byte[] b = new byte[2 * 1024];
                int len;
                RandomAccessFile accessFile = new RandomAccessFile(filePath, "rwd");
                accessFile.seek(startPos + downloadSize);
                System.out.println("线程数量：" + Thread.activeCount());
                while ((len = is.read(b)) != -1) {
                    accessFile.write(b, 0, len);
                    downloadSize += len;
                    System.out.println("downloadSize + ：" + "线程ID：" + ":" + downloadSize / 1024 + "KB");
                    totalSize += len;
                    dbDao.updateDownloadinfos(threadId, downloadSize, url);
                    downloadListener.downloadProgress(totalSize);
                    if (totalSize == fileSize) {
                        dbDao.deleteDownloadInfos(url);
                        downloadListener.downloadSuccess();
                    }
                    if (downloadSize >= totalDownloadSize) {
                        break;
                    }
                    if (statue == downloadStatue.Pause || statue == downloadStatue.Reset) {
                        break;
                    }
                }
                is.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface DownloadListener {
        void downloadProgress(int downloadedSize);//记录当前线程下载的大小
        void downloadReset();//重新下载
        void downloadSuccess();//下载成功时
    }
}
