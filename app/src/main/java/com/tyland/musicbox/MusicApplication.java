package com.tyland.musicbox;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tyland.musicbox.service.MusicService;

/**
 * Created by tyland on 2018/4/27.
 */
public class MusicApplication extends Application {
    private IMusicService mService;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = (IMusicService) service;
            Intent intent = new Intent(ActionConstant.ACTION_SERVICE_CONNECTED);
            sendBroadcast(intent);
        }
    };

    public IMusicService getService() {
        return mService;
    }
}
