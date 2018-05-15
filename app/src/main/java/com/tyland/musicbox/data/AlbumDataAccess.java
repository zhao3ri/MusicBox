package com.tyland.musicbox.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Artists;

import com.tyland.musicbox.model.Album;

import java.util.ArrayList;
import java.util.List;


public class AlbumDataAccess {
	private Context mContext;

	public AlbumDataAccess(Context context) {
		mContext = context;
	}

	/**
	 *获取本地专辑列表
	 */
	public List<Album> getAllAlbumList() {
		ContentResolver cr = mContext.getContentResolver();

		String[] projection = new String[] { BaseColumns._ID,
				AlbumColumns.ALBUM_KEY, AlbumColumns.ALBUM,
				AlbumColumns.ALBUM_ART, AlbumColumns.ARTIST,
				AlbumColumns.FIRST_YEAR, AlbumColumns.LAST_YEAR,
				AlbumColumns.NUMBER_OF_SONGS };
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,
				null, Albums.DEFAULT_SORT_ORDER);

		List<Album> albumList = new ArrayList<Album>();
		while (cursor.moveToNext()) {
			Album album = new Album();
			album.setId(cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID)));
			album.setAlbumKey(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_KEY)));
			album.setAlbum(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM)));
			album.setAlbumArt(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_ART)));
			album.setArtist(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ARTIST)));
			album.setFirstYear(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.FIRST_YEAR)));
			album.setLastYear(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.LAST_YEAR)));

			albumList.add(album);
		}
		cursor.close();
		return albumList;
	}

	/**
	 *根据歌手id查找专辑
	 */
	public List<Album> getAlbumListByArtist(long artistId) {
		ContentResolver cr = mContext.getContentResolver();

		String[] projection = new String[] { BaseColumns._ID,
				AlbumColumns.ALBUM_KEY, AlbumColumns.ALBUM,
				AlbumColumns.ALBUM_ART, AlbumColumns.ARTIST,
				AlbumColumns.FIRST_YEAR, AlbumColumns.LAST_YEAR,
				AlbumColumns.NUMBER_OF_SONGS };
		Cursor cursor = cr.query(
				Artists.Albums.getContentUri("external", artistId), projection,
				null, null, Albums.DEFAULT_SORT_ORDER);

		List<Album> albumList = new ArrayList<Album>();
		while (cursor.moveToNext()) {
			Album album = new Album();
			album.setId(cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID)));
			album.setAlbumKey(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_KEY)));
			album.setAlbum(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM)));
			album.setAlbumArt(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_ART)));
			album.setArtist(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ARTIST)));
			album.setFirstYear(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.FIRST_YEAR)));
			album.setLastYear(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.LAST_YEAR)));

			albumList.add(album);
		}
		cursor.close();
		return albumList;
	}

	/**
	 * 获得指定的专辑
	 * */
	public Album getAlbumById(long albumId) {
		ContentResolver cr = mContext.getContentResolver();
		Album album = null;

		String[] projection = new String[] { BaseColumns._ID,
				AlbumColumns.ALBUM_KEY, AlbumColumns.ALBUM,
				AlbumColumns.ALBUM_ART, AlbumColumns.ARTIST,
				AlbumColumns.FIRST_YEAR, AlbumColumns.LAST_YEAR,
				AlbumColumns.NUMBER_OF_SONGS };
		String selection = BaseColumns._ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(albumId) };
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection,
				selection, selectionArgs, null);

		if (cursor.moveToFirst()) {
			album = new Album();
			album.setId(cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID)));
			album.setAlbumKey(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_KEY)));
			album.setAlbum(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM)));
			album.setAlbumArt(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_ART)));
			album.setArtist(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ARTIST)));
			album.setFirstYear(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.FIRST_YEAR)));
			album.setLastYear(cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.LAST_YEAR)));
		}
		return album;
	}

	/**
	 *获得专辑的歌手名
	 */
	public String getAlbumArtById(int albumId) {
		ContentResolver cr = mContext.getContentResolver();

		String[] projection = new String[] { AlbumColumns.ALBUM_ART };
		String selection = BaseColumns._ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(albumId) };
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection,
				selection, selectionArgs, null);

		String art = "";
		if (cursor.moveToFirst()) {
			art = cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_ART));
		}
		cursor.close();
		return art;
	}

	/**
	 * 获取指定歌手的专辑列表名称
	 */
	public List<String> getAlbumArtsByArtistId(long artistId) {
		ContentResolver cr = mContext.getContentResolver();

		String[] projection = new String[] { AlbumColumns.ALBUM_ART };
		Uri uri = Artists.Albums.getContentUri("external", artistId);
		Cursor cursor = cr.query(uri, projection, null, null, null);

		List<String> arts = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String art = cursor.getString(cursor
					.getColumnIndexOrThrow(AlbumColumns.ALBUM_ART));
			arts.add(art);
		}
		cursor.close();
		return arts;
	}
}

