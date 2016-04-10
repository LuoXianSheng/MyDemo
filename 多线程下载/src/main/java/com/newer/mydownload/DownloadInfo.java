package com.newer.mydownload;

/**
 * Created by Mr_LUO on 2016/4/5.
 */
public class DownloadInfo {

    private int threadID;//下载的线程id
    private int startPosition;//下载开始位置
    private int endPosition;//下载结束位置
    private int downloadSize;//已经下载的大小
    private String url;//下载资源

    public DownloadInfo(int threadID, int startPosition, int endPosition, int downloadSize, String url) {
        this.threadID = threadID;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.downloadSize = downloadSize;
        this.url = url;
    }

    public int getThreadID() {
        return threadID;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public int getDownloadSize() {
        return downloadSize;
    }

    public String getUrl() {
        return url;
    }
}
