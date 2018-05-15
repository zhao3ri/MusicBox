package com.tyland.musicbox.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.tyland.musicbox.IMusicService;
import com.tyland.musicbox.MusicApplication;
import com.tyland.musicbox.R;
import com.tyland.musicbox.ActionConstant;
import com.tyland.musicbox.data.AlbumDataAccess;
import com.tyland.musicbox.model.Album;
import com.tyland.musicbox.model.Music;
import com.tyland.musicbox.service.PlayState;
import com.tyland.musicbox.util.Log;
import com.tyland.musicbox.util.PermissionUtils;
import com.tyland.musicbox.util.Utils;

import java.util.List;

/**
 * Created by tyland on 2018/4/27.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected View viewMusicMsg;
    protected ImageView ivAlbum;
    protected TextView tvMusicTitle;
    protected TextView tvMusicArtist;
    protected Button btnPlay;
    protected Button btnPrevious;
    protected Button btnNext;
    protected Button btnQueue;
    protected Button btnBack;

    private MusicReceiver mReceiver;
    private boolean isCurrentRunningForeground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        registerReceivers();
        initViews();
    }


    private void registerReceivers() {
        mReceiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ActionConstant.ACTION_SERVICE_CONNECTED);
        filter.addAction(ActionConstant.ACTION_MUSIC_CHANGE);
        filter.addAction(ActionConstant.ACTION_QUEUE_CHANGE);
        filter.addAction(ActionConstant.ACTION_STATE_CHANGE);
        filter.addAction(ActionConstant.ACTION_RUNNING_FOREGROUND);

        registerReceiver(mReceiver, filter);
    }

    private void initViews() {
        setupView();
        if (viewMusicMsg != null) {
            viewMusicMsg.setOnClickListener(this);
        }
        if (btnPlay != null) {
            btnPlay.setOnClickListener(this);
        }
        if (btnNext != null) {
            btnNext.setOnClickListener(this);
        }
        if (btnPrevious != null) {
            btnPrevious.setOnClickListener(this);
        }
        if (btnQueue != null) {
            btnQueue.setOnClickListener(this);
        }
        if (btnBack != null) {
            btnBack.setOnClickListener(this);
        }
        updateMusic();
        updatePlayState();
    }

    protected abstract void setupView();

    protected IMusicService getService() {
        return ((MusicApplication) getApplication()).getService();
    }

    public int requestPermission(int requestCode) {
        return PermissionUtils.requestPermission(this, requestCode, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, null);
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(requestCode);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_music_message:
                Intent playingIntent = new Intent(this, PlayingActivity.class);
                startActivity(playingIntent);
                break;
            case R.id.btn_play:
                if (getService().getPlayState() == PlayState.PLAYING) {
                    getService().pause();
                } else {
                    getService().play();
                }
                break;
            case R.id.btn_previous:
                getService().previous();
                break;
            case R.id.btn_next:
                getService().next();
                break;
            case R.id.btn_queue:
                Intent intent = new Intent(this, QueueActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        isCurrentRunningForeground = Utils.isRunningForeground(getApplicationContext());
        if (isCurrentRunningForeground) {
            Log.d(getName() + "切换到前台");
            Intent intent = new Intent(ActionConstant.ACTION_RUNNING_FOREGROUND);
            sendBroadcast(intent);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        isCurrentRunningForeground = Utils.isRunningForeground(getApplicationContext());
        if (!isCurrentRunningForeground) {
            Log.d(getName() + "切换到后台");
        }
        super.onStop();
    }

    private class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(getName() + " receive:" + intent.getAction());
            refreshView(intent);
        }

    }

    protected void refreshView(Intent intent) {
        try {
            if (intent != null && (intent.getAction().equals(ActionConstant.ACTION_MUSIC_CHANGE)
                    || intent.getAction().equals(ActionConstant.ACTION_SERVICE_CONNECTED)
                    || intent.getAction().equals(ActionConstant.ACTION_QUEUE_CHANGE)))
                updateMusic();
        } catch (IndexOutOfBoundsException e) {
            setEmptyMusicView();
        }
        updatePlayState();
    }

    private void updateMusic() {
        if (getService() == null) {
            return;
        }
        Music music = getService().getCurrentMusic();
        if (music != null) {
            if (tvMusicTitle != null)
                tvMusicTitle.setText(music.getTitle());
            if (tvMusicArtist != null)
                tvMusicArtist.setText(music.getArtist());
            if (ivAlbum != null) {
                AlbumDataAccess access = new AlbumDataAccess(this);
                Album album = access.getAlbumById(music.getAlbumId());
                if (album != null)
                    loadImage(album.getAlbumArt(), ivAlbum, getDefaultAlbumId());
            }
        } else {
            setEmptyMusicView();
        }
    }

    private void updatePlayState() {
        if (getService() == null) {
            return;
        }
        if (getService().getCurrentQueueCount() <= 1) {
            if (btnNext != null)
                btnNext.setEnabled(false);
            if (btnPrevious != null)
                btnPrevious.setEnabled(false);
        } else {
            if (btnNext != null)
                btnNext.setEnabled(true);
            if (btnPrevious != null)
                btnPrevious.setEnabled(true);
        }
        if (getService().getPlayState() == PlayState.PLAYING) {
            btnPlay.setBackgroundResource(R.drawable.btn_pause);
        } else {
            btnPlay.setBackgroundResource(R.drawable.btn_play);
        }
    }

    protected void setEmptyMusicView() {
        if (tvMusicTitle != null)
            tvMusicTitle.setText(R.string.empty_music_title);
        if (tvMusicArtist != null)
            tvMusicArtist.setText("");
        if (ivAlbum != null)
            loadDrawable(getDefaultAlbumId(), ivAlbum);
        if (btnPlay != null)
            btnPlay.setBackgroundResource(R.drawable.btn_play);
    }

    protected int getDefaultAlbumId() {
        return R.drawable.icon_default_music;
    }

    public int checkSelfPermission(int requestCode) {
        return PermissionUtils.checkSelfPermission(this, requestCode);
    }

    protected void onPermissionGranted(int requestCode) {
    }

    protected void loadImage(String url, ImageView imageView, int defId) {
        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(url), imageView, Utils.getDefaultOptions(defId));
    }

    protected void loadDrawable(int resId, ImageView view) {
        ImageLoader.getInstance().displayImage("drawable://" + resId, view, Utils.getDefaultOptions(resId));
    }

    protected void addToPlayQueue(Music m) {
        int index = getService().getCurrentQueue().indexOf(m);
        if (index != -1) {
            getService().removeFromPlayQueue(index);
        }
        getService().addToPlayQueue(m);
        getService().setCurrentIndex((getService().getCurrentQueueCount() - 1));
    }

    protected String getName() {
        return getComponentName().getClassName();
    }
}
