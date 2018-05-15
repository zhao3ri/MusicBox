package com.tyland.musicbox.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyland.musicbox.R;
import com.tyland.musicbox.model.Album;

/**
 * Created by tyland on 2018/5/10.
 */
public class AlbumDetailActivity extends BaseActivity {
    private ImageView ivAlbumImg;
    private TextView tvAlbumName;
    private TextView tvAlbumArt;

    private long albumId;
    private Album mAlbum;
    public static final String KEY_ALBUM_ID = "album_id";
    public static final String KEY_ALBUM = "album";
    private static final int DEF_ALBUM_ID = 0;

    @Override
    protected void setupView() {
        setContentView(R.layout.activity_album_detail);
        btnBack= (Button) findViewById(R.id.btn_back);
        viewMusicMsg = findViewById(R.id.rl_music_message);
        tvMusicTitle = (TextView) findViewById(R.id.tv_music_name);
        ivAlbum = (ImageView) findViewById(R.id.iv_album_music);
        tvMusicArtist = (TextView) findViewById(R.id.tv_music_artist);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnQueue = (Button) findViewById(R.id.btn_queue);
        btnQueue.setVisibility(View.VISIBLE);

        ivAlbumImg = (ImageView) findViewById(R.id.iv_album_detail_img);
        tvAlbumName = (TextView) findViewById(R.id.tv_album_detail_name);
        tvAlbumArt = (TextView) findViewById(R.id.tv_album_detail_artist);
        initView();
    }

    private void initView() {
        handleIntent();
        loadImage(mAlbum.getAlbumArt(), ivAlbumImg, R.drawable.bg_default_album);
        tvAlbumName.setText(mAlbum.getAlbum());
        tvAlbumArt.setText(mAlbum.getArtist());
        MusicFragment musicFragment=new MusicFragment();
        Bundle args=new Bundle();
        args.putLong(KEY_ALBUM_ID, albumId);
        musicFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, musicFragment)
                .commit();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        albumId = intent.getLongExtra(KEY_ALBUM_ID, DEF_ALBUM_ID);
        Bundle bundle = intent.getExtras();
        mAlbum = (Album) bundle.getSerializable(KEY_ALBUM);
    }
}
