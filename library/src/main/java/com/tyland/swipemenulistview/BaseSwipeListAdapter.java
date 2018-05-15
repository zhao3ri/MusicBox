package com.tyland.swipemenulistview;

import android.widget.BaseAdapter;

/**
 * Created by tyland on 2018/4/29.
 */
public abstract class BaseSwipeListAdapter extends BaseAdapter {
    public boolean getSwipeEnableByPosition(int position) {
        return true;
    }
}
