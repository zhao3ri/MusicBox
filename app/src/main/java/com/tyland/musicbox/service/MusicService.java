package com.tyland.musicbox.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tyland.musicbox.ActionConstant;
import com.tyland.musicbox.IMusicService;
import com.tyland.musicbox.data.MusicDataAccess;
import com.tyland.musicbox.data.MusicQueueHelper;
import com.tyland.musicbox.model.Music;
import com.tyland.musicbox.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyland on 2018/4/27.
 */
public class MusicService extends Service {
    private MusicBinder mBinder;
    private MediaPlayer mMediaPlayer;
    private int mPlayState = PlayState.STOP;
    private int mPlayMethod = PlayMethod.QUEUE;
    private int mCurrentIndex;
    private List<Music> mCurrentQueue;//音乐播放列表
    private Music playingMusic;
    private MusicDataAccess musicAccess;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new MusicBinder();
        musicAccess = new MusicDataAccess(getApplicationContext());
        mCurrentQueue = MusicQueueHelper.getQueueMusic(getApplicationContext());
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//歌曲播放前调用

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mPlayState = PlayState.PLAYING;// 状态改为播放
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// 歌曲结束后调用

            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (mPlayMethod) {
                    case PlayMethod.REPEAT:
                        mBinder.repeat();
                        break;
                    case PlayMethod.SHUFFLE:
                        mBinder.shuffle();
                        break;
                    case PlayMethod.CURRENT:
                        mBinder.current();
                        break;
                    case PlayMethod.QUEUE:
                        mBinder.queue();
                        break;
                }
                sendBroadcast(ActionConstant.ACTION_MUSIC_CHANGE);
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public MediaPlayer getPlayer() {
        return mMediaPlayer;
    }

    /**
     * 播放器实现
     */
    private class MusicBinder extends Binder implements IMusicService {

        @Override
        public void play() {
            if (mPlayState == PlayState.PAUSE) {//暂停状态
                mMediaPlayer.start();//继续播放
                mPlayState = PlayState.PLAYING;
                sendBroadcast(ActionConstant.ACTION_STATE_CHANGE);
            } else if (mPlayState == PlayState.STOP || mPlayState == PlayState.PLAYING) {
                playAtIndex(mCurrentIndex);
            }
        }

        @Override
        public void play(Music m) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mPlayState = PlayState.STOP;
            if (m == null) {
                Log.e("current music is null.");
                return;
            }

            try {
                mMediaPlayer.setDataSource(m.getData());
                mMediaPlayer.prepareAsync();
                mPlayState = PlayState.PLAYING;
                if (playingMusic == null || !playingMusic.equals(m)) {
                    playingMusic = m;
                    Log.d("播放" + playingMusic.getTitle());
                    sendBroadcast(ActionConstant.ACTION_MUSIC_CHANGE);
                }
                sendBroadcast(ActionConstant.ACTION_STATE_CHANGE);
            } catch (IOException e) {
                Log.e("找不到" + m.getData());
            }

        }

        @Override
        public void playAtIndex(int index) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mPlayState = PlayState.STOP;

