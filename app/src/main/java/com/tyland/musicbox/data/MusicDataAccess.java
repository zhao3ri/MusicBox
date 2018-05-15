package com.tyland.musicbox.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.tyland.musicbox.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyland on 2018/4/28.
 */
public class MusicDataAccess {
    private Context mContext;

    public MusicDataAccess(Context ctx) {
        mContext = ctx;
    }

    /**
     * 获取本地音乐列表
     */
    public List<Music> getAllMusic() {
        ContentResolver cr = mContext.getContentResolver();
        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE,
                MediaStore.Audio.AudioColumns.TITLE_KEY, MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.YEAR,
                MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.BOOKMARK, MediaStore.Audio.AudioColumns.COMPOSER,
                MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ALBUM_KEY, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_KEY};
        String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + "=?";
        String[] selectionArgs = new String[]{String.valueOf(1)};
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        List<Music> musicList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Music music = new Music();
            music.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            music.setTitle(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
            music.setTitleKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE_KEY)));
            music.setDisplayName(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
            music.setTrack(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)));
            music.setYear(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)));
            music.setData(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
            music.setDuration(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
            music.setBookmark(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.BOOKMARK)));
            music.setComposer(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER)));
            music.setAlbumId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)));
            music.setAlbum(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
            music.setAlbumKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_KEY)));
            music.setArtistId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)));
            music.setArtist(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
            music.setArtistKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_KEY)));

            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    /**
     * 获取指定专辑的音乐列表
     * */
    public List<Music> getMusicByAlbumId(long albumId) {
        ContentResolver cr = mContext.getContentResolver();

        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE,
                MediaStore.Audio.AudioColumns.TITLE_KEY, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.YEAR,
                MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.BOOKMARK, MediaStore.Audio.AudioColumns.COMPOSER,
                MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_KEY, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_KEY};
        String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + "=? and " + MediaStore.Audio.AudioColumns.ALBUM_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(1),
                String.valueOf(albumId)};
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, MediaStore.Audio.AudioColumns.TRACK);

        List<Music> musicList = new ArrayList<Music>();
        while (cursor.moveToNext()) {
            Music music = new Music();
            music.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            music.setTitle(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
            music.setTitleKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE_KEY)));
            music.setDisplayName(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
            music.setTrack(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)));
            music.setYear(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)));
            music.setData(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
            music.setDuration(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
            music.setBookmark(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.BOOKMARK)));
            music.setComposer(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER)));
            music.setAlbumId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)));
            music.setAlbum(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
            music.setAlbumKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_KEY)));
            music.setArtistId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)));
            music.setArtist(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
            music.setArtistKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_KEY)));

            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    public Music getMusicByPath(String path) {
        ContentResolver cr = mContext.getContentResolver();

        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE,
                MediaStore.Audio.AudioColumns.TITLE_KEY, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.YEAR,
                MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.BOOKMARK, MediaStore.Audio.AudioColumns.COMPOSER,
                MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_KEY, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_KEY};
        String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + "=? and " + MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{String.valueOf(1), path};
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, MediaStore.Audio.AudioColumns.TRACK);

        Music music = new Music();
        while (cursor.moveToNext()) {
            music.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            music.setTitle(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
            music.setTitleKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE_KEY)));
            music.setDisplayName(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
            music.setTrack(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)));
            music.setYear(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)));
            music.setData(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
            music.setDuration(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
            music.setBookmark(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.BOOKMARK)));
            music.setComposer(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER)));
            music.setAlbumId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)));
            music.setAlbum(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
            music.setAlbumKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_KEY)));
            music.setArtistId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)));
            music.setArtist(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
            music.setArtistKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_KEY)));
        }
        cursor.close();
        return music;
    }

    public List<Music> getMusicByArtistId(long artId) {
        ContentResolver cr = mContext.getContentResolver();

        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE,
                MediaStore.Audio.AudioColumns.TITLE_KEY, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.YEAR,
                MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.BOOKMARK, MediaStore.Audio.AudioColumns.COMPOSER,
                MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_KEY, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_KEY};
        String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + "=? and " + MediaStore.Audio.AudioColumns.ARTIST_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(1),
                String.valueOf(artId)};
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, MediaStore.Audio.AudioColumns.TRACK);

        List<Music> musicList = new ArrayList<Music>();
        while (cursor.moveToNext()) {
            Music music = new Music();
            music.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            music.setTitle(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
            music.setTitleKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE_KEY)));
            music.setDisplayName(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
            music.setTrack(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)));
            music.setYear(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)));
            music.setData(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
            music.setDuration(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
            music.setBookmark(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.BOOKMARK)));
            music.setComposer(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER)));
            music.setAlbumId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)));
            music.setAlbum(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
            music.setAlbumKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_KEY)));
            music.setArtistId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)));
            music.setArtist(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
            music.setArtistKey(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_KEY)));

            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }
}
