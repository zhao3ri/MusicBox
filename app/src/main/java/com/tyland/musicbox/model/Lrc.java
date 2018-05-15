package com.tyland.musicbox.model;

import java.util.List;

/**
 * Created by tyland on 2018/5/14.
 */
public class Lrc {
    private String album;
    private String title;
    private String artist;
    private List<LrcBean> lrcBeen;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<LrcBean> getLrcBeen() {
        return lrcBeen;
    }

    public void setLrcBeen(List<LrcBean> lrcBeen) {
        this.lrcBeen = lrcBeen;
    }

    public static class LrcBean {
        private String lrc;
        private long start;
        private long end;

        public LrcBean() {
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }
    }
}
