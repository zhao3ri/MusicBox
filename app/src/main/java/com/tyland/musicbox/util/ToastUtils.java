package com.tyland.musicbox.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * toast提示工具类
 * Created by tyland on 2018/4/27.
 */
public class ToastUtils {
    public static void show(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(Context context, int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(Context context, int resId, int time) {
        Toast toast = Toast.makeText(context, resId, time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
