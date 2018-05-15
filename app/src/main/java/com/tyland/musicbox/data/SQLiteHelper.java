package com.tyland.musicbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.MediaStore;

/**
 * Created by tyland on 2018/4/30.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Music.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Music";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            BaseColumns._ID + " integer primary key," +
            MediaStore.MediaColumns.TITLE + " varchar not null," +
            MediaStore.Audio.AudioColumns.TITLE_KEY + " varchar null," +
            MediaStore.MediaColumns.DISPLAY_NAME + " varchar null," +
            MediaStore.MediaColumns.DATA + " varchar not null," +
            MediaStore.Audio.AudioColumns.TRACK + " integer null," +
            MediaStore.Audio.AudioColumns.YEAR + " integer null," +
            MediaStore.Audio.AudioColumns.DURATION + " integer not null," +
            MediaStore.Audio.AudioColumns.BOOKMARK + " integer null," +
            MediaStore.Audio.AudioColumns.COMPOSER + " varchar null," +
            MediaStore.Audio.AudioColumns.ARTIST + " varchar not null," +
            MediaStore.Audio.AudioColumns.ARTIST_ID + " integer not null," +
            MediaStore.Audio.AudioColumns.ARTIST_KEY + " varchar null," +
            MediaStore.Audio.AudioColumns.ALBUM_ID + " integer not null," +
            MediaStore.Audio.AudioColumns.ALBUM + " varchar null," +
            MediaStore.Audio.AudioColumns.ALBUM_KEY + " varchar null" + ")";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(CREATE_TABLE);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
