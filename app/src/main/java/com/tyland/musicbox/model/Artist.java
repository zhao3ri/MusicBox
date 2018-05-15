package com.tyland.musicbox.model;

import java.io.Serializable;

public class Artist implements Serializable {
	private static final long serialVersionUID = 8741284275898252172L;

	private long id;
	private String artistKey;
	private String artist;
	private int numberOfAlbums;
	private int numberOfTracks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getArtistKey() {
		return artistKey;
	}

	public void setArtistKey(String artistKey) {
		this.artistKey = artistKey;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getNumberOfAlbums() {
		return numberOfAlbums;
	}

	public void setNumberOfAlbums(int numberOfAlbums) {
		this.numberOfAlbums = numberOfAlbums;
	}

	public int getNumberOfTracks() {
		return numberOfTracks;
	}

	public void setNumberOfTracks(int numberOfTracks) {
		this.numberOfTracks = numberOfTracks;
	}

	@Override
	public String toString() {
		return "Artist{" +
				"id=" + id +
				", artistKey='" + artistKey + '\'' +
				", artist='" + artist + '\'' +
				", numberOfAlbums=" + numberOfAlbums +
				", numberOfTracks=" + numberOfTracks +
				'}';
	}
}
