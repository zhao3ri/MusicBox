package com.tyland.musicbox.ui;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tyland.musicbox.IMusicService;
import com.tyland.musicbox.MusicApplication;
import com.tyland.musicbox.R;
import com.tyland.musicbox.util.PermissionUtils;

/**
 * Created by tyland on 2018/4/28.
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getView(inflater, container, savedInstanceState);
    }

    public abstract View getView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected IMusicService getService() {
        return ((MusicApplication) getActivity().getApplication()).getService();
    }

    protected void setEmptyListView(View parentView, AbsListView listView) {
        View emptyView = parentView.findViewById(R.id.rl_empty);
        if (emptyView == null) {
            emptyView = View.inflate(getContext(), R.layout.view_empty_list, null);
            emptyView.setVisibility(View.GONE);
            ((ViewGroup) listView.getParent()).addView(emptyView);
        }
        listView.setEmptyView(emptyView);

    }

    protected void checkPermission(int requestCode){
        if (checkSelfPermission(requestCode) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(requestCode);
        }else{
            requestPermission(requestCode);
            onPermissionDenied(requestCode);
        }
    }

    private int checkSelfPermission(int requestCode) {
        return getCurrentActivity().checkSelfPermission(requestCode);
    }

    protected int requestPermission(int requestCode) {
        return getCurrentActivity().requestPermission(requestCode);
    }

    public void onPermissionGranted(int requestCode) {
    }

    public void onPermissionDenied(int requestCode){
    }

    protected BaseActivity getCurrentActivity() {
        return (BaseActivity) getActivity();
    }
}
