package com.tyland.musicbox.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyland.musicbox.ActionConstant;
import com.tyland.musicbox.R;
import com.tyland.musicbox.model.Music;
import com.tyland.musicbox.service.PlayMethod;
import com.tyland.musicbox.util.Log;
import com.tyland.musicbox.util.Utils;
import com.tyland.swipemenulistview.SwipeMenu;
import com.tyland.swipemenulistview.SwipeMenuCreator;
import com.tyland.swipemenulistview.SwipeMenuItem;
import com.tyland.swipemenulistview.SwipeMenuListView;

import java.util.List;

/**
 * Created by tyland on 2018/5/7.
 */
public class QueueActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private SwipeMenuListView mListView;
    private List<Music> mMusicQueue;
    private MusicAdapter mAdapter;
    private Button btnMethod;
    private TextView tvClear;

    private static final int DEL_MENU = 0;

    @Override
    protected void setupView() {
        setContentView(R.layout.activity_music_queue);
        btnBack= (Button) findViewById(R.id.btn_back);
        viewMusicMsg = findViewById(R.id.rl_music_message);
        tvMusicTitle = (TextView) findViewById(R.id.tv_music_name);
        ivAlbum = (ImageView) findViewById(R.id.iv_album_music);
        tvMusicArtist = (TextView) findViewById(R.id.tv_music_artist);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnQueue = (Button) findViewById(R.id.btn_queue);

        mListView = (SwipeMenuListView) findViewById(R.id.lv_music_queue);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        btnMethod = (Button) findViewById(R.id.btn_method);

        tvClear.setOnClickListener(this);

        mMusicQueue = getService().getCurrentQueue();
        btnMethod.setVisibility(View.VISIBLE);
        btnMethod.setOnClickListener(this);
        initListView();
    }

    private void initListView() {
        View emptyView = findViewById(R.id.rl_empty);
        mListView.setEmptyView(emptyView);
        mListView.setOnItemClickListener(this);
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case DEL_MENU:
                        Log.d("delete" + mMusicQueue.get(position).getTitle());
                        getService().removeFromPlayQueue(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        setQueueAdapter();
    }

    private void setQueueAdapter() {
        if (mAdapter == null) {
            mAdapter = new MusicAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mListView.setAdapter(mAdapter);
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
            delItem.setBackground(getResources().getDrawable(R.drawable.btn_delete_queue));
            delItem.setWidth(getResources().getDimensionPixelOffset(R.dimen.list_swipe_item_width));
            delItem.setTitle(getString(R.string.delete_to_queue));
            delItem.setTitleSize(Utils.px2dp(getApplicationContext(), getResources().getDimensionPixelSize(R.dimen.text_default_size)));
            delItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(DEL_MENU, delItem);
        }
    };

    private class MusicAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mMusicQueue == null ? 0 : mMusicQueue.size();
        }

        @Override
        public Object getItem(int position) {
            return mMusicQueue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mMusicQueue.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.view_music_items, parent, false);
                holder = new ViewHolder();
                holder.tvMusicTitle = (TextView) convertView.findViewById(R.id.tv_music_title);
                holder.tvMusicArtist = (TextView) convertView.findViewById(R.id.tv_music_album);
                holder.tvMusicDuration = (TextView) convertView.findViewById(R.id.tv_music_duration);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Music music = mMusicQueue.get(position);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getService().playAtIndex(position);
    }

    @Override
    protected void refreshView(Intent intent) {
        super.refreshView(intent);
        int method = getService().getPlayMethod();
        switch (method) {
            case PlayMethod.REPEAT:
                btnMethod.setBackgroundResource(R.drawable.btn_method_repeat);
                break;
            case PlayMethod.SHUFFLE:
                btnMethod.setBackgroundResource(R.drawable.btn_method_shuffle);
                break;
            case PlayMethod.CURRENT:
                btnMethod.setBackgroundResource(R.drawable.btn_method_current);
                break;
            case PlayMethod.QUEUE:
                btnMethod.setBackgroundResource(R.drawable.btn_method_queue);
                break;
        }
        if (intent != null) {
            if (intent.getAction().equals(ActionConstant.ACTION_QUEUE_CHANGE)) {
                setQueueAdapter();
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_method) {
            int method = getService().getPlayMethod();
            switch (method) {
                case PlayMethod.REPEAT:
                    btnMethod.setBackgroundResource(R.drawable.btn_method_shuffle);
                    getService().setPlayMethod(PlayMethod.SHUFFLE);
                    break;
                case PlayMethod.SHUFFLE:
                    btnMethod.setBackgroundResource(R.drawable.btn_method_current);
                    getService().setPlayMethod(PlayMethod.CURRENT);
                    break;
                case PlayMethod.CURRENT:
                    btnMethod.setBackgroundResource(R.drawable.btn_method_queue);
                    getService().setPlayMethod(PlayMethod.QUEUE);
                    break;
                case PlayMethod.QUEUE:
                    btnMethod.setBackgroundResource(R.drawable.btn_method_repeat);
                    getService().setPlayMethod(PlayMethod.REPEAT);
                    break;
            }
        } else if (v.getId() == R.id.tv_clear) {
            getService().clearPlayQueue();
        }
    }
}
