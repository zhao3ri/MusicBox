package com.tyland.musicbox.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyland.musicbox.R;
import com.tyland.musicbox.data.AlbumDataAccess;
import com.tyland.musicbox.data.ArtistDataAccess;
import com.tyland.musicbox.data.MusicDataAccess;
import com.tyland.musicbox.model.Album;
import com.tyland.musicbox.model.Artist;
import com.tyland.musicbox.model.Music;
import com.tyland.musicbox.util.Log;
import com.tyland.musicbox.util.PermissionUtils;
import com.tyland.musicbox.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyland on 2018/4/28.
 */
public class ArtistFragment extends BaseFragment implements ExpandableListView.OnChildClickListener {
    private ExpandableListView mArtistListView;
    private ArtistAdapter mAdapter;
    private List<Artist> artistList;
    private List<List<Music>> musicList;

    @Override
    public View getView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        mArtistListView = (ExpandableListView) view.findViewById(R.id.elv_artist);
        checkPermission(PermissionUtils.CODE_READ_EXTERNAL_STORAGE);
        setEmptyListView(view, mArtistListView);
        return view;
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        updateArtData();
        setAdapter();
    }

    @Override
    public void onPermissionDenied(int requestCode) {
        setAdapter();
    }

    private void updateArtData() {
        artistList = new ArtistDataAccess(getContext()).getArtistList();
        musicList = new ArrayList<>();
        MusicDataAccess musicAccess = new MusicDataAccess(getContext());
        for (Artist a : artistList) {
            List<Music> musics = musicAccess.getMusicByArtistId(a.getId());
            musicList.add(musics);
        }
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new ArtistAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mArtistListView.setAdapter(mAdapter);
        mArtistListView.setOnChildClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Music music = musicList.get(groupPosition).get(childPosition);
        getCurrentActivity().addToPlayQueue(music);
        getService().play(music);
        return true;
    }

    private class ArtistAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return artistList == null ? 0 : artistList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return musicList == null ? 0 : musicList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return artistList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return musicList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return artistList.get(groupPosition).getId();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return musicList.get(groupPosition).get(childPosition).getId();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
            final GroupViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.view_artist_items, null);
                holder = new GroupViewHolder();
                holder.ivArtImg = (ImageView) convertView.findViewById(R.id.iv_artist_item_img);
                holder.ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
                holder.tvArtName = (TextView) convertView.findViewById(R.id.tv_artist_item_name);
                convertView.setTag(holder);
            } else {
                holder = (GroupViewHolder) convertView.getTag();
            }
            Artist artist = artistList.get(groupPosition);
            holder.tvArtName.setText(artist.getArtist());
            if (!isExpanded) {
                holder.ivArrow.setBackgroundResource(R.drawable.icon_down_arrow);
            } else {
                holder.ivArrow.setBackgroundResource(R.drawable.icon_up_arrow);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.view_artist_item_music_items, parent, false);
                holder = new ChildViewHolder();
                holder.ivAlbumImg = (ImageView) convertView.findViewById(R.id.iv_artist_album_img);
                holder.tvMusicTitle = (TextView) convertView.findViewById(R.id.tv_music_title);
                holder.tvMusicAlbum = (TextView) convertView.findViewById(R.id.tv_music_album);
                holder.tvMusicDuration = (TextView) convertView.findViewById(R.id.tv_music_duration);
                convertView.setTag(holder);
            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }
            Music music = musicList.get(groupPosition).get(childPosition);
            holder.tvMusicTitle.setText(music.getTitle());
            holder.tvMusicAlbum.setText(String.format(getString(R.string.album_name_bracket), music.getAlbum()));
            holder.tvMusicDuration.setText(Utils.timeFormatTommss(music.getDuration()));
            AlbumDataAccess access = new AlbumDataAccess(getContext());
            Album album = access.getAlbumById(music.getAlbumId());
            getCurrentActivity().loadImage(album.getAlbumArt(), holder.ivAlbumImg, R.drawable.icon_default_music);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupViewHolder {
            ImageView ivArtImg;
            ImageView ivArrow;
            TextView tvArtName;
        }

        class ChildViewHolder {
            ImageView ivAlbumImg;
            TextView tvMusicTitle;
            TextView tvMusicAlbum;
            TextView tvMusicDuration;
        }
    }

}
