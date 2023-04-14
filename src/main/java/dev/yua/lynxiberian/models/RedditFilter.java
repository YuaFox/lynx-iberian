package dev.yua.lynxiberian.models;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class RedditFilter implements Filter, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String subreddit;

    // Filters
    @Column
    private String tag;
    @Column(nullable = false)
    private boolean exactTag;
    @Column
    private String titleContains;
    @Column
    private String mediaType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isExactTag() {
        return exactTag;
    }

    public void setExactTag(boolean exactTag) {
        this.exactTag = exactTag;
    }

    public String getTitleContains() {
        return titleContains;
    }

    public void setTitleContains(String titleContains) {
        this.titleContains = titleContains;
    }

    @Override
    public boolean isOk(Media media) {
        if(media instanceof RedditMedia redditMedia){
            if(this.tag != null){
                if(redditMedia.getTag() == null) return false;
                if(this.exactTag){
                    if(! redditMedia.getTag().equalsIgnoreCase(this.tag)) return false;
                }else{
                    if(! redditMedia.getTag().toLowerCase().contains(this.tag.toLowerCase())) return false;
                }
            }
            if(this.titleContains != null){
                if(! redditMedia.getTitle().toLowerCase().contains(this.titleContains.toLowerCase())) return false;
            }
            return true;
        }
        return false;
    }
}