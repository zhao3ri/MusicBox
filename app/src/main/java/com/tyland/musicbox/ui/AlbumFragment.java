package com.tyland.musicbox.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.tyland.musicbox.R;
import com.tyland.musicbox.data.AlbumDataAccess;
import com.tyland.musicbox.model.Album;
import com.tyland.musicbox.util.Log;
import com.tyland.musicbox.util.PermissionUtils;
import com.tyland.musicbox.util.Utils;

import java.util.List;

/**
 * Created by tyland on 2018/4/28.
 */
public class AlbumFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private List<Album> mAlbumList;
    private AlbumAdapter mAdapter;

    @Override
    public View getView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);
        mGridView = (GridView) view.findViewById(R.id.gv_album);
        checkPermission(PermissionUtils.CODE_READ_EXTERNAL_STORAGE);
        setEmptyListView(view, mGridView);
        mGridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        updateAlbumData();
        setAdapter();
    }

    @Override
    public void onPermissionDenied(int requestCode) {
        setAdapter();
    }

    private void updateAlbumData() {
        mAlbumList = new AlbumDataAccess(getContext()).getAllAlbumList();
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new AlbumAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), AlbumDetailActivity.class);
        intent.putExtra(AlbumDetailActivity.KEY_ALBUM_ID, id);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AlbumDetailActivity.KEY_ALBUM, mAlbumList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
        Log.d(mAlbumList.get(position).toString());
    }

    private class AlbumAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAlbumList == null ? 0 : mAlbumList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAlbumList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mAlbumList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.view_album_items, null);
                holder = new ViewHolder();
                holder.ivAlbumImg = (ImageView) convertView.findViewById(R.id.iv_album_img);
                holder.tvAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
                holder.tvAlbumArt = (TextView) convertView.findViewById(R.id.tv_album_artist);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Album album = mAlbumList.get(position);
            getCurrentActivity().loadImage(album.getAlbumArt(), holder.ivAlbumImg, R.drawable.bg_default_album);
            holder.tvAlbumName.setText(album.getAlbum());
            holder.tvAlbumArt.setText(album.getArtist());
            return convertView;
        }

        class ViewHolder {
            ImageView ivAlbumImg;
            TextView tvAlbumName;
            TextView tvAlbumArt;
        }
    }
}
