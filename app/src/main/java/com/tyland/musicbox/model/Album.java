package com.tyland.musicbox.model;

import java.io.Serializable;

public class Album implements Serializable {
    private static final long serialVersionUID = 8333850634965657283L;

    private long id;
    private String albumKey;
    private String album;
    private String albumArt;
    private String artist;
    private String firstYear;
    private String lastYear;
    private int numberOfSongs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(String firstYear) {
        this.firstYear = firstYear;
    }

    public String getLastYear() {
        return lastYear;
    }

    public void setLastYear(String lastYear) {
        this.lastYear = lastYear;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", albumKey='" + albumKey + '\'' +
                ", album='" + album + '\'' +
                ", albumArt='" + albumArt + '\'' +
                ", artist='" + artist + '\'' +
                ", firstYear='" + firstYear + '\'' +
                ", lastYear='" + lastYear + '\'' +
                ", numberOfSongs=" + numberOfSongs +
                '}';
    }
}
