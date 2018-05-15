package com.tyland.musicbox;

import com.tyland.musicbox.model.Music;

import java.util.List;

/**
 * Created by tyland on 2018/4/27.
 */
public interface IMusicService {
    /**
     * 播放
     */
    void play();

    /**
     * 播放指定歌曲
     */
    void play(Music m);

    /**
     * 播放当前列表指定歌曲
     *
     * @param index
     */
    void playAtIndex(int index);

    /**
     * 获取当前播放状态
     */
    int getPlayState();

    /**
     * 下一曲
     */
    void next();

    /**
     * 上一曲
     */
    void previous();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止
     */
    void stop();

    /**
     * 获得当前播放音乐
     */
    Music getCurrentMusic();

    /**
     * 获得当前歌曲播放进度
     */
    int getCurrentPlayPosition();

    /**
     * 获得当前播放队列
     */
    List<Music> getCurrentQueue();

    /**
     * 获得当前播放队列歌曲总数
     */
    int getCurrentQueueCount();

    /**
     * 设置当前播放位置
     */
    void setCurrentIndex(int index);

    /**
     * 设置播放进度
     */
    void setProgress(int msec);

    /**
     * 获得当前播放模式
     */
    int getPlayMethod();

    /**
     * 设置播放模式
     */
    void setPlayMethod(int method);

    /**
     * 获得随机播放位置
     */
    int getRandomIndex();

    /**
     * 添加到播放队列
     * */
    void addToPlayQueue(Music m);

    /**
     * 添加到播放队列
     * */
    void addToPlayQueue(List<Music> musicList);

    /**
     * 从队列中移除
     * */
    void removeFromPlayQueue(int index);

    /**
     * 清空队列
     * */
    void clearPlayQueue();

    /**
     * 随机播放
     * */
    void shuffle();

    /**
     * 循环播放
     * */
    void repeat();

    /**
     * 顺序播放
     * */
    void queue();

    /**
     * 单曲循环
     * */
    void current();

}
