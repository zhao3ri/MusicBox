package com.tyland.musicbox.ui;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tyland.musicbox.ActionConstant;
import com.tyland.musicbox.R;
import com.tyland.musicbox.model.Lrc;
import com.tyland.musicbox.service.PlayState;
import com.tyland.musicbox.util.Log;
import com.tyland.musicbox.util.PermissionUtils;
import com.tyland.musicbox.util.Utils;
import com.tyland.musicbox.widget.LrcView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tyland on 2018/5/12.
 */
public class PlayingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    private TextView tvNowPosition;
    private TextView tvEndPosition;
    private SeekBar sbProgress;
    private Button btnStop;
    private LrcView mLrcView;

    private List<String> lrcPath;
    private List<Lrc> lrcs;
    private Timer timer;
    private ObjectAnimator mRotateAnimator;

    private static final String LRC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";

    @Override
    protected void setupView() {
        setContentView(R.layout.activity_playing);
        btnBack = (Button) findViewById(R.id.btn_back);
        tvMusicTitle = (TextView) findViewById(R.id.tv_music_title);
        ivAlbum = (ImageView) findViewById(R.id.iv_album_music);
        tvMusicArtist = (TextView) findViewById(R.id.tv_music_artist);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);

        tvNowPosition = (TextView) findViewById(R.id.tv_now_positon);
        tvEndPosition = (TextView) findViewById(R.id.tv_end_position);
        sbProgress = (SeekBar) findViewById(R.id.sb_music_progress);
        btnStop = (Button) findViewById(R.id.btn_stop);
        mLrcView = (LrcView) findViewById(R.id.tv_music_lyric);
        btnStop.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);

        mRotateAnimator = ObjectAnimator.ofFloat(ivAlbum, "rotation", 0.0f, 360.0f);
        mRotateAnimator.setDuration(10000);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        mRotateAnimator.setRepeatCount(-1);
        mRotateAnimator.setRepeatMode(ObjectAnimator.RESTART);

        mLrcView.setService(getService());
        mLrcView.init();
        if (checkSelfPermission(PermissionUtils.CODE_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(PermissionUtils.CODE_READ_EXTERNAL_STORAGE);
        } else {
            requestPermission(PermissionUtils.CODE_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvNowPosition.setText(Utils.timeFormatTommss(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onPermissionGranted(int requestCode) {
        Log.d("path:" + LRC_PATH);
        lrcPath = Utils.getLrcPath(LRC_PATH);
        lrcs = new ArrayList<>();
        if (lrcPath != null && !lrcPath.isEmpty()) {
            for (String path : lrcPath) {
                String lrcStr = Utils.readFile(path);
                Log.d("lrcStr:" + lrcStr);
                Lrc lrc = Utils.parseStr2Lrc(lrcStr);
                lrcs.add(lrc);
            }
            initLrc();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = sbProgress.getProgress();
        getService().setProgress(progress);
    }

    @Override
    protected void refreshView(Intent intent) {
        super.refreshView(intent);
        if (intent != null && intent.getAction().equals(ActionConstant.ACTION_MUSIC_CHANGE)) {
            initLrc();
            sbProgress.setProgress(0);
            mRotateAnimator.end();
            ivAlbum.clearAnimation();
        }
        sbProgress.setMax((int) getService().getCurrentMusic().getDuration());
        if (getService().getPlayState() == PlayState.STOP) {
            sbProgress.setProgress(0);
            tvNowPosition.setText("00:00");
            ivAlbum.clearAnimation();
        } else {
            sbProgress.setProgress(getService().getCurrentPlayPosition());
            tvNowPosition.setText(Utils.timeFormatTommss(getService().getCurrentPlayPosition()));
        }
        tvEndPosition.setText(Utils.timeFormatTommss(getService().getCurrentMusic().getDuration()));
        setTimer();
        updateAnim();
    }

    private void initLrc() {
        try {
            if (lrcs != null && !lrcs.isEmpty()) {
                boolean hasLrc = false;
                for (Lrc lrc : lrcs) {
                    if (getService().getCurrentMusic().getAlbum().equals(lrc.getAlbum()) && getService().getCurrentMusic().getTitle().equals(lrc.getTitle())) {
                        mLrcView.setLrc(lrc);
                        hasLrc = true;
                        break;
                    }
                }
                if (!hasLrc) {
                    Log.d("no lrc....");
                    mLrcView.setLrc(null);
                }
            }
        } catch (NullPointerException e) {
            mLrcView.setLrc(null);
        } finally {
            mLrcView.init();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void updateAnim() {
        if (getService().getPlayState() == PlayState.PLAYING) {
            if (mRotateAnimator.isPaused()) {
                mRotateAnimator.resume();
            } else {
                mRotateAnimator.start();
            }
        } else if (getService().getPlayState() == PlayState.PAUSE) {
            mRotateAnimator.pause();
        } else {
            mRotateAnimator.end();
        }
    }

    private void setTimer() {
        if (timer == null) {
            timer = new Timer();

            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    if (getService().getPlayState() == PlayState.PLAYING) {
                        int progress = sbProgress.getProgress();
                        progress += 500;
                        sbProgress.setProgress(progress);
                    }
                }

            };
            timer.schedule(task, 500, 500);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_stop:
                getService().stop();
                break;
            case R.id.btn_previous:
            case R.id.btn_next:
                sbProgress.setProgress(0);
                break;
        }
    }

    @Override
    protected void setEmptyMusicView() {
        super.setEmptyMusicView();
        sbProgress.setProgress(0);
        tvNowPosition.setText("00:00");
        tvEndPosition.setText("00:00");
        mRotateAnimator.end();
        ivAlbum.clearAnimation();
    }

    @Override
    protected int getDefaultAlbumId() {
        return R.drawable.img_default_record;
    }

    @Override
    public void finish() {
        super.finish();
        mRotateAnimator.end();
        ivAlbum.clearAnimation();
    }
}
