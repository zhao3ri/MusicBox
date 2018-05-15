package com.tyland.musicbox.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.tyland.musicbox.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyland on 2018/4/30.
 */
public class MusicQueueHelper {
    /**
     * 获取本地保存的音乐列表
     */
    public static List<Music> getQueueMusic(Context context) {
        SQLiteDatabase db = new SQLiteHelper(context).getReadableDatabase();
        String[] columns = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE,
                MediaStore.Audio.AudioColumns.TITLE_KEY, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.YEAR,
                MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.BOOKMARK, MediaStore.Audio.AudioColumns.COMPOSER,
                MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_KEY, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_KEY};
        List<Music> musics = new ArrayList<Music>();
        Cursor c = db.query(SQLiteHelper.TABLE_NAME, columns, null, null, null, null, null);
        while (c.moveToNext()) {
            Music music = new Music();
            music.setId(c.getLong(c.getColumnIndexOrThrow(BaseColumns._ID)));
            music.setTitle(c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
            music.setTitleKey(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE_KEY)));
            music.setDisplayName(c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
            music.setTrack(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)));
            music.setYear(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)));
            music.setData(c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
            music.setDuration(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
            music.setBookmark(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.BOOKMARK)));
            music.setComposer(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER)));
            music.setAlbumId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)));
            music.setAlbum(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
            music.setAlbumKey(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_KEY)));
            music.setArtist(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
            music.setArtistId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)));
            music.setArtistKey(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_KEY)));
            musics.add(music);
        }
        db.close();
        c.close();
        return musics;
    }

    /**
     * 添加音乐到队列
     */
    public static void addQueueMusic(Context context, Music m) {
        SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID, m.getId());
        values.put(MediaStore.MediaColumns.TITLE, m.getTitle());
        values.put(MediaStore.Audio.AudioColumns.TITLE_KEY, m.getTitleKey());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, m.getDisplayName());
        values.put(MediaStore.Audio.AudioColumns.TRACK, m.getTrack());
        values.put(MediaStore.Audio.AudioColumns.YEAR, m.getYear());
        values.put(MediaStore.MediaColumns.DATA, m.getData());
        values.put(MediaStore.Audio.AudioColumns.DURATION, m.getDuration());
        values.put(MediaStore.Audio.AudioColumns.BOOKMARK, m.getBookmark());
        values.put(MediaStore.Audio.AudioColumns.COMPOSER, m.getComposer());
        values.put(MediaStore.Audio.AudioColumns.ALBUM_ID, m.getAlbumId());
        values.put(MediaStore.Audio.AudioColumns.ALBUM, m.getAlbum());
        values.put(MediaStore.Audio.AudioColumns.ALBUM_KEY, m.getAlbumKey());
        values.put(MediaStore.Audio.AudioColumns.ARTIST, m.getArtist());
        values.put(MediaStore.Audio.AudioColumns.ARTIST_ID, m.getArtistId());
        values.put(MediaStore.Audio.AudioColumns.ARTIST_KEY, m.getArtistKey());
        db.insert(SQLiteHelper.TABLE_NAME, "", values);
        db.close();
    }

    /**
     * 将音乐从队列中删除
     */
    public static void removeQueueMusic(Context context, Music m) {
        SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase();
        String whereClause = BaseColumns._ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(m.getId())};
        db.delete(SQLiteHelper.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    /**
     * 清除播放队列
     */
    public static void clearQueueMusic(Context context, List<Music> musics) {
        SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase();
        for (Music m : musics) {
            String whereClause = BaseColumns._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(m.getId())};
            db.delete(SQLiteHelper.TABLE_NAME, whereClause, whereArgs);
        }
        db.close();
    }
}