            Music currMusic = null;
            if (mCurrentQueue != null && mCurrentQueue.size() > 0) {
                currMusic = mCurrentQueue.get(index);
                mCurrentIndex = index;
                play(currMusic);
            }
        }

        @Override
        public int getPlayState() {
            return mPlayState;
        }

        @Override
        public void next() {
            if (mCurrentQueue == null || mCurrentQueue.size() == 0) {
                Log.e("current queue is null");
                return;
            }
            if (mCurrentIndex < mCurrentQueue.size() - 1) {
                mCurrentIndex++;
            } else {
                mCurrentIndex = 0;
            }

            playAtIndex(mCurrentIndex);
        }

        @Override
        public void previous() {
            if (mCurrentQueue == null || mCurrentQueue.size() == 0) {
                Log.e("current queue is null");
                return;
            }
            if (mCurrentIndex >= 1) {
                mCurrentIndex--;
            } else {
                mCurrentIndex = mCurrentQueue.size() - 1;
            }
            playAtIndex(mCurrentIndex);
        }

        @Override
        public void pause() {
            if (mPlayState == PlayState.PLAYING) {
                mMediaPlayer.pause();
                mPlayState = PlayState.PAUSE;
            }
            sendBroadcast(ActionConstant.ACTION_STATE_CHANGE);
        }

        @Override
        public void stop() {
            mMediaPlayer.stop();
            mPlayState = PlayState.STOP;
            sendBroadcast(ActionConstant.ACTION_STATE_CHANGE);
        }

        @Override
        public Music getCurrentMusic() {
            return mCurrentQueue == null || mCurrentQueue.size() == 0 ? null : mCurrentQueue.get(mCurrentIndex);
        }

        @Override
        public int getCurrentPlayPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        @Override
        public List<Music> getCurrentQueue() {
            return mCurrentQueue;
        }

        @Override
        public int getCurrentQueueCount() {
            return mCurrentQueue == null ? 0 : mCurrentQueue.size();
        }

        @Override
        public void setCurrentIndex(int index) {
            mCurrentIndex = index;
        }

        @Override
        public void setProgress(int msec) {
            mMediaPlayer.seekTo(msec);
        }

        @Override
        public int getPlayMethod() {
            return mPlayMethod;
        }

        @Override
        public void setPlayMethod(int method) {
            mPlayMethod = method;
        }

        @Override
        public int getRandomIndex() {
            int index;
            if (mCurrentQueue.size() > 1) {
                do {
                    index = (int) (Math.random() * mCurrentQueue.size());
                } while (index == mCurrentIndex);
            } else {
                index = mCurrentIndex;
            }
            return index;
        }

        @Override
        public void addToPlayQueue(Music m) {
            mCurrentQueue.add(m);
            MusicQueueHelper.addQueueMusic(getApplicationContext(), m);
            sendBroadcast(ActionConstant.ACTION_QUEUE_CHANGE);
        }

        @Override
        public void addToPlayQueue(List<Music> musicList) {
            for (Music m : musicList) {
                mCurrentQueue.add(m);
                MusicQueueHelper.addQueueMusic(getApplicationContext(), m);
            }
            sendBroadcast(ActionConstant.ACTION_QUEUE_CHANGE);
        }

        @Override
        public void removeFromPlayQueue(int index) {
            if (index == mCurrentIndex) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mPlayState = PlayState.STOP;
            } else if (index < mCurrentIndex) {
                setCurrentIndex(mCurrentIndex - 1);
            }

            MusicQueueHelper.removeQueueMusic(getApplicationContext(), mCurrentQueue.get(index));
            mCurrentQueue.remove(index);
            sendBroadcast(ActionConstant.ACTION_QUEUE_CHANGE);
        }

        @Override
        public void clearPlayQueue() {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mPlayState = PlayState.STOP;

            MusicQueueHelper.clearQueueMusic(getApplicationContext(), mCurrentQueue);
            mCurrentQueue.clear();
            sendBroadcast(ActionConstant.ACTION_QUEUE_CHANGE);
        }

        @Override
        public void shuffle() {
            mCurrentIndex = getRandomIndex();
            playAtIndex(mCurrentIndex);
        }

        @Override
        public void repeat() {
            next();
        }

        @Override
        public void queue() {
            if (mCurrentIndex < mCurrentQueue.size() - 1) {
                mCurrentIndex++;
                playAtIndex(mCurrentIndex);
            } else {
                stop();
            }
        }

        @Override
        public void current() {
            playAtIndex(mCurrentIndex);
        }

    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
//        unregisterReceiver(mReceiver);
        mBinder = null;
        playingMusic = null;
        super.onDestroy();
    }
}
