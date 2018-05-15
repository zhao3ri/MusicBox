package com.tyland.swipemenulistview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyland on 2018/4/29.
 */
public class SwipeMenu {
    private Context mContext;
    private List<SwipeMenuItem> mItems;
    private int mViewType;

    public SwipeMenu(Context context) {
        mContext = context;
        mItems = new ArrayList<SwipeMenuItem>();
    }

    public Context getContext() {
        return mContext;
    }

    public void addMenuItem(SwipeMenuItem item) {
        mItems.add(item);
    }

    public void addMenuItem(int location, SwipeMenuItem item) {
        mItems.add(location, item);
    }

    public void removeMenuItem(SwipeMenuItem item) {
        mItems.remove(item);
    }

    public void removeMenuItem(int location) {
        mItems.remove(location);
    }

    public void removeAllMenus() {
        mItems.clear();
    }

    public List<SwipeMenuItem> getMenuItems() {
        return mItems;
    }

    public SwipeMenuItem getMenuItem(int index) {
        return mItems.get(index);
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }
}
