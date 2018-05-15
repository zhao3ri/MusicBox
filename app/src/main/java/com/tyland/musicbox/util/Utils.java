package com.tyland.musicbox.util;

import android.app.ActivityManager;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tyland.musicbox.R;
import com.tyland.musicbox.model.Lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tyland on 2018/4/29.
 */
public class Utils {
    /**
     * 将时间转为mm:ss格式
     */
    public static String timeFormatTommss(long time) {
//		int hour = 0;
        int minute = 0;
        int second = 0;

        second = (int) (time / 1000);

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
//            hour = minute / 60;
            minute = minute % 60;
        }
        return new DecimalFormat("00").format(minute) +
                ":" + new DecimalFormat("00").format(second);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = getDeviceDensity(context);
        return (int) (dp * scale + 0.5);
    }

    public static int px2dp(Context context, float px) {
        final float scale = getDeviceDensity(context);
        return (int) (px / scale + 0.5);
    }

    /**
     * 获取屏幕像素密度.
     */
    public static float getDeviceDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static ImageLoader getImageLoader(Context context, String path) {
        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            File cacheDir = new File(path);
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024));
            try {
                builder.diskCache(new LruDiskCache(cacheDir, new HashCodeFileNameGenerator(), 5 * 1024 * 1024));// 5Mb sd卡(本地)缓存的最大值
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageLoaderConfiguration config = builder.build();
            ImageLoader.getInstance().init(config);
        }
        return loader;
    }

    public static DisplayImageOptions getDefaultOptions(int defId) {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defId)
                .showImageOnFail(defId)
                .showImageOnLoading(defId)
                .cacheInMemory(false).cacheOnDisk(false).build();
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            //应用程序位于堆栈的顶层
            if (context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
            Log.d(String.format("Foreground App:%s", tasksInfo.get(0).topActivity.getPackageName()));
        }
        return false;
    }

    public static boolean regexMatches(String regex, String input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (m.find())
            Log.d("matcher " + m.group());
        return m.matches();
    }

    public static String readFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File file = new File(fileName);
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                fis.close();
                isr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 传入的参数为标准歌词字符串
     *
     * @param lrcStr
     * @return
     */
    public static Lrc parseStr2Lrc(String lrcStr) {
        Lrc lrc = new Lrc();
        List<Lrc.LrcBean> lrcBeen = new ArrayList<>();
        String lrcText = lrcStr.replaceAll("&#58;", ":")
                .replaceAll("&#10;", "\n")
                .replaceAll("&#46;", ".")
                .replaceAll("&#32;", " ")
                .replaceAll("&#45;", "-")
                .replaceAll("&#13;", "\r").replaceAll("&#39;", "'");
        String[] split = lrcText.split("\n");
        for (int i = 0; i < split.length; i++) {
            String lrcLine = split[i];
            if (lrcLine.contains("[ti:")) {
                String title = subLrcLine(lrcLine, "[ti:");
                lrc.setTitle(title);
            } else if (lrcLine.startsWith("[ar:")) {
                String art = subLrcLine(lrcLine, "[ar:");
                lrc.setArtist(art);
            } else if (lrcLine.startsWith("[al:")) {
                String album = subLrcLine(lrcLine, "[al:");
                lrc.setAlbum(album);
            }
            if (regexMatches("\\d{2}\\:\\d{2}\\.\\d{2}", subLrcLine(lrcLine, "["))) {
                String min = lrcLine.substring(lrcLine.indexOf("[") + 1, lrcLine.indexOf("[") + 3);
                String seconds = lrcLine.substring(lrcLine.indexOf(":") + 1, lrcLine.indexOf(":") + 3);
                String mills = lrcLine.substring(lrcLine.indexOf(".") + 1, lrcLine.indexOf(".") + 3);
                long startTime = Long.valueOf(min) * 60 * 1000 + Long.valueOf(seconds) * 1000 + Long.valueOf(mills) * 10;
                String text = lrcLine.substring(lrcLine.indexOf("]") + 1);
                Lrc.LrcBean lrcBean = new Lrc.LrcBean();
                lrcBean.setStart(startTime);
                lrcBean.setLrc(text);
                lrcBeen.add(lrcBean);
                if (lrcBeen.size() > 1) {
                    lrcBeen.get(lrcBeen.size() - 2).setEnd(startTime);
                }
                if (i == split.length - 1) {
                    lrcBeen.get(lrcBeen.size() - 1).setEnd(startTime + 100000);
                }
            }
        }
        lrc.setLrcBeen(lrcBeen);
        return lrc;
    }

    private static String subLrcLine(String line, String flag) {
        return line.substring(line.indexOf(flag) + flag.length(), line.indexOf("]"));
    }

    public static List<String> getLrcPath(String path) {
        List<String> lrcPath = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile()) {
                int idx = f.getPath().lastIndexOf(".");
                if (idx <= 0) {
                    continue;
                }
                String suffix = f.getPath().substring(idx);
                if (suffix.toLowerCase().equals(".lrc")) {
                    lrcPath.add(f.getPath());
                }
            }
        }
        return lrcPath;
    }
}
