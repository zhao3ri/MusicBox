package com.tyland.musicbox.service;

/**
 * 播放模式
 * Created by tyland on 2018/4/27.
 */
public interface PlayMethod {
    /**
     * 循环播放
     */
    int REPEAT = 0x0;

    /**
     * 随机播放
     */
    int SHUFFLE = 0x1;

    /**
     * 单曲循环
     */
    int CURRENT = 0x2;

    /**
     * 顺序播放
     */
    int QUEUE = 0x3;
}
