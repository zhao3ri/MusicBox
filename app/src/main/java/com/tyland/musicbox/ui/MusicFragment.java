package com.tyland.musicbox.ui;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyland.musicbox.R;
import com.tyland.musicbox.data.MusicDataAccess;
import com.tyland.musicbox.model.Music;
import com.tyland.musicbox.util.Log;
import com.tyland.musicbox.util.PermissionUtils;
import com.tyland.musicbox.util.Utils;
import com.tyland.swipemenulistview.SwipeMenu;
import com.tyland.swipemenulistview.SwipeMenuCreator;
import com.tyland.swipemenulistview.SwipeMenuItem;
import com.tyland.swipemenulistview.SwipeMenuListView;

import java.util.List;

/**
 * Created by tyland on 2018/4/28.
 */
public class MusicFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private SwipeMenuListView mListView;
    private List<Music> mMusicList;
    private MusicAdapter mAdapter;
    private static final int ADD_MENU = 0;

    @Override
    public View getView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        mListView = (SwipeMenuListView) view.findViewById(R.id.lv_music_list);

        checkPermission(PermissionUtils.CODE_READ_EXTERNAL_STORAGE);
        setEmptyListView(view, mListView);
        mListView.setOnItemClickListener(this);
//        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case ADD_MENU:
                        // open
                        Log.d("添加" + mMusicList.get(position).getTitle());
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        return view;
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        updateMusicData();
        setAdapter();
    }

    @Override
    public void onPermissionDenied(int requestCode) {
        setAdapter();
    }

    private void updateMusicData() {
        Log.d("update music data........");
        Bundle arg = getArguments();
        MusicDataAccess access = new MusicDataAccess(getContext());
        if (arg == null) {
            mMusicList = access.getAllMusic();
        } else {
            long albumId = arg.getLong(AlbumDetailActivity.KEY_ALBUM_ID);
            mMusicList = access.getMusicByAlbumId(albumId);
        }

    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new MusicAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Music music = mMusicList.get(position);
        Log.d("点击了" + music.getTitle());
        getCurrentActivity().addToPlayQueue(music);
        getService().play(music);
        Log.d(music.toString());
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem addItem = new SwipeMenuItem(getActivity());
            addItem.setBackground(getResources().getDrawable(R.drawable.btn_add_queue));
            addItem.setWidth(getResources().getDimensionPixelOffset(R.dimen.list_swipe_item_width));
            addItem.setTitle(getString(R.string.add_to_queue));
            addItem.setTitleSize(Utils.px2dp(getContext(), getResources().getDimensionPixelSize(R.dimen.text_add_swipe_menu_size)));
            addItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(ADD_MENU, addItem);
        }
    };


    private class MusicAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mMusicList == null ? 0 : mMusicList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMusicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mMusicList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.view_music_items, parent, false);
                holder = new ViewHolder();
                holder.tvMusicTitle = (TextView) convertView.findViewById(R.id.tv_music_title);
                holder.tvMusicArtist = (TextView) convertView.findViewById(R.id.tv_music_album);
                holder.tvMusicDuration = (TextView) convertView.findViewById(R.id.tv_music_duration);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Music music = mMusicList.get(position);
            holder.tvMusicTitle.setText(music.getTitle());
            holder.tvMusicArtist.setText(music.getArtist());
            holder.tvMusicDuration.setText(Utils.timeFormatTommss(music.getDuration()));
            return convertView;
        }

        private class ViewHolder {
            TextView tvMusicTitle;
            TextView tvMusicArtist;
            TextView tvMusicDuration;
        }
    }

}
