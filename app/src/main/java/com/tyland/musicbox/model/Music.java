package com.tyland.musicbox.model;

import java.io.Serializable;

/**
 * Created by tyland on 2018/4/28.
 */
public class Music implements Serializable {
    private static final long serialVersionUID = -2680891485789456224L;

    private long id;
    private String title;
    private String titleKey;
    private String displayName;
    private String data;
    private int track;
    private int year;
    private long duration;
    private long bookmark;
    private String composer;
    private int albumId;
    private String album;
    private String albumKey;
    private int artistId;
    private String artist;
    private String artistKey;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getBookmark() {
        return bookmark;
    }

    public void setBookmark(long bookmark) {
        this.bookmark = bookmark;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistKey() {
        return artistKey;
    }

    public void setArtistKey(String artistKey) {
        this.artistKey = artistKey;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", titleKey='" + titleKey + '\'' +
                ", displayName='" + displayName + '\'' +
                ", data='" + data + '\'' +
                ", track=" + track +
                ", year=" + year +
                ", duration=" + duration +
                ", bookmark=" + bookmark +
                ", composer='" + composer + '\'' +
                ", albumId=" + albumId +
                ", album='" + album + '\'' +
                ", albumKey='" + albumKey + '\'' +
                ", artistId=" + artistId +
                ", artist='" + artist + '\'' +
                ", artistKey='" + artistKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return id == ((Music) o).id;
    }
}
