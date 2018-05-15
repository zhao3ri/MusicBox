package com.tyland.musicbox.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.ArtistColumns;

import com.tyland.musicbox.model.Artist;

import java.util.ArrayList;
import java.util.List;


public class ArtistDataAccess {
	private Context mContext;

	public ArtistDataAccess(Context context) {
		mContext = context;
	}

	/**
	 * 读取本地媒体库中的所有艺人信息列表
	 * 
	 * @return
	 */
	public List<Artist> getArtistList() {
		ContentResolver cr = mContext.getContentResolver();

		String[] projection = new String[] { BaseColumns._ID,
				ArtistColumns.ARTIST_KEY,
				ArtistColumns.ARTIST,
				ArtistColumns.NUMBER_OF_ALBUMS,
				ArtistColumns.NUMBER_OF_TRACKS };

		Cursor cursor = cr.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
				projection, null, null,
				MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

		List<Artist> artistList = new ArrayList<Artist>();
		while (cursor.moveToNext()) {
			Artist artist = new Artist();
			artist.setId(cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID)));
			artist.setArtistKey(cursor.getString(cursor
					.getColumnIndexOrThrow(ArtistColumns.ARTIST_KEY)));
			artist.setArtist(cursor.getString(cursor
					.getColumnIndexOrThrow(ArtistColumns.ARTIST)));
			artist.setNumberOfAlbums(cursor.getInt(cursor
					.getColumnIndexOrThrow(ArtistColumns.NUMBER_OF_ALBUMS)));
			artist.setNumberOfTracks(cursor.getInt(cursor
					.getColumnIndexOrThrow(ArtistColumns.NUMBER_OF_TRACKS)));
			artistList.add(artist);
		}
		cursor.close();
		return artistList;
	}
	
}
