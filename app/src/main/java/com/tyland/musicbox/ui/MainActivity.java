package com.tyland.musicbox.ui;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tyland.musicbox.R;
import com.tyland.musicbox.util.Log;
import com.tyland.musicbox.util.PermissionUtils;
import com.tyland.musicbox.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private NoScrollViewPager mViewPager;
    private RadioGroup mRdgTitle;
    private List<BaseFragment> mPageList;

    private static final int LOCAL_MUSIC = 0;
    private static final int ARTIST_LIST = 1;
    private static final int ALBUM_LIST = 2;

    @Override
    protected void setupView() {
        setContentView(R.layout.activity_main);
        initFragment();
        mViewPager = (NoScrollViewPager) findViewById(R.id.vp_music);
        viewMusicMsg = findViewById(R.id.rl_music_message);
        tvMusicTitle = (TextView) findViewById(R.id.tv_music_name);
        ivAlbum = (ImageView) findViewById(R.id.iv_album_music);
        tvMusicArtist = (TextView) findViewById(R.id.tv_music_artist);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnQueue = (Button) findViewById(R.id.btn_queue);

        btnQueue.setVisibility(View.VISIBLE);

        mViewPager.setAdapter(new MusicAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRdgTitle = (RadioGroup) findViewById(R.id.rg_title);
        initTitle();
    }

    private void initFragment() {
        if (mPageList != null)
            return;
        mPageList = new ArrayList<>();
        mPageList.add(LOCAL_MUSIC, new MusicFragment());
        mPageList.add(ARTIST_LIST, new ArtistFragment());
        mPageList.add(ALBUM_LIST, new AlbumFragment());
    }

    private void initTitle() {
        setCurrentPage(LOCAL_MUSIC);
        mRdgTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_local:
                        setCurrentPage(LOCAL_MUSIC);
                        break;
                    case R.id.rb_play:
                        setCurrentPage(ARTIST_LIST);
                        break;
                    case R.id.rb_album:
                        setCurrentPage(ALBUM_LIST);
                        break;
                }
            }
        });
    }

    private void setCurrentPage(int index) {
        ((RadioButton) mRdgTitle.getChildAt(index)).setChecked(true);
        mViewPager.setCurrentItem(index, false);
        updateTitle(index);
    }

    private void updateTitle(int index) {
        int count = mRdgTitle.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton radioButton = (RadioButton) mRdgTitle.getChildAt(i);
            radioButton.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            if (i == index)
                radioButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        }
    }

    private class MusicAdapter extends FragmentStatePagerAdapter {

        public MusicAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int arg0) {
            return mPageList.get(arg0);
        }

        @Override
        public int getCount() {
            return mPageList.size();
        }

    }

    @Override
    protected void onPermissionGranted(int requestCode) {
        switch (requestCode) {
            case PermissionUtils.CODE_RECORD_AUDIO:
                Log.d("Result Permission Grant CODE_RECORD_AUDIO");
                break;
            case PermissionUtils.CODE_GET_ACCOUNTS:
                Log.d("Result Permission Grant CODE_GET_ACCOUNTS");
                break;
            case PermissionUtils.CODE_READ_PHONE_STATE:
                Log.d("Result Permission Grant CODE_READ_PHONE_STATE");
                break;
            case PermissionUtils.CODE_CALL_PHONE:
                Log.d("Result Permission Grant CODE_CALL_PHONE");
                break;
            case PermissionUtils.CODE_CAMERA:
                Log.d("Result Permission Grant CODE_CAMERA");
                break;
            case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                Log.d("Result Permission Grant CODE_ACCESS_FINE_LOCATION");
                break;
            case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                Log.d("Result Permission Grant CODE_ACCESS_COARSE_LOCATION");
                break;
            case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                Log.d("Result Permission Grant CODE_READ_EXTERNAL_STORAGE");
                mPageList.get(LOCAL_MUSIC).onPermissionGranted(requestCode);
                mPageList.get(ARTIST_LIST).onPermissionGranted(requestCode);
                mPageList.get(ALBUM_LIST).onPermissionGranted(requestCode);
                break;
            case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                Log.d("Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE");
                break;
            default:
                break;
        }
    }
}
